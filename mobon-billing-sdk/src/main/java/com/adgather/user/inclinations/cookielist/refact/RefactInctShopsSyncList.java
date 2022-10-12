package com.adgather.user.inclinations.cookielist.refact;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.inter.RefactCookieList;
import com.adgather.user.inclinations.cookieval.inct.InctShops;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctShopsCtr;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctShopLog;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 새로 구현된 객체(OldInctShopLog만)의 리스트 동기화 관리객체
 * @author yhlim
 *
 */
public class RefactInctShopsSyncList extends ObjectSyncList<OldInctShopLog> implements  RefactCookieList {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RefactInctShopsSyncList.class);
	
	/** cookie값의 임시저장(비교 용도) **/
	private String strCheckCookieValue;
	
	public RefactInctShopsSyncList(String newDelimeter ) {
		super(newDelimeter);
	}
	
	/** Implement Methods***************************************************************/
	/** 기존 쿠키를 리스트로 구성 **/
	@Override
	public void setCookieValue(String cookieValue, CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");
		clear();
		
		// shoplog URLDecode 전체 적용
		if(cookieDef.getCodeConverter() != null) {
			cookieValue = cookieDef.getCodeConverter().decode(cookieValue);
		}
		
		// json 형식 이상 중 배열 형태가 남아 있는 경우에만 복원 가능(이전 shop_log형식중)
		if(StringUtils.isNotEmpty(cookieValue) && cookieValue.indexOf("},{") > 0 && !cookieValue.endsWith("}]}")) {
			StringUtils.replace(cookieValue, "},{", "}]}");
		}
		
		
		// 몽고 데이터와 비교 하기 위한 임시값 저장
		strCheckCookieValue = cookieValue;
		
		// 기존 cookie의 shopLog의 syncTime은 0으로 설정
		this.syncTime = 0;
		
		_setOld(cookieValue);
	}

	/** 리스트를 쿠키값으로 변경 (Watable 기능 제외, maxLen 제외) **/
	@Override
	public String getCookieValue(int maxLen, CookieDef cookieDef) throws Exception {
		JSONObject jsonCookieValue = _getOld();
				
		String cookieValue = null;
		if(cookieDef.getCodeConverter() != null && jsonCookieValue != null) {
			cookieValue = cookieDef.getCodeConverter().encode(jsonCookieValue.toString());
		}
		
		return cookieValue;
	}

	@Override
	public void setMongoValue(Object mongoObj, CookieDef cookieDef) throws Exception {
		if(!(mongoObj instanceof String))	throw new Exception("Unsupport Object.[" + mongoObj + "]");			// String만 이용
		
		clear();
		
		String mongoDoc = (String)mongoObj;
		// 쿠키값이 몽고의 데이터 앞이 같은 경우 몽고 변경 처리 하지 않음.
		if(strCheckCookieValue != null && mongoDoc != null && !mongoDoc.startsWith(strCheckCookieValue)) {
			this.syncTime = 1;		// 1이면 몽고 변경 merge기능 실행
		} else  {
			this.syncTime = 0;
		}

		_setOld(mongoDoc);

	}

	@Override
	public Object getMongoValue() throws Exception {
		JSONObject jsonCookieValue = _getOld();
		
		if(jsonCookieValue  == null)		return null;
		
		return jsonCookieValue.toString();
	}
	
	
	@Override
	public String getKey() {
		return super.getKey();
	}
	

	@Override
	public Map<String, String> getRefactCookieValue(int maxLen, RefactCookieDef refactCooieDef) throws Exception {
		Map<String, ObjectSyncList<InctShops>> syncLists = _getRefactSyncLists();
		
		Map<String, String> cookieMap = new HashMap<String, String>();
		for (Map.Entry<String, ObjectSyncList<InctShops>> entry : syncLists.entrySet()) {
			ObjectSyncList<InctShops> syncList = entry.getValue();
			if(syncList.size() > 0) {
				cookieMap.put(entry.getKey(), entry.getValue().getCookieValue(maxLen, refactCooieDef));
			}
		}
		return cookieMap;
	}

	@Override
	public Map<String, Object> getRefactMongoValue(RefactCookieDef refactCooieDef) throws Exception {
		Map<String, ObjectSyncList<InctShops>> syncLists = _getRefactSyncLists();
		
		Map<String, Object> mongoMap = new HashMap<String, Object>();
		for (Map.Entry<String, ObjectSyncList<InctShops>> entry : syncLists.entrySet()) {
			ObjectSyncList<InctShops> syncList = entry.getValue();
			if(syncList.size() > 0) {
				mongoMap.put(entry.getKey(), entry.getValue().getMongoValue());
			}
		}
		return mongoMap;
	}

	@Override
	public RefactInctShopsSyncList clone() {
		RefactInctShopsSyncList newList = new RefactInctShopsSyncList(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((OldInctShopLog)((CookieVal)get(idx)).clone());
			}
		} catch (Exception e) {}
		
		return newList;
	}
	
	/** Inner Methods***************************************************************/
	private void _setOld(String cookieValue) throws Exception {
		if(cookieValue == null)	 throw new Exception("Cookie Value Is Null.");
		
		// JSON으로 변경
		JSONObject jsonValue = null;
		try {
			jsonValue = JSONObject.fromObject(cookieValue);
		} catch (Exception e) {}
		
//		if(jsonValue == null || jsonValue.size() == 0)		throw new Exception("Json Is Empty.");
		if(jsonValue == null || jsonValue.size() == 0)		return;
		if(!jsonValue.containsKey("data"))								throw new Exception("Json Is Empty.");
		
		JSONArray jsonArray = jsonValue.getJSONArray("data");
		for (int idx = 0; idx < jsonArray.size(); idx++) {
			try {
				OldInctShopLog newObj = new OldInctShopLog();
				newObj.setCookieValue(jsonArray.get(idx));
				add(newObj);	
			} catch (Exception e) {
				// 형식이 잘 못된 것은 로그 출력 하지 않음.
				//logger.error(ErrorLog.getStack(e));
			}
		}
	}
	
	private JSONObject _getOld() {
		final int cnt = size();
		if(cnt == 0)		return null;
		JSONArray jsonArray = new JSONArray();
		for (int idx = 0; idx < cnt; idx++) {
			OldInctShopLog obj = get(idx);
			if(obj == null)		continue;
			
			jsonArray.add(obj.getCookieValue());
		}
		
		JSONObject jsonCookieValue = new JSONObject();
		jsonCookieValue.put("data", jsonArray);
		return jsonCookieValue;
	}
	
	/** 쿠키/몽고 변경 객체 생성 **/
	private Map<String, ObjectSyncList<InctShops>> _getRefactSyncLists() {
		Map<String, ObjectSyncList<InctShops>> map = new HashMap<String, ObjectSyncList<InctShops>>();
		
		ObjectSyncList<InctShops> cwList = new ObjectSyncList<InctShops>(DELIMETER);		// cwShopLogs
		cwList.setCurTime(this.curTime);
		cwList.setSyncTime(this.syncTime);

		ObjectSyncList<InctShops> srList = new ObjectSyncList<InctShops>(DELIMETER);			// srShopLogs
		srList.setCurTime(this.curTime);
		srList.setSyncTime(this.syncTime);

		ObjectSyncList<InctShops> rcList = new ObjectSyncList<InctShops>(DELIMETER);			// rcShopLogs
		rcList.setCurTime(this.curTime);
		rcList.setSyncTime(this.syncTime);

		ObjectSyncList<InctShops> spList = new ObjectSyncList<InctShops>(DELIMETER);		// spShopLogs
		spList.setCurTime(this.curTime);
		spList.setSyncTime(this.syncTime);
		
		for (int idx = 0; idx < size(); idx++) {
			OldInctShopLog obj = get(idx);
			if(InctShopsCtr.isCwShopLog(obj)) {
				cwList.add(new InctShops(obj));	
			} else if(InctShopsCtr.isSrShopLog(obj)) {								
				srList.add(new InctShops(obj));
			} else if(InctShopsCtr.isRcShopLog(obj)) {
				rcList.add(new InctShops(obj));
			} else if(InctShopsCtr.isSpShopLog(obj)) {
				spList.add(new InctShops(obj));
			}
		}
		
		map.put(CookieDefRepository.INCT_CW, cwList);
		map.put(CookieDefRepository.INCT_SR, srList);
		map.put(CookieDefRepository.INCT_RC, rcList);
		map.put(CookieDefRepository.INCT_SP, spList);
		return map;
	}
	

}