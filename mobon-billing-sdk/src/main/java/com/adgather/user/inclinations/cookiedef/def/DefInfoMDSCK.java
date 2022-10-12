package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.minct.MInctDSCK;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * MDSCK 쿠키 정의
 * @author yhlim
 *
 */
public class DefInfoMDSCK extends CookieDef {			// yhlim 20171017 ab테스트를 위한 cookie 정의(10/18~11/01 유지예정)
//public class DefInctUm extends CookieDef {				// yhlim 20171017 기존 cookie 정의
	public DefInfoMDSCK(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new MInctDSCK();
	}

	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<MInctDSCK>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_MDSCK_TYPE_EXPIRE");	// 2개월 고정
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_MDSCK_TYPE_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_MDSCK_TYPE_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_MDSCK_TYPE_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_MDSCK_TYPE_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_MDSCK_TYPE_ENABLE");
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
