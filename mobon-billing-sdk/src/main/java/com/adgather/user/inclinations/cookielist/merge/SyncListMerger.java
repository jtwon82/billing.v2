package com.adgather.user.inclinations.cookielist.merge;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.user.inclinations.cookieval.inter.Waitable;
import com.google.common.collect.Sets;

/**
 * 동기화리스트 데이터 취합기
 * @author yhlim
 *
 */
public final class SyncListMerger {
	private SyncListMerger() {}
	
	public static <E> SyncList<E> append(CookieDef cookieDef, SyncList<E> formerObj, SyncList<E> latterObj, long curTime) {
		if(formerObj == null && latterObj == null)		return null;
		if(formerObj == null){																									// cookie데이터가 없을 경우 mongo 데이터 대체 (쿠키 저장 필요)
			// 쿠키와 몽고 데이터 변경은 이전 설정을 유지한다.
			return latterObj;
		}
		
		if(latterObj == null) {																								// mongo데이터가 없을 경우 cookie 데이터 대체 (몽고 저장 필요)
			// 다른 대상이 적용되는 경우 쿠키만 적용한다.
			formerObj.setNeedUpdateCookie(true);			
			return formerObj;
		}
		
		SyncList<E> sumData = null;
		try {
			sumData = SyncListFactory.create(cookieDef, curTime);										// 새로 생성하는 데이터(syncTime 현재 것으로 설정)
		} catch (Exception e) {}
		if(sumData == null) {
			return null;
		}
		
		Map<String, Integer> formerIdxMap = formerObj.getIdxMap();							// former 인덱스 정보(A)
		Map<String, Integer> latterIdxMap = latterObj.getIdxMap();							// latter 인덱스 정보(B)
		Set<String> formerKeys = formerIdxMap.keySet();									// former 키정보
		Set<String> latterKeys = latterIdxMap.keySet();										// latter 키정보
		Set<String> diffKeys = Sets.difference(latterKeys, formerKeys);
		
		sumData.addAll(formerObj);
		//sumData.addAll(latterObj);
		if(diffKeys != null) {
			for(String key : diffKeys) {
				Integer idx = latterIdxMap.get(key);
				if(idx == null)	continue;
				
				E obj = latterObj.get(idx);
				if(obj == null) continue;
				sumData.add(obj);
			}
		}
		sumData.setNeedUpdateAll(true);
		return sumData;
	}

	// merge cookie data and mongo data (return size mongo data)
	/**
	 * 취합(cookie값과 mongo의 값 취함)
	 * - syncTime 으로 former/latter 구분
	 * - former와 latter의 교집합
	 * - 교집합의 마지막위치로 A/B 부분 구분
	 * - formerA와 latterA의 여집합 C
	 * - A+B-C처리
	 * @method merge
	 * @see
	 * @param cObj
	 * @param mObj
	 * @param maxCnt
	 * @return T
	 */
	public static <E> SyncList<E> merge(CookieDef cookieDef, SyncList<E> cObj, SyncList<E> mObj, long curTime) {
		if(cObj == null && mObj == null)		return null;
		if(cObj == null){																									// cookie데이터가 없을 경우 mongo 데이터 대체 (쿠키 저장 필요)
			mObj.setNeedUpdateCookie(true);
			mObj.setNeedUpdateMongo(false);
			return mObj;
		}
		
		if(mObj == null) {																								// mongo데이터가 없을 경우 cookie 데이터 대체 (몽고 저장 필요)
			cObj.setNeedUpdateCookie(false);			
			cObj.setNeedUpdateMongo(true);			
			return cObj;
		}
		
		if(cObj.getSyncTime() == mObj.getSyncTime()){
			mObj.setNeedUpdateAll(false);	
			return mObj;									// cookie데이터와 mongo데이터가 같은 경우 (저장 불필요)
		}

		if(cookieDef.isFixePose()) {																									// 덮어 씌는 방식인 경우 변경 시간으로 판단
			if(cObj.getSyncTime() > mObj.getSyncTime()) {
				cObj.setNeedUpdateCookie(false);
				cObj.setNeedUpdateMongo(true);
				return cObj;
			} else {
				mObj.setNeedUpdateCookie(true);
				mObj.setNeedUpdateMongo(false);
				return mObj;
			}
		}
		
																																// cookie데이터와 mongo데이터가 변경된 경우 (쿠키/몽고 둘다 저장 필요)
		SyncList<E> former = null;																					// former/latter 선택
		SyncList<E> latter = null;
		if(cObj.getSyncTime() > mObj.getSyncTime()) {
			former = cObj;
			latter = mObj;
		} else {
			former = mObj;
			latter = cObj;
		}
		
		Map<String, Integer> formerIdxMap = former.getIdxMap();						// former 인덱스 정보(A)
		Map<String, Integer> latterIdxMap = latter.getIdxMap();							// latter 인덱스 정보(B)
		
		Set<String> formerKeys = formerIdxMap.keySet();									// former 키정보
		Set<String> latterKeys = latterIdxMap.keySet();										// latter 키정보
		Set<String> intersectionKeys = Sets.intersection(formerKeys, latterKeys);	// former와 latter의 키 교집합
		
		Set<String> formerAKeys = _getAPartKeys(formerIdxMap, intersectionKeys);// former 앞부분 키 조회
		Set<String> latterAKeys = _getAPartKeys(latterIdxMap, intersectionKeys);	// latter 앞부분 키 조회
		
		Set<String> diffKeys = Sets.difference(latterAKeys, formerAKeys);				// former를 이용한 latter의 차집합(D)
		Set<String> mergeKeys = Sets.union(formerKeys, latterKeys);					// 최종 취합 키값들 (A + B - D)
		mergeKeys = Sets.difference(mergeKeys, diffKeys);
		Set<String> selectKeys = new HashSet<String>(mergeKeys);						// 선택키(제거하며 선택키 이용) 
		
		SyncList<E> mergeData = null;
		try {
			mergeData = SyncListFactory.create(cookieDef, curTime);					// 취합 데이터 (syncTime을 현재것으로 설정)
		} catch (Exception e) {}
		if(mergeData == null) {
			return null;
		}
		
		int formerIdx = 0;
		int latterIdx = 0;

		final int formerSize = former == null ? 0 : former.size();
		final int latterSize = latter == null ? 0 : latter.size();
		while(formerIdx < formerSize || latterIdx < latterSize) {								// 전순위 취합(중복제거)
			if(latterIdx >= latterSize) {					// latter가 없을 경우 former추가
				_addMergeData(former.get(formerIdx), selectKeys, mergeData);
				formerIdx++;
			
			} else if (formerIdx >= formerSize) {		// former가 없을 경우 latter추가
				_addMergeData(latter.get(latterIdx), selectKeys, mergeData);
				latterIdx++;
				
			} else {												// former와 latter가 있는 경우 
				boolean bSelectedLatterOne = false;					// 선택된 latter인지 확인
				E latterOne = null; 											// latter에 추가 대상 선택(selectedKeys에 없는 latter는 삭제 대상) 
				do {
					latterOne = latter.get(latterIdx);
					latterIdx++;
				} while( (bSelectedLatterOne = _isContainSelectedKey(latterOne, selectKeys)) && latterIdx < latterSize);
				
				if(!bSelectedLatterOne) {										// latterOne가 선택되지 않았을 경우 former만 모두 추가
					continue;
				}
				
				E formerOne = null;								
				do {																	// latterOne가 나올 때 까지 추가(같은것 이전)
					formerOne = _addMergeData(former.get(formerIdx), selectKeys, mergeData);
					formerIdx++;
				} while( !_equalsFreq(formerOne, latterOne) && formerIdx < formerSize);
				
				// latterOne에 waitTime적용 
				Waitable.setWaitData(formerOne, latterOne);
				_addMergeData(latterOne, selectKeys, mergeData);	// latterOne 추가(같은 것 추가)
			}
		}
		if(mergeData.size() == 0) {																// 취합된것이 없을 경우 null
			return null;
		}
		
		mergeData.setNeedUpdateAll(true);										// cookie/mongo 모두 변경된 상태로 추후 둘다 저장
		
		return mergeData;
	}

	private static <E> E _addMergeData(E src, Set<String> selectedKeys, List<E> descList) {
		boolean bSelectedKey = false;
		if (src instanceof CookieVal) {
			bSelectedKey = selectedKeys.remove(((CookieVal)src).getKey());
		} else {
			bSelectedKey = selectedKeys.remove(String.valueOf(src));
		}
		
		if (bSelectedKey) {
			descList.add(src);
		}
		
		return src;
	}
	
	private static <E> boolean _isContainSelectedKey(E src, Set<String> selectedKeys) {
		return (src instanceof CookieVal) ? selectedKeys.contains(((CookieVal)src).getKey()) : selectedKeys.contains(String.valueOf(src));
	}
	
	private static <E> boolean _equalsFreq(E obj1, E obj2) {
		if (obj1 instanceof CookieVal) {
			CookieVal cValObj1 = (CookieVal) obj1;
			CookieVal cValObj2 = (CookieVal) obj2;
			return cValObj1.getKey().equals(cValObj2.getKey());
			
		} else {
		
			return obj1.equals(obj2);
		}
	}
	
	private static int _getDivisionPosition(Map<String, Integer> srcIdxMap, Set<String> intersectionKeys) {
		int divisionPosition = -1;	
		for (Map.Entry<String, Integer> entry : srcIdxMap.entrySet()) {
			if (intersectionKeys.contains(entry.getKey())) {
				divisionPosition = Math.max(divisionPosition, entry.getValue());
			}
		}
		
		return divisionPosition;
	}
	
	private static Set<String> _getAPartKeys(Map<String, Integer> srcIdxMap, Set<String> intersectionKeys) {
		Integer disitionPosition = _getDivisionPosition(srcIdxMap, intersectionKeys);
		
		Set<String> aPartKeySet = new HashSet<String>(); 
		for (Map.Entry<String, Integer> entry : srcIdxMap.entrySet()) {
			if (entry.getValue() <= disitionPosition) {
				aPartKeySet.add(entry.getKey());
			}
		}
		return aPartKeySet;
	}
	
}
