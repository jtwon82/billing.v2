package com.mobon.billing.report.disk.json;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * @author 
 *
 */
public class AppDevice {

	private String TRML_SEQ; // INT UNSIGNED NOT NULL COMMENT '단말기순서|단말기 순서, 자동증가||', -- 단말기순서
	private String TRML_UNQ_VAL; // VARCHAR(50)  NOT NULL COMMENT '단말기고유값|단말기의 고유값||', -- 단말기고유값
	private String OS_TP_CODE; // VARCHAR(2)   NOT NULL COMMENT 'OS구분코드|OS구분코드|MOBON_COM_CODE.OS_TP_CODE|', -- OS구분코드
	private String REG_USER_ID; // VARCHAR(30)  NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|', -- 등록사용자ID
	private String REG_DTTM; // DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||', -- 등록일자
	private String ALT_USER_ID; //  VARCHAR(30)  NULL     COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|', -- 수정사용자ID
	private String ALT_DTTM; //DATETIME     NULL     COMMENT '수정일자|수정시간을 기록||' -- 수정일자
	
	/* 파라미터 추가 */
	private String model;
	private String osv;
	private String carrier;
	private String ua;
	
	JSONObject parameter;
	
	
	public Map getParameter() {
		return parameter;
	}
	public void setParameter(JSONObject parameter) {
		this.parameter = parameter;
	}
	public String getTRML_SEQ() {
		return TRML_SEQ;
	}
	public void setTRML_SEQ(String tRML_SEQ) {
		TRML_SEQ = tRML_SEQ;
	}
	public String getTRML_UNQ_VAL() {
		return TRML_UNQ_VAL;
	}
	public void setTRML_UNQ_VAL(String tRML_UNQ_VAL) {
		TRML_UNQ_VAL = tRML_UNQ_VAL;
	}
	public String getOS_TP_CODE() {
		return ((OS_TP_CODE != null && OS_TP_CODE.length() >= 0) ? OS_TP_CODE : "ao").toLowerCase();
	}
	public void setOS_TP_CODE(String oS_TP_CODE) {
		OS_TP_CODE = oS_TP_CODE;
	}
	public String getREG_USER_ID() {
		return REG_USER_ID;
	}
	public void setREG_USER_ID(String rEG_USER_ID) {
		REG_USER_ID = rEG_USER_ID;
	}
	public String getREG_DTTM() {
		return REG_DTTM;
	}
	public void setREG_DTTM(String rEG_DTTM) {
		REG_DTTM = rEG_DTTM;
	}
	public String getALT_USER_ID() {
		return ALT_USER_ID;
	}
	public void setALT_USER_ID(String aLT_USER_ID) {
		ALT_USER_ID = aLT_USER_ID;
	}
	public String getALT_DTTM() {
		return ALT_DTTM;
	}
	public void setALT_DTTM(String aLT_DTTM) {
		ALT_DTTM = aLT_DTTM;
	}
	
	public String toString() {
		return new StringBuffer()
		.append("")
		.toString();
	}
	
	/** 
	 * 2018/04/13 
	 * 파라미터 추가
	 * 요청은 받지만, 데이터를 수집하지 않음.
	 *  
	 */
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOsv() {
		return osv;
	}
	public void setOsv(String osv) {
		this.osv = osv;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getUa() {
		return ua;
	}
	public void setUa(String ua) {
		this.ua = ua;
	}
	
	public Map toMap() {
		return new HashMap();
	}
}
