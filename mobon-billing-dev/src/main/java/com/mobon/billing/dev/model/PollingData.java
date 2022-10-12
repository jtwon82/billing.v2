package com.mobon.billing.dev.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.FrameRtbData;
import com.mobon.billing.model.v15.NearData;
import com.mobon.billing.model.v15.ShortCutInfoData;

import net.sf.json.JSONObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData implements Serializable {

	@JsonAlias({"key"})				private String key="";
	@JsonAlias({"className"})		private String className="";
	@JsonProperty("dumpType")	    private String dumpType="";
	@JsonProperty("sendDate")		private String sendDate="";
	
	@JsonAlias({"yyyymmdd"})					private String yyyymmdd="";
	@JsonProperty("platform")	    			private String platform="";
	@JsonProperty("product")	    			private String product="nor";
    @JsonAlias({ "adGubun", "gb", "gubun" })	private String adGubun="";
    @JsonAlias({ "userId", "userid", "u" })				private String advertiserId="";
    @JsonAlias({ "scriptUserId", "scriptId", "mediaid" })	private String scriptUserId="";
	@JsonAlias({"site_code", "sc"})				private String siteCode="0";
    @JsonAlias({"media_code", "s", "no", "script_no"})		private String mediaCode="0";
    @JsonAlias({"media_code", "s", "no", "script_no"})		private String scriptNo="0";
	@JsonProperty("type")						private String type="";
    @JsonProperty("ordercnt")					private int ordercnt=0;
	
    @JsonProperty("point")						private String point="0";
    @JsonProperty("mpoint")						private String mpoint="0";
    @JsonAlias({ "viewcnt1", "viewcnt" })		private int viewcnt1=0;
    @JsonProperty("viewcnt2")					private int viewcnt2=0;
    @JsonProperty("viewcnt3")					private int viewcnt3=0;
    @JsonProperty("clickcnt")					private int clickcnt=0;
    
    public PollingData() {
    	this.yyyymmdd= DateUtils.getDate("yyyyMMdd", new Date());
    	this.platform= Arrays.asList("01","02").get(Math.abs(new Random().nextInt())%2);
    	this.product= Arrays.asList("01","02","03","04","05").get(Math.abs(new Random().nextInt())%5);
    	this.adGubun= Arrays.asList("01","02","03","04","05","06","07","08","09").get(Math.abs(new Random().nextInt())%9);
    	this.siteCode= testMD5((Math.abs(new Random().nextInt())%3000)+"");
    	this.viewcnt1= 1;
    	this.clickcnt= Math.abs(new Random().nextInt())%2;
    }

	public static String testMD5(String str) {
		String MD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			MD5 = null;
		}
		return MD5;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public PollingData(String message) {
		JSONObject jSONObject = JSONObject.fromObject(message);
		String className = (String) jSONObject.get("className");
		BaseCVData record=null;
		if ( G.AdChargeData.equals(className) ) {
			AdChargeData tmpAdCharge = AdChargeData.fromHashMap(jSONObject);
			record = tmpAdCharge.toBaseCVData();
//			record = processBaseCVData( record );		// AdChargeData
			
		} else if ( G.DrcData.equals(className) ) {
			DrcData tmpDrc = DrcData.fromHashMap(jSONObject);
			record = tmpDrc.toBaseCVData();
//			record = processBaseCVData( record );
			
		} else if ( G.ShortCutData.equals(className) ) {
			ShortCutData a = ShortCutData.fromHashMap(jSONObject);
			ShortCutInfoData tmp = a.toShortCutInfoData();
			record = tmp.toBaseCVData();
//			record = processShortCutInfoData(record);
			
		} else if ( G.RTBReportData.equals(className) || G.RTBDrcData.equals(className) ) {
			if( G.RTBReportData.equals(className) ) {
				RTBReportData tmp = RTBReportData.fromHashMap(jSONObject);
				record = tmp.toBaseCVData();
//				record = processRtbViewData(record);
			} else {
				RTBDrcData tmp = RTBDrcData.fromHashMap(jSONObject);
				record = tmp.toBaseCVData();
//				record = processRtbClickData(record);
			}
		}
		if(record!=null) {
			this.yyyymmdd= record.getYyyymmdd();
			this.platform= record.getPlatform();
			this.adGubun= record.getAdGubun();
			this.siteCode= record.getSiteCode();
			this.mediaCode= record.getScriptNo()+"";
			this.viewcnt1= record.getViewCnt();
			this.viewcnt2= record.getViewCnt2();
			this.viewcnt3= record.getViewCnt3();
			this.clickcnt= record.getClickCnt();
			this.type= record.getType();
		}
		
	}

	public String getKeyNew() {
    	return String.format("%s.%s.%s.%s.%s.%s", this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getScriptNo());
    }
	
	public String getSiteCodeScriptNo() {
		return String.format("%s.%s.%s.%s.%s.%s", this.getYyyymmdd(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getScriptNo(), this.getType());
	}
	
	public BaseCVData toBaseCVData() {
		BaseCVData result= new BaseCVData();
		result.setSendDate(this.sendDate);
		result.setDumpType(this.dumpType);
		result.setClassName(this.className);
		
		result.setYyyymmdd(this.yyyymmdd);
		result.setPlatform(this.platform);
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.siteCode);
		result.setScriptNo(Integer.parseInt(this.scriptNo));
		result.setAdvertiserId(this.advertiserId);
		result.setScriptUserId(this.scriptUserId);
		
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setClickCnt(this.getClickcnt());
		
		return result;
	}
	public NearData toNearData() {
		NearData result = new NearData();

		result.setKey(this.getKey());
		result.setSendDate(this.getSendDate());
		
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(Integer.parseInt( StringUtils.trimToNull2(this.getMediaCode(),"0") ));
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setClickCnt(this.getClickcnt());
		result.setPoint( Float.parseFloat(this.getPoint()) );
		result.setMpoint( Float.parseFloat(this.getMpoint()) );
		
		return result;
	}
	
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
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setClickCnt(this.getClickcnt());
		result.setPoint( Float.parseFloat(this.getPoint()) );
		result.setMpoint( Float.parseFloat(this.getMpoint()) );
		
		return result;
	}
	public void sumGethering(Object _from) {
		PollingData from = (PollingData)_from;
		
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewcnt1()>0)		this.setViewcnt1( this.getViewcnt1() + from.getViewcnt1() );
			if(from.getViewcnt3()>0)	this.setViewcnt3( this.getViewcnt3() + from.getViewcnt3() );
		}
		else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickcnt()>0 )	this.setClickcnt( this.getClickcnt() + from.getClickcnt() );
			//else this.setClickCnt( this.getClickCnt() + 1 );
		}
		else if ( G.VIEWCLICK.equals( from.getType() ) ) {
			if(from.getViewcnt1()>0)		this.setViewcnt1( this.getViewcnt1() + from.getViewcnt1() );
			if(from.getViewcnt3()>0)	this.setViewcnt3( this.getViewcnt3() + from.getViewcnt3() );

			if( from.getClickcnt()>0 )	this.setClickcnt( this.getClickcnt() + from.getClickcnt() );
			else this.setClickcnt( this.getClickcnt() + 1 );
		}
		
		this.setPoint( (Float.parseFloat(this.getPoint()) + Float.parseFloat(from.getPoint()))+"" );
		this.setMpoint( (Float.parseFloat(this.getMpoint()) + Float.parseFloat(from.getMpoint()))+"" );
		
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

	public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
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

	public String getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(String scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrdercnt() {
		return ordercnt;
	}

	public void setOrdercnt(int ordercnt) {
		this.ordercnt = ordercnt;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getMpoint() {
		return mpoint;
	}

	public void setMpoint(String mpoint) {
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
		return clickcnt;
	}

	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}

	public String getClassName() {
		return className;
	}

	public String getDumpType() {
		return dumpType;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

}
