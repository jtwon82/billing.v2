package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.IntgTpCodeData;

import ch.qos.logback.classic.Logger;
import net.sf.json.JSONArray;

import org.bson.Document;

public class BaseCVData extends ClickViewData implements Serializable, Cloneable {
	
	private String gender; // 성별
	private String userAge; // 나이
	private String chargeType="";
	private String AD_TYPE; // 
	private String subMda; //유효클릭 관련
	
	//private ABTestData ABTestData; // abTest용 데이터
	private String serviceHostId; // A/B 호스트ip(마지막자리)
	private String abTest; // A/B Test 용
	private String abType; // A/B 프리퀀시 구분자
	private String rtbType; // A/B ECPM 구분자
	private String abTestTy; // A/B 추천 테스트 구분자 데이터
	
	//private FrameData frameData; // 프레임데이타
	private String frameId; // 프레임 지정id
	private String prdtTpCode; // 상품코드
	private String frameCombiKey; //
	private String frameType; // 

	private int frameCycleNum=0; // 프레임 사이클회차
	private String frameSelector; // 프레임 선택자

	// 옷잘남 ST 에서사용
	private int ago_viewcnt1=0;
	private int ago_viewcnt2=0;
	private String flagST="";
	
	// ico 에서 사용
	private boolean chargeAble=true;

	// 카카오에서사용
	private String rtbUseMoneyYn="N";
	
	// 지역데이터
	private String userNearCode = ""; // 유저의 지역코드
	private String nearCode = ""; // 지역코드 동코드 이상 시군구
	private Boolean nearYn = false; // 지역광고 체크
	private int newIpCnt = 0; // 처음조회된 지역 건수
	private int rgnIpCnt = 0; // 지역조회된 IP 건수
	private String realIp;

	// 플레이 링크
	private int plAdviewCnt=0;
	private int plMediaViewCnt=0;
	
	//앱 모수성과 체크
	private String fromApp="N";

	// 전환시 사용
	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	private String handlingPointData= "branchAction";

	// omitType
	private String omitType= "01";

	private boolean noExposureYN  = false; // 미노출 여부
	private String mainDomainYN  = "Y"; // ADBLOCK 여부


	private boolean isCrossAuIdYN  = false; // CrossDevice 여부

	// 통합키
	private String intgTpCode= "";
	private List<IntgTpCodeData> intgTpCodes = new ArrayList<IntgTpCodeData>();
	private String crossbrYn = "";	// 크로스브라우져 여부
	private String intgYn= "0";		// 통합코드 여부
	private String intgSeq= "0";
	private String kwrdSeq= "0";
	private String adcSeq= "0";
	private String ctgrNo = "0";
	private String ctgrYn = "";	// 크로스브라우져 여부
	private String ergabt = ""; // 추천서비스 ABTEST 여부
	private String ergdetail = "";

	private int tTime = 0;
	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	
	private String pointChargeAble = "true";
	
	private String adPhoneNum="";
	private String socialYn="";
	
	//KPI MongoDB 
	private String kpiNo = "0";
	private String itlTpCode = "01";
	
	private int aiCateNo= 0;
	
	private String frameMatrExposureYN = "N";// 프레임 AB 테스트 구분값 
	
	private String frameSendTpCode = ""; // 프레임 tpCode 
	
	//소재 카피 
	private JSONArray mobAdGrpData = null;		// 소재카피 array 
	private String grpId = "";							//소재 카피 ID 
	private String grpTpCode = "";						//소재 카피 Tpcode
	private String subjectCopyTpCode = "";				//소재 카피 img_copy_tp_code
	private String regUserId = "BATCH";				//소재 카피 uuid
	private String advrtsStleTpCode = "";			//소재카피 고정배너여부코드 02- 사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	//Frame 카이스트 모델 통계 	
	private String frameKaistRstCode = "";
	private String frameRtbTypeCode = "";
	
	//AIBlock Code 
	private String bdgtDstbTpCode = ""; //aI Block 코드 
	
	//클릭프리컨시
	private String chrgTpCode = "";					// 클릭프리컨시 TpCode
	private String svcTpCode = "";					// 클릭프리컨시 svbTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무 
	
	// 플러스콜
	private int avalCallTime = 0;				// 유효콜 콜타임
	private int dbCnvrsCnt = 0;					// DB 전환수

	//타겟팅 여부 확인
	private boolean targetYn = false; 			// 상품타겟팅 여부
	
	@Override
	public BaseCVData clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseCVData) super.clone();
	}
	
	public BaseCVData() {
	}
	
	public String getFrameMatrExposureYN() {
		return frameMatrExposureYN;
	}

	public void setFrameMatrExposureYN(String frameMatrExposureYN) {
		this.frameMatrExposureYN = frameMatrExposureYN;
	}

	public String getPlatformCode() {
		try {
			return G.convertPLATFORM_CODE(this.getPlatform());
		}catch(Exception e) {
			return "";
		}
	}
	public String getProductCode() {
		try {
			return G.convertPRDT_CODE(this.getProduct());
		}catch(Exception e) {
			return "";
		}
	}
	public String getAdGubunCode() {
		try {
			return G.convertTP_CODE(this.getAdGubun());
		}catch(Exception e) {
			return "";
		}
	}
	public String getSubAdGubunCode() {
		try {
			return G.convertSUBADGUBUN_CODE(this.getSubadgubun());
		}catch(Exception e) {
			return "";
		}
	}

	public void setKpiNo(String kpiNo) {
		this.kpiNo = kpiNo;
	}
	public void setItlTpCode(String itlTpCode) {
		this.itlTpCode = itlTpCode;
	}
	public String getKpiNo() {
		return this.kpiNo;
	}
	public String getItlTpCode() {
		return this.itlTpCode;
	}
 
	public BaseCVData(String yyyymmdd, String platform, String product, String adGubun, String siteCode, int scriptNo, String advertiserId) {
		this.setYyyymmdd(yyyymmdd);
		this.setPlatform(platform);
		this.setProduct(product);
		this.setAdGubun(adGubun);
		this.setSiteCode(siteCode);
		this.setScriptNo(scriptNo);
		this.setAdvertiserId(advertiserId);
	}

	public Document toDocument(String key) {
		
		Document setData = new Document();
		setData.append("_id", String.format("%s-%s", DateUtils.getDate("yyyyMMdd-HH"), this.sendDate));
		setData.append("STATS_DTTM", this.yyyymmdd);
		setData.append("PLTFOM_TP_CODE", this.platform);
		setData.append("ADVRTS_PRDT_CODE", this.product);
		if(key.indexOf("SITECODE")>-1) {
			setData.append("SITE_CODE", this.siteCode);
			setData.append("ADVER_ID", this.advertiserId);
			setData.append("SEQ", this.kno);
			setData.append("MEDIA_PYMNT_AMT", Float.valueOf(this.mpoint));
		} else if (key.contains("KPI")){
			setData.append("ADVRTS_TP_CODE",this.adGubun);
			setData.append("KPI_NO", this.kpiNo);
			setData.append("ADVER_ID", this.advertiserId);
			setData.append("MEDIA_ID", this.scriptUserId);
			setData.append("ITL_TP_CODE", this.itlTpCode);
			setData.append("MEDIA_SCRIPT_NO", Integer.valueOf(this.scriptNo));
		} else {
			setData.append("MEDIA_SCRIPT_NO", Integer.valueOf(this.scriptNo));
			setData.append("MEDIA_ID", this.scriptUserId);
			setData.append("SEQ", this.kno);
			setData.append("MEDIA_PYMNT_AMT", Float.valueOf(this.mpoint));
		}

		setData.append("ADVRTS_AMT", Float.valueOf(this.point));
		setData.append("TOT_EPRS_CNT", Integer.valueOf(this.viewCnt));
		setData.append("PAR_EPRS_CNT", Integer.valueOf(this.viewCnt3));
		setData.append("CLICK_CNT", Integer.valueOf(this.clickCnt));
		
		
		
		return setData;
	}
	
	public static BaseCVData fromHashMap(Map from) {
		BaseCVData result = new BaseCVData();
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.abTest	 = StringUtils.trimToNull2(from.get("abTest"));
		result.no	 = Long.parseLong(StringUtils.trimToNull2(from.get("no"),"0"));
		result.mcgb	 = StringUtils.trimToNull2(from.get("mcgb"));
		result.frameCycleNum	 = Integer.parseInt(StringUtils.trimToNull2(from.get("frameCycleNum"),"0"));
		result.gender	 = StringUtils.trimToNull2(from.get("gender"));
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"));
		result.clickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		result.serverName	 = StringUtils.trimToNull2(from.get("serverName"));
		result.className	 = StringUtils.trimToNull2(from.get("className"));
		result.type	 = StringUtils.trimToNull2(from.get("type"));
		result.platform	 = StringUtils.trimToNull2(from.get("platform"));
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.userAge	 = StringUtils.trimToNull2(from.get("userAge"));
		result.setDumpType(StringUtils.trimToNull2(from.get("dumpType")));
		result.viewCnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt3"),"0"));
		result.subMda	 = StringUtils.trimToNull2(from.get("subMda"));
		result.viewCnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt2"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.viewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.scriptNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"));
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"));
		result.product	 = StringUtils.trimToNull2(from.get("product"));
		result.siteCode	 = StringUtils.trimToNull2(from.get("siteCode"));
		result.kpiNo	= StringUtils.trimToNull2(from.get("kpiNo"));
		result.itlTpCode	= StringUtils.trimToNull2(from.get("itlTpCode"));
		result.mediasiteNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("mediasiteNo"),"0"));
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.frameId	 = StringUtils.trimToNull2(from.get("frameId"));
		result.prdtTpCode	 = StringUtils.trimToNull2(from.get("prdtTpCode"));
		result.frameCombiKey	 = StringUtils.trimToNull2(from.get("frameCombiKey"));
		result.frameType	 = StringUtils.trimToNull2(from.get("frameType"));
		result.abType	 = StringUtils.trimToNull2(from.get("abType"));
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"));
		result.setSubadgubun( StringUtils.trimToNull2(from.get("subadgubun"), "" ));
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.kno	 = StringUtils.trimToNull2(from.get("kno"));
		result.advertiserId	 = StringUtils.trimToNull2(from.get("advertiserId"));
		result.pbGubun	 = StringUtils.trimToNull2(from.get("pbGubun"));
		result.frameSelector	 = StringUtils.trimToNull2(from.get("frameSelector"));
		result.keyCode	 = StringUtils.trimToNull2(from.get("keyCode"));
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"));
		result.setAu_id( StringUtils.trimToNull2(from.get("au_id")) );
		result.mobonLinkCate	 = StringUtils.trimToNull2(from.get("mobonLinkCate"));
		result.AD_TYPE	 = StringUtils.trimToNull2(from.get("AD_TYPE"));
		result.serviceHostId	 = StringUtils.trimToNull2(from.get("serviceHostId"));
		result.yyyymmdd	 = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.pCode	 = StringUtils.trimToNull2(from.get("pCode"));
		result.clickChk	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("clickChk")));
		result.interlock	 = StringUtils.trimToNull2(from.get("interlock"));
		result.statYn		= StringUtils.trimToNull2(from.get("statYn"), "Y");
		result.rtbType	 = StringUtils.trimToNull2(from.get("rtbType"));
		result.flagST	= StringUtils.trimToNull2(from.get("flagST"));
		result.setRtbUseMoneyYn(StringUtils.trimToNull2(from.get("rtb_usemoney_yn")));
		
		result.nearYn	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("nearYn"), "false"));
		result.nearCode	= StringUtils.trimToNull2(from.get("nearCode"), "");
		result.realIp	= StringUtils.trimToNull2(from.get("realIp"), "");
		result.rgnIpCnt	= Integer.parseInt(StringUtils.trimToNull2(from.get("rgnIpCnt"), "0"));
		result.newIpCnt	= Integer.parseInt(StringUtils.trimToNull2(from.get("newIpCnt"), "0"));
		
		result.plAdviewCnt		= Integer.parseInt(StringUtils.trimToNull2(from.get("plAdviewCnt"), "0"));
		result.plMediaViewCnt	= Integer.parseInt(StringUtils.trimToNull2(from.get("plMediaViewCnt"), "0"));

		// 전환시 사용.
		result.bHandlingStatsMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false"));
		result.bHandlingStatsPointMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false"));

		result.omitType = StringUtils.trimToNull2(from.get("omitType"),"01");

		result.intgTpCode	= StringUtils.trimToNull2(from.get("intgTpCode"),"");
		result.crossbrYn	= StringUtils.trimToNull2(from.get("crossbrYn"),"N");
		result.kwrdSeq	= StringUtils.trimToNull2(from.get("kwrdSeq"),"0");
		result.adcSeq	= StringUtils.trimToNull2(from.get("adcSeq"),"0");

		result.tTime		= Integer.parseInt(StringUtils.trimToNull2(from.get("tTime"),"0"));

		
		result.ctgrNo = StringUtils.trimToNull2(from.get("ctgrNo"),"0");
		result.ctgrYn	= StringUtils.trimToNull2(from.get("ctgrYn"),"N");
		result.ergabt	= StringUtils.trimToNull2(from.get("ergabt"),"");
		result.ergdetail	= StringUtils.trimToNull2(from.get("ergdetail"),"");
		
		result.setOsCode( StringUtils.trimToNull2(from.get("osCode"),"") );
		result.setBrowserCode( StringUtils.trimToNull2(from.get("browserCode"),"") );
		result.setBrowserCodeVersion( StringUtils.trimToNull2(from.get("browserCodeVersion"),"") );
		result.setDeviceCode( StringUtils.trimToNull2(from.get("deviceCode"),"") );
		
		result.setAdPhoneNum( StringUtils.trimToNull2(from.get("adPhoneNum"),"") );
		
		result.pointChargeAble	= StringUtils.trimToNull2(from.get("pointChargeAble"),"true");
		result.socialYn	= StringUtils.trimToNull2(from.get("socialYn"),"N");

		result.recomTpCode	= StringUtils.trimToNull2(from.get("recomTpCode"),"");
		result.setRecomAlgoCode( StringUtils.trimToNull2(from.get("recomAlgoCode"),""));
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));
		
		//소재 카피
		result.setMobAdGrpData((JSONArray) from.get("mobAdGrp"));
		result.setGrpId(StringUtils.trimToNull2(from.get("grpId"),""));
		result.setGrpTpCode(StringUtils.trimToNull2(from.get("grpTpCode"), ""));
		result.setSubjectCopyTpCode(StringUtils.trimToNull2(from.get("subjectCopyTpCode"), ""));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"), "99"));

		
		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
		result.setFrameKaistRstCode(StringUtils.trimToNull2(from.get("frameKaistRstCode"), "998"));
		result.setFrameRtbTypeCode(StringUtils.trimToNull2(from.get("frameRtbTypeCode"), ""));
		
		//ai Block코드 
		result.setBdgtDstbTpCode(StringUtils.trimToNull2(from.get("bdgtDstbTpCode"), ""));

		// unique
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("svcTpCode"), ""));
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chrgTpCode"), ""));
		
		return result;
	}
	
	public NearData toNearData() {
		NearData result = new NearData();
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(this.getScriptNo());
		result.setType(this.getType());
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		//result.setKno(this.getKno());
		result.setViewCnt(this.getViewCnt());
		result.setViewCnt2(this.getViewCnt2());
		result.setViewCnt3(this.getViewCnt3());
		result.setClickCnt(this.getClickCnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		
		result.setADSTRD_CODE(this.getNearCode());
		result.setNEW_IP_CNT(this.getNewIpCnt());
		result.setRGN_IP_CNT(this.getRgnIpCnt());
		
		return result;
	}

	public AppTargetData toAppTargetData() {
		AppTargetData result = new AppTargetData();
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(this.getScriptNo());
		result.setType(this.getType());
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		result.setViewCnt(this.getViewCnt());
		result.setViewCnt2(this.getViewCnt2());
		result.setViewCnt3(this.getViewCnt3());
		result.setClickCnt(this.getClickCnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		return result;
	}
	
	public ChrgLogData toChrgLogData() {
		ChrgLogData result = new ChrgLogData();
		
		result.setYyyymmdd(this.getYyyymmdd());
		result.setProduct(this.getProduct());
		result.setSvcTpCode(this.getSvcTpCode());
		result.setChrgTpCode(this.getChrgTpCode());
		result.setScriptNo(Integer.toString(this.getScriptNo()));
		result.setSiteCode(this.getSiteCode());
		result.setpCode(this.getpCode());
		result.setKeyIp(this.getKeyIp());
		result.setPoint(this.getPoint());
		result.setSendDate(this.getSendDate());
		result.setEtc("07".equals(this.getSvcTpCode()) ? this.getAdvertiserId() : null);
		
		return result;
	}
	
	public PluscallLogData toPluscallLogData() {
		PluscallLogData result = new PluscallLogData();
		
		result.setYyyymmdd(this.getYyyymmdd());
		result.setHh(this.getHh());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSiteCode());
		result.setScriptNo(this.getScriptNo());
		result.setInterlock(this.getInterlock());
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		result.setViewCnt(this.getViewCnt());
		result.setViewCnt3(this.getViewCnt3());
		result.setClickCnt(this.getClickCnt());
		result.setPoint(this.getPoint());
		result.setAvalCallTime(this.getAvalCallTime());
		result.setAvalCallCnt((this.getAvalCallTime() >= 1) ? 1 : 0);
		result.setDbCnvrsCnt(this.getDbCnvrsCnt());
		
		return result;
	}
	
	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getKno(), this.getInterlock() , this.isNoExposureYN()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getFlagST(), this.getStatYn(), this.getPointChargeAble()
				, this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon());

		// ADVRTS_TP_CODE	PLTFOM_TP_CODE    	STATS_DTTM  ADVRTS_PRDT_CODE  ADVRSB_TP_CODE  RECOM_TP_CODE  SITE_CODE  MEDIA_PAR_NO  PCODE
//		this.setKeyCodeClickViewPcode(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
//				, this.getYyyymmdd(), this.getProduct(), this.getSubadgubun(), this.getRecomTpCode(), this.getRecomAlgoCode()
//				, this.getSiteCode(), this.getScriptNo(), this.getpCode()
//				, this.getType()
//				));
		
		setKeyCodeMediaCharge(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getProduct(), this.getScriptNo(), this.getInterlock(), this.getType()
				, this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon()));
		
		setKeyCodeIntgCntr(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getIntgSeq()
				, this.getIntgTpCode(), this.getScriptUserId(), this.getAdvertiserId()
				, this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon()));
		
		setKeyCodeIntgCntrKgr(String.format("%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getAdvertiserId(), this.getKgr()));

		setKeyCodeIntgCntrTtime(String.format("%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getAdvertiserId(), this.gettTime()));
		
		setKeyClientEnvironment(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getInterlock(), this.getType()
				, this.getDeviceCode(), this.getOsCode(), this.getBrowserCode(),this.getBrowserCodeVersion()));
		
		setKeyAdverClientEnvironment(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getAdvertiserId(), this.getPlatform(), this.getProduct(), this.getInterlock(), this.getType()
				, this.getDeviceCode(), this.getOsCode(), this.getBrowserCode()));
		
		setKeyClientAgeGender(String.format("%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getProduct(), this.getScriptNo(), this.getUserAge(), this.getGender()));
		
		this.setKeyCodeUMSiteCode(String.format("%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getType(), this.getSiteCode(), this.getKno() ));
		
		this.setKeyCodeUMScriptNo(String.format("%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getType(), this.getScriptNo(), this.getKno() ));
		
		this.setKeyCodePoint2(String.format("%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getAdGubun(), this.getScriptNo()));
		
		this.setKeyPhone(String.format("%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getScriptNo()));
		
//		this.setKeyCodeKpiNo(String.format("%s_%s_%s_%s_%s_%s_%s_%s"
//				, this.getYyyymmdd(), this.getHh(), this.getPlatform(),this.getScriptNo(), this.getKpiNo(), this.getProductCode(), this.getAdvertiserId(), this.getAdGubun()));
		
		//STATS_DTTM`, `ADVER_ID`, `ADVRTS_TP_CODE`, `PLTFOM_TP_CODE`, `ADVRTS_PRDT_CODE`, `ADVRSB_TP_CODE`, `RECOM_TP_CODE`, `RECOM_ALGO_CODE`, `ITL_TP_CODE`
//		this.setKeyCodeViewPcode(String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s",
//				this.getYyyymmdd(), this.getAdvertiserId(), this.getAdGubun(), this.getPlatform(), this.getProductCode(),
//				this.getSubAdGubunCode(),this.getRecomTpCode(), this.getRecomAlgoCode(), this.getInterlock()));
		
		this.setKeyCampMediaRetrnStats(String.format("%s_%s_%s_%s_%s_%s",
				this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSiteCode(),this.getScriptNo()));
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%10 );
		setGroupingIntgCntr(String.format("[%s, %s]", this.getAdGubun(), Long.parseLong(this.getIntgSeq())%10 ));
		setGroupingIntgCntrTtime(String.format("[%s]", this.gettTime()%20 ));
		setGroupingS(String.format("[%s]", this.getScriptNo()%20 ));
		this.setGroupingAlgo(String.format("[%s]", Math.abs( String.format("%s_%s_%s_%s_%s", this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSubadgubun(), this.getRecomAlgoCode() ).hashCode() % 20  )));
		
		if( !"0".equals(this.getKno()) && !StringUtils.isNumeric(this.getKno()) ) {
			this.setKno("0");
		}
		setGroupingSeq(String.format("[%s]", Long.parseLong(this.getKno())%20 ));
		setKpiGroupingSeq(String.format("[%s]", this.getScriptNo()%30));
		
		
		return keyCode;
	}
	
	public String getKeyCodeClickView() {
		this.setGrouping(String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%10 ));
		
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getKno(), this.getInterlock() , this.isNoExposureYN()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getFlagST(), this.getStatYn(), this.getPointChargeAble()
				, this.getHandlingPointData());
	}


	public String getKeyCodeADBlock() {

		return String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getProduct(), this.getType(), this.isNoExposureYN()
				, this.getAdvertiserId(), this.getMainDomainYN());
	}

	public String getKeyCodeCrossAUID() {

		return String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getProduct(), this.getType(), this.isNoExposureYN()
				, this.getAdvertiserId(), this.isCrossAuIdYN());
	}

	public String getKeyCodeViewPcode() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s",
				this.getYyyymmdd(), this.getAdvertiserId(), this.getAdGubun(), this.getPlatform(), this.getProductCode(),
				this.getSubAdGubunCode(),this.getRecomTpCode(), this.getRecomAlgoCode(), this.getInterlock());
	}
	public String getKeyCodeClickUnique() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getPlatform(), this.getProduct(), this.getAdGubun(), this.getSiteCode(), this.getAdvertiserId()
				, this.getType(), this.getInterlock(), this.getKeyIp(), this.isNoExposureYN(), this.getDumpType(), this.getSvcTpCode(), this.getChrgTpCode());
	}
	public String getKeyCodeClickViewPcode() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
			, this.getYyyymmdd(), this.getProduct(), this.getSubadgubun(), this.getRecomTpCode(), this.getRecomAlgoCode()
			, this.getSiteCode(), this.getScriptNo(), this.getpCode()
			, this.getType());
	}
	public String getKeyCodeKpiNo() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s"
				, this.getYyyymmdd(), this.getHh(), this.getPlatformCode(), this.getProductCode(), this.getAdGubunCode(), this.getScriptNo(), this.getKpiNo()
				, this.getAdvertiserId(), this.getType());
	}
	public String getKeyCodeOpenrtb() {
		setGroupingSeq(String.format("[%s]", (this.getScriptNo())%20 ));
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getInterlock(), this.isNoExposureYN()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getPointChargeAble(), this.getHandlingPointData());
	}
	public String getKeyCodeSubjectCopy() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
				 this.getYyyymmdd(), this.getHh() ,this.getAdGubun(), this.getPlatform(),this.getProduct()
				,this.getType(),this.getFrameSelector(), this.getSiteCode() ,this.getAdvertiserId(), this.getScriptNo(),
				this.getInterlock(), this.getGrpId(), this.getGrpTpCode(), this.getSubjectCopyTpCode(), this.getFrameId(),
				this.getRegUserId()
		);
	}
	
	public String getKeyAiBlock() {
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getHh(), this.getAdGubun(), this.getPlatform(), this.getProduct(), this.getType(),
				this.getSiteCode(), this.getAdvertiserId(), this.getInterlock(), this.getBdgtDstbTpCode());
	}
	public String getKeyCodeAi() {
		this.setGrouping(String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%10 ));
		
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getKno(), this.getAiType(), this.getInterlock() , this.isNoExposureYN()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getFlagST(), this.getStatYn());
	}

	public String getKeyCodeABPcodeRecom(){
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getAdGubunCode(), this.getRecomAlgoCode(),
				this.getRecomTpCode(), this.getProductCode(), this.getPlatformCode(), this.getAbTestTy());
	}

	@Override
	public void sumGethering(Object _from) {
		BaseCVData from = (BaseCVData)_from;
		
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
			if(from.getPlAdviewCnt()>0)	this.setPlAdviewCnt( this.getPlAdviewCnt() + from.getPlAdviewCnt() );
			if(from.getPlMediaViewCnt()>0)	this.setPlMediaViewCnt( this.getPlMediaViewCnt() + from.getPlMediaViewCnt() );
		}
		else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			//else this.setClickCnt( this.getClickCnt() + 1 );
		}
		else if ( G.VIEWCLICK.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
			if(from.getPlAdviewCnt()>0)	this.setPlAdviewCnt( this.getPlAdviewCnt() + from.getPlAdviewCnt() );
			if(from.getPlMediaViewCnt()>0)	this.setPlMediaViewCnt( this.getPlMediaViewCnt() + from.getPlMediaViewCnt() );

			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			else this.setClickCnt( this.getClickCnt() + 1 );
		}
		else if ( G.RETRNAVAL.equals( from.getType()) ) {
			if(from.getRetrnCnt()>0)	this.setRetrnCnt( this.getRetrnCnt() + from.getRetrnCnt() );
			if(from.getAvalCnt()>0)	this.setAvalCnt( this.getAvalCnt() + from.getAvalCnt() );
		}
		
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		
		this.setLoopCnt( from.getLoopCnt()+1 );
	}

	public String getDetailInfo() {
		return String.format("%s vc-%s  vc2-%s  vc3-%s  c-%s  p-%s mp-%s", this.generateKey(), this.viewCnt, this.viewCnt2, this.viewCnt3, this.clickCnt, this.point, this.mpoint);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}


	public String getServiceHostId() {
		return serviceHostId;
	}

	public void setServiceHostId(String serviceHostId) {
		this.serviceHostId = serviceHostId;
	}

	public String getAbType() {
		return abType;
	}

	public void setAbType(String abType) {
		this.abType = abType;
	}

	public String getRtbType() {
		return rtbType;
	}

	public void setRtbType(String rtbType) {
		this.rtbType = rtbType;
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

	public String getAD_TYPE() {
		return AD_TYPE;
	}

	public void setAD_TYPE(String aD_TYPE) {
		AD_TYPE = aD_TYPE;
	}

	public String getSubMda() {
		return subMda;
	}

	public void setSubMda(String subMda) {
		this.subMda = subMda;
	}

	public String getAbTest() {
		return abTest;
	}

	public void setAbTest(String abTest) {
		this.abTest = abTest;
	}
	public int getAgo_viewcnt2() {
		return ago_viewcnt2;
	}
	public void setAgo_viewcnt2(int ago_viewcnt2) {
		this.ago_viewcnt2 = ago_viewcnt2;
	}
	public int getAgo_viewcnt1() {
		return ago_viewcnt1;
	}
	public void setAgo_viewcnt1(int ago_viewcnt1) {
		this.ago_viewcnt1 = ago_viewcnt1;
	}
	public String getFlagST() {
		return flagST;
	}
	public void setFlagST(String flagST) {
		this.flagST = flagST;
	}
	public boolean isChargeAble() {
		return chargeAble;
	}
	public void setChargeAble(boolean chargeAble) {
		this.chargeAble = chargeAble;
	}

	public String getRtbUseMoneyYn() {
		return rtbUseMoneyYn;
	}

	public void setRtbUseMoneyYn(String rtbUseMoneyYn) {
		this.rtbUseMoneyYn = rtbUseMoneyYn;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
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

	public int getNewIpCnt() {
		return newIpCnt;
	}

	public void setNewIpCnt(int newIpCnt) {
		this.newIpCnt = newIpCnt;
	}

	public int getRgnIpCnt() {
		return rgnIpCnt;
	}

	public void setRgnIpCnt(int rgnIpCnt) {
		this.rgnIpCnt = rgnIpCnt;
	}

	public String getRealIp() {
		return realIp;
	}

	public void setRealIp(String realIp) {
		this.realIp = realIp;
	}

	public int getPlMediaViewCnt() {
		return plMediaViewCnt;
	}

	public void setPlMediaViewCnt(int plMediaViewCnt) {
		this.plMediaViewCnt = plMediaViewCnt;
	}

	public int getPlAdviewCnt() {
		return plAdviewCnt;
	}

	public void setPlAdviewCnt(int plAdviewCnt) {
		this.plAdviewCnt = plAdviewCnt;
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

	public String getIntgTpCode() {
		return intgTpCode;
	}

	public void setIntgTpCode(String intgTpCode) {
		this.intgTpCode = intgTpCode;
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

	public String getIntgSeq() {
		return intgSeq;
	}

	public void setIntgSeq(String intgSeq) {
		this.intgSeq = intgSeq;
	}

	public String getCrossbrYn() {
		return crossbrYn;
	}

	public void setCrossbrYn(String crossbrYn) {
		this.crossbrYn = crossbrYn;
	}

	public List<IntgTpCodeData> getIntgTpCodes() {
		return intgTpCodes;
	}

	public void setIntgTpCodes(List<IntgTpCodeData> intgTpCodes) {
		this.intgTpCodes = intgTpCodes;
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
	
	public String getPointChargeAble() {
		return pointChargeAble;
	}

	public void setPointChargeAble(String pointChargeAble) {
		this.pointChargeAble = pointChargeAble;
	}
	
	public String getErgdetail() {
		return ergdetail;
	}

	public void setErgdetail(String ergdetail) {
		this.ergdetail = ergdetail;
	}

	public int getDbCnvrsCnt() {
		return dbCnvrsCnt;
	}

	public void setDbCnvrsCnt(int dbCnvrsCnt) {
		this.dbCnvrsCnt = dbCnvrsCnt;
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

	public String getSocialYn() {
		return socialYn;
	}

	public void setSocialYn(String socialYn) {
		this.socialYn = socialYn;
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

	public void setNoExposureYN(boolean noExposureYN) {
		this.noExposureYN = noExposureYN;
	}

	public String getMainDomainYN() {
		return mainDomainYN;
	}

	public void setMainDomainYN(String mainDomainYN) {
		this.mainDomainYN = mainDomainYN;
	}

	public String getFrameSendTpCode() {
		return frameSendTpCode;
	}

	public void setFrameSendTpCode(String frameSendTpCode) {
		this.frameSendTpCode = frameSendTpCode;
	}

	public String getHandlingPointData() {
		return handlingPointData;
	}

	public void setHandlingPointData(String handlingPointData) {
		this.handlingPointData = handlingPointData;
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

	public boolean isCrossAuIdYN() {
		return isCrossAuIdYN;
	}

	public void setCrossAuIdYN(boolean crossAuIdYN) {
		isCrossAuIdYN = crossAuIdYN;
	}

	public String getBdgtDstbTpCode() {
		return bdgtDstbTpCode;
	}

	public void setBdgtDstbTpCode(String bdgtDstbTpCode) {
		this.bdgtDstbTpCode = bdgtDstbTpCode;
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

	public String getAbTestTy() {
		return abTestTy;
	}

	public void setAbTestTy(String abTestTy) {
		this.abTestTy = abTestTy;
	}

	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}

	public boolean isTargetYn() {
		return targetYn;
	}

	public void setTargetYn(boolean targetYn) {
		this.targetYn = targetYn;
	}

	public String getAdvrtsStleTpCode() {
		return advrtsStleTpCode;
	}

	public void setAdvrtsStleTpCode(String advrtsStleTpCode) {
		this.advrtsStleTpCode = advrtsStleTpCode;
	}
}
