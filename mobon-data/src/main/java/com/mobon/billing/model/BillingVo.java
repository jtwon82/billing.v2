package com.mobon.billing.model;

public class BillingVo {
	
	private String className="";
	private String keyCode="";
	private String grouping=""; // 그룹핑
	private String sendDate="";	// 전송날짜
	private String hh=""; // 시간
	private String yyyymmdd=""; // 날짜

	private String platform=""; // 웹 모바일 구분
	private String adGubun=""; // 광고구분
	private String product=""; // 광고상품
	private String svcTpCode="";
	
	private String advertiserId=""; // 광고주아이디
	private String siteCode=""; // 캠페인코드
	private String scriptUserId=""; // 매체스크립트 아이디
	private int scriptNo=0; // 스크립트코드 (s와 동일)
	
	private long shopDataNo = 0;
	private long mobShopDataNo = 0;
	
	
	public long getShopDataNo() {
		return shopDataNo;
	}
	public void setShopDataNo(long shopDataNo) {
		this.shopDataNo = shopDataNo;
	}
	public long getMobShopDataNo() {
		return mobShopDataNo;
	}
	public void setMobShopDataNo(long mobShopDataNo) {
		this.mobShopDataNo = mobShopDataNo;
	}
}