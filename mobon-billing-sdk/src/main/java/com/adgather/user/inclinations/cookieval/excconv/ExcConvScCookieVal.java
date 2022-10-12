package com.adgather.user.inclinations.cookieval.excconv;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

public class ExcConvScCookieVal implements CookieVal {

    public ExcConvScCookieVal() {}

    public ExcConvScCookieVal(String cookieValue) throws Exception {
        setCookieValue(cookieValue);
    }

    public ExcConvScCookieVal(Document mongoDoc) throws Exception {
        setMongoValue(mongoDoc);
    }

    public ExcConvScCookieVal(ExcConvScCookieVal cookieVal) {
        this.siteCode = cookieVal.getSiteCode();
        this.convDateTime = cookieVal.getConvDateTime();
        this.expireHours = cookieVal.getExpireHours();
    }

    /** 캠패인코드 */
    private String siteCode;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

        this.siteCode = strs[0];
        this.convDateTime = strs[1];
        this.expireHours = strs[2];
    }

    @Override
    public Object getCookieValue() {
        return String.format("%s%s%s%s%s", StringUtils.defaultString(siteCode), DELIMETER, StringUtils.defaultString(convDateTime), DELIMETER, StringUtils.defaultString(expireHours));
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
        this.siteCode = mongoDoc.getString("siteCode");
        this.convDateTime = mongoDoc.getString("convDateTime");
        this.expireHours = mongoDoc.getString("expireHours");
    }

    @Override
    public Object getMongoValue() {
        Document doc = new Document();
        doc.put("siteCode", this.siteCode);
        doc.put("convDateTime", this.convDateTime);
        doc.put("expireHours", this.expireHours);
        return doc;
    }

    @Override
    public ExcConvScCookieVal clone() throws CloneNotSupportedException {
        return new ExcConvScCookieVal(this);
    }

    @Override
    public String getKey() {
        return StringUtils.defaultString(siteCode);
    }

    @Override
    public void modValue(Object element, boolean bAppendValue) {
        ExcConvScCookieVal obj = (ExcConvScCookieVal)element;
        this.siteCode = obj.getSiteCode();
        this.convDateTime = obj.getConvDateTime();
        this.expireHours = obj.getExpireHours();
    }
}
