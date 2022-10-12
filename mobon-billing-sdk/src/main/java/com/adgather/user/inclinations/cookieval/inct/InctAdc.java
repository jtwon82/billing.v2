package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctAdcCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.util.HangulCharsetDetector;

/**
 * Adc 오디언스 데이터
 *
 */
public class InctAdc implements CookieVal {
	/** values ****************************************/
	private String adcSeq;				// 오디언스 SEQ
	private String updDate;

	/** create method **********************************/
	public InctAdc() {}
	
	public InctAdc(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctAdc(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctAdc(InctAdc obj) {
		this.adcSeq = obj.adcSeq;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public String getUpdDate() {
		return updDate;
	}
	public String getAdcSeq() {
		return adcSeq;
	}
	public void setAdcSeq(String adcSeq) {
		this.adcSeq = adcSeq;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
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
		
		if(HangulCharsetDetector.isBrokenString(strs[0])) throw new Exception("InctAdcSeq Is Broken");
		
		this.adcSeq = strs[0];
		this.updDate = strs.length >= 2 ? strs[1] : InctAdcCtr.getUpdDate();		
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, adcSeq, DELIMETER
					, StringUtils.defaultString(updDate));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	/** Document 배열 형태의 몽고 값 **/
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.adcSeq = mongoDoc.getString("adcSeq");
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctAdcCtr.getUpdDate();
		
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("adcSeq", this.adcSeq);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctAdc clone() throws CloneNotSupportedException {
		return new InctAdc(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(adcSeq);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctAdc))		return;
		InctAdc obj = (InctAdc)element;
		this.adcSeq = obj.getAdcSeq();
		this.updDate = InctAdcCtr.getUpdDate();
	}
}
