package com.mobon.billing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class ActionData extends BillingVo implements Serializable {
	
	public String className="";
	public String sendDate="";	// 전송날짜
	public String yyyymmdd=""; // 날짜
	public String hh="01"; // 시간
	public String keyIp=""; // 사용자 ip
	private String au_id;
	public String pCode=""; // 상품코드
	public long shoplogNo=0L;
	public String siteCode=""; // 캠페인코드
	public String advertiserId=""; // 광고주아이디
	public String scriptNo=""; // 스크립트코드 (s와 동일)
	public String scriptUserId=""; // 메체 userid
	public long kno=0L; // 비상품타게팅에대한 key no (KL, UM)
	public float point=0; // 포인트
	public String adGubun=""; // 광고구분
	public String subadgubun=""; // sub 광고구분
	public String recomTpCode="01";
	private String recomAlgoCode;
	private String mainPcode;
	private String clickPcode;
	private int freqCnt;
	
	public String actGubun="";
	public String product=""; // b:배너, i:아이커버, s:브랜드링크 (모바일, 아이커버, 웹인지, 브랜드링크, 앱)
	public String mcgb=""; // 타게팅여부
	
	public String platform=""; // 웹 모바일 구분
	public String type=""; // 노출, 클릭
	public String gender=""; // 성별
	public String userAge=""; // 나이
	public String no=""; // 테이블의 NO값
	public String serviceHostId=""; // A/B 호스트ip(마지막자리)
	public String abTests=""; // A/B Test 용
//	public ABTestData aABTestData; // A/B Test
	public float mpoint=0L; // 정산 포인트
	public int freqLog=0; // 프리퀀시 로그용

	private String intgYn="N";		// 통합여부
	private String crossbrYn = "";	// 크로스브라우져 여부
	private long actSeq=0L;
	private Map intgSeq= new HashMap();
	private String svcTpCode="";	// 서비스종류
	private String chrgTpCode="01";	// 과금종류
	private String advrtsTpCode="01";	// 광고구분
	private int intgLogCnt=1;	// 종합로그 갯수
	private String kwrdSeq="0";
	private String adcSeq="0";
	private int tTime=0;
	private String ctgrNo="0";
	private String ctgrYn="";
	private String ergabt="";
	private String ergdetail="";

	private String fromApp="N";

	private int aiCateNo= 0;

	private boolean noExposureYN;

	private String abTestTy;

	//삼품타겟팅 여부
	private Boolean targetYn = false;

	private JSONArray mobAdGrp = null;
	//소재 부모코드
	private String mobonAdGrpIdI = "";
	//소재 시퀀스
	private String adGrpTpCodeI = "";
	//이미지 코드
	private String imageTpCode = "";
	//카피 부모코드
	private String mobonAdGrpIdC = "";
	//카피 시퀀스
	private String adGrpTpCodeC = "";
	//카피 코드
	private String cpTpCode = "";
	//
	private String advrtsStleTpCode = "";

	//prdtTpCode
	private String prdtTpCode = "";

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public String getHh() {
		return hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getKeyIp() {
		return keyIp;
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

	public long getShoplogNo() {
		return shoplogNo;
	}

	public void setShoplogNo(long shoplogNo) {
		this.shoplogNo = shoplogNo;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
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

	public long getKno() {
		return kno;
	}

	public void setKno(long kno) {
		this.kno = kno;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}

	public String getActGubun() {
		return actGubun;
	}

	public void setActGubun(String actGubun) {
		this.actGubun = actGubun;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getMcgb() {
		return mcgb;
	}

	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getServiceHostId() {
		return serviceHostId;
	}

	public void setServiceHostId(String serviceHostId) {
		this.serviceHostId = serviceHostId;
	}

	public String getAbTests() {
		return abTests;
	}

	public void setAbTests(String abTests) {
		this.abTests = abTests;
	}

	public float getMpoint() {
		return mpoint;
	}

	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}

	public int getFreqLog() {
		return freqLog;
	}

	public void setFreqLog(int freqLog) {
		this.freqLog = freqLog;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " "+ JSONObject.fromObject(this).toString();
	}
	
	public abstract String generateKey();
	
	public abstract String sumGethering(ActionData from);

	public String getKwrdSeq() {
		return kwrdSeq;
	}

	public void setKwrdSeq(String kwrdSeq) {
		this.kwrdSeq = kwrdSeq;
	}

	public String getSvcTpCode() {
		return svcTpCode;
	}

	public void setSvcTpCode(String svcTpCode) {
		this.svcTpCode = svcTpCode;
	}

	public String getChrgTpCode() {
		return chrgTpCode;
	}

	public void setChrgTpCode(String chrgTpCode) {
		this.chrgTpCode = chrgTpCode;
	}

	public int getIntgLogCnt() {
		return intgLogCnt;
	}

	public void setIntgLogCnt(int intgLogCnt) {
		this.intgLogCnt = intgLogCnt;
	}

	public long getActSeq() {
		return actSeq;
	}

	public void setActSeq(long actSeq) {
		this.actSeq = actSeq;
	}

	public String getFromApp() {
		return fromApp;
	}

	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}

	public String getIntgYn() {
		return intgYn;
	}

	public void setIntgYn(String intgYn) {
		this.intgYn = intgYn;
	}

	public String getAdvrtsTpCode() {
		return advrtsTpCode;
	}

	public void setAdvrtsTpCode(String advrtsTpCode) {
		this.advrtsTpCode = advrtsTpCode;
	}

	public String getAdcSeq() {
		return adcSeq;
	}

	public void setAdcSeq(String adcSeq) {
		this.adcSeq = adcSeq;
	}

	public Map getIntgSeq() {
		return intgSeq;
	}

	public void setIntgSeq(Map intgSeq) {
		this.intgSeq = intgSeq;
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

	public String getSubadgubun() {
		return subadgubun;
	}

	public void setSubadgubun(String subadgubun) {
		this.subadgubun = subadgubun;
	}

	public String getRecomTpCode() {
		return recomTpCode;
	}

	public void setRecomTpCode(String recomTpCode) {
		this.recomTpCode = recomTpCode;
	}

	public String getAu_id() {
		return au_id;
	}

	public void setAu_id(String au_id) {
		this.au_id = au_id;
	}

	public String getMainPcode() {
		return mainPcode;
	}

	public void setMainPcode(String mainPcode) {
		this.mainPcode = mainPcode;
	}

	public String getClickPcode() {
		return clickPcode;
	}

	public void setClickPcode(String clickPcode) {
		this.clickPcode = clickPcode;
	}

	public int getFreqCnt() {
		return freqCnt;
	}

	public void setFreqCnt(int freqCnt) {
		this.freqCnt = freqCnt;
	}

	public String getRecomAlgoCode() {
		return recomAlgoCode;
	}

	public void setRecomAlgoCode(String recomAlgoCode) {
		this.recomAlgoCode = recomAlgoCode;
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

	public String getAbTestTy() {
		return abTestTy;
	}

	public void setAbTestTy(String abTestTy) {
		this.abTestTy = abTestTy;
	}

	public Boolean getTargetYn() {
		return targetYn;
	}

	public void setTargetYn(Boolean targetYn) {
		this.targetYn = targetYn;
	}

	public JSONArray getMobAdGrp() {
		return mobAdGrp;
	}

	public void setMobAdGrp(JSONArray mobAdGrp) {
		this.mobAdGrp = mobAdGrp;
	}

	public String getMobonAdGrpIdI() {
		return mobonAdGrpIdI;
	}

	public void setMobonAdGrpIdI(String mobonAdGrpIdI) {
		this.mobonAdGrpIdI = mobonAdGrpIdI;
	}

	public String getAdGrpTpCodeI() {
		return adGrpTpCodeI;
	}

	public void setAdGrpTpCodeI(String adGrpTpCodeI) {
		this.adGrpTpCodeI = adGrpTpCodeI;
	}

	public String getImageTpCode() {
		return imageTpCode;
	}

	public void setImageTpCode(String imageTpCode) {
		this.imageTpCode = imageTpCode;
	}

	public String getMobonAdGrpIdC() {
		return mobonAdGrpIdC;
	}

	public void setMobonAdGrpIdC(String mobonAdGrpIdC) {
		this.mobonAdGrpIdC = mobonAdGrpIdC;
	}

	public String getAdGrpTpCodeC() {
		return adGrpTpCodeC;
	}

	public void setAdGrpTpCodeC(String adGrpTpCodeC) {
		this.adGrpTpCodeC = adGrpTpCodeC;
	}

	public String getCpTpCode() {
		return cpTpCode;
	}

	public void setCpTpCode(String cpTpCode) {
		this.cpTpCode = cpTpCode;
	}

	public String getPrdtTpCode() {
		return prdtTpCode;
	}

	public void setPrdtTpCode(String prdtTpCode) {
		this.prdtTpCode = prdtTpCode;
	}

	public String getAdvrtsStleTpCode() {
		return advrtsStleTpCode;
	}

	public void setAdvrtsStleTpCode(String advrtsStleTpCode) {
		this.advrtsStleTpCode = advrtsStleTpCode;
	}
}
