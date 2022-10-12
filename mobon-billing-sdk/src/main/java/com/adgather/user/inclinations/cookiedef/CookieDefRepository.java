package com.adgather.user.inclinations.cookiedef;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.cookiedef.def.*;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.refact.RefactDefAge;
import com.adgather.user.inclinations.cookiedef.refact.RefactDefGender;
import com.adgather.user.inclinations.cookiedef.refact.RefactDefIcKl;
import com.adgather.user.inclinations.status.RefactStatus;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class CookieDefRepository {
	
	/** COOKIE DEF 적용 시기 ***************************************************************/
	private static final int APPLY_DAY_NOW = 0;			//신규사항
	public static final int APPLY_DAY_20171107_BEFORE = 20171107;
	
	
	/** 이전 COOKIE 정보 상수(삭제대상) *******************************************************************/
	public static final String SHOP_LOG = "shop_log";			// 샵로그
	public static final String IC_HU = "ic_hu";					// 방문횟수로그
	public static final String IC_UM = "ic_um";					// 방문횟수로그
	public static final String IC_KL = "ic_ki";					// 키워드로그
	public static final String AGE = "age";						// 나이
	public static final String GENDER = "gender";				// 성별
	
	
	/** COOKIE 정보 상수 *******************************************************************/
	public static final String INCT_CW = "iCw";			// 장바구니 샵로그
	public static final String INCT_SR = "iSr";			// 본상품 샵로그
	public static final String INCT_RC = "iRc";			// 리사이클 샵로그
	public static final String INCT_SP = "iSp";			// 추천 샵로그
	
	public static final String INCT_HU = "iHu";					// 헤비유저 도메인
	public static final String INCT_HU_CONV = "iHuConv";		// 헤비유저 컨버젼
	public static final String INCT_HU_SHORTCUT = "iHuShortCut";		// 헤비유저 바콘
	public static final String INCT_UM = "iUm";					// 방문 도메인
	public static final String INCT_KL = "iKl";					// 키워드
	public static final String INCT_KM = "iKm";					// 문맥유입키워드
	public static final String INCT_KC = "iKc";					// 문맥카테고리
	public static final String INCT_KCC = "iKcc";				// 문맥카테고리 5카운트이상 설정(Y/N)
	public static final String INCT_MR = "iMr";					// 문맥매칭 키워드 최대 5개
	public static final String INCT_MRF = "iMrf";				// 문맥 리타기팅 광고 노출 카운트(자체 프리퀀시 용도)
	public static final String INCT_TAG = "iTag";				// 테그
	public static final String INCT_CATE = "iCate";				// 카테
	public static final String INCT_HCATE = "iHCate";			// 헤비카테(최근순, api로 넘어오는 많이 이용한 카테고리 수집을 위함)
	public static final String INCT_INSTALL = "iInstall";		// 앱설치정보
	

	public static final String INCT_GENDER = "iGender2";		// 성별
	public static final String INCT_AGE = "iAge2";				// 나이
	public static final String INCT_ADC = "iAdc";				// 오디언스
	
	public static final String REAL_GENDER = "iGender";			// 성별
	public static final String REAL_AGE = "iAge";				// 나이

	public static final String FREQ_BANNER_CW = "fBCw";			// 일반베너 장바구니 프리퀀시
	public static final String FREQ_BANNER_SR = "fBSr";			// 일반베너 본상품 프리퀀시
	public static final String FREQ_BANNER_RC = "fBRc";			// 일반베너 리사이클 프리퀀시
	public static final String FREQ_BANNER_SP = "fBSp";			// 일반베너 추천 프리퀀시
	public static final String FREQ_BANNER_KL = "fBKl";			// 일반베너 키워드 프리퀀시
	public static final String FREQ_BANNER_HU = "fBHu";			// 일반베너 헤비유저 프리퀀시
	public static final String FREQ_BANNER_RM = "fBRm";			// 일반베너 리턴매칭 프리퀀시
	public static final String FREQ_BANNER_RR = "fBRr";			// 일반베너 리턴매칭 리사이클 프리퀀시
	public static final String FREQ_BANNER_UM = "fBUm";			// 일반베너 성향 프리퀀시
	public static final String FREQ_BANNER_AU = "fBAu";			// 일반베너 오디언스 프리퀀시
	
	public static final String FREQ_SKY_CW = "fSCw";			// 브랜드링크 장바구니 프리퀀시 
	public static final String FREQ_SKY_SR = "fSSr";			// 브랜드링크 본상품 프리퀀시
	public static final String FREQ_SKY_RC = "fSRc";			// 브랜드링크 리사이클 프리퀀시
	public static final String FREQ_SKY_SP = "fSSp";			// 브랜드링크 추천 프리퀀시
	public static final String FREQ_SKY_KL = "fSKl";			// 일반베너 키워드 프리퀀시
	public static final String FREQ_SKY_HU = "fSHu";			// 브랜드링크 헤비유저 프리퀀시
	public static final String FREQ_SKY_RM = "fSRm";			// 브랜드링크 리턴매칭 프리퀀시
	public static final String FREQ_SKY_RR = "fSRr";			// 브랜드링크 리턴매칭 리사이클 프리퀀시
	public static final String FREQ_SKY_UM = "fSUm";			// 브랜드링크 성향 프리퀀시

	public static final String FREQ_ICO_CW = "fICw";			// 아이커버 장바구니 프리퀀시
	public static final String FREQ_ICO_SR = "fISr";			// 아이커버 본상품 프리퀀시
	public static final String FREQ_ICO_RC = "fIRc";			// 아이커버 리사이클 프리퀀시
	public static final String FREQ_ICO_SP = "fISp";			// 아이커버 추천 프리퀀시
	public static final String FREQ_ICO_KL = "fIKl";			// 아이커버 추천 프리퀀시
	public static final String FREQ_ICO_HU = "fIHu";			// 아이커버 헤비유저 프리퀀시
	public static final String FREQ_ICO_RM = "fIRm";			// 아이커버 리턴매칭 프리퀀시
	public static final String FREQ_ICO_RR = "fIRr";			// 아이커버 리턴매칭 리사이클 프리퀀시
	public static final String FREQ_ICO_UM = "fIUm";			// 아이커버 성향 프리퀀시
	public static final String FREQ_ADVER_ICO = "fAdver_ico";			// 아이커버 광고주 노출 프리퀀시

	public static final String FREQ_PL_CW = "fPlCw"; // 플레이링크 장바구니 프리퀀시
	public static final String FREQ_PL_SR = "fPlSr"; // 플레이링크 본상품 프리퀀시
	public static final String FREQ_PL_RC = "fPlRc"; // 플레이링크 리사이클 프리퀀시
	public static final String FREQ_PL_HU = "fPlHu"; // 플레이링크 해비유저 프리퀀시
	public static final String FREQ_PL_RM = "fPlRm"; // 플레이링크 리턴매칭 프리퀀시
	public static final String FREQ_PL_UM = "fPlUm"; // 플레이링크 성향매칭 프리퀀시
	public static final String FREQ_PL_KL = "fPlKl"; // 플레이링크 키워드 프리퀀시


	public static final String ABTEST_TYPE = "abTestType";		// AB테스트 타입
	public static final String ABTEST = "abTest";		// AB테스트 타입

	public static final String UNLIMITED_FREQ_BANNER_CW = "ulFBCw";			// 일반배너 장바구니 무한 사이클 프리퀀시
	public static final String UNLIMITED_FREQ_BANNER_SR = "ulFBSr";			// 일반배너 본상품 무한 사이클 프리퀀시
	public static final String UNLIMITED_FREQ_BANNER_SP = "ulFBSp";			// 일반배너 추천상품 무한 사이클 프리퀀시
	public static final String NORMAL_FREQ_BANNER_HU = "nFBHu";				//HU 정상 프리퀀시(30분 초기화 없음)
	public static final String NORMAL_FREQ_BANNER_RM = "nFBRm";				//RM 정상 프리퀀시(30분 초기화 없음)

	public static final String NEAR_CODE = "NearCode";			// 지역타겟팅

	public static final String FREQ_ADVER_CLICK = "fAdverC";			// 광고주 프리퀀시 임시적인 것임(20180327 잡코리에 임시적용임; 전체적용 계획 없음.)
	public static final String FREQ_ADVER_VIEW = "fAdverV";					// 

	
	
	

	public static final String M_HU = "mHu";					// 앱 패키지 - 헤비유저 도메인
	public static final String M_RM = "mRm";					// 앱 패키지 - 리턴매칭
	public static final String M_UM = "mUm";					// 앱 패키지 - 성향
	public static final String M_ABTEST = "mABTest";			// 위메프 A/B 테스트(모바일 전용) 
	public static final String M_DSCK = "mDSCK";			    // DSCK 
	public static final String M_AT = "mAt";			    // DSCK

	public static final String EXC_CONV_AD = "excConvAd"; // 전환제외 광고주 및 시간
	public static final String EXC_CONV_SC = "excConvSc"; // 전환제외 캠패인 및 시간

	/** COOKIE 클래스 정보 *******************************************************************/
	protected static CookieDef defAbtestType = null;			// 선처리 cookie(AB테스트 용등 다른 쿠키를 체크하기 위한 용도.)
	private static Map<String, CookieDef> defMapOld = null;					// 일반처리 cookie
	private static Map<String, CookieDef> defMap = null;
	static {
		defAbtestType = new DefInfoAbTest(ABTEST_TYPE);
		defMapOld = _getDefMap(APPLY_DAY_20171107_BEFORE);		// 20171107일 이전 적용 사항
		defMap = _getDefMap(APPLY_DAY_NOW);						// 20171107일 이후 적용 사항
		
	}
	protected static Map<String, CookieDef> getCookieDefMap(boolean bAddUpdDateOfDomain) {
		if(!bAddUpdDateOfDomain) {
			return defMapOld;
		} else {
			return defMap;
		}
	}
	protected static CookieDef getCookieDef(String cookieName, boolean bAddUpdDateOfDomain) {
		return getCookieDefMap(bAddUpdDateOfDomain).get(cookieName);
	}
	
	
	
	/**
	 * 쿠키 정의 목록 설정
	 * @method _getDefMap
	 * @see
	 * @return Map<String,CookieDef>
	 */
	private static Map<String, CookieDef> _getDefMap(int applyDay) {

		/** 소비자 성향 정보 *****/
		Map<String, CookieDef> tempDefMap = new HashMap<String, CookieDef>();

		/**이전 쿠키 정의***********************************************************/
		//tempDefMap.put(SHOP_LOG, new RefactDefShopLog(SHOP_LOG, "shoplog"));
		
		//tempDefMap.put(IC_HU, new RefactDefIcHu(IC_HU, INCT_HU));
		
		//tempDefMap.put(IC_UM, new RefactDefIcUm(IC_UM, INCT_UM));		// 쿠키 명칭이 다른 경우 사용
		
		tempDefMap.put(IC_KL, new RefactDefIcKl(IC_KL, INCT_KL));
		tempDefMap.put(AGE, new RefactDefAge(AGE, INCT_AGE));
		tempDefMap.put(GENDER, new RefactDefGender(GENDER, INCT_GENDER));
		
		
		/**신규 쿠키 정의***********************************************************/
		tempDefMap.put(INCT_CW, new DefInctCw(INCT_CW));
		
		tempDefMap.put(INCT_SR, new DefInctSr(INCT_SR));
		
		tempDefMap.put(INCT_RC, new DefInctRc(INCT_RC));
		
		tempDefMap.put(INCT_SP, new DefInctSp(INCT_SP));

//8월2일 이후 배포
		tempDefMap.put(INCT_HU, new DefInctHu(INCT_HU, applyDay));
		tempDefMap.put(INCT_HU_CONV, new DefInctHuConv(INCT_HU_CONV));
		tempDefMap.put(INCT_HU_SHORTCUT, new DefInctHuShortCut(INCT_HU_SHORTCUT));
		
		tempDefMap.put(INCT_UM, new DefInctUm(INCT_UM, applyDay));
		tempDefMap.put(INCT_INSTALL, new DefInctInstall(INCT_INSTALL));
		
		tempDefMap.put(INCT_KL, new DefInctKl(INCT_KL));
		
		tempDefMap.put(INCT_KM, new DefInctKm(INCT_KM));

		tempDefMap.put(INCT_KC, new DefInctKc(INCT_KC));
		tempDefMap.put(INCT_KCC, new DefInctKcc(INCT_KCC));
		
		tempDefMap.put(INCT_MR, new DefInctMr(INCT_MR));
		
		tempDefMap.put(INCT_MRF, new DefInctMrf(INCT_MRF));

/* 순차적용 예정
		tempDefMap.put(INCT_TAG, new DefInctTag());
*/
		tempDefMap.put(INCT_CATE, new DefInctCate(INCT_CATE));
		

		tempDefMap.put(INCT_HCATE, new DefInctHCate(INCT_HCATE));

		tempDefMap.put(INCT_GENDER, new DefInctGender(INCT_GENDER));

		tempDefMap.put(INCT_AGE, new DefInctAge(INCT_AGE));
		
		tempDefMap.put(REAL_GENDER, new DefRealGender(REAL_GENDER));

		tempDefMap.put(REAL_AGE, new DefRealAge(REAL_AGE));	

		tempDefMap.put(FREQ_BANNER_CW, new DefFreqUnlimited(FREQ_BANNER_CW));			// 개수 제한 필요

		tempDefMap.put(FREQ_BANNER_SR, new DefFreqUnlimited(FREQ_BANNER_SR));
		
		tempDefMap.put(FREQ_BANNER_RC, new DefFreqShops(FREQ_BANNER_RC));
		
		tempDefMap.put(FREQ_BANNER_SP, new DefFreqShops(FREQ_BANNER_SP));
		
		//tempDefMap.put(FREQ_BANNER_KL, new DefFreqKl(FREQ_BANNER_KL));	// KL 무한 프리퀀시

		tempDefMap.put(FREQ_BANNER_HU, new DefFreqHu(FREQ_BANNER_HU));
		
		tempDefMap.put(FREQ_BANNER_RM, new DefFreqRm(FREQ_BANNER_RM));
		
		tempDefMap.put(FREQ_BANNER_RR, new DefFreqRm(FREQ_BANNER_RR));
		
		tempDefMap.put(FREQ_BANNER_UM, new DefFreqUm(FREQ_BANNER_UM));
		
		tempDefMap.put(FREQ_BANNER_AU, new DefFreqAu(FREQ_BANNER_AU));

		tempDefMap.put(FREQ_SKY_CW, new DefFreqShops(FREQ_SKY_CW));
		
		tempDefMap.put(FREQ_SKY_SR, new DefFreqShops(FREQ_SKY_SR));
		
		tempDefMap.put(FREQ_SKY_RC, new DefFreqShops(FREQ_SKY_RC));
		
		tempDefMap.put(FREQ_SKY_SP, new DefFreqShops(FREQ_SKY_SP));
		
		//tempDefMap.put(FREQ_SKY_KL, new DefFreqKl(FREQ_SKY_KL));	// KL 무한 프리퀀시
		
		tempDefMap.put(FREQ_SKY_HU, new DefFreqHu(FREQ_SKY_HU));
		
		tempDefMap.put(FREQ_SKY_RM, new DefFreqRm(FREQ_SKY_RM));
		
		tempDefMap.put(FREQ_SKY_RR, new DefFreqRm(FREQ_SKY_RR));
		
		tempDefMap.put(FREQ_SKY_UM, new DefFreqUm(FREQ_SKY_UM));

		tempDefMap.put(FREQ_ICO_CW, new DefFreqShops(FREQ_ICO_CW));
		
		tempDefMap.put(FREQ_ICO_SR, new DefFreqShops(FREQ_ICO_SR));
		
		tempDefMap.put(FREQ_ICO_RC, new DefFreqShops(FREQ_ICO_RC));
		
		tempDefMap.put(FREQ_ICO_SP, new DefFreqShops(FREQ_ICO_SP));
		
		//tempDefMap.put(FREQ_ICO_KL, new DefFreqKl(FREQ_ICO_KL));	// KL 무한 프리퀀시
		
		tempDefMap.put(FREQ_ICO_HU, new DefFreqHu(FREQ_ICO_HU));
		
		tempDefMap.put(FREQ_ICO_RM, new DefFreqRm(FREQ_ICO_RM));
		
		tempDefMap.put(FREQ_ICO_RR, new DefFreqRm(FREQ_ICO_RR));
		
		tempDefMap.put(FREQ_ICO_UM, new DefFreqUm(FREQ_ICO_UM));

		tempDefMap.put(FREQ_PL_CW, new DefFreqShops(FREQ_PL_CW));

		tempDefMap.put(FREQ_PL_SR, new DefFreqShops(FREQ_PL_SR));

		tempDefMap.put(FREQ_PL_RC, new DefFreqShops(FREQ_PL_RC));

		tempDefMap.put(FREQ_PL_HU, new DefFreqHu(FREQ_PL_HU));

		tempDefMap.put(FREQ_PL_RM, new DefFreqRm(FREQ_PL_RM));

		tempDefMap.put(FREQ_PL_UM, new DefFreqUm(FREQ_PL_UM));

		tempDefMap.put(FREQ_ADVER_ICO, new DefFreqAdverIco(FREQ_ADVER_ICO));  //아이커버 광고주 노출 프리퀀시

		tempDefMap.put(UNLIMITED_FREQ_BANNER_CW, new DefFreqUnlimited(UNLIMITED_FREQ_BANNER_CW));	//장바구니 무한 프리퀀시
		tempDefMap.put(UNLIMITED_FREQ_BANNER_SR, new DefFreqUnlimited(UNLIMITED_FREQ_BANNER_SR));	//본상품 무한 프리퀀시
		tempDefMap.put(UNLIMITED_FREQ_BANNER_SP, new DefFreqUnlimited(UNLIMITED_FREQ_BANNER_SP));	//추천상품 무한 프리퀀시

		tempDefMap.put(NORMAL_FREQ_BANNER_HU, new DefFreqHuNormal(NORMAL_FREQ_BANNER_HU));	//HU AB테스트용 20190129~
		tempDefMap.put(NORMAL_FREQ_BANNER_RM, new DefFreqRmNormal(NORMAL_FREQ_BANNER_RM));	//RM AB테스트용 20190129~
		
		tempDefMap.put(NEAR_CODE, new DefNearCode(NEAR_CODE));
		
		tempDefMap.put(FREQ_ADVER_CLICK, new DefFreqAdverClick(FREQ_ADVER_CLICK));
		tempDefMap.put(FREQ_ADVER_VIEW, new DefFreqAdverView(FREQ_ADVER_VIEW));
		
		
		
		tempDefMap.put(M_HU, new DefMInctHU(M_HU));
		tempDefMap.put(M_RM, new DefMInctRM(M_RM));
		tempDefMap.put(M_UM, new DefMInctUM(M_UM));
		tempDefMap.put(M_ABTEST, new DefInfoMAbTest(M_ABTEST));
		tempDefMap.put(M_DSCK, new DefInfoMDSCK(M_DSCK));
		tempDefMap.put(M_AT, new DefMInctAT(M_AT));

		tempDefMap.put(EXC_CONV_AD, new DefExcConvAd(EXC_CONV_AD));
		tempDefMap.put(EXC_CONV_SC, new DefExcConvSc(EXC_CONV_SC));
		
		tempDefMap.put(INCT_ADC, new DefInctAdc(INCT_ADC));

		return Collections.unmodifiableMap(tempDefMap);
	}
	
	
	/** 변환전 샵로그 쿠키 명칭 확인  **/
	public static String getRefactingCookieName(String cookieName) {
		if(StringUtils.isEmpty(cookieName))		return null;
		
		// 명칭이 변경되지 않으면 이전 명칭 이용
		String resCookieName = cookieName;
		
		if(!CIFunctionController.isRefactingShops()) {
			resCookieName = getLoadShopsCookieName(resCookieName);
		}
		
		return resCookieName;
	}
	
	/** 변환전 샵로그 쿠키 명칭 확인(AB테스트 포함)  **/
	public static String getLoadCookieNameNAbTest(String cookieName, RefactStatus refactStaus) {
		if(StringUtils.isEmpty(cookieName))		return null;
		
		// 명칭이 변경되지 않으면 이전 명칭 이용
		String resCookieName = cookieName;
		
		if(refactStaus == null)				return resCookieName;

		// cookieName in (iCw, iSr, iRc, iSp)이며 샵로그 변환 이전 이면 shop_log 쿠키 이용
		if((INCT_CW.equals(cookieName) 
			|| INCT_SR.equals(cookieName) 
			|| INCT_RC.equals(cookieName) 
			|| INCT_SP.equals(cookieName) ) && !refactStaus.isRefacting(SHOP_LOG) ) {		// 변환 전
			resCookieName = getLoadShopsCookieName(resCookieName);
		
		} else if (INCT_UM.equals(cookieName) && !refactStaus.isRefacting(IC_UM)) {
			resCookieName = IC_UM;
		
		} else if (INCT_HU.equals(cookieName) && !refactStaus.isRefacting(IC_HU)) {
			resCookieName = IC_HU;
			
		} /*else if (INCT_KL.equals(cookieName) && !refactStaus.isRefacting(IC_KL)) {
			resCookieName = IC_KL;
		} */
		
		//기타 cookieName 변환 기능 설정

		return resCookieName;
	}
	
	
	private static String getLoadShopsCookieName(String cookieName) {
		String resCookieName = cookieName;
		switch (cookieName) {
		case INCT_CW:
		case INCT_SR:
		case INCT_RC:
		case INCT_SP:
			resCookieName = SHOP_LOG;
			break;
		}
		return resCookieName;
	}
}
