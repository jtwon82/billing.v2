package com.adgather.user.inclinations.cookieval.inct.ctr;

//import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.util.PropertyHandler;

public class InctHuCtr extends CommonCtr {
	private static final Logger logger = Logger.getLogger(InctHuCtr.class);
	
	public static int getEarnerMinVisit() {
		return PropertyHandler.getInt("HU_VISIT_COUNT", 3);
	}
	
	public static int getFrontCutCnt() {
		return PropertyHandler.getInt("HU_REMOVE_COUNT", 3);
	}
	
	/**
	 * visit domain 처리 
	public static List<InctHu> getEarnerVisitDomain(List<InctHu> inctHuList) {
		if(inctHuList == null)	return null;
		
		List<InctHu> newList = new ArrayList<InctHu>();
		for(InctHu inctHu : inctHuList) {
			if(inctHu.getVisitCnt() < getEarnerMinVisit()) continue;
			
			newList.add(inctHu);
		}
		return newList;
	}
	*/
	
	/** HU 정렬(HU는 방문횟수에 따라 정렬된다. 정렬된 순서에 따라 타겟팅 우선순위가 적용된다.) **/
	public static <E extends InctHu> void sort(List<E> list) {
		if(CollectionUtils.isEmpty(list))	return;
		
		//비교자(방문횟수)
		Comparator<E> comparator = new Comparator<E>() {
			@Override
			public int compare(E obj1, E obj2) {
				return Integer.compare(obj1.getVisitCnt(),  obj2.getVisitCnt()) * -1;
			}
		};
		
		Collections.sort(list, comparator);
	}

	
	/** ADD or Mod에 정렬상태에 따라 적용 필요 B**************************************************/
	/** 현처리 방법 
	 * 	1. 정렬: 방문순 정렬(같은 방문횟수는 최근방문순 정렬)
	 *  2. 변경: 추가 대상이 있는 경우 방문횟수 증가(순서 재정렬 필요)
	 *  3. 추가: 추가 대상이 없을 경우 방문횟수를 1로 설정후 추가
	 *  4. 최대개수가 넘을 경우 : 유지할 최소 방문횟수 이하(9번째 이후, 유지도메인 방문횟수 이하)는 제거, 그리고 추가대상 마지막에 추가
	 * */
	
	// [신규 쿠키에 적용] 방문횟수를 무한대로 적용할 수 없다. 일정 기간 방문횟수를 절감해 줄 필요가 있다.(방문횟수 보다 가중치가 적절하다.)
	
	/** 정렬이 반영된 추가 **/
	public static <E extends InctHu> boolean sortAdd(List<E> list, E obj, int maxCnt, int earnerMinVisit, int frontCutCnt) {
		if(list == null) 	return false;		// 리스트가 비여 있을 수 있다. 그러나 null이면 안된다.
		if(StringUtils.isEmpty(obj.getDomain()))	return false;
		
		boolean bRes = sortMod(list, obj);
		if(bRes)		return true;
		
		int removeFrontCnt = 0;
		int removeBackCnt = 0;  
		if(list.size() >= maxCnt && earnerMinVisit > 0) {
			// 최대 개수 보다 넘을 경우 일정 개수를 삭제하고 추가한다.
			// 조건1. 유지도메인 최소 방문 횟수 적용(ex: 3회 이하는 삭제 후 신규로 대체)
			if(list.get(maxCnt - 2).getVisitCnt() >= earnerMinVisit) {	// 마지막 2번째 것이 최소 방문수보다 작을 경우 (앞에 3개 삭제)
				removeFrontCnt = frontCutCnt;
			} else {													// 그렇지 않을 경우(뒤에 1개 삭제)
				removeBackCnt = 1;		
			}
			
			for(int idx = list.size() - 1; idx >= 0; idx--) {
				if(removeBackCnt > 0) {
					list.remove(idx);
					removeBackCnt--;
				} else if (removeFrontCnt > 0 && idx < removeFrontCnt) {
					list.remove(idx);
					removeFrontCnt--;
				}
			}
		}
		
		int addPosition = list.size();
		for(int idx = list.size() - 1; idx >= 0; idx--) {
			if(list.get(idx).getVisitCnt() > obj.getVisitCnt()) {
				addPosition = idx + 1;
				break;
			}
			if(idx == 0) {
				addPosition = 0;
			}
		}
		
		list.add(addPosition, obj);
		
		return true;
	}
	
	/** 정렬이 반영된 변경 **/
	public static <E extends InctHu> boolean sortMod(List<E> list, E obj) {
		if(CollectionUtils.isEmpty(list)) 	return false;
		if(StringUtils.isEmpty(obj.getDomain()))	return false;
		
		boolean bRes = false;
		E tObj = null;
		int beforePosition = 0;
		for(int idx = 0; idx < list.size(); idx++) {
			if(equalDomain(obj, list.get(idx))) {
				tObj = list.get(idx);
				
				logger.debug("sortMod bfore tObj[" + tObj.getDomain() + "] visitCnt:[" + tObj.getVisitCnt() + "]");
				
				tObj.modValue(obj, true);
				beforePosition = idx;
				bRes = true;
				break;
			}
		}
		if(tObj == null) return false;
		
		list.remove(tObj);
		
		int addPosition = 0; 			// 최후 추가 위치
		for(int idx = beforePosition - 1; idx >= 0; idx--) {
			if(list.get(idx).getVisitCnt() > tObj.getVisitCnt()) {
				addPosition = idx + 1;
				break;
			}
		}
		logger.debug("sortMod after tObj[" + tObj.getDomain() + "] visitCnt:[" + tObj.getVisitCnt() + "]");
		list.add(addPosition, tObj);
		
		return bRes;
	}
	
	private static <E extends InctHu> boolean equalDomain(E obj1, E obj2) {
		if(obj1 == null || StringUtils.isEmpty(obj1.getDomain()))	return false;
		if(obj2 == null || StringUtils.isEmpty(obj2.getDomain()))	return false;

		return obj1.getDomain().equals(obj2.getDomain());
	}
	
	private InctHuCtr() {}
	
	/** 헤비유저 상품별 프리퀀시 종료 여부적용 
	public static InctHu setEndBanner(InctHu inct, boolean bEnd) {
		if(inct == null) {
			return null;
		}
		
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(InctHu.KEY_END_BANNER, bEnd);
		inct.setModEndList(map);
		return inct;
	}
	
	public static InctHu setEndIco(InctHu inct, boolean bEnd) {
		if(inct == null) {
			return null;
		}
		
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(InctHu.KEY_END_ICO, bEnd);
		inct.setModEndList(map);
		return inct;
	}
	
	public static InctHu setEndSky(InctHu inct, boolean bEnd) {
		if(inct == null) {
			return null;
		}
		
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(InctHu.KEY_END_SKY, bEnd);
		inct.setModEndList(map);
		return inct;
	}**/
	
	
}
