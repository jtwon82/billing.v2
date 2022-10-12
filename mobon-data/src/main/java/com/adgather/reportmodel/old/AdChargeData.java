package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;

import net.sf.json.JSONArray;

public class AdChargeData extends com.adgather.lang.old.ObjectToString implements Serializable{
	private static final long serialVersionUID = -282851877117772935L;
	private String site_code;
	private String adGubun;
	private String yyyymmdd;
	private String userId;
	private float point;
	private String Ip;
	private String ipInfoList;
	private String NO;
	private String kno = "0";
	private String kgr = "";
	private String scriptUserId;
	private String type;
	private String PCODE;
	private String product;
	private boolean useYmdhms=false;
	private String ymdhms;
	private Timestamp regdate;
	private String regUserId="";
	private String siteUrl;
	/* use um, kl, ad */
	private int viewcnt1=0;
	private int viewcnt2=0;
	private int viewcnt3;

	private int ago_viewcnt1=0;
	private int ago_viewcnt2=0;

	/* use conversion */
	private String ordRFUrl;
	private String ordQty;
	private String PNm;
	private String price;
	private String ordCode;
	private String uname;
	private String usex;
	private String upno;
	private String direct;
	private String keywordValue="";
	private String keywordType="";
	private String keywordSessionType="";
	private String keywordUrl="";
	private String cnvrsTpCode="";

	private String mcgb;

	private String ADPRODUCT;
	private String MEDIA_ID;
	private String ACTGUBUN;
	private String media_code;
	private String IN_HOUR;
	private int pastClickMinute;
	private String ORDCODE;
	private float mpoint = 0;

	private String platform;
	private int freqLog;
	private String gender;
	private String age;
	private String r_gubun;
	private String conv_con;

	private String serviceHostId;
	private String abtest;
	private String ab_type;		// AB 프리퀀시 구분자
	private String rtb_type;		// AB ECPM 구분자
	private int time;

	private String frameId;				//프레임 지정id
	private String prdtTpCode;				// 상품코드
	private String frameCombiKey;				//
	private String frameType;				//

	private int frameCycleNum;		//프레임 사이클회차
	private String frameSelector;		//프레임 선택자
	
	
	private String debugString;  // 디버그용 변수입니다.
	
	private String dumpType; // dumpObject type
	// TODO : dump > was 이동
	private int cookieDirect; 
	private int cookieInDirect;
	private int scriptNo;
	
	private String au_id; 
	//추천시스템용
	private String ergabtests;
	private String subadgubun;
	private String pltfom_tp_code;
	
	private String last_click_time;
	private String mobon_yn;
	private String inflow_route;

	private int partition=0;
	private Long offset=0L;
	private String key="";
	private String mobonlinkcate = "";
	
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	private String statYn="Y";

	private String flagST="";		// 망고스타일의경우 '구좌노출과 광고주 총노출을 같이 잡는' 경우에대한 조건
	private boolean chargeAble=true;
	private String chargeType="";

	private String insertType = "";
	
	//지역타겟팅
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

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	// omitType
	private String omitType= "01";
	
	private String intgTpCode = "";	// 종합코드
	private String crossbrYn = "";	// 크로스브라우져 여부
	private String intgYn= "0";		// 통합코드 여부
	private String kwrdSeq= "0";	// 키워드 시컨스
	private String adcSeq= "0";		// 오디언스 시컨스
	private String ctgrNo= "0";		// 카테고리 번호
	private String ctgrYn= "";		// 카테고리 수집여부
	private String ergabt= "";		// 추천테스트
	private String ergdetail = ""; 
	private int tTime = 0;			// 티타임
	
	private String deviceDiv="";	// 컨버전시 디바이스 종류  PC, MA:mobileAnd, MI:mobileIos, TA"TabAnd, TI:TabIos, ME:MobileEtc
	private String os="";
	private String browser="";
	private String browserVersion="";
	
	// 브라우저별 통계 데이터 적재를 위한 신규 키값
	private String osCode="";	
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	
	private String crossLoginIp="";
	
	private String continueConv="";
	private String longContinueConv="";
	private String in1hourYn="";
	private String socialYn="";
	
	//MOB_CNVRS_RENEW_NCL
	private String trkTpCode = "";
	
	//FRME_IMG_DAY_STATS
	private JSONArray adverProdData = null;
	private String frameSize = "";

	private int aiCateNo= 0;
	private boolean noExposureYN  = false; // 미노출 여부
	private String mainDomainYN  = "Y"; // ADBLOCK 여부


	private boolean isCrossAuIdYN  = false; // CrossDevice 여부

	private String frameMatrExposureYN = "N";// 프레임 AB 테스트 구분값 
	private String frameSendTpCode = "";
	
	//소재 카피 
	private JSONArray mobAdGrpData = null;		// 소재카피 array 
	private String grpId = "";							//소재 카피 ID 
	private String grpTpCode = "";						//소재 카피 Tpcode
	private String subjectCopyTpCode = "";				//소재 카피 img_copy_tp_code
	private String advrtsStleTpCode = ""; 				// 소재 카피 고정배너 여부 코드  02-허용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	//Frame 카이스트 모델 통계 	
	private String frameKaistRstCode = "";
	private String frameRtbTypeCode = "";
	
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 chrgTpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무 
	
	public static AdChargeData fromHashMap(Map from) {
		AdChargeData result = new AdChargeData();
		result.ACTGUBUN	 = StringUtils.trimToNull2(from.get("ACTGUBUN"),"");
		result.ADPRODUCT	 = StringUtils.trimToNull2(from.get("ADPRODUCT"),"");
		result.IN_HOUR	 = StringUtils.trimToNull2(from.get("IN_HOUR"),"");
		result.MEDIA_ID	 = StringUtils.trimToNull2(from.get("MEDIA_ID"),"");
		result.NO	 = StringUtils.trimToNull2(from.get("NO"),"0");
		result.ORDCODE	 = StringUtils.trimToNull2(from.get("ORDCODE"),"");
		result.cnvrsTpCode	= StringUtils.trimToNull2(from.get("cnvrsTpCode"),"");
		result.PCODE	 = StringUtils.trimToNull2(from.get("PCODE"),"");
		result.PNm	 = StringUtils.trimToNull2(from.get("PNm"),"");
		result.ab_type	 = StringUtils.trimToNull2(from.get("ab_type"),"");
		result.abtest	 = StringUtils.trimToNull2(from.get("abtest"),"");
//		result.actionLogList	 = StringUtils.trimToNull2(from.get("actionLogList"));
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"),"").toUpperCase();
		result.age	 = StringUtils.trimToNull2(from.get("age"),"");
		result.ago_viewcnt1	 = Integer.parseInt(StringUtils.trimToNull2(from.get("ago_viewcnt1"),"0"));
		result.ago_viewcnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("ago_viewcnt2"),"0"));
		result.au_id	 = StringUtils.trimToNull2(from.get("au_id"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.conv_con	 = StringUtils.trimToNull2(from.get("conv_con"),"");
		result.cookieDirect	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cookieDirect"),"0"));
		result.cookieInDirect	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cookieInDirect"),"0"));
		result.debugString	 = StringUtils.trimToNull2(from.get("debugString"),"");
		result.direct	 = StringUtils.trimToNull2(from.get("direct"),"0");
		result.dumpType	 = StringUtils.trimToNull2(from.get("dumpType"),"");
//		result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.ergabtests	 = StringUtils.trimToNull2(from.get("ergabtests"),"");
		result.frameCycleNum	 = Integer.parseInt(StringUtils.trimToNull2(from.get("frameCycleNum"),"0"));
		result.frameId	 = StringUtils.trimToNull2(from.get("frameId"),"");
		result.prdtTpCode	 = StringUtils.trimToNull2(from.get("prdtTpCode"),"");
		result.frameCombiKey	 = StringUtils.trimToNull2(from.get("frameCombiKey"),"");
		result.frameType	 = StringUtils.trimToNull2(from.get("frameType"),"");
		result.frameSelector	 = StringUtils.trimToNull2(from.get("frameSelector"),"");
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.gender	 = StringUtils.trimToNull2(from.get("gender"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"), DateUtils.getDate("HH"));
		result.inflow_route	 = StringUtils.trimToNull2(from.get("inflow_route"),"");
		result.Ip	 = StringUtils.trimToNull2(from.get("ip"),"");
		result.crossLoginIp	= StringUtils.trimToNull2(from.get("crossLoginIp"),"");
		result.last_click_time	 = StringUtils.trimToNull2(from.get("last_click_time"),"");
		result.limit	 = Integer.parseInt(StringUtils.trimToNull2(from.get("limit"),"0"));
		result.mcgb	 = StringUtils.trimToNull2(from.get("mcgb"),"");
		result.media_code	 = StringUtils.trimToNull2(from.get("media_code"),"0");
		result.mobon_yn	 = StringUtils.trimToNull2(from.get("mobon_yn"),"");
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.ordCode	 = StringUtils.trimToNull2(from.get("ordCode"),"");
		result.ordQty	 = StringUtils.trimToNull2(from.get("ordQty"),"0");
		result.ordRFUrl	 = StringUtils.trimToNull2(from.get("ordRFUrl"),"");
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.keywordValue	= StringUtils.trimToNull2(from.get("keywordValue"),"");
		result.keywordType	= StringUtils.trimToNull2(from.get("keywordType"),"");
		result.keywordSessionType	= StringUtils.trimToNull2(from.get("keywordSessionType"),"");
		result.keywordUrl	= StringUtils.trimToNull2(from.get("keywordUrl"),"");
		
		result.pastClickMinute	 = Integer.parseInt(StringUtils.trimToNull2(from.get("pastClickMinute"),"0"));
		result.platform	 = StringUtils.trimToNull2(from.get("platform"),"");
		result.pltfom_tp_code	 = StringUtils.trimToNull2(from.get("pltfom_tp_code"),"");
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.price	 = StringUtils.trimToNull2(from.get("price"),"0");
		result.product	 = StringUtils.trimToNull2(from.get("product"),"");
		result.r_gubun	 = StringUtils.trimToNull2(from.get("r_gubun"),"");
//		result.regdate	 = StringUtils.trimToNull2(from.get("regdate"));
		result.rtb_type	 = StringUtils.trimToNull2(from.get("rtb_type"),"");
		result.scriptNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.serviceHostId	 = StringUtils.trimToNull2(from.get("serviceHostId"),"");
		result.siteUrl	 = StringUtils.trimToNull2(from.get("siteUrl"),"");
		result.site_code	 = StringUtils.trimToNull2(from.get("site_code"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.subadgubun	 = StringUtils.trimToNull2(from.get("subadgubun"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
//		result.time	 = StringUtils.trimToNull2(from.get("time"));
		result.type	 = StringUtils.trimToNull2(from.get("type"),"");
		result.uname	 = StringUtils.trimToNull2(from.get("uname"),"");
		result.upno	 = StringUtils.trimToNull2(from.get("upno"),"");
		result.userId	 = StringUtils.trimToNull2(from.get("userId"),"");
		result.usex	 = StringUtils.trimToNull2(from.get("usex"),"");
		result.viewcnt1	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt1"),"0"));
		result.viewcnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt2"),"0"));
		result.viewcnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt3"),"0"));
		result.ymdhms	 = StringUtils.trimToNull2(from.get("ymdhms"),"");
		result.yyyymmdd	 = StringUtils.trimToNull2(from.get("yyyymmdd"),"");
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"0");
		result.flagST	= StringUtils.trimToNull2(from.get("flagST"),"");
		result.chargeAble	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("chargeAble"),"true"));
		result.chargeType	= StringUtils.trimToNull2(from.get("chargeType"),"");
		result.interlock	= StringUtils.trimToNull2(from.get("interlock"),"01");
		result.setStatYn(StringUtils.trimToNull2(from.get("statYn"),"Y"));
		
		// 지역
		result.nearYn	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("nearYn"),"false"));
		result.nearCode	= StringUtils.trimToNull2(from.get("nearCode"),"");
		result.userNearCode = StringUtils.trimToNull2(from.get("userNearCode"),"");
		result.realIp 	= StringUtils.trimToNull2(from.get("realIp"),"");
		result.newIpCnt	= Integer.parseInt(StringUtils.trimToNull2(from.get("newIpCnt"),"0"));
		result.rgnIpCnt	= Integer.parseInt(StringUtils.trimToNull2(from.get("rgnIpCnt"),"0"));
		result.tTime		= Integer.parseInt(StringUtils.trimToNull2(from.get("tTime"),"0"));

		// 플레이링크
		result.plAdviewCnt 	= Integer.parseInt(StringUtils.trimToNull2(from.get("plAdviewCnt"),"0"));
		result.plMediaViewCnt 	= Integer.parseInt(StringUtils.trimToNull2(from.get("plMediaViewCnt"),"0"));
		
		// 앱 모수성과 체크
		result.fromApp		= StringUtils.trimToNull2(from.get("fromApp"), "N");
		result.ipInfoList	= StringUtils.trimToNull2(from.get("ipInfoList"), "");
		
		// 전환시 사용.
		result.bHandlingStatsMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false"));
		result.bHandlingStatsPointMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false"));
		
		result.useYmdhms= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("useYmdhms"),"false"));
		
		result.omitType = StringUtils.trimToNull2(from.get("omitType"),"01");
		
		result.intgTpCode	= StringUtils.trimToNull2(from.get("intgTpCode"),"");
		result.crossbrYn	= StringUtils.trimToNull2(from.get("crossbrYn"),"N");
		result.setIntgYn(StringUtils.trimToNull2(from.get("intgYn"),"N"));
		result.kno	 = StringUtils.trimToNull2(from.get("kno"),"0");
		result.kgr	= StringUtils.trimToNull2(from.get("kwrdGrpNo"),"0");
		result.setKwrdSeq(StringUtils.trimToNull2(from.get("kwrdSeq"),"0"));
		result.setAdcSeq(StringUtils.trimToNull2(from.get("adcSeq"),"0"));
		result.setCtgrNo(StringUtils.trimToNull2(from.get("ctgrNo"),"0"));
		result.ctgrYn	= StringUtils.trimToNull2(from.get("ctgrYn"),"N");
		result.ergabt	= StringUtils.trimToNull2(from.get("ergabt"),"");
		result.ergdetail	= StringUtils.trimToNull2(from.get("ergdetail"),"");

		result.setDeviceDiv( StringUtils.trimToNull2(from.get("deviceDiv"),"PC") );
		result.setOs( StringUtils.trimToNull2(from.get("os"),"") );
		result.setBrowser( StringUtils.trimToNull2(from.get("browser"),"") );
		result.setBrowserVersion( StringUtils.trimToNull2(from.get("browserVersion"),"") );
		
		result.setOsCode( StringUtils.trimToNull2(from.get("osCode"),"etc") );
		result.setBrowserCode( StringUtils.trimToNull2(from.get("browserCode"),"etc") );
		result.setBrowserCodeVersion( StringUtils.trimToNull2(from.get("browserCodeVersion"),"") );
		result.setDeviceCode( StringUtils.trimToNull2(from.get("deviceCode"),"MA") );
		
		result.setContinueConv( StringUtils.trimToNull2(from.get("continueConv"),"") );
		result.setLongContinueConv( StringUtils.trimToNull2(from.get("longContinueConv"),"") );
		result.setIn1hourYn( StringUtils.trimToNull2(from.get("in1hourYn"),"N") );
		result.socialYn	= StringUtils.trimToNull2(from.get("socialYn"),"N");
		
		result.setRegUserId(StringUtils.trimToNull2(from.get("regUserId"),""));
		//MOB_CNVRS_RENEW_NCL
		result.setTrkTpCode(StringUtils.trimToNull2(from.get("trkTpCode"), "90"));
		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"), "0")) );

		//FRME_IMG_DAY_STATS
		result.setAdverProdData((JSONArray) from.get("adverProdData"));
		result.setFrameSize(StringUtils.trimToNull2(from.get("frameSize"), ""));


		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));
		
		//소재 카피
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp")));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"),"99"));

		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
		result.setFrameKaistRstCode(StringUtils.trimToNull2(from.get("frameKaistRstCode"), "998"));
		result.setFrameRtbTypeCode(StringUtils.trimToNull2(from.get("frameRtbTypeCode"), ""));
		
		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));
		
		return result;
	}
	public BaseCVData toBaseCVData(){
		BaseCVData result = new BaseCVData();
		// base
		result.setHh(this.getHh());
		result.setYyyymmdd(this.getYyyymmdd());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSite_code());
		if(this.getScriptNo()!=0)	result.setScriptNo(this.getScriptNo());
		if(this.getScriptNo()==0) result.setScriptNo( Integer.parseInt(this.getMedia_code()) );
		result.setAdvertiserId(this.getUserId());
		result.setScriptUserId(this.getScriptUserId());
		result.setType(this.getType());
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());

		// sum
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());

		//append
		result.setAgo_viewcnt1(this.getAgo_viewcnt1());
		result.setAgo_viewcnt2(this.getAgo_viewcnt2());
		result.setFlagST(this.getFlagST());
		result.setChargeAble(this.getChargeAble());
		result.setClassName(this.getClassName());
		result.setDumpType(this.getDumpType());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setSendDate(this.getSendDate());
		result.setFreqLog(this.getFreqLog());
		result.setKeyIp(this.getIp());
		result.setAu_id(this.getAu_id());
		result.setMcgb(this.getMcgb());
		result.setpCode(this.getPCODE());
		result.setGender(this.getGender());
		result.setUserAge(this.getAge());
		result.setServiceHostId(this.getServiceHostId());
		result.setAbTest(this.getAbtest());
		result.setAbType(this.getAb_type());
		result.setRtbType(this.getRtb_type());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameType(this.getFrameType());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameCycleNum(this.getFrameCycleNum());
		result.setFrameSelector(this.getFrameSelector());
		result.setMobonLinkCate(this.getMobonlinkcate());
		result.setChargeType(this.getChargeType());

		// 지역
		result.setNearYn(this.getNearYn());
		result.setNearCode(this.getNearCode());
		result.setUserNearCode(this.getUserNearCode());
		result.setRealIp(this.getRealIp());
		result.setNewIpCnt(this.getNewIpCnt());
		result.setRgnIpCnt(this.getRgnIpCnt());
		
		// 플레이링크
		result.setPlAdviewCnt(this.getPlAdviewCnt());
		result.setPlMediaViewCnt(this.getPlMediaViewCnt());
		
		// 앱성과
		result.setFromApp(this.getFromApp());

		// 전환에서사용
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());
		
		// 통계누락 여부
		result.setOmitType(this.getOmitType());
		
		// 종합키
		result.setIntgTpCode(this.getIntgTpCode());
		result.setCrossbrYn(this.getCrossbrYn());
		result.setIntgYn(this.getIntgYn());
		result.setKno(this.getKno());
		result.setKgr(this.getKgr());
		result.setKwrdSeq(this.getKwrdSeq());
		result.setAdcSeq(this.getAdcSeq());
		result.setCtgrNo(this.getCtgrNo());
		result.settTime(this.gettTime());
		result.setCtgrYn(this.getCtgrYn());
		result.setErgabt(this.getErgabt());
		result.setErgdetail(this.getErgdetail());
		
		result.setDeviceCode(G.convertDEVICE_TP_CODE(this.getDeviceCode()));
		result.setOsCode(G.convertOS_TP_CODE(this.getOsCode()));
		result.setBrowserCode(G.convertBROWSER_TP_CODE(this.getBrowserCode()));
		result.setBrowserCodeVersion(G.convertBROWSER_VERSION(this.getBrowserCodeVersion()));
		
		result.setErgdetail(this.getErgdetail());
		
		result.setRegUserId(this.getRegUserId());
		result.setAiCateNo(this.getAiCateNo());
		
		result.generateKey();
		
		result.setSubadgubun(this.getSubadgubun());

		//FRME_IMG_DAY_STATS
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize(this.getFrameSize());

		result.setNoExposureYN(this.isNoExposureYN());
		result.setMainDomainYN(this.getMainDomainYN());
		result.setCrossAuIdYN(this.isCrossAuIdYN());

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
		
		return result;
	}

	public ConvData toConvData()  {
		ConvData result = new ConvData();
		result.setYyyymmdd(this.getYyyymmdd());
		result.setYmdhms(this.getYmdhms());
		result.setHh(this.getHh());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setSiteCode(this.getSite_code());
		if(StringUtils.isNotEmpty(this.getMedia_code()) && !"0".equals(this.getMedia_code())) result.setScriptNo(Integer.parseInt(this.getMedia_code()));
		//result.setScriptNo(this.getScriptNo());
		result.setScriptUserId(this.getScriptUserId());
		result.setOrdCode(this.getOrdCode());
		result.setCnvrsTpCode(this.getCnvrsTpCode());
		result.setSendDate(this.getSendDate());
		result.setAdvertiserId(this.getUserId());
		result.setDirect(Integer.parseInt(this.getDirect()));
		result.setInHour(this.getIN_HOUR());
		result.setMobonYn(this.getMobon_yn());
		result.setOrdQty(this.getOrdQty());
		result.setOrdRFUrl(this.getOrdRFUrl());
		result.setPnm(this.getPNm());
		result.setPrice(this.getPrice());
		result.setGender(this.getGender());
		result.setpCode(this.getPCODE());
		result.setInterlock(this.getInterlock());
		
		result.setUserAge(this.getAge());
		result.setInflowRoute(this.getInflow_route());
		result.setKeyIp(this.getIp());
		result.setIpInfoList(this.getIpInfoList());
		result.setCrossLoginIp(this.getCrossLoginIp());
		result.setLastClickTime(this.getLast_click_time());
		result.setRegDate(this.getRegdate());
		result.setRtbType(this.getRtb_type());
		result.setServiceHostId(this.getServiceHostId());
		result.setTime(this.getTime());
		result.setType(this.getType());
		result.setUserName(this.getUname());
		result.setUserPno(this.getUpno());
		result.setGender(this.getUsex());
		
		result.setCookieDirect(this.getCookieDirect());
		result.setCookieInDirect(this.getCookieInDirect());
		result.setAbTests(this.getAbtest());
		result.setFrameCycleNum(this.getFrameCycleNum());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		
		result.setFrameSelector(this.getFrameSelector());
		result.setFreqLog(this.getFreqLog());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());

		result.setNearYn(this.getNearYn());
		result.setNearCode(this.getNearCode());
		result.setUserNearCode(this.getUserNearCode());
		result.setRealIp(this.getRealIp());

		result.setMainDomainYN(this.getMainDomainYN());
		result.setCrossAuIdYN(this.isCrossAuIdYN());
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());

		result.setUseYmdhms(this.isUseYmdhms());
		
		result.setIntgTpCode(this.getIntgTpCode());
		result.setCrossbrYn(this.getCrossbrYn());
		result.setKno(this.getKno());
		result.setKwrdSeq(this.getKwrdSeq());
		
		result.setErgabt(this.getErgabt());
		result.setErgdetail(this.getErgdetail());
		result.setAu_id(this.getAu_id());
		
		result.setDeviceDiv(this.getDeviceDiv());
		result.setOs(this.getOs());
		result.setBrowser(this.getBrowser());
		result.setBrowserVersion(this.getBrowserVersion());
		
		result.setOsCode(this.getOsCode());
		result.setBrowserCode(this.getBrowserCode());
		result.setBrowserCodeVersion(this.getBrowserCodeVersion());
		result.setDeviceCode(this.getDeviceCode());
		
		result.setKeywordType(this.getKeywordType());
		result.setKeywordValue(this.getKeywordValue());
		result.setKeywordSessionType(this.getKeywordSessionType());
		result.setKeywordUrl(this.getKeywordUrl());

		result.setContinueConv( StringUtils.trimToNull2(this.getContinueConv(),"") );
		result.setLongContinueConv( StringUtils.trimToNull2(this.getLongContinueConv(),"") );
		result.setIn1hourYn( StringUtils.trimToNull2(this.getIn1hourYn(),"") );
		result.setSocialYn(this.getSocialYn());
		
		result.setRegUserId(this.getRegUserId());
		
		//MOB_CNVRS_RENEW_NCL
		result.setTrkTpCode(this.getTrkTpCode());
		
		result.setSubadgubun(this.getSubadgubun());
		
		
		//Frame 미노출 여부
		result.setFrameMatrExposureYN(this.getFrameMatrExposureYN());
		//FrameSendTpCode
		result.setFrameSendTpCode(this.getFrameSendTpCode());
		
		//소재 카피 데이터
		result.setMobAdGrpData(this.getMobAdGrpData());
		
		result.setFrameCombiTargetYn(this.getFrameCombiTargetYn());
		result.setFrameKaistRstCode(this.getFrameKaistRstCode());
		result.setFrameRtbTypeCode(this.getFrameRtbTypeCode());
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize(this.getFrameSize());
		
		// Ai캠페인
		result.setAiType(this.getAiType());

		result.generateKey();
		return result;
	}
//	public static AdChargeData fromViewInfoX(ViewInfo from) {
//		AdChargeData result = new AdChargeData();
//		result.setYyyymmdd(from.getViewDate());
//		result.setMedia_code(from.getScriptNo()+"");
//		result.setSite_code(from.getSiteCode());
//		result.setAdGubun(from.getAdGubun());
//		result.setKno(from.getKno());
//		result.setScriptUserId(from.getScriptUserId());
//		result.setUserId(from.getUserId());
//		result.setPlatform(from.getPlatformType());
//		result.setProduct(from.getProductType());
//		
//		result.setViewcnt1(from.getViewCnt1());
//		result.setViewcnt2(from.getViewCnt2());
//		result.setViewcnt3(from.getViewCnt3());
//		result.setPoint(from.getMpoint());
//		result.setMpoint(from.getMpoint());
//		
//		return result;
//	}

	public String getPltfom_tp_code() {
		return pltfom_tp_code;
	}
	public void setPltfom_tp_code(String pltfom_tp_code) {
		this.pltfom_tp_code = pltfom_tp_code;
	}
	public String getSubadgubun() {
		return subadgubun;
	}
	public void setSubadgubun(String subadgubun) {
		this.subadgubun = subadgubun;
	}
	public String getErgabtests() {
		return ergabtests;
	}
	public void setErgabtests(String ergabtests) {
		this.ergabtests = ergabtests;
	}

	private List<Map<String, Object>> actionLogList = new ArrayList<Map<String,Object>>();

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public String getLast_click_time() {
		return last_click_time;
	}
	public void setLast_click_time(String last_click_time) {
		this.last_click_time = last_click_time;
	}
	public String getMobon_yn() {
		return mobon_yn;
	}
	public void setMobon_yn(String mobon_yn) {
		this.mobon_yn = mobon_yn;
	}
	public String getInflow_route() {
		return inflow_route;
	}
	public void setInflow_route(String inflow_route) {
		this.inflow_route = inflow_route;
	}
	public String getR_gubun() {
		return r_gubun;
	}
	public void setR_gubun(String r_gubun) {
		this.r_gubun = r_gubun;
	}
	public String getConv_con() {
		return conv_con;
	}
	public void setConv_con(String conv_con) {
		this.conv_con = conv_con;
	}
	public int getAgo_viewcnt1() {
		return ago_viewcnt1;
	}
	public void setAgo_viewcnt1(int ago_viewcnt1) {
		this.ago_viewcnt1 = ago_viewcnt1;
	}
	public int getAgo_viewcnt2() {
		return ago_viewcnt2;
	}
	public void setAgo_viewcnt2(int ago_viewcnt2) {
		this.ago_viewcnt2 = ago_viewcnt2;
	}

	/**
	 * "ip":"221.165.102.130.2rt_Timtart_Time" 위와같은 IP로 인해 컨버젼이 안잡히는 버그 수정
	 * 20160621 yhlim : Ip정보가 null 확인 추가
	 * @return
	 */
	public String getIp() {
		return Ip != null && Ip.length() > 20 ? Ip.substring(0, 20) : Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public float getMpoint() {
		return mpoint;
	}
	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}
	public String getADPRODUCT() {
		return ADPRODUCT;
	}
	public void setADPRODUCT(String aDPRODUCT) {
		ADPRODUCT = aDPRODUCT;
	}
	public String getMEDIA_ID() {
		return MEDIA_ID;
	}
	public void setMEDIA_ID(String mEDIA_ID) {
		MEDIA_ID = mEDIA_ID;
	}
	public String getACTGUBUN() {
		return ACTGUBUN;
	}
	public void setACTGUBUN(String aCTGUBUN) {
		ACTGUBUN = aCTGUBUN;
	}
	public String getMedia_code() {
		return media_code;
	}
	public void setMedia_code(String media_code) {
		this.media_code = media_code;
	}
	public String getIN_HOUR() {
		return IN_HOUR;
	}
	public void setIN_HOUR(String iN_HOUR) {
		IN_HOUR = iN_HOUR;
	}
	public String getORDCODE() {
		return ORDCODE;
	}
	public void setORDCODE(String oRDCODE) {
		ORDCODE = oRDCODE;
	}
	public String getInfo(String st){
		try{
			return st + toString();
		}catch(Exception e){
			return "getInfo : error";
		}
	}

	public Timestamp getRegdate() {
		return regdate;
	}
	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	public String getYmdhms() {
		return ymdhms;
	}
	public void setYmdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}
	public String getSite_code() {
		if(site_code==null) site_code="";
		return site_code;
	}
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}
	public String getAdGubun() {
		return adGubun;
	}
	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}
	public String getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public float getPoint() {
		return point;
	}
	public void setPoint(float point) {
		this.point = point;
	}
	public String getNO() {
		if(this.NO==null || this.NO.equals("")){
			this.NO="0";
		}
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getScriptUserId() {
		return scriptUserId;
	}
	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getOrdRFUrl() {
		return ordRFUrl;
	}
	public void setOrdRFUrl(String ordRFUrl) {
		this.ordRFUrl = ordRFUrl;
	}
	public String getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}
	public String getPNm() {
		return PNm;
	}
	public void setPNm(String pNm) {
		PNm = pNm;
	}
	public String getOrdCode() {
		return ordCode;
	}
	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}
	public String getPrice() {
		try{
			Integer.parseInt(price);
		}catch(Exception e){
			price="0";
		}
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUsex() {
		return usex;
	}
	public void setUsex(String usex) {
		this.usex = usex;
	}
	public String getUpno() {
		return upno;
	}
	public void setUpno(String upno) {
		this.upno = upno;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
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
	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public int getViewcnt1() {
		return viewcnt1;
	}
	public void setViewcnt1(int viewcnt1) {
		this.viewcnt1 = viewcnt1;
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
	public String getAbtest() {
		return abtest;
	}
	public void setAbtest(String abtest) {
		this.abtest = abtest;
	}
	public String getAb_type() {
		return ab_type;
	}
	public void setAb_type(String ab_type) {
		this.ab_type = ab_type;
	}
	public String getRtb_type() {
		return rtb_type;
	}
	public void setRtb_type(String rtb_type) {
		this.rtb_type = rtb_type;
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
	/**
   * @return the siteUrl
   */
  public String getSiteUrl() {
    return siteUrl;
  }
  /**
   * @param siteUrl the siteUrl to set
   */
  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }
  /**
   * @return the au_id
   */
  public String getAu_id() {
    return au_id;
  }
  /**
   * @param au_id the au_id to set
   */
  public void setAu_id(String au_id) {
    this.au_id = au_id;
  }
  public List<Map<String, Object>> getActionLogList() {
		return actionLogList;
	}
	public void addActionLogList(Map<String, Object> actionLog) {
		this.actionLogList.add(actionLog);
	}
	
	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

  public String getDebugString() {
    return debugString;
  }
  public void setDebugString(String debugString) {
    this.debugString = debugString;
  }
public int getCookieDirect() {
	return cookieDirect;
}
public void setCookieDirect(int cookieDirect) {
	this.cookieDirect = cookieDirect;
}
public int getCookieInDirect() {
	return cookieInDirect;
}
public void setCookieInDirect(int cookieInDirect) {
	this.cookieInDirect = cookieInDirect;
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
	public int getScriptNo() {
		return scriptNo;
	}
	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}
	public String getMobonlinkcate() {
		return mobonlinkcate;
	}
	public void setMobonlinkcate(String mobonlinkcate) {
		this.mobonlinkcate = mobonlinkcate;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getPastClickMinute() {
		return pastClickMinute;
	}
	public void setPastClickMinute(int pastClickMinute) {
		this.pastClickMinute = pastClickMinute;
	}
	public String getInterlock() {
		return interlock;
	}
	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}
	public String getFlagST() {
		return flagST;
	}
	public void setFlagST(String flagST) {
		this.flagST = flagST;
	}
	public boolean getChargeAble() {
		return chargeAble;
	}
	public void setChargeAble(boolean chargeAble) {
		this.chargeAble = chargeAble;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getNearCode() {
		return nearCode;
	}
	public void setNearCode(String nearCode) {
		this.nearCode = nearCode;
	}
	public String getUserNearCode() {
		return userNearCode;
	}
	public void setUserNearCode(String userNearCode) {
		this.userNearCode = userNearCode;
	}
	public int getNewIpCnt() {
		return newIpCnt;
	}
	public void setNewIpCnt(int newIpCnt) {
		this.newIpCnt = newIpCnt;
	}
	public String getInsertType() {
		return insertType;
	}
	public void setInsertType(String insertType) {
		this.insertType = insertType;
	}
	public int getPlAdviewCnt() {
		return plAdviewCnt;
	}
	public void setPlAdviewCnt(int plAdviewCnt) {
		this.plAdviewCnt = plAdviewCnt;
	}
	public int getPlMediaViewCnt() {
		return plMediaViewCnt;
	}
	public void setPlMediaViewCnt(int plMediaViewCnt) {
		this.plMediaViewCnt = plMediaViewCnt;
	}
	public String getRealIp() {
		return realIp;
	}
	public void setRealIp(String realIp) {
		this.realIp = realIp;
	}
	public int getRgnIpCnt() {
		return rgnIpCnt;
	}
	public void setRgnIpCnt(int rgnIpCnt) {
		this.rgnIpCnt = rgnIpCnt;
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
	public boolean isUseYmdhms() {
		return useYmdhms;
	}
	public void setUseYmdhms(boolean useYmdhms) {
		this.useYmdhms = useYmdhms;
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
	public String getDeviceDiv() {
		return deviceDiv;
	}
	public void setDeviceDiv(String deviceDiv) {
		this.deviceDiv = deviceDiv;
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
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
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
	public String getCrossLoginIp() {
		return crossLoginIp;
	}
	public void setCrossLoginIp(String crossLoginIp) {
		this.crossLoginIp = crossLoginIp;
	}
	public String getKeywordValue() {
		return keywordValue;
	}
	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}
	public String getKeywordType() {
		return keywordType;
	}
	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
	}
	public String getKeywordSessionType() {
		return keywordSessionType;
	}
	public void setKeywordSessionType(String keywordSessionType) {
		this.keywordSessionType = keywordSessionType;
	}
	public String getKeywordUrl() {
		return keywordUrl;
	}
	public void setKeywordUrl(String keywordUrl) {
		this.keywordUrl = keywordUrl;
	}
	public void setActionLogList(List<Map<String, Object>> actionLogList) {
		this.actionLogList = actionLogList;
	}
	public String getContinueConv() {
		return continueConv;
	}
	public void setContinueConv(String continueConv) {
		this.continueConv = continueConv;
	}
	public String getSocialYn() {
		return socialYn;
	}
	public void setSocialYn(String socialYn) {
		this.socialYn = socialYn;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}
	public String getIpInfoList() {
		return ipInfoList;
	}
	public void setIpInfoList(String ipInfoList) {
		this.ipInfoList = ipInfoList;
	}
	public String getCnvrsTpCode() {
		return cnvrsTpCode;
	}
	public void setCnvrsTpCode(String cnvrsTpCode) {
		this.cnvrsTpCode = cnvrsTpCode;
	}
	public String getLongContinueConv() {
		return longContinueConv;
	}
	public void setLongContinueConv(String longContinueConv) {
		this.longContinueConv = longContinueConv;
	}
	public String getIn1hourYn() {
		return in1hourYn;
	}
	public void setIn1hourYn(String in1hourYn) {
		this.in1hourYn = in1hourYn;
	}

	//MOB_CNVRS_RENEW_NCL
	public String getTrkTpCode() {
		return this.trkTpCode;
	}
	public void setTrkTpCode(String trkTpCode) {
		this.trkTpCode = trkTpCode;
	}
	public int getAiCateNo() {
		return aiCateNo;
	}
	public void setAiCateNo(int aiCateNo) {
		this.aiCateNo = aiCateNo;
	}

	//FRME_IMG_DAY_STATS
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
