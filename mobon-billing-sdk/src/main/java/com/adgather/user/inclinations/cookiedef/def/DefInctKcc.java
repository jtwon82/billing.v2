package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.inct.InctKcc;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

/**
 * KCC(문맥) 카테고리 쿠키 카운트 정보
 * @author dsChoi
 *
 */
public class DefInctKcc extends CookieDef {
	public DefInctKcc(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new InctKcc();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<InctKcc>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_INCT_KCC_EXPIRE");		
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_KCC_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_KCC_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_KCC_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_KCC_BYTES"));
	}

	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_IC_KCC_ENABLED");
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		//return Base64Converter.getInstance();
		return null;	// InctKcc에서 처리
	}
}
