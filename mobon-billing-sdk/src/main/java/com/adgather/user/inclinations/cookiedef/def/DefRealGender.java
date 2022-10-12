package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.IntegerSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

/**
 * 성별(성향) 쿠키 정의
 * @author yhlim
 *
 */
public class DefRealGender extends CookieDef {
	public DefRealGender(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new Integer(0);
	}
		
	@Override
	public SyncList<?> newList() {
		return new IntegerSyncList(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return 60*60*24*30*24;		// 24개월
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
		return true;			//남/여 순으로 성별 설정
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_REAL_GENDER_ENABLED");
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
