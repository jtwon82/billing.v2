package com.adgather.user.inclinations.cookiedef.refact;

import java.util.HashMap;
import java.util.Map;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.refact.RefactIntegerSyncList;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.user.inclinations.memory.simulator.IntegerSyncListSimul;
import com.adgather.util.PropertyHandler;

/**
 * 기존 샵로그 쿠키 변환기 정의 
 * @author yhlim
 *
 */
public class RefactDefGender extends RefactCookieDef {
	public RefactDefGender(String cookieKey, String refactCookieKey) {
		super(cookieKey, cookieKey, refactCookieKey, refactCookieKey);
	}

	/** CookieDef 구현 ********************************************/
	private static final String LIST_DELIMETER_OLD = "|";
	
	@Override
	public Object newObj() {
		return new Integer(0);
	}
	
	@Override
	public SyncList<?> newList() {
		return new RefactIntegerSyncList(LIST_DELIMETER_OLD, LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_GENDER_EXPIRE"); // 6개월
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(2);
		memoryObj.setMongoMaxLen(1024);
		memoryObj.setCookieMaxCnt(2);
		memoryObj.setCookieMaxLen(1024);
	}

	@Override
	public boolean isFixePose() {
		return true;
	}

	@Override
	public boolean isUseMongo() {
		return false;
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return null;
	}
	
	/** RefactCookieDef 구현 ********************************************/
	@Override
	public boolean isRefacting() {
		return CIFunctionController.isRefactingGender();
	}

	@Override
	public Map<String, SimulMemoryObj> createSimulMemoryObj(MemoryObj obj) {
		Map<String, SimulMemoryObj> map = new HashMap<String, SimulMemoryObj>();
		map.put(CookieDefRepository.INCT_GENDER, new IntegerSyncListSimul(obj));
		return map;
	}
}
