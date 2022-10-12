package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctKcCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.util.HangulCharsetDetector;

/**
 * Kc 문맥 카테고리 데이터
 * @author dsChoi
 *
 */
public class InctKc implements CookieVal {
	/** values ****************************************/
	private String category;				// 카테고리
	private String updDate;
	private int categoryCnt;				// 카운트

	/** create method **********************************/
	public InctKc() {}
	
	public InctKc(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctKc(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctKc(InctKc obj) {
		this.category = obj.category;
		this.categoryCnt = obj.categoryCnt;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getCategoryCnt() {
		return categoryCnt;
	}
	public void setCategoryCnt(int categoryCnt) {
		this.categoryCnt = categoryCnt;
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
		
		if(HangulCharsetDetector.isBrokenString(strs[0])) throw new Exception("InctKc Category Is Broken");
		
		this.categoryCnt = NumberUtils.toInt(strs[0],1);
		this.category = strs[1];
		this.updDate = strs.length >= 3 ? strs[2] : InctKcCtr.getUpdDate();		
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s%s%s"
					, categoryCnt, DELIMETER, category, DELIMETER
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
		this.category = mongoDoc.getString("cate");
		this.categoryCnt = mongoDoc.getInteger("cnt", 1);
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctKcCtr.getUpdDate();
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("cate", this.category);
		doc.put("cnt", this.categoryCnt);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctKc clone() throws CloneNotSupportedException {
		return new InctKc(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(category);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctKc))		return;
		InctKc obj = (InctKc)element;
		if(bAppendValue) {
			this.categoryCnt += obj.getCategoryCnt();
		} else {
			this.categoryCnt = obj.getCategoryCnt();
		}
		this.updDate = InctKcCtr.getUpdDate();
	}
}
