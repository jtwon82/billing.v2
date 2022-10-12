package com.mobon.billing.logging.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData {

	@JsonProperty("sendDate")					public String sendDate="";
	@JsonAlias({"className"})					private String className="";
	@JsonProperty("dumpType")	   			 	private String dumpType="";
	@JsonProperty("type")						private String type="";
	@JsonAlias({"yyyymmdd"})					private String yyyymmdd="";
	@JsonAlias({"site_code", "sc"})				private String siteCode="";
    @JsonAlias({"media_code", "s", "no", "script_no"})		private String mediaCode="";
    @JsonAlias({ "userId", "u" })				private String userId="";
    @JsonAlias({ "scriptUserId", "scriptId" })	private String scriptUserId="";
    @JsonProperty("point")						private float point=0;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	private String getPointLoggingString() {
		return String.format("%s %s %s %s", this.getYyyymmdd(), this.getUserId(), this.getScriptUserId(), this.getPoint());
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
