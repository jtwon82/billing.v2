package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.inct.InctKl;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

/**
 * KI(성향) 키워드 쿠키 정보
 * @author yhlim
 *
 */
public class DefInctKl extends CookieDef {
	public DefInctKl(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new InctKl();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<InctKl>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_INCT_KL_EXPIRE");		// 6개월 고정 (ic_hu, ic_um, _ic_ki, tag, cat, gender, age 동일 적용)
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGODB_IC_KI_COUNT_LIMIT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGODB_IC_KI_BYTE_LIMIT"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_IC_KI_COUNT_LIMIT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_IC_KI_BYTE_LIMIT"));
	}

	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_IC_KL_ENABLED");
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		//return Base64Converter.getInstance();
		return null;	// InctKl에서 처리
	}
}
