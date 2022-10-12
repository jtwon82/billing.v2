package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.InctHuConvSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.inct.InctHuConv;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * HU(성향) 컨버전횟수 쿠키 정보
 * @author kwseo
 *
 */
public class DefInctHuConv extends CookieDef {
	public DefInctHuConv(String cookieKey) {
		super(cookieKey);
	}

	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new InctHuConv();
	}
	
	@Override
	public SyncList<?> newList() {
		return new InctHuConvSyncList<InctHuConv>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_HU_CONV_EXPIRE"); // 2년과 6개월 혼재사용중 2년으로 설정
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_HU_CONV_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_HU_CONV_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_HU_CONV_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_HU_CONV_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}

	@Override
	public boolean isUseMongo() {
		return CIFunctionController.isUseMongoInctHu();
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
