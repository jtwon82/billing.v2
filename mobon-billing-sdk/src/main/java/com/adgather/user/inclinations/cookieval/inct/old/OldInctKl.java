package com.adgather.user.inclinations.cookieval.inct.old;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.cookieval.inct.InctKl;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctUmCtr;
import com.adgather.util.HangulCharsetDetector;


/**
 * UM 도메인 로우 데이터
 * @author yhlim
 *
 */
public class OldInctKl extends InctKl {
	/** values ****************************************/

	/** create method **********************************/
	public OldInctKl() {}
	
	public OldInctKl(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public OldInctKl(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public OldInctKl(OldInctKl obj) {
		super(obj);
	}
	
	public OldInctKl(InctKl obj) {
		super(obj);
	}
	
	
	/** Implements method  **********************************/
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
		
		if(HangulCharsetDetector.isBrokenString(cookieValue)) throw new Exception("InctKl Keyword Is Broken");
		
		super.setKeyword(Base64Converter.getInstance().decode(cookieValue));
		super.setUpdDate(InctUmCtr.getUpdDate());		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
	}
	
	@Override
	public Object getCookieValue() {
		return StringUtils.defaultString(Base64Converter.getInstance().encode(super.getKeyword()));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		/** 이전 데이터 저장하지 않음 **/
	}
	
	@Override
	public Object getMongoValue() {
		/** 이전 데이터 저장하지 않음 **/
		return null;
	}
	
	@Override
	public OldInctKl clone() throws CloneNotSupportedException {
		return new OldInctKl(this);
	}
}
