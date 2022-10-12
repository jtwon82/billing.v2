package com.adgather.user.inclinations.cookieval.freq.ctr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqAdver;
import com.adgather.user.inclinations.cookieval.inct.ctr.CommonCtr;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.util.PropertyHandler;

public class FreqAdverClickCtr extends CommonCtr {
	/** static **********************************************************************************************/
	
	private static boolean isAdverFreqId(String adverId)  {
		if(StringUtils.isEmpty(adverId)) 	return false;
		
		return PropertyHandler.contain("ADVER_FREQ_CLICK_IDS", ",", adverId);
	}
	
	/** 광고주 id를 직접적으로 사용하지 않기 위함 **/
	private static String getHashCodeOfAdverId(String adverId) {
		if(StringUtils.isEmpty(adverId))	return "";
		
		return String.valueOf(Math.abs(adverId.hashCode()));
	}

	public static void count(ConsumerInclinations inclinations
			, String userId) {
		
		if(inclinations == null)	return;
		if(StringUtils.isEmpty(userId))	return;
		if(!isAdverFreqId(userId))	return;
		
		String hashCode = getHashCodeOfAdverId(userId);
		String keyName = hashCode;																	// 광고/상품구분 적용 제외
		FreqAdver freq = new FreqAdver();
		freq.setKeyName(keyName);
		freq.setFreqCnt(1);
		freq.setUpdDate(getUpdDate());
		inclinations.addCookie(CookieDefRepository.FREQ_ADVER_CLICK, freq, true);
	}
	
	public static boolean isOver(AdGubun adGubunObj, String adverId, Map<String, FreqAdver> freqMap, long nowTime) {
		if(adGubunObj == null)					return false;
		if(StringUtils.isEmpty(adverId))		return false;
		if(!isAdverFreqId(adverId))				return false;

		if(freqMap == null)		return false;
		
		
		String hashCode = getHashCodeOfAdverId(adverId);
		String keyName = hashCode;																	// 광고/상품구분 적용 제외
		
		FreqAdver freqAdver = freqMap.get(keyName);
		if(freqAdver == null)		return false;
		
		Date nowDate =  new Date(nowTime);
		Date updDate = toDate(freqAdver.getUpdDate(), nowDate);		// 광고주 프리퀀시의 시간(없을 경우 현재시간)
		if(DateUtils.isSameDay(nowDate, updDate))	return true;  // 시간 확인
		
		return false;										//시간 처리반 적용
	}
	
	public static Map<String, FreqAdver> getFreqMap(ConsumerInclinations inclinations) {
		if(inclinations == null)	return null;
		
		List<FreqAdver> freqList = inclinations.getCookie(CookieDefRepository.FREQ_ADVER_CLICK);
		Map<String, FreqAdver> map = new HashMap<String, FreqAdver>();
		if(freqList == null)	return map;
		
		for(FreqAdver freq : freqList) {
			map.put(freq.getKey(), freq);
		}
		return map;
	}

	/** general **********************************************************************************************/
	private FreqAdverClickCtr() {}
}
