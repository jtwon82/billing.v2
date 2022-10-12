package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.near.NearCode;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 지역타겟팅 쿠키 정보
 * @author kwseo
 *
 */
public class DefNearCode extends CookieDef {
	public DefNearCode(String cookieKey) {
		super(cookieKey);
	}

	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new NearCode();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<NearCode>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_NEAR_CODE_EXPIRE"); // 2년과 6개월 혼재사용중 2년으로 설정
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_NEAR_CODE_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_NEAR_CODE_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_NEAR_CODE_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_NEAR_CODE_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_NEAR_CODE_ENABLE");
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
