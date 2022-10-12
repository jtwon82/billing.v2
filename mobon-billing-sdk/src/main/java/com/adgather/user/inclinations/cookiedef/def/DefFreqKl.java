package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqKl;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * 키워드광고(KL) 프리퀀시 쿠키 정의
 * @author yhlim
 *
 */
public class DefFreqKl extends CookieDef {
	public DefFreqKl(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new FreqKl();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<FreqKl>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_FREQ_KL_EXPIRE");
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("COOKIE_FREQ_KL_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("COOKIE_FREQ_KL_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("MONGO_FREQ_KL_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("MONGO_FREQ_KL_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_FREQ_KL_ENABLE");
	}
	
	
	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		if(isUseMongo()) {
			return true;
		}
		
		if(CIFunctionController.isUseMongFreqMs(mediaScriptNo)) {
			return CIFunctionController.isUseMongFreqMsGubun(GlobalConstants.KL);
		}
		
		return false;
	}

	@Override
	public CodeConverter getCodeConverter() {
		//return Base64Converter.getInstance();
		return null;
	}
}
