package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.adgather.constants.G;
import com.mobon.billing.model.ClickViewData;

import net.sf.json.JSONArray;

public class ShortCutInfoData extends ClickViewData implements Serializable {
	
	private String serviceHostId; // 서버아이피 serviceHostId
	private int realClickCnt; // 부정클릭판단 realClickCnt
	private String abTests; // abTests
	private Date downDate; // 다운날짜 downDate
	private String gender; // 성별 gender
	private String age; // 나이 age
	
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동

	private String dumpType; // dumpType

	//앱 모수성과 체크
	private String fromApp="N";

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	// omitType
	private String omitType= "01";
	
	private String intgTpCode= "0";
	private String crossbrYn = "";	// 크로스브라우져 여부
	private String intgYn= "0";		// 통합코드 여부
	private String kwrdSeq= "0";
	private String adcSeq= "0";
	private int tTime=0;
	private String ctgrNo= "0";
	private String ctgrYn = "";
	private String ergabt = "";
	private String ergdetail = "";

	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	private String adPhoneNum="";
	private String frameId="";
	private String prdtTpCode="";
	private String frameCombiKey="";
	private String frameType="";

	private int aiCateNo= 0;
	
	private String frameMatrExposureYN = "N";// 프레임 AB 테스트 구분값 
	private String frameSendTpCode = "";
	
	//소재 카피 
	private JSONArray mobAdGrpData = null;		// 소재카피 array 
	private String grpCode= "";					//소재카피 부모코드 
	private String grpSubjectId = "";			//소재 ID 
	private String grpSubjectTpCode = "";		//소재 코드 
	private String grpCopyId = "";				//카피 ID 
	private String grpCopyTpCode ="";			//카피 코드 
	private String grpImgTpCode = "";			//소재카피 이미지 코드
	private String advrtsStleTpCode = ""; 		//소재카피 고정배너 여부 02 - 사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
		
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 chrgTpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무
	
	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public String getPrdtTpCode() {
		return prdtTpCode;
	}

	public void setPrdtTpCode(String prdtTpCode) {
		this.prdtTpCode = prdtTpCode;
	}
	public String getFrameCombiKey() {
		return frameCombiKey;
	}

	public void setFrameCombiKey(String frameCombiKey) {
		this.frameCombiKey = frameCombiKey;
	}
	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	public ShortCutInfoData() {
	}

	public String getServiceHostId() {
		return serviceHostId;
	}

	public void setServiceHostId(String serviceHostId) {
		this.serviceHostId = serviceHostId;
	}

	public int getRealClickCnt() {
		return realClickCnt;
	}

	public void setRealClickCnt(int realClickCnt) {
		this.realClickCnt = realClickCnt;
	}

	public String getAbTests() {
		return abTests;
	}

	public void setAbTests(String abTests) {
		this.abTests = abTests;
	}

	public Date getDownDate() {
		return downDate;
	}

	public void setDownDate(Date downDate) {
		this.downDate = downDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	
	public String getFrameMatrExposureYN() {
		return frameMatrExposureYN;
	}

	public void setFrameMatrExposureYN(String frameMatrExposureYN) {
		this.frameMatrExposureYN = frameMatrExposureYN;
	}

	@Override
	public String generateKey() {
		// TODO Auto-generated method stub
//		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", getAdGubun(), getPlatform(), getYyyymmdd(), getSiteCode(), getScriptNo(), getKno(), getType(), getProduct(), getMcgb());
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getAdGubun(), this.getPlatform()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getType(), this.getYyyymmdd(), this.getProduct(), this.getSiteCode(), this.getKno()
				, this.getScriptNo(), this.getInterlock());
		grouping = String.format("[%s, %s]", getAdGubun(), getPlatform());
		return grouping;
	}

	@Override
	public void sumGethering(Object _from) {
		
	}


	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}
	
	public BaseCVData toBaseCVData(){
		BaseCVData result = new BaseCVData();

		result.setClassName(this.getClassName());
		result.setYyyymmdd(this.getYyyymmdd()); //.getSdate());
		result.setHh(this.getHh());
		result.setSendDate(this.getSendDate());
		result.setKeyCode(this.getKeyCode());
		result.setAdvertiserId(this.getAdvertiserId()); //.getUserid());
		result.setScriptUserId(this.getScriptUserId()); //.getMediaid());
		result.setProduct(this.getProduct()); //.getSvc_type());
		result.setPlatform(this.getPlatform());
		result.setType(this.getType());
		result.setSiteCode(this.getSiteCode()); //.getSite_code());
		result.setAdGubun(this.getAdGubun()); //.getGubun());
		result.setScriptNo(this.getScriptNo()); //.getScript_no());
		result.setKeyIp(this.getKeyIp());
		result.setAu_id(this.getAu_id());
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());
		result.setServiceHostId(this.getServiceHostId());
		
		result.setViewCnt(this.getViewCnt()); //.getViewcnt());
		result.setViewCnt2(this.getViewCnt2());
		result.setViewCnt3(this.getViewCnt3());
		result.setClickCnt(this.getClickCnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		
		result.setDumpType(this.getDumpType());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setAbTest(this.getAbTests()); //.getAbtests());
		result.setGender(this.getGender());
		result.setFreqLog(this.getFreqLog());

		result.setFromApp(this.getFromApp());
		
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());

		// 통계누락 여부
		result.setOmitType(this.getOmitType());
		
		result.setIntgTpCode(this.getIntgTpCode());
		result.setCrossbrYn(this.getCrossbrYn());
		result.setIntgYn(this.getIntgYn());
		result.setKno(this.getKno());
		result.setKgr(this.getKgr());
		result.setKwrdSeq(this.getKwrdSeq());
		result.setAdcSeq(this.getAdcSeq());
		result.settTime(this.gettTime());
		result.setCtgrNo(this.getCtgrNo());
		result.setCtgrYn(this.getCtgrYn());
		result.setErgabt(this.getErgabt());
		
		result.setDeviceCode(G.convertDEVICE_TP_CODE(this.getDeviceCode()));
		result.setOsCode(G.convertOS_TP_CODE(this.getOsCode()));
		result.setBrowserCode(G.convertBROWSER_TP_CODE(this.getBrowserCode()));
		result.setBrowserCodeVersion(G.convertBROWSER_VERSION(this.getBrowserCodeVersion()));
		
		result.setAdPhoneNum(this.getAdPhoneNum());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		
		result.setAiCateNo(this.getAiCateNo());
		result.setNoExposureYN(this.isNoExposureYN());
		result.setMainDomainYN(this.getMainDomainYN());
		result.setFrameSendTpCode(this.getFrameSendTpCode());
		
		result.setFrameCombiTargetYn(this.getFrameCombiTargetYn());
		
		//클릭프리컨시
		result.setChrgTpCode(this.getChrgTpCode());
		result.setSvcTpCode(this.getSvcTpCode());
		
		// Ai캠페인
		result.setAiType(this.getAiType());

		//소재카피
		result.setMobAdGrpData(this.getMobAdGrpData());
		result.setAdvrtsStleTpCode(this.getAdvrtsStleTpCode());

		result.generateKey();
		return result;
	}
	
	public String getInterlock() {
		return interlock;
	}

	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}

	public boolean isbHandlingStatsMobon() {
		return bHandlingStatsMobon;
	}

	public void setbHandlingStatsMobon(boolean bHandlingStatsMobon) {
		this.bHandlingStatsMobon = bHandlingStatsMobon;
	}

	public boolean isbHandlingStatsPointMobon() {
		return bHandlingStatsPointMobon;
	}

	public void setbHandlingStatsPointMobon(boolean bHandlingStatsPointMobon) {
		this.bHandlingStatsPointMobon = bHandlingStatsPointMobon;
	}

	public String getFromApp() {
		return fromApp;
	}

	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}

	public String getOmitType() {
		return omitType;
	}

	public void setOmitType(String omitType) {
		this.omitType = omitType;
	}
	
	public String getKwrdSeq() {
		return kwrdSeq;
	}

	public void setKwrdSeq(String kwrdSeq) {
		this.kwrdSeq = kwrdSeq;
	}

	public String getIntgYn() {
		return intgYn;
	}

	public void setIntgYn(String intgYn) {
		this.intgYn = intgYn;
	}

	public String getAdcSeq() {
		return adcSeq;
	}

	public void setAdcSeq(String adcSeq) {
		this.adcSeq = adcSeq;
	}

	public String getIntgTpCode() {
		return intgTpCode;
	}

	public void setIntgTpCode(String intgTpCode) {
		this.intgTpCode = intgTpCode;
	}

	public String getCrossbrYn() {
		return crossbrYn;
	}

	public void setCrossbrYn(String crossbrYn) {
		this.crossbrYn = crossbrYn;
	}

	public int gettTime() {
		return tTime;
	}

	public void settTime(int tTime) {
		this.tTime = tTime;
	}
	
	public String getCtgrNo() {
		return ctgrNo;
	}

	public void setCtgrNo(String ctgrNo) {
		this.ctgrNo = ctgrNo;
	}
	
	public String getCtgrYn() {
		return ctgrYn;
	}

	public void setCtgrYn(String ctgrYn) {
		this.ctgrYn = ctgrYn;
	}
	
	public String getErgabt() {
		return ergabt;
	}

	public void setErgabt(String ergabt) {
		this.ergabt = ergabt;
	}
	
	public String getErgdetail() {
		return ergdetail;
	}

	public void setErgdetail(String ergdetail) {
		this.ergdetail = ergdetail;
	}

	public String getOsCode() {
		return osCode;
	}
	public void setOsCode(String osCode) {
		this.osCode = osCode;
	}
	public String getBrowserCode() {
		return browserCode;
	}
	public void setBrowserCode(String browserCode) {
		this.browserCode = browserCode;
	}
	public String getBrowserCodeVersion() {
		return browserCodeVersion;
	}
	public void setBrowserCodeVersion(String browserCodeVersion) {
		this.browserCodeVersion = browserCodeVersion;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getAdPhoneNum() {
		return adPhoneNum;
	}

	public void setAdPhoneNum(String adPhoneNum) {
		this.adPhoneNum = adPhoneNum;
	}

	public int getAiCateNo() {
		return aiCateNo;
	}

	public void setAiCateNo(int aiCateNo) {
		this.aiCateNo = aiCateNo;
	}

	public String getFrameSendTpCode() {
		return frameSendTpCode;
	}

	public void setFrameSendTpCode(String frameSendTpCode) {
		this.frameSendTpCode = frameSendTpCode;
	}

	public JSONArray getMobAdGrpData() {
		return mobAdGrpData;
	}

	public void setMobAdGrpData(JSONArray mobAdGrpData) {
		this.mobAdGrpData = mobAdGrpData;
	}

	public String getGrpCode() {
		return grpCode;
	}

	public void setGrpCode(String grpCode) {
		this.grpCode = grpCode;
	}

	public String getGrpSubjectId() {
		return grpSubjectId;
	}

	public void setGrpSubjectId(String grpSubjectId) {
		this.grpSubjectId = grpSubjectId;
	}

	public String getGrpSubjectTpCode() {
		return grpSubjectTpCode;
	}

	public void setGrpSubjectTpCode(String grpSubjectTpCode) {
		this.grpSubjectTpCode = grpSubjectTpCode;
	}

	public String getGrpCopyId() {
		return grpCopyId;
	}

	public void setGrpCopyId(String grpCopyId) {
		this.grpCopyId = grpCopyId;
	}

	public String getGrpCopyTpCode() {
		return grpCopyTpCode;
	}

	public void setGrpCopyTpCode(String grpCopyTpCode) {
		this.grpCopyTpCode = grpCopyTpCode;
	}

	public String getGrpImgTpCode() {
		return grpImgTpCode;
	}

	public void setGrpImgTpCode(String grpImgTpCode) {
		this.grpImgTpCode = grpImgTpCode;
	}

	public String getFrameCombiTargetYn() {
		return frameCombiTargetYn;
	}

	public void setFrameCombiTargetYn(String frameCombiTargetYn) {
		this.frameCombiTargetYn = frameCombiTargetYn;
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

	public String getAiType() {
		return aiType;
	}

	public void setAiType(String aiType) {
		this.aiType = aiType;
	}

	public String getAdvrtsStleTpCode() {
		return advrtsStleTpCode;
	}

	public void setAdvrtsStleTpCode(String advrtsStleTpCode) {
		this.advrtsStleTpCode = advrtsStleTpCode;
	}
}
