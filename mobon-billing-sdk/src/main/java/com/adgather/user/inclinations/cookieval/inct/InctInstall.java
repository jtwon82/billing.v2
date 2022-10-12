package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctUmCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * 앱설치
 * @author yhlim
 *
 */
public class InctInstall implements CookieVal {
	/** values ****************************************/
	private String appName;				// 도메인
	private String installDate;

	/** create method **********************************/
	public InctInstall() {}
	
	public InctInstall(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctInstall(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctInstall(InctInstall obj) {
		this.appName = obj.appName;
		this.installDate = obj.installDate;
	}
	
	/** value get/set method **********************************/
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppName() {
		return appName;
	}
	public String getInstallDate() {
		return installDate;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	/** Implements method  **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(cookieValue instanceof String) {
			setCookieValue((String)cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setCookieValue(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length < 1)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.appName = strs[0];
		this.installDate = strs.length >= 2 ? strs[1] : InctUmCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, StringUtils.defaultString(appName), DELIMETER
					, StringUtils.defaultString(installDate));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else if(mongoDoc instanceof String) {
			setMongoValue((String)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	/** Document 배열 형태의 몽고 값 **/
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.appName = mongoDoc.getString("appName");
		this.installDate = mongoDoc.getString("installDate");
	}
	/** String 배열 형태의 몽고 값 **/
	public void setMongoValue(String mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.appName = mongoDoc;
		this.installDate = InctHuCtr.getUpdDate();
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("appName", this.appName);
		if(this.installDate != null)	doc.put("installDate", this.installDate);
		return doc;
	}
	
	@Override
	public InctInstall clone() throws CloneNotSupportedException {
		return new InctInstall(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(appName);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctInstall))		return;
		
		InctInstall inctApp = (InctInstall)element;
		this.installDate = inctApp.getInstallDate();
	}
}
