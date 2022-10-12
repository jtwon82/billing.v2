package com.mobon.billing.dump.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.file.point.PointSummary;
import com.mobon.billing.dump.service.PointDataService;
import com.mobon.billing.dump.service.PointFileService;
import com.mobon.billing.dump.service.PointSaveService;
import com.mobon.billing.dump.service.pointImpl.PointDataSelectServiceImpl;
import com.mobon.billing.dump.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PointScheduler {
	
	  @Resource 
	  private PointFileService pointFileService;
	 
	  @Resource
	  private PointSaveService pointSaveService;
	  
	  @Resource 
	  private PointDataSelectServiceImpl pointDataSelectServiceImpl;
	
	  @Resource
	  private PointDataService pointDataService;
	  
	  @Value("${POINT_LOG_DIR}") 
	  private String POINTLogPath;
	  
	  @Value("${POINT_RETRY_LOG_DIR}")
	  private String POINTRetryLogPath;

	 	
 //	@Async
//	@Scheduled(cron="0 10 * * * *")
	public void PointLogInsertScheduler() throws Exception {
		log.info("#### Point Scheduler Start ####");
		
		PointSummary stats = new PointSummary();
		
		Map<String, PointDataStats> resultData = new HashMap<String, PointDataStats>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH");
		String sysDate = format.format(cal.getTime());
		
		File dir = new File(POINTLogPath);
		
		File file = this.getLogFileData(dir, sysDate);
		
		if (file == null) {
			log.info("#### Point Log File Not Exist #### ");
			return;
		}
		
		resultData = pointFileService.loadFile(file, stats);
		
		
		try {
			pointSaveService.SaveRealTimePointData(resultData);
		} catch (Exception e) {
			log.error("#### Point Data Insert DB Error ####" + e);
			if (FileUtils.makeDir(POINTRetryLogPath)) {
				
				File retryFile = this.getLogFileData(dir, sysDate);
				pointFileService.makeRetryFile(retryFile);
			}
		}
	}
	
	//	@Async 
	//	@Scheduled(cron = "00 30 09 * * *")
	public void RetryPointLogScheduler() throws Exception {
		log.info("#### Point Retry Scheduler Start ####");
		/*재처리 로직 .... 재처리 Path 에 파일이 하나라도 있으면 해당 파일을 읽어서 처리한다.
		 *  처리가 안되면 다시 파일로 떨구고 기존 파일은 삭제한다.*/
		
		Map<String, PointDataStats> resultData = new HashMap<String, PointDataStats>();
		PointSummary stats = new PointSummary();
		
		File dir = new File(POINTRetryLogPath);
		File[] files = dir.listFiles();
		
		if (files.length == 0) {
			log.info("#### No Retry Point Log File ####");
			return;
		}
				
		resultData = pointFileService.RetryLoadFile(files, stats);
	
		try {
			pointSaveService.RetrySavePointData(resultData);
			for (File file : files) {
				if (FileUtils.deleteFile(POINTRetryLogPath, file.getName())) {
					log.info("#### Retry File Delete #### " + file.getName());
				}				
			}
		} catch (Exception e) {
			log.error("#### Retry Point Data Insert DB Error ####" + e);
			
		}
	}
	
	//@Async
	//@Scheduled(cron = "00 00 13 * * *")
	//@Scheduled(cron = "00 */5 * * * *")
	public void CheckPointDataScheduler() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String yesterDay = format.format(cal.getTime());
		int yesterDayToInt =Integer.parseInt(yesterDay);

		log.info("CHECKPOINTDATAScheduler::::" + yesterDayToInt);
		//1. MOB_POINT_DATA : SELECT  WHERE : STATS_DTTM 
		List<PointDataStats> pointLogDataList = pointDataSelectServiceImpl.selectPointLogDataList(yesterDayToInt);
		log.info("PointLogDataList = "+ pointLogDataList.size());
		//2. MOB_CAMP_STATS : SELECT WHERE : STATS_DTTM 
		List<MobCampMediaStats> campStatsDataList = pointDataSelectServiceImpl.selectCampStatsDataList(yesterDayToInt);
		log.info("CampStatsDataList = "+ campStatsDataList.size());
		//3. POINTSERVICE : BIZ(LOGIC)
		try {
			pointDataService.resultDiffPointData(pointLogDataList, campStatsDataList);
		}catch (Exception e) {
			log.error("#### Check Point Data Error ####" + e);
		}
		//4. END 
	}
	
	private File getLogFileData(File dir, String fileName) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (!file.isFile()) {
					continue;
				}
				if (file.getName().contains(fileName)) {					
					log.info("#####Point fileName ##### " + file.getName());					
					return file;
				}
			}
		} catch (Exception e) {
			log.error("#####Point User Id Point Data Error #####" + e);
		}
		return null;
	}
}
