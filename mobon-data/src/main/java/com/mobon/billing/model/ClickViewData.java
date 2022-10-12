package com.mobon.billing.model;

import java.io.Serializable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class ClickViewData implements Serializable {

	public String dumpType; // dumpType	
	public String className; // 클래스이름
	public String sendDate;	// 전송날짜
	public String keyCode="";	// 고유키
	public String keyCodeClickViewPcode="";
	public String keyCodeViewPcode = "";
	public String keyCodePoint2="";	// 고유키 point2
	private String keyCodeExternal="";	// 고유키
	public String keyCodeMediaCharge="";	// 고유키
	private String keyCodeIntgCntr="";	// 고유키
	public String keyCodeMdPcode="";	// 고유키2
	private String keyCodeIntgCntrKgr="";	// 고유키 키워드그룹
	private String keyCodeIntgCntrUM="";	// 고유키 성향
	private String keyCodeIntgCntrTtime="";	// 고유키 티타임
	private String keyCodeUMSiteCode="";	// 고유키UM sc
	private String keyCodeUMScriptNo="";	// 고유키UM s
	private String keyCodeKpiNo="";
	private String keyClientEnvironment="";	// 고유키 사용자환경통계
	private String keyAdverClientEnvironment="";	// 고유키 광고주사용자환경통계
	private String keyClientAgeGender="";	// 고유키 사용자연령성별통계
	private String keyCodeAdverProductHH="";	// 고유키 광고주별시간상품통계
	private String keyCampMediaRetrnStats=""; // 
	private String keyPhone="";	// 고유키 Phone
	private String keyAiBlock = "";
	public String grouping=""; // 그룹핑
	private String groupingS=""; // 그룹핑S
	private String groupingSeq=""; // 그룹핑Seq
	private String groupingExternal=""; // 그룹핑 external
	private String groupingIntgCntr=""; // 그룹핑
	private String groupingIntgCntrTtime=""; // 그룹핑 Ttime
	private String groupingIntgCntrUM=""; // 그룹핑 UM
	private String groupingAlgo=""; 

	public String hh=""; // 시간
	public String keyIp=""; // 고유키
	private String au_id=""; // 고유키
	public String pCode=""; // 상품코드
	public String mcgb=""; // 타게팅여부
	public long no=0; // 키코드
	public int freqLog=0; // 노출프리퀀시 횟수
	public boolean clickChk=true;	// 클릭과금 반영여부
	public String mobonLinkCate=""; // 외부 연동api 서브카테고리용
	public String pbGubun=""; // 퀴즈구분
	public int partition=0; // kafka partition 
	public long offset=0L; // kafka lastoffset
	public String key=""; // kafka key
	public String serverName=""; // kafka server.name
	
	public String advertiserId=""; // 광고주아이디 advertiserId
	private String exl_seq=""; // 연동순서
	private String send_tp_code=""; // 송출코드
	public String scriptUserId=""; // 메체 userid
	public String mediaTpCode="99";	// 매체 tpcode
	public int eprsRestRate= 100;	// 매체 tpcode
	public String type=""; // 노출, 클릭
	public String yyyymmdd=""; // 날짜
	public String platform=""; // 웹 모바일 w, m
	public String adGubun=""; // 광고 구분
	public String subadgubun="";
	public String recomTpCode="01";
	private String recomAlgoCode;
	private String ergdetail="";
	public String product=""; // 일반베너/아이커버/브랜드링크  b, i, s
	public String siteCode=""; // 캠페인 코드
	public String kno="0"; // 비타겟일경우 adlink의 no값, 타겟의경우 0
	private String kgr="";
	private String kwrdSeq="0"; //
	public int mediasiteNo=0; // 매체번호
	public int scriptNo=0; // 매체스크립트 코드
	public int scriptHirnkNo=0;
	public String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	public String statYn="Y";

	public int viewCnt=0; // 광고주 총노출
	public int viewCnt2=0; // 광고주 구좌노출
	public int viewCnt3=0; // 구좌노출 카운트
	public int clickCnt=0; // 클릭카운트
	public int orderCnt=0; // 컨버전카운트
	public float point=0; // 포인트
	public float mpoint=0; // 메체 정산 포인트
	public float ppoint=0; // 외부연동 ppoint 포인트
	public float pointUSD=0; // 에드센스 달러 데이터
	
	public int avalEprsCnt=0; // 유효PV카운트
	
	// 2019-07-31 추가 시작
	public int nativeViewCnt=0; // 네이티브 뷰 카운트
	public int reNewsCnt=0; 	// 네이티브 추천기사 카운트
	public int auNewsCnt=0; 	// 네이티브 오디언스 기사 카운트
	public int poNewsCnt=0; 	// 네이티브 
	public int laNewsCnt=0; 	// 네이티브 최신기사 카운트
	public int cmNewsCnt=0; 	// 네이티브 카테고리 카운트
	public int totalNewsCnt=0; 	// 네이티브 총기사 카운트
	public int rmNewsCnt=0;		// 네이티브 리턴기사 카운트
	
	// 2019-07-31 끝
	// 2019-08-13 추가 시작
	public String rptTpCd  = ""; // 네이티브 기사 구분자.
	public String newsType = ""; // 네이티브 클릭 뉴스 타입
	public int newsClickCnt=0;  // 네이티브 기사 클릭카운트
	public int newsVewCnt=0; 	// 네이티브 기사 뷰카운트
	public int abTestCnt=0;		// 네이티브 ab 테스트 카운트
	
	//2020-11-24 KPIGrouping 추가 
	public String kpiGroupingSeq = "";
	
	//2020-12-10 유효수, 반송수
	public int retrnCnt=0;
	public int avalCnt=0;

	private int loopCnt=1;
	private String regUserId="";

	private boolean noExposureYN = false;
	private String mainDomainYN  = "Y"; // ADBLOCK 여부


	private boolean isCrossAuIdYN  = false; // CrossDevice 여부

	//2021-02-01 FRME_IMG_DAY_STATS

	private JSONArray adverProdData = null;
	private String frameSize = "";

	public int getAbTestCnt() {
		return abTestCnt;
	}
	public void setAbTestCnt(int abTestCnt) {
		this.abTestCnt = abTestCnt;
	}
	
	public int getRmNewsCnt() {
		return rmNewsCnt;
	}
	public void setRmNewsCnt(int rmNewsCnt) {
		this.rmNewsCnt = rmNewsCnt;
	}
	
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public int getNewsClickCnt() {
		return newsClickCnt;
	}
	public void setNewsClickCnt(int newsClickCnt) {
		this.newsClickCnt = newsClickCnt;
	}

	public int getNewsVewCnt() {
		return newsVewCnt;
	}
	public void setNewsVewCnt(int newsVewCnt) {
		this.newsVewCnt = newsVewCnt;
	}
	public String getRptTpCd() {
		return rptTpCd;
	}
	public void setRptTpCd(String rptTpCd) {
		this.rptTpCd = rptTpCd;
	}
	
	public ClickViewData() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " "+ JSONObject.fromObject(this).toString();
	}
	
	public abstract String generateKey();
	
	public abstract void sumGethering(Object from);

	
	
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

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public String getKeyIp() {
		return keyIp != null && keyIp.length() > 20 ? keyIp.substring(0, 20) : keyIp;
	}

	public void setKeyIp(String keyIp) {
		this.keyIp = keyIp;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getKno() {
		if( "".equals(kno) || kno == null ) kno="0";
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

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public int getFreqLog() {
		return freqLog;
	}

	public void setFreqLog(int freqLog) {
		this.freqLog = freqLog;
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

	public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public int getViewCnt2() {
		return viewCnt2;
	}

	public void setViewCnt2(int viewCnt2) {
		this.viewCnt2 = viewCnt2;
	}

	public int getViewCnt3() {
		return viewCnt3;
	}

	public void setViewCnt3(int viewCnt3) {
		this.viewCnt3 = viewCnt3;
	}

	public int getClickCnt() {
		return clickCnt;
	}

	public void setClickCnt(int clickCnt) {
		this.clickCnt = clickCnt;
	}
	
	public int getOrderCnt() {
		return orderCnt;
	}
	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}
	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public boolean isClickChk() {
		return clickChk;
	}

	public void setClickChk(boolean clickChk) {
		this.clickChk = clickChk;
	}

	public String getMobonLinkCate() {
		return mobonLinkCate;
	}

	public void setMobonLinkCate(String mobonLinkCate) {
		this.mobonLinkCate = mobonLinkCate;
	}

	public String getPbGubun() {
		return pbGubun;
	}

	public void setPbGubun(String pbGubun) {
		this.pbGubun = pbGubun;
	}

	public String getHh() {
		return hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getMediasiteNo() {
		return mediasiteNo;
	}

	public void setMediasiteNo(int mediasiteNo) {
		this.mediasiteNo = mediasiteNo;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getInterlock() {
		return interlock;
	}

	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}

	public float getPpoint() {
		return ppoint;
	}

	public void setPpoint(float ppoint) {
		this.ppoint = ppoint;
	}

	public float getPointUSD() {
		return pointUSD;
	}

	public void setPointUSD(float pointUSD) {
		this.pointUSD = pointUSD;
	}

	public int getAvalEprsCnt() {
		return avalEprsCnt;
	}

	// 2019-07-31 추가----------------------------------
	public void setNativeViewCnt(int nativeViewCnt) {
		this.nativeViewCnt = nativeViewCnt;
	}
	public int getNativeViewCnt() {
		return nativeViewCnt;
	}
	public void setReNewsCnt(int reNewsCnt) {
		this.reNewsCnt = reNewsCnt;
	}
	public void setAuNewsCnt(int auNewsCnt) {
		this.auNewsCnt = auNewsCnt;
	}
	public void setPoNewsCnt(int poNewsCnt) {
		this.poNewsCnt = poNewsCnt;
	}
	public void setLaNewsCnt(int laNewsCnt) {
		this.laNewsCnt = laNewsCnt;
	}
	public void setTotalNewsCnt(int totalNewsCnt) {
		this.totalNewsCnt = totalNewsCnt;
	}
	public int getReNewsCnt() {
		return reNewsCnt;
	}
	public int getAuNewsCnt() {
		return auNewsCnt;
	}
	public int getPoNewsCnt() {
		return poNewsCnt;
	}
	public int getLaNewsCnt() {
		return laNewsCnt;
	}
	public int getTotalNewsCnt() {
		return totalNewsCnt;
	}
	
	public void setCmNewsCnt(int cmNewsCnt) {
		this.cmNewsCnt = cmNewsCnt;
	}
	public int getCmNewsCnt() {
		return cmNewsCnt;
	}
	
	// 2019-07-31 추가----------------------------------
	
	public void setAvalEprsCnt(int avalEprsCnt) {
		this.avalEprsCnt = avalEprsCnt;
	}

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

	public String getKeyCodeMediaCharge() {
		return keyCodeMediaCharge;
	}

	public void setKeyCodeMediaCharge(String keyCodeMediaCharge) {
		this.keyCodeMediaCharge = keyCodeMediaCharge;
	}

	public String getMediaTpCode() {
		return mediaTpCode;
	}

	public void setMediaTpCode(String mediaTpCode) {
		this.mediaTpCode = mediaTpCode;
	}

	public int getEprsRestRate() {
		return eprsRestRate;
	}

	public void setEprsRestRate(int eprsRestRate) {
		this.eprsRestRate = eprsRestRate;
	}

	public String getKeyCodeMdPcode() {
		return keyCodeMdPcode;
	}

	public void setKeyCodeMdPcode(String keyCodeMdPcode) {
		this.keyCodeMdPcode = keyCodeMdPcode;
	}

	public String getKeyCodeIntgCntr() {
		return keyCodeIntgCntr;
	}

	public void setKeyCodeIntgCntr(String keyCodeIntgCntr) {
		this.keyCodeIntgCntr = keyCodeIntgCntr;
	}

	public String getKwrdSeq() {
		return kwrdSeq;
	}

	public void setKwrdSeq(String kwrdSeq) {
		this.kwrdSeq = kwrdSeq;
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

	public int getRetrnCnt() {
		return retrnCnt;
	}
	public void setRetrnCnt(int retrnCnt) {
		this.retrnCnt = retrnCnt;
	}
	public int getAvalCnt() {
		return avalCnt;
	}
	public void setAvalCnt(int avalCnt) {
		this.avalCnt = avalCnt;
	}
	
	public String getKeyCodeIntgCntrKgr() {
		return keyCodeIntgCntrKgr;
	}

	public void setKeyCodeIntgCntrKgr(String keyCodeIntgCntrKgr) {
		this.keyCodeIntgCntrKgr = keyCodeIntgCntrKgr;
	}

	public String getExl_seq() {
		return exl_seq;
	}

	public void setExl_seq(String exl_seq) {
		this.exl_seq = exl_seq;
	}

	public String getSend_tp_code() {
		return send_tp_code;
	}

	public void setSend_tp_code(String send_tp_code) {
		this.send_tp_code = send_tp_code;
	}

	public String getKeyCodeExternal() {
		return keyCodeExternal;
	}

	public void setKeyCodeExternal(String keyCodeExternal) {
		this.keyCodeExternal = keyCodeExternal;
	}

	public String getKeyCodeIntgCntrUM() {
		return keyCodeIntgCntrUM;
	}

	public void setKeyCodeIntgCntrUM(String keyCodeIntgCntrUM) {
		this.keyCodeIntgCntrUM = keyCodeIntgCntrUM;
	}

	public String getKeyCodeIntgCntrTtime() {
		return keyCodeIntgCntrTtime;
	}

	public void setKeyCodeIntgCntrTtime(String keyCodeIntgCntrTtime) {
		this.keyCodeIntgCntrTtime = keyCodeIntgCntrTtime;
	}

	public String getKeyClientEnvironment() {
		return keyClientEnvironment;
	}
	
	public void setKeyClientEnvironment(String keyClientEnvironment) {
		this.keyClientEnvironment = keyClientEnvironment;
	}
	
	public String getKeyAdverClientEnvironment() {
		return keyAdverClientEnvironment;
	}
	
	public void setKeyAdverClientEnvironment(String keyAdverClientEnvironment) {
		this.keyAdverClientEnvironment = keyAdverClientEnvironment;
	}
	
	public String getKeyClientAgeGender() {
		return keyClientAgeGender;
	}
	public void setKeyClientAgeGender(String keyClientAgeGender) {
		this.keyClientAgeGender = keyClientAgeGender;
	}
	
	public String getGroupingIntgCntr() {
		return groupingIntgCntr;
	}

	public void setGroupingIntgCntr(String groupingIntgCntr) {
		this.groupingIntgCntr = groupingIntgCntr;
	}

	public String getGroupingIntgCntrTtime() {
		return groupingIntgCntrTtime;
	}

	public void setGroupingIntgCntrTtime(String groupingIntgCntrTtime) {
		this.groupingIntgCntrTtime = groupingIntgCntrTtime;
	}

	public String getGroupingIntgCntrUM() {
		return groupingIntgCntrUM;
	}

	public void setGroupingIntgCntrUM(String groupingIntgCntrUM) {
		this.groupingIntgCntrUM = groupingIntgCntrUM;
	}
	public String getGroupingExternal() {
		return groupingExternal;
	}
	public void setGroupingExternal(String groupingExternal) {
		this.groupingExternal = groupingExternal;
	}
	public String getGroupingS() {
		return groupingS;
	}
	public void setGroupingS(String groupingS) {
		this.groupingS = groupingS;
	}
	public String getKeyCodeUMSiteCode() {
		return keyCodeUMSiteCode;
	}
	public void setKeyCodeUMSiteCode(String keyCodeUMSiteCode) {
		this.keyCodeUMSiteCode = keyCodeUMSiteCode;
	}
	public String getKeyCodeUMScriptNo() {
		return keyCodeUMScriptNo;
	}
	public void setKeyCodeUMScriptNo(String keyCodeUMScriptNo) {
		this.keyCodeUMScriptNo = keyCodeUMScriptNo;
	}
	public String getGroupingSeq() {
		return groupingSeq;
	}
	public void setGroupingSeq(String groupingSeq) {
		this.groupingSeq = groupingSeq;
	}

	public String getKeyCodePoint2() {
		return keyCodePoint2;
	}
	public void setKeyCodePoint2(String keyCodePoint2) {
		this.keyCodePoint2 = keyCodePoint2;
	}
	public String getKeyPhone() {
		return keyPhone;
	}
	public void setKeyPhone(String keyPhone) {
		this.keyPhone = keyPhone;
	}
	public String getSubadgubun() {
		return subadgubun;
	}
	public void setSubadgubun(String subadgubun) {
		this.subadgubun = subadgubun;
	}
	public String getKeyCodeClickViewPcode() {
		return keyCodeClickViewPcode;
	}
	public void setKeyCodeClickViewPcode(String keyCodeClickViewPcode) {
		this.keyCodeClickViewPcode = keyCodeClickViewPcode;
	}
	public String getRecomTpCode() {
		return recomTpCode;
	}
	public void setRecomTpCode(String recomTpCode) {
		this.recomTpCode = recomTpCode;
	}
	public String getKeyCodeAdverProductHH() {
		return keyCodeAdverProductHH;
	}
	public void setKeyCodeAdverProductHH(String keyCodeAdverProductHH) {
		this.keyCodeAdverProductHH = keyCodeAdverProductHH;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}
	public String getErgdetail() {
		return ergdetail;
	}
	public void setErgdetail(String ergdetail) {
		this.ergdetail = ergdetail;
	}
	public String getAu_id() {
		return au_id;
	}
	public void setAu_id(String au_id) {
		this.au_id= au_id;
	}
	public String getRecomAlgoCode() {
		return recomAlgoCode;
	}
	public void setRecomAlgoCode(String recomAlgoCode) {
		this.recomAlgoCode = recomAlgoCode;
	}
	public String getKpiGroupingSeq() {
		return kpiGroupingSeq;
	}
	public void setKpiGroupingSeq(String kpiGroupingSeq) {
		this.kpiGroupingSeq = kpiGroupingSeq;
	}
	public String getKeyCodeKpiNo() {
		return keyCodeKpiNo;
	}
	public void setKeyCodeKpiNo(String keyCodeKpiNo) {
		this.keyCodeKpiNo = keyCodeKpiNo;
	}
	public int getLoopCnt() {
		return loopCnt;
	}
	public void setLoopCnt(int loopCnt) {
		this.loopCnt = loopCnt;
	}
	public int increaseLoopCnt() {
		this.loopCnt = loopCnt+1;
		return this.loopCnt;
	}
	public String getKeyCodeViewPcode() {
		return this.keyCodeViewPcode;
	}
	public void setKeyCodeViewPcode(String keyCodeViewPcode) {
		this.keyCodeViewPcode = keyCodeViewPcode;
	}
	public String getKeyCampMediaRetrnStats() {
		return this.keyCampMediaRetrnStats;
	}
	public void setKeyCampMediaRetrnStats(String keyCampMediaRetrnStats) {
		this.keyCampMediaRetrnStats = keyCampMediaRetrnStats;
	}
	public String getGroupingAlgo() {
		return groupingAlgo;
	}
	public void setGroupingAlgo(String groupingAlgo) {
		this.groupingAlgo = groupingAlgo;
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

	public int getScriptHirnkNo() {
		return scriptHirnkNo;
	}
	public void setScriptHirnkNo(int scriptHirnkNo) {
		this.scriptHirnkNo = scriptHirnkNo;
	}

	public boolean isCrossAuIdYN() {
		return isCrossAuIdYN;
	}

	public void setCrossAuIdYN(boolean crossAuIdYN) {
		isCrossAuIdYN = crossAuIdYN;
	}
	
	public String getKeyAiBlock() {
		return keyAiBlock;
	}
	public void setKeyAiBlock(String keyAiBlock) {
		this.keyAiBlock = keyAiBlock;
	}
	
}
