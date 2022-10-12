package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctKccCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.util.HangulCharsetDetector;

/**
 * Kcc 문맥 카테고리 데이터
 * @author dsChoi
 *
 */
public class InctKcc implements CookieVal {
	/** values ****************************************/
	private String cateFiveCntYn;	// 카테고리 카운트 5회이상
	private String updDate;

	/** create method **********************************/
	public InctKcc() {}
	
	public InctKcc(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctKcc(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctKcc(InctKcc obj) {
		this.cateFiveCntYn = obj.cateFiveCntYn;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public String getCateFiveCntYn() {
		return cateFiveCntYn;
	}
	public void setCateFiveCntYn(String cateFiveCntYn) {
		this.cateFiveCntYn = cateFiveCntYn;
	}
	public String getUpdDate() {
		return updDate;
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
		
		if(HangulCharsetDetector.isBrokenString(strs[0])) throw new Exception("InctKcc Category Is Broken");
		
		this.cateFiveCntYn = strs[0];
		this.updDate = strs.length >= 2 ? strs[1] : InctKccCtr.getUpdDate();		
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, cateFiveCntYn, DELIMETER
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
		this.cateFiveCntYn = mongoDoc.getString("cateFiveCntYn");
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctKccCtr.getUpdDate();
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("cateFiveCntYn", this.cateFiveCntYn);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctKcc clone() throws CloneNotSupportedException {
		return new InctKcc(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(cateFiveCntYn);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctKcc))		return;
		InctKcc obj = (InctKcc)element;
		this.cateFiveCntYn = obj.getCateFiveCntYn();
		this.updDate = InctKccCtr.getUpdDate();
	}
}
