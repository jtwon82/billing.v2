package com.mobon.conversion.domain.old;

public interface ConversionCode {

  public static final int CONV_SESSION = 0;		// 세션
  public static final int CONV_DIRECT = 1;		// 직접
  public static final int CONV_INDIRECT = 2;	// 간접
  public static final int CONV_INVALID = 3;		//
  
  public static final String SERVICE_CODE = "01";
  public static final String SERVICE_CONVERSION_V2 = "10";
  public static final String SERVICE_CONVERSION_PCODE = "11";
  
  
  public static final String VALID_SESSION_CODE = "11";		// 정상 세션컨버전
  public static final String VALID_DIRECT_CODE = "12";		// 정상 직접컨버전
  public static final String VALID_INDIRECT_CODE = "13";	// 정상 간접컨버전
  
  public static final String INVALID_SESSION_CODE = "81";	// 세션컨버전 무효
  public static final String INVALID_DIRECT_CODE = "82";	// 직접컨버전 무효
  public static final String INVALID_INDIRECT_CODE = "83";	// 간접컨버전 무효
  public static final String INVALID_CONV_CODE = "84";		// 간접컨버전 무효
  public static final String INVALID_PRICE_OVER_CODE = "85";		// 금액 제한 무효
  
  
}
