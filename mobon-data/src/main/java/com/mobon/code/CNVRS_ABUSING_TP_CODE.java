package com.mobon.code;

public enum CNVRS_ABUSING_TP_CODE {

	//CONVERSION ABUSING
	
	SSN("01")//세션전환 어뷰징
	, DIRECT("02")//직접전환 어뷰징
	
	, INDIRECT("03")//간접전환 어뷰징
	, ONE_HUNDRED_MILLION("04")//1억원 어뷰징
	
	, SEC_5("05")//전환도달 5초 어뷰징			OK
	, SEC_10("06")//전환도달 10초 어뷰징 1원
	, UNDER_1MIN("07")//1분내 전환2건이상 어뷰징	OK
	
	, EXCEPT("08")//어뷰징 제외
	, PRICE1_SEC_10("09")//1원 전환도달 10초 어뷰징
	, ETC("99")//기타 어뷰징
	;

	private String value;

	CNVRS_ABUSING_TP_CODE(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
