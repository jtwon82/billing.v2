package com.mobon.billing.dump.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import com.mobon.billing.dump.service.CommonService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.service.DumpFileService;
import com.mobon.billing.dump.service.DumpSaveService;
import com.mobon.billing.dump.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @FileName : ABTestScheduler.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : ABTEST DUMP 관련 스케쥴러.
 */
@Slf4j
@Component
public class ABTestScheduler {
	
	@Resource(name="abTestSaveService")
	private DumpSaveService abTestSaveService;
	
	@Resource(name="abTestFileService")
	private DumpFileService abTestFileService;

	@Resource(name="commonService")
	private CommonService commonService;

	private static boolean isIdle= true;
	/**
	 * @Method Name : ABTestDataInsertScheduler
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : ABTEST파일을 읽고 DB에 등록하는 스케쥴러.
	 */
	@Async
	@Scheduled(cron = "00 40 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	//@Scheduled(cron = "00 56 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	public void ABTestDataInsertScheduler() {

		/* 프레임 AB 테스트 예외지면 수집 */
		GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO = commonService.selectFrmeMediaInfoList();
		log.info("# Reload GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO.size():{}", GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO.size());


		if(isIdle) {
			isIdle= false;
				
			String AnHourAgo = DateUtils.getAnHourAgo(-1);
			
			log.info("##### ABTEST File Process Start #####");
			log.info("### ABTEST File Read Start");
			
			Map<String, Object> totResultData = new HashMap<String, Object>();
			Collection<Future<Object>> futures = new ArrayList<Future<Object>>();
			
			/* 파일 읽기 */
			try {
				totResultData = abTestFileService.FileReadProcess(AnHourAgo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("#### ABTEST File Read End");
			log.info("### Save ABTEST Data Start");
			/* 읽은 데이터 저장 */
			for(Map.Entry<String, Object> resultData : totResultData.entrySet()) {
				futures.add(abTestSaveService.SaveDumpData(totResultData , resultData.getKey()));
			}
			 
			/* 처리 결과 실패 시 재처리용 파일 작성 */
			abTestFileService.makeRetryFile(futures);
			
			log.info("### Save ABTEST Data End");
			log.info("##### ABTEST File Process End #####");
			
			isIdle= true;
		}
	}
	
	
	/**
	 * @Method Name : ABTestDataInsertRetryScheduler
	 * @Date : 2020. 08. 24.
	 * @Author : dkchoi
	 * @Comment : ABTEST 재처리 파일을 읽고 DB에 등록하는 스케쥴러.
	 */
	@Async
	@Scheduled(cron = "00 10 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	public void ABTestDataInsertRetryScheduler() {
	      
		
		log.info("$$$$ ABTEST Retry File Process Start $$$$");
		
		
		log.info("$$$$ ABTEST Retry File Read Start");
		
		Map<String, Object> totResultData = new HashMap<String, Object>();
		Collection<Future<Object>> futures = new ArrayList<Future<Object>>();
		
		/* 파일 읽기 */
		try {
			totResultData = abTestFileService.RetryFileReadProcess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("$$$$ ABTEST Retry File Read End");
		
		
		log.info("$$$ Retry Save ABTEST Data Start");
		
		if(totResultData != null) {
			/* 읽은 데이터 저장 */
			for(Map.Entry<String, Object> resultData : totResultData.entrySet()) {
				futures.add(abTestSaveService.SaveDumpData(totResultData , resultData.getKey()));
			}
		}
			
		
		/* 처리 결과 성공 시 재처리 파일 이동 */
		abTestFileService.moveRetryFile(futures);

		log.info("$$$ Retry Save ABTEST Data End");
		
		log.info("$$$$ ABTEST Retry File Process End $$$$");
	      
	}
	
	
	
	/**
	 * @Method Name : ABTestRetrySuccFileRemoveScheduler
	 * @Date : 2020. 08. 24.
	 * @Author : dkchoi
	 * @Comment : 재처리 성공 파일 제거 스케쥴러.
	 */
    @Async
    @Scheduled(cron = "0 0 05 * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
    public void ABTestRetrySuccFileRemoveScheduler(){
    	abTestFileService.removeRetryFile(DateUtils.getDate(-10)); // 보관기간
        log.info("# Remove Old RetrySucc Files #");
    }
    
}
