package com.adgather.user.inclinations.cookielist.inter;

import java.util.Map;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;

/**
 * 쿠키 리스트 데이터 정의(리스트 객체 구현)
 * @author yhlim
 *
 */
public interface CookieList {
	/** 쿠키값으로 객체 설정 **/
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception;
	
	/** 객체로 쿠키값 생성 **/
	public String getCookieValue(final int maxLen, final CookieDef cookieDef) throws Exception;
	
	/** 몽고값으로 객체 설정 **/
	public void setMongoValue(Object mongoObj, final CookieDef cookieDef) throws Exception;
	
	/** 객체로 몽고값 생성 **/
	public Object getMongoValue() throws Exception;
	
	/** 객체 복사 **/
	public CookieList clone() throws CloneNotSupportedException;
	
	/** 유일값 **/
	public String getKey();
	
	/** 유일값의 인덱스 정보 **/
	public abstract Map<String, Integer> getIdxMap();
}
