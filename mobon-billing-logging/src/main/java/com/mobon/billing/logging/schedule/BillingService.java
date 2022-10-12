package com.mobon.billing.logging.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mobon.billing.logging.config.CacheConfig;
import com.mobon.billing.logging.schedule.dao.BillingDao;
import com.mobon.billing.util.FileUtils;

@Component
public class BillingService {
	private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

	@Value("${cron.path}")
	private String cronPath;

	@Autowired
	private BillingDao billingDao;

	@Autowired
	private CacheConfig cache;

    public List buildFrameMonitor() {
    	List<Map> list = billingDao.selectFrameMonitor();
    	logger.info("buildMediaChrgMonitor list - {}", new Gson().toJson(list));
    	
//    	fileWrite("frameRtbBatchCheck.txt", list);
    	FileUtils.fileWrite(cronPath, "frameRtbBatchCheck.txt", new Gson().toJson(list));
    	
    	return list;
    }
    
    public List buildRebuildConversionMonitor() {
    	List<Map<String, String>> list = billingDao.selectRebuildConversionMonitor();
    	logger.info("buildRebuildConversionMonitor list - {}", new Gson().toJson(list));
    	
//    	fileWrite("rebuildConvCheck.txt", list);
    	FileUtils.fileWrite(cronPath, "rebuildConvCheck.txt", new Gson().toJson(list));
    	
    	return list;
    }
    
	public List chkingBeforeHourData() {
		List list1 = billingDao.chkingBeforeHourData();
		logger.debug("chkingBeforeHourData list - {}", new Gson().toJson(list1));

		FileUtils.fileWrite(cronPath, "chkingBeforeHourData.txt", new Gson().toJson(list1));

		return list1;
	}

	public List buildBatchRunningTime() {
		List list1 = billingDao.selectChkingBatchRuningTime();
		logger.debug("buildBatchRunningTime list - {}", new Gson().toJson(list1));

		FileUtils.fileWrite(cronPath, "batchRunningTime.txt", new Gson().toJson(list1));

		return list1;
	}

	public List buildReportCtr() {
		List list2 = billingDao.selectReportCtr();
		logger.info("buildReportCtr list - {}", new Gson().toJson(list2));

		FileUtils.fileWrite(cronPath, "reportCtr.txt", new Gson().toJson(list2));

		return list2;
	}

	public List buildMediaChrgMonitor() {
		List<Map> list = billingDao.selectMediaChrgMonitor();
		List<Map> listAlarm = new ArrayList();

		for (Map row : list) {
			String DTTM_PAR = String.format("%s_%s", row.get("STATS_DTTM"), row.get("PAR"));
			String ALARM = row.get("ALARM").toString();

			if (!cache.getCacheDayOfpar(DTTM_PAR) && !ALARM.equals("X")) {
				listAlarm.add(row);
			}
		}
		logger.debug("buildMediaChrgMonitor list - {}", new Gson().toJson(listAlarm));

		FileUtils.fileWrite(cronPath, "mediaChagStats.txt", new Gson().toJson(listAlarm)); // listAlarm);

		return listAlarm;
	}

	public List buildUserCtgrMonitor() {
		List<Map> list = billingDao.selectUserCtgrMonitor();
		List<Map> listAlarm = new ArrayList();

		for (Map row : list) {
			String CKEY = String.format("%s_%s", row.get("USER_ID"), row.get("PAR"));
			String ALARM = row.get("ALARM").toString();

			if (!cache.getCache("UserCtgr", CKEY) && !ALARM.equals("")) {
				listAlarm.add(row);
			}
		}

		FileUtils.fileWrite(cronPath, "UserCtgrStats.txt", new Gson().toJson(listAlarm));// listAlarm);

		return listAlarm;
	}

	public List selectChkingZeroViewClickConv() {
		List<Map> list = billingDao.selectChkingZeroViewClickConv();
		List<Map> listAlarm = new ArrayList();

		for (Map row : list) {
			String KEY = String.format("%s_%s", row.get("GG"), row.get("STATS_HH"));
			String CNT = row.get("CNT").toString();

			row.put("KEY", KEY);
			listAlarm.add(row);

		}

		FileUtils.fileWrite(cronPath, "ChkingZeroViewClickConv.txt", new Gson().toJson(listAlarm)); // listAlarm);

		return listAlarm;
	}

	public List<Map<String, String>> buildDailyMonitor() {
		logger.info("buildDailyMonitor START");
		List<Map<String, String>> listAlarm = new ArrayList();

		// ADVER_ID = '' 인 경우
		Map<String, Object> emptyAdverId = billingDao.selectEmptyAdverId();
		if (emptyAdverId != null) {
			Map<String, String> map = new HashMap<>();
			map.put("CHK", (String) emptyAdverId.get("CHK"));
			map.put("MSG", String.format("[%s - %s ROWS]", emptyAdverId.get("TOPIC"), emptyAdverId.get("MSG")));
			listAlarm.add(map);
		}

		// kakao 이면서 ITL_TP_CODE != '03' 인 경우
		Map<String, Object> invalidItlTpCode = billingDao.selectInvalidItlTpCodeForKakao();
		if (invalidItlTpCode != null) {
			Map<String, String> map = new HashMap<>();
			map.put("CHK", (String) invalidItlTpCode.get("CHK"));
			map.put("MSG", String.format("[%s - %s ROWS]", invalidItlTpCode.get("TOPIC"), invalidItlTpCode.get("MSG")));
			listAlarm.add(map);
		}

		// ADVRTS_TP_CODE = '56' 인데 ADVRTS_PRDT_CODE != '09' 인 경우
		Map<String, Object> invalidPerformanceAd = billingDao.selectInvalidPerformanceAd();
		if (invalidPerformanceAd != null) {
			Map<String, String> map = new HashMap<>();
			map.put("CHK", (String) invalidPerformanceAd.get("CHK"));
			map.put("MSG", String.format("[%s - %s ROWS]", invalidPerformanceAd.get("TOPIC"), invalidPerformanceAd.get("MSG")));
			listAlarm.add(map);
		}

		// ITL_TP_CODE = '99' 이면서 view = click = amt = 0 인 경우
		Map<String, Object> invalidEmptyValue = billingDao.selectInvalidEmptyValue();
		if (invalidEmptyValue != null) {
			Map<String, String> map = new HashMap<>();
			map.put("CHK", (String) invalidEmptyValue.get("CHK"));
			map.put("MSG", String.format("[%s - %s ROWS]", invalidEmptyValue.get("TOPIC"), invalidEmptyValue.get("MSG")));
			listAlarm.add(map);
		}

		FileUtils.fileWrite(cronPath, "DailyChk.txt", new Gson().toJson(listAlarm));
		logger.info("buildDailyMonitor END");

		return listAlarm;
	}

}
