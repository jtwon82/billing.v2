package com.adgather.user.inclinations.cookieval.freq.ctr;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqKl;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.user.inclinations.etc.FreqCountResult;
import com.adgather.user.inclinations.etc.FreqInfo;
import com.adgather.util.PropertyHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreqKlCtr {
	/** static **********************************************************************************************/
	public static FreqInfo getFreqInfo(AdGubun adGubunObj) {
		if(AdGubun.isNotValidate(adGubunObj))	return null;
		
		FreqInfo freqInfo = null;
		switch(adGubunObj.getAdGubun()) {
		case GlobalConstants.KL :
			if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_KL, CookieDefRepository.FREQ_ICO_KL, PropertyHandler.getInt("KL_ICO_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_KL, CookieDefRepository.FREQ_SKY_KL, PropertyHandler.getInt("KL_SKY_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_KL, CookieDefRepository.FREQ_PL_KL, PropertyHandler.getInt("KL_PL_FREQUENCY", 999));
			} else {	// (adGubunObj.equalProduct(GlobalConstants.BANNER))
				freqInfo = new FreqInfo(CookieDefRepository.INCT_KL, CookieDefRepository.FREQ_BANNER_KL, PropertyHandler.getInt("KL_FREQUENCY", 40));
			}
			break;
		}
		
		return freqInfo;
	}
	
	public static FreqCountResult count(ConsumerInclinations inclinations
			, AdGubun adGubunObj
			, String keyword
			, FreqInfo freqInfo
			, Map<String, Integer> freqMap
			, int addCnt) {
		FreqCountResult result = new FreqCountResult();
		
		if(inclinations == null)	return result;
		if(adGubunObj == null)		return result;
		if(StringUtils.isEmpty(keyword))			return result;
		if(freqInfo == null)		return result;

		FreqKl freq = new FreqKl();
		freq.setKeyword(keyword);
		freq.setFreqCnt(addCnt);

		int nextFreq = 0;
		if (freqMap != null) {
			Integer nowFreq = freqMap.get(freq.getKey());
			if (nowFreq != null) {
				nextFreq += nowFreq;
			}
		}

		nextFreq += addCnt;
		result.setCounted(true);
		result.setFreqCnt(nextFreq);

		if (nextFreq >= freqInfo.getMaxFreq()) {
			result.setOver(true);
		}

		inclinations.addCookie(freqInfo.getFreqCookieName(), freq, true);
		
		//프리퀀시가 끝난 도메인은 프리퀀시 정보 유지 한다.
		return result;
	}
	
	public static boolean isOver(String keyword, FreqInfo freqInfo, Map<String, Integer> freqMap) {
		if(StringUtils.isEmpty(keyword))		return false;
		if(freqInfo == null)	return false;
		if(freqMap == null)		return false;
		
		Integer freqCnt = freqMap.get(keyword);
		if(freqCnt == null)		return false;
		
		return freqCnt >= freqInfo.getMaxFreq();
	}
	
	public static Map<String, Integer> getFreqMap(ConsumerInclinations inclinations, AdGubun adGubunObj, FreqInfo freqInfo) {
		if(inclinations == null)	return null;
		if(adGubunObj == null)		return null;
		if(freqInfo == null)		return null;
		
		List<FreqKl> freqKlList = inclinations.getCookie(freqInfo.getFreqCookieName());
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(freqKlList == null)	return map;
		
		for(FreqKl freqKl : freqKlList) {
			map.put(freqKl.getKey(), freqKl.getFreqCnt());
		}
		return map;
	}
	
	
	/** general **********************************************************************************************/
	private FreqKlCtr() {}
}
