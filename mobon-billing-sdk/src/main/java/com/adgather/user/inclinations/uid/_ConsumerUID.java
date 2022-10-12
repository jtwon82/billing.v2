package com.adgather.user.inclinations.uid;

public class _ConsumerUID {
	/** static values ***********************************/
	public static final char TYPE_IP_INFO = 'I';		// IP로구성된 UID이용
	public static final char TYPE_AU_ID = 'A';			// Java VM ID나 ADTRUTH ID를 이용한 UID이용

	/** values ****************************************/
	private char uidType = TYPE_IP_INFO;				// 사용하는 키 유형(기본 ipInfo 설정)
	private String ipInfo;										// IP로 구성된 키(브라우저 단위; IP + RANDOMVALUE)
	private String auId;											// Java VM ID나 ADTRUTH ID (크로스 브라우저 단위)

	/** create method **********************************/
	public _ConsumerUID() {}

	/** value get/set method **********************************/
	public char getUidType() {
		return uidType;
	}
	public void setUidType(char uidType) {
		this.uidType = uidType;
	}
	public String getIpInfo() {
		return ipInfo;
	}
	public void setIpInfo(String ipInfo) {
		this.ipInfo = ipInfo;
	}
	public String getAuId() {
		return auId;
	}
	public void setAuId(String auId) {
		this.auId = auId;
	}

	/** etc method  *****************************************/
	/** 사용자 고유 아이디 (ipInfo/auId 둘중 하나, keyType에 의해 결정) **/
	public String getConsumerUID() {
		return uidType == TYPE_AU_ID ? auId : ipInfo;
	}
}
