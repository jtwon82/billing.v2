package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.CookieDefAbTester;
import com.adgather.user.inclinations.cookielist.InctHuSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.old.InctHu20171107BeforeSyncList;
import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.user.inclinations.cookieval.inct.old.InctHu20171107Before;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;


/**
 * HU(성향) 방문횟수 쿠키 정보
 * @author yhlim
 *
 */
public class DefInctHu extends CookieDefAbTester {		// yhlim 20171017 ab테스트를 위한 cookie 정의(10/18~11/01 유지예정)
// public class DefInctHu extends CookieDef {			// yhlim 20171017 기존 cookie 정의

	private int applyDay;
	
	public DefInctHu(String cookieKey, int applyDay) {
		super(cookieKey);
		this.applyDay = applyDay;
	}

	/** CookieDef 구현 ********************************************/
	@Override
	public Object newObj() {
		if(applyDay == 0) {
			return new InctHu();
		} else if(applyDay <= CookieDefRepository.APPLY_DAY_20171107_BEFORE) {
			return new InctHu20171107Before();
		} else {
			return new InctHu();
		}
	}
	
	@Override
	public SyncList<?> newList() {
		if(applyDay == 0) {
			return new InctHuSyncList<InctHu>(LIST_DELIMETER);
		} else if (applyDay <= CookieDefRepository.APPLY_DAY_20171107_BEFORE) {
			return new InctHu20171107BeforeSyncList(LIST_DELIMETER);
		} else {
			return new InctHuSyncList<InctHu>(LIST_DELIMETER);
		}
	}
	
	@Override
	public int getExpire() {
		return  PropertyHandler.getInt("COOKIE_INCT_HU_EXPIRE"); // 2년과 6개월 혼재사용중 2년으로 설정
	}
	
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_HU_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_HU_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_HU_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_HU_BYTES"));
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
