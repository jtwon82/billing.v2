package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.inct.InctInstall;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * UM(성향) 도메인 쿠키 정의
 * @author yhlim
 *
 */
public class DefInctInstall extends CookieDef {
	public DefInctInstall(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new InctInstall();
	}

	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<InctInstall>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return 60*60*24*30*24;		// 24개월
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_APP_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_APP_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_APP_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_APP_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_INCT_APP_ENABLED");
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return null;
	}
}
