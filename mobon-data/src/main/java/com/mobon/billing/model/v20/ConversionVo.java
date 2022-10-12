package com.mobon.billing.model.v20;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConversionVo extends CommonVo implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ConversionVo.class);

	private int intervalHour = 0;				// 쿼리로 뽑아올 기간
	private int cookieDirect = 0; 				// 광고주가 설정한 직접매출 인정기간 (시간)
	private int cookieInDirect = 0; 			// 광고주가 설정한 간접매출 인정기간 (일)
	private int convDelayTimeMinute = 0;		// 전환딜레이 (분)
	private String pnm = ""; 					// 주문상품명
	private String price = ""; 					// 주문상품가격
	private String ymdhms = "";					// 전환일자 송출에서 컨트롤할때 값
	private String ordQty = ""; 				// 주문수량
	private String ordCode = ""; 				// 주문번호
	private String ordPcode = ""; 				// 주문상품코드
	private String logDttm = "";				// 노출이력 등록일시
	private String socialYn = "";				// 소셜판별 플래그
	private String trkTpCode = "90";			// 애드트래커 코드
	private String inflowRoute = "";			// 라우팅정보
	private String cnvrsTpCode = ""; 			// 전환타입코드
	private String directYn = "N";				// 직접매출 DB 인서트값(노출>전환 cookieDirect 시간이내:Y, 초과:N)
	private String sesionSelngYn = "N";			// 브라우저매출 DB 인서트값(노출>전환 30분 이내:Y, 초과:N)
	private String sesionSelng2Yn = "N";		// 세션매출 DB 인서트값(노출>전환 6시간이내:Y, 초과:N)
	private String regUserId = "";				// 전환 등록자
	private String ipInfoList = "";				// 쇼플 전환에 사용
	private String duplicatedOrderNo = "";		// 중복되는 주문번호
	private List<String> crossLoginIp = null;	// 쇼플 전환에 사용
	private boolean useYmdhms = false;			// 전환일자 송출에서 컨트롤할때 플래그
	private boolean conversionDirect = false;	// 강제 전환용 플래그 true:강제O, false:강제X

	/**
	 * PollingData를 이용해서
	 * 1. 객체변환
	 * 2. 유효성검증
	 * 3. 데이터가공
	 * 을 수행해서 해당 객체 반환 혹은 생성 안될경우 null 반환
	 */
	public static ConversionVo create(PollingData from) {
		PollingData pollingData = from.copy();
		ConversionVo conversionVo = null;

		// 객체변환
		conversionVo = ConversionVo.fromPollingData(pollingData);

		if (!validate(conversionVo)) {
			// 유효성 검증
			conversionVo = null;
		} else if (!convert(conversionVo)) {
			// 데이터 가공
			conversionVo = null;
		}

		return conversionVo;
	}

	/**
	 * ConversionVo의 유효성 검사
	 * @return true : 유효, false : 무효
	 */
	protected static boolean validate(ConversionVo from) {
		boolean result = true;

		try {
			// 테이블 파티션 체크 - 2주
			LocalDate objDate = LocalDate.parse(from.getYyyymmdd(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			if (objDate.isBefore(LocalDate.now().minusWeeks(2))) {
				logger.error("ConversionVo over partition - {}", from);
				result = false;
			}

			// 필수갑 체크
			/*if (StringUtils.isBlank(from.getAuId())
					|| StringUtils.isBlank(from.getIp())
					|| StringUtils.isBlank(from.getAdvertiserId())
					|| StringUtils.isBlank(from.getOrdCode())
					|| StringUtils.isBlank(from.getAdGubun())
					|| StringUtils.isBlank(from.getSiteCode())
					|| StringUtils.isBlank(from.getScriptNo())
			) {*/
			if (StringUtils.isBlank(from.getAuId())
					|| StringUtils.isBlank(from.getIp())
					|| StringUtils.isBlank(from.getAdvertiserId())
					|| StringUtils.isBlank(from.getOrdCode())
			) {
				logger.debug("ConversionVo no required values - {}", from);
				result = false;
			}

			// 무효 데이터 처리 안함
			/*if ("91".equals(from.getChrgTpCode())) {
				logger.debug("ConversionVo chrgTpCode is '91'");
				result = false;
			}*/

			// 스크립트 넘버 = 0 이면서 등록자가 rebuild 아니면 처리 안함
			/*if ("0".equals(from.getScriptNo()) && !"rebuild".equals(from.getRegUserId())) {
				logger.debug("ConversionVo scriptNo is 0, keyIp-{}, ordCode-{}", from.getIp(), from.getOrdCode());
				result = false;
			}*/

			// 1999 지면 외에는 2일전의 전환데이터만 처리한다
			/*LocalDateTime sendDate = LocalDateTime.parse(from.getSendDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			if (!"1999".equals(from.getScriptNo()) && sendDate.isBefore(LocalDateTime.now().minusDays(2))) {
				logger.info("ConversionVo is past date - {}", from);
				result = false;
			}*/

			// 2일전의 전환데이터만 처리한다
			LocalDateTime sendDate = LocalDateTime.parse(from.getSendDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			if (sendDate.isBefore(LocalDateTime.now().minusDays(2))) {
				logger.info("ConversionVo is past date - {}", from);
				result = false;
			}
		} catch (Exception e) {
			result = false;
			logger.error("ConversionVo validate err - {} / object - {}", e ,from);
		}

		return result;
	}

	/**
	 * PollingData를 ConversionVo에 맞게 데이터를 가공
	 */
	protected static boolean convert(ConversionVo from) {
		boolean result = true;

		try {
			// 주문번호 길이 제한
			String tempOrdCode = (from.getOrdCode() == null) ? "" : from.getOrdCode();
			if (tempOrdCode.length() > 30) {
				tempOrdCode = from.getOrdCode().substring(0,30);
			}
			from.setOrdCode(tempOrdCode);

			// 상품코드 길이 제한
			String tempOrdPcode = (from.getOrdPcode() == null) ? "" : from.getOrdPcode();
			from.setOrdPcode((tempOrdPcode.length() > 100) ? tempOrdPcode.substring(0,100) : tempOrdPcode);

			// 상품 주문 수량 체크
			String tempOrdQty = (from.getOrdQty() == null) ? "0" : from.getOrdQty();
			from.setOrdQty((Integer.parseInt(tempOrdQty) < 0) ? "1" : tempOrdQty);

			// 상품명 길이 제한
			String tempPnm = (from.getPnm() == null) ? "" : from.getPnm();
			tempPnm = (tempPnm.length() > 150) ? tempPnm.substring(0,149) : tempPnm;
			tempPnm = (tempPnm.contains("?")) ? "" : tempPnm;
			from.setPnm(tempPnm);

			// api에서 ymdhms를 세팅해줄경우 sendDate 변경
			from.setSendDate((from.isUseYmdhms()) ? from.getYmdhms() : from.getSendDate());

			// yyyymmdd 비어있을경우 sendDate 로 체워넣음
			String tempYyyymmdd = DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(from.getSendDate()));
			from.setYyyymmdd(StringUtils.isBlank(from.getYyyymmdd()) ? tempYyyymmdd : from.getYyyymmdd());

			// 사용자 정보 세팅
			from.setOsCode((!StringUtils.isNumeric(from.getOsCode())) ?
					G.convertOS_TP_CODE(from.getOsCode()) : from.getOsCode());
			from.setBrowserCode((!StringUtils.isNumeric(from.getBrowserCode())) ?
					G.convertBROWSER_TP_CODE(from.getBrowserCode()) : from.getBrowserCode());
			from.setDeviceCode((!StringUtils.isNumeric(from.getDeviceCode())) ?
					G.convertDEVICE_TP_CODE(from.getDeviceCode()) : from.getDeviceCode());
			from.setBrowserCodeVersion((!StringUtils.isNumeric(from.getBrowserCodeVersion())) ?
					G.convertBROWSER_VERSION(from.getBrowserCodeVersion()) : from.getBrowserCodeVersion());

			// 플랫폼 코드 형식 변경
			/*if (!StringUtils.isBlank(from.getPlatform())) {
				from.setPlatform(from.getPlatform().substring(0, 1).toUpperCase());
			}*/

			// 소셜의 경우 별도로 데이터 세팅
			/*if ("1999".equals(from.getScriptNo())
					|| ("Y".equals(from.getSocialYn())
						&& ("19872".equals(from.getScriptNo()) || "22370".equals(from.getScriptNo())))) {
				result = convertShoppulData(from);
			}*/

			logger.debug("PollingData to ConversionVo convert succ");
		} catch (Exception e) {
			result = false;
			logger.error("PollingData to ConversionVo convert err - {} / object - {}", e, from);
		}

		return result;
	}

	/**
	 * 쇼플에 대한 전환 데이터 강제셋 메소드
	 */
	protected static boolean convertShoppulData(ConversionVo from) {
		boolean result = true;

		try {
			// 노출 여부 관계 없이 강제 전환
			from.setConversionDirect(true);

			// 주문번호 가져오기
			from.setOrdCode(from.getOrdCode().split(" ")[0]);

			if ("19872".equals(from.getScriptNo()) || "22370".equals(from.getScriptNo())) {
				from.setInDirectChargeValue(true);	// social 지면은 간접으로 처리
				from.setScriptUserId("social");
			} else {
				from.setSessionChargeValue(true);	// shoppul 은 세션으로 처리
			}

			from.setCnvrsTpCode("");
			from.setOrdPcode("1");
			from.setScriptUserId((StringUtils.isBlank(from.getScriptUserId())) ? "shoppul" : from.getScriptUserId());
			from.setProduct((StringUtils.isBlank(from.getProduct())) ? "b" : from.getProduct());
			from.setNoExposureYN("N");
			from.setLogDttm(DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(from.getSendDate())));
		} catch (Exception e) {
			result = false;
			logger.error("PollingData to ConversionVo convertShoppulData err - {} / object - {}", e, from);
		}

		return result;
	}

	/* 모비온 전환 프리퀀시별 데이터 세팅 메소드 - START */
	/**
	 * MOBON - 브라우저 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setBrowserChargeValueVerMobon(boolean isAvailalbe) {
		this.setSesionSelng2Yn((isAvailalbe) ? "Y" : "N");
	}

	/**
	 * MOBON - 세션 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setSessionChargeValueVerMobon(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieDirect());	// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "11" : "81");
		this.setSesionSelngYn((isAvailalbe) ? "Y" : "N");
		this.setDirectYn("Y");
	}

	/**
	 * MOBON - 직접 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setDirectChargeValueVerMobon(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieDirect());	// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "12" : "82");
		this.setSesionSelngYn("N");
		this.setDirectYn((isAvailalbe) ? "Y" : "N");
	}

	/**
	 * MOBON - 간접 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setInDirectChargeValueVerMobon(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieInDirect() * 24);	// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "13" : "83");
		// this.setSesionSelngYn((isAvailalbe) ? "N" : "Y");
		this.setSesionSelngYn("N");
		this.setDirectYn("N");
	}

	/**
	 * MOBON - 무효 매출 필드 셋
	 */
	public void setInvalidChargeValueVerMobon() {
		this.setServiceAndCharge("84");
	}
	/* 모비온 전환 프리퀀시별 데이터 세팅 메소드 - END */

	/* 브랜딩 전환 프리퀀시별 데이터 세팅 메소드 - START */
	/**
	 * Branding - 브라우저 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setBrowserChargeValue(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieDirect());			// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "11" : "81");
		this.setDirectYn("Y");
		this.setSesionSelngYn("Y");
		this.setSesionSelng2Yn((isAvailalbe) ? "Y" : "N");
	}

	/**
	 * Branding - 세션 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setSessionChargeValue(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieDirect());			// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "11" : "81");
		this.setDirectYn("Y");
		this.setSesionSelngYn((isAvailalbe) ? "Y" : "N");
		this.setSesionSelng2Yn("N");
	}

	/**
	 * Branding - 직접 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setDirectChargeValue(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieDirect());	// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "12" : "82");
		this.setDirectYn((isAvailalbe) ? "Y" : "N");
		this.setSesionSelngYn("N");
		this.setSesionSelng2Yn("N");
	}

	/**
	 * Branding - 간접 매출 필드 셋
	 * @param isAvailalbe : true-유효 전환, false-무효 전환
	 */
	public void setInDirectChargeValue(boolean isAvailalbe) {
		this.setIntervalHour(this.getCookieInDirect() * 24);	// 서치 시간 세팅
		this.setServiceAndCharge((isAvailalbe) ? "13" : "83");
		this.setDirectYn("N");
		this.setSesionSelngYn("N");
		this.setSesionSelng2Yn("N");
	}

	/**
	 * Branding - 무효 매출 필드 셋
	 */
	public void setInvalidChargeValue() {
		this.setServiceAndCharge("84");
	}
	/* 브랜딩 전환 프리퀀시별 데이터 세팅 메소드 - END */

	/**
	 * 강제 전환 여부 판단해서 서비스 코드 및 매출 코드 세트
	 *
	 * <pre>
	 * 강제O : svcTpCode = '', chrgTpCode = ''
	 * 강제X : svcTpCode = value, chrgTpCode = value
	 * </pre>
	 */
	public void setServiceAndCharge(String chrgTpCode) {
		this.setSvcTpCode((this.isConversionDirect()) ? "" : "13");
		this.setChrgTpCode((this.isConversionDirect()) ? "" : chrgTpCode);
	}

	/**
	 * 객체 고유갑 생성 메소드
	 */
	public String getKey() {
		return String.format("%s_%s_%s_%s_%s",
				this.getYyyymmdd(), this.getAdvertiserId(), this.getOrdCode(), this.getIp(), this.getAuId());
	}

	/**
	 * 데이터 누적처리 메소드
	 */
	public void sumGethering(Object _from) {
		ConversionVo from = (ConversionVo) _from;

		this.setOrdQty(Integer.toString(Integer.parseInt(this.getOrdQty()) + Integer.parseInt(from.getOrdQty())));
		this.setPrice(Integer.toString(Integer.parseInt(this.getPrice()) + Integer.parseInt(from.getPrice())));
	}

	/**
	 * PollingData -> 해당 객체 생성 메소드
	 * (변수 타입이 안맞는 경우에만 해당 메소드에서 별도 로직으로 set 을 하고 그 이외에는 convert 에서 다 맞춰서 넘어온다)
	 */
	public static ConversionVo fromPollingData(PollingData from) {
		ConversionVo result= new ConversionVo();

		try {
			result.setDumpType(from.getDumpType());
			result.setClassName(from.getClassName());
			result.setSendDate(from.getSendDate());
			result.setYyyymmdd(from.getYyyymmdd());
			result.setHh(from.getHh());
			result.setAuId(from.getAuId());
			result.setIp(from.getIp());
			result.setPlatform(from.getPlatform());
			result.setProduct(from.getType());		// 전환은 product가 type 으로 넘어옴
			result.setAdGubun(from.getAdGubun());
			result.setAdvertiserId(from.getAdvertiserId());
			result.setSiteCode(from.getSiteCode());
			result.setKpiNo(from.getKpiNo());
			result.setScriptUserId(from.getScriptUserId());
			result.setScriptNo(from.getMediaCode());
			result.setScriptHirnkNo(from.getScriptHirnkNo());
			result.setSvcTpCode(from.getSvcTpCode());
			result.setChrgTpCode(from.getChrgTpCode());
			result.setNoExposureYN(from.getNoExposureYN().startsWith("true") ? "Y" : "N");
			result.setOsCode(from.getOsCode());
			result.setBrowserCode(from.getBrowserCode());
			result.setDeviceCode(from.getDeviceCode());
			result.setBrowserCodeVersion(from.getBrowserCodeVersion());
			result.setTargetYn(G.checkTargetYN(from.getAdGubun()));
			result.setCookieDirect(from.getCookieDirect());
			result.setCookieInDirect(Math.min(from.getCookieInDirect(), 30));	// 간접 매출 상한선 (최대 30일 이내)
			result.setPnm(from.getPnm());
			result.setPrice(from.getPrice());
			result.setYmdhms(from.getYmdhms());
			result.setOrdQty(from.getOrdQty());
			result.setOrdCode(from.getOrdCode());
			result.setOrdPcode(from.getOrdPcode());
			result.setSocialYn(from.getSocialYn());
			result.setTrkTpCode(StringUtils.isBlank(from.getTrkTpCode()) ? "90" : from.getTrkTpCode());
			result.setInflowRoute(from.getInflowRoute());
			result.setCnvrsTpCode(from.getCnvrsTpCode());
			result.setSesionSelngYn(("1".equals(from.getDirect())) ? "Y" : "N");
			result.setRegUserId(from.getRegUserId());
			result.setIpInfoList(from.getIpInfoList());
			result.setCrossLoginIp(from.getCrossLoginIp());
			result.setUseYmdhms(from.isUseYmdhms());

			logger.debug("ConversionVo fromPollingData succ");
		} catch (Exception e) {
			result = null;
			logger.error("ConversionVo fromPollingData err - {} / object - {}", e, from);
		}

		return result;
	}

	/**
	 * 생성자를 사용해서 객체 깊은 복사하는 메소드
	 */
	public ConversionVo copy() {
		return new ConversionVo(this);
	}

	/**
	 * 갹채 갚은 복사 생성자 (필드 추가되면 여기도 추가 필요, 밖에서 사용못하게 private)
	 */
	protected ConversionVo(ConversionVo obj) {
		super(obj);
		this.cookieDirect = obj.cookieDirect;
		this.cookieInDirect = obj.cookieInDirect;
		this.convDelayTimeMinute = obj.convDelayTimeMinute;
		this.pnm = obj.pnm;
		this.price = obj.price;
		this.ymdhms = obj.ymdhms;
		this.ordQty = obj.ordQty;
		this.ordCode = obj.ordCode;
		this.ordPcode = obj.ordPcode;
		this.logDttm = obj.logDttm;
		this.socialYn = obj.socialYn;
		this.trkTpCode = obj.trkTpCode;
		this.inflowRoute = obj.inflowRoute;
		this.cnvrsTpCode = obj.cnvrsTpCode;
		this.directYn = obj.directYn;
		this.sesionSelngYn = obj.sesionSelngYn;
		this.sesionSelng2Yn = obj.sesionSelng2Yn;
		this.regUserId = obj.regUserId;
		this.ipInfoList = obj.ipInfoList;
		this.duplicatedOrderNo = obj.duplicatedOrderNo;
		this.crossLoginIp = obj.crossLoginIp;
		this.useYmdhms = obj.useYmdhms;
		this.conversionDirect = obj.conversionDirect;
	}

	/**
	 * Default Constructor (밖에서 사용못하게 protected)
	 */
	protected ConversionVo() {
		super();
	}

	/**
	 * Getter, Setter
	 */
	public int getIntervalHour() {
		return intervalHour;
	}

	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
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

	public int getConvDelayTimeMinute() {
		return convDelayTimeMinute;
	}

	public void setConvDelayTimeMinute(int convDelayTimeMinute) {
		this.convDelayTimeMinute = convDelayTimeMinute;
	}

	public String getPnm() {
		return pnm;
	}

	public void setPnm(String pnm) {
		this.pnm = pnm;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getYmdhms() {
		return ymdhms;
	}

	public void setYmdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}

	public String getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}

	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}

	public String getOrdPcode() {
		return ordPcode;
	}

	public void setOrdPcode(String ordPcode) {
		this.ordPcode = ordPcode;
	}

	public String getLogDttm() {
		return logDttm;
	}

	public void setLogDttm(String logDttm) {
		this.logDttm = logDttm;
	}

	public String getSocialYn() {
		return socialYn;
	}

	public void setSocialYn(String socialYn) {
		this.socialYn = socialYn;
	}

	public String getTrkTpCode() {
		return trkTpCode;
	}

	public void setTrkTpCode(String trkTpCode) {
		this.trkTpCode = trkTpCode;
	}

	public String getInflowRoute() {
		return inflowRoute;
	}

	public void setInflowRoute(String inflowRoute) {
		this.inflowRoute = inflowRoute;
	}

	public String getCnvrsTpCode() {
		return cnvrsTpCode;
	}

	public void setCnvrsTpCode(String cnvrsTpCode) {
		this.cnvrsTpCode = cnvrsTpCode;
	}

	public String getDirectYn() {
		return directYn;
	}

	public void setDirectYn(String directYn) {
		this.directYn = directYn;
	}

	public String getSesionSelngYn() {
		return sesionSelngYn;
	}

	public void setSesionSelngYn(String sesionSelngYn) {
		this.sesionSelngYn = sesionSelngYn;
	}

	public String getSesionSelng2Yn() {
		return sesionSelng2Yn;
	}

	public void setSesionSelng2Yn(String sesionSelng2Yn) {
		this.sesionSelng2Yn = sesionSelng2Yn;
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

	public String getDuplicatedOrderNo() {
		return duplicatedOrderNo;
	}

	public void setDuplicatedOrderNo(String duplicatedOrderNo) {
		this.duplicatedOrderNo = duplicatedOrderNo;
	}

	public List<String> getCrossLoginIp() {
		return crossLoginIp;
	}

	public void setCrossLoginIp(List<String> crossLoginIp) {
		this.crossLoginIp = crossLoginIp;
	}

	public boolean isUseYmdhms() {
		return useYmdhms;
	}

	public void setUseYmdhms(boolean useYmdhms) {
		this.useYmdhms = useYmdhms;
	}

	public boolean isConversionDirect() {
		return conversionDirect;
	}

	public void setConversionDirect(boolean conversionDirect) {
		this.conversionDirect = conversionDirect;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
