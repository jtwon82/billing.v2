package com.adgather.user.inclinations.cookieval.inct.old;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.InctHu;

public class OldInctHu extends InctHu {
	private static final String DELIMETER = "#";
	/** values ****************************************/
	/** create method **********************************/
	public OldInctHu() {
		super();
	}
	
	public OldInctHu(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public OldInctHu(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public OldInctHu(InctHu obj) {
		super(obj);
	}
	
	/** value get/set method **********************************/
	
	/** Implements method  ***********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(cookieValue instanceof String) {
			setCookieValue((String)cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setCookieValue(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length != 2)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.setVisitCnt(NumberUtils.toInt(strs[0], 1));		// 도메인이 있으면 최소 방문 1회 설정
		this.setDomain(strs[1]);
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s"
					, getVisitCnt(), DELIMETER, StringUtils.defaultString(getDomain()));
	}
	
	@Override
	public OldInctHu clone() throws CloneNotSupportedException {
		return new OldInctHu(this);
	}
}
