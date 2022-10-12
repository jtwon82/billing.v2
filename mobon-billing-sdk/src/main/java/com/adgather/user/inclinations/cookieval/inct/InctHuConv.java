package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;


/**
 * HU 컨버전 로우 데이터
 * @author kwseo
 *
 */
public class InctHuConv implements InctHUCookieVal {
	/** values ****************************************/
	private int convCnt;				// 컨버전횟수
	private String domain;				// 도메인

	/** create method **********************************/
	public InctHuConv() {}
	
	public InctHuConv(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctHuConv(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctHuConv(InctHuConv obj) {
		this.domain = obj.domain;
		this.convCnt = obj.convCnt;
	}
	
	/** value get/set method **********************************/
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getConvCnt() {
		return convCnt;
	}
	public void setConvCnt(int convCnt) {
		this.convCnt = convCnt;
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
		if(strs == null || strs.length != 2)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.convCnt = NumberUtils.toInt(strs[0], 1);		// 도메인이 있으면 최소 컨버전 1회 설정
		this.domain = strs[1];
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s"
					, convCnt, DELIMETER, StringUtils.defaultString(domain));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.convCnt = mongoDoc.getInteger("convCnt", 0);
		this.domain = mongoDoc.getString("domain");
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("convCnt", this.convCnt);
		doc.put("domain", this.domain);
		return doc;
	}
	
	@Override
	public InctHuConv clone() throws CloneNotSupportedException {
		InctHuConv newObject = new InctHuConv();
		newObject.convCnt = this.convCnt;
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
		if (bAppendValue) {
			this.convCnt += obj.convCnt;
		} else {
			this.convCnt = obj.convCnt;
		}
	}
}
