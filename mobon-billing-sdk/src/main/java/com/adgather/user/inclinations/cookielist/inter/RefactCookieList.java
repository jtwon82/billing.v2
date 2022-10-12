package com.adgather.user.inclinations.cookielist.inter;

import java.util.Map;

import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;

/**
 * 쿠키변환기를 담는 리스트 구현
 * 예전의 쿠키/몽고 형식을 새로운 형태로 교체 할 수 있는 기능
 * @date 2017. 7. 7.
 * @param 
 * @exception
 * @see
*/
public interface RefactCookieList {
	/** 객체로 쿠키값 생성(key: cookie Key, value, cookie Value) **/
	public Map<String, String> getRefactCookieValue(final int maxLen, final RefactCookieDef cookieDef) throws Exception;
	
	/** 객체로 몽고값 생성 **/
	public Map<String, Object> getRefactMongoValue(final RefactCookieDef cookieDef) throws Exception;
}
