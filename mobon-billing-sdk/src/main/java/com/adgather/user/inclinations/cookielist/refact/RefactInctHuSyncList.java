package com.adgather.user.inclinations.cookielist.refact;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.InctHuSyncList;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.inter.RefactCookieList;
import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctHu;

/**
 * 새로 구현된 객체(InctHu만)의 리스트 동기화 관리객체
 * (Element는 기존이나 신규형태가 같다. 그러나 리스트 분리자는 다르다.)
 * @author yhlim
 *
 */
public class RefactInctHuSyncList extends InctHuSyncList<OldInctHu> implements  RefactCookieList {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RefactInctHuSyncList.class);

	/** cookie값의 임시저장(비교 용도) **/
	private String strCheckCookieValue;
	
	private final String OLD_DELIMETER;
	
	public RefactInctHuSyncList(String oldDelimeter, String newDelimeter ) {
		super(newDelimeter);
		this.OLD_DELIMETER = oldDelimeter;
	}

	/** Implement Methods***************************************************************/
	/** 기존 쿠키를 리스트로 구성 **/
	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		if(!(element instanceof InctHu))	return false;
		
		return super.applyAdd(new OldInctHu((InctHu)element), bAppendValue, maxCnt);
	}
	
	@Override
	public boolean applyDel(Object element) {
		if(!(element instanceof InctHu))	return false;
		
		return super.applyDel(new OldInctHu((InctHu)element));
	}
	
	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if(!(element instanceof InctHu))	return false;

		return super.applyMod(new OldInctHu((InctHu)element), bAppendValue);
	}
	
	@Override
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");

		clear();
		
		// 몽고 데이터와 비교 하기 위한 임시값 저장
		strCheckCookieValue = cookieValue;
		
		// 기존 cookie의 shopLog의 syncTime은 0으로 설정
		this.syncTime = 0;
		
		if(StringUtils.isEmpty(cookieValue))	 throw new Exception("Cookie Value Is Null.");
		
		String[] arrayValue = StringUtils.splitPreserveAllTokens(cookieValue, OLD_DELIMETER); 
		
//		if(ArrayUtils.isEmpty(arrayValue))		throw new Exception("Array Is Empty.");
		if(ArrayUtils.isEmpty(arrayValue))		return;
		
		for (String element : arrayValue) {
			if(StringUtils.isEmpty(element))	continue;
			
			try {
				add(new OldInctHu(element));
			} catch (Exception e) {
				//logger.error(ErrorLog.getStack(e));
			}
		}
	}

	/** 리스트를 쿠키값으로 변경 (Watable 기능 제외, maxLen 제외) **/
	@Override
	public String getCookieValue(int maxLen, final CookieDef cookieDef) throws Exception {
		return _getOld();
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
	public Map<String, String> getRefactCookieValue(int maxLen, RefactCookieDef refactCookieDef) throws Exception {
		Map<String, ObjectSyncList<InctHu>> syncLists = _getRefactSyncLists(refactCookieDef.getRefactCookieKey());
		
		Map<String, String> cookieMap = new HashMap<String, String>();
		for (Map.Entry<String, ObjectSyncList<InctHu>> entry : syncLists.entrySet()) {
			ObjectSyncList<InctHu> syncList = entry.getValue();
			if(syncList.size() > 0) {
				cookieMap.put(entry.getKey(), entry.getValue().getCookieValue(maxLen, refactCookieDef));
			}
		}
		return cookieMap;
	}

	@Override
	public Map<String, Object> getRefactMongoValue(RefactCookieDef refactCookieDef) throws Exception {
		Map<String, ObjectSyncList<InctHu>> syncLists = _getRefactSyncLists(refactCookieDef.getRefactMongoKey());
		
		Map<String, Object> mongoMap = new HashMap<String, Object>();
		for (Map.Entry<String, ObjectSyncList<InctHu>> entry : syncLists.entrySet()) {
			ObjectSyncList<InctHu> syncList = entry.getValue();
			if(syncList.size() > 0) {
				mongoMap.put(entry.getKey(), entry.getValue().getMongoValue());
			}
		}
		return mongoMap;
	}

	@Override
	public RefactInctHuSyncList clone() {
		RefactInctHuSyncList newList = new RefactInctHuSyncList(OLD_DELIMETER, DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((OldInctHu)get(idx).clone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newList;
	}
	
	/** Inner Methods***************************************************************/

	
	private String _getOld() {
		final int cnt = size();
		if(cnt == 0)		return null;
		
		StringBuffer buf = new StringBuffer();
		for (int idx = 0; idx < cnt; idx++) {
			OldInctHu obj = get(idx);
			if(obj == null)		continue;
			
			buf.append(obj.getCookieValue()).append(OLD_DELIMETER);
		}
		
		return buf.toString();
	}
	
	/** 쿠키/몽고 변경 객체 생성 **/
	private Map<String, ObjectSyncList<InctHu>> _getRefactSyncLists(String cookieName) {
		if(cookieName == null)	return null;
		
		Map<String, ObjectSyncList<InctHu>> map = new HashMap<String, ObjectSyncList<InctHu>>();
		
		ObjectSyncList<InctHu> list = new ObjectSyncList<InctHu>(DELIMETER);		// iHu
		list.setCurTime(this.curTime);
		list.setSyncTime(this.syncTime);
		if(size() > 0) {
			for(int idx = 0; idx < size(); idx++) {
				OldInctHu oldInctHu = get(idx);
				list.add(new InctHu(oldInctHu));
			}
		}
				
		map.put(cookieName, list);
		return map;
	}
	

}