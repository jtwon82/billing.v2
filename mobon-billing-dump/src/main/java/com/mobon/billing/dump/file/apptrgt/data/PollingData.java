package com.mobon.billing.dump.file.apptrgt.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobon.billing.dump.utils.DateUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData {

	@JsonAlias({"className"})
	private String className="";

	@JsonProperty("dumpType")
    private String dumpType="";
	

	@JsonAlias({"yyyymmdd"})
	private String yyyymmdd="";
	
	@JsonProperty("sendDate")
	private String sendDate="";

	@JsonProperty("platform")
    private String platform="";

	@JsonProperty("product")
    private String product="nor";

    @JsonAlias({ "adGubun", "gb", "gubun" })
    private String adGubun="";

    @JsonAlias({ "u", "userId", "userid" })
    private String advertiserId="";
    
    @JsonAlias({ "scriptUserId", "scriptId" })
    private String scriptUserId="";

	@JsonAlias({"site_code", "sc"})
    private String siteCode="";
    
    @JsonAlias({"media_code", "s", "no", "script_no"})
    private String mediaCode="";
    
	@JsonProperty("type")
    private String type="";


    @JsonProperty("point")
    private String point="0";

    @JsonProperty("mpoint")
    private String mpoint="0";

    @JsonAlias({ "viewcnt1", "viewcnt" })
    private int viewcnt1=0;

    @JsonProperty("viewcnt2")
    private int viewcnt2=0;

    @JsonProperty("viewcnt3")
    private int viewcnt3=0;

    @JsonProperty("clickcnt")
    private int clickcnt=0;

	public String getKey() {
    	return String.format("%s.%s.%s.%s.%s.%s.%s.%s", this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getMediaCode(), this.getAdvertiserId(), this.getScriptUserId());
    }

    
	public String getYyyymmdd() {
		try {
			if("drcCharge".equals(this.getDumpType())
					|| "RTBReportData".equals(className)
					|| "RTBDrcData".equals(className)) {
				
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
			setClickcnt(1);
			
		}
		
		this.className = className;
	}

	public void setDumpType(String dumpType) {
		
		if("drcCharge".equals(dumpType) || "shopconCharge".equals(dumpType) ) {
			setType("C");
			setClickcnt(1);

		} else if("normalCharge".equals(dumpType) || "mobileCharge".equals(dumpType) ) {
			setType("V");
			
		} else if("skyCharge".equals(dumpType)) {
			if("V".equals(this.getType())) {
				setType("V");
			}else {
				setType("C");
			}

		} else if("icoCharge".equals(dumpType)) {
			setType("V");

		} else if("plCharge".equals(dumpType)) {
			if("V".equals(this.getType())) {
				setType("V");
			}else {
				setType("C");
			}

		} else if("actionCharge".equals(dumpType)) {
			setType("A");
			
		}
		
		this.dumpType = dumpType;
	}

}
