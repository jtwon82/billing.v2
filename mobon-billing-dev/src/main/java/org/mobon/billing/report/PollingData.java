package org.mobon.billing.report;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobon.billing.model.v15.FrameRtbData;

import net.sf.json.JSONArray;

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
//    @JsonAlias({ "userId", "u" })				private String advertiserId="";
    @JsonAlias({ "scriptUserId", "scriptId", "mediaid" })	private String scriptUserId="";
	@JsonAlias({"site_code", "sc"})				private String siteCode="";
    @JsonAlias({"media_code", "s", "no", "script_no", "mediaCode"})		private String mediaCode="";
	@JsonProperty("type")						private String type="";
	
	@JsonProperty("ordCode")					private String ordCode="";
    @JsonProperty("point")						private float point=0;
    @JsonProperty("mpoint")						private float mpoint=0;
    @JsonAlias({ "viewcnt1", "viewcnt" })		private int viewcnt1=0;
    @JsonAlias({"viewcnt2", "viewCnt2"})					private int viewcnt2=0;
    @JsonAlias({"viewcnt3", "viewCnt3"})					private int viewcnt3=0;
    @JsonProperty("clickcnt")					private int clickcnt=0;
    @JsonProperty("ordercnt")					private int ordercnt=0;
    
    @JsonProperty("direct")						private String direct="";
    
    @JsonAlias({ "userId", "u" })				private String userId="";
    @JsonProperty("frameCombiKey")				private String frameCombiKey="";
    
    @JsonProperty("frameId")					private String frameId="";
	@JsonProperty("frameCycleNum")				private int frameCycleNum=0;
//	@JsonProperty("frameCycleNum")				private int frameCycleNum=0;
	@JsonProperty("frameSelector")				private String frameSelector="0";
	@JsonProperty("prdtTpCode")					private String prdtTpCode="";
	@JsonProperty("price")						private float price=0;
	@JsonAlias({"advrtsTpCode", "fmAdvrtsTpCode"})				private String advrtsTpCode="";
	//FrameSizeData 
	@JsonProperty("adverProdData") 				private JSONArray adverProdData = null;
	@JsonProperty("frameSize")					private String frameSize = "";
	@JsonAlias({"ip", "keyIp"})							private String ip = "";
	@JsonProperty("cookieDirect")				private String cookieDirect =  "";
	@JsonProperty("noExposureYN")				private boolean expousreYN = false; 	//미노출 
	@JsonProperty("cookieInDirect") 			private int cookieInDirect = 0;
	@JsonProperty("frameSendTpCode")			private String frameSendTpCode = "";
	@JsonProperty("ctgrNo")						private String ctgrNo = "";		//카테고리 
	
	@JsonProperty("frameKaistRstCode")			private String frameKaistRstCode = "";
	@JsonProperty("frameRtbTypeCode")			private String frameRtbTypeCode = "";
	
	
	private String bnrCode = "";
	private String cate1 = "";
	private String imgTpCode="";
	private String pCode = "";
	private String inHour = "0";
	private String matrAlgmSeq = "0";
	
	private int browserSessionOrderCnt = 0;
	private float browserSessionOrderAmt = 0;
	private int sessionOrderCnt = 0;
	private float sessionOrderAmt = 0;
	private int directOrderCnt = 0;
	private float directOrderAmt = 0;
	
	private int recognitionYyyymmdd = 0;
	
	private BigDecimal divisionViewCnt;
	
		
		
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String toStringReport() {
		return String.format("%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s", this.getYyyymmdd(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getMediaCode()
				, this.getFrameId(), this.getFrameCycleNum(), this.getFrameSelector(), this.getType(), this.getViewcnt1(), this.getClickcnt());
	}

//	public String getKeyNew() {
//    	return String.format("%s.%s.%s.%s.%s.%s", this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getMediaCode());
//    }
	
//	public NearData toNearData() {
//		NearData result = new NearData();
//
//		result.setKey(this.getKey());
//		result.setSendDate(this.getSendDate());
//		
//		result.setYyyymmdd(this.getYyyymmdd());
//		result.setPlatform(this.getPlatform());
//		result.setProduct(this.getProduct());
//		result.setAdGubun(this.getAdGubun());
//		result.setSiteCode(this.getSiteCode());
//		result.setScriptNo(Integer.parseInt( StringUtils.trimToNull2(this.getMediaCode(),"0") ));
//		result.setAdvertiserId(this.getAdvertiserId());
//		result.setScriptUserId(this.getScriptUserId());
//		
//		result.setViewCnt(this.getViewcnt1());
//		result.setViewCnt2(this.getViewcnt2());
//		result.setViewCnt3(this.getViewcnt3());
//		result.setClickCnt(this.getClickcnt());
//		result.setPoint(this.getPoint());
//		result.setMpoint(this.getMpoint());
//		
//		return result;
//	}
	
	public FrameRtbData toFrameRtbData() {
		FrameRtbData result = new FrameRtbData();
		
		result.setKey(this.getKey());
		result.setSendDate(this.getSendDate());
		
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(Integer.parseInt( StringUtils.trimToNull2(this.getMediaCode(),"0") ));
//		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		
		result.setDumpType(this.getDumpType());
		
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setClickCnt(this.getClickcnt());
		result.setOrderCnt(this.getOrdercnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		
		result.setFrameId(this.getFrameId());
		result.setFrameCycleNum(this.getFrameCycleNum());
		result.setMediaCode(this.getMediaCode());
		result.setFrameSelector(StringUtils.trimToNull2(this.getFrameSelector(), "0"));
		
		if ("".equals(this.getPrdtTpCode()) 
				&& !"".equals(this.getFrameId())) {			
			String frameId = String.valueOf(this.getFrameId().charAt(1)); 
			this.setPrdtTpCode("Y".equals(frameId)?"01":"02");
		}
		
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setType(this.getType());
		result.setAdvrtsTpCode(this.getAdvrtsTpCode());
		
		result.setPrice(this.getPrice());
		result.setUserId(this.getUserId());
		
		result.setOrdCode(this.getOrdCode());
		
		result.setFrameCombiKey(this.getFrameCombiKey());

		// 광고주별 통계 배너코드 세팅
		if(StringUtils.isNotEmpty(this.getFrameId())){
			result.setBnrCode(this.getFrameId().substring(10, 13));
		} else if (StringUtils.isNotEmpty(this.getFrameCombiKey())) {
			result.setBnrCode(this.getFrameCombiKey().substring(3,6));
		} 
		
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize( this.getFrameSize());
		result.setCate1(this.getCate1());
		result.setImgTpCode(this.getImgTpCode());
		result.setpCode(this.getpCode());
		result.setMatrAlgmSeq(this.getMatrAlgmSeq());
		
		result.setBrowserSessionOrderAmt(this.getBrowserSessionOrderAmt());
		result.setBrowserSessionOrderCnt(this.getBrowserSessionOrderCnt());
		result.setSessionOrderAmt(this.getSessionOrderAmt());
		result.setSessionOrderCnt(this.getSessionOrderCnt());
		result.setDirectOrderAmt(this.getDirectOrderAmt());
		result.setDirectOrderCnt(this.getDirectOrderCnt());		
		result.setIp(this.getIp());
		result.setCookieDirect(this.getCookieDirect());
		
		result.setFrameSendTpCode(StringUtils.trimToNull2(this.getFrameSendTpCode(),"98"));
		result.setCtgrNo(StringUtils.trimToNull2(this.getCtgrNo(), ""));
		
		result.setDivisionViewCnt(this.getDivisionViewCnt() == null ? new BigDecimal(0) : this.getDivisionViewCnt());
		
		result.setFrameKaistRstCode(StringUtils.trimToNull2(this.getFrameKaistRstCode(),"998"));
		result.setFrameRtbTypeCode(StringUtils.trimToNull2(this.getFrameRtbTypeCode(), ""));
		
		return result;
	}

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
		
//		if("RTBReportData".equals(className)) {
//			setType("V");
//			
//		} else if("RTBDrcData".equals(className)) {
//			setType("C");
//			setClickcnt(1);
//			
//		} else if("ShortCutData".equals(className)) {
//			setType("C");
//			setClickcnt(1);
//			
//		}
		
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

//	public String getAdvertiserId() {
//		return advertiserId;
//	}
//
//	public void setAdvertiserId(String advertiserId) {
//		this.advertiserId = advertiserId;
//	}

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
			setOrdercnt(1);
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
    		clickcnt=1;
    	}
		return clickcnt;
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

	public String getPrdtTpCode() {
		
		return prdtTpCode;
	}

	public void setPrdtTpCode(String prdtTpCode) {		
		this.prdtTpCode = prdtTpCode;		
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getAdvrtsTpCode() {
		return advrtsTpCode;
	}

	public void setAdvrtsTpCode(String advrtsTpCode) {
		this.advrtsTpCode = advrtsTpCode;
	}

	public int getOrdercnt() {
		return ordercnt;
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

	public String getBnrCode() { return bnrCode; }

	public void setBnrCode(String bnrCode) { this.bnrCode = bnrCode; }

	public JSONArray getAdverProdData() {
		return adverProdData;
	}

	public void setAdverProdData(JSONArray adverProdData) {
		this.adverProdData = adverProdData;
	}

	public String getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(String frameSize) {
		this.frameSize = frameSize;
	}

	public String getCate1() {
		return cate1;
	}

	public void setCate1(String cate1) {
		this.cate1 = cate1;
	}

	public String getImgTpCode() {
		return imgTpCode;
	}

	public void setImgTpCode(String imgTpCode) {
		this.imgTpCode = imgTpCode;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getInHour() {
		return inHour;
	}

	public void setInHour(String inHour) {
		this.inHour = inHour;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getSessionOrderCnt() {
		return sessionOrderCnt;
	}

	public void setSessionOrderCnt(int sessionOrderCnt) {
		this.sessionOrderCnt = sessionOrderCnt;
	}

	public float getSessionOrderAmt() {
		return sessionOrderAmt;
	}

	public void setSessionOrderAmt(float sessionOrderAmt) {
		this.sessionOrderAmt = sessionOrderAmt;
	}

	public int getDirectOrderCnt() {
		return directOrderCnt;
	}

	public void setDirectOrderCnt(int directOrderCnt) {
		this.directOrderCnt = directOrderCnt;
	}

	public float getDirectOrderAmt() {
		return directOrderAmt;
	}

	public void setDirectOrderAmt(float directOrderAmt) {
		this.directOrderAmt = directOrderAmt;
	}

	public int getBrowserSessionOrderCnt() {
		return browserSessionOrderCnt;
	}

	public void setBrowserSessionOrderCnt(int browserSessionOrderCnt) {
		this.browserSessionOrderCnt = browserSessionOrderCnt;
	}

	public float getBrowserSessionOrderAmt() {
		return browserSessionOrderAmt;
	}

	public void setBrowserSessionOrderAmt(float browserSessionOrderAmt) {
		this.browserSessionOrderAmt = browserSessionOrderAmt;
	}

	public String getCookieDirect() {
		return cookieDirect;
	}

	public void setCookieDirect(String cookieDirect) {
		this.cookieDirect = cookieDirect;
	}

	public boolean getExpousreYN() {
		return expousreYN;
	}

	public void setExpousreYN(boolean expousreYN) {
		this.expousreYN = expousreYN;
	}

	public int getRecognitionYyyymmdd() {
		return recognitionYyyymmdd;
	}

	public void setRecognitionYyyymmdd(int recognitionYyyymmdd) {
		this.recognitionYyyymmdd = recognitionYyyymmdd;
	}

	public int getCookieInDirect() {
		return cookieInDirect;
	}

	public void setCookieInDirect(int cookieInDirect) {
		this.cookieInDirect = cookieInDirect;
	}

	public String getFrameSendTpCode() {
		return frameSendTpCode;
	}

	public void setFrameSendTpCode(String frameSendTpCode) {
		this.frameSendTpCode = frameSendTpCode;
	}

	public String getCtgrNo() {
		return ctgrNo;
	}

	public void setCtgrNo(String ctgrNo) {
		this.ctgrNo = ctgrNo;
	}

	public String getMatrAlgmSeq() {
		return matrAlgmSeq;
	}

	public void setMatrAlgmSeq(String matrAlgmSeq) {
		this.matrAlgmSeq = matrAlgmSeq;
	}

	public BigDecimal getDivisionViewCnt() {
		return divisionViewCnt;
	}

	public void setDivisionViewCnt(BigDecimal divisionViewCnt) {
		this.divisionViewCnt = divisionViewCnt;
	}

	public String getFrameKaistRstCode() {
		return frameKaistRstCode;
	}

	public void setFrameKaistRstCode(String frameKaistRstCode) {
		this.frameKaistRstCode = frameKaistRstCode;
	}

	public String getFrameRtbTypeCode() {
		return frameRtbTypeCode;
	}

	public void setFrameRtbTypeCode(String frameRtbTypeCode) {
		this.frameRtbTypeCode = frameRtbTypeCode;
	}
	
	
}
