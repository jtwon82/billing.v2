package com.adgather.user.inclinations.cookiedef.inter;

import java.util.Map;

import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;

/**
 * 변환 가능 쿠키 정의 객체
 * @author yhlim
 *
 */
public abstract class RefactCookieDef extends CookieDef {
	private String refactCookieKey;		/** 쿠키이름 변경 **/
	private String refactMongoKey;		
	
	public RefactCookieDef(String cookieKey, String mongoKey, String refactCookieKey, String refactMongoKey) {
		super(cookieKey, mongoKey);
		this.refactCookieKey = refactCookieKey;
		this.refactMongoKey = refactMongoKey;
	}
	
	public String getRefactCookieKey() {
		return refactCookieKey;
	}

	public String getRefactMongoKey() {
		return refactMongoKey;
	}

	/** 쿠키 변환 여부 확인 **/
	public abstract boolean isRefacting();

	/** 신규 MemoryObject의 대행자(이전 쿠키를 이후 쿠키 처럼 사용하기 위함.) **/
	public abstract Map<String, SimulMemoryObj> createSimulMemoryObj(MemoryObj obj);
}
