package com.adgather.user.inclinations.cookieval.minct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

public class MInctDSCK
					implements
					CookieVal {
	protected String dsck; // 앱 패키지
	protected String ip_info;
	protected String updDate;
	
	public MInctDSCK() {
		
	}
	
	public MInctDSCK(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public MInctDSCK(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public MInctDSCK(MInctDSCK obj) {
		this.dsck = obj.dsck;
		this.ip_info = obj.ip_info;
		this.updDate = obj.updDate;
	}
	
	/** get/set */
	public String getDsck() {
		return dsck;
	}

	public void setDsck(String dsck) {
		this.dsck = dsck;
	}

	public String getIp_info() {
		return ip_info;
	}

	public void setIp_info(String ip_info) {
		this.ip_info = ip_info;
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
//		System.out.println(cookieValue);
		if(strs == null || strs.length < 1)		throw new Exception("Cookie Value Is Not Validate.");
		
//		this.dsck = strs[0];
		this.ip_info = cookieValue; //strs[1];
//		this.updDate = strs.length >= 3 ? strs[2] : InctHuCtr.getUpdDate();	
		
//		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
//		if(strs == null || strs.length < 1)		throw new Exception("Cookie Value Is Not Validate.");
		
//		this.dsck = cookieValue;
//		this.ip_info = cookieValue;
//		this.getIp_info();
//		this.updDate = this.getUpdDate();
	}
	

	@Override
	public Object getCookieValue() {
		return String.format("%s"
//		 					, StringUtils.defaultString(this.dsck), DELIMETER
		 					,StringUtils.defaultString(this.ip_info)); //, DELIMETER
//		 					, StringUtils.defaultString(this.updDate));
//		return this.getIp_info();
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
		this.dsck = mongoDoc.getString("dsck");
		this.ip_info = mongoDoc.getString("ip_info");
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
	}

	@Override
	public Object getMongoValue() {
		// TODO Auto-generated method stub
		Document doc = new Document();
		if(this.dsck != null) {
			doc.put("dsck", this.dsck);
		}
		doc.put("ip_info", this.ip_info);
		if(this.updDate != null) {
			doc.put("updDate", this.updDate);
		}
		return doc;
	}

	@Override
	public MInctDSCK clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new MInctDSCK(this);
		
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return StringUtils.defaultString(this.ip_info);
	}
	
	@Override
	public void modValue(	Object element,
							boolean bAppendValue) {
		// TODO Auto-generated method stub
		if(!(element instanceof MInctDSCK))		return;
		MInctDSCK obj = (MInctDSCK) element;
		this.dsck = obj.dsck;
		this.ip_info = obj.ip_info;
		this.updDate = InctHuCtr.getUpdDate();
	}

}
