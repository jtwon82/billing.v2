package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.IntegerSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 나이(성향) 쿠키 정의
 * @author yhlim
 *
 */
public class DefInctAge extends CookieDef {
	public DefInctAge(String cookieKey) {
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
		return  PropertyHandler.getInt("COOKIE_INCT_AGE_EXPIRE"); // 6개월
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(7);
		memoryObj.setMongoMaxLen(1024);
		memoryObj.setCookieMaxCnt(7);
		memoryObj.setCookieMaxLen(1024);
	}
	
	@Override
	public boolean isFixePose() {
		return true;			//0/10/20/30/40/50/60 순으로 연령 설정
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_INCT_AGE_ENABLED");
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
