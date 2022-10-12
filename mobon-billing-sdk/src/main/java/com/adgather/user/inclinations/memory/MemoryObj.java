package com.adgather.user.inclinations.memory;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.inter.RefactCookieList;
import com.adgather.user.inclinations.cookieval.inter.Waitable;

import com.adgather.util.ErrorLog;




/**
 * 초기 데이터 기억 객체(cookie/mongo 데이터 관리)
 * @date 2017. 6. 20.
 * @param @param <T>
 * @exception
 * @see
*/
public class MemoryObj<E> {
	private static final Logger logger = Logger.getLogger(MemoryObj.class);
	
	/*
	 * 데이터 처리 순서
	 *  1. cookie beginning 데이터 설정
	 *  2. mongo beginning 데이터 설정
	 *  3. merge
	 *  4. merge를 이용 mongo/cookie 데이터 생성 (service 가능 데이터)
	 *  5. merged mongo 데이터를 mongo bignning/current 에 설정
	 *  6. merged cookie 데이터를 cookie bignning/current에 설정
	 *  7. 서비스 로직에 cookie bignning 데이터 이용
	 *  8. 서비스 로직으로 인한 변경사항 current 데이터에 반영(mongo/cookie 동일 반영)
	 *  9. 기타 서비스 로직을 모든 테움
	 *  10. mongo current 데이터 변경 사항 확인
	 *  11. mongo current 데이터 mongo에 반영(mongo 수집데이터 모두 추합, ex) shopLog, ic_um....)
	 *  12. cookie current 데이터 변경 사항 확인
	 *  13. cookie current 데이터 cookie에 반영(수집데이터 모두중 변경된 것만 cookie에 반영)
	 * 
	 * */
	private CookieDef cookieDef;	/** 쿠키정의 **/
	
	private int cookieMaxCnt;		/** 쿠키에서 원자 최대개수 **/
	private int cookieMaxLen;		/** 쿠키값의 최대 길이 **/
	private int mongoMaxCnt;		/** 몽고에서 원자 최대개수 **/
	private int mongoMaxLen;		/** 몽고값의 최대 길이 **/
	
	private SyncList<E> staticData;	/** 이전 상태 확인지 **/
	private SyncList<E> stageData;	/** 서비스 변경 상태 적용지 **/		// mongo 데이터 (1차 mongo 데이터, 2차 merged mongo 데이터, 3차 modify mongo 데이터)
	
	protected int savedMongoCnt = -1;				// 초기값  -1(값 설정되지 않은 상태, 0은 0개인 상태)
	
	/** ab테스트를 위한 생성자 **/ // yhlime 20171017 hu/um 쿠키 개수 ab테스트로 임시 사용
	public MemoryObj(final CookieDef cookieDef) {
		if(cookieDef == null)		return;		// SimulatedMemoryObject인 경우
		cookieDef.infuseLimit(this);
		this.cookieDef = cookieDef;
	}
	
	
	public CookieDef getCookieDef() {
		return cookieDef;
	}
	
	public void setCookieMaxCnt(int cookieMaxCnt) {
		this.cookieMaxCnt = cookieMaxCnt;
	}
	public int getCookieMaxCnt() {
		return this.cookieMaxCnt;
	}
	public void setCookieMaxLen(int cookieMaxLen) {
		this.cookieMaxLen = cookieMaxLen;
	}
	public void setMongoMaxCnt(int mongoMaxCnt) {
		this.mongoMaxCnt = mongoMaxCnt;
	}
	public int getMongoMaxCnt() {
		return mongoMaxCnt;
	}
	public void setMongoMaxLen(int mongoMaxLen) {
		this.mongoMaxLen = mongoMaxLen;
	}

	public int getSavedMongoCnt() {
		return savedMongoCnt;
	}
	
	public SyncList<E> getCookieData() {
		return staticData;
	}
	
	// 쿠키 데이터 적용(기능 확인을 위한 메소드 명칭 분리)
	public void setCookieData(SyncList<E> cookieData) {
		this.staticData = cookieData;
		if (cookieData == null) {
			this.stageData = null;
		} else {
			this.stageData = cookieData.clone();			// 변경상태 반영 데이터 복사
		}
		//setStatus(this.staticData);
	}
	
	// 몽고 데이터 적용(기능 확인을 위한 메소드 명칭 분리)	
	public void setMongoData(SyncList<E> mergeData) {
		this.staticData = mergeData;
		if (mergeData == null) {
			this.stageData = null;
		} else {
			this.stageData = mergeData.clone();			// 변경상태 반영 데이터 복사
		}
		
		//setStatus(this.staticData);
	 }
	
	public SyncList<E> getServiceData() {
		// 서비스 데이터는 wait 데이터 제외
		return _getCloneData(staticData, cookieMaxCnt, true);
	}
	
	public SyncList<E> getServiceData(int maxCnt) {
		// 서비스 데이터는 wait 데이터 제외
		return _getCloneData(staticData, maxCnt, true);
	}
	
	public String getSaveCookieData() {
		
		// 쿠키 데이터 wait 데이터 제외
		SyncList<E> obj = _getCloneData(stageData, cookieMaxCnt, true);
		if(obj == null)		return null;
		
		String str = null;
		try {
			str = obj.getCookieValue(cookieMaxLen, cookieDef);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		return str;		// null일 경우 공백처리
	}
	
	public Map<String, String> getSaveRefactCookieData() {
		if(!(cookieDef instanceof RefactCookieDef))	return null;
		
		// 쿠키 데이터 wait 데이터 제외
		SyncList<E> obj = _getCloneData(stageData, cookieMaxCnt, true);
		if(obj == null)		return null;
		
		Map<String, String> map = null;
		try {
			map = ((RefactCookieList)obj).getRefactCookieValue(cookieMaxLen, (RefactCookieDef)cookieDef);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		return map;
	}
	
	public Object getSaveMongoData() {
		savedMongoCnt = 0;					// 몽고 저장된 값 0 설정 
		
		// 몽고 데이터 wait 데이터 포함
		SyncList<E> obj = _getCloneData(stageData, mongoMaxCnt, false);
		if(obj == null)		return null;
		
		savedMongoCnt = obj.size();			// 몽고 저장된 개수 설정
		
		Object resObj = null;
		try {
			resObj = obj.getMongoValue();
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		return resObj;
	}
	
	public Map<String, Object> getSaveRefactMongoData() {
		if(!(cookieDef instanceof RefactCookieDef))	return null;
		
		// 몽고 데이터 wait 데이터 포함
		SyncList<E> obj = _getCloneData(stageData, mongoMaxCnt, false);
		if(obj == null)		return null;
		
		Map<String, Object> map = null;
		try {
			map = ((RefactCookieList)obj).getRefactMongoValue((RefactCookieDef)cookieDef);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		return map;
	}
	
	public SyncList<E> getSaveSyncList() {
		// 저장 데이터 wait 데이터 포함
		return _getCloneData(stageData, -1, false);
	}
	
	public SyncList<E> getSaveSyncList(int maxCnt) {
		// 저장 데이터 wait 데이터 포함
		return _getCloneData(stageData, maxCnt, false);
	}
	
	/** 데이터 복제 (maxsize(-1;무제한, 0;Clear)) **/
	private SyncList<E> _getCloneData(final SyncList<E> srcData, final int maxSize, boolean bWaitFilter) {
		if(srcData == null)		return null;
		
		SyncList<E> copyData = srcData.clone();
		if(copyData == null)			return null;
		
		// 복제시 제약사항 처리(최대개수/waitFilter/clearWaitTime)
		for(int idx = copyData.size() - 1; idx >= 0; idx--) {
			if(idx >= maxSize && maxSize > -1) {
				// 최대개수 초과이면 삭제
				copyData.remove(idx);
				continue;
			}
			E obj = copyData.get(idx);
			if(bWaitFilter && Waitable.isWaitData(obj, copyData.getCurTime())) {
				// wait필터 필요 여부에 따라  데이터 제거
				copyData.remove(idx);
			} else {
				// waitTime값 0으로 설정해서 전달
				Waitable.clearWaitData(obj, copyData.getCurTime());
			}
		}
		

		if(!StringUtils.equals(staticData.getClass().getName(), stageData.getClass().getName())) {
			logger.error("SyncList Clone Fail.[" + staticData.getClass().getName() + "=>" + stageData.getClass().getName() + "]", null);
		}
		
		return copyData;
	}

	public void applyAdd(Object element, boolean bAppendValue) {
		stageData.applyAdd(element, bAppendValue, Math.max(this.cookieMaxCnt, this.mongoMaxCnt));
	}
	public void applyMod(Object element, boolean bAppendValue) {
		stageData.applyMod(element, bAppendValue);
	}
	public void applyDel(Object element) {
		stageData.applyDel(element);
	}

	public boolean isModifedCookie() {
		// 변경대상의 쿠키 변경여부
		return this.stageData.isNeedUpdateCookie();
	}
	public boolean isModifedMongo() {
		// 변경대상의 몽고 변경 여부
		return this.stageData.isNeedUpdateMongo();
	}

	public void setNeedUpdateAll() {
		if(this.stageData == null)		return;
		
		this.stageData.setNeedUpdateCookie(true);
		this.stageData.setNeedUpdateMongo(true);
	}
	
	public void setSyncTime(long syncTime) {
		stageData.setSyncTime(syncTime);
	}
	public long getLastUpdTime() {
		return (staticData == null) ? 0 : staticData.getSyncTime();
	}
}
	
	
