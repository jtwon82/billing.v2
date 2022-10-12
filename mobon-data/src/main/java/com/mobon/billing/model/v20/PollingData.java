package com.mobon.billing.model.v20;

import com.adgather.util.old.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON 매핑용 객체
 * (JSON에 없는 데이터는 필드에 선언하지 않음)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData {

	private static final Logger logger = LoggerFactory.getLogger(PollingData.class);

	// 기본 데이터
	@JsonAlias({"key"})								private String key = "";					// kafka message key
	@JsonAlias({"className"})						private String className = "";				// 바인딩 객체명
	@JsonProperty("dumpType")	   			 		private String dumpType = "";				// 처리타입
	@JsonProperty("sendDate")						private String sendDate = "";				// 전송일시

	// 기본 key 관련 데이터
	@JsonAlias({"yyyymmdd", "ymd","sdate"})			private String yyyymmdd = "";				// 처리일자
	@JsonAlias({"hh"})								private String hh = "";						// 처리시간
	@JsonAlias({"ip","keyIp"})						private String ip = "";						// 사용자 IP
	@JsonAlias({"auId","au_id"})					private String auId= "";					// 사용자 고유값
	@JsonProperty("uniId")							private String uniId = "";					// UniId
	@JsonProperty("platform")	    				private String platform = "";				// 플랫폼
	@JsonProperty("product")	    				private String product = "nor";				// 광고 노출형태
	@JsonAlias({"adGubun", "gb", "gubun"})			private String adGubun = "";				// 광고 타게팅기법
	@JsonAlias({"userId", "userid", "u", "uid"})	private String advertiserId = "";			// 광고주ID
	@JsonAlias({"scriptUserId", "scriptId",
			"sid", "media_id", "mediaid"})			private String scriptUserId = "";			// 매체ID
	@JsonAlias({"site_code", "sc"})					private String siteCode = "";				// 사이트코드
	@JsonProperty("kpiNo")							private String kpiNo = "";					// kpiNo
	@JsonAlias({"media_code", "s", "script_no",
			"mediaCode", "no"})						private String mediaCode = "";				// 매체코드
	@JsonProperty("scriptHirnkNo")					private String scriptHirnkNo = "";			// 부모지면 매체코드
	@JsonProperty("type")							private String type = "";					// 노출(V), 클릭(C)
	@JsonProperty("chargeCode")						private String chrgTpCode = "";				// 과금타입
	@JsonProperty("serviceCode")					private String svcTpCode = "";				// 서비스타입
	@JsonProperty("noExposureYN")					private String noExposureYN = "N";			// 미노출여부 (Y:미노출, N:정상노출)

	// value 관련 데이터
	@JsonAlias({"viewcnt1", "viewcnt"})				private int viewcnt1 = 0;					// 노출
	@JsonProperty("viewcnt2")						private int viewcnt2 = 0;					//
	@JsonProperty("viewcnt3")						private int viewcnt3 = 0;					// 지면노출
	@JsonProperty("clickcnt")						private int clickcnt = 0;					// 클릭
	@JsonAlias({"point","p"})						private float point = 0;					// 포인트
	@JsonProperty("mpoint")							private float mpoint = 0;					// 매체 정산 포인트
	@JsonProperty("avalCallTime")					private int avalCallTime = 0;				// 유효콜 콜타임
	@JsonProperty("dbCnvrsCnt")						private int dbCnvrsCnt = 0;					// DB 전환수

	// 전환데이터
	@JsonProperty("ordCode")						private String ordCode = "";				// 주문번호
	@JsonProperty("ordQty")							private String ordQty = "";					// 주문수량
	@JsonProperty("PCODE")							private String ordPcode = "";				// 주문상품번호
	@JsonProperty("price")							private String price = "";					// 주문금액
	@JsonProperty("osCode")							private String osCode = "";					// 운영체제코드 (= OS_TP_CODE)
	@JsonProperty("browserCode")					private String browserCode = "";			// 브라우저코드 (= BROWSER_TP_CODE)
	@JsonProperty("deviceCode")						private String deviceCode = "";				// 디바이스코드 (= DEVICE_TP_CODE)
	@JsonProperty("browserCodeVersion")				private String browserCodeVersion = "";		// 브라우저버전
	@JsonProperty("inflow_route")					private String inflowRoute = "";			// 라우팅정보
	@JsonProperty("PNm")							private String pnm = "";					// 주문상품명
	@JsonProperty("trkTpCode")						private String trkTpCode = "90";			// 애드트래커 코드
	@JsonProperty("ymdhms")							private String ymdhms = "";					// 전환일자 송출에서 컨트롤할때 사용
	@JsonProperty("useYmdhms")						private boolean useYmdhms = false;			// 전환일자 송출에서 컨트롤할때 사용
	@JsonProperty("socialYn")						private String socialYn = "";				// 소셜판별 플래그
	@JsonProperty("cookieDirect")					private int cookieDirect = 0; 				// 직접매출 인정기간 (일)
	@JsonProperty("cookieInDirect")					private int cookieInDirect = 0; 			// 간접매출 인정기간 (일)
	@JsonProperty("direct")							private String direct = ""; 				// 전환타입 0:세션X, !0:세션O
	@JsonProperty("cnvrsTpCode")					private String cnvrsTpCode = ""; 			// 전환타입코드
	@JsonProperty("regUserId")						private String regUserId = ""; 				// 전환 등록자
	@JsonProperty("ipInfoList")						private String ipInfoList = ""; 			// 간접컨버전 수집시에 사용하는 ip 리스트
	@JsonProperty("crossLoginIp")					private List<String> crossLoginIp = new ArrayList<>();	//  ?

	/**
	 * 객체 깊은 복사 메소드
	 */
	public PollingData copy() {
		return new PollingData(this);
	}

	/**
	 * 객체 깊은 복사 생성자
	 */
	private PollingData(PollingData obj) {
		this.key = obj.key;
		this.className = obj.className;
		this.dumpType = obj.dumpType;
		this.sendDate = obj.sendDate;
		this.yyyymmdd = obj.yyyymmdd;
		this.hh = obj.hh;
		this.ip = obj.ip;
		this.auId = obj.auId;
		this.uniId = obj.uniId;
		this.platform = obj.platform;
		this.product = obj.product;
		this.adGubun = obj.adGubun;
		this.advertiserId = obj.advertiserId;
		this.scriptUserId = obj.scriptUserId;
		this.siteCode = obj.siteCode;
		this.kpiNo = obj.kpiNo;
		this.mediaCode = obj.mediaCode;
		this.scriptHirnkNo = obj.scriptHirnkNo;
		this.type = obj.type;
		this.chrgTpCode = obj.chrgTpCode;
		this.svcTpCode = obj.svcTpCode;
		this.noExposureYN = obj.noExposureYN;
		this.viewcnt1 = obj.viewcnt1;
		this.viewcnt2 = obj.viewcnt2;
		this.viewcnt3 = obj.viewcnt3;
		this.clickcnt = obj.clickcnt;
		this.point = obj.point;
		this.mpoint = obj.mpoint;
		this.avalCallTime = obj.avalCallTime;
		this.dbCnvrsCnt = obj.dbCnvrsCnt;
		this.ordCode = obj.ordCode;
		this.ordQty = obj.ordQty;
		this.ordPcode = obj.ordPcode;
		this.price = obj.price;
		this.osCode = obj.osCode;
		this.browserCode = obj.browserCode;
		this.deviceCode = obj.deviceCode;
		this.browserCodeVersion = obj.browserCodeVersion;
		this.inflowRoute = obj.inflowRoute;
		this.pnm = obj.pnm;
		this.trkTpCode = obj.trkTpCode;
		this.ymdhms = obj.ymdhms;
		this.useYmdhms = obj.useYmdhms;
		this.socialYn = obj.socialYn;
		this.cookieDirect = obj.cookieDirect;
		this.cookieInDirect = obj.cookieInDirect;
		this.direct = obj.direct;
		this.cnvrsTpCode = obj.cnvrsTpCode;
		this.regUserId = obj.regUserId;
		this.ipInfoList = obj.ipInfoList;
		this.crossLoginIp = obj.crossLoginIp;
	}

	/**
	 * Default Constructor
	 */
	public PollingData() {
	}

	/**
	 * Getter, Setter
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getYyyymmdd() {
		try {
			if (StringUtils.isBlank(yyyymmdd) || G.DRCCHARGE.equals(this.getDumpType()) || G.SHOPCONCHARGE.equals(this.getDumpType())
					|| "RTBReportData".equals(className) || "RTBDrcData".equals(className) || "ShortCutData".equals(className)) {
				return DateUtils.getDate("yyyyMMdd", DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", this.getSendDate()));
			}
		} catch (Exception e) {
		}

		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public String getHh() {
		return (StringUtils.isBlank(hh)) ? DateUtils.getDate("HH") : hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAuId() {
		return auId;
	}

	public void setAuId(String auId) {
		this.auId = auId;
	}

	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
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
		String tempScriptUserId = this.scriptUserId;

		if (StringUtils.isBlank(tempScriptUserId)) {
			tempScriptUserId = scriptUserId;
		}

		this.scriptUserId = tempScriptUserId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getKpiNo() {
		return kpiNo;
	}

	public void setKpiNo(String kpiNo) {
		this.kpiNo = kpiNo;
	}

	public String getMediaCode() {
		return mediaCode;
	}

	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}

	public String getScriptHirnkNo() {
		return scriptHirnkNo;
	}

	public void setScriptHirnkNo(String scriptHirnkNo) {
		this.scriptHirnkNo = scriptHirnkNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getNoExposureYN() {
		return noExposureYN;
	}

	public void setNoExposureYN(String noExposureYN) {
		this.noExposureYN = noExposureYN;
	}

	public int getViewcnt1() {
		return viewcnt1;
	}

	public void setViewcnt1(int viewcnt1) {
		int tempViewCnt1 = this.viewcnt1;

		if (tempViewCnt1 == 0) {
			tempViewCnt1 = viewcnt1;
		}

		this.viewcnt1 = tempViewCnt1;
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

	public int getClickcnt() {
		return clickcnt;
	}

	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
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

	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}

	public String getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}

	public String getOrdPcode() {
		return ordPcode;
	}

	public void setOrdPcode(String ordPcode) {
		this.ordPcode = ordPcode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getInflowRoute() {
		return inflowRoute;
	}

	public void setInflowRoute(String inflowRoute) {
		this.inflowRoute = inflowRoute;
	}

	public String getPnm() {
		return pnm;
	}

	public void setPnm(String pnm) {
		this.pnm = pnm;
	}

	public String getTrkTpCode() {
		return trkTpCode;
	}

	public void setTrkTpCode(String trkTpCode) {
		this.trkTpCode = trkTpCode;
	}

	public String getYmdhms() {
		return ymdhms;
	}

	public void setYmdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}

	public boolean isUseYmdhms() {
		return useYmdhms;
	}

	public void setUseYmdhms(boolean useYmdhms) {
		this.useYmdhms = useYmdhms;
	}

	public String getSocialYn() {
		return socialYn;
	}

	public void setSocialYn(String socialYn) {
		this.socialYn = socialYn;
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

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getCnvrsTpCode() {
		return cnvrsTpCode;
	}

	public void setCnvrsTpCode(String cnvrsTpCode) {
		this.cnvrsTpCode = cnvrsTpCode;
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

	public List<String> getCrossLoginIp() {
		return crossLoginIp;
	}

	public void setCrossLoginIp(List<String> crossLoginIp) {
		this.crossLoginIp = crossLoginIp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
