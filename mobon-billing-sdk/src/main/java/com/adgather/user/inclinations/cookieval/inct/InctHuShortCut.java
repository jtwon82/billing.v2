package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

/**
 * HU 바콘설치 도메인 로우 데이터
 * 
 * @author kwseo
 *
 */
public class InctHuShortCut implements InctHUCookieVal {
	/** values ****************************************/
	private String domain; // 도메인

	/** create method **********************************/
	public InctHuShortCut() {
	}

	public InctHuShortCut(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}

	public InctHuShortCut(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}

	public InctHuShortCut(InctHuShortCut obj) {
		this.domain = obj.domain;
	}

	/** value get/set method **********************************/
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/** Implements method **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if (cookieValue instanceof String) {
			setCookieValue((String) cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}

	public void setCookieValue(String cookieValue) throws Exception {

		if (StringUtils.isEmpty(cookieValue)) throw new Exception("Cookie Value Is Empty.");
		this.domain = cookieValue;

	}

	@Override
	public Object getCookieValue() {
		return String.format("%s", StringUtils.defaultString(domain));
	}

	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if (mongoDoc instanceof Document) {
			setMongoValue((Document) mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}

	public void setMongoValue(Document mongoDoc) throws Exception {
		if (mongoDoc == null) throw new Exception("Mongo Value Is Empty.");
		this.domain = mongoDoc.getString("domain");
	}

	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("domain", this.domain);
		return doc;
	}

	@Override
	public InctHuShortCut clone() throws CloneNotSupportedException {
		InctHuShortCut newObject = new InctHuShortCut();
		newObject.domain = this.domain;
		return newObject;
	}

	@Override
	public String getKey() {
		return StringUtils.defaultString(domain);
	}

	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctHuConv))		return;
		
		InctHuConv obj = (InctHuConv)element;
	}

}
