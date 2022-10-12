package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;

public class BounceRateData extends com.adgather.lang.old.ObjectToString implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2265721986338239663L;
	
	private String adverId;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String altDttm;
	private String browserCodeVersion;
	private String browserTpValue;
	private String className;
	private String deviceTpValue;
	private String dumpType;
	private String hh;
	private int limit;
	private String mediaId;
	private String mediaScriptNo;
	private String orderBy;
	private String osTpValue;
	private String osVersion;
	private int retrnCnt;
	private int avalCnt;
	private String platformType;
	private String pltfomTpCode;
	private String productType;
	private String platform;
	private String product;
	private String adGubun;
	private String sendDate;
	private String siteCode;
	private String startDate;
	private String statsDttm;
	private String targetDate;
	private String userIp;
	private String key;
	private boolean empty;
	private String endDate;
	private String type = "RA";

	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 TpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	
	public static BounceRateData fromHashMap(Map from) {
		
		BounceRateData result = new BounceRateData();
		result.adverId = StringUtils.trimToNull2(from.get("adverId"),"");
		result.advrtsPrdtCode = StringUtils.trimToNull2(from.get("advrtsPrdtCode"),"");
		result.advrtsTpCode = StringUtils.trimToNull2(from.get("advrtsTpCode"),"");
		result.altDttm = StringUtils.trimToNull2(from.get("altDttm"),"");
		result.browserCodeVersion = StringUtils.trimToNull2(from.get("browserCodeVersion"),"");
		result.browserTpValue = StringUtils.trimToNull2(from.get("browserTpValue"),"");
		result.className = StringUtils.trimToNull2(from.get("className"),"");
		result.deviceTpValue = StringUtils.trimToNull2(from.get("deviceTpValue"),"");
		result.dumpType= StringUtils.trimToNull2(from.get("dumpType"),"");
		result.hh = StringUtils.trimToNull2(from.get("hh"),"");
		result.limit = Integer.parseInt(StringUtils.trimToNull2(from.get("limit"),"0"));
		result.mediaId = StringUtils.trimToNull2(from.get("mediaId"),"");
		result.mediaScriptNo = StringUtils.trimToNull2(from.get("mediaScriptNo"),"");
		result.orderBy = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.osTpValue = StringUtils.trimToNull2(from.get("osTpValue"),"");
		result.osVersion = StringUtils.trimToNull2(from.get("osVersion"),"");
		result.retrnCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("retrnCnt"),"0"));
		result.avalCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("avalCnt"),"0"));
		result.platformType = StringUtils.trimToNull2(from.get("platformType"),"");
		result.pltfomTpCode = StringUtils.trimToNull2(from.get("pltfomTpCode"),"");
		result.productType = StringUtils.trimToNull2(from.get("productType"),"");
		result.sendDate = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.siteCode = StringUtils.trimToNull2(from.get("siteCode"),"");
		result.startDate = StringUtils.trimToNull2(from.get("startDate"),"");
		result.statsDttm = StringUtils.trimToNull2(from.get("statsDttm"),"");
		result.targetDate = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.userIp = StringUtils.trimToNull2(from.get("userIp"),"");
		result.key = StringUtils.trimToNull2(from.get("key"),"");
		result.empty = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("empty"),"false"));
		result.endDate = StringUtils.trimToNull2(from.get("endDate"),"");
		result.platform = StringUtils.trimToNull2(from.get("platform"),"");
		result.product = StringUtils.trimToNull2(from.get("product"),"");
		result.adGubun = StringUtils.trimToNull2(from.get("adGubun"),"");

		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		return result;
	}
	
	public BaseCVData toBaseCVData(){
		BaseCVData result = new BaseCVData();
		// base
		result.setYyyymmdd(String.format("%s", DateUtils.getDate("yyyyMMdd", DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", this.getSendDate()))));
		result.setAdvertiserId(this.getAdverId());
		result.setPlatform(this.getPltfomTpCode());
		result.setProduct(this.getProductType());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo( Integer.parseInt(this.mediaScriptNo) );
		result.setScriptUserId(this.mediaId);
		result.setInterlock("kakao".equals(this.mediaId) || "mkakao".equals(this.mediaId) ? "03" : "01");
		result.setDeviceCode(this.deviceTpValue);
		result.setOsCode(this.osTpValue);
		result.setBrowserCode(this.browserTpValue);
		result.setBrowserCodeVersion(G.convertBROWSER_VERSION(this.browserCodeVersion));
		result.setRetrnCnt(this.retrnCnt);
		result.setAvalCnt(this.avalCnt);
		result.setType(this.type);
		
		result.setSvcTpCode(this.svcTpCode);
		result.setChrgTpCode(this.chrgTpCode);
		
		result.generateKey();
		return result;
	}
	
	public String getAdverId() {
		return adverId;
	}
	public void setAdverId(String adverId) {
		this.adverId = adverId;
	}
	public String getAdvrtsPrdtCode() {
		return advrtsPrdtCode;
	}
	public void setAdvrtsPrdtCode(String advrtsPrdtCode) {
		this.advrtsPrdtCode = advrtsPrdtCode;
	}
	public String getAdvrtsTpCode() {
		return advrtsTpCode;
	}
	public void setAdvrtsTpCode(String advrtsTpCode) {
		this.advrtsTpCode = advrtsTpCode;
	}
	public String getAltDttm() {
		return altDttm;
	}
	public void setAltDttm(String altDttm) {
		this.altDttm = altDttm;
	}
	public String getBrowserCodeVersion() {
		return browserCodeVersion;
	}
	public void setBrowserCodeVersion(String browserCodeVersion) {
		this.browserCodeVersion = browserCodeVersion;
	}
	public String getBrowserTpValue() {
		return browserTpValue;
	}
	public void setBrowserTpValue(String browserTpValue) {
		this.browserTpValue = browserTpValue;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDeviceTpValue() {
		return deviceTpValue;
	}
	public void setDeviceTpValue(String deviceTpValue) {
		this.deviceTpValue = deviceTpValue;
	}
	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaScriptNo() {
		return mediaScriptNo;
	}
	public void setMediaScriptNo(String mediaScriptNo) {
		this.mediaScriptNo = mediaScriptNo;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOsTpValue() {
		return osTpValue;
	}
	public void setOsTpValue(String osTpValue) {
		this.osTpValue = osTpValue;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public int getRetrnCnt() {
		return retrnCnt;
	}
	public void setRetrnCnt(int retrnCnt) {
		this.retrnCnt = retrnCnt;
	}
	public int getAvalCnt() {
		return avalCnt;
	}
	public void setAvalCnt(int avalCnt) {
		this.avalCnt = avalCnt;
	}
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getPltfomTpCode() {
		return pltfomTpCode;
	}
	public void setPltfomTpCode(String pltfomTpCode) {
		this.pltfomTpCode = pltfomTpCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStatsDttm() {
		return statsDttm;
	}
	public void setStatsDttm(String statsDttm) {
		this.statsDttm = statsDttm;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}

	public String getChrgTpCode() {
		return chrgTpCode;
	}

	public void setChrgTpCode(String chrgTpCode) {
		this.chrgTpCode = chrgTpCode;
	}

	public String getSvcTpCode() {
		return svcTpCode;
	}

	public void setSvcTpCode(String svcTpCode) {
		this.svcTpCode = svcTpCode;
	}
}
