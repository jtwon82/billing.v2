package com.adgather.user.inclinations.cookielist.refact;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.IntegerSyncList;
import com.adgather.user.inclinations.cookielist.StringLSyncList;
import com.adgather.user.inclinations.cookielist.inter.RefactCookieList;

/**
 * 새로 구현된 객체(InctHu만)의 리스트 동기화 관리객체
 * (Element는 기존이나 신규형태가 같다. 그러나 리스트 분리자는 다르다.)
 * @author yhlim
 *
 */
public class RefactIntegerSyncList extends IntegerSyncList implements RefactCookieList {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RefactIntegerSyncList.class);
	
	// ic_um 배열 분할자
	//private static final String DELIMETER = "|||";
	
	/** cookie값의 임시저장(비교 용도) **/
	private String strCheckCookieValue;
	
	private final String OLD_DELIMETER;
	
	public RefactIntegerSyncList(String oldDelimeter, String newDelimeter ) {
		super(newDelimeter);
		this.OLD_DELIMETER = oldDelimeter;
	}

	
	/** Implement Methods***************************************************************/
	/** 기존 쿠키를 리스트로 구성 **/
	@Override
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");
		
		clear();
		
		// 몽고 데이터와 비교 하기 위한 임시값 저장
		strCheckCookieValue = cookieValue;
		
		// 기존 cookie의 shopLog의 syncTime은 0으로 설정
		this.syncTime = 0;
		
		if(StringUtils.isEmpty(cookieValue))	 throw new Exception("Cookie Value Is Null.");
		
		if(StringUtils.contains(cookieValue, "|")) {
			cookieValue = StringUtils.replace(cookieValue, "|", DELIMETER);
		}
		
		String[] arrayValue = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER); 
		
//		if(ArrayUtils.isEmpty(arrayCookieValue))		throw new Exception("Array Is Empty.");
		if(ArrayUtils.isEmpty(arrayValue))		return;
		
		for (String element : arrayValue) {
			if(StringUtils.isEmpty(element))	continue;
			
			try {
				/** 인코딩 제외 **/
				
				add(NumberUtils.toInt(element, 0));	
			} catch (Exception e) {
				//logger.error(ErrorLog.getStack(e));
			}
		}
	}

	/** 리스트를 쿠키값으로 변경 (Watable 기능 제외, maxLen 제외) **/
	@Override
	public String getCookieValue(int maxLen, CookieDef cookieDef) throws Exception {
		final int cnt = size();
		if(cnt == 0)		return null;
		
		StringBuffer buf = new StringBuffer();
		for (int idx = 0; idx < cnt; idx++) {
			Integer obj = get(idx);
			if(obj == null)		continue;
			
			/** 인코딩 제외 **/
			
			buf.append(obj).append(DELIMETER);
			if(maxLen > 0 && buf.length() > maxLen) break;
		}
		
		if(maxLen > 0 && buf.length() > maxLen) {
			final int delimiterLen = DELIMETER.length();
			int lastIdx = buf.lastIndexOf(DELIMETER, maxLen - delimiterLen);
			if (lastIdx > -1) {
				buf.setLength(lastIdx + delimiterLen);
			}
		}
		

		if(buf.length() == 0)		return null;
		
		return buf.toString();
	}

	@Override
	public void setMongoValue(Object mongoDoc, CookieDef cookieDef) throws Exception {
/*기존 동기화 시간이 없는 경우 값확인 필요[처리 : mongoDoc 우선]
		// 쿠키값이 몽고의 데이터 앞이 같은 경우 몽고 변경 처리 하지 않음.
		if(strCheckCookieValue != null && mongoDoc != null && !mongoDoc. startsWith(strCheckCookieValue)) {
			this.syncTime = 1;		// 1이면 몽고 변경 merge기능 실행
		} else  {
			this.syncTime = 0;
		}
*/
		super.setMongoValue(mongoDoc, cookieDef);			// 기존 몽고 데이터가 없어 신규 형식 이용
	}

	@Override
	public Object getMongoValue() throws Exception {
		return super.getMongoValue();						// 기존 몽고 데이터가 없어 신규 형식 이용
	}
	
	
	@Override
	public String getKey() {
		return super.getKey();
	}
	

	@Override
	public Map<String, String> getRefactCookieValue(int maxLen,	RefactCookieDef refactCooieDef) throws Exception {
		Map<String,IntegerSyncList> syncLists = _getRefactSyncLists(refactCooieDef.getRefactCookieKey());
		
		Map<String, String> cookieMap = new HashMap<String, String>();
		for (Map.Entry<String, IntegerSyncList> entry : syncLists.entrySet()) {
			IntegerSyncList syncList = entry.getValue();
			if(syncList.size() > 0) {
				cookieMap.put(entry.getKey(), entry.getValue().getCookieValue(maxLen, refactCooieDef));
			}
		}
		return cookieMap;
	}

	@Override
	public Map<String, Object> getRefactMongoValue(RefactCookieDef refactCooieDef) throws Exception {
		Map<String, IntegerSyncList> syncLists = _getRefactSyncLists(refactCooieDef.getRefactMongoKey());
		
		Map<String, Object> mongoMap = new HashMap<String, Object>();
		for (Map.Entry<String, IntegerSyncList> entry : syncLists.entrySet()) {
			IntegerSyncList syncList = entry.getValue();
			if(syncList.size() > 0) {
				mongoMap.put(entry.getKey(), entry.getValue().getMongoValue());
			}
		}
		return mongoMap;
	}

	@Override
	public RefactIntegerSyncList clone() {
		RefactIntegerSyncList newList = new RefactIntegerSyncList(OLD_DELIMETER, DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add(get(idx));
			}
		} catch (Exception e) {}
		
		return newList;
	}
	
	/** Inner Methods***************************************************************/
	
	/** 쿠키/몽고 변경 객체 생성 **/
	private Map<String, IntegerSyncList> _getRefactSyncLists(String refactKey) {
		if(refactKey == null)	return null; 
		
		Map<String, IntegerSyncList> map = new HashMap<String, IntegerSyncList>();
		
		IntegerSyncList list = new IntegerSyncList(DELIMETER);		// iUm
		list.setCurTime(this.curTime);
		list.setSyncTime(this.syncTime);
		if(size() > 0) {
			list.addAll(this);
		}
				
		map.put(refactKey, list);
		return map;
	}
}