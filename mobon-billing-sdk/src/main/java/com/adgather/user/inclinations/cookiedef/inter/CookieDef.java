package com.adgather.user.inclinations.cookiedef.inter;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.memory.MemoryObj;


/**
 * 쿠키 정의 
 * @date 2017. 6. 26.
 * @param 
 * @exception
 * @see
*/
public abstract class CookieDef {
	public static final String LIST_DELIMETER = "#";	
	
	private String cookieKey;			/** 쿠키 키값 **/
	private String mongoKey;			/** 몽고 키값 **/
	
	public CookieDef(String cookieKey) {
		this.cookieKey = cookieKey;
		this.mongoKey = cookieKey;
	}
	public CookieDef(String cookieKey, String mongoKey) {
		this.cookieKey = cookieKey;
		this.mongoKey = mongoKey;
	}
	
	public String getCookieKey() {
		return cookieKey;
	}
	public String getMongoKey() {
		return mongoKey;
	}
	
	/** 빈 객체 생성  **/
	public abstract Object newObj();

	/** 빈 리스트 생성 **/
	public abstract SyncList<?> newList();
	
	/** 쿠키의 expire time **/
	public abstract int getExpire();

	/** 쿠키의 개수(몽고 개수 설정 포함) **/	
	public abstract void infuseLimit(MemoryObj<?> memoryObj);

	/** 고정형태 여부(길이가 고정되어 있고, 위치값에 특정 의미를 담는 경우) **/
	public abstract boolean isFixePose();
	
	/** 쿠키 사용여부(기본 저장) **/
	public boolean isUseCookie() {
		return true;
	}
	
	/** 몽고 사용여부 **/
	public abstract boolean isUseMongo();

	/** 몽고 사용여부 **/
	public abstract boolean isUseMongo(int mediaScriptNo);
	
	/** 암복호화 처리 **/
	public abstract CodeConverter getCodeConverter();
}
