package com.adgather.user.inclinations.cookieval.freq;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * 도메인 관련 프리퀀시 로우 데이터
 * @author yhlim
 *
 */
public class FreqSimple implements CookieVal{
	/** values ****************************************/
	public final String DELIMETER = "^";
	
	private String keyName;				//키명칭
	private int freqCnt;				//프리퀀시
	
	/** create method **********************************/
	public FreqSimple() {}
	
	public FreqSimple(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public FreqSimple(Document doc) throws Exception {
		setMongoValue(doc);
	}
	

	/** value get/set method **********************************/
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getFreqCnt() {
		return freqCnt;
	}

	public void setFreqCnt(int freqCnt) {
		this.freqCnt = freqCnt;
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
		
		this.keyName = strs[0];
		this.freqCnt = NumberUtils.toInt(strs[1], 0);
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%d", StringUtils.defaultString(keyName), DELIMETER, freqCnt);
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
		this.keyName = mongoDoc.getString("key");
		this.freqCnt = mongoDoc.getInteger("freqCnt", 0);
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("key", this.keyName);
		doc.put("freqCnt", this.freqCnt);
		return doc;
	}
	
	@Override
	public FreqSimple clone() throws CloneNotSupportedException {
		FreqSimple newObj = new FreqSimple();
		newObj.keyName = this.keyName;
		newObj.freqCnt = this.freqCnt;
		return newObj;
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(keyName);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof FreqSimple))		return;
		
		FreqSimple obj = (FreqSimple)element;
		if (bAppendValue) {
			this.freqCnt += obj.freqCnt;
		} else {
			this.freqCnt = obj.freqCnt;
		}
	}
}
