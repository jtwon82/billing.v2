package com.mobon.billing.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;

import com.adgather.util.old.StringUtils;
import net.sf.json.JSONObject;

public abstract class ConversionData implements Serializable {

	public String className="";
	public String keyCode="";
	public String grouping=""; // 그룹핑
	public String sendDate="";	// 전송날짜
	public String yyyymmdd=""; // 날짜
	public String hh=""; // 시간
	public int time=0; // 현재시간
	public Timestamp regDate; // 현재날짜
	public String keyIp=""; // 사용자 ip
	private String ipInfoList=""; // 간접컨버전 수집시
	private int posIpinfo=-1;
	public Long no=0L; // actionlog no
	public String advertiserId=""; // 광고주아이디
	public String scriptUserId=""; // 매체스크립트 아이디
	public String siteCode=""; // 캠페인코드
	public String adGubun=""; // 광고구분
	public String subadgubun="";
	public String recomTpCode="01";
	private String recomAlgoCode;
	public String platform=""; // 웹 모바일 구분
	public String product=""; // 광고상품
	public String type=""; // 노출, 클릭
	public String pCode=""; // 상품코드
	public String ordRFUrl=""; // 이전주소 referer
	public String ordPcode=""; // pcode
	public String ordCode=""; // 주문번호
	public int ordCnt=0;
	public int under1Min=0;
	public String chkingOrdCode=""; // 주문번호 확인용
	public String ordQty=""; // 수량
	public String pnm=""; // 상품명
	public String price=""; // 가격
	public String userName=""; // 이름
	public String gender=""; // 성별
	public String userPno=""; // 전화번호
	public String userAge=""; // 나이
	public int direct=0; // 컨버전타입 24:직접, 7:간접, 1:실시간
	public String inHour=""; // 24시간 : 직, 간접
	
	private int direct2=0;	// abtest 세션여부
	private String inHour2="0";	// abtest 직,간접여부
	
	public String mcgb=""; //
	public int scriptNo=0; // 스크립트코드 (s와 동일)
	private int retryCnt=0;

	//public ConvTraceData convTrace; // 모비온광고 마지막클릭시간
	public String lastClickTime=""; // 모비온광고 마지막클릭시간
	private String clickRegDate="";	// action_log clickdate
	public String mobonYn=""; // 모비온광고 여부
	public String inflowRoute=""; // 모비온광고 사용자경로 분석용
	
	//public FrameData frameData; // 프레임 지정id
	public String serviceHostId=""; // A/B 호스트ip(마지막자리)
	public String abTests=""; // A/B Test 용
	public String abType=""; // A/B 프리퀀시 구분자
	public String rtbType=""; // A/B ECPM 구분자
	
	//public ABTestData aBTestData; // A/B ECPM 구분자
	public String frameId=""; // 프레임 지정id
	public String prdtTpCode=""; // 상품코드
	public String frameCombiKey=""; //
	public String frameType=""; // 

	public int frameCycleNum=0; // 프레임 사이클회차
	public String frameSelector=""; // 프레임 선택자
	
	public int freqLog=0; // 프리퀀시 로그용

	public int partition=0; // kafka partition 
	public long offset=0L; // kafka lastoffset
	public String key=""; // kafka key
	public String serverName=""; // kafka server.name

	/* 신규 프리퀀시 정책 A/B 테스트를 위한 필드 */
	public int before_direct;		// 기존 direct
	public String before_inHour;	// 기존 inHour
	private String regUserId="";
	private String regDttm="";
	
	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public String getKeyIp() {
		return keyIp;
	}

	public void setKeyIp(String keyIp) {
		this.keyIp = keyIp;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
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

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getOrdRFUrl() {
		return ordRFUrl;
	}

	public void setOrdRFUrl(String ordRFUrl) {
		this.ordRFUrl = ordRFUrl;
	}

	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = StringUtils.removeEmoji(ordCode);
	}

	public String getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}

	public String getPnm() {
		return pnm;
	}

	public void setPnm(String pnm) {
		/* 우선 이모지를 String 에서 제외하고
         String 의 길이가 200 (DB 컬럼 사이즈) 이 넘으면
         우선 URL 디코드 수행하고 그래도 길이가 200이 넘거나
         혹은 디코딩 Exception 발생하면 길이를 200 까지 제한해서 세트 */

		String tempPnm = StringUtils.removeEmoji(pnm);

		if (tempPnm.length() >= 200) {
			try {
				String decodePnm = URLDecoder.decode(tempPnm, "UTF-8");

				tempPnm = (decodePnm.length() >= 200) ? decodePnm.substring(0,200) : decodePnm;
			} catch (UnsupportedEncodingException e) {
				tempPnm = tempPnm.substring(0,200);
			}
		}

		this.pnm = tempPnm;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserPno() {
		return userPno;
	}

	public void setUserPno(String userPno) {
		this.userPno = userPno;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getFreqLog() {
		return freqLog;
	}

	public void setFreqLog(int freqLog) {
		this.freqLog = freqLog;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	

	@Override
	public String toString() {
		return getClass().getSimpleName() + " "+ JSONObject.fromObject(this).toString();
	}
	
	public abstract String generateKey();
	
	public abstract String sumGethering();

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

	public String getLastClickTime() {
		return lastClickTime;
	}

	public void setLastClickTime(String lastClickTime) {
		this.lastClickTime = lastClickTime;
	}

	public String getMobonYn() {
		return mobonYn;
	}

	public void setMobonYn(String mobonYn) {
		this.mobonYn = mobonYn;
	}

	public String getInflowRoute() {
		return inflowRoute;
	}

	public void setInflowRoute(String inflowRoute) {
		this.inflowRoute = inflowRoute;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getInHour() {
		return inHour;
	}

	public void setInHour(String inHour) {
		this.inHour = inHour;
	}

	public String getMcgb() {
		return mcgb;
	}

	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getHh() {
		return hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getOrdPcode() {
		return ordPcode;
	}

	public void setOrdPcode(String ordPcode) {
		this.ordPcode = ordPcode;
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

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public int getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	
	public int increaseRetryCnt() {
		return ++retryCnt;
	}

	public int getBefore_direct() {
		return before_direct;
	}

	public void setBefore_direct(int before_direct) {
		this.before_direct = before_direct;
	}

	public String getBefore_inHour() {
		return before_inHour;
	}

	public void setBefore_inHour(String before_inHour) {
		this.before_inHour = before_inHour;
	}
	
	public String getChkingOrdCode() {
		return chkingOrdCode;
	}

	public void setChkingOrdCode(String chkingOrdCode) {
		this.chkingOrdCode = chkingOrdCode;
	}

	public int getDirect2() {
		return direct2;
	}

	public void setDirect2(int direct2) {
		this.direct2 = direct2;
	}

	public String getInHour2() {
		return inHour2;
	}

	public void setInHour2(String inHour2) {
		this.inHour2 = inHour2;
	}

	public String getClickRegDate() {
		return clickRegDate;
	}

	public void setClickRegDate(String clickRegDate) {
		this.clickRegDate = clickRegDate;
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

	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}

	public String getRegDttm() {
		return regDttm;
	}

	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}

	public int getOrdCnt() {
		return ordCnt;
	}

	public void setOrdCnt(int ordCnt) {
		this.ordCnt = ordCnt;
	}

	public int getUnder1Min() {
		return under1Min;
	}

	public void setUnder1Min(int under1Min) {
		this.under1Min = under1Min;
	}

	public String getRecomAlgoCode() {
		return recomAlgoCode;
	}

	public void setRecomAlgoCode(String recomAlgoCode) {
		this.recomAlgoCode = recomAlgoCode;
	}

	public String getIpInfoList() {
		return ipInfoList;
	}

	public void setIpInfoList(String ipInfoList) {
		this.ipInfoList = ipInfoList;
	}

	public int getPosIpinfo() {
		return posIpinfo;
	}

	public void setPosIpinfo(int posIpinfo) {
		this.posIpinfo = posIpinfo;
	}
}
