package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.adgather.constants.old.GlobalConstants;
import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;

import net.sf.json.JSONArray;

public class ShortCutData extends ObjectToString implements Serializable{
	private static final long serialVersionUID = 1L;
	private String keyIp;
	private String au_id;
	private String userid;
	private String site_code;
	private String sdate;
	private String svc_type;
	private String gubun;
	private String mediaid;
	private int script_no;
	private int viewcnt = 0;
	private int clickcnt = 0;
	private int realclickcnt = 0;
	private Date downdate;
	private int freqLog = 0;
	private String gender;
	private String age;
	private String type;
	private String serviceHostId;
	private String abtests;
	
	private String dumpType; // dumpObject type
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	private String statYn="Y";

	private int partition;
	private Long offset;
	private String key;
	
	// TODO : dump > was �̵�
	private float point=0;
	private float mpoint=0;
	private String scriptUserId;
	
	//앱 모수성과 체크
	private String fromApp="N";

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	// omitType
	private String omitType= "01";

	private String intgTpCode = "";
	private String crossbrYn = "";	// 크로스브라우져 여부
	private String intgYn= "0";		// 통합코드 여부
	private String kno= "0";
	private String kgr= "0";
	private String kwrdSeq= "0";
	private String adcSeq= "0";
	private int tTime=0;
	private String ctgrNo= "0";
	private String ctgrYn= "";
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
	private boolean noExposureYN  = false; // 미노출 여부
	private String mainDomainYN  = "Y"; // ADBLOCK 여부

	private boolean isCrossAuIdYN  = false; // CrossDevice 여부

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
	private String advrtsStleTpCode = "";		//소재카피 고정배너 여부 02- 사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 chrgTpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무
	
	public static ShortCutData fromHashMap(Map from) {
		ShortCutData result = new ShortCutData();
		result.abtests	 = StringUtils.trimToNull2(from.get("abtests"),"");
		result.age	 = StringUtils.trimToNull2(from.get("age"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.clickcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickcnt"),"0"));
		//result.downdate	 = StringUtils.trimToNull2(from.get("downdate"));
		//result.day	 = StringUtils.trimToNull2(from.get("day"));
		//result.hours	 = StringUtils.trimToNull2(from.get("hours"));
		//result.minutes	 = StringUtils.trimToNull2(from.get("minutes"));
		//result.month	 = StringUtils.trimToNull2(from.get("month"));
		//result.seconds	 = StringUtils.trimToNull2(from.get("seconds"));
		//result.time	 = StringUtils.trimToNull2(from.get("time"));
		//result.timezoneOffset	 = StringUtils.trimToNull2(from.get("timezoneOffset"));
		//result.year	 = StringUtils.trimToNull2(from.get("year"));
		result.dumpType	 = StringUtils.trimToNull2(from.get("dumpType"),"");
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.gender	 = StringUtils.trimToNull2(from.get("gender"),"");
		result.gubun	 = StringUtils.trimToNull2(from.get("gubun"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"),"");
		result.au_id	 = StringUtils.trimToNull2(from.get("au_id"),"");
		//result.limit	 = StringUtils.trimToNull2(from.get("limit"));
		result.mediaid	 = StringUtils.trimToNull2(from.get("mediaid"),"");
		result.setMpoint(Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0")));
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.setPoint(Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0")));
		result.realclickcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("realclickcnt"),"0"));
		result.setScriptUserId(StringUtils.trimToNull2(from.get("scriptUserId"),""));
		result.script_no	 = Integer.parseInt(StringUtils.trimToNull2(from.get("script_no"),"0"));
		result.sdate	 = StringUtils.trimToNull2(from.get("sdate"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.serviceHostId	 = StringUtils.trimToNull2(from.get("serviceHostId"),"");
		result.site_code	 = StringUtils.trimToNull2(from.get("site_code"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.svc_type	 = StringUtils.trimToNull2(from.get("svc_type"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.type	 = StringUtils.trimToNull2(from.get("type"),"");
		result.userid	 = StringUtils.trimToNull2(from.get("userid"),"");
		result.viewcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt"),"0"));
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"");
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.setStatYn(StringUtils.trimToNull2(from.get("statYn"),"Y"));

		// 앱 모수성과 체크
		result.fromApp		= StringUtils.trimToNull2(from.get("fromApp"), "N");
		
		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));

		result.setOmitType(StringUtils.trimToNull2(from.get("omitType"),"01"));
		
		result.setIntgTpCode(StringUtils.trimToNull2(from.get("intgTpCode"),""));
		result.crossbrYn	= StringUtils.trimToNull2(from.get("crossbrYn"),"N");
		result.setIntgYn(StringUtils.trimToNull2(from.get("intgYn"),"N"));
		result.setKno(StringUtils.trimToNull2(from.get("kno"),"0"));
		result.setKgr(StringUtils.trimToNull2(from.get("kwrdGrpNo"),"0"));
		result.setKwrdSeq(StringUtils.trimToNull2(from.get("kwrdSeq"),"0"));
		result.setAdcSeq(StringUtils.trimToNull2(from.get("adcSeq"),"0"));
		result.settTime(Integer.parseInt(StringUtils.trimToNull2(from.get("tTime"),"0")));
		result.setCtgrNo(StringUtils.trimToNull2(from.get("ctgrNo"),"0"));
		result.ctgrYn	= StringUtils.trimToNull2(from.get("ctgrYn"),"N");
		result.setErgabt(StringUtils.trimToNull2(from.get("ergabt"),""));
		result.setErgdetail(StringUtils.trimToNull2(from.get("ergdetail"),"")); 
		
		result.setOsCode( StringUtils.trimToNull2(from.get("osCode"),"") );
		result.setBrowserCode( StringUtils.trimToNull2(from.get("browserCode"),"") );
		result.setBrowserCodeVersion( StringUtils.trimToNull2(from.get("browserCodeVersion"),"") );
		result.setDeviceCode( StringUtils.trimToNull2(from.get("deviceCode"),"") );

		result.setAdPhoneNum(StringUtils.trimToNull2(from.get("adPhoneNum"),""));
		
		result.setFrameId( StringUtils.trimToNull2(from.get("frameId"),"") );
		result.setPrdtTpCode( StringUtils.trimToNull2(from.get("prdtTpCode"),"") );
		result.setFrameCombiKey( StringUtils.trimToNull2(from.get("frameCombiKey"),"") );
		result.setFrameType( StringUtils.trimToNull2(from.get("frameType"),"") );
		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"),"0")) );
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));

		//소재 카피
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp") != null ?from.get("mobAdGrp") : null ));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"),"99"));

		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
			
		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));
		
		return result;
	}

	public ShortCutInfoData toShortCutInfoData(){
		ShortCutInfoData result = new ShortCutInfoData();
		// base
		result.setHh(this.getHh());
		result.setYyyymmdd(this.getSdate());
		result.setPlatform(GlobalConstants.UPPER_M);
		result.setProduct(GlobalConstants.MBW);
		result.setAdGubun(this.getGubun());
		result.setSiteCode(this.getSite_code());
		result.setScriptNo(this.getScript_no());
		result.setAdvertiserId(this.getUserid());
		result.setScriptUserId(this.getMediaid());
		result.setKno("0");
		result.setKgr(this.getKgr());
		result.setType("C");
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());

		// sum
		result.setClickCnt(1);
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		
		// append
		result.setSendDate(this.getSendDate());
		result.setKeyIp(this.getKeyIp());
		result.setAu_id(this.getAu_id());
		result.setFreqLog(this.getFreqLog());
		result.setType(this.getType());
		result.setServiceHostId(this.getServiceHostId());
		result.setRealClickCnt(this.getRealclickcnt());
		result.setAbTests(this.getAbtests());
		result.setRealClickCnt(this.getRealclickcnt());
		result.setProduct(this.getSvc_type());
		result.setDownDate(this.getDowndate());
		result.setGender(this.getGender());
		result.setAge(this.getAge());
		result.setViewCnt(this.getViewcnt());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setClassName(this.getClassName());
		
		result.setFromApp(this.getFromApp());
		
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());

		// 통계누락 여부
		result.setOmitType(this.getOmitType());
		
		result.setIntgTpCode(this.getIntgTpCode());
		result.setCrossbrYn(this.getCrossbrYn());
		result.setIntgYn(this.getIntgYn());
		result.setKwrdSeq(this.getKwrdSeq());
		result.setAdcSeq(this.getAdcSeq());
		result.settTime(this.gettTime());
		result.setCtgrNo(this.getCtgrNo());
		result.setCtgrYn(this.getCtgrYn());
		result.setErgabt(this.getErgabt());
		result.setErgdetail(this.getErgdetail());
		
		result.setDeviceCode(this.getDeviceCode());
		result.setOsCode(this.getOsCode());
		result.setBrowserCode(this.getBrowserCode());
		result.setBrowserCodeVersion(this.getBrowserCodeVersion());
		
		result.setAdPhoneNum(this.getAdPhoneNum());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		
		result.setAiCateNo(this.getAiCateNo());
		result.setNoExposureYN(this.isNoExposureYN());
		result.setMainDomainYN(this.getMainDomainYN());
		result.setCrossAuIdYN(this.isCrossAuIdYN());

		result.setFrameMatrExposureYN(this.getFrameMatrExposureYN());
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

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyIp() {
		return keyIp;
	}
	public void setKeyIp(String keyIp) {
		this.keyIp = keyIp;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getSvc_type() {
		return svc_type;
	}
	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getMediaid() {
		return mediaid;
	}
	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}
	public int getScript_no() {
		return script_no;
	}
	public void setScript_no(int script_no) {
		this.script_no = script_no;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public int getClickcnt() {
		return clickcnt;
	}
	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}
	public int getRealclickcnt() {
		return realclickcnt;
	}
	public void setRealclickcnt(int realclickcnt) {
		this.realclickcnt = realclickcnt;
	}
	public Date getDowndate() {
		return downdate;
	}
	public void setDowndate(Date downdate) {
		this.downdate = downdate;
	}
	public int getFreqLog() {
		return freqLog;
	}
	public void setFreqLog(int freqLog) {
		this.freqLog = freqLog;
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

	public String getInfo(String s){
		try{
			return s +toString();
		}catch(Exception e){
			return "getInfo:"+e;
		}
	}
	public String getServiceHostId() {
		return serviceHostId;
	}
	public void setServiceHostId(String serviceHostId) {
		this.serviceHostId = serviceHostId;
	}
	public String getAbtests() {
		return abtests;
	}
	public void setAbtests(String abtests) {
		this.abtests = abtests;
	}
	
	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Long getOffset() {
		return offset;
	}
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public int getPartition() {
		return partition;
	}
	public void setPartition(int partition) {
		this.partition = partition;
	}
	public String getInterlock() {
		return interlock;
	}
	public void setInterlock(String interlock) {
		this.interlock = interlock;
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

	public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
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

	public String getKno() {
		return kno;
	}

	public void setKno(String kno) {
		this.kno = kno;
	}

	public String getAdcSeq() {
		return adcSeq;
	}

	public void setAdcSeq(String adcSeq) {
		this.adcSeq = adcSeq;
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

	public String getStatYn() {
		return statYn;
	}

	public void setStatYn(String statYn) {
		this.statYn = statYn;
	}

	public String getKgr() {
		return kgr;
	}

	public void setKgr(String kgr) {
		this.kgr = kgr;
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
	public boolean isNoExposureYN() {
		return noExposureYN;
	}

	public String getMainDomainYN() {
		return mainDomainYN;
	}

	public void setNoExposureYN(boolean noExposureYN) {
		this.noExposureYN = noExposureYN;
	}

	public void setMainDomainYN(String mainDomainYN) {
		this.mainDomainYN = mainDomainYN;
	}

	public String getFrameMatrExposureYN() {
		return frameMatrExposureYN;
	}

	public void setFrameMatrExposureYN(String frameMatrExposureYN) {
		this.frameMatrExposureYN = frameMatrExposureYN;
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

	public String getAu_id() {
		return au_id;
	}

	public void setAu_id(String au_id) {
		this.au_id = au_id;
	}

	public String getFrameCombiTargetYn() {
		return frameCombiTargetYn;
	}

	public void setFrameCombiTargetYn(String frameCombiTargetYn) {
		this.frameCombiTargetYn = frameCombiTargetYn;
	}

	public boolean isCrossAuIdYN() {
		return isCrossAuIdYN;
	}

	public void setCrossAuIdYN(boolean crossAuIdYN) {
		isCrossAuIdYN = crossAuIdYN;
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