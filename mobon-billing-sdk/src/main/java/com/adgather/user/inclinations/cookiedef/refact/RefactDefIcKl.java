package com.adgather.user.inclinations.cookiedef.refact;

import java.util.HashMap;
import java.util.Map;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.refact.RefactInctKlSyncList;
import com.adgather.user.inclinations.cookielist.refact.RefactStringLSyncList;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctKl;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.user.inclinations.memory.simulator.InctKlSimul;
import com.adgather.util.PropertyHandler;

/**
 * 기존 샵로그 쿠키 변환기 정의 
 * @author yhlim
 *
 */
public class RefactDefIcKl extends RefactCookieDef {
	public RefactDefIcKl(String cookieKey, String refactCookieKey) {
		super(cookieKey, cookieKey, refactCookieKey, refactCookieKey);
	}

	/** CookieDef 구현 ********************************************/
	private static final String LIST_DELIMETER_OLD = "|||";
	
	@Override
	public Object newObj() {
		return new OldInctKl();
	}
	
	@Override
	public SyncList<?> newList() {
		return new RefactInctKlSyncList(LIST_DELIMETER_OLD, LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_KL_EXPIRE"); // 6개월
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGODB_IC_KI_COUNT_LIMIT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGODB_IC_KI_BYTE_LIMIT"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_IC_KI_COUNT_LIMIT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_IC_KI_BYTE_LIMIT"));
	}

	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return false;		/** 이전 데이터 몽고 사용하지 않음 **/
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return Base64Converter.getInstance();
	}
	
	/** RefactCookieDef 구현 ********************************************/
	@Override
	public boolean isRefacting() {
		return CIFunctionController.isRefactingInctKl();
	}

	@Override
	public Map<String, SimulMemoryObj> createSimulMemoryObj(MemoryObj obj) {
		Map<String, SimulMemoryObj> map = new HashMap<String, SimulMemoryObj>();
		map.put(CookieDefRepository.INCT_KL, new InctKlSimul(obj));
		return map;
	}
}
