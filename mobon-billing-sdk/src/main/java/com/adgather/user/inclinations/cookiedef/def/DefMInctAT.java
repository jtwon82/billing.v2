package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.minct.MInctAT;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 쿠키 정보
 * @author 
 *
 */
public class DefMInctAT extends CookieDef {
	public DefMInctAT(String cookieKey) {
		super(cookieKey);
	}

	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new MInctAT();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<MInctAT>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_MINCT_AP_EXPIRE"); // 2년과 6개월 혼재사용중 2년으로 설정
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_MINCT_AP_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_MINCT_AP_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_MINCT_AP_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_MINCT_AP_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_MINCT_AP_ENABLE");
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
