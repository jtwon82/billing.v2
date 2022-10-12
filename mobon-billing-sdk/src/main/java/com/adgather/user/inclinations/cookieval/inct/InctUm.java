package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctUmCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * UM 도메인 로우 데이터
 * @author yhlim
 *
 */
public class InctUm implements CookieVal {
	/** values ****************************************/
	private String domain;				// 도메인
	private String updDate;
	
	private String fromApp;

	/** create method **********************************/
	public InctUm() {}
	
	public InctUm(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctUm(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctUm(InctUm obj) {
		this.domain = obj.domain;
		this.updDate = obj.updDate;
		
		if(obj.fromApp != null)
			this.fromApp = obj.fromApp;
	}
	
	
	/** value get/set method **********************************/
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUpdDate() {
		return updDate;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	
	public String getFromApp() {
		return fromApp;
	}
	
	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
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
		
		this.domain = strs[0];
		this.updDate = strs.length >= 2 ? strs[1] : InctUmCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
		
		if(strs.length >= 3 && strs[2] != null) {
			this.fromApp = strs[2];
		}
	}
	
	@Override
	public Object getCookieValue() {
		if(fromApp != null && !fromApp.equals(""))
			return String.format("%s%s%s%s%S"
						, StringUtils.defaultString(domain), DELIMETER
						, StringUtils.defaultString(updDate), DELIMETER
						, StringUtils.defaultString(fromApp));
		else
			return String.format("%s%s%s"
					, StringUtils.defaultString(domain), DELIMETER
					, StringUtils.defaultString(updDate));
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
		this.domain = mongoDoc.getString("domain");
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
		if(mongoDoc.getString("fromApp") != null)
			this.fromApp = mongoDoc.getString("fromApp");
	}
	/** String 배열 형태의 몽고 값 **/
	public void setMongoValue(String mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.domain = mongoDoc;
		this.updDate = InctHuCtr.getUpdDate();
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("domain", this.domain);
		if(this.updDate != null)	doc.put("updDate", this.updDate);
		if(this.fromApp != null) doc.put("fromApp",  this.fromApp);
		return doc;
	}
	
	@Override
	public InctUm clone() throws CloneNotSupportedException {
		return new InctUm(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(domain);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctUm))		return;
		
		this.updDate = InctHuCtr.getUpdDate();
	}
}
