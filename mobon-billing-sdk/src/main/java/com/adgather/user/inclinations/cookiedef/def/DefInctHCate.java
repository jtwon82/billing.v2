package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.inct.InctCate;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

/**
 * 카테로리(성향) 쿠키 정의
 * @author yhlim
 *
 */
public class DefInctHCate extends CookieDef {
	public DefInctHCate(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new InctCate();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<InctCate>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_HCATE_EXPIRE");		// 6개월 고정 (ic_hu, ic_um, _ic_ki, tag, cat, gender, age 동일 적용)
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_HCATE_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_HCATE_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_HCATE_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_HCATE_BYTES"));
	}
	
	@Override 
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_INCT_HCATE_ENABLED");
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
