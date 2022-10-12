package com.openrtb.schedule;

import com.openrtb.config.OpenRtbConfig;
import com.openrtb.service.CorrectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class CorrectionScheduledTasks {
	@Autowired
	private OpenRtbConfig config;

	@Autowired
	private CorrectionService correctionService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@Scheduled(cron = "${cron.TestSchedule}")
	public void TestSchedule() {

		log.info("test {}", config);

		correctionService.dbConnTest();
	}

	@Scheduled(cron = "${cron.correctionDailySchedule}")
	public void correctionDailySchedule() {
		correctionService.dbConnTest();

		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.add(Calendar.DATE, -1); // 전일 날짤 구함
		String beforeDate = sdf.format(cal.getTime());

		try {
			for (String itlTpCode : config.getItlTpCodeList()) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("date", beforeDate);
				param.put("itl_tp_code", itlTpCode);

				// 시간대 작업
				correctionService.checkTimeData(param);

				// 일자별 작업
				correctionService.checkDailyData(param);
			}
		} catch (Exception e) {
			log.error("Daily 작업 Error", e);
		}
	}

	@Scheduled(cron = "${cron.correctionManuallySchedule}")
	public void correctionManuallySchedule() {
		correctionService.dbConnTest();

		File file = new File(config.getManuallyFilePath());
		FileInputStream fis = null;

		if (file.exists()) {
			log.info("Manually Check File 작업시작!!");

			Properties properties = new Properties();
			try {
				fis = new FileInputStream(file);
				properties.load(new BufferedInputStream(fis));

				// 시간대별 작업 체크
				if (properties.getProperty("checkTimeDayList") != null
						&& !"".equals(properties.getProperty("checkTimeDayList"))) {
					log.info(properties.getProperty("checkTimeDayList"));
					String[] checkTimeDayList = properties.getProperty("checkTimeDayList").split(",");
					for (String checkTimeDay : checkTimeDayList) {
						Map<String, String> param = new HashMap<String, String>();
						param.put("date", checkTimeDay.split("_")[0]);
						param.put("itl_tp_code", checkTimeDay.split("_")[1]);

						// 시간대 작업
						correctionService.checkTimeData(param);
					}
				}

				// 일자별 작업 체크
				if (properties.getProperty("checkDayList") != null
						&& !"".equals(properties.getProperty("checkDayList"))) {
					log.info(properties.getProperty("checkDayList"));
					String[] checkDayList = properties.getProperty("checkDayList").split(",");

					for (String checkDay : checkDayList) {
						Map<String, String> param = new HashMap<String, String>();
						param.put("date", checkDay.split("_")[0]);
						param.put("itl_tp_code", checkDay.split("_")[1]);

						// 일자별 작업
						correctionService.checkDailyData(param);
					}
				}
			} catch (IOException e) {
				log.error("Manually 파일 작업 에러", e);
			} finally {
				if (fis != null) {
					try {
						fis.close(); // 파일 닫음
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (file != null) {
					file.delete(); // 작업 완료한 파일은 삭제
				}
			}
		} else {
			log.info("Manually Check File None!!");
		}
	}
}
