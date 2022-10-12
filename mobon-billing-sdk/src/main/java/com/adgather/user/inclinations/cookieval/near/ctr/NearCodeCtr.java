package com.adgather.user.inclinations.cookieval.near.ctr;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookieval.inct.ctr.CommonCtr;
import com.adgather.user.inclinations.cookieval.near.NearCode;

public class NearCodeCtr extends CommonCtr {
	private static final Logger logger = Logger.getLogger(NearCodeCtr.class);
	
	
	/** 현처리 방법 
	 * 	1. 최신 지역코드 정렬
	 * */
	
	
	/** 정렬이 반영된 추가 **/
	public static <E extends NearCode> boolean sortAdd(List<E> list, E obj, int maxCnt, int earnerMinVisit, int frontCutCnt) {
		if(list == null) 	return false;		// 리스트가 비여 있을 수 있다. 그러나 null이면 안된다.
		if(StringUtils.isEmpty(obj.getNearCode()))	return false;
		
		boolean bRes = sortMod(list, obj);
		if(bRes)		return true;
		
		if(list.size() >= maxCnt) {
			// 최대 개수 보다 넘을 경우 가장 뒤 지역코드를 삭제하고 추가한다.
			// 조건1. 최신 지역코드가 앞으로 세팅함으로 가장 뒤의 지역코드를 삭제
			int idx = list.size() - 1;
			list.remove(idx);
		}
		list.add(0, obj);
		
		return true;
	}
	
	/** 정렬이 반영된 변경 **/
	public static <E extends NearCode> boolean sortMod(List<E> list, E obj) {
		if(CollectionUtils.isEmpty(list)) 	return false;
		if(StringUtils.isEmpty(obj.getNearCode()))	return false;
		
		boolean bRes = false;
		E tObj = null;
		int beforePosition = 0;
		for(int idx = 0; idx < list.size(); idx++) {
			if(equalNearCode(obj, list.get(idx))) {
				tObj = list.get(idx);
				
				logger.debug("sortMod bfore tObj[" + tObj.getNearCode() + "] NearCnt:[" + tObj.getNearCnt() + "]");
				
				tObj.modValue(obj, true);
				beforePosition = idx;
				bRes = true;
				break;
			}
		}
		if(tObj == null) return false;
		
		list.remove(tObj);

		//최신순 
		logger.debug("sortMod after tObj[" + tObj.getNearCode() + "] NearCnt:[" + tObj.getNearCnt() + "]");
		list.add(0, tObj);
		
		return bRes;
	}
	
	private static <E extends NearCode> boolean equalNearCode(E obj1, E obj2) {
		if(obj1 == null || StringUtils.isEmpty(obj1.getNearCode()))	return false;
		if(obj2 == null || StringUtils.isEmpty(obj2.getNearCode()))	return false;

		return obj1.getNearCode().equals(obj2.getNearCode());
	}
	
	private NearCodeCtr() {}
	

	
	
}
