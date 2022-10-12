package com.mobon.billing.model;

import com.mobon.code.constant.old.CodeConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PollingData {

	// 기본 데이터
	@JsonAlias({"key"})								private String key = "";				// kafka message key
	@JsonAlias({"className"})						private String className = "";			// 바인딩 객체명
	@JsonProperty("dumpType")	   			 		private String dumpType = "";			// 처리 타입
	@JsonProperty("sendDate")						private String sendDate = "";			// 전송일시

	// 기본 key 관련 데이터
	@JsonAlias({"yyyymmdd", "ymd","sdate"})			private String yyyymmdd = "";			// 처리일자
	@JsonAlias({"hh"})								private String hh = "";					// 처리시간
	@JsonProperty("platform")	    				private String platform = "01";			// 플랫폼
	@JsonProperty("product")	    				private String product = "01";			// 광고 노출형태
	@JsonAlias({"adGubun", "gb", "gubun"})			private String adGubun = "";			// 광고 타게팅기법
	@JsonAlias({"userId", "userid", "u", "uid"})	private String advertiserId = "";		// 광고주ID
	@JsonAlias({"scriptUserId", "scriptId",
			"sid", "media_id", "mediaid"})			private String scriptUserId = "";		// 매체ID
	@JsonAlias({"site_code", "sc"})					private String siteCode = "";			// 사이트코드
	@JsonAlias({"media_code", "s", "script_no"
			, "mediaCode", "no"})					private String mediaCode = "0";			// 매체코드
	@JsonProperty("scriptHirnkNo")					private String scriptHirnkNo= "";		//
	@JsonProperty("type")							private String type = "";				// 노출(V), 클릭(C)
	@JsonProperty("chargeCode")						private String chrgTpCode = "";			// 과금타입
	@JsonProperty("serviceCode")					private String svcTpCode = "";			// 서비스타입
	@JsonProperty("interlock")						private String interlock = "01";		// 연동정보 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	@JsonAlias({"ip","keyIp"})						private String ip = "";					// ip

	// value 관련 데이터
	@JsonAlias({"viewcnt1", "viewcnt"})				private int viewcnt1 = 0;				// 노출
	@JsonProperty("viewcnt2")						private int viewcnt2 = 0;				//
	@JsonProperty("viewcnt3")						private int viewcnt3 = 0;				// 지면노출
	@JsonProperty("clickcnt")						private int clickcnt = 0;				// 클릭
	@JsonAlias({"point","p"})						private float point = 0;				// 포인트
	@JsonProperty("mpoint")							private float mpoint = 0;				// 매체 정산 포인트
	@JsonProperty("ordercnt")						private int ordercnt = 0;				// 주문 수
	@JsonProperty("avalCallTime")					private int avalCallTime = 0;			// 유효콜 콜타임
	@JsonProperty("dbCnvrsCnt")						private int dbCnvrsCnt = 0;				// DB 전환수

	// 기본 key 이외의 key or flag 관련 데이터
	@JsonProperty("kpiNo")							private String kpiNo= "";				// kpiNo
	@JsonAlias({"auId","au_id"})					private String auId= "";				// auid
	@JsonProperty("uniId")							private String uniId = "";				// UniId
	@JsonProperty("chargeType")						private String chargeType = "";			// 과금타입 ???
	@JsonProperty("statYn")							private String statYn = "Y";			// 사용여부
	@JsonProperty("noExposureYN")					private String noExposureYN = "N";		// 미노출 여부

	// 로직에서 셋하는 데이터
	private String ctgrSeq= "";					// 카테고리의 번호
	private String mediaTpCode= "";				// media_site.scate

	private static final Logger logger = LoggerFactory.getLogger(PollingData.class);

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * convertData
	 * PollingData 를 Billing 용으로 value 관련 데이터를 변환하는 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-5-16
	 */
	public void convertData() {
		boolean result = true;

		// className 기반으로 데이터 변환
		if (G.RTBReportData.equals(this.getClassName())) {
			this.setProduct(G.MBW);
			this.setType(G.VIEW);
			this.setPoint(0);
			this.setPlatform(this.getPlatform().substring(0,1).toUpperCase());
			this.setInterlock(G.convertITL_TP_CODE(this.getScriptUserId()));

		} else if (G.RTBDrcData.equals(this.getClassName())) {
			this.setPlatform(this.getPlatform().substring(0,1).toUpperCase());
			this.setType(G.CLICK);
			this.setClickcnt(1);
			this.setInterlock(G.convertITL_TP_CODE(this.getScriptUserId()));

		} else if (G.ShortCutData.equals(this.getClassName())) {
			this.setPlatform(G.UPPER_M);
			this.setProduct(G.MBW);

			// 정상로직 (나중에 이거로 바꿔야함)
			/*this.setType(G.CLICK);
			this.setClickcnt(1);*/

			// 현재 CLICK_VIEW 로직
			this.setClickcnt((G.CLICK.equals(this.getType())) ? 1 : this.getClickcnt());

		} else if (G.AdChargeData.equals(this.getClassName())) {
			// dumpType 기반으로 데이터 변환
			convertFromDumpType();

		} else if (G.DrcData.equals(this.getClassName())) {
			this.setType(G.CLICK);
			this.setClickcnt(1);

			// dumpType 기반으로 데이터 변환
			convertFromDumpType();

		}

		// 아이커버 인경우 노출 = 클릭이므로 type 구분없이 벨류 세팅
		if ("03".equals(G.convertPRDT_CODE(this.getProduct()))) {
			this.setViewcnt3(this.getViewcnt1());
			this.setClickcnt(this.getViewcnt1());
		}

		// 플러스콜 인경우 클릭카운트 제어 - 유효콜 및 DB전환이 ConversionData 로 이동되면 삭제 가능
		if (G.DrcData.equals(this.getClassName())
				&& "08".equals(G.convertPRDT_CODE(this.getProduct()))
				&& (this.getAvalCallTime() >= 1 || this.getDbCnvrsCnt() >= 1)) {
			this.setClickcnt(0);
		}

		// 코드타입 변환 (항상 맨 아래 위치) - 부모 상속버전으로 바뀌면 이거 없애야함
		if (!StringUtils.isNumeric(this.getPlatform()))		this.setPlatform(G.convertPLATFORM_CODE(this.getPlatform()));
		if (!StringUtils.isNumeric(this.getProduct())) 		this.setProduct(G.convertPRDT_CODE(this.getProduct()));
		if (!StringUtils.isNumeric(this.getAdGubun())) 		this.setAdGubun(G.convertTP_CODE(this.getAdGubun()));
	}

	/**
	 * convertFromDumpType
	 * dumpType 기반으로 데이터 변환
	 *
	 * @author  : sjpark3
	 * @since   : 2022-6-2
	 */
	private void convertFromDumpType() {
		// 공통변형
		this.setAdGubun(("KP".equals(this.getAdGubun())) ? "KL" : this.getAdGubun());

		// dumpType 에 따른 벨류처리
		if (G.SKYCHARGE.equals(this.getDumpType())) {
			if (G.CLICK.equals(this.getType())) {
				this.setClickcnt(1);
			} else {
				if ((G.SKY + G.GUBUN + G.LOWER_M).equals(this.getProduct())) {
					this.setProduct(G.MBB);
				}
			}

		} else if (G.ICOCHARGE.equals(this.getDumpType())) {
			if ((G.ICO + G.GUBUN + G.LOWER_M).equals(this.getProduct()) || G.LOWER_M.equals(this.getPlatform())) {
				this.setProduct(G.MBE);
			}
			this.setViewcnt1(Math.max(this.getViewcnt1(), 1));

		} else if (G.PLAY_LINK_CHARGE.equals(this.getDumpType())) {
			if (G.CLICK.equals(this.getType())) {
				this.setClickcnt(1);
			} else {
				if (CodeConstants.AD.equalsIgnoreCase(this.getChrgTpCode())) {
					this.setMpoint(0);
				} else if (CodeConstants.MEDIA.equalsIgnoreCase(this.getChrgTpCode())) {
					this.setPoint(0);
				}
			}

		} else if (G.NORMALCHARGE.equals(this.getDumpType())) {
			if (!StringUtils.isBlank(this.getAdvertiserId()) && !StringUtils.isBlank(this.getAdGubun())) {
				if (!"PE".endsWith(this.getAdGubun()) && !"CA".endsWith(this.getAdGubun())) {
					this.setViewcnt1(Math.max(this.getViewcnt1(), 1));
				}
			}

		} else if (G.MOBILECHARGE.equals(this.getDumpType())) {

		} else if (G.DRCCHARGE.equals(this.getDumpType())) {
			this.setType(G.CLICK);
			this.setClickcnt(1);

			if (!G.NCT.equals(this.getProduct())
					&& (G.MBW.equals(this.getProduct()) || G.MBA.equals(this.getProduct())
					|| "mba_no_script".equals(this.getProduct()))) {
				this.setProduct(G.MBW);
			}

		} else if (G.SHOPCONCHARGE.equals(this.getDumpType())) {
			this.setType(G.CLICK);
			this.setClickcnt(1);

		} else if (G.ADDCHARGE.equals(this.getDumpType())) {
			this.setViewcnt1(0);
			this.setViewcnt2(0);
			this.setViewcnt3(0);
			this.setClickcnt(0);

			if ("i".equals(this.getProduct())) {
				this.setViewcnt1(1);
			} else if ("s".equals(this.getProduct())) {
				this.setViewcnt1(1);
				this.setViewcnt3(1);
			} else {
				this.setType(G.CLICK);
				this.setClickcnt(1);
			}

		} else if (G.ACTIONCHARGE.equals(this.getDumpType())) {
			this.setType("A");

		} else if (G.CONVERSIONCHARGE.equals(this.getDumpType())) {
			this.setType(G.CONVERSION);
			this.setOrdercnt(1);

		}

		// google, adfit 클릭카운트 안함
		if ("N".equals(this.getStatYn())) {
			this.setViewcnt1(0);
			this.setViewcnt2(0);
			this.setViewcnt3(0);
			this.setClickcnt(0);
		}
	}

	/**
	 * deepCopy
	 */
	public PollingData deepCopy() {
		PollingData result = new PollingData();

		result.setKey(this.getKey());
		result.setClassName(this.getClassName());
		result.setDumpType(this.getDumpType());
		result.setSendDate(this.getSendDate());
		result.setYyyymmdd(this.getYyyymmdd());
		result.setHh(this.getHh());
		result.setPlatform(this.getPlatform());
		result.setProduct(this.getProduct());
		result.setAdGubun(this.getAdGubun());
		result.setAdvertiserId(this.getAdvertiserId());
		result.setScriptUserId(this.getScriptUserId());
		result.setSiteCode(this.getSiteCode());
		result.setMediaCode(this.getMediaCode());
		result.setScriptHirnkNo(this.getScriptHirnkNo());
		result.setType(this.getType());
		result.setChrgTpCode(this.getChrgTpCode());
		result.setSvcTpCode(this.getSvcTpCode());
		result.setInterlock(this.getInterlock());
		result.setIp(this.getIp());
		result.setViewcnt1(this.getViewcnt1());
		result.setViewcnt2(this.getViewcnt2());
		result.setViewcnt3(this.getViewcnt3());
		result.setClickcnt(this.getClickcnt());
		result.setPoint(this.getPoint());
		result.setMpoint(this.getMpoint());
		result.setOrdercnt(this.getOrdercnt());
		result.setAvalCallTime(this.getAvalCallTime());
		result.setDbCnvrsCnt(this.getDbCnvrsCnt());
		result.setKpiNo(this.getKpiNo());
		result.setAuId(this.getAuId());
		result.setUniId(this.getUniId());
		result.setChargeType(this.getChargeType());
		result.setStatYn(this.getStatYn());
		result.setNoExposureYN(this.getNoExposureYN());
		result.setCtgrSeq(this.getCtgrSeq());
		result.setMediaTpCode(this.getMediaTpCode());

		return result;
	}

	/**
	 * getter, setter
 	 */
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getYyyymmdd() {
		try {
			if (G.DRCCHARGE.equals(this.getDumpType())
					|| G.SHOPCONCHARGE.equals(this.getDumpType())
					|| G.DrcData.equals(this.getClassName())
					|| G.RTBReportData.equals(this.getClassName())
					|| G.RTBDrcData.equals(this.getClassName())
					|| G.ShortCutData.equals(this.getClassName())
					|| StringUtils.isBlank(this.yyyymmdd)) {
				return DateUtils.getDate("yyyyMMdd", DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", this.getSendDate()));
			}
		} catch (Exception e) {
			logger.error("parse err - {}. {}", e, this);
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

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
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

	public int getOrdercnt() {
		return ordercnt;
	}

	public void setOrdercnt(int ordercnt) {
		this.ordercnt = ordercnt;
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

	public String getKpiNo() {
		return kpiNo;
	}

	public void setKpiNo(String kpiNo) {
		this.kpiNo = kpiNo;
	}

	public String getAuId() {
		return auId;
	}

	public void setAuId(String auId) {
		this.auId = auId;
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

	public int getClickcnt() {
		return clickcnt;
	}

	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
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

	public String getInterlock() {
		return interlock;
	}

	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getStatYn() {
		return statYn;
	}

	public void setStatYn(String statYn) {
		this.statYn = statYn;
	}

	public String getNoExposureYN() {
		return noExposureYN;
	}

	public void setNoExposureYN(String noExposureYN) {
		String tempNoExposureYN = noExposureYN;

		if (!"Y".equals(tempNoExposureYN) && !"N".equals(tempNoExposureYN)) {
			tempNoExposureYN = (Boolean.parseBoolean(noExposureYN)) ? "Y" : "N";
		}

		this.noExposureYN = tempNoExposureYN;
	}

	public String getCtgrSeq() {
		return ctgrSeq;
	}

	public void setCtgrSeq(String ctgrSeq) {
		this.ctgrSeq = ctgrSeq;
	}

	public String getMediaTpCode() {
		return mediaTpCode;
	}

	public void setMediaTpCode(String mediaTpCode) {
		this.mediaTpCode = mediaTpCode;
	}
}
