package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqShops;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 상픔광고(CW/SR/RC/SP) 프리퀀시 쿠키 정의
 * @author yhlim
 *
 */
public class DefFreqShops extends CookieDef {
	public DefFreqShops(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new FreqShops();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<FreqShops>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_FREQ_SHOPS_EXPIRE");
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("COOKIE_FREQ_SHOPS_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("COOKIE_FREQ_SHOPS_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("MONGO_FREQ_SHOPS_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("MONGO_FREQ_SHOPS_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_FREQ_SHOPS_ENABLE");
	}

	
	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		if(isUseMongo()) {
			return true;
		}
		
		if(CIFunctionController.isUseMongFreqMs(mediaScriptNo)) {
			return CIFunctionController.isUseMongFreqMsGubun("SHOPS");
		}
		
		return false;
	}
	
	@Override
	public CodeConverter getCodeConverter() {
		return null;
	}
}
