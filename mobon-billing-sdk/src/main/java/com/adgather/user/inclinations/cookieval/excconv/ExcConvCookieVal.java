package com.adgather.user.inclinations.cookieval.excconv;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

public class ExcConvCookieVal implements CookieVal {

    public ExcConvCookieVal() {}

    public ExcConvCookieVal(String cookieValue) throws Exception {
        setCookieValue(cookieValue);
    }

    public ExcConvCookieVal(Document mongoDoc) throws Exception {
        setMongoValue(mongoDoc);
    }

    public ExcConvCookieVal(ExcConvCookieVal cookieVal) {
        this.adUserId = cookieVal.getAdUserId();
        this.convDateTime = cookieVal.getConvDateTime();
        this.expireHours = cookieVal.getExpireHours();
    }

    /** 광고주ID */
    private String adUserId;
    /** 구매전환일시 */
    private String convDateTime;
    /** 구매전환만료시간 */
    private String expireHours;

    public String getExpireHours() {
        return expireHours;
    }

    public void setExpireHours(String expireHours) {
        this.expireHours = expireHours;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(String adUserId) {
        this.adUserId = adUserId;
    }

    public String getConvDateTime() {
        return convDateTime;
    }

    public void setConvDateTime(String convDateTime) {
        this.convDateTime = convDateTime;
    }

    @Override
    public void setCookieValue(Object cookieValue) throws Exception {
        if(cookieValue instanceof String) {
            setCookieValue((String)cookieValue);
        } else {
            throw new Exception("Unsupported Object.");
        }
    }

    public void setCookieValue(String cookieValue) throws Exception {
        if(StringUtils.isEmpty(cookieValue)) throw new Exception("Cookie Value Is Empty.");
        String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
        if(strs == null || strs.length != 3)		throw new Exception("Cookie Value Is Not Validate.");

        this.adUserId = strs[0];
        this.convDateTime = strs[1];
        this.expireHours = strs[2];
    }

    @Override
    public Object getCookieValue() {
        return String.format("%s%s%s%s%s", StringUtils.defaultString(adUserId), DELIMETER, StringUtils.defaultString(convDateTime), DELIMETER, StringUtils.defaultString(expireHours));
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
        if (mongoDoc == null) throw new Exception("Mongo Value Is Empty.");
        this.adUserId = mongoDoc.getString("adUserId");
        this.convDateTime = mongoDoc.getString("convDateTime");
        this.expireHours = mongoDoc.getString("expireHours");
    }

    @Override
    public Object getMongoValue() {
        Document doc = new Document();
        doc.put("adUserId", this.adUserId);
        doc.put("convDateTime", this.convDateTime);
        doc.put("expireHours", this.expireHours);
        return doc;
    }

    @Override
    public ExcConvCookieVal clone() throws CloneNotSupportedException {
        return new ExcConvCookieVal(this);
    }

    @Override
    public String getKey() {
        return StringUtils.defaultString(adUserId);
    }

    @Override
    public void modValue(Object element, boolean bAppendValue) {
        ExcConvCookieVal obj = (ExcConvCookieVal)element;
        this.adUserId = obj.getAdUserId();
        this.convDateTime = obj.getConvDateTime();
        this.expireHours = obj.getExpireHours();
    }
}
