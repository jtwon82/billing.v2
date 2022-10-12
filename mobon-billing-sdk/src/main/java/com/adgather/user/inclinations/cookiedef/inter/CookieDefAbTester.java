package com.adgather.user.inclinations.cookiedef.inter;

import com.adgather.user.inclinations.memory.MemoryObj;


/**
 * 쿠키 정의 
 * @date 2017. 6. 26.
 * @param 
 * @exception
 * @see
*/
public abstract class CookieDefAbTester extends CookieDef {
	public CookieDefAbTester(String cookieKey) {
		super(cookieKey);
	}
	public CookieDefAbTester(String cookieKey, String mongoKey) {
		super(cookieKey, mongoKey);
	}
	
	/** 개수 abtest 여부 처리 **/
	public abstract void infuseLimit(MemoryObj<?> memoryObj);
	
}
