package com.mobon.code.constant.old;

public interface CodeConstants {

  public static final String LANDING_CODE = "MBIL_TP_CODE";
  public static final String LANDING_CODE_MOBILE_WEB_URL = "01";
  public static final String LANDING_CODE_ANDROID = "02";
  public static final String LANDING_CODE_IOS = "03";
  
  public static final String PRODUCT_CODE = "ADVRTS_PRDT_CODE";
  public static final String PRODUCT_CODE_BANNER = "01"; // 배너
  public static final String PRODUCT_CODE_SKY = "02"; // 브랜드링크
  public static final String PRODUCT_CODE_ICO = "03"; // 아이커버
  public static final String PRODUCT_CODE_SHOWCON = "04"; // 쇼콘
  public static final String PRODUCT_CODE_MM = "05"; // 문맥
  public static final String PRODUCT_CODE_PL = "06"; // 플레이링크
  
  public static final String CONVERSION_CODE = "01";
  public static final String DRC_CODE = "02";
  public static final String SETCHARGE_CODE = "03";
  public static final String DRC_SHOPCON_CODE = "04";
  public static final String DRC_SKY = "05";
  String DRC_PL = "06";
  
  // 매체 상품구분 타입
  /**
   * 매체 상품화배너
   */
  public static final String MEDIA_PRODUCT = "01";
  /**
   * 매체 고정화배너
   */
  public static final String MEDIA_NON_PRODUCT = "02";
  /**
   * 매체 고정,상품화배너
   */
  public static final String MEDIA_ALL_PRODUCT = "03";
  
  // 과금 코드
  /**
   * 정상 과금
   */
  public static final String CHARGE_SUCCESS_NORMAL_CODE = "01";
  /**
   * 프리퀀시 무효클릭
   */
  public static final String CHARGE_FAIL_FREQUENCY_CODE = "91";
  /**
   * 라이브 off 무효클릭
   */
  public static final String CHARGE_FAIL_LIVEOFF_CODE = "92";
  
  /**
   * 광고수집구분코드
   */
  public static final String AD_SETUP_CODE = "adverSetupCode";
  /**
   * 웹
   */
  public static final String AD_SETUP_CODE_WEB = "01";
  /**
   * 모바일
   */
  public static final String AD_SETUP_CODE_MOBILE = "02";
  /**
   * 반응형 웹(웹/모바일)
   */
  public static final String AD_SETUP_CODE_RESPONSIVE_WEB = "03";
  /**
   * 수집중지
   */
  public static final String AD_SETUP_CODE_STOP_SHOPDATA = "04";
  
  /**
   * 플랫폼구분코드
   */
  public static final String PLTFOM_TP_CODE = "pltfomTpCode";
  public static final String PLTFOM_TP_CODE_WEB = "01"; // 웹
  public static final String PLTFOM_TP_CODE_MOBILE = "02"; // 모바일

  /**
   * 광고구분코드
   */
  public static final String ADVRTS_TP_CODE = "advrtsTpCode";
  public static final String ADVRTS_TP_CODE_AD = "01"; // 베이스(AD) 
  public static final String ADVRTS_TP_CODE_CA = "02"; // 할인금액쇼콘(CA)
  public static final String ADVRTS_TP_CODE_CC = "03"; // 이벤트타겟팅(CC)
  public static final String ADVRTS_TP_CODE_CW = "04"; // 장바구니(CW)
  public static final String ADVRTS_TP_CODE_HU = "05"; // 헤비유저(HU)
  public static final String ADVRTS_TP_CODE_KL = "06"; // 키워드(KL)
  public static final String ADVRTS_TP_CODE_KP = "07"; // 키워드상품(KP)
  public static final String ADVRTS_TP_CODE_PB = "08"; // 프리미엄배너(PB)
  public static final String ADVRTS_TP_CODE_PE = "09"; // 할인율쇼콘(PE)
  public static final String ADVRTS_TP_CODE_RC = "10"; // 리사이클(RC)
  public static final String ADVRTS_TP_CODE_RM = "11"; // 자사 타게팅(RM)
  public static final String ADVRTS_TP_CODE_RR = "12"; // RM리사이클(RR)
  public static final String ADVRTS_TP_CODE_SA = "13"; // 브랜드박스(SA)
  public static final String ADVRTS_TP_CODE_SH = "14"; // 브랜드박스 안 캐시백상품(SH)
  public static final String ADVRTS_TP_CODE_SJ = "15"; // 쇼핑입점(SJ)
  public static final String ADVRTS_TP_CODE_SP = "16"; // 추천(일반상품)(SP)
  public static final String ADVRTS_TP_CODE_SR = "17"; // 본상품(SR)
  public static final String ADVRTS_TP_CODE_ST = "18"; // 투데이베스트(ST)
  public static final String ADVRTS_TP_CODE_UM = "19"; // 성향(도메인)(UM)
  public static final String ADVRTS_TP_CODE_MM = "20"; // 문맥매칭
  public static final String ADVRTS_TP_CODE_KB = "21"; // 키워드볼트
  public static final String ADVRTS_TP_CODE_IB = "22"; // 유입키워드매칭

  String MEDIA = "media"; // 매체만 과금
  String AD = "ad"; // 광고주만 과금
  String ALL = "all"; // 매체와 광고주가 과금 시간이 같을때 (ex: 즉시, 광고주 과금시간 5초 : 매체 과금시간 5초)
}
