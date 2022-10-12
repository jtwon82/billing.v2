package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqAdver;
import com.adgather.user.inclinations.memory.MemoryObj;


/**
 * UM프리퀀시 쿠키 정의
 * @author yhlim
 *
 */
public class DefFreqAdverView extends CookieDef {
	public DefFreqAdverView(String cookieKey) {
		super(cookieKey);
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		return new FreqAdver();
	}
	
	@Override
	public SyncList<?> newList() {
		return new ObjectSyncList<FreqAdver>(LIST_DELIMETER);
	}
	
	@Override
	public int getExpire() {
		return 63072000;		// 60*60*24*365*2 = 63072000
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
