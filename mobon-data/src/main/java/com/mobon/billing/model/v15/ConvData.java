package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ConversionData;

import net.sf.json.JSONArray;

public class ConvData extends ConversionData implements Serializable {
	
	private long shopconSerealNo = 0;
	private long shopconWeight = 0;
	private boolean useYmdhms=false;
	private String ymdhms="";
	private int pastClickMinute=0;

	// TODO : dump > was 이동
	private int cookieDirect=0; 
	private int cookieInDirect=0; 
	private boolean conversionDirect=false;

	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동

	// 지역코드
	private String userNearCode = ""; // 유저의 지역코드
	private String nearCode = ""; // 지역코드 동코드 이상 시군구
	private Boolean nearYn = false; // 지역광고 체크
	private int newIpCnt = 0; // 처음조회된 지역 건수
	private int rgnIpCnt = 0; // 지역조회된 IP 건수
	private String realIp;

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	
	private String kno="0";
	private String kwrdSeq="0";
	private String intgSeq="0";
	private Map<String, String> intgSeqs= new HashMap();
	private Map<String, String> abusingMap= new HashMap();
	private boolean abusingExcept= false;

	private String intgTpCode="";
	private String crossbrYn="";
	private int intgLogCnt=1;
	private String svcTpCode="";
	private String advrtsTpCode="";
	private String cnvrsTpCode="02";
	private String pltfomTpCode="";
	private String ergabt="";
	private String ergdetail="";

	private int diffClickTime=0;
	
	private int ctgrSeq=0;
	private String au_id="";

	private String deviceDiv = "PC";
	private String os="";
	private String browser="";
	private String browserVersion="";

	private String osCode="";
	private String browserCode="";
	private String browserCodeVersion="";
	private String deviceCode="";
	
	private String crossLoginIp="";
	private String keywordValue="";
	private String keywordType="";
	private String keywordSessionType="";
	private String keywordUrl="";

	private String continueConv="";
	private String longContinueConv="";
	private String in1hourYn="";
	private String socialYn="";
	
	private String cnvrsAbusingTpCode="";
	
	//MOB_CNVRS_RENEW_NCL
	private String trkTpCode = "";
	private boolean noExposureYN  = false; // 미노출 여부
	private String mainDomainYN  = "Y"; // ADBLOCK 여부


	private boolean isCrossAuIdYN  = false; // CrossDevice 여부
	
	//브라우저 세션 
	private String browserDirect = "N";
	private String frameMatrExposureYN = "N";// 프레임 AB 테스트 구분값
	private String frameSendTpCode = "";
	
	//소재 카피 
	private JSONArray mobAdGrpData = null;		// 소재카피 array 
	private String grpId = "";							//소재 카피 ID 
	private String grpTpCode = "";						//소재 카피 Tpcode
	private String subjectCopyTpCode = "";				//소재 카피 img_copy_tp_code
	private String advrtsStleTpCode = ""; 				// 소재 카피 고정배너 여부 02 - 사용
	
	//Frame RTB 조합형 대상 필터링 
	private String frameCombiTargetYn = "N";		//AB TEST 조합형 대상 필터링  (AB_FRME_SIZE)
	//Frame 카이스트 모델 통계 	
	private String frameKaistRstCode = "";
	private String frameRtbTypeCode = "";
	//Frame 전환 재전송을 위한 추가 
	private JSONArray adverProdData = new JSONArray();
	private String frameSize = "";
	
	//전환시  기여부여 모델 
	private BigDecimal contributeOrdCnt;
	private BigDecimal contributePrice;
	//action log 조회된 기준 날짜 
	private String partdt = "";
	
	//Ai캠페인
	private String aiType = "";					// 캠페인 생성시 ai 사용유무

	//abTestTy
	private String abTestTy = "";

	//상품 타겟팅 여부
	private boolean targetYn = false;
	
	public ConvData(String yyyymmdd, String userId, String siteCode) {
		this.setYyyymmdd(yyyymmdd);
		this.setSiteCode(siteCode);
	}

	public ConvData() {
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

	public String getConvKey() {
		return String.format("%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getOrdCode(), this.getKeyIp());
	}
	
	public void sumGethering(ConvData record) {
	}

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getPlatform()
				, this.getKeyIp(), this.getAdvertiserId(), this.getScriptNo(), this.getSiteCode(), this.getAdGubun()
				, this.getProduct(), this.getType());
		setGrouping(String.format("[%s]", this.getPlatform()));
		return keyCode;
	}

	@Override
	public String sumGethering() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static ConvData fromHashMap(Map from) {
		ConvData result = new ConvData();
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.sendDate	= StringUtils.trimToNull2(from.get("sendDate"),"");
		result.cookieInDirect	= Integer.parseInt(StringUtils.trimToNull2(from.get("cookieInDirect"),""));
		result.type	= StringUtils.trimToNull2(from.get("type"),"");
		result.inflowRoute	= StringUtils.trimToNull2(from.get("inflowRoute"),"");
		result.userAge	= StringUtils.trimToNull2(from.get("userAge"),"");
		result.ordCode	= StringUtils.trimToNull2(from.get("ordCode"),"");
		result.chkingOrdCode	= StringUtils.trimToNull2(from.get("chkingOrdCode"),"");
		result.lastClickTime	= StringUtils.trimToNull2(from.get("lastClickTime"),"");
		result.price	= StringUtils.trimToNull2(from.get("price"),"");
		result.ymdhms	= StringUtils.trimToNull2(from.get("ymdhms"),"");
		result.scriptUserId	= StringUtils.trimToNull2(from.get("scriptUserId"),"");
		result.ordPcode	= StringUtils.trimToNull2(from.get("ordPcode"),"");
		result.offset	= Integer.parseInt(StringUtils.trimToNull2(from.get("offset"),""));
		result.frameId	= StringUtils.trimToNull2(from.get("frameId"),"");
		result.prdtTpCode	= StringUtils.trimToNull2(from.get("prdtTpCode"),"");
		result.frameCombiKey	= StringUtils.trimToNull2(from.get("frameCombiKey"),"");
		result.frameType	= StringUtils.trimToNull2(from.get("frameType"),"");
		result.ordRFUrl	= StringUtils.trimToNull2(from.get("ordRFUrl"),"");
		result.freqLog	= Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),""));
		result.grouping	= StringUtils.trimToNull2(from.get("grouping"),"");
		result.pnm	= StringUtils.trimToNull2(from.get("pnm"),"");
		result.pastClickMinute	= Integer.parseInt(StringUtils.trimToNull2(from.get("pastClickMinute"),""));
		result.keyCode	= StringUtils.trimToNull2(from.get("keyCode"),"");
		result.serviceHostId	= StringUtils.trimToNull2(from.get("serviceHostId"),"");
		result.pCode	= StringUtils.trimToNull2(from.get("pCode"),"");
		result.shopconSerealNo	= Integer.parseInt(StringUtils.trimToNull2(from.get("shopconSerealNo"),""));
		result.rtbType	= StringUtils.trimToNull2(from.get("rtbType"),"");
		result.no	= Long.parseLong(StringUtils.trimToNull2(from.get("no"),""));
		result.mcgb	= StringUtils.trimToNull2(from.get("mcgb"),"");
		result.frameCycleNum	= Integer.parseInt(StringUtils.trimToNull2(from.get("frameCycleNum"),""));
		result.gender	= StringUtils.trimToNull2(from.get("gender"),"");
		result.abTests	= StringUtils.trimToNull2(from.get("abTests"),"");
		result.direct	= Integer.parseInt(StringUtils.trimToNull2(from.get("direct"),""));
		//result.regDate	= StringUtils.trimToNull2(from.get("regDate"),"");
		result.serverName	= StringUtils.trimToNull2(from.get("serverName"),"");
		result.className	= StringUtils.trimToNull2(from.get("className"),"");
		result.ordQty	= StringUtils.trimToNull2(from.get("ordQty"),"");
		result.platform	= StringUtils.trimToNull2(from.get("platform"),"");
		result.partition	= Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),""));
		result.scriptNo	= Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),""));
		result.userPno	= StringUtils.trimToNull2(from.get("userPno"),"");
		result.key	= StringUtils.trimToNull2(from.get("key"),"");
		result.mobonYn	= StringUtils.trimToNull2(from.get("mobonYn"),"");
		result.product	= StringUtils.trimToNull2(from.get("product"),"");
		result.siteCode	= StringUtils.trimToNull2(from.get("siteCode"),"");
		result.abType	= StringUtils.trimToNull2(from.get("abType"),"");
		result.adGubun	= StringUtils.trimToNull2(from.get("adGubun"),"");
		result.cookieDirect	= Integer.parseInt(StringUtils.trimToNull2(from.get("cookieDirect"),""));
		result.shopconWeight	= Integer.parseInt(StringUtils.trimToNull2(from.get("shopconWeight"),""));
		result.userName	= StringUtils.trimToNull2(from.get("userName"),"");
		result.advertiserId	= StringUtils.trimToNull2(from.get("advertiserId"),"");
		result.frameSelector	= StringUtils.trimToNull2(from.get("frameSelector"),"");
		result.inHour	= StringUtils.trimToNull2(from.get("inHour"),"");
		result.keyIp	= StringUtils.trimToNull2(from.get("keyIp"),"");
		result.yyyymmdd	= StringUtils.trimToNull2(from.get("yyyymmdd"),"");
		result.time	= Integer.parseInt(StringUtils.trimToNull2(from.get("time"),""));
		result.interlock	= StringUtils.trimToNull2(from.get("interlock"),"");
		result.ergabt	= StringUtils.trimToNull2(from.get("ergabt"),"");
		result.ergdetail	= StringUtils.trimToNull2(from.get("ergdetail"),"");
		result.keywordValue	= StringUtils.trimToNull2(from.get("keywordValue"),"");
		result.keywordType	= StringUtils.trimToNull2(from.get("keywordType"),"");
		result.keywordSessionType	= StringUtils.trimToNull2(from.get("keywordSessionType"),"");
		result.keywordUrl	= StringUtils.trimToNull2(from.get("keywordUrl"),"");

		// 전환시 사용.
		result.bHandlingStatsMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false"));
		result.bHandlingStatsPointMobon	= Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false"));

		result.crossbrYn	= StringUtils.trimToNull2(from.get("crossbrYn"),"N");
		
		result.useYmdhms = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("useYmdhms"),"false"));
		
		result.deviceDiv = StringUtils.trimToNull2(from.get("deviceDiv"),"PC");
		result.setOs(StringUtils.trimToNull2(from.get("os"),""));
		result.setBrowser(StringUtils.trimToNull2(from.get("browser"),""));
		result.setBrowserVersion(StringUtils.trimToNull2(from.get("browserVersion"),""));
		
		result.setOsCode(StringUtils.trimToNull2(from.get("osCode"),"etc"));
		result.setBrowserCode(StringUtils.trimToNull2(from.get("drowserCode"),"etc"));
		result.setBrowserCodeVersion(StringUtils.trimToNull2(from.get("drowserCodeVersion"),"etc"));
		result.setDeviceCode(StringUtils.trimToNull2(from.get("deviceCode"),"MA"));

		result.setContinueConv(StringUtils.trimToNull2(from.get("continueConv"),""));
		result.setLongContinueConv(StringUtils.trimToNull2(from.get("longContinueConv"),""));
		result.setIn1hourYn(StringUtils.trimToNull2(from.get("in1hourYn"),"N"));

		result.socialYn	= StringUtils.trimToNull2(from.get("socialYn"),"N");
		result.cnvrsAbusingTpCode	= StringUtils.trimToNull2(from.get("cnvrsAbusingTpCode"),"");

		result.recomTpCode	= StringUtils.trimToNull2(from.get("recomTpCode"),"");
		result.setRecomAlgoCode( StringUtils.trimToNull2(from.get("recomAlgoCode"),"") );
		result.setAu_id( StringUtils.trimToNull2(from.get("AU_ID"),"") );

		result.conversionDirect = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("conversionDirect"), "false"));
		result.trkTpCode = StringUtils.trimToNull2(from.get("trkTpCode"), "");
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setMainDomainYN(StringUtils.trimToNull2(from.get("mainDomainYN"),"Y"));
		result.setCrossAuIdYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("isCrossAuIdYN"),"false")));

		result.setFrameMatrExposureYN(StringUtils.trimToNull2(from.get("frameMatrExposureYN"), "N"));
		result.setFrameSendTpCode(StringUtils.trimToNull2(from.get("frameSendTpCode"), ""));

		//소재카피
		result.setMobAdGrpData((JSONArray) (from.get("mobAdGrp")));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"),"99"));
		
		//Frame RTB 조합형 대상 필터링
		result.setFrameCombiTargetYn(StringUtils.trimToNull2(from.get("frameCombiTargetYN"), "N"));
		result.setFrameKaistRstCode(StringUtils.trimToNull2(from.get("frameKaistRstCode"), "998"));
		result.setFrameRtbTypeCode(StringUtils.trimToNull2(from.get("frameRtbTypeCode"), ""));
		if (from.get("adverProdData") == null) {
			result.setAdverProdData(new JSONArray());
		} else {
			result.setAdverProdData((JSONArray) (from.get("adverProdData")));			
		}
		
		result.setFrameSize(StringUtils.trimToNull2(from.get("frameSize"), ""));
		
		// Ai캠페인
		result.setAiType(StringUtils.trimToNull2(from.get("aiType"), ""));

		//abTestTy
		result.setAbTestTy(StringUtils.trimToNull2(from.get("abTestTy"), ""));
		
		return result;
	}

	public AdChargeData toAdChargeData(){
		AdChargeData result = new AdChargeData();
		result.setAb_type(this.getAbType());
		result.setAdGubun(this.getAdGubun());
		result.setDirect(this.getDirect()+"");
		result.setFrameCycleNum(this.getFrameCycleNum());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		result.setFrameSelector(this.getFrameSelector());
		result.setInflow_route(this.getInflowRoute());
		result.setIp(this.getKeyIp());
		result.setLast_click_time(this.getLastClickTime());
		result.setMedia_code(this.getScriptNo()+"");
		result.setMobon_yn(this.getMobonYn());
		result.setOrdCode(this.getOrdCode());
		result.setOrdQty(this.getOrdQty());
		result.setOrdRFUrl(this.getOrdRFUrl());
		result.setPNm(this.getPnm());
		result.setPrice(this.getPrice());
		result.setRegdate(this.getRegDate());
		result.setRtb_type(this.getRtbType());
		result.setSite_code(this.getSiteCode());
		result.setTime(this.getTime());
		result.setType(this.getType());
		result.setUname(this.getUserName());
		result.setUpno(this.getUserPno());
		result.setUserId(this.getAdvertiserId());
		result.setUsex(this.getGender());
		result.setYyyymmdd(this.getYyyymmdd());
		result.setNoExposureYN(this.isNoExposureYN());
		result.setMainDomainYN(this.getMainDomainYN());
		result.setCrossAuIdYN(this.isCrossAuIdYN());
		result.setFrameMatrExposureYN(this.getFrameMatrExposureYN());
		result.setFrameSendTpCode(this.getFrameSendTpCode());
		
		//소재 카피 
		result.setMobAdGrpData(this.getMobAdGrpData());
		result.setAdvrtsStleTpCode(this.getAdvrtsStleTpCode());
		
		result.setFrameCombiTargetYn(this.getFrameCombiTargetYn());
		result.setFrameKaistRstCode(this.getFrameKaistRstCode());
		result.setFrameRtbTypeCode(this.getFrameRtbTypeCode());
		
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize(this.getFrameSize());
		
		// Ai캠페인
		result.setAiType(this.getAiType());

		
		return result;
	}
//	public String toAdChargeDataJson(){
//		return new Gson().toJson( toAdChargeData() );
//	}

	public BaseCVData toBaseCVData(){
		BaseCVData result = new BaseCVData();
		result.setAdGubun(this.getAdGubun());
		result.setAdvertiserId(this.getAdvertiserId());
		result.setGender(this.getGender());
		result.setKeyIp(this.getKeyIp());
		result.setUserAge(this.getUserAge());
		result.setServiceHostId(this.getServiceHostId());
		result.setAbTest(this.getAbTests());
		result.setAbType(this.getAbType());
		result.setFrameId(this.getFrameId());
		result.setPrdtTpCode(this.getPrdtTpCode());
		result.setFrameCombiKey(this.getFrameCombiKey());
		result.setFrameType(this.getFrameType());
		result.setFrameCycleNum(this.getFrameCycleNum());
		result.setFrameSelector(this.getFrameSelector());
		result.setScriptNo(this.getScriptNo());
		result.setSiteCode(this.getSiteCode());
		result.setType(this.getType());
		result.setYyyymmdd(this.getYyyymmdd());
		result.setFreqLog(this.getFreqLog());
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
		result.setFrameKaistRstCode(this.getFrameKaistRstCode());
		result.setFrameRtbTypeCode(this.getFrameRtbTypeCode());
		
		result.setAdverProdData(this.getAdverProdData());
		result.setFrameSize(this.getFrameSize());
		
		// Ai캠페인
		result.setAiType(this.getAiType());

		result.setAbTestTy(this.getAbTestTy());

		return result;
	}
	

	public long getShopconWeight() {
		return shopconWeight;
	}

	public void setShopconWeight(long shopconWeight) {
		this.shopconWeight = shopconWeight;
	}

	public long getShopconSerealNo() {
		return shopconSerealNo;
	}

	public void setShopconSerealNo(long shopconSerealNo) {
		this.shopconSerealNo = shopconSerealNo;
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

	public String getYmdhms() {
		return ymdhms;
	}

	public void setYmdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}

	public String getInterlock() {
		return interlock;
	}

	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}

	public int getPastClickMinute() {
		return pastClickMinute;
	}

	public void setPastClickMinute(int pastClickMinute) {
		this.pastClickMinute = pastClickMinute;
	}

	public boolean isConversionDirect() {
		return conversionDirect;
	}

	public void setConversionDirect(boolean conversionDirect) {
		this.conversionDirect = conversionDirect;
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

	public boolean isUseYmdhms() {
		return useYmdhms;
	}

	public void setUseYmdhms(boolean useYmdhms) {
		this.useYmdhms = useYmdhms;
	}

	public String getKno() {
		return kno;
	}

	public void setKno(String kno) {
		this.kno = kno;
	}

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

	public String getCnvrsTpCode() {
		return cnvrsTpCode;
	}

	public void setCnvrsTpCode(String cnvrsTpCode) {
		this.cnvrsTpCode = cnvrsTpCode;
	}

	public String getPltfomTpCode() {
		return pltfomTpCode;
	}

	public void setPltfomTpCode(String pltfomTpCode) {
		this.pltfomTpCode = pltfomTpCode;
	}

	public String getIntgSeq() {
		return intgSeq;
	}

	public void setIntgSeq(String intgSeq) {
		this.intgSeq = intgSeq;
	}

	public String getAdvrtsTpCode() {
		return advrtsTpCode;
	}

	public void setAdvrtsTpCode(String advrtsTpCode) {
		this.advrtsTpCode = advrtsTpCode;
	}

	public Map<String, String> getIntgSeqs() {
		return intgSeqs;
	}

	public void setIntgSeqs(Map intgSeqs) {
		this.intgSeqs = intgSeqs;
	}

	public String getIntgTpCode() {
		return intgTpCode;
	}

	public void setIntgTpCode(String intgTpCode) {
		this.intgTpCode = intgTpCode;
	}

	public int getIntgLogCnt() {
		return intgLogCnt;
	}

	public void setIntgLogCnt(int intgLogCnt) {
		this.intgLogCnt = intgLogCnt;
	}

	public int getDiffClickTime() {
		return diffClickTime;
	}

	public void setDiffClickTime(int diffClickTime) {
		this.diffClickTime = diffClickTime;
	}

	public String getCrossbrYn() {
		return crossbrYn;
	}

	public void setCrossbrYn(String crossbrYn) {
		this.crossbrYn = crossbrYn;
	}

	public int getCtgrSeq() {
		return ctgrSeq;
	}

	public void setCtgrSeq(int ctgrSeq) {
		this.ctgrSeq = ctgrSeq;
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

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
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

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getBrowserCodeVersion() {
		return browserCodeVersion;
	}

	public void setBrowserCodeVersion(String browserCodeVersion) {
		this.browserCodeVersion = browserCodeVersion;
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

	public String getCnvrsAbusingTpCode() {
		return cnvrsAbusingTpCode;
	}

	public void setCnvrsAbusingTpCode(String cnvrsAbusingTpCode) {
		this.cnvrsAbusingTpCode = cnvrsAbusingTpCode;
	}

	public String getAu_id() {
		return au_id;
	}

	public void setAu_id(String au_id) {
		this.au_id = au_id;
	}

	public Map<String, String> getAbusingMap() {
		return abusingMap;
	}

	public void setAbusingMap(Map<String, String> abusingMap) {
		this.abusingMap = abusingMap;
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
	public String getBrowserDirect() {
		return browserDirect;
	}

	public void setBrowserDirect(String browserDirect) {
		this.browserDirect = browserDirect;
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

	public boolean isAbusingExcept() {
		return abusingExcept;
	}

	public void setAbusingExcept(boolean abusingExcept) {
		this.abusingExcept = abusingExcept;
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

	public BigDecimal getContributeOrdCnt() {
		return contributeOrdCnt;
	}

	public void setContributeOrdCnt(BigDecimal contributeOrdCnt) {
		this.contributeOrdCnt = contributeOrdCnt;
	}

	public BigDecimal getContributePrice() {
		return contributePrice;
	}

	public void setContributePrice(BigDecimal contributePrice) {
		this.contributePrice = contributePrice;
	}

	public String getPartdt() {
		return partdt;
	}

	public void setPartdt(String partdt) {
		this.partdt = partdt;
	}
	
	public String getAiType() {
		return aiType;
	}
	
	public void setAiType(String aiType) {
		this.aiType = aiType;
	}

	public String getAbTestTy() {
		return abTestTy;
	}

	public void setAbTestTy(String abTestTy) {
		this.abTestTy = abTestTy;
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
