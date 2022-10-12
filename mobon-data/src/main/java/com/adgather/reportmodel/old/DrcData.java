package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;

import net.sf.json.JSONArray;

public class DrcData extends ObjectToString implements Serializable{
	private static final long serialVersionUID = 1L;
	private String keyIp;
	private String au_id;
	private String u;
	private String gb;
	private String subadgubun;
	private String sc;
	private String s;
	private String mc;
	private String no;
	private String pCode;
	private String kno;
	private String kgr;
	private String mcgb;
	private String product;
	private float point;
	private long actDate;
	private String platform;
	private String mobonlinkcate = "";
	private String pb_gubun;
	private int freqLog;
	private String gender;
	private String age;
	private String scriptId;
	private String serviceHostId;
	private String abtests;
	private String subMda;

	// TODO : dump > was 이동
	private float mpoint;
	private boolean clickChk = true;
	private String scriptUserId;
	private String scriptNo;
	private String mediasiteNo;
	private String AD_TYPE;
	
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	private String statYn="Y";
	
	private String dumpType; // dumpObject type
	
	private int partition=0;
	private Long offset=0L;
	private String key="";
	
	// omitType
	private String omitType= "01";
	
	private String intgTpCode = "";
	private String crossbrYn = "";	// 크로스브라우져 여부
	private String intgYn= "0";		// 통합코드 여부
	private String kwrdSeq= "0";
	private String adcSeq= "0";
	private String ctgrNo= "0";
	private String ctgrYn= "";
	private String ergabt= "";
	private String ergdetail= "";

	private int tTime=0;
	
	//RTB용 추가
	private String frameId;
	private String prdtTpCode;
	private String frameCombiKey;
	private String frameType;

	private int cycleNum;
	private String frameSelector;

	private String userNearCode = ""; // 유저의 지역코드
	private String nearCode = ""; // 지역코드 동코드 이상 시군구
	private Boolean nearYn = false; // 지역광고 체크

	//앱 모수성과 체크
	private String fromApp="N";
	
	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	
	private String adPhoneNum="";
	
	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	private int aiCateNo= 0;
	private boolean noExposureYN  = false; // 미노출 여부
	private String mainDomainYN  = "Y"; // ADBLOCK 여부
	private boolean isCrossAuIdYN  = false; // CrossDevice 여부

	//FRME_IMG_DAY_STATS
	private JSONArray adverProdData = null;
	private String frameSize = "";
	
	private String frameMatrExposureYN = "Y";// 프레임 AB 테스트 구분값 
	private String frameSendTpCode = "";
	
	//소재 카피 
	private JSONArray mobAdGrpData = null;		// 소재카피 array 
	private String grpId = "";							//소재 카피 ID 
	private String grpTpCode = "";						//소재 카피 Tpcode
	private String subjectCopyTpCode = "";				//소재 카피 img_copy_tp_code
	private String regUserId="BATCH";
	private String advrtsStleTpCode = "";				//소재 카피 고정배너 여부

	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 chrgTpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무
	
	// 플러스콜 유효콜 시간
	private int avalCallTime = 0;				// 플러스콜 유효콜일 경우 시간 데이터
	private int dbCnvrsCnt = 0;					// DB 전환수
	
	public static DrcData fromHashMap(Map from) {
		DrcData result = new DrcData();
		result.AD_TYPE	 = StringUtils.trimToNull2(from.get("AD_TYPE"),"");
		result.abtests	 = StringUtils.trimToNull2(from.get("abtests"),"");
		//result.actDate	 = StringUtils.trimToNull2(from.get("actDate"));
		result.age	 = StringUtils.trimToNull2(from.get("age"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.clickChk	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("clickChk")));
		result.cycleNum	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cycleNum"),"0"));
		result.dumpType	 = StringUtils.trimToNull2(from.get("dumpType"),"");
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.frameId	 = StringUtils.trimToNull2(from.get("frameId"),"");
		result.prdtTpCode	 = StringUtils.trimToNull2(from.get("prdtTpCode"),"");
		result.frameCombiKey	 = StringUtils.trimToNull2(from.get("frameCombiKey"),"");
		result.frameType	 = StringUtils.trimToNull2(from.get("frameType"),"");
		result.frameSelector	 = StringUtils.trimToNull2(from.get("frameSelector"),"");
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.gb	 = StringUtils.trimToNull2(from.get("gb"),"");
		result.subadgubun	 = StringUtils.trimToNull2(from.get("subadgubun"),"");
		result.gender	 = StringUtils.trimToNull2(from.get("gender"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"),"");
		result.au_id	= StringUtils.trimToNull2(from.get("au_id"),"");
		result.limit	 = Integer.parseInt(StringUtils.trimToNull2(from.get("limit"),"0"));
		result.mc	 = StringUtils.trimToNull2(from.get("mc"),"");
		result.mcgb	 = StringUtils.trimToNull2(from.get("mcgb"),"");
		result.mediasiteNo	 = StringUtils.trimToNull2(from.get("mediasiteNo"),"0");
		result.mobonlinkcate	 = StringUtils.trimToNull2(from.get("mobonlinkcate"),"");
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.no	 = StringUtils.trimToNull2(from.get("no"),"0");
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.pCode	 = StringUtils.trimToNull2(from.get("pCode"),"");
		result.pb_gubun	 = StringUtils.trimToNull2(from.get("pb_gubun"),"");
		result.platform	 = StringUtils.trimToNull2(from.get("platform"),"");
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.product	 = StringUtils.trimToNull2(from.get("product"),"");
		result.s	 = StringUtils.trimToNull2(from.get("s"),"");
		result.sc	 = StringUtils.trimToNull2(from.get("sc"),"");
		result.scriptId	 = StringUtils.trimToNull2(from.get("scriptId"),"");
		result.scriptNo	 = StringUtils.trimToNull2(from.get("scriptNo"),"");
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.serviceHostId	 = StringUtils.trimToNull2(from.get("serviceHostId"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.u	 = StringUtils.trimToNull2(from.get("u"),"");
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"");
		result.interlock	= StringUtils.trimToNull2(from.get("interlock"),"01");
		result.setStatYn(StringUtils.trimToNull2(from.get("statYn"),"Y"));

		result.setNearYn(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("nearYn"),"false")));
		result.setNearCode(StringUtils.trimToNull2(from.get("nearCode"),""));

		// 앱 모수성과 체크
		result.setFromApp(StringUtils.trimToNull2(from.get("fromApp"), "N"));
		
		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		result.omitType = StringUtils.trimToNull2(from.get("omitType"),"01");
		
		result.intgTpCode	= StringUtils.trimToNull2(from.get("intgTpCode"),"");
		result.crossbrYn	= StringUtils.trimToNull2(from.get("crossbrYn"),"N");
		result.setIntgYn(StringUtils.trimToNull2(from.get("intgYn"),"N"));
		result.kno	 = StringUtils.trimToNull2(from.get("kno"),"0");
		result.kgr	= StringUtils.trimToNull2(from.get("kwrdGrpNo"),"0");
		result.setKwrdSeq(StringUtils.trimToNull2(from.get("kwrdSeq"),"0"));
		result.setAdcSeq(StringUtils.trimToNull2(from.get("adcSeq"),"0"));
		result.settTime(Integer.parseInt(StringUtils.trimToNull2(from.get("tTime"),"0")));
		result.setCtgrNo(StringUtils.trimToNull2(from.get("ctgrNo"),"0"));
		result.ctgrYn	= StringUtils.trimToNull2(from.get("ctgrYn"),"N");
		result.ergabt	= StringUtils.trimToNull2(from.get("ergabt"),"");
		result.ergdetail	= StringUtils.trimToNull2(from.get("ergdetail"),"");
		result.adPhoneNum	= StringUtils.trimToNull2(from.get("adPhoneNum"),"");
		
		result.setOsCode( StringUtils.trimToNull2(from.get("osCode"),"") );
		result.setBrowserCode( StringUtils.trimToNull2(from.get("browserCode"),"") );
		result.setBrowserCodeVersion( StringUtils.trimToNull2(from.get("browserCodeVersion"),"") );
		result.setDeviceCode( StringUtils.trimToNull2(from.get("deviceCode"),"") );

		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"), "0")) );
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		//FRME_IMG_DAY_STATS
		result.setAdverProdData((JSONArray) from.get("adverProdData"));
		result.setFrameSize(StringUtils.trimToNull2(from.get("frameSize"), ""));
		
		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));
		//소재카피
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp") != null ?from.get("mobAdGrp") : null ));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"),"99"));

		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
		
		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));
		
		// 플러스콜
		result.setAvalCallTime(Integer.parseInt(StringUtils.trimToNull2(from.get("avalCallTime"),"0")));
		result.setDbCnvrsCnt(Integer.parseInt(StringUtils.trimToNull2(from.get("dbCnvrsCnt"),"0")));
		
		return result;
	}
	
	public BaseCVData toBaseCVData(){
		BaseCVData result = new BaseCVData();
		// base
		result.setHh(this.getHh());
		result.setYyyymmdd(DateUtils.getDate2("yyyyMMdd", this.getSendDate()));
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getGb());
		result.setSubadgubun(this.getSubadgubun());
		result.setSiteCode(this.getSc());
		if( !"0".equals(this.getNo()) && StringUtils.isNumeric(this.getNo()) ) result.setNo( Long.parseLong(this.getNo()) );
		//if(StringUtils.isNotEmpty(this.getScriptNo()) && !"null".equals(this.getScriptNo()) ) result.setScriptNo(Integer.parseInt(this.getScriptNo()));
		if(StringUtils.isNotEmpty(this.getMediasiteNo()) && !"null".equals(this.getMediasiteNo()) ) result.setMediasiteNo(Integer.parseInt(this.getMediasiteNo()));
		if(StringUtils.isNotEmpty(this.getMc())) result.setScriptNo( Integer.parseInt(this.getMc()) );
		result.setAdvertiserId(this.getU());
		result.setScriptUserId(this.getScriptUserId());
		result.setType("C");
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());

		// sum
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		result.setClickCnt(1);
		
		// append
		result.setMobonLinkCate(this.getMobonlinkcate());
		result.setDumpType(this.getDumpType());
		result.setSendDate(this.getSendDate());
		result.setFreqLog(this.getFreqLog());
		result.setKeyIp(this.getKeyIp());
		result.setAu_id(this.getAu_id());
		result.setMcgb(this.getMcgb());
		result.setpCode(this.getpCode());
		result.setGender(this.getGender());
		result.setUserAge(this.getAge());
		result.setServiceHostId(this.getServiceHostId());
		result.setAbTest(this.getAbtests());
		result.setMpoint(this.getMpoint());
		result.setClickChk(this.isClickChk());
		result.setAD_TYPE(this.getAD_TYPE());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		result.setFrameCycleNum(this.getCycleNum());
		result.setFrameSelector(this.getFrameSelector());
		result.setSubMda(this.getSubMda());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setClassName(this.getClassName());
		
		result.setNearYn(this.getNearYn());
		result.setNearCode(this.getNearCode());
		
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
		result.setErgdetail(this.getErgdetail());
		result.setDeviceCode(G.convertDEVICE_TP_CODE(this.getDeviceCode()));
		result.setOsCode(G.convertOS_TP_CODE(this.getOsCode()));
		result.setBrowserCode(G.convertBROWSER_TP_CODE(this.getBrowserCode()));
		result.setBrowserCodeVersion(G.convertBROWSER_VERSION(this.getBrowserCodeVersion()));
		
		result.setAdPhoneNum(this.getAdPhoneNum());
		result.setAiCateNo(this.getAiCateNo());
		result.setNoExposureYN(this.isNoExposureYN());
		result.setMainDomainYN(this.getMainDomainYN());
		result.setCrossAuIdYN(this.isCrossAuIdYN());

		//FRME_IMG_DAY_STATS
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize(this.getFrameSize());		

		result.setFrameMatrExposureYN(this.getFrameMatrExposureYN());
		result.setFrameSendTpCode(this.getFrameSendTpCode());
		
		//소재 카피 
		result.setMobAdGrpData(this.getMobAdGrpData());
		result.setGrpId(this.getGrpId());
		result.setGrpTpCode(this.getGrpTpCode());
		result.setSubjectCopyTpCode(this.getSubjectCopyTpCode());
		result.setAdvrtsStleTpCode(this.getAdvrtsStleTpCode());

		result.setFrameCombiTargetYn(this.getFrameCombiTargetYn());
		result.setRegUserId(this.getRegUserId());
		
		//클릭프리컨시
		result.setChrgTpCode(this.getChrgTpCode());
		result.setSvcTpCode(this.getSvcTpCode());
		
		// Ai캠페인
		result.setAiType(this.getAiType());
		
		// 플러스콜
		result.setAvalCallTime(this.getAvalCallTime());
		result.setDbCnvrsCnt(this.getDbCnvrsCnt());
		
		result.generateKey();
		return result;
	}
	
	public String getMobonlinkcate() {
		return mobonlinkcate;
	}
	public void setMobonlinkcate(String mobonlinkcate) {
		this.mobonlinkcate = mobonlinkcate;
	}

	public float getPoint() {
		return point;
	}
	public void setPoint(float point) {
		this.point = point;
	}
	public String getInfo(String st){
		return st + toString();
	}
	public String getKeyIp() {
		return keyIp;
	}
	public void setKeyIp(String keyIp) {
		this.keyIp = keyIp;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	public String getSc() {
		return sc;
	}
	public void setSc(String sc) {
		this.sc = sc;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public long getActDate() {
		return actDate;
	}
	public void setActDate(long actDate) {
		this.actDate = actDate;
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
	
	public int getCycleNum() {
		return cycleNum;
	}
	public void setCycleNum(int cycleNum) {
		this.cycleNum = cycleNum;
	}
	public String getFrameSelector() {
		return frameSelector;
	}
	public void setFrameSelector(String frameSelector) {
		this.frameSelector = frameSelector;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPb_gubun() {
		return pb_gubun;
	}
	public void setPb_gubun(String pb_gubun) {
		this.pb_gubun = pb_gubun;
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
	public String getAD_TYPE() {
		return AD_TYPE;
	}
	public void setAD_TYPE(String aD_TYPE) {
		AD_TYPE = aD_TYPE;
	}
	public String getMediasiteNo() {
		return mediasiteNo;
	}
	public void setMediasiteNo(String mediasiteNo) {
		this.mediasiteNo = mediasiteNo;
	}
	public String getScriptNo() {
		return scriptNo;
	}
	public void setScriptNo(String scriptNo) {
		this.scriptNo = scriptNo;
	}
	public String getScriptUserId() {
		return scriptUserId;
	}
	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}
	public boolean isClickChk() {
		return clickChk;
	}
	public void setClickChk(boolean clickChk) {
		this.clickChk = clickChk;
	}
	public float getMpoint() {
		return mpoint;
	}
	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}
	public String getSubMda() {
		return subMda;
	}
	public void setSubMda(String subMda) {
		this.subMda = subMda;
	}
	public int getPartition() {
		return partition;
	}
	public void setPartition(int partition) {
		this.partition = partition;
	}
	public Long getOffset() {
		return offset;
	}
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getInterlock() {
		return interlock;
	}
	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}
	public String getUserNearCode() {
		return userNearCode;
	}
	public void setUserNearCode(String userNearCode) {
		this.userNearCode = userNearCode;
	}
	public String getNearCode() {
		return nearCode;
	}
	public void setNearCode(String nearCode) {
		this.nearCode = nearCode;
	}
	public Boolean getNearYn() {
		return nearYn;
	}
	public void setNearYn(Boolean nearYn) {
		this.nearYn = nearYn;
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
	public String getScriptId() {
		return scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
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

	public String getSubadgubun() {
		return subadgubun;
	}

	public void setSubadgubun(String subadgubun) {
		this.subadgubun = subadgubun;
	}

	public String getAu_id() {
		return au_id;
	}

	public void setAu_id(String au_id) {
		this.au_id = au_id;
	}

	public int getAiCateNo() {
		return aiCateNo;
	}

	public void setAiCateNo(int aiCateNo) {
		this.aiCateNo = aiCateNo;
	}

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

	public int getAvalCallTime() {
		return avalCallTime;
	}

	public void setAvalCallTime(int avalCallTime) {
		this.avalCallTime = avalCallTime;
	}

	public int getDbCnvrsCnt() {
		return dbCnvrsCnt;
	}

	public void setDbCnvrsCnt(int dbCnvrsCnt) {
		this.dbCnvrsCnt = dbCnvrsCnt;
	}

	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}

	public String getAdvrtsStleTpCode() {
		return advrtsStleTpCode;
	}

	public void setAdvrtsStleTpCode(String advrtsStleTpCode) {
		this.advrtsStleTpCode = advrtsStleTpCode;
	}
}