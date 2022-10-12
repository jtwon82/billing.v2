package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqAdverIco;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;
import com.adgather.util.StringUtils;

import java.util.List;

/**
 * 아이커버 프리퀀시(광고주) 쿠키 정의
 * @author ytkim
 *
 */
public class DefFreqAdverIco extends CookieDef {

	public DefFreqAdverIco(String userid) {
		super(userid);
	}

	/** CookieDef 구현 ********************************************/
	/** 빈 객체 생성  **/
	@Override
	public Object newObj() {
		return new FreqAdverIco();
	}

	/** 빈 리스트 생성 **/
	@Override
	public SyncList<?> newList() {
		return new ObjectLSyncList<FreqAdverIco>(LIST_DELIMETER);
	}

	/** 쿠키의 expire time **/
	@Override
	public int getExpire() {
		return PropertyHandler.getInt("COOKIE_FREQ_ADVER_ICO_EXPIRE");
	}

	/** 쿠키 사용여부(기본 저장) **/
	@Override
	public boolean isUseCookie() {
		return true;
	}

	/** 쿠키의 개수(몽고 개수 설정 포함) **/
	@Override
	public void infuseLimit(MemoryObj<?> memoryObj) {
		memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_FREQ_ADVER_ICO_COUNT"));
		memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_FREQ_ADVER_ICO_BYTES"));
		memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_FREQ_ADVER_ICO_COUNT"));
		memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_FREQ_ADVER_ICO_BYTES"));
	}

	/** 고정형태 여부(길이가 고정되어 있고, 위치값에 특정 의미를 담는 경우) **/
	@Override
	public boolean isFixePose() {
		return false;
	}

	/** 몽고 사용여부 **/
	@Override
	public boolean isUseMongo() {
		return PropertyHandler.isTrue("MONGO_FREQ_ADVER_ICO_ENABLE");
	}

	/** 몽고 사용여부 **/
	@Override
	public boolean isUseMongo(int mediaScriptNo) {
		return isUseMongo();
	}

	/** 암복호화 처리 **/
	@Override
	public CodeConverter getCodeConverter() {
		return null;
	}

	/** 기간만료 삭제 처리 **/
	public static void removeIcoFreqLog(ConsumerInclinations inclinations) {
		if(inclinations == null)	return;

		List<FreqAdverIco> list = inclinations.getCookie(CookieDefRepository.FREQ_ADVER_ICO);
		if(list == null)				return;
		for (int idx = list.size()-1; idx >= 0; idx--) {
			FreqAdverIco freqAdverIco = list.get(idx);
			if(isNeedDelete(freqAdverIco)) {
				inclinations.delCookie(CookieDefRepository.FREQ_ADVER_ICO, freqAdverIco);
			}
		}
	}

	/** 기간만료 필터 **/
	public static boolean isNeedDelete(FreqAdverIco freqAdverIco) {
		boolean needDelete = false;
		if(freqAdverIco == null)		needDelete = true;
		if(StringUtils.isEmpty(freqAdverIco.getExpireTime()))	needDelete = false;
		else {
			if (Long.parseLong(freqAdverIco.getExpireTime()) < System.currentTimeMillis()) {
				needDelete = true;
			}
		}
		return needDelete;
	}
}
