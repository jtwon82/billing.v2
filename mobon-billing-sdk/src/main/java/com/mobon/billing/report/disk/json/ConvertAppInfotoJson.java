
package com.mobon.billing.report.disk.json;

import java.io.File;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobon.billing.report.disk.DateUtils;
import com.mobon.billing.report.disk.Disk;

public class ConvertAppInfotoJson {

	private static final Logger logger = LoggerFactory.getLogger(ConvertAppInfotoJson.class);
		
	public void appInfo(Disk disk, AppInfo appInfo) {
//		JSONArray rows = JSONArray.fromObject(appInfo.getAPP_PKG_NM().replaceAll("&quot;", ""));
		JSONObject parameters = new JSONObject();
		parameters.put("ADID", appInfo.getAppDevice().getTRML_UNQ_VAL()); // 단말기고유값
		parameters.put("PACKAGE", appInfo.getAPP_PKG_NM()); // OS구분코드
		parameters.put("OS", appInfo.getOS_TP_CODE()); // OS구분코드
//		parameters.put("REG_USER_ID", appInfo.getREG_USER_ID()); // 등록사용자ID
//		parameters.put("REG_DTTM", appInfo.getREG_DTTM()); // 등록일자
//		parameters.put("ALT_USER_ID", appInfo.getALT_USER_ID()); // 수정사용자ID
//		parameters.put("ALT_DTTM", new Date()); // 수정일자
//		System.out.println(appInfo.getAppDevice().getTRML_UNQ_VAL());
		disk.write(parameters, "./data");
		/*
		 * 
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = (JSONObject) rows.get(i);
			// key : package, value : update
			String key = String.valueOf(row.keys().next());
			appInfo.setAPP_PKG_NM(key);
			appInfo.setAPP_LAST_ALT_DT(row.getString(key));
			addDBDevice(appInfo); // 앱사용자단말기정보 입력 
			addDBAppInfo(appInfo); // 앱정보 입력
			addDBDeviceAppMapping(disk, appInfo); // 앱사용자관리정보 입력 
		}
		*/

	}
	
	/**
	 * 앱사용자 단말기 정보(APP_USER_TRML_INFO) 등록 
	 * @param appInfo
	 */
	public void addDBDevice(AppInfo appInfo) {
//		logger.debug(appInfo.toString());

		JSONObject parameters = new JSONObject();
		parameters.put("TRML_UNQ_VAL", appInfo.getAppDevice().getTRML_UNQ_VAL()); // 단말기고유값
		parameters.put("OS_TP_CODE", appInfo.getOS_TP_CODE()); // OS구분코드
		parameters.put("REG_USER_ID", appInfo.getREG_USER_ID()); // 등록사용자ID
		parameters.put("REG_DTTM", appInfo.getREG_DTTM()); // 등록일자
		parameters.put("ALT_USER_ID", appInfo.getALT_USER_ID()); // 수정사용자ID
		parameters.put("ALT_DTTM", new Date()); // 수정일자
				
		appInfo.getAppDevice().setParameter(parameters);
	}
	
	/**
	 * 앱정보(APP_INFO) 등록 
	 * @param appInfo
	 */
	public void addDBAppInfo(AppInfo appInfo) {
		JSONObject parameters = new JSONObject();
		parameters.put("APP_PKG_NM", appInfo.getAPP_PKG_NM());
		parameters.put("APP_TITLE_NM", appInfo.getAPP_TITLE_NM());
		parameters.put("APP_LAST_ALT_DT", appInfo.getAPP_LAST_ALT_DT());
		parameters.put("APP_CTGR_SEQ", appInfo.getAPP_CTGR_SEQ());
		parameters.put("REG_USER_ID", "");
		parameters.put("REG_DTTM", appInfo.getREG_DTTM());
		parameters.put("ALT_USER_ID", "");
		parameters.put("ALT_DTTM", new Date());
						
		appInfo.setParameter(parameters);
	}
	
	/**
	 * 앱사용자관리정보(APP_USER_MNG_INFO) 등록
	 * @param appInfo
	 */
	public void addDBDeviceAppMapping(Disk disk, AppInfo appInfo) {
		try {
//			logger.debug("appInfo.getAppDevice().getTRML_SEQ() : " + appInfo.getAppDevice().getTRML_SEQ());
			JSONObject parameters = new JSONObject();
//			logger.debug(appInfo.getAppDevice().getTRML_SEQ() + ":" + appInfo.getAPP_SEQ() + " #########################################");
			parameters.put("TRML_SEQ", appInfo.getAppDevice().getTRML_SEQ());       
			parameters.put("APP_SEQ", appInfo.getAPP_SEQ());           
			if(appInfo.getAPP_PKG_NM().length() <= 50) {
				parameters.put("APP_PKG_NM", appInfo.getAPP_PKG_NM());
			} else {
				parameters.put("APP_PKG_NM", appInfo.getAPP_PKG_NM().substring(0, 50));
				parameters.put("APP_PKG_NM_ORG", appInfo.getAPP_PKG_NM());
			}
			parameters.put("TRML_UNQ_VAL", appInfo.getAppDevice().getTRML_UNQ_VAL()); // 단말기고유값
			parameters.put("APP_UPD_DT", appInfo.getAPP_LAST_ALT_DT());        
			parameters.put("MMNY_APP_CNT", appInfo.getAppUserMngInfo().getMMNY_APP_CNT());      
			parameters.put("MMNY_APP_CLICK_CNT", appInfo.getAppUserMngInfo().getMMNY_APP_CLICK_CNT());
			parameters.put("UN_INSTL_CNT", appInfo.getAppUserMngInfo().getUN_INSTL_CNT());      
			parameters.put("UN_INSTL_CLICK_CNT", appInfo.getAppUserMngInfo().getUN_INSTL_CLICK_CNT());
			parameters.put("ISLGN_CNT", appInfo.getAppUserMngInfo().getISLGN_CNT());         
			parameters.put("ISLGN_CLICK_CNT", appInfo.getAppUserMngInfo().getISLGN_CLICK_CNT());
			parameters.put("REG_USER_ID", appInfo.getREG_USER_ID());       
			parameters.put("REG_DTTM", appInfo.getREG_DTTM());
			parameters.put("ALT_USER_ID", appInfo.getAppUserMngInfo().getALT_USER_ID());       
			parameters.put("ALT_DTTM", new Date());
			appInfo.getAppUserMngInfo().setParameter(parameters);
			
//			System.out.println(appInfo.toJson().toString());
			disk.write(appInfo.toJson(), "./data"+File.separator+DateUtils.getToDayYYMMDD());
//			logger.info(appInfo.toMap().toString());
		} finally {
			
		}
	}
	
	
	public static void main(String args[]) {
		String a = "com.test";
		String[] aa = a.split("\\.");
		System.out.println(a.length() + ":" + a.substring(0, 8));
	}
}
