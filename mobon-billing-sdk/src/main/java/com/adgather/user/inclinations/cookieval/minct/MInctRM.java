package com.adgather.user.inclinations.cookieval.minct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

public class MInctRM
					implements
					CookieVal {
	protected String pkg; // 앱 패키지
	protected String updDate;
	
	public MInctRM() {
		
	}
	
	public MInctRM(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public MInctRM(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public MInctRM(MInctRM obj) {
		this.pkg = obj.pkg;
		this.updDate = obj.updDate;
	}
	
	/** get/set */
	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	
	public String getUpdDate() {
		return updDate;
	}

	public void setUpdDate(String updDate) {
		this.updDate = updDate;
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
		
		this.pkg = strs[0];
		this.updDate = strs.length >= 2 ? strs[1] : InctHuCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
	}

	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, StringUtils.defaultString(this.pkg), DELIMETER
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
		this.pkg = mongoDoc.getString("pkg");
		
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
		
		
	}

	@Override
	public Object getMongoValue() {
		// TODO Auto-generated method stub
		Document doc = new Document();
		doc.put("pkg", this.pkg);
		if(this.updDate != null)	doc.put("updDate", this.updDate);
		
		return doc;
	}

	@Override
	public MInctRM clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new MInctRM(this);
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return StringUtils.defaultString(pkg);
	}
	
	@Override
	public void modValue(	Object element,
							boolean bAppendValue) {
		// TODO Auto-generated method stub
		if(!(element instanceof MInctRM))		return;
		this.updDate = InctHuCtr.getUpdDate();
	}

}
