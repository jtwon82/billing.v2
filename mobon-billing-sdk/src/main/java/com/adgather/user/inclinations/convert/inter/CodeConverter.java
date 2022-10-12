package com.adgather.user.inclinations.convert.inter;

/**
 * 코드 변환기(BASE64, URLENCODING ...)
 * @date 2017. 6. 28.
 * @param 
 * @exception
 * @see
*/
public interface CodeConverter {
	/** 암호화 **/
	public abstract String encode(String data);
	
	/** 복호화 **/
	public abstract String decode(String data);
}
