package com.mobon.billing.report.disk.json;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.mobon.billing.report.disk.DateUtils;


/**
 * 앱 정보 수집 정보 최초 진입클래스 
 * 구성
 * SDK -> 송출(MSDKAppPackageGatherServlet) -> dump -> batch(수집된 package 정보를 이용하여 앱스토어(구글 등) 카테고리 정보를 수집)
 * SDK 요청은 1일 1회로 한다.
 * @author 
 *
 */
public class AppInfo {
	
	
	private AppDevice appDevice = new AppDevice();

	// APP_INFO
	private String APP_SEQ; // INT UNSIGNED 	-- 앱순서
	private String OS_TP_CODE; // VARCHAR(2)   	-- OS구분코드
	private String APP_PKG_NM; // VARCHAR(50)  	-- 앱패키지명
	private String APP_TITLE_NM; // VARCHAR(50) -- 앱타이틀명
	private String APP_LAST_ALT_DT;// DATE NOT 	-- 앱최종수정일
	private String APP_CTGR_SEQ; // INT UNSIGNED-- 앱카테고리순서
	private String REG_USER_ID; // VARCHAR(30)  -- 등록사용자ID
	private String REG_DTTM; // DATETIME     	-- 등록일자
	private String ALT_USER_ID; // VARCHAR(30)  -- 수정사용자ID
	private String ALT_DTTM; // DATETIME     	-- 수정일자
	
	/* 앱 추가 정보 */
	
	
	private AppUserMngInfo appUserMngInfo = new AppUserMngInfo();
	
	private AppGPS gps = new AppGPS();
	
	JSONObject parameter;
	
	public Map getParameter() {
		return parameter;
	}

	public void setParameter(JSONObject parameter) {
		this.parameter = parameter;
	}

	public AppInfo() {
	}
	
	public AppInfo(JSONObject req) {
//		System.out.println(req.getString("adid"));
//		System.out.println(req.getString("pkg"));
		this.getAppDevice().setTRML_UNQ_VAL(req.getString("adid"));
		
		this.OS_TP_CODE = (req.getString("os").equals("null")) ? "ao" : req.getString("os");
		// 2018/04/13 
		// 단일 문자열 수집에서 json 타입으로 변경
		// 그로인해 dump 코드 변경.
		this.APP_PKG_NM = req.getString("pkg");
//		System.out.println(this.APP_PKG_NM);
		
		this.APP_TITLE_NM = "";
		this.APP_LAST_ALT_DT = DateUtils.getFormatDate("YYYYMMddHHmmss");
		 
		this.REG_USER_ID = "";
		this.REG_DTTM = DateUtils.getFormatDate("YYYYMMddHHmmss");
		
		// 이하 정보는 요청은 받지만 수집은 하지 않음.
		// 향후 필요할 경우 사용함.
		/*
		this.getAppDevice().setModel(req.getParameter("model"));
		this.getAppDevice().setOsv(req.getParameter("osv"));
		this.getAppDevice().setCarrier(req.getParameter("carrier"));
		this.getAppDevice().setUa(req.getParameter("ua"));
		
		this.gps.setIp(req.getParameter("ip"));
		this.gps.setConnectiontype(req.getParameter("connectiontype"));
		this.gps.setGeotype(req.getParameter("geotype"));
		this.gps.setLat(req.getParameter("lat"));
		this.gps.setLon(req.getParameter("lon"));
		*/
		
//		req.getParameter("s");
//		req.getParameter("us");
	}
	
	public String getUrl(String pkg) {
		return new StringBuffer().append("https://play.google.com/store/apps/details?id=")
				.append(pkg.trim())
				.append("&hl=ko")
				.toString();
	}
	
	public AppDevice getAppDevice() {
		return appDevice;
	}

	public void setAppDevice(AppDevice appDevice) {
		this.appDevice = appDevice;
	}

	public String getAPP_SEQ() {
		return APP_SEQ;
	}

	public void setAPP_SEQ(String aPP_SEQ) {
		APP_SEQ = aPP_SEQ;
	}

	public String getOS_TP_CODE() {
		return ((OS_TP_CODE != null && OS_TP_CODE.length() >= 0) ? OS_TP_CODE : "ao").toLowerCase();
	}

	public void setOS_TP_CODE(String oS_TP_CODE) {
		OS_TP_CODE = oS_TP_CODE;
	}

	public String getAPP_PKG_NM() {
		return APP_PKG_NM;
	}

	public void setAPP_PKG_NM(String aPP_PKG_NM) {
		APP_PKG_NM = aPP_PKG_NM;
	}

	public String getAPP_TITLE_NM() {
		return APP_TITLE_NM;
	}

	public void setAPP_TITLE_NM(String aPP_TITLE_NM) {
		APP_TITLE_NM = aPP_TITLE_NM;
	}

	public String getAPP_LAST_ALT_DT() {
		return APP_LAST_ALT_DT;
	}

	public void setAPP_LAST_ALT_DT(String aPP_LAST_ALT_DT) {
		APP_LAST_ALT_DT = aPP_LAST_ALT_DT;
	}

	public String getAPP_CTGR_SEQ() {
		return APP_CTGR_SEQ;
	}

	public void setAPP_CTGR_SEQ(String aPP_CTGR_SEQ) {
		APP_CTGR_SEQ = aPP_CTGR_SEQ;
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

	public AppUserMngInfo getAppUserMngInfo() {
		return appUserMngInfo;
	}

	public void setAppUserMngInfo(AppUserMngInfo appUserMngInfo) {
		this.appUserMngInfo = appUserMngInfo;
	}
	
	
	
	public String toString() {
		return new StringBuffer()
		.append("")
		.append("").toString();
	}
	
	public JSONObject toJson() {
		JSONObject parameter = new JSONObject();
		parameter.put(AppInfo.class.getSimpleName(), this.getParameter());
//		parameter.put(AppDevice.class.getSimpleName(), this.getAppDevice().getParameter());
//		parameter.put(AppUserMngInfo.class.getSimpleName(), this.getAppUserMngInfo().getParameter());
		return parameter;
	}
	
	public static boolean validateDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd", java.util.Locale.KOREA); 
		sdf.setLenient(false);
		try {
			sdf.parse(s);
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public static void main(String args[]) {
		String udate = "Ù¢Ù Ù¡Ù§Ù Ù¤Ù Ù¡"; //"99999999"; //
		char c = udate.charAt(0);
		if((c >= 48 && c <= 57) && udate.length() == 8) {
			System.out.println(c+ " : " + validateDate(udate));
		} else {
			System.out.println("error");
		}

	}
}