package com.adgather.user.inclinations.cookieval.freq;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;



/**
 * 샵로그 관련 로우 데이터
 * @author yhlim
 *
 */
public class FreqShops implements CookieVal {
	/** values ****************************************/
	private String siteCode;			// 캠패인코드
	private String pCode;				// 상품코드
	private int freqCnt;					// 프리퀀시

	/** create method **********************************/
	public FreqShops() {}
	
	public FreqShops(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public FreqShops(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public FreqShops(FreqShops obj) {
		this.siteCode = obj.siteCode;
		this.pCode = obj.pCode;
		this.freqCnt = obj.freqCnt;
	}
	
	/** value get/set method **********************************/
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
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
		if(strs == null || strs.length != 3)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.siteCode = strs[0];
		this.pCode = strs[1];
		this.freqCnt = NumberUtils.toInt(strs[2], 0);
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s%s%d", StringUtils.defaultString(siteCode), DELIMETER
														   , StringUtils.defaultString(pCode),DELIMETER,freqCnt);
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
		this.siteCode = mongoDoc.getString("siteCode");
		this.pCode = mongoDoc.getString("pCode");
		this.freqCnt = mongoDoc.getInteger("freqCnt", 0);
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("siteCode", this.siteCode);
		doc.put("pCode", this.pCode);
		doc.put("freqCnt", this.freqCnt);
		return doc;
	}
	
	@Override
	public FreqShops clone() throws CloneNotSupportedException {
		return new FreqShops(this);
	}
	
	@Override
	public String getKey() {
		return String.format("%s%s%s", StringUtils.defaultString(siteCode), DELIMETER, StringUtils.defaultString(pCode));
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof FreqShops))		return;
		
		FreqShops obj = (FreqShops)element;
		if (bAppendValue) {
			this.freqCnt += obj.freqCnt;
		} else {
			this.freqCnt = obj.freqCnt;
		}
	}
}
