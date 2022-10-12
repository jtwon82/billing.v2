package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctMrfCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

/**
 * Mr 문맥타기팅 키워드 데이터
 * @author dsChoi
 *
 */
public class InctMrf implements CookieVal {
	/** values ****************************************/
	private int adCnt;				// 카운트
	private String objKey;
	private String updDate;

	/** create method **********************************/
	public InctMrf() {}
	
	public InctMrf(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctMrf(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctMrf(InctMrf obj) {
		this.adCnt = obj.adCnt;
		this.objKey = obj.objKey;
		this.updDate = obj.updDate;
	}
	
	/** value get/set method **********************************/
	public int getAdCnt() {
		return adCnt;
	}
	public void setAdCnt(int adCnt) {
		this.adCnt = adCnt;
	}
	public String getObjKey() {
		return objKey;
	}
	public void setObjKey(String objKey) {
		this.objKey = objKey;
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
		if(StringUtils.isEmpty(cookieValue)) {throw new Exception("Cookie Value Is Empty.");}
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length < 1)	{throw new Exception("Cookie Value Is Not Validate.");}
		
		this.adCnt = NumberUtils.toInt(strs[0],1);
		if(this.adCnt == 0) {
			this.adCnt = 1;
		}
		
		this.objKey = strs[1];
		
		this.updDate = strs.length >= 3 ? strs[2] : InctMrfCtr.getUpdDate();	
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s%s%s"
				, adCnt, DELIMETER, StringUtils.defaultString(objKey), DELIMETER
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
		if(mongoDoc == null) {throw new Exception("Mongo Value Is Empty.");}
		this.objKey = mongoDoc.getString("objKey");
		this.adCnt = mongoDoc.getInteger("cnt", 1);
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctMrfCtr.getUpdDate();
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("objKey", this.objKey);
		doc.put("cnt", this.adCnt);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public InctMrf clone() throws CloneNotSupportedException {
		return new InctMrf(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(objKey);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctMrf)) {return;}
		InctMrf obj = (InctMrf)element;
		if(bAppendValue) {
			this.adCnt += obj.getAdCnt();
		} else {
			this.adCnt = obj.getAdCnt();
		}
		this.updDate = InctMrfCtr.getUpdDate();
	}
}
