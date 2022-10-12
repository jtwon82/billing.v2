package com.mobon.billing.sample.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobon.billing.sample.util.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData {

	@JsonAlias({"key"})							private String key="";
	@JsonAlias({"className"})					private String className="";
	@JsonProperty("dumpType")	   			 	private String dumpType="";
	@JsonProperty("sendDate")					private String sendDate="";
	
	@JsonAlias({"yyyymmdd"})					private String yyyymmdd="";
	@JsonProperty("platform")	    			private String platform="";
	@JsonProperty("product")	    			private String product="nor";
    @JsonAlias({ "adGubun", "gb", "gubun" })	private String adGubun="";
    @JsonAlias({ "userId", "u" })				private String advertiserId="";
    @JsonAlias({ "scriptUserId", "scriptId" })	private String scriptUserId="";
	@JsonAlias({"site_code", "sc"})				private String siteCode="";
    @JsonAlias({"media_code", "s", "no"})		private String mediaCode="0";
	//    @JsonAlias({"media_code", "s", "no"})		private String scriptNo="0";
    @JsonProperty("userId")						private String userId="";
    @JsonProperty("type")						private String type="";
	
	@JsonAlias({ "viewcnt1", "viewcnt" })		private int viewcnt1=0;
    @JsonProperty("viewcnt2")					private int viewcnt2=0;
    @JsonProperty("viewcnt3")					private int viewcnt3=0;
    @JsonProperty("clickcnt")					private int clickcnt=0;
    @JsonProperty("point")						private float point=0;
    @JsonProperty("mpoint")						private float mpoint=0;
    
    @JsonProperty("ordCode")					private String ordCode="";
    @JsonProperty("ordercnt")					private int ordercnt=0;
    @JsonProperty("direct")						private String direct="";
    @JsonProperty("price")						private int price=0;
    
    // 프레임데이터
    @JsonProperty("frameCombiKey")				private String frameCombiKey="";
    @JsonProperty("frameId")					private String frameId="";
	@JsonProperty("frameCycleNum")				private int frameCycleNum=0;
	@JsonProperty("frameSelector")				private String frameSelector="0";
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public SampleVo toSampleVo() {
		SampleVo result = new SampleVo();
		result.setDumpType(this.getDumpType());
		result.setSendDate(this.getSendDate());
		
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(Integer.parseInt( StringUtils.trimToNull2(this.getMediaCode(),"0") ));
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		result.setType(this.getType());

		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setClickCnt(this.getClickcnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		
		return result;
	}
	
//	public FrameRtbData toFrameRtbData() {
//		FrameRtbData result = new FrameRtbData();
//		
//		result.setKey(this.getKey());
//		result.setSendDate(this.getSendDate());
//		
//		result.setYyyymmdd(this.getYyyymmdd());
//		result.setPlatform(this.getPlatform());
//		result.setProduct(this.getProduct());
//		result.setAdGubun(this.getAdGubun());
//		result.setSiteCode(this.getSiteCode());
//		result.setScriptNo(Integer.parseInt( StringUtils.trimToNull2(this.getScriptNo(),"0") ));
//		result.setAdvertiserId(this.getAdvertiserId());
//		result.setScriptUserId(this.getScriptUserId());
//		
//		result.setDumpType(this.getDumpType());
//		
//		result.setViewCnt(this.getViewcnt1());
//		result.setViewCnt2(this.getViewcnt2());
//		result.setViewCnt3(this.getViewcnt3());
//		result.setClickCnt(this.getClickcnt());
//		result.setOrderCnt(this.getOrdercnt());
//		result.setPoint(this.getPoint());
//		result.setMpoint(this.getMpoint());
//		
//		result.setFrameId(this.getFrameId());
//		result.setFrameCycleNum(this.getFrameCycleNum());
//		result.setMediaCode(this.getMediaCode());
//		result.setFrameSelector(this.getFrameSelector());
//		result.setPrdtTpCode(this.getPrdtTpCode());
//		result.setType(this.getType());
//		result.setAdvrtsTpCode(this.getAdvrtsTpCode());
//		
//		result.setPrice(this.getPrice());
//		result.setUserId(this.getUserId());
//		
//		result.setOrdCode(this.getOrdCode());
//		
//		result.setFrameCombiKey(this.getFrameCombiKey());
//
//		return result;
//	}

	public String getKey() {
		return this.key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getYyyymmdd() {
		try {
			if(G.DRCCHARGE.equals(this.getDumpType())
					|| "RTBReportData".equals(className)
					|| "RTBDrcData".equals(className)
					|| "ShortCutData".equals(className)
					) {
				
				return  String.format("%s", DateUtils.getDate("yyyyMMdd", DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", this.getSendDate()))) ;
			}
		}catch(Exception e) {
		}
		
		return yyyymmdd;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		
		if("RTBReportData".equals(className)) {
			setType("V");
			
		} else if("RTBDrcData".equals(className)) {
			setType("C");
			
		} else if("ShortCutData".equals(className)) {
			setType("C");
			
		}
		
		this.className = className;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

    public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
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

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		
		if(G.DRCCHARGE.equals(dumpType) || G.SHOPCONCHARGE.equals(dumpType) ) {
			setType("C");
			
		} else if(G.NORMALCHARGE.equals(dumpType) || G.MOBILECHARGE.equals(dumpType) ) {
			setType("V");
			
		} else if(G.SKYCHARGE.equals(dumpType)) {
			if(G.VIEW.equals(this.getType())) {
				setType("V");
			}else {
				setType("C");
			}

		} else if(G.ICOCHARGE.equals(dumpType)) {
			setType("V");

		} else if(G.PLAY_LINK_CHARGE.equals(dumpType)) {
			if(G.VIEW.equals(this.getType())) {
				setType("V");
			}else {
				setType("C");
			}

		} else if(G.ACTIONCHARGE.equals(dumpType)) {
			setType("A");
			
		} else if(G.CONVERSIONCHARGE.equals(dumpType)) {
			setType("CONV");
			
		}
		
		this.dumpType = dumpType;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
	}
	
	public float getMpoint() {
		return mpoint;
	}

	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}

	public int getViewcnt1() {
		return viewcnt1;
	}

	public void setViewcnt1(int viewcnt1) {
		this.viewcnt1 = viewcnt1;
	}

	public int getViewcnt2() {
		return viewcnt2;
	}

	public void setViewcnt2(int viewcnt2) {
		this.viewcnt2 = viewcnt2;
	}

	public int getViewcnt3() {
		return viewcnt3;
	}

	public void setViewcnt3(int viewcnt3) {
		this.viewcnt3 = viewcnt3;
	}
	
    public int getClickcnt() {
    	if( "C".equals(this.getType()) ) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
	}

	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	public String getMediaCode() {
		return mediaCode;
	}

	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}
    
    public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}


    public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public int getFrameCycleNum() {
		return frameCycleNum;
	}

	public void setFrameCycleNum(int frameCycleNum) {
		this.frameCycleNum = frameCycleNum;
	}

	public String getFrameSelector() {
		return frameSelector;
	}

	public void setFrameSelector(String frameSelector) {
		this.frameSelector = frameSelector;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getOrdercnt() {
    	if( "CONV".equals(this.getType()) ) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
	}

	public void setOrdercnt(int ordercnt) {
		this.ordercnt = ordercnt;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}


	public String getFrameCombiKey() {
		return frameCombiKey;
	}

	public void setFrameCombiKey(String frameCombiKey) {
		this.frameCombiKey = frameCombiKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}

}
