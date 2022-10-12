package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;

import net.sf.json.JSONArray;


/**
 * RTB 클릭을 관리하는 적용 클래스
 * RTB 클릭정보가 담겨있음.
 * @date 2017. 2. 14.
 * @param 
 * @exception
 * @see
 */
public class RTBDrcData extends ObjectToString implements Serializable{
	private static final long serialVersionUID = 1L;
	private String keyIp;
	private String au_id;
	private String userId;
	private String gb;
	private String sc;
	private String no;
	private String pcode;
	private String kno;
	private String kgr;
	private String product;
	private long actDate;
	private String platform;
	private String mobonlinkcate = "";
	private int freqLog;
	private String gender;
	private String age;
	private String scriptId;
	private String serviceHostId;
	private String tagid;
	private String ab_type;
	private String rtb_type;
	private String abtest;
	private float point;

	private int scriptNo;
	private int scriptHirnkNo; // 자식의 모지면
	private String rtbUsedMoneyYN="N";
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	private String statYn="Y";
	
	private int partition;
	private Long offset;
	private String key;

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
	private String ctgrYn= "";
	private String ergabt= "";
	private String ergdetail= "";

	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";

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
	private String advrtsStleTpCode = "";		//소재카피 고정배너 여부 02-사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 TpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무 
	
	public static RTBDrcData fromHashMap(Map from) {
		RTBDrcData result = new RTBDrcData();
		
		result.ab_type	 = StringUtils.trimToNull2(from.get("ab_type"),"");
		result.abtest	= StringUtils.trimToNull2(from.get("abtest"),"");
		result.actDate	 = Long.parseLong(StringUtils.trimToNull2(from.get("actDate"),"0"));
		result.age	 = StringUtils.trimToNull2(from.get("age"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.cycleNum	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cycleNum"),"0"));
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.frameId	 = StringUtils.trimToNull2(from.get("frameId"),"");
		result.prdtTpCode	 = StringUtils.trimToNull2(from.get("prdtTpCode"),"");
		result.frameCombiKey	 = StringUtils.trimToNull2(from.get("frameCombiKey"),"");
		result.frameType	 = StringUtils.trimToNull2(from.get("frameType"),"");
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.gb	 = StringUtils.trimToNull2(from.get("gb"),"");
		result.gender	 = StringUtils.trimToNull2(from.get("gender"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"),"");
		result.setAu_id(StringUtils.trimToNull2(from.get("au_id"),""));
		result.limit	 = Integer.parseInt(StringUtils.trimToNull2(from.get("limit"),"0"));
		result.mobonlinkcate	 = StringUtils.trimToNull2(from.get("mobonlinkcate"));
		result.no	 = StringUtils.trimToNull2(from.get("no"),"");
		result.scriptHirnkNo	= Integer.parseInt(StringUtils.trimToNull2(from.get("scriptHirnkNo"),"0"));
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.pcode	 = StringUtils.trimToNull2(from.get("pcode"),"");
		result.platform	 = StringUtils.trimToNull2(from.get("platform"),"");
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.product	 = StringUtils.trimToNull2(from.get("product"),"");
		result.rtb_type	 = StringUtils.trimToNull2(from.get("rtb_type"),"");
		result.sc	 = StringUtils.trimToNull2(from.get("sc"),"");
		result.scriptId	 = StringUtils.trimToNull2(from.get("scriptId"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.serviceHostId	 = StringUtils.trimToNull2(from.get("serviceHostId"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.tagid	 = StringUtils.trimToNull2(from.get("tagid"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.userId	 = StringUtils.trimToNull2(from.get("userId"),"");
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"");
		result.interlock	= StringUtils.trimToNull2(from.get("interlock"),"01");
		result.setStatYn(StringUtils.trimToNull2(from.get("statYn"),"Y"));

		// 앱 모수성과 체크
		result.setFromApp(StringUtils.trimToNull2(from.get("fromApp"), "N"));

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));

		result.omitType = StringUtils.trimToNull2(from.get("omitType"),"01");

		result.intgTpCode	= StringUtils.trimToNull2(from.get("intgTpCode"),"");
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
		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"),"0")) );
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));
		
		//소재 카피 
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp") != null ?from.get("mobAdGrp") : null ));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"), "99"));
		
		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
		
		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));
				
		return result;
	}
	
	public BaseCVData toBaseCVData() {
		BaseCVData result = new BaseCVData();
		// base
		result.setYyyymmdd(DateUtils.getDate("yyyyMMdd"));
		result.setHh(this.getHh());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getGb());
		result.setSiteCode(this.getSc());
		result.setScriptNo( Integer.parseInt(this.getNo()) );
		result.setScriptHirnkNo(this.getScriptHirnkNo());
		result.setAdvertiserId(this.getUserId());
		result.setScriptUserId(this.getScriptId()); //.getScriptUserId());
		result.setType("C");
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());
		result.setKeyIp(this.getKeyIp());
		result.setAu_id(this.getAu_id());
				
		// sum
		result.setClickCnt(1);
		result.setPoint(this.getPoint());
		//result.setMpoint(this.getPoint());
		
		// append
		result.setPoint(this.getPoint());
		result.setAbType(this.getAb_type());
		result.setAbTest(this.getAbtest());
		result.setRtbType(this.getRtb_type());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setSendDate(this.getSendDate());
		result.setClassName(this.getClassName());
		result.setpCode(this.getPcode());
		
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

		//소재카피
		result.setMobAdGrpData(this.getMobAdGrpData());
		result.setAdvrtsStleTpCode(this.getAdvrtsStleTpCode());
		
		//클릭프리컨시
		result.setChrgTpCode(this.getChrgTpCode());
		result.setSvcTpCode(this.getSvcTpCode());
		
		// Ai캠페인
		result.setAiType(this.getAiType());

		result.generateKey();
		return result;
	}
	
	public String getErgabt() {
		return ergabt;
	}

	public void setErgabt(String ergabt) {
		this.ergabt = ergabt;
	}

	public String getRtb_type() {
		return rtb_type;
	}
	public void setRtb_type(String rtb_type) {
		this.rtb_type = rtb_type;
	}
	public String getAb_type() {
		return ab_type;
	}
	public void setAb_type(String ab_type) {
		this.ab_type = ab_type;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getScriptId() {
		return scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	//RTB용 추가
	private String frameId;
	private String prdtTpCode;
	private String frameCombiKey;
	private String frameType;
	private int cycleNum;

	public String getMobonlinkcate() {
		return mobonlinkcate;
	}
	public void setMobonlinkcate(String mobonlinkcate) {
		this.mobonlinkcate = mobonlinkcate;
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
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public int getScriptHirnkNo() {
		return scriptHirnkNo;
	}

	public void setScriptHirnkNo(int scriptHirnkNo) {
		this.scriptHirnkNo = scriptHirnkNo;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
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
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
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

	public String getRtbUsedMoneyYN() {
		return rtbUsedMoneyYN;
	}

	public void setRtbUsedMoneyYN(String rtbUsedMoneyYN) {
		this.rtbUsedMoneyYN = rtbUsedMoneyYN;
	}

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
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
	
	public String getAbtest() {
		return abtest;
	}

	public void setAbtest(String abtest) {
		this.abtest = abtest;
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