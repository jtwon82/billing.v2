package com.adgather.user.inclinations;

import org.apache.commons.lang3.StringUtils;

import com.adgather.constants.GlobalConstants;
//import com.adgather.servlet.ConfigServlet;
import com.adgather.util.PropertyHandler;

/**
 * 소비자 성향 기능 제어자
 *   - 20180119 yhlim 몽고저장 여부 프로퍼티 이동(각 CookieDef에 설정, 여려 CookieDef에 설정하는 부분은 기능 제어자에 남겨둠) 
 * @date 2017. 6. 28.
 * @param 
 * @exception
 * @see
*/
public class CIFunctionController {
	private CIFunctionController() {}
	
	/** 소비자 정보 몽고 사용 여부(몽고사용 여부 최우선 기능) **/
	public static boolean isNotUseMongoStorage(String devicePrefix) {
		// 최후의 순간 몽고 사용하지 않음.
//		if(ConfigServlet.allStopShoppul) {
//			return true;
//		}
		
		// 소비자 성향 데이터 몽고 사용을 안하는 경우
		if(!PropertyHandler.getProperty("USER_LOG_STORAGE").equals(GlobalConstants.MONGODB)) {
			return true;
		}
		
		return PropertyHandler.containString("MONGO_USER_LOG_BLOCK_DEVICES", devicePrefix);		
	}

	public static boolean isNotWriteMongo() {
		return PropertyHandler.isFalse("MONGO_ENABLE_WRITE");
	}
	
	/** 샵로그 쿠키 변환 여부 **/
	public static boolean isRefactingShops() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_SHOP_LOG");				// 샵로그 재설정 처리 (프로퍼티 미설정)
	}
	
	/** 샵로그정보 몽고 사용 여부(부분 기능) **/
	public static boolean isUseMongoShops() {
		return PropertyHandler.isTrue("MONGO_SHOP_LOG_ENABLED");
	}
	
	/** 카카오에 전달할 샵로그 개수 **/
	public static int getKakaoShopsCnt() {
		return PropertyHandler.getInt("KAKAO_INCT_SHOPS_COUNT", 10);
	}
	public static float getKakaoPerCw() {
		return PropertyHandler.getFloat("KAKAO_INCT_SHOPS_PER_CW", 0.25f);	// 퍼센트
	}
	
	/** 방문로그 쿠키 변환 여부 **/
	public static boolean isRefactingInctHu() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_IC_HU");	
	}

	/** 방문로그 몽고 사용여부  **/
	public static boolean isUseMongoInctHu() {
		return PropertyHandler.isTrue("MONGO_IC_HU_ENABLED");
	}
	
	/** domains 쿠키 변환 여부 **/
	public static boolean isRefactingInctUm() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_IC_UM");
	}
	
	/** domains 쿠키 변환 여부 **/
	public static boolean isRefactingInctKl() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_IC_KL");	
	}

	/** 나이 몽고 쿠키변환 여부 **/
	public static boolean isRefactingAge() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_AGE");
	}
	
	/** 성별 몽고 사용여부 **/
	public static boolean isRefactingGender() {
		return PropertyHandler.isTrue("COOKIE_REFACTING_GENDER");
	}
	
	public static boolean isRefactFormat() {
		return PropertyHandler.isTrue("COOKIE_SYNC_REFACT_FORMAT");
	}
	
	public static boolean isUseMongFreqMs(int mediaScriptNo) {
		if(mediaScriptNo <= 0) 	return false;
		
		return PropertyHandler.contain("MONGO_FREQ_ENABLE_MS", ",", String.valueOf(mediaScriptNo));
	}
	
	public static boolean isUseMongFreqMsGubun(String adGubun) {
		if(StringUtils.isEmpty(adGubun))	return false;
		
		return PropertyHandler.contain("MONGO_FREQ_ENABLE_MS_ADGUBUN", ",", adGubun);
	}
	
	public static boolean isAddUpdDateOfDomain() {
		return PropertyHandler.isTrue("COOKIE_DOMAIN_ADD_UPDDATE");
	}

	public static boolean isUseMongoExcConvAd() {
		return PropertyHandler.isTrue("MONGO_EXC_CONV_AD_ENABLED");
	}
	public static boolean isUseMongoExcConvSc() {
		return PropertyHandler.isTrue("MONGO_EXC_CONV_SC_ENABLED");
	}
}
