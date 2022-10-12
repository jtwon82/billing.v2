package com.mobon.billing.dump.schedule;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobon.billing.dump.domainmodel.apptrgt.AppTrgtAdverMediaStats;
import com.mobon.billing.dump.file.apptrgt.AppTrgtSummary;
import com.mobon.billing.dump.service.apptrgtimpl.AppTrgtFileProcessImpl;
import com.mobon.billing.dump.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @FileName : AppTrgtScheduler.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 5. 12. 
 * @Author 
 * @Comment : App타겟 DUMP 관련 스케쥴러.
 */
@Slf4j
@Component
public class AppTrgtScheduler {
	
	@Resource
	private AppTrgtFileProcessImpl appTrgtFileReaderImpl;

	@Value("${APPTRGT_LOG_DIR}")
	private String APPTRGTLogPath;
	
	/**
	 * @throws Exception 
	 * @Method Name : 
	 * @Date : 
	 * @Author : 
	 * @Comment : 
	 */
	@Async
	@Scheduled(cron = "0 30 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
//	@Scheduled(cron = "*/3 * * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	public void AppTrgtDataInsertScheduler() throws Exception {
		log.info("##### AppTrgt File Process Start #####");

		Map<String, AppTrgtAdverMediaStats> resultData = new HashMap<String, AppTrgtAdverMediaStats>();
		AppTrgtSummary stats = new AppTrgtSummary();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -50);
		String logFileName = DateUtils.getDate("yyyy-MM-dd_HH", new Date(cal.getTimeInMillis()));
		
		File dir = new File(APPTRGTLogPath);
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if(!file.isFile())
					continue;
				 
				if(file.getName().indexOf("_ing")>-1 || file.getName().indexOf(".zip")>-1) {
					log.info("# continue fileName : " + file.getName());
					continue;
				}
				
				log.info("file.getName() {}", file.getName());
				
				if(file.getName().indexOf( logFileName )>-1) {
					log.info("{}.getName().indexOf( {} )>-1", file.getName(), logFileName);
					
					resultData = appTrgtFileReaderImpl.loadFile(file, stats);
					
				}
				
			}
		} catch(Exception e) {
			
		}


		appTrgtFileReaderImpl.SaveAppTrgtData(resultData);

		log.info("##### AppTrgt File Process End #####");

	}
}
