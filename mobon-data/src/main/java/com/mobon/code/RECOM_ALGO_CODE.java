package com.mobon.code;

public enum RECOM_ALGO_CODE {

	ETC("00")
	, CW_trgt("01")
	, RC_trgt("02")
	, SR_trgt("03")
	, CR_3or2click_top30("04")	// 인기재
	, LS_1click_top30("05")		// 랜덤상품
	, CT_3or2click2_top30("06")	// 카테고리 인기재
	, RP_pastLogic_nonXbox("07")// 
	, SC_other_shopRandom("08")	// 
	
	, HS_crsc_dummy("09")		// 유저기반 추천
	, HS_crsc("10")				// 상품기반 같은카테고리
	, HS_croc("11")				// 상품기반 다른카테고리
	, HS_pnks("12")				// 키워드추천
	, HS_crsc_ai("21")
	, HS_sssc("22")				//상품기반추천 같은카테고리 통계
	, HS_sssc_ai("23")
	, HS_croc_ai("24")
	, HS_ssoc("25")
	, HS_ssoc_ai("26")
	
	, SM_crsc_dummy("13")		// 유저기반 추천
	, SM_crsc("14")				// 상품기반 같은카테고리
	, SM_croc("15")				// 상품기반 다른카테고리
	, SM_pnks("16")				// 키워드추천
	
	, SM_vtDummy("17")			// 전환탑
	, SM_ar("18")				// 보완재 추천AR
	, SS_sm("19")
	, SSnSM_st("20")
	, SM_crsc_ai("27")
	, SM_sssc("28")
	, SM_sssc_ai("29")
	, SM_croc_ai("30")
	, SM_ssoc("31")
	, SM_ssoc_ai("32")

	, AR("33")
	, VT("34")
	, CC("35")
	, GA("36")
	;

	private String value;

	RECOM_ALGO_CODE(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
