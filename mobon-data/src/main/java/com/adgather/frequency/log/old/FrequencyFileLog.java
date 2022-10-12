package com.adgather.frequency.log.old;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.util.old.FileLog;
import com.google.common.base.Joiner;
import com.mobon.billing.model.v15.BaseCVData;

public class FrequencyFileLog extends FileLog {
	// STATIC //////////////////////////////////////////////////////
	private static FrequencyFileLog instance;
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	public static final String VIEW = "view";
	public static final String CLICK = "click";
	public static final String CONV = "conv";

	private static FrequencyFileLog getInstance() {
		if (instance == null) {
			instance = new FrequencyFileLog();
		}
		return instance;
	}

	private static void log(String log) {
		try {
			getInstance().appendLn(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(AdChargeData data, String action) {
		FrequencyFileLog._timeLog(data.getMedia_code(), data.getSite_code(), data.getPlatform(), CONV.equals(action) ? data.getType() : data.getProduct(), data.getAdGubun(), data.getFreqLog(), action, 1, null, String.valueOf(data.getPoint()), null, null, data.getIp(), data.getGender(), data.getAge(), data.getServiceHostId(), data.getAbtest());
	}

	public static void log(AdChargeData data, String action, String convType, String ordPrice) {
		FrequencyFileLog._timeLog(data.getMedia_code(), data.getSite_code(), data.getPlatform(), CONV.equals(action) ? data.getType() : data.getProduct(), data.getAdGubun(), data.getFreqLog(), action, 1, null, String.valueOf(data.getPoint()), convType, ordPrice, data.getIp(), data.getGender(), data.getAge(), data.getServiceHostId(), data.getAbtest());
	}

	public static void log(DrcData data, String action) {
		FrequencyFileLog._timeLog(data.getS(), data.getSc(), data.getPlatform(), data.getProduct(), data.getGb(), data.getFreqLog(), action, 1, null, String.valueOf(data.getPoint()), null, null, data.getKeyIp(), data.getGender(), data.getAge(), data.getServiceHostId(), data.getAbtests());
	}

	/**
	 * @param ms
	 *            : 구좌번호
	 * @param siteCode
	 *            : 캠패인 코드
	 * @param platform
	 *            : w/m
	 * @param product
	 *            : b/i/s
	 * @param adGubun
	 *            :
	 * @param freq
	 *            :
	 * @param action
	 *            : view/click/conv
	 * @param cnt
	 *            :
	 * @param freqMode
	 *            : first
	 * @param point
	 *            : 광고비용
	 * @param convType
	 *            : r(Real)/d(Direct)/i(Indirect)
	 * @param ordPrice
	 *            : 전환의 상품 구매 비용
	 */
	private static void _timeLog(String ms // 매체스크립트 코드
			, String siteCode // 캠페인 코드
			, String platform // 웹 모바일
			, String product // 일반베너/아이커버/브랜드링크
			, String adGubun // 광고 구분
			, int freq // 노출프리퀀시 횟수
			, String action // 노출/클릭/전환 구분
			, int cnt // 갯수
			, String freqMode // 프리퀀시 적용 알고리즘유형(프리퀀시RTB 유형)
			, String point // 포인트
			, String convType // 전환 유형
			, String ordPrice // 전환 구매가격
			, String uuid // 단말고유ID
			, String gender // 성별
			, String age // 나이
			, String serviceHostId // 노출광고 ID
			, String abtypes) { // AB테스트 타입셋
		_timeLog(ms, siteCode, platform, product, adGubun, freq == 0 ? "" : String.valueOf(freq), action, cnt == 0 ? "" : String.valueOf(cnt), freqMode, point, convType, ordPrice, uuid, gender, age, serviceHostId, abtypes);
	}

	private static void _timeLog(String ms // 매체스크립트 코드
			, String siteCode // 캠페인 코드
			, String platform // 웹 모바일(w/m)
			, String product // 일반베너/아이커버/브랜드링크(b/i/s)
			, String adGubun // 광고 구분
			, String freq // 노출프리퀀시 횟수
			, String action // 노출/클릭/전환 구분
			, String cnt // 갯수
			, String freqMode // 프리퀀시 적용 알고리즘유형(프리퀀시RTB 유형)
			, String point // 포인트
			, String convType // 전환 유형
			, String ordPrice // 전환 구매가격
			, String uuid // 단말고유ID
			, String gender // 성별
			, String age // 나이
			, String serviceHostId // 노출광고 ID
			, String abtypes) {
		try {
			if (freqMode == null)
				freqMode = "";
			if (point == null)
				point = "";
			if (convType == null)
				convType = "";
			if (ordPrice == null)
				ordPrice = "";

			if ("conv".equals(action)) {
				if ("1".equals(convType)) {
					convType = "real";
				} else if ("24".equals(convType)) {
					convType = "direct";
				} else {
					convType = "indirect";
				}
			}
			if (uuid == null)
				uuid = "";
			if (gender == null)
				gender = "";
			if (age == null)
				age = "";
			if (serviceHostId == null)
				serviceHostId = "";
			if (abtypes == null)
				abtypes = "";

			if (StringUtils.isEmpty(platform))
				return;

			platform = platform.toUpperCase();

			String strLog = Joiner.on("\t").useForNull("").join(SDF.format(new Date()), ms, siteCode, platform, product, adGubun, freq, action, cnt, freqMode, point, convType, ordPrice, uuid, gender, age, serviceHostId, abtypes);
			log(strLog);
		} catch (Exception e) {
		}
	}

	// INNER //////////////////////////////////////////////////////
	public static final String PREFIX = "collect_freq_";
	public static final String EXTENSION = ".log";
	public static final boolean DAY_RENAME = true;

	public FrequencyFileLog() {
		super("/home/dreamsearch/logs/collect/frequency", PREFIX, EXTENSION, DAY_RENAME);
	}
}
