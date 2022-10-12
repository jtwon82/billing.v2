package com.adgather.user.inclinations.cookieval.freq;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;


/**
 * 아이커버 프리퀀시(광고주) 관련 객체
 * @author ytkim
 *
 */
public class FreqAdverIco implements CookieVal {
	/** values ****************************************/
	private String userid;			// 광고주아이디
	private String expireTime;      // 만료기간

	/** create method **********************************/
	public FreqAdverIco() {}

	public FreqAdverIco(String userid, String expireTime) throws Exception {
		this.userid = userid;
		this.expireTime = expireTime;
	}

	public FreqAdverIco(FreqAdverIco obj) {
		this.userid = obj.userid;
		this.expireTime = obj.expireTime;
	}

	/** value get/set method **********************************/
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	/** value get/set method **********************************/
	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
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
			this.userid = strs[0];
			this.expireTime = strs[1];
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s", StringUtils.defaultString(userid), DELIMETER, expireTime);
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
			this.userid = mongoDoc.getString("userid");
			this.expireTime = mongoDoc.getString("expireTime");
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("userid", this.userid);
		doc.put("expireTime", this.expireTime);
		return doc;
	}
	
	@Override
	public FreqAdverIco clone() throws CloneNotSupportedException {
		return new FreqAdverIco(this);
	}
	
	@Override
	public String getKey() {
		return String.format("%s%s%s", StringUtils.defaultString(userid), DELIMETER, expireTime);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
	}

    @Override
    public String toString() {
        return "FreqAdverIco{" +
                "userid='" + userid + '\'' +
                ", expireTime='" + expireTime + '\'' +
                '}';
    }
}
