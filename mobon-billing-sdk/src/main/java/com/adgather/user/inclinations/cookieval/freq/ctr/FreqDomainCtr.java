package com.adgather.user.inclinations.cookieval.freq.ctr;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqDomain;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.user.inclinations.etc.FreqCountResult;
import com.adgather.user.inclinations.etc.FreqInfo;
import com.adgather.util.PropertyHandler;
import org.apache.commons.lang3.StringUtils;
//import com.adgather.abtest.SampleCGroupTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreqDomainCtr {
	/** static **********************************************************************************************/
	public static FreqInfo getFreqInfo(AdGubun adGubunObj) {
		if(AdGubun.isNotValidate(adGubunObj))	return null;
		
		FreqInfo freqInfo = null;
		switch(adGubunObj.getAdGubun()) {
		case GlobalConstants.HU :
			if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_ICO_HU, PropertyHandler.getInt("HU_ICO_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_SKY_HU, PropertyHandler.getInt("HU_SKY_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_PL_HU, PropertyHandler.getInt("HU_PL_FREQUENCY", 2));
			} else {	// (adGubunObj.equalProduct(GlobalConstants.BANNER))
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_BANNER_HU, PropertyHandler.getInt("HU_FREQUENCY", 40));
			}	
			break;
		case GlobalConstants.RM :
			if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_ICO_RM, PropertyHandler.getInt("RM_ICO_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_SKY_RM, PropertyHandler.getInt("RM_SKY_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_PL_RM, PropertyHandler.getInt("RM_PL_FREQUENCY", 4));
			} else {	// (adGubunObj.equalProduct(GlobalConstants.BANNER))
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_BANNER_RM, PropertyHandler.getInt("RM_FREQUENCY", 40));
			}
			break;
		case GlobalConstants.UM :
			if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_ICO_UM, PropertyHandler.getInt("UM_ICO_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_SKY_UM, PropertyHandler.getInt("UM_SKY_FREQUENCY", 5));
			} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_PL_UM, PropertyHandler.getInt("UM_PL_FREQUENCY", 999));
			} else {	// (adGubunObj.equalProduct(GlobalConstants.BANNER))
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_BANNER_UM, PropertyHandler.getInt("UM_FREQUENCY", 40));
			}
			break;
		case GlobalConstants.AU :
			if (adGubunObj.equalProduct(GlobalConstants.BANNER)) {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_ADC, CookieDefRepository.FREQ_BANNER_AU, PropertyHandler.getInt("AU_FREQUENCY", 5));
			}
			break;
		}
		return freqInfo;
	}
	/**
	 * HU/RM AB테스트를 위해 분기로직태움
	 * A : 기존 프리퀀시
	 * B/C : abtest 로직
	 * 최초배포: 2019년 1월 29일
	 */
//	public static FreqInfo getFreqInfo(ConsumerInclinations inclinations, AdGubun adGubunObj) {
//		if(AdGubun.isNotValidate(adGubunObj))	return null;
//		FreqInfo freqInfo = null;
//		if(inclinations == null)	return null;
//		//String adGubun = adGubunObj.getAdGubun();
////		String type = SampleCGroupTest.getTestGubun(inclinations, "FREQUENCY_ABC_TEST");
////		if("B".equals(type)) { 
////			return getBtestFreqInfo(adGubunObj, freqInfo);
////		}else if("C".equals(type)) { 
////			return getCtestFreqInfo(adGubunObj, freqInfo);
////		}else { //A
////			return getFreqInfo(adGubunObj); 
////		}
//	}
	/**
	 * B테스트용
	 * 서비스 제한: HU, RM의 배너
	 * 최종 배포일: 2019년 1월 29일
	 * 서비스내용: Web, Mobile 구분됨
	 * cookie expire time : 한달(nFBHu/nFBRm)
	 * Web - HU - Btest : 15회
	 * Web - RM - Btest : 20회
	 * Mobile - HU - Btest : 12회
	 * Mobile - RM - Btest : 15회
	 * 최초배포: 2019년 1월 29일
	 */
	private static FreqInfo getBtestFreqInfo(AdGubun adGubunObj, FreqInfo freqInfo) {
		switch(adGubunObj.getAdGubun()) {
		case GlobalConstants.HU :
			if(PropertyHandler.getBoolean("FREQUENCY_ABC_TEST_ACTIVE",false)) {
				if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.NORMAL_FREQ_BANNER_HU, PropertyHandler.getInt("HU_M_B_FREQUENCY", 12));
				}else {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.NORMAL_FREQ_BANNER_HU, PropertyHandler.getInt("HU_W_B_FREQUENCY", 15));
				}
			} else {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_BANNER_HU, PropertyHandler.getInt("HU_FREQUENCY", 40));
			}
			break;
		case GlobalConstants.RM :
			if(PropertyHandler.getBoolean("FREQUENCY_ABC_TEST_ACTIVE",false)) {
				if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.NORMAL_FREQ_BANNER_RM, PropertyHandler.getInt("RM_M_B_FREQUENCY", 15));
				} else {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.NORMAL_FREQ_BANNER_RM, PropertyHandler.getInt("RM_W_B_FREQUENCY", 20));
				}
			} else {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_BANNER_RM, PropertyHandler.getInt("RM_FREQUENCY", 40));
			}
			break;
		}
		return freqInfo;
	}
	/**
	 * C테스트용
	 * 서비스 제한: HU, RM의 배너
	 * 최종 배포일: 2019년 1월 29일
	 * 서비스내용: Web, Mobile 구분됨
	 * cookie expire time : 한달
	 * Web - HU - Ctest : 20회
	 * Web - RM - Ctest : 30회
	 * Mobile - HU - Ctest : 17회
	 * Mobile - RM - Ctest : 25회
	 * 최초배포: 2019년 1월 29일
	 */
	private static FreqInfo getCtestFreqInfo(AdGubun adGubunObj, FreqInfo freqInfo) {
		switch(adGubunObj.getAdGubun()) {
		case GlobalConstants.HU :
			if(PropertyHandler.getBoolean("FREQUENCY_ABC_TEST_ACTIVE",false)) {
				if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {//20회
					freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.NORMAL_FREQ_BANNER_HU, PropertyHandler.getInt("HU_M_C_FREQUENCY", 17));
				}else {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.NORMAL_FREQ_BANNER_HU, PropertyHandler.getInt("HU_W_C_FREQUENCY", 20));
				}
			} else {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_HU, CookieDefRepository.FREQ_BANNER_HU, PropertyHandler.getInt("HU_FREQUENCY", 40));
			}
			break;
		case GlobalConstants.RM :
			if(PropertyHandler.getBoolean("FREQUENCY_ABC_TEST_ACTIVE",false)) {
				if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.NORMAL_FREQ_BANNER_RM, PropertyHandler.getInt("RM_M_C_FREQUENCY", 25));
				} else {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.NORMAL_FREQ_BANNER_RM, PropertyHandler.getInt("RM_W_C_FREQUENCY", 30));
				}
			} else {
				freqInfo = new FreqInfo(CookieDefRepository.INCT_UM, CookieDefRepository.FREQ_BANNER_RM, PropertyHandler.getInt("RM_FREQUENCY", 40));
			}
			break;
		}
		return freqInfo;
	}
	public static FreqCountResult count(ConsumerInclinations inclinations
			, AdGubun adGubunObj
			, String inctDomain
			, FreqInfo freqInfo
			, Map<String, Integer> freqMap
			, int addCnt) {
		FreqCountResult result = new FreqCountResult();
		
		if(inclinations == null)	return result;
		if(adGubunObj == null)		return result;
		if(StringUtils.isEmpty(inctDomain))			return result;
		if(freqInfo == null)		return result;

		FreqDomain freq = new FreqDomain();
		freq.setDomain(inctDomain);
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
			result.putInfo("cycleTg", true);
		}
		inclinations.addCookie(freqInfo.getFreqCookieName(), freq, true);
		//프리퀀시가 끝난 도메인은 프리퀀시 정보 유지 한다.
		
		return result;
	}
	
	public static boolean isOver(String inctDomain, FreqInfo freqInfo, Map<String, Integer> freqMap) {
		if(StringUtils.isEmpty(inctDomain))		return false;
		if(freqInfo == null)	return false;
		if(freqMap == null)		return false;
		
		Integer freqCnt = freqMap.get(inctDomain);
		if(freqCnt == null)		return false;
		
		return freqCnt >= freqInfo.getMaxFreq();
	}
	
	public static Map<String, Integer> getFreqMap(ConsumerInclinations inclinations, AdGubun adGubunObj, FreqInfo freqInfo) {
		if(inclinations == null)	return null;
		if(adGubunObj == null)		return null;
		if(freqInfo == null)		return null;
		
		List<FreqDomain> freqDomainList = inclinations.getCookie(freqInfo.getFreqCookieName());
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(freqDomainList == null)	return map;
		
		for(FreqDomain freqDomain : freqDomainList) {
			map.put(freqDomain.getKey(), freqDomain.getFreqCnt());
		}
		return map;
	}
	
	
	/** general **********************************************************************************************/
	private FreqDomainCtr() {}
}
