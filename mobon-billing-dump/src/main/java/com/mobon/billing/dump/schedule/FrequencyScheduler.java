package com.mobon.billing.dump.schedule;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.service.CommonService;
import com.mobon.billing.dump.service.DumpFileService;
import com.mobon.billing.dump.service.DumpSaveService;
import com.mobon.billing.dump.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @FileName : FrequencyScheduler.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 26.
 * @Author dkchoi
 * @Comment : Frequency DUMP 관련 스케쥴러.
 */
@Slf4j
@Component
public class FrequencyScheduler {
	
	@Resource(name="freqSaveService")
	private DumpSaveService freqSaveService;
	
	@Resource(name="freqFileService")
	private DumpFileService freqFileService;

	@Resource(name="commonService")
	private CommonService commonService;

	private static boolean isIdle= true;
	/**
	 * @Method Name : FrequencyDataInsertScheduler
	 * @Date : 2021. 3. 30.
	 * @Author : dkchoi
	 * @Comment : Frequency 파일을 읽고 DB에 등록하는 스케쥴러.
	 */
	@Async
	@Scheduled(cron = "00 50 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	public void FrequencyDataInsertScheduler() {

		// SDK 지면 구분을 위한 지면 리스트 조회
		GlobalConstants.FREQSDKDAYSTATS_SDK_MS_NO = commonService.selectSdkMediaScriptList();

		if(isIdle) {
			isIdle= false;
				
			String AnHourAgo = DateUtils.getAnHourAgo(-1);
			
			log.info("@@@@ Frequency File Process Start @@@@@");
			log.info("@@@ Frequency File Read Start");
			
			Map<String, Object> totResultData = new HashMap<String, Object>();
			Collection<Future<Object>> futures = new ArrayList<Future<Object>>();
			
			/* 파일 읽기 */
			try {
				totResultData = freqFileService.FileReadProcess(AnHourAgo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info("@@@@ Frequency File Read End");
			log.info("@@@ Save Frequency Data Start");
			/* 읽은 데이터 저장 */
			for(Map.Entry<String, Object> resultData : totResultData.entrySet()) {
				futures.add(freqSaveService.SaveDumpData(totResultData , resultData.getKey()));
			}

			/* 처리 결과 실패 시 재처리용 파일 작성 */
			freqFileService.makeRetryFile(futures);
			
			log.info("@@@ Save Frequency Data End");
			log.info("@@@@@ Frequency File Process End @@@@@");
			
			isIdle= true;
		}
	}


	/**
	 * @Method Name : FrequencyDataInsertRetryScheduler
	 * @Date : 2021. 04. 07.
	 * @Author : dkchoi
	 * @Comment : Frequency 재처리 파일을 읽고 DB에 등록하는 스케쥴러.
	 */
	@Async
	@Scheduled(cron = "00 15 * * * *") // 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7) 0,7은 일요일
	public void FrequencyDataInsertRetryScheduler() {


		log.info("!!!! Frequency Retry File Process Start !!!!");


		log.info("!!!! Frequency Retry File Read Start");

		Map<String, Object> totResultData = new HashMap<String, Object>();
		Collection<Future<Object>> futures = new ArrayList<Future<Object>>();

		/* 파일 읽기 */
		try {
			totResultData = freqFileService.RetryFileReadProcess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("!!!! Frequency Retry File Read End");


		log.info("!!! Retry Save Frequency Data Start");

		if(totResultData != null) {
			/* 읽은 데이터 저장 */
			for(Map.Entry<String, Object> resultData : totResultData.entrySet()) {
				futures.add(freqSaveService.SaveDumpData(totResultData , resultData.getKey()));
			}
		}


		/* 처리 결과 성공 시 재처리 파일 이동 */
		freqFileService.moveRetryFile(futures);

		log.info("!!! Retry Save Frequency Data End");

		log.info("!!!! Frequency Retry File Process End !!!!");

	}

}
