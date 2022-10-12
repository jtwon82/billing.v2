package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.StringSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 테그(성향) 쿠키 정의
 * @author yhlim
 *
 */
public class DefInctTag extends CookieDef {
	public DefInctTag(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new String();
	}
	
	@Override
	public SyncList<?> newList() {
		return new StringSyncList(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return 60*60*24*30*6;		// 6개월 고정 (ic_hu, ic_um, _ic_ki, tag, cat, gender, age 동일 적용)
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
		return false;
	}

	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return Base64Converter.getInstance();
	}
}
