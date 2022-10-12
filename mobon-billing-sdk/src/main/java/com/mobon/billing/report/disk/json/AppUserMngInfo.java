package com.mobon.billing.report.disk.json;

import java.util.Map;

import net.sf.json.JSONObject;

public class AppUserMngInfo {
	
	private String TRML_SEQ;			// INT UNSIGNED NOT NULL COMMENT '단말기순서|단말기의 고유번호|APP_USER_TRML_INFO.TRML_SEQ|', -- 단말기순서
	private String APP_SEQ;           	//INT UNSIGNED NOT NULL COMMENT '앱순서|앱의 고유번호|APP_INFO.APP_SEQ|', -- 앱순서
	private String APP_UPD_DT;			//DATE         NOT NULL COMMENT '앱업데이트일|앱의 업데이트 일자||', -- 앱업데이트일
	private int MMNY_APP_CNT = 0;		//INTEGER      NOT NULL DEFAULT 0 COMMENT '자사앱횟수|몽고DB에 자사앱 타게팅으로 선택된 수, 기본값 0||', -- 자사앱횟수
	private int MMNY_APP_CLICK_CNT = 0;	//INTEGER      NOT NULL DEFAULT 0 COMMENT '자사앱클릭횟수|자사앱 타게팅 광고에서 받은 클릭 수, 기본값 0||', -- 자사앱클릭횟수
	private int UN_INSTL_CNT = 0;		//INTEGER      NOT NULL DEFAULT 0 COMMENT '미설치횟수|몽고DB에 미설치 타게팅으로 선택된 수, 기본값 0||', -- 미설치횟수
	private int UN_INSTL_CLICK_CNT = 0;	//INTEGER      NOT NULL DEFAULT 0 COMMENT '미설치클릭횟수|미설치앱 타게팅 광고에서 받은 클릭수, 기본값 0||', -- 미설치클릭횟수
	private int ISLGN_CNT = 0;			//INTEGER      NOT NULL DEFAULT 0 COMMENT '재설치횟수|몽고DB에 재사용 타게팅으로 선택된 수, 기본값 0||', -- 재설치횟수
	private int ISLGN_CLICK_CNT = 0;	//INTEGER      NOT NULL DEFAULT 0 COMMENT '재설치클릭횟수|재사용앱 타게팅 광고에서 받은 클릭수, 기본값 0||', -- 재설치클릭횟수
	private String REG_USER_ID;			//VARCHAR(30)  NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|', -- 등록사용자ID
	private String REG_DTTM;			//DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||', -- 등록일자
	private String ALT_USER_ID;			//VARCHAR(30)  NULL     COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|', -- 수정사용자ID
	private String ALT_DTTM; 			//DATETIME     NULL     COMMENT '수정일자|수정시간을 기록||' -- 수정일자
	
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
	public String getAPP_SEQ() {
		return APP_SEQ;
	}
	public void setAPP_SEQ(String aPP_SEQ) {
		APP_SEQ = aPP_SEQ;
	}
	public String getAPP_UPD_DT() {
		return APP_UPD_DT;
	}
	public void setAPP_UPD_DT(String aPP_UPD_DT) {
		APP_UPD_DT = aPP_UPD_DT;
	}
	public int getMMNY_APP_CNT() {
		return MMNY_APP_CNT;
	}
	public void setMMNY_APP_CNT(int mMNY_APP_CNT) {
		MMNY_APP_CNT = mMNY_APP_CNT;
	}
	public int getMMNY_APP_CLICK_CNT() {
		return MMNY_APP_CLICK_CNT;
	}
	public void setMMNY_APP_CLICK_CNT(int mMNY_APP_CLICK_CNT) {
		MMNY_APP_CLICK_CNT = mMNY_APP_CLICK_CNT;
	}
	public int getUN_INSTL_CNT() {
		return UN_INSTL_CNT;
	}
	public void setUN_INSTL_CNT(int uN_INSTL_CNT) {
		UN_INSTL_CNT = uN_INSTL_CNT;
	}
	public int getUN_INSTL_CLICK_CNT() {
		return UN_INSTL_CLICK_CNT;
	}
	public void setUN_INSTL_CLICK_CNT(int uN_INSTL_CLICK_CNT) {
		UN_INSTL_CLICK_CNT = uN_INSTL_CLICK_CNT;
	}
	public int getISLGN_CNT() {
		return ISLGN_CNT;
	}
	public void setISLGN_CNT(int iSLGN_CNT) {
		ISLGN_CNT = iSLGN_CNT;
	}
	public int getISLGN_CLICK_CNT() {
		return ISLGN_CLICK_CNT;
	}
	public void setISLGN_CLICK_CNT(int iSLGN_CLICK_CNT) {
		ISLGN_CLICK_CNT = iSLGN_CLICK_CNT;
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
	
	
}
