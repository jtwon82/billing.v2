package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqSimple;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * UM프리퀀시 쿠키 정의
 * @author yhlim
 *
 */
public class DefFreqSimple extends CookieDef {
	public DefFreqSimple(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new FreqSimple();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<FreqSimple>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_FREQ_UM_EXPIRE");
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(30);
		memoryObj.setMongoMaxLen(2048);
		memoryObj.setCookieMaxCnt(30);
		memoryObj.setCookieMaxLen(2048);
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public  boolean isUseMongo() {
		return false;
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
