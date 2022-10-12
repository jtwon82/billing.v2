package com.adgather.user.inclinations.cookieval.inter;


/**
 * 쿠키 로우 데이터 정의(클래스 정의시 구현)
 * @author yhlim
 *
 */
public interface CookieVal {
	/** 기본 분할자 **/
	public static final String DELIMETER = "^";
	
	/** 쿠키값으로 객체 설정 (cookieValue String) **/
	public void setCookieValue(Object cookieValue) throws Exception;
	
	/** 객체로 쿠키값  (return String) **/
	public Object getCookieValue();
	
	/** 몽고값으로 객체 설정 (mongoDoc Document) **/
	public void setMongoValue(Object mongoDoc) throws Exception;
	
	/** 객체로 몽고값 생성 (return Document) **/
	public Object getMongoValue();
	
	/** 객체 복사 **/
	public CookieVal clone() throws CloneNotSupportedException;
	
	/** 유일값 **/
	public String getKey();
	
	/** 변경설정중 값의 추가 형태 처리 **/
	public void modValue(Object element, boolean bAppendValue);
}
