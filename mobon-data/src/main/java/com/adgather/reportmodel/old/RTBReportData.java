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
 * RTB 전용 통계데이터를 쌓기 위한 도메인
 * @date 2017. 2. 14.
 * @param 
 * @exception
 * @see
 */
@SuppressWarnings("serial")
public class RTBReportData extends ObjectToString implements Serializable {
	
	private String au_id;
	int sdate;			// 날짜
	int time;			// 시간(타임)

	String site_code;		// 광고주의 사이트코드
	String userid;			// 광고주 아이디
	String gubun;			// 광고 구분자

	int media_code;	// 매체 코드
	int scriptHirnkNo; // 자식의 모지면
	String media_id;		// 매체 아이디
	String tagid;			// 슬롯 아이디

	String ab_type = "A";		// AB 구분자
	String rtb_type = "A";		// RTB 타입 (A, B, C)
	private String abtest;

	int viewcnt;			// 노출수
	int unique_viewcnt; // 유니크한 노출수 (현재 미개발)
	int clickcnt;				// 클릭수

	int win_notice;		// 입찰 성공 횟수
	int bid_try = 0;				// 모비온에서 입찰 시도 횟수
	int bid_request = 0;		// 카카오측에서 입찰 요청 횟수
	float success_point;	// 카카오 입찰 성공 CPM (second_price_plus)
	float real_point;			// 카카오 지면에 실제노출할때의 CPM (second_price_plus)
	float cost;					// 모비온 입찰(시도) 요청 CPM
	float success_cost;			// 모비온 입찰 성공 CPM - 입찰 요청했던 당시의 (rtb_media_scirpt 의 bidCost)
	float real_cost;			// 모비온 입찰 성공 CPM - 입찰 요청했던 당시의 (rtb_media_scirpt 의 bidCost)
	float click_point;

	int realtime_price;		// 실시간 과금금액
	int direct_price;			// 직접 과금금액
	int indirect_price;		// 간접 과금금액
	int convcnt;		// 전환수

	int viewcnt1;
	int viewcnt2;
	int viewcnt3;
	String platform; // W or M
	float mpoint;
	
	String kno;
	private String kgr;
	
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
	private String ctgrNo= "0";
	private String ctgrYn= "";
	private String ergabt = "";
	private String ergdetail = "";
	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	
	private int tTime=0;
	
	private String frameId = "";
	private String prdtTpCode = "";
	private String frameCombiKey = "";
	private String frameType = "";

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
	private String advrtsStleTpCode = "";		//소재카피 고정배너 여부  02 - 사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	
	//클릭프리컨시
	private String chrgTpCode = "";				// 클릭프리컨시 TpCode
	private String svcTpCode = "";				// 클릭프리컨시 svcTpCode
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무

	private String product = "";
	
	public static RTBReportData fromHashMap(Map from) {
		RTBReportData result = new RTBReportData();
		
		result.ab_type	 = StringUtils.trimToNull2(from.get("ab_type"),"");
		result.abtest	= StringUtils.trimToNull2(from.get("abtest"),"");
		result.bid_request	 = Integer.parseInt(StringUtils.trimToNull2(from.get("bid_request"),"0"));
		result.bid_try	 = Integer.parseInt(StringUtils.trimToNull2(from.get("bid_try"),"0"));
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.setAu_id(StringUtils.trimToNull2(from.get("au_id"),""));
		result.click_point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("click_point"),"0"));
		result.clickcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickcnt"),"0"));
		result.convcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("convcnt"),"0"));
		result.cost	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cost"),"0"));
		result.direct_price	 = Integer.parseInt(StringUtils.trimToNull2(from.get("direct_price"),"0"));
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.gubun	 = StringUtils.trimToNull2(from.get("gubun"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.indirect_price	 = Integer.parseInt(StringUtils.trimToNull2(from.get("indirect_price"),"0"));
		result.limit	 = Integer.parseInt(StringUtils.trimToNull2(from.get("limit"),"0"));
		result.media_code	 = Integer.parseInt(StringUtils.trimToNull2(from.get("media_code"),"0"));
		result.scriptHirnkNo	= Integer.parseInt(StringUtils.trimToNull2(from.get("scriptHirnkNo"),"0"));
		result.media_id	 = StringUtils.trimToNull2(from.get("media_id"),"");
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.platform	 = StringUtils.trimToNull2(from.get("platform"),"");
		result.realtime_price	 = Integer.parseInt(StringUtils.trimToNull2(from.get("realtime_price"),"0"));
		result.rtb_type	 = StringUtils.trimToNull2(from.get("rtb_type"),"");
		result.sdate	 = Integer.parseInt(StringUtils.trimToNull2(from.get("sdate"),"0"));
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.site_code	 = StringUtils.trimToNull2(from.get("site_code"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.success_cost	 = Float.parseFloat(StringUtils.trimToNull2(from.get("success_cost"),"0"));
		result.success_point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("success_point"),"0"));
		result.tagid	 = StringUtils.trimToNull2(from.get("tagid"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.time	 = Integer.parseInt(StringUtils.trimToNull2(from.get("time"),"0"));
		result.userid	 = StringUtils.trimToNull2(from.get("userid"),"");
		result.viewcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt"),"0"));
		result.viewcnt1	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt1"),"0"));
		result.viewcnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt2"),"0"));
		result.viewcnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt3"),"0"));
		result.win_notice	 = Integer.parseInt(StringUtils.trimToNull2(from.get("win_notice"),"0"));
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
		result.frameId	= StringUtils.trimToNull2(from.get("frameId"),"");
		result.prdtTpCode	= StringUtils.trimToNull2(from.get("prdtTpCode"),"");
		result.frameCombiKey	= StringUtils.trimToNull2(from.get("frameCombiKey"),"");
		result.frameType	= StringUtils.trimToNull2(from.get("frameType"),"");
		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"),"0")) );
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"),"N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));
		
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp") != null ?from.get("mobAdGrp") : null ));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"), "99"));
		
		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
				
		//클릭프리컨시
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chargeCode"), ""));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("serviceCode"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));

		result.product = StringUtils.trimToNull2(from.get("product"), G.MBW);
		
		return result;
	}

	public BaseCVData toBaseCVData() {
		BaseCVData result = new BaseCVData();
		// base
		result.setYyyymmdd(this.getSdate()+"");
		result.setHh(this.getHh());
		result.setPlatform(this.getPlatform());
		// result.setProduct(G.MBW);
		result.setProduct(this.getProduct());	// 해당 객체에 product 코드 넘겨받는 식으로 변경
		result.setAdGubun(this.getGubun()); //.getGb());
		result.setSiteCode(this.getSite_code()); //.getSc());
		result.setScriptHirnkNo(this.getScriptHirnkNo());
		result.setScriptNo( this.getMedia_code() ); //Integer.parseInt(this.getMc()) );
		result.setAdvertiserId(this.getUserid()); //.getU());
		result.setScriptUserId(this.getMedia_id()); //.getScriptUserId());
		result.setType("V");
		result.setInterlock(this.getInterlock());
		result.setStatYn(this.getStatYn());
		result.setAu_id(this.getAu_id());
		
		// sum
		// 카카오 viewcnt 가 있지만 0으로 넘어오고 쓰지 않음
		result.setViewCnt(this.getViewcnt1());
		result.setViewCnt2(this.getViewcnt2());
		result.setViewCnt3(this.getViewcnt3());
		result.setPoint(0); //.getPoint());
		result.setMpoint(this.getMpoint());

		// append
		result.setAbType(this.getAb_type());
		result.setRtbType(this.getRtb_type());
		result.setAbTest(this.getAbtest());
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		result.setSendDate(this.getSendDate());
		result.setClassName(this.getClassName());
		
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
		
		//클릭프리컨시
		result.setChrgTpCode(this.getChrgTpCode());
		result.setSvcTpCode(this.getSvcTpCode());

		//소재카피
		result.setMobAdGrpData(this.getMobAdGrpData());
		result.setAdvrtsStleTpCode(this.getAdvrtsStleTpCode());
		
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
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
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
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public float getMpoint() {
		return mpoint;
	}
	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getGubun() {
		return gubun;
	}
	public int getConvcnt() {
		return convcnt;
	}
	public void setConvcnt(int convcnt) {
		this.convcnt = convcnt;
	}
	public float getClick_point() {
		return click_point;
	}

	public void setClick_point(float click_point) {
		this.click_point = click_point;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
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

	public int getUnique_viewcnt() {
		return unique_viewcnt;
	}

	public void setUnique_viewcnt(int unique_viewcnt) {
		this.unique_viewcnt = unique_viewcnt;
	}

	public int getClickcnt() {
		return clickcnt;
	}

	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}

	public int getRealtime_price() {
		return realtime_price;
	}

	public void setRealtime_price(int realtime_price) {
		this.realtime_price = realtime_price;
	}

	public int getDirect_price() {
		return direct_price;
	}

	public void setDirect_price(int direct_price) {
		this.direct_price = direct_price;
	}

	public int getIndirect_price() {
		return indirect_price;
	}

	public void setIndirect_price(int indirect_price) {
		this.indirect_price = indirect_price;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

//	public int getEtc1() {
//		return etc1;
//	}
//
//	public void setEtc1(int etc1) {
//		this.etc1 = etc1;
//	}
//
//	public int getEtc2() {
//		return etc2;
//	}
//
//	public void setEtc2(int etc2) {
//		this.etc2 = etc2;
//	}
//
//	public int getEtc3() {
//		return etc3;
//	}
//
//	public void setEtc3(int etc3) {
//		this.etc3 = etc3;
//	}
//
//	public int getEtc4() {
//		return etc4;
//	}
//
//	public void setEtc4(int etc4) {
//		this.etc4 = etc4;
//	}
//
//	public int getEtc5() {
//		return etc5;
//	}
//
//	public void setEtc5(int etc5) {
//		this.etc5 = etc5;
//	}
//
//	public int getEtc6() {
//		return etc6;
//	}
//
//	public void setEtc6(int etc6) {
//		this.etc6 = etc6;
//	}
//
//	public int getEtc7() {
//		return etc7;
//	}
//
//	public void setEtc7(int etc7) {
//		this.etc7 = etc7;
//	}

	public String getSite_code() {
		return site_code;
	}

	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public int getSdate() {
		return sdate;
	}

	public void setSdate(int sdate) {
		this.sdate = sdate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getMedia_code() {
		return media_code;
	}

	public void setMedia_code(int media_code) {
		this.media_code = media_code;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public int getViewcnt() {
		return viewcnt;
	}

	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}

	public int getWin_notice() {
		return win_notice;
	}

	public void setWin_notice(int win_notice) {
		this.win_notice = win_notice;
	}

	public int getBid_try() {
		return bid_try;
	}

	public void setBid_try(int bid_try) {
		this.bid_try = bid_try;
	}

	public int getBid_request() {
		return bid_request;
	}

	public void setBid_request(int bid_request) {
		this.bid_request = bid_request;
	}

	public float getSuccess_point() {
		return success_point;
	}

	public void setSuccess_point(float success_point) {
		this.success_point = success_point;
	}

	public float getReal_point() {
		return real_point;
	}

	public void setReal_point(float real_point) {
		this.real_point = real_point;
	}

	public float getReal_cost() {
		return real_cost;
	}

	public void setReal_cost(float real_cost) {
		this.real_cost = real_cost;
	}

	public float getSuccess_cost() {
		return success_cost;
	}

	public void setSuccess_cost(float success_cost) {
		this.success_cost = success_cost;
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

	public int getScriptHirnkNo() {
		return scriptHirnkNo;
	}

	public void setScriptHirnkNo(int scriptHirnkNo) {
		this.scriptHirnkNo = scriptHirnkNo;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
}