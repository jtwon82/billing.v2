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
	 *            : ????????????
	 * @param siteCode
	 *            : ????????? ??????
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
	 *            : ????????????
	 * @param convType
	 *            : r(Real)/d(Direct)/i(Indirect)
	 * @param ordPrice
	 *            : ????????? ?????? ?????? ??????
	 */
	private static void _timeLog(String ms // ?????????????????? ??????
			, String siteCode // ????????? ??????
			, String platform // ??? ?????????
			, String product // ????????????/????????????/???????????????
			, String adGubun // ?????? ??????
			, int freq // ?????????????????? ??????
			, String action // ??????/??????/?????? ??????
			, int cnt // ??????
			, String freqMode // ???????????? ?????? ??????????????????(????????????RTB ??????)
			, String point // ?????????
			, String convType // ?????? ??????
			, String ordPrice // ?????? ????????????
			, String uuid // ????????????ID
			, String gender // ??????
			, String age // ??????
			, String serviceHostId // ???????????? ID
			, String abtypes) { // AB????????? ?????????
		_timeLog(ms, siteCode, platform, product, adGubun, freq == 0 ? "" : String.valueOf(freq), action, cnt == 0 ? "" : String.valueOf(cnt), freqMode, point, convType, ordPrice, uuid, gender, age, serviceHostId, abtypes);
	}

	private static void _timeLog(String ms // ?????????????????? ??????
			, String siteCode // ????????? ??????
			, String platform // ??? ?????????(w/m)
			, String product // ????????????/????????????/???????????????(b/i/s)
			, String adGubun // ?????? ??????
			, String freq // ?????????????????? ??????
			, String action // ??????/??????/?????? ??????
			, String cnt // ??????
			, String freqMode // ???????????? ?????? ??????????????????(????????????RTB ??????)
			, String point // ?????????
			, String convType // ?????? ??????
			, String ordPrice // ?????? ????????????
			, String uuid // ????????????ID
			, String gender // ??????
			, String age // ??????
			, String serviceHostId // ???????????? ID
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
