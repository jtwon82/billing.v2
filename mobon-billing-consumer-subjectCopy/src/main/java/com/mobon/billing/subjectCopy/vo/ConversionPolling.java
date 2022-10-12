package com.mobon.billing.subjectCopy.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.sf.json.JSONArray;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionPolling implements Cloneable, Serializable {

	@JsonAlias({"yyyymmdd"})		private String yyyymmdd="";
	@JsonAlias({"platformTpCode"})	private String platformTpCode = "";
	@JsonAlias({"advrtsPrdtCode"})  private String advrtsPrdtCode= "";
	@JsonAlias({"advrtsTpCode"})	private String advrtsTpCode="";
	@JsonAlias({"itlTpCode"})		private String itlTpCode="";
	@JsonAlias({"imgTpCode"})		private String imgTpCode="";
	@JsonAlias({"siteCode"})		private String siteCode="";
	@JsonAlias({"mobAdGrpData"})	private JSONArray mobAdGrpData = null;
	@JsonAlias({"adverId"})			private String adverId = "";
	@JsonAlias({"mediaScriptNo"})	private String mediaScriptNo = "";
	@JsonAlias({"directYn"})		private String directYn = "";
	@JsonAlias({"sesionSelngYn"})	private String sesionSelngYn = "";
	@JsonAlias({"sesionSelng2Yn"})	private String sesionSelng2Yn="";
	@JsonAlias({"mobOrderYn"})		private String mobOrderYn = "";
	@JsonAlias({"orderCnt"})		private int orderCnt = 0;
	@JsonAlias({"orderAmt"})		private int orderAmt = 0;
	@JsonAlias({"orderQy"})			private int orderQy = 0;
	@JsonAlias({"noExposureYN"})	private boolean noExposureYn = false;
	@JsonAlias({"scriptUserId"}) 	private String scriptUserId = "";
	@JsonAlias({"sendDate"})		private String sendDate="";
	@JsonAlias({"frameId"})			private String frameId = "";
	@JsonAlias({"frameSelector"})	private String frameSelector = "";
	@JsonAlias({"advrtsStleTpCode"}) private String advrtsStleTpCode = "";

	
	private String grpId = "";							//소재 카피 ID 
	private String grpTpCode = "";						//소재 카피 Tpcode
	private String subjectCopyTpCode = "";				//소재 카피 img_copy_tp_code
	
	private String hh = "";

	private String kpiNo = "0";
	
	@Override
	public ConversionPolling clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (ConversionPolling) super.clone();
	}
	
	public String getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public String getPlatformTpCode() {
		return platformTpCode;
	}
	public void setPlatformTpCode(String platformTpcode) {
		this.platformTpCode = platformTpcode;
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
	public String getItlTpCode() {
		return itlTpCode;
	}
	public void setItlTpCode(String itlTpCode) {
		this.itlTpCode = itlTpCode;
	}
	public String getImgTpCode() {
		return imgTpCode;
	}
	public void setImgTpCode(String imgTpCode) {
		this.imgTpCode = imgTpCode;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getAdverId() {
		return adverId;
	}
	public void setAdverId(String adverId) {
		this.adverId = adverId;
	}
	public String getMediaScriptNo() {
		return mediaScriptNo;
	}
	public void setMediaScriptNo(String mediaScriptNo) {
		this.mediaScriptNo = mediaScriptNo;
	}
	public String getDirectYn() {
		return directYn;
	}
	public void setDirectYn(String directYn) {
		this.directYn = directYn;
	}
	public String getSesionSelngYn() {
		return sesionSelngYn;
	}
	public void setSesionSelngYn(String sesionSelngYn) {
		this.sesionSelngYn = sesionSelngYn;
	}
	public String getSesionSelng2Yn() {
		return sesionSelng2Yn;
	}
	public void setSesionSelng2Yn(String sesionSelng2Yn) {
		this.sesionSelng2Yn = sesionSelng2Yn;
	}
	public String getMobOrderYn() {
		return mobOrderYn;
	}
	public void setMobOrderYn(String mobOrderYn) {
		this.mobOrderYn = mobOrderYn;
	}
	public int getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}
	public int getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(int orderAmt) {
		this.orderAmt = orderAmt;
	}
	public int getOrderQy() {
		return orderQy;
	}
	public void setOrderQy(int orderQy) {
		this.orderQy = orderQy;
	}
	
	public String getKpiNo() {
		return kpiNo;
	}
	public void setKpiNo(String kpiNo) {
		this.kpiNo = kpiNo;
	}
	public JSONArray getMobAdGrpData() {
		return mobAdGrpData;
	}
	public void setMobAdGrpData(JSONArray mobAdGrpData) {
		this.mobAdGrpData = mobAdGrpData;
	}
	public String getGrpId() {
		return grpId;
	}
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	public String getGrpTpCode() {
		return grpTpCode;
	}
	public void setGrpTpCode(String grpTpCode) {
		this.grpTpCode = grpTpCode;
	}
	public String getSubjectCopyTpCode() {
		return subjectCopyTpCode;
	}
	public void setSubjectCopyTpCode(String subjectCopyTpCode) {
		this.subjectCopyTpCode = subjectCopyTpCode;
	}
	public boolean isNoExposureYn() {
		return noExposureYn;
	}
	public void setNoExposureYn(boolean noExposureYn) {
		this.noExposureYn = noExposureYn;
	}

	public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}

	public String getHh() {
		return hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public String getFrameSelector() {
		return frameSelector;
	}

	public void setFrameSelector(String frameSelector) {
		this.frameSelector = frameSelector;
	}

	public String getAdvrtsStleTpCode() {
		return advrtsStleTpCode;
	}

	public void setAdvrtsStleTpCode(String advrtsStleTpCode) {
		this.advrtsStleTpCode = advrtsStleTpCode;
	}
}
