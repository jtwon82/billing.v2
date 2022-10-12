package com.adgather.user.inclinations.cookieval.inct.ctr;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.freq.FreqShops;
import com.adgather.user.inclinations.cookieval.inct.InctShops;
import com.adgather.user.inclinations.cookieval.inter.Waitable;
import com.adgather.user.inclinations.etc.AdGubun;
import com.adgather.util.PropertyHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 샵로그(성향) 정보 제어
 * @author yhlim
 *
 */
public final class InctShopsCtr {
    protected final static Logger logger = LoggerFactory.getLogger(InctShopsCtr.class);

	/**
	 * 플레이링크 관련 구분자(상품구분별 관리 필요하지 않기에 1개의 구분자만 활용)
	 * 적용 대상 : CW, SR, RC
	 * 추천(SP)은 사용하지 않기에 추가하지 않음.
 	 */
	private static final String PL_GB = "P";

	//CW관련 코드
	private static final String BANNER_CW = "G";
	private static final String SKY_CW = "H";
	private static final String ICO_CW = "I";
//	public static final String CWGB = BANNER_CW + SKY_CW + ICO_CW;
	private static final char[] CWGB_CHARS = (BANNER_CW + SKY_CW + ICO_CW + PL_GB).toCharArray();

	//SR관련 코드
	private static final String BANNER_SR = "A";
	private static final String ICO_SR = "B";
	private static final String SKY_SR = "C";
//	private static final String SRGB = BANNER_SR + SKY_SR + ICO_SR;
//	private static final char[] SRGB_CHARS = (BANNER_SR + SKY_SR + ICO_SR).toCharArray();

	//RC관련 코드
	private static final String BANNER_RC = "D";
	private static final String ICO_RC = "E";
	private static final String SKY_RC = "F";
//	private static final String RCGB = BANNER_RC + SKY_RC + ICO_RC;
	private static final char[] RCGB_CHARS = (BANNER_RC + SKY_RC + ICO_RC + PL_GB).toCharArray();

	//SP관련 코드
	private static final String BANNER_SP = "A";
	private static final String ICO_SP = "B";
	private static final String SKY_SP = "C";
//	private static final String SPGB = BANNER_SP + SKY_SP + ICO_SP;
	private static final char[] SPGB_CHARS = (BANNER_SP + SKY_SP + ICO_SP).toCharArray();


	/** 샵로그 광고 구분 확인 **/
	public static boolean isCwShopLog(InctShops obj) {
		if(obj == null)		return false;
		
		return isCwShopLog(obj.getCwgb());
	}
	public static boolean isCwShopLog(String cwgb) {
		return StringUtils.containsAny(cwgb, CWGB_CHARS);
	}
	
	public static boolean isSrShopLog(InctShops obj) {
		if(obj == null)		return false;
		
		return isSrShopLog(obj.getMcgb(), obj.getCwgb());
	}
	public static boolean isSrShopLog(String mcgb, String cwgb) {
		return StringUtils.isEmpty(mcgb) && StringUtils.isEmpty(cwgb);					// null or "" 이면 SR
	}
	
	public static boolean isRcShopLog(InctShops obj) {
		if(obj == null)		return false;
		
		return  isRcShopLog(obj.getMcgb());
	}
	public static boolean isRcShopLog(String mcgb) {
		return  StringUtils.containsAny(mcgb, RCGB_CHARS);
	}
	
	public static boolean isSpShopLog(InctShops obj) {
		if(obj == null)		return false;
		
		return isSpShopLog(obj.getMcgb());
	}
	public static boolean isSpShopLog(String mcgb) {
		return  StringUtils.containsAny(mcgb, SPGB_CHARS);
	}

	/** 종료 여부 확인 **/
	public static boolean isEndFreqTg(InctShops obj, String adGubun, String productType) {
		if(obj == null)								return true;			// 종료 처리 함으로 해당 샵로그는 사용 못하도록 설정
		
		String checkTg = getTg(adGubun, productType);
		if(StringUtils.isEmpty(checkTg))		return true;
		if(obj.getTg() == null)					return false;
		return obj.getTg().contains(checkTg);
	}
	
	/** 샵로그 프리퀀시 종료 구분자 추가 **/
	public static String recreateTgOfEndFreq(InctShops obj, String productType) {
		if(obj == null)							return "";
		
		String endTg = getTg(productType, obj.getTg(), obj.getMcgb(), obj.getCwgb());
		if(StringUtils.isEmpty(endTg))		return obj.getTg();
		if(StringUtils.isEmpty(obj.getTg()))	return endTg;
		if(obj.getTg().contains(endTg))	return obj.getTg();
		
		return obj.getTg()+endTg;
	}
	public static String recreateTgOfEndFreq(String productType, String tg, String mcgb, String cwgb) {
		String endTg = getTg(productType, tg, mcgb, cwgb);
		if(StringUtils.isEmpty(endTg))		return tg;
		if(StringUtils.isEmpty(tg))			return endTg;
		if(tg.contains(endTg))				return tg;
		
		return tg+endTg;
	}
	
	/** 샵로그 구분자 확인 **/
	public static String getTg(String productType, String tg, String mcgb, String cwgb) {
		String adGubun = null;
		
		if (isCwShopLog(cwgb)) {
			adGubun = GlobalConstants.CW;
			
		} else if (isSrShopLog(mcgb, cwgb)) {
			adGubun = GlobalConstants.SR;
			
		} else if (isRcShopLog(mcgb)) {
			adGubun = GlobalConstants.RC;
			
		} else if (isSpShopLog(mcgb)) {
			adGubun = GlobalConstants.SP;
		}
		
		return getTg(adGubun, productType);
	}
	
	public static String getTg(String adGubun, String productType){

		if(GlobalConstants.CW.equals(adGubun)){
			// 1.확장 노출 값인 TG값 확인 - banner 인경우
			if(GlobalConstants.BANNER.equals(productType) || GlobalConstants.NATIVE.equals(productType))		return BANNER_CW;

			// 1.확장 노출 값인 TG값 확인 - ICOVER 인경우
			if(GlobalConstants.ICO.equals(productType))			return ICO_CW;
			
			// 1.확장 노출 값인 TG값 확인 - SKY 인경우
			if(GlobalConstants.SKY.equals(productType)
				|| GlobalConstants.PLAY_LINK_OLD.equals(productType))			return SKY_CW;

			// 1.확장 노출 값인 TG값 확인 - PLAYLINK
			if(GlobalConstants.PLAY_LINK.equals(productType)) return PL_GB;
			
		}else if(GlobalConstants.SR.equals(adGubun)){
			// 2.확장 노출 값인 TG값 확인 - banner 인경우 A
			if(GlobalConstants.BANNER.equals(productType) || GlobalConstants.NATIVE.equals(productType))		return BANNER_SR;

			// 2.확장 노출 값인 TG값 확인 - ICOVER 인경우 B
			if(GlobalConstants.ICO.equals(productType))			return ICO_SR;
						
			// 2.확장 노출 값인 TG값 확인 - SKY 인경우 C
			if(GlobalConstants.SKY.equals(productType)
				|| GlobalConstants.PLAY_LINK_OLD.equals(productType))			return SKY_SR;

			// 2.확장 노출 값인 TG값 확인 - PL 인경우
			if(GlobalConstants.PLAY_LINK.equals(productType))			return PL_GB;

		}else if(GlobalConstants.RC.equals(adGubun)){
			// 3.확장 노출 값인 TG값 확인 - banner 인경우 A
			if(GlobalConstants.BANNER.equals(productType))		return BANNER_RC;
			
			// 3.확장 노출 값인 TG값 확인 - SKY 인경우 C
			if(GlobalConstants.SKY.equals(productType)
				|| GlobalConstants.PLAY_LINK_OLD.equals(productType))			return SKY_RC;

			// 3.확장 노출 값인 TG값 확인 - ICOVER 인경우 B
			if(GlobalConstants.ICO.equals(productType))			return ICO_RC;

			if(GlobalConstants.PLAY_LINK.equals(productType))			return PL_GB;

		}else if(GlobalConstants.SP.equals(adGubun)){
			// 4.확장 노출 값인 TG값 확인 - banner 인경우 A
			if(GlobalConstants.BANNER.equals(productType))		return BANNER_SP;

			// 4.확장 노출 값인 TG값 확인 - ICOVER 인경우 B
			if(GlobalConstants.ICO.equals(productType))			return ICO_SP;
			
			// 4.확장 노출 값인 TG값 확인 - SKY 인경우 C
			if(GlobalConstants.SKY.equals(productType)
				|| GlobalConstants.PLAY_LINK_OLD.equals(productType))			return SKY_SP;
			
		}

		return "";
	}
	
	/** 수집일 처리 **/
	//기존 수집날짜 형식
//	private static final DateFormat FD_D = new SimpleDateFormat("yyyyMMdd");
	//신규 수집날짜 형식
//	private static final DateFormat FD_TD = new SimpleDateFormat("yyyyMMddHHmmss");
	
	//수집기한 확인(확인기간이 넘었는지 여부)
	public static boolean isOverTd(InctShops obj, int aliveDays) {
		if(aliveDays <=0)		return false;

		String td = obj.getTd();
		if(StringUtils.isEmpty(td))		return true;			// 수집일이 없는 경우 삭제
		
		if(td.length() > 8) {											// 시간까지 수집할 경우를 위해 절삭
			td = td.substring(0, 8);
		}
		
		Date date = null;
		try {
			DateFormat fd_d = new SimpleDateFormat("yyyyMMdd");
			date = fd_d.parse(td);
		} catch (Exception e) {
		    // 에러가 많이 나면, 아래 하단의 date 가 null 일때 true 로 바꿔 'G' tag를 묻히는 로직수정이 필요함.
			logger.error("Error td : {}, errorMessage : {}", td, e.getMessage() , e);
		}
		
		if(date == null)		return true;

		Date keepDate = DateUtils.addDays(date, aliveDays);
		if(keepDate == null)	return true;

		return keepDate.getTime() < System.currentTimeMillis(); 
	}
	
	/** 현재시간 수집일 생성 **/
	public static String createTd() {
		DateFormat fd_td = new SimpleDateFormat("yyyyMMddHHmmss");
		return fd_td.format(new Date());
	}
	
	/** 종료된 샵로그 삭제 처리 **/
	public static void removeEndShopLog(ConsumerInclinations inclinations, String adGubun) {
		if(inclinations == null)	return;
		if(adGubun == null)		return;
		
		String cookieName = getCookieName(adGubun);
		if(cookieName == null)	return;
		
		List<InctShops> list = inclinations.getCookie(cookieName);
		if(list == null)				return;
		for (int idx = list.size()-1; idx >= 0; idx--) {
			InctShops inctShopLog = list.get(idx);
			if(isNeedDelete(inctShopLog)) {
				inclinations.delCookie(cookieName, inctShopLog);
			}
		}
	}
	
	/** 종료된 샵로그 확인(저장 할 때 적용) **/
	public static boolean isNeedDelete(InctShops inctShopLog) {
		if(inctShopLog == null)		return true;
		if(StringUtils.isEmpty(inctShopLog.getTg()))		return false;
		
		if (StringUtils.isNotEmpty(inctShopLog.getCwgb()) && StringUtils.containsOnly(inctShopLog.getCwgb(), inctShopLog.getTg())) {
			// 장바구나 샵로그 삭제 시기 
			return true;
			
		} else if (StringUtils.isEmpty(inctShopLog.getMcgb()) && StringUtils.containsOnly(BANNER_SR+ICO_SR+SKY_SR+PL_GB, inctShopLog.getTg())) {
			// 본상품 샵로그 삭제 시기
			return true;
		
		} else if (StringUtils.isNotEmpty(inctShopLog.getMcgb()) && StringUtils.containsOnly(inctShopLog.getMcgb(), inctShopLog.getTg())) {
			// 기타 샵로그 삭제기시(mcgb와 tg가 같은 경우)
			return true;
		}
		
		return false;
	}

	/** 샵로그의 쿠키 명칭 확인 **/
	public static String getCookieName(InctShops obj) {
		if(obj == null) 	return null;
		if (isCwShopLog(obj)) {
			return CookieDefRepository.INCT_CW;
			
		} else if (isSrShopLog(obj)) {
			return CookieDefRepository.INCT_SR;
			
		} else if (isRcShopLog(obj)) {
			return CookieDefRepository.INCT_RC;
			
		} else if (isSpShopLog(obj)) {
			return CookieDefRepository.INCT_SP;
		}
		
		return null;
	}
	
	/** 광고구분의 쿠키 명칭 확인 **/
	public static String getCookieName(String adGubun) {
		if(StringUtils.isEmpty(adGubun))		return null;
		
		String cookieName = null;
		switch (adGubun) {
		case GlobalConstants.CW:
			cookieName = CookieDefRepository.INCT_CW;
			break;
		case GlobalConstants.SR:
			cookieName = CookieDefRepository.INCT_SR;
			break;
		case GlobalConstants.RC:
			cookieName = CookieDefRepository.INCT_RC;
			break;
		case GlobalConstants.SP:
			cookieName = CookieDefRepository.INCT_SP;
			break;
		}
		
		return cookieName;
	}

	/** 데이터 서비스 대기시간 생성  **/
	private static long _getWaitTime(long curTime) {
		int waitTime = 1000*60*PropertyHandler.getInt("MONGO_INCT_SHOPS_WAIT_TIME", 0);
		return  (waitTime > 0 ? curTime + waitTime : 0);
	}
	
	/** 데이터 서비스 대기시간 설정 **/
	public static boolean setWait(ConsumerInclinations inclinations, InctShops inctShopLog, String adGubun){
		if(true)					return false;		// 코드 이용중지
		if(inclinations == null)	return false;
		if(inctShopLog == null)		return false;
		
		String cookieName = getCookieName(adGubun);
		if(StringUtils.isEmpty(cookieName))	return false;
		
		Waitable.setWaitData(inctShopLog, _getWaitTime(inclinations.getCurTime()));
		inclinations.modCookie(cookieName, inctShopLog, true);
		return true;
	}
	
	/** 데이터 서비스 대기시간 값 삭제 **/
	public static boolean unsetWait(ConsumerInclinations inclinations, InctShops inctShopLog, String adGubun){
		if(true)					return false;		// 코드 이용중지
		if(inclinations == null)	return false;
		if(inctShopLog == null)		return false;
		
		String cookieName = getCookieName(adGubun);
		if(StringUtils.isEmpty(cookieName))	return false;
		
		Waitable.setWaitData(inctShopLog, 0);
		inclinations.modCookie(cookieName, inctShopLog, true);
		return true;
	}
	
	public static String setTgOfEndFreq(ConsumerInclinations inclinations, String inctCookieName, InctShops inctShops, String product) {
		if(inctShops == null) return null;
		if(StringUtils.isEmpty(inctCookieName)) return null;
		if(StringUtils.isEmpty(product)) return null;
		
		String newTg = recreateTgOfEndFreq(inctShops, product);
		inctShops.setTg(newTg);
		inclinations.modCookie(inctCookieName, inctShops, true);
		
		return newTg;
	}
	
	public static List<InctShops> getShopDatas(ConsumerInclinations inclinations, AdGubun adGubunObj) {
		if(inclinations == null)	return null;
		if(AdGubun.isNotValidate(adGubunObj))	return null;
		
		String cookieName = getCookieName(adGubunObj.getAdGubun());
		
		// sr shopLog로 그만 받아진다.
		List<InctShops> shopLogs = inclinations.getCookie(cookieName);
		if(shopLogs == null || shopLogs.size() == 0)		return null;
		
		// sr의 종료 테그를 확인
		String tg = InctShopsCtr.getTg(adGubunObj.getAdGubun(), adGubunObj.getProduct());
		if(tg == null)					return null;
		
		// 프리퀀시 종료 처리 된 것은 제거(copy본에만 반영됨.)
		for (int idx = shopLogs.size() -1; idx >= 0 ; idx--) {
			if (InctShopsCtr.isEndFreqTg(shopLogs.get(idx), adGubunObj.getAdGubun(), adGubunObj.getProduct())) {
				shopLogs.remove(idx);				// 검색용 데이터만 삭제(실 쿠키는 유지됨.)
			}
		}
		return shopLogs;
	}
	public static List<InctShops> getUnlimitedFreqShopDatas(ConsumerInclinations inclinations, AdGubun adGubunObj) {
		if(inclinations == null)	return null;
		if(AdGubun.isNotValidate(adGubunObj))	return null;

		String cookieName = getCookieName(adGubunObj.getAdGubun());

		return inclinations.getCookie(cookieName);
	}
	
	/** 무한프리퀀시의 경우  max frequency가 넘었을 경우 shoplog상에서 제거하고 
	 * 그 목록을 메모리에 담아두었다가 유효한 상품이 없을 경우 max frequency cookie 값을
	 * 삭제하고 max frequency 대상 상품에 대해서 유효성 체크를 하도록 구성함. **/
	public static List<InctShops> removeMaxUnlimitedFreqShopLog(ConsumerInclinations inclinations, AdGubun adGubunObj, List<InctShops> shopLogs) {
		if(inclinations == null) {
			return null;
		}
		if(AdGubun.isNotValidate(adGubunObj)) {
			return null;
		}
		
		// 프리퀀시가 비어있더라도 cycleEndBanner 가 true 인 경우에는 상품을 초기화 시킴.
		for (int idx = shopLogs.size() -1; idx >= 0 ; idx--) {
			InctShops inctShop = shopLogs.get(idx);			
			if (inctShop.isCycleEndBanner()) {
				shopLogs.remove(idx);
				continue;
			}
		}

		List<FreqShops> freqShopList = getFreqShops(inclinations, adGubunObj);
		if (CollectionUtils.isEmpty(freqShopList)) {			
			return shopLogs;
		}
		
		int fre = PropertyHandler.getInt("STATS_FREQUENCY",999);

		for (int idx = shopLogs.size() -1; idx >= 0 ; idx--) {
			InctShops inctShop = shopLogs.get(idx);
			for (FreqShops freqShop : freqShopList) {
				if (freqShop.getSiteCode().equals(inctShop.getSiteCode()) && freqShop.getpCode().equals(inctShop.getpCode())) {
					if (freqShop.getFreqCnt() >= fre) {
						shopLogs.remove(idx);
						break;
					}
					break;
				}
			}
		}
		return shopLogs;
	}

	public static List<FreqShops> getFreqShops(ConsumerInclinations inclinations, AdGubun adGubunObj) {
		List<FreqShops> freqShopList = null;
		switch (adGubunObj.getAdGubun()) {
			case GlobalConstants.CW:
				if(adGubunObj.equalProduct(GlobalConstants.BANNER)) {
					freqShopList = inclinations.getCookie(CookieDefRepository.UNLIMITED_FREQ_BANNER_CW); // 일반배너 CW 무한 사이클 프리퀀시
				}
				break;
			case GlobalConstants.SR:
				if(adGubunObj.equalProduct(GlobalConstants.BANNER)) {
					freqShopList = inclinations.getCookie(CookieDefRepository.UNLIMITED_FREQ_BANNER_SR); // 일반배너 SR 무한 사이클 프리퀀시
				}
				break;
			case GlobalConstants.SP:
				if(adGubunObj.equalProduct(GlobalConstants.BANNER)) {
					freqShopList = inclinations.getCookie(CookieDefRepository.UNLIMITED_FREQ_BANNER_SP); // 일반배너 SP 무한 사이클 프리퀀시
				}
				break;
			default:
				break;

		}
		return freqShopList;
	}
	private InctShopsCtr() {}
}
