package com.adgather.user.inclinations.cookiedef.refact;

import java.util.HashMap;
import java.util.Map;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.refact.RefactInctHuSyncList;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctHu;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.user.inclinations.memory.simulator.InctHuSimul;
import com.adgather.util.PropertyHandler;

/**
 * 기존 샵로그 쿠키 변환기 정의 
 * @author yhlim
 *
 */
public class RefactDefIcHu extends RefactCookieDef {
	public RefactDefIcHu(String cookieKey, String refactCookieKey) {
		super(cookieKey, cookieKey, refactCookieKey, refactCookieKey);
	}

	/** CookieDef 구현 ********************************************/
	private static final String LIST_DELIMETER_OLD = "|||";
	
	@Override
	public Object newObj() {
		return new OldInctHu();
	}

	@Override
	public SyncList<?> newList() {
		return new RefactInctHuSyncList(LIST_DELIMETER_OLD, LIST_DELIMETER);
	}

	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_HU_EXPIRE"); // 2년과 6개월 혼재사용중 2년으로 설정
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_HU_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_HU_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("HU_MAX_DOMAIN"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("HU_MAX_DOMAIN_BYTES"));
	}

	@Override
	public boolean isFixePose() {
		return false;
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
		return CIFunctionController.isRefactingInctHu();
	}

	@Override
	public Map<String, SimulMemoryObj> createSimulMemoryObj(MemoryObj obj) {
		Map<String, SimulMemoryObj> map = new HashMap<String, SimulMemoryObj>();
		map.put(CookieDefRepository.INCT_HU, new InctHuSimul(obj));
		return map;
	}
}
