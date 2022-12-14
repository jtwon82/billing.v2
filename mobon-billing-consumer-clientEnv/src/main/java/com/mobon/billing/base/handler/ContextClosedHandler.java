package com.mobon.billing.base.handler;

import java.io.File;
import java.util.Map;

import com.mobon.billing.core.schedule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.mobon.billing.base.schedule.TaskRetryData;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.env.schedule.TaskCampMediaRetrnAvalData;
import com.mobon.billing.model.v15.BaseCVData;

@Component
public class ContextClosedHandler
		implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware, BeanPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);

	private static final int TIME_WAIT = 30 * 1000;

	private ApplicationContext context;

	public boolean closed = false;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@Autowired
	private TaskClientEnvirmentData taskClientEnvirmentData;
	@Autowired
	private TaskAdverClientEnvirmentData taskAdverClientEnvirmentData;
	@Autowired
	private TaskClientAgeGenderData taskClientAgeGenderData;
	@Autowired
	private TaskCampMediaRetrnAvalData taskCampMediaRetrnAvalData;
	@Autowired
	private TaskADBlockData taskADBlockData;
	@Autowired
	private TaskCrossAUIDData taskCrossAUIDData;


	/////////////////////////////////////////////////
	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	@Qualifier("AdverClientEnvirmentDataWorkQueue")
	private WorkQueueTaskData adverClientEnvirmentDataWorkQueue;

	@Autowired
	@Qualifier("ClientEnvirmentDataWorkQueue")
	private WorkQueueTaskData clientEnvirmentDataWorkQueue;
	
	@Autowired
	@Qualifier("ClientAgeGenderDataWorkQueue")
	private WorkQueueTaskData clientAgeGenderDataWorkQueue;
	
	@Autowired
	@Qualifier("CampMediaRetrnAvalDataWorkQueue")
	private WorkQueueTaskData campMediaRetrnAvalDataWorkQueue;

	@Autowired
	@Qualifier("ADBlockDataWorkQueue")
	private WorkQueueTaskData ADBlockDataWorkQueue;

	@Autowired
	@Qualifier("CrossAUIDDataWorkQueue")
	private WorkQueueTaskData CrossAUIDDataWorkQueue;

	@Value("${log.path}")
	private String logPath;
	/////////////////////////////////////////////////

	@Value("${batch.close.delay.ms}")
	int batchCloseDelayMs;
	
	@Value("${profile.id}")
	private String	profileId;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {

		try {
			closed = true;

			logger.info("countDown start");

			// for consumer stop
			kafkaListenerEndpointRegistry.stop();

			Thread.sleep(1000);
			
			// for
			taskClientEnvirmentData.mongoToMariaClientEnvirmentV3();
			taskADBlockData.mongoToMariaADBlockV3();
			taskAdverClientEnvirmentData.mongoToMariaAdverClientEnvirmentV3();
			taskClientAgeGenderData.mongoToMariaClientAgeGenderV3();
			taskCampMediaRetrnAvalData.mongoToMariaCampMediaRetrnAvalV3();
			taskCrossAUIDData.mongoToMariaCrossAUIDV3();
			
			// for kafka autocommit
			Thread.sleep(batchCloseDelayMs);

		} catch (Exception e) {
			logger.error("onApplicationEvent err", e);
		}
		
		// ??????????????? ???????????? ??????
		if ("Dev".equals(profileId)) {
			
		}else {
			while (!chkRetryProcess()) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
		}

		logger.info("countDown end");

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public Object postProcessAfterInitialization(Object object, String arg1) throws BeansException {
		return object;
	}

	@Override
	public Object postProcessBeforeInitialization(Object object, String arg1) throws BeansException {
		if (object instanceof ThreadPoolTaskScheduler) {
			((ThreadPoolTaskScheduler) object).setAwaitTerminationSeconds(TIME_WAIT);
			((ThreadPoolTaskScheduler) object).setWaitForTasksToCompleteOnShutdown(true);
		}
		if (object instanceof ThreadPoolTaskExecutor) {
			((ThreadPoolTaskExecutor) object).setWaitForTasksToCompleteOnShutdown(true);
		}
		return object;
	}

	/*
	 * ????????? ????????? ?????? ?????????. 1) ???????????? ????????? ?????? : fileRowCnt 2) queue??? ?????? ?????? ?????? ????????? ?????? ??? :
	 * workQueue.getQueueSize(); 3) retry ????????? ?????? ?????? : logPath+"retry\" 4) queue??? ?????????
	 * ?????? retry ????????? ????????? ?????? ?????? ???????????? ????????? 5) ???????????? Thread??? ?????? ??????.
	 */
	public boolean chkRetryProcess() {

		boolean result = false;
		// q size
		int qSize = adverClientEnvirmentDataWorkQueue.getQueueSize()
				+ clientEnvirmentDataWorkQueue.getQueueSize()
				+ clientAgeGenderDataWorkQueue.getQueueSize()
				+ campMediaRetrnAvalDataWorkQueue.getQueueSize()
				+ ADBlockDataWorkQueue.getQueueSize()
				+ CrossAUIDDataWorkQueue.getQueueSize();


		// ?????? Thread ???
		int threadCnt = TaskRetryData.getThreadCnt();
		threadCnt += TaskClientEnvirmentData.getThreadCnt()
				+ TaskAdverClientEnvirmentData.getThreadCnt()
				+ TaskClientAgeGenderData.getThreadCnt()
				+ TaskCampMediaRetrnAvalData.getThreadCnt()
				+ TaskADBlockData.getThreadCnt()
				+ TaskCrossAUIDData.getThreadCnt();


		// retry ?????? ??????
		File file = new File(logPath + "retry");

		File[] fileArr = file.listFiles();

		///// sumObject check
		int sumObjCnt = 0;
		
		Map<String, BaseCVData> mapClientEnvirmentData = sumObjectManager.getMapClientEnvirmentData();
		Map<String, BaseCVData> mapADBlockData = sumObjectManager.getMapADBlockData();
		Map<String, BaseCVData> mapAdverClientEnvirmentData = sumObjectManager.getMapAdverClientEnvirmentData();
		Map<String, BaseCVData> mapClientAgeGenderData = sumObjectManager.getMapClientAgeGenderData();
		Map<String, BaseCVData> mapCampMediaRetrnAvalData = sumObjectManager.getMapCampMediaRetrnAvalData();
		Map<String, BaseCVData> mapCrossAUIDData = sumObjectManager.getMapCrossAUIDData();

		sumObjCnt += mapClientEnvirmentData.size()
				+ mapAdverClientEnvirmentData.size()
				+ mapClientAgeGenderData.size()
				+ mapCampMediaRetrnAvalData.size()
				+ mapADBlockData.size()
				+ mapCrossAUIDData.size();

		logger.info("###################################################");
		logger.info("TaskRetryData Exe Thread  Cnt =>{}", TaskRetryData.getThreadCnt());
		logger.info("Exe Thread  Cnt =>{}", threadCnt);
		logger.info("file path : {}", file.getPath());
		logger.info("qSize : {},sumObjCnt : {}", qSize, sumObjCnt);
		logger.info("retry file Cnt : {}", fileArr.length);
		logger.info("###################################################");

		if ((qSize == 0) && (threadCnt == 0) && (sumObjCnt == 0)) {

			// ?????? ???
			result = true;

		}

		return result;
	}

}