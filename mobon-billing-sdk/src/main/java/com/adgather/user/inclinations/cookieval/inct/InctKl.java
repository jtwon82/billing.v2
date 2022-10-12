package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctUmCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.util.HangulCharsetDetector;


/**
 * UM 도메인 로우 데이터
 * @author yhlim
 *
 */
public class InctKl implements CookieVal {
	/** values ****************************************/
	private String keyword;				// 도메인
	private String updDate;

	/** create method **********************************/
	public InctKl() {}
	
	public InctKl(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctKl(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctKl(InctKl obj) {
		this.keyword = obj.keyword;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
		
		if(HangulCharsetDetector.isBrokenString(strs[0])) throw new Exception("InctKl Keyword Is Broken");
		
		this.keyword = Base64Converter.getInstance().decode(strs[0]);
		this.updDate = strs.length >= 2 ? strs[1] : InctUmCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, StringUtils.defaultString(Base64Converter.getInstance().encode(keyword)), DELIMETER
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
		this.keyword = mongoDoc.getString("kwd");
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
	}
	/** String 배열 형태의 몽고 값 **/
	public void setMongoValue(String mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.keyword = mongoDoc;
		this.updDate = InctHuCtr.getUpdDate();
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("kwd", this.keyword);
		if(this.updDate != null)	doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctKl clone() throws CloneNotSupportedException {
		return new InctKl(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(keyword);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctKl))		return;
		
		this.updDate = InctHuCtr.getUpdDate();
	}
}
