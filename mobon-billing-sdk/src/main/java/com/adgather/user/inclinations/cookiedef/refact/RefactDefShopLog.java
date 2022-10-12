package com.adgather.user.inclinations.cookiedef.refact;

import java.util.HashMap;
import java.util.Map;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.URLConverter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.refact.RefactInctShopsSyncList;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctShopLog;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.user.inclinations.memory.simulator.InctCwSimul;
import com.adgather.user.inclinations.memory.simulator.InctRcSimul;
import com.adgather.user.inclinations.memory.simulator.InctSpSimul;
import com.adgather.user.inclinations.memory.simulator.InctSrSimul;
import com.adgather.util.PropertyHandler;

/**
 * 기존 샵로그 쿠키 변환기 정의 
 * @author yhlim
 *
 */
public class RefactDefShopLog extends RefactCookieDef {
	public RefactDefShopLog(String cookieKey, String mongoKey) {
		super(cookieKey, mongoKey, null, null);
	}

	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new OldInctShopLog();
	}

	@Override
	public SyncList<?> newList() {
		return new RefactInctShopsSyncList(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("SHOPLOG_COOKIE_EXPIRE_TIME");
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGODB_SHOP_LOG_COUNT_LIMIT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGODB_SHOP_LOG_BYTE_LIMIT"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("MAX_SHOP_COUNT"));
		memoryObj.setCookieMaxLen(1024);
	}

	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return CIFunctionController.isUseMongoShops();
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return URLConverter.getInstance();
	}
	
	/** RefactCookieDef 구현 ********************************************/
	@Override
	public boolean isRefacting() {
		return CIFunctionController.isRefactingShops();
	}

	@Override
	public Map<String, SimulMemoryObj> createSimulMemoryObj(MemoryObj obj) {
		Map<String, SimulMemoryObj> map = new HashMap<String, SimulMemoryObj>();
		map.put(CookieDefRepository.INCT_CW, new InctCwSimul(obj));
		map.put(CookieDefRepository.INCT_SR, new InctSrSimul(obj));
		map.put(CookieDefRepository.INCT_RC, new InctRcSimul(obj));
		map.put(CookieDefRepository.INCT_SP, new InctSpSimul(obj));
		return map;
	}
}
