package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;

import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctKmCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.util.HangulCharsetDetector;


/**
 * Km 문맥 유입키워드 데이터
 * @author dsChoi
 *
 */
public class InctKm implements CookieVal {
	/** values ****************************************/
	private String keyword;				// 키워드
	private String updDate;
	private int keywordCnt;				// 카운트

	/** create method **********************************/
	public InctKm() {}
	
	public InctKm(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctKm(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctKm(InctKm obj) {
		this.keyword = obj.keyword;
		this.keywordCnt = obj.keywordCnt;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getKeywordCnt() {
		return keywordCnt;
	}
	public void setKeywordCnt(int keywordCnt) {
		this.keywordCnt = keywordCnt;
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
		
		if(HangulCharsetDetector.isBrokenString(strs[0])) throw new Exception("InctKm Keyword Is Broken");
		
		this.keywordCnt = NumberUtils.toInt(strs[0],1);
		if(this.keywordCnt == 0) {
			this.keywordCnt = 1;
		}
		this.keyword = Base64Converter.getInstance().decode(strs[1]);
		this.updDate = strs.length >= 3 ? strs[2] : InctKmCtr.getUpdDate();		
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s%s%s"
					, keywordCnt, DELIMETER, StringUtils.defaultString(Base64Converter.getInstance().encode(keyword)), DELIMETER
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
		this.keyword = mongoDoc.getString("kwd");
		this.keywordCnt = mongoDoc.getInteger("cnt", 1);
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctKmCtr.getUpdDate();
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("kwd", this.keyword);
		doc.put("cnt", this.keywordCnt);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctKm clone() throws CloneNotSupportedException {
		return new InctKm(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(keyword);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctKm))		return;
		InctKm obj = (InctKm)element;
		if(bAppendValue) {
			this.keywordCnt += obj.getKeywordCnt();
		} else {
			this.keywordCnt = obj.getKeywordCnt();
		}
		this.updDate = InctKmCtr.getUpdDate();
	}
}
