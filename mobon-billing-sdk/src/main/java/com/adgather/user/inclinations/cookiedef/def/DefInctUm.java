package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.CookieDefAbTester;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.old.InctUm20171107BeforeSyncList;
import com.adgather.user.inclinations.cookieval.inct.InctUm;
import com.adgather.user.inclinations.cookieval.inct.old.InctUm20171107Before;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * UM(성향) 도메인 쿠키 정의
 * @author yhlim
 *
 */
public class DefInctUm extends CookieDefAbTester {			// yhlim 20171017 ab테스트를 위한 cookie 정의(10/18~11/01 유지예정)
//public class DefInctUm extends CookieDef {				// yhlim 20171017 기존 cookie 정의
	
	private int applyDay;
	
	public DefInctUm(String cookieKey, int applyDay) {
		super(cookieKey);
		this.applyDay = applyDay;
	}
	
	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		if(applyDay == 0) {
			return new InctUm();
		} else if(applyDay <= CookieDefRepository.APPLY_DAY_20171107_BEFORE) {
			return new InctUm20171107Before();
		} else {
			return new InctUm();
		}
	}

	@Override
	public SyncList<?> newList() {
		if(applyDay == 0) {
			return new ObjectLSyncList<InctUm>(LIST_DELIMETER);
		} else if (applyDay <= CookieDefRepository.APPLY_DAY_20171107_BEFORE) {
			return new InctUm20171107BeforeSyncList(LIST_DELIMETER);
		} else {
			return new ObjectLSyncList<InctUm>(LIST_DELIMETER);
		}
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_UM_EXPIRE");	// 6개월 고정 (ic_hu, ic_um, _ic_ki, tag, cat, gender, age 동일 적용)
	}

	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_UM_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_UM_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_UM_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_UM_BYTES"));
	}
	
	@Override
	public boolean isFixePose() {
		return false;
	}
	
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_IC_UM_ENABLED");
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
