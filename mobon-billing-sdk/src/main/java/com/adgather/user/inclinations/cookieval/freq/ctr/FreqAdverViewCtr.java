package com.adgather.user.inclinations.cookieval.freq.ctr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqAdver;
import com.adgather.user.inclinations.cookieval.inct.ctr.CommonCtr;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.util.PropertyHandler;

public class FreqAdverViewCtr extends CommonCtr {
	/** static **********************************************************************************************/
	private static final int FREQ_UNDEFIND = 0; // 프리퀀시 제약없음
	private static int getAdverFreq(String adverId) {
		if(StringUtils.isEmpty(adverId)) 	return FREQ_UNDEFIND;
		
		String adverIdSerchKey = adverId + "^";
		String adverFreq = PropertyHandler.getContainString("ADVER_FREQ_VIEW_IDS", ",", adverIdSerchKey);
		
		if(StringUtils.isEmpty(adverFreq) || adverFreq.indexOf("^") < 0 )	return FREQ_UNDEFIND;
		
		String strSeq = adverFreq.substring(adverIdSerchKey.length());
		return NumberUtils.toInt(strSeq, FREQ_UNDEFIND);
	}
	
	/** 광고주 id를 직접적으로 사용하지 않기 위함 **/
	private static String getHashCodeOfAdverId(String adverId) {
		if(StringUtils.isEmpty(adverId))	return "";
		
		return String.valueOf(Math.abs(adverId.hashCode()));
	}

	public static boolean count(ConsumerInclinations inclinations
			, AdGubun adGubunObj
			, String adverId
			, Map<String, FreqAdver> freqMap
			, long nowTime) {
		
		if(inclinations == null)	return false;
		if(adGubunObj == null)		return false;
		if(StringUtils.isEmpty(adverId))	return false;
		int maxFreq = getAdverFreq(adverId);
		if(maxFreq <= FREQ_UNDEFIND)	return false;
		if(freqMap == null)	return false;
		
		String keyName =  getHashCodeOfAdverId(adverId)+adGubunObj.getProduct()+adGubunObj.getAdGubun();
		FreqAdver nowFreq = freqMap.get(keyName);
		
		Date nowDate =  new Date(nowTime);
		Date updDate = nowFreq == null ? nowDate : toDate(nowFreq.getUpdDate(), nowDate);		// 광고주 프리퀀시의 시간(없을 경우 현재시간)
		if(!DateUtils.isSameDay(updDate, nowDate)) {
			FreqAdver freq = new FreqAdver();
			freq.setKeyName(keyName);
			freq.setFreqCnt(1);
			freq.setUpdDate(getUpdDate());
			inclinations.addCookie(CookieDefRepository.FREQ_ADVER_VIEW, freq, false);
			
		} else if(nowFreq != null && nowFreq.getFreqCnt() >= maxFreq) {
			return false;
			
		} else {
			FreqAdver freq = new FreqAdver();
			freq.setKeyName(keyName);
			freq.setFreqCnt(1);
			freq.setUpdDate(getUpdDate());
			inclinations.addCookie(CookieDefRepository.FREQ_ADVER_VIEW, freq, true);
		}
		return true;
	}
	
	public static boolean isOver(AdGubun adGubunObj, String adverId, Map<String, FreqAdver> freqMap, long nowTime) {
		if(adGubunObj == null)				return false;
		if(StringUtils.isEmpty(adverId))	return false;
		int maxFreq = getAdverFreq(adverId);
		if(maxFreq <= FREQ_UNDEFIND)		return false;
		if(MapUtils.isEmpty(freqMap))		return false;
		
		String keyName = getHashCodeOfAdverId(adverId)+adGubunObj.getProduct()+adGubunObj.getAdGubun();
		FreqAdver nowFreq = freqMap.get(keyName);
		
		Date nowDate =  new Date(nowTime);
		Date updDate = nowFreq == null ? nowDate : toDate(nowFreq.getUpdDate(), nowDate);		// 광고주 프리퀀시의 시간(없을 경우 현재시간)
		
		if(!DateUtils.isSameDay(updDate, nowDate))	return false;  // 시간 확인
		
		return nowFreq != null && nowFreq.getFreqCnt() >= maxFreq;
	}
	
	public static Map<String, FreqAdver> getFreqMap(ConsumerInclinations inclinations) {
		if(inclinations == null)	return null;
		
		List<FreqAdver> freqList = inclinations.getCookie(CookieDefRepository.FREQ_ADVER_VIEW);
		Map<String, FreqAdver> map = new HashMap<String, FreqAdver>();
		if(freqList == null)	return map;
		
		for(FreqAdver freq : freqList) {
			map.put(freq.getKey(), freq);
		}
		return map;
	}

	/** general **********************************************************************************************/
	private FreqAdverViewCtr() {}
}
