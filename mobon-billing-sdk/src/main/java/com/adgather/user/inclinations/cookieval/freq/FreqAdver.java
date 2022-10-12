package com.adgather.user.inclinations.cookieval.freq;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.CommonCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * 도메인 관련 프리퀀시 로우 데이터
 * @author yhlim
 *
 */
public class FreqAdver implements CookieVal{
	/** values ****************************************/
	public final String DELIMETER = "^";
	
	private String keyName;				//키명칭
	private int freqCnt;				//프리퀀시
	private String updDate;				//변경일
	
	/** create method **********************************/
	public FreqAdver() {}
	
	public FreqAdver(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public FreqAdver(Document doc) throws Exception {
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
		if(strs == null || strs.length < 2)		throw new Exception("Cookie Value Is Not Validate.");
				
		this.keyName = strs[0];
		this.freqCnt = NumberUtils.toInt(strs[1], 0);
		this.updDate = (strs.length >= 3 ? strs[2]:  CommonCtr.getUpdDate());
		
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%d%s%s", StringUtils.defaultString(keyName), DELIMETER
									, freqCnt, DELIMETER
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
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.keyName = mongoDoc.getString("key");
		this.freqCnt = mongoDoc.getInteger("freqCnt", 0);
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : CommonCtr.getUpdDate();
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("key", this.keyName);
		doc.put("freqCnt", this.freqCnt);
		doc.put("updDate", this.updDate);
		return doc;
	}
	
	@Override
	public FreqAdver clone() throws CloneNotSupportedException {
		FreqAdver newObj = new FreqAdver();
		newObj.keyName = this.keyName;
		newObj.freqCnt = this.freqCnt;
		newObj.updDate = this.updDate;
		return newObj;
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(keyName);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof FreqAdver))		return;
		
		FreqAdver obj = (FreqAdver)element;
		if (bAppendValue) {
			this.freqCnt += obj.freqCnt;
		} else {
			this.freqCnt = obj.freqCnt;
			this.updDate = obj.updDate;			// 변경시에는 날짜 적용하지 않음.
		}
	}
}
