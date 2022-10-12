package com.adgather.user.inclinations.cookieval.freq.ctr;

//import com.adgather.abtest.SampleBGroupTest;
import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqShops;
import com.adgather.user.inclinations.cookieval.inct.InctShops;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctShopsCtr;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.user.inclinations.etc.FreqCountResult;
import com.adgather.user.inclinations.etc.FreqInfo;
import com.adgather.util.PropertyHandler;
import com.adgather.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreqShopsCtr {
	/**
	 * 현재 배너만 적용되는 부분.
	 * 장바구니, 본상품 Defult 정책: 5회 무한
	 * 최종 배포일: 12월 11일
	 * SP 무한로직 추가!
	 */
	private static FreqInfo getDefaultFreqInfo(AdGubun adGubunObj, FreqInfo freqInfo) {
		switch(adGubunObj.getAdGubun()) {
			case GlobalConstants.CW :
				freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_BANNER_CW, PropertyHandler.getInt("CW_FREQUENCY", 40), CookieDefRepository.UNLIMITED_FREQ_BANNER_CW, PropertyHandler.getInt("UL_J_CW_FREQUENCY", 5));
				break;
			case GlobalConstants.SR :
				freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_BANNER_SR, PropertyHandler.getInt("SR_DEFAULT_FREQUENCY", 40), CookieDefRepository.UNLIMITED_FREQ_BANNER_SR, PropertyHandler.getInt("UL_J_SR_FREQUENCY", 5));
				break;
			case GlobalConstants.SP :
				freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_BANNER_SP, PropertyHandler.getInt("SP_DEFAULT_FREQUENCY", 40), CookieDefRepository.UNLIMITED_FREQ_BANNER_SP, PropertyHandler.getInt("UL_J_SP_FREQUENCY", 5));
				break;
		}
		return freqInfo;
	}
	/**
	 * 배너 프리퀀시 AB테스트 용
	 * 최종 배포일: 12월 11일
	 * 본상품 AB테스트 진행
	 * SR : 20회 내림
	 * SP : 40회 내림
	 */
	private static FreqInfo getDownFreqInfo(AdGubun adGubunObj, FreqInfo freqInfo) {
		switch(adGubunObj.getAdGubun()) {
			case GlobalConstants.SR :
				freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_BANNER_SR, PropertyHandler.getInt("L_SR_FREQUENCY", 20));
				break;
			case GlobalConstants.SP :
				freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_BANNER_SP, PropertyHandler.getInt("SP_DEFAULT_FREQUENCY", 40));
				break;
		}
		return freqInfo;
	}
	/**
	 *이 로직은 배너의 장바구니, 본상품, 추천상품에 대해서는 상관이 없다.
	 *정책이 배너의 장바구니, 본상품, 추천상품은 5회 무한이기때문에 .
	 *나머지 아이커버와 브랜드링크와 연관이 있다.
	 */
	public static FreqInfo getFreqInfo(AdGubun adGubunObj) {
		if(AdGubun.isNotValidate(adGubunObj))	return null;
		FreqInfo freqInfo = null;
		switch(adGubunObj.getAdGubun()) {
			case GlobalConstants.CW :
				if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
					if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_ICO_CW, PropertyHandler.getInt("CW_MICO_FREQUENCY", 5));
					} else {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_ICO_CW, PropertyHandler.getInt("CW_ICO_FREQUENCY", 5));
					}
				} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_SKY_CW, PropertyHandler.getInt("CW_SKY_FREQUENCY", 5));
				} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_PL_CW, PropertyHandler.getInt("CW_PL_FREQUENCY", 10));
				} else {		//adGubunObj.equalProduct(GlobalConstants.BANNER)
					freqInfo = new FreqInfo(CookieDefRepository.INCT_CW, CookieDefRepository.FREQ_BANNER_CW, PropertyHandler.getInt("CW_FREQUENCY", 40));
				}
				break;
			case GlobalConstants.SR :
				if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
					if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_ICO_SR, PropertyHandler.getInt("SR_MICO_FREQUENCY", 5));
					} else {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_ICO_SR, PropertyHandler.getInt("SR_ICO_FREQUENCY", 5));
					}
				} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_SKY_SR, PropertyHandler.getInt("SR_SKY_FREQUENCY", 5));
				} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_PL_SR, PropertyHandler.getInt("SR_PL_FREQUENCY", 10));
				} else {		//(adGubunObj.equalProduct(GlobalConstants.BANNER))
					freqInfo = new FreqInfo(CookieDefRepository.INCT_SR, CookieDefRepository.FREQ_BANNER_SR, PropertyHandler.getInt("SR_DEFAULT_FREQUENCY", 40));
				}
				break;
			case GlobalConstants.RC :
				if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
					if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_RC, CookieDefRepository.FREQ_ICO_RC, PropertyHandler.getInt("RC_MICO_FREQUENCY", 5));
					} else {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_RC, CookieDefRepository.FREQ_ICO_RC, PropertyHandler.getInt("RC_ICO_FREQUENCY", 5));
					}
				} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_RC, CookieDefRepository.FREQ_SKY_RC, PropertyHandler.getInt("RC_SKY_FREQUENCY", 5));
				} else if (adGubunObj.equalProduct(GlobalConstants.PLAY_LINK)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_RC, CookieDefRepository.FREQ_PL_RC, PropertyHandler.getInt("RC_PL_FREQUENCY", 10));
				} else {		// (adGubunObj.equalProduct(GlobalConstants.BANNER))
					freqInfo = new FreqInfo(CookieDefRepository.INCT_RC, CookieDefRepository.FREQ_BANNER_RC, PropertyHandler.getInt("RC_DEFAULT_FREQUENCY", 40));
				}
				break;
			case GlobalConstants.SP :
				if (adGubunObj.equalProduct(GlobalConstants.ICO)) {
					if(adGubunObj.equalPlatform(GlobalConstants.MOBILE)) {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_ICO_SP, PropertyHandler.getInt("SP_MICO_FREQUENCY", 5));
					} else {
						freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_ICO_SP, PropertyHandler.getInt("SP_ICO_FREQUENCY", 5));
					}
				} else if (adGubunObj.equalProduct(GlobalConstants.SKY)) {
					freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_SKY_SP, PropertyHandler.getInt("SP_SKY_FREQUENCY", 5));

				} else {		// (adGubunObj.equalProduct(GlobalConstants.BANNER))
					freqInfo = new FreqInfo(CookieDefRepository.INCT_SP, CookieDefRepository.FREQ_BANNER_SP, PropertyHandler.getInt("SP_DEFAULT_FREQUENCY", 40));
				}
				break;
		}
		return freqInfo;
	}
	/**
	 * 카카오 조건절에 대한 무조건 false 처리(기존 클래스 수정하지 않는 방법)
	 */
//	public static FreqInfo getFreqInfo(ConsumerInclinations inclinations, AdGubun adGubunObj) {
//		return getFreqInfo(inclinations, adGubunObj, true);
//	}
	/**
	 * 8월 21일 bymin : kakao true/false
	 * 카카오는 제외. 카카오는 무한프리퀀시
	 * 최종 배포일: 12월 11일
	 * CW : 무한
	 * SR : AB테스트(무한/20회)
	 * SP : 무한
	 */
//	public static FreqInfo getFreqInfo(ConsumerInclinations inclinations, AdGubun adGubunObj, boolean kakaoChk) {
//		if(AdGubun.isNotValidate(adGubunObj))	return null;
//		FreqInfo freqInfo = null;
//		if(inclinations == null)	return null;
//		String adGubun = adGubunObj.getAdGubun();
//		if("CW".equals(adGubun)) {
//			return getDefaultFreqInfo(adGubunObj, freqInfo); //배너 기존 5회 로직(장바구니, 본상품)
//		}else if("SR".equals(adGubun)) {
//			if(PropertyHandler.getBoolean("FREQUENCY_SR_TEST_ACTIVE",false) && kakaoChk) {
////				String type = SampleBGroupTest.getTestGubun(inclinations, "FREQUENCY_SR_TEST");
////				if("A".equals(type)) { // 기존 5회 무한 로직
////					return getDefaultFreqInfo(adGubunObj, freqInfo);
////				}else { // B 테스트 - 본상품 20회 내림
////					return getDownFreqInfo(adGubunObj, freqInfo);
////				}
//			}else {
//				return getDefaultFreqInfo(adGubunObj, freqInfo);
//			}
//		}else { //SP
//			if(PropertyHandler.getBoolean("SP_FREQUENCY_UNLIMITED_USE",false)) {
//				return getDefaultFreqInfo(adGubunObj, freqInfo); // 5회 무한로직
//			}else {
//				return getDownFreqInfo(adGubunObj, freqInfo); // 40회 내림
//			}
//		}
//	}
	/**
	 * 아이커버, 브랜드링크에서 사용
     * 배너 RC에서 사용
	 */
	public static FreqCountResult count(ConsumerInclinations inclinations
			, AdGubun adGubunObj
			, InctShops inctShops
			, FreqInfo freqInfo
			, Map<String, Integer> freqMap
			, int addCnt) {
		FreqCountResult result = new FreqCountResult();

		if(inclinations == null)	return result;
		if(adGubunObj == null)		return result;
		if(inctShops == null)		return result;
		if(freqInfo == null)		return result;

		FreqShops freqShops = new FreqShops();
		freqShops.setSiteCode(inctShops.getSiteCode());
		freqShops.setpCode(inctShops.getpCode());
		freqShops.setFreqCnt(addCnt);

		int nextFreq = 0;
		if(freqMap != null) {	// 프리퀀시 최대 값 확인
			Integer nowFreq = freqMap.get(freqShops.getKey());
			if(nowFreq != null) {
				nextFreq += nowFreq;
			}
		}

		nextFreq += addCnt;
		result.setCounted(true);
		result.setFreqCnt(nextFreq);

		if(nextFreq >= freqInfo.getMaxFreq()) {		// 최대 프리퀀시인 경우(프리퀀시 삭제, inctShops에 프리퀀시 만료 적용)
			String newTg = InctShopsCtr.setTgOfEndFreq(inclinations, freqInfo.getInctCookieName(), inctShops, adGubunObj.getProduct());		// 종료 테그 적용
			inclinations.delCookie(freqInfo.getFreqCookieName(), freqShops);

			result.setOver(true);
			result.putInfo("tg", newTg);
			return result;
		}

		inclinations.addCookie(freqInfo.getFreqCookieName(), freqShops, true);
		return result;
	}
	public static Map<String, Integer> getFreqMap(ConsumerInclinations inclinations, AdGubun adGubunObj, FreqInfo freqInfo) {
		return getFreqMap(inclinations, adGubunObj, freqInfo, false);
	}
	public static Map<String, Integer> getFreqMap(ConsumerInclinations inclinations, AdGubun adGubunObj, FreqInfo freqInfo, boolean kakaoChk) {
		if(inclinations == null)	return null;
		if(adGubunObj == null)		return null;
		if(freqInfo == null)		return null;

		List<FreqShops> freqShopList = null;
		if (kakaoChk) {
			freqShopList = inclinations.getCookie(freqInfo.getUnlimitedFreqCookieName());
		} else {
			freqShopList = inclinations.getCookie(freqInfo.getFreqCookieName());
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(freqShopList == null)	return map;

		for(FreqShops freqShops : freqShopList) {
			map.put(freqShops.getKey(), freqShops.getFreqCnt());
		}
		return map;
	}
	/** general **********************************************************************************************/
	private FreqShopsCtr() {}

	public static Map<String, Integer> getUnlimitedFreqMap(ConsumerInclinations inclinations, AdGubun adGubunObj, FreqInfo freqInfo) {
		if(inclinations == null)	return null;
		if(adGubunObj == null)		return null;
		if(freqInfo == null)		return null;

		List<FreqShops> freqShopList = inclinations.getCookie(freqInfo.getUnlimitedFreqCookieName());
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(freqShopList == null)	return map;

		for(FreqShops freqShops : freqShopList) {
			map.put(freqShops.getKey(), freqShops.getFreqCnt());
		}
		return map;
	}
	/**
	 * 4웛 13일 수정내용
	 * 최종 배포일: 12월 11일
	 * 1) 배너 CW, SR, SP에 대해서 타는 로직
	 */
	public static FreqCountResult count(ConsumerInclinations inclinations
			, AdGubun adGubunObj
			, InctShops inctShop
			, FreqInfo freqInfo
			, Map<String, Integer> freqMap
			, Map<String, Integer> unlimitedFreqMap
			, int addCnt){
		FreqCountResult result = new FreqCountResult();

		if(inclinations == null)	return result;
		if(adGubunObj == null) 		return result;
		if(inctShop == null) 		return result;
		if(freqInfo == null) 		return result;

		FreqShops freqShops = new FreqShops();
		freqShops.setSiteCode(inctShop.getSiteCode());
		freqShops.setpCode(inctShop.getpCode());
		freqShops.setFreqCnt(addCnt);

		int nextFreq = 0;
		if(freqMap != null) {	// 프리퀀시 최대 값 확인
			Integer nowFreq = freqMap.get(freqShops.getKey());
			if(nowFreq != null) {
				nextFreq += nowFreq;
			}
		}
		nextFreq += addCnt;
		result.setCounted(true);
		result.setFreqCnt(nextFreq);

		int nextUnlimitedFreq = 0;
		if(unlimitedFreqMap != null) {	// unlimited 프리퀀시 최대 값 확인
			Integer nowFreq = unlimitedFreqMap.get(freqShops.getKey());
			if(nowFreq != null) {
				nextUnlimitedFreq += nowFreq;
			}
		}
		nextUnlimitedFreq += addCnt;
		result.setUnlimitedFreqCnt(nextUnlimitedFreq);

		String unlimitedFreqCookieName = freqInfo.getUnlimitedFreqCookieName();
		if (StringUtils.isNotEmpty(unlimitedFreqCookieName)){
			inclinations.addCookie(unlimitedFreqCookieName, freqShops, true); // add in memory
		}
		// 장바구니, 본상품, 추천상품에 대해서 이제 nextUnlimitedFreq >= freqInfo.getMaxUnlimitedFreq() 이것이 의미없어짐
		// nextUnlimitedFreq 가 기존에는 5가되면 다시 초기화되어 1부터 시작되었는데, 이 값이 이제 999까지 간다. 통계위해서..
		String shopLogCookieName = InctShopsCtr.getCookieName(inctShop);
		if(nextUnlimitedFreq % 5 == 0) { //default는 5회무한
			inctShop.setCycleEndBanner(true);
			result.putInfo("cycleTg", true);
			inclinations.modCookie(shopLogCookieName, inctShop, true);
		}
		//999회 이상이면 ulfbsr, ulfbcw, ulfbsp 쿠키값을 삭제한다.
		int maxFrequency = PropertyHandler.getInt("STATS_FREQUENCY",999);
		if(nextUnlimitedFreq >= maxFrequency) { //999가 되었을때 쿠키 삭제
			inclinations.delCookie(freqInfo.getUnlimitedFreqCookieName(), freqShops);
		}
		return result;
	}
	/**
	 * 프리퀀시 초기화
	 * @param inclinations
	 * @param maxFreqShopLogs
	 * @param adGubunObj
	 */
	public static void initUnlimitedFreqCookies(ConsumerInclinations inclinations, List<InctShops> maxFreqShopLogs, AdGubun adGubunObj) {
		if(inclinations == null)	return;
		if(CollectionUtils.isEmpty(maxFreqShopLogs))	return;
		if (adGubunObj == null) return;

//		String adGubun = adGubunObj.getAdGubun();

		List<FreqShops> freqShopList = InctShopsCtr.getFreqShops(inclinations, adGubunObj);

		// for 문안에서 method를 호출하지 않고, 빈값일 경우에 cycle값만 초기화한다.
		boolean freqEmpty = false;
		if (CollectionUtils.isEmpty(freqShopList)) {
			freqEmpty = true;
		}
		for (InctShops inctShop : maxFreqShopLogs) {
			if (inctShop.isCycleEndBanner()) {
				inctShop.setCycleEndBanner(false);
				String shopLogCookieName = InctShopsCtr.getCookieName(inctShop);
				inclinations.modCookie(shopLogCookieName, inctShop, true);
			}
			if (freqEmpty) {
				continue;
			} else {
				FreqShops freqShop = new FreqShops();
				freqShop.setSiteCode(inctShop.getSiteCode());
				freqShop.setpCode(inctShop.getpCode());
			}
		}
	}
}