package com.adgather.user.inclinations.cookieval.abtest;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;

public class InfoAbTest implements CookieVal {
	/** values ****************************************/
	private String testing;			// 테스트 항목
	private String type;			// 테스트 타입

	/** create method **********************************/
	public InfoAbTest() {}
	
	public InfoAbTest(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InfoAbTest(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InfoAbTest(InfoAbTest obj) {
		this.testing = obj.testing;
		this.type = obj.type;
	}

	/** value get/set method **********************************/
	public String getTesting() {
		return testing;
	}

	public void setTesting(String testing) {
		this.testing = testing;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		
		this.testing = strs[0];		// 도메인이 있으면 최소 방문 1회 설정
		this.type = strs[1];
	}

	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
				, StringUtils.defaultString(this.testing), DELIMETER, StringUtils.defaultString(this.type));
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
		this.testing = mongoDoc.getString("testing");
		this.type = mongoDoc.getString("type");
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("testing", this.testing);
		doc.put("type", this.type);
		return doc;
	}

	@Override
	public InfoAbTest clone() throws CloneNotSupportedException {
		InfoAbTest newObj = new InfoAbTest();
		newObj.testing = this.testing;
		newObj.type = this.type;
		return newObj;
	}

	@Override
	public String getKey() {
		return StringUtils.defaultString(this.testing);
	}

	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InfoAbTest))		return;
		
		InfoAbTest obj = (InfoAbTest)element;
		this.type = obj.type;
	}
}
