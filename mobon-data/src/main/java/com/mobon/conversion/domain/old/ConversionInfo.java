package com.mobon.conversion.domain.old;

import com.adgather.lang.old.ObjectToString;

public class ConversionInfo extends ObjectToString {
	private String yyyymmdd;
	private int scriptNo;
	private String clientId;
	/**
	 * 01 : conversion 02 : drc 03 : setcharge
	 */
	private String serviceCode;
	/**
	 * 11 : 정상 세션 컨버전 매출 12 : 정상 직접 컨버전 매출 13 : 정상 간접 컨버전 매출 81 : 세션 컨버전 무효 처리 82 :
	 * 직접 컨버전 무효 처리 83 : 간접 컨버전 무효 처리
	 */
	private String chargeCode;
	private String userId;
	private String pcode;
	private String etc;
	private String orderCode;
	private int orderPrice;
	private String actionLogNo;

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getActionLogNo() {
		return actionLogNo;
	}

	public void setActionLogNo(String actionLogNo) {
		this.actionLogNo = actionLogNo;
	}

}
