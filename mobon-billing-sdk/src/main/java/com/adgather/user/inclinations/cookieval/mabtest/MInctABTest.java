package com.adgather.user.inclinations.cookieval.mabtest;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;

public class MInctABTest
					implements
					CookieVal {
	protected String testID; // 앱 패키지
	protected String testType;
	
	public MInctABTest() {
		
	}
	
	public MInctABTest(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public MInctABTest(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public MInctABTest(MInctABTest obj) {
		this.testID = obj.testID;
		this.testType = obj.testType;
	}
	
	/** get/set */
	public String getTestID() {
		return testID;
	}

	public void setTestID(String testID) {
		this.testID = testID;
	}
	
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}
	/** */

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
		
		this.testID = strs[0];
		this.testType = strs[1]; //strs.length >= 2 ? strs[1] : InctHuCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
	}

	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, StringUtils.defaultString(this.testID), DELIMETER
					, StringUtils.defaultString(this.testType));
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
		this.testID = mongoDoc.getString("testID");
		
		String temptestType = mongoDoc.getString("testType");
		this.testType = temptestType; //tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
		
		
	}

	@Override
	public Object getMongoValue() {
		// TODO Auto-generated method stub
		Document doc = new Document();
		doc.put("testID", this.testID);
		doc.put("testType", this.testType);
		
		return doc;
	}

	@Override
	public MInctABTest clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new MInctABTest(this);
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return StringUtils.defaultString(testID);
	}
	
	@Override
	public void modValue(	Object element,
							boolean bAppendValue) {
		// TODO Auto-generated method stub
		if(!(element instanceof MInctABTest)) return;
		MInctABTest obj = (MInctABTest) element;
		
		this.testID = obj.testID;
		this.testType = obj.testType;
	}

}
