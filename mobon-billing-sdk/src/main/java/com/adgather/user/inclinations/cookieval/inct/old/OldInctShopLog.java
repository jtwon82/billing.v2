package com.adgather.user.inclinations.cookieval.inct.old;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.InctShops;

/**
 * 이전 샵로그(성향) 로우 데이터
 * 
 * 신규로 변경시 InctShopLog의 CookieValueElement 이용
 * 기존으로 변경시 RefactCookieValueElement 이용
 * 하나의 ROW 단위로만 처리
 * @date 2017. 7. 7.
 * @param 
 * @exception
 * @see
*/
public class OldInctShopLog extends InctShops {
	
	/** values ****************************************/
	/** create method **********************************/
	public OldInctShopLog() {
		super();
	}
	
	public OldInctShopLog(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public OldInctShopLog(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public OldInctShopLog(InctShops obj){
		super(obj);
	}
	
	
	/** value get/set method **********************************/
	
	/** Implements method  ***********************************/
	@Override
	public OldInctShopLog clone() throws CloneNotSupportedException {
		return new OldInctShopLog(this);
	}
	
	/** 기존 쿠키 값으로 객체 설정(shopLog는 json만 받음) **/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(!(cookieValue instanceof JSONObject)) {
			throw new Exception("Unsupport Object.[" + cookieValue + "]");			// JSONObject만 이용
		}
	
		setRefactCookieValue((JSONObject) cookieValue);
	}
	private void setRefactCookieValue(JSONObject jsonCookieValue) throws Exception {
		_set(jsonCookieValue);
	}

	/** 기존 쿠키 값으로 변경(shopLog는 json만 넘김) **/
	@Override
	public Object getCookieValue() {
		return _get();
	}

	/** 기존 몽고 값으로 객체 설정(shopLog는 json만 받음) **/
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(!(mongoDoc instanceof JSONObject)) {
			throw new Exception("Unsupport Object.[" + mongoDoc + "]");			// JSONObject만 이용
		}
		
		setRefactMongoValue((JSONObject) mongoDoc);
	}
	private void setRefactMongoValue(JSONObject jsonMongoDoc) throws Exception {
		_set(jsonMongoDoc);
	}

	/** 기존 몽고 값으로 변경(shopLog는 json만 넘김) **/
	@Override
	public Object getMongoValue() {
		return _get();
	}

	/** 기존 유일값 **/
	@Override
	public String getKey() {
		return super.getKey();
	}
	
	/** 값 설정 **/
	private void _set(JSONObject json) throws Exception {
		if(json == null || json.size() == 0)		throw new Exception("JSON Value Is Empty.");
		
		this.setSiteCode(json.getString("sc"));
		this.setpCode(json.getString("pcd"));
		if(json.containsKey("tg"))		this.setTg(json.getString("tg"));
		if(json.containsKey("mcgb"))	this.setMcgb(json.getString("mcgb"));
		if(json.containsKey("cwgb"))	this.setCwgb(json.getString("cwgb"));
		if(json.containsKey("td"))		this.setTd(json.getString("td"));
	}
	
	/** 값 반환 **/
	private JSONObject _get() {
		// ex) {"sc":"2f7f159a8b8fcf23d9fa43ebbc73b57a","pcd":"44679","tg":"","mcgb":"","cwgb":"GHI","td":"20170704"}
		
		JSONObject json = new JSONObject();
		json.put("sc", this.getSiteCode());
		json.put("pcd", this.getpCode());
		json.put("tg", StringUtils.defaultIfEmpty(this.getTg(), ""));
		json.put("mcgb", StringUtils.defaultIfEmpty(this.getMcgb(), ""));
		json.put("cwgb", StringUtils.defaultIfEmpty(this.getCwgb(), ""));
		json.put("td", StringUtils.defaultIfEmpty(this.getTd(), ""));
		return json;
	}

	/****************************************************/
}
