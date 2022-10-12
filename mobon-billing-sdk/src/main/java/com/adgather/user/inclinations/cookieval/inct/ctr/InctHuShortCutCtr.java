package com.adgather.user.inclinations.cookieval.inct.ctr;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookieval.inct.InctHuShortCut;
import com.adgather.util.PropertyHandler;

public class InctHuShortCutCtr {
	private static final Logger logger = Logger.getLogger(InctHuShortCutCtr.class);
	
	public static int getMongoMaxCnt() {
		return PropertyHandler.getInt("MONGO_INCT_HU_COUNT", 3);
	}
	
	/** ADD or Mod에 정렬상태에 따라 적용 필요 B**************************************************/
	/** 현처리 방법 
	 * 	1. 정렬: 최근바콘설치순
	 *  2. 변경: 최신순
	 *  3. 최대개수가 넘을 경우 : 최근순임으로 가장 뒤 도메인 삭제
	 * */
	
	/** 정렬이 반영된 추가 **/
	public static <E extends InctHuShortCut> boolean sortAdd(List<E> list, E obj, int maxCnt) {
		if(list == null) 	return false;		// 리스트가 비여 있을 수 있다. 그러나 null이면 안된다.
		if(StringUtils.isEmpty(obj.getDomain()))	return false;
		
		boolean bRes = sortMod(list, obj);
		if(bRes)		return true;
		
		if(list.size() >= maxCnt) {
			// 최대 개수 보다 넘을 경우 가장 뒤 도메인을 삭제하고 추가한다.
			// 조건1. 최신 컨버전 도메인이 앞으로 세팅함으로 가장 뒤의 도메인을 삭제
			int idx = list.size() - 1;
			list.remove(idx);
		}
		list.add(0, obj);
		
		return true;
	}
	
	/** 정렬이 반영된 변경 **/
	public static <E extends InctHuShortCut> boolean sortMod(List<E> list, E obj) {
		if(CollectionUtils.isEmpty(list)) 	return false;
		if(StringUtils.isEmpty(obj.getDomain()))	return false;
		
		boolean bRes = false;
		E tObj = null;
		int beforePosition = 0;
		for(int idx = 0; idx < list.size(); idx++) {
			if(equalDomain(obj, list.get(idx))) {
				tObj = list.get(idx);
				logger.debug("sortMod bfore tObj[" + tObj.getDomain() + "]");
				tObj.modValue(obj, true);
				bRes = true;
				break;
			}
		}
		if(tObj == null) return false;
		
		list.remove(tObj);

		//바콘 HU는 최근 설치된 도메인을 우선 순위로 둔다 
		logger.debug("sortMod after tObj[" + tObj.getDomain() + "]");
		list.add(0, tObj);
		
		return bRes;
	}
	
	private static <E extends InctHuShortCut> boolean equalDomain(E obj1, E obj2) {
		if(obj1 == null || StringUtils.isEmpty(obj1.getDomain()))	return false;
		if(obj2 == null || StringUtils.isEmpty(obj2.getDomain()))	return false;

		return obj1.getDomain().equals(obj2.getDomain());
	}
	
	/** ADD or Mod에 정렬상태에 따라 적용 필요 *************************************************E*/
	
	private InctHuShortCutCtr() {}
}
