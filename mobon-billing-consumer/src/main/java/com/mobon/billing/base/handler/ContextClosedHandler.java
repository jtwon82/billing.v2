package com.mobon.billing.base.handler;

import java.io.File;
import java.util.List;
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
//import com.mobon.billing.branch.schedule.TaskAppTargetData;
import com.mobon.billing.branch.schedule.TaskIntgCntrActionData;
import com.mobon.billing.branch.schedule.TaskNearData;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;

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
	private TaskClickViewData taskClickViewData;
	@Autowired
	private TaskClickViewOpenrtbData taskClickViewOpenrtbData;
	@Autowired
	private TaskClickViewPcodeData taskClickViewPcodeData;
	@Autowired
	private TaskClientEnvirmentData taskClientEnvirmentData;
	@Autowired
	private TaskClickViewPointData taskClickViewPointData;
	@Autowired
	private TaskShopInfoData taskShopInfoData;
	@Autowired
	private TaskShopStatsInfoData taskShopStatsInfoData;
	@Autowired
	private TaskExternalData taskExternalData;
	@Autowired
	private TaskActionData taskActionData;
	@Autowired
	private TaskConvData taskConvData;
	@Autowired
	private TaskConvAbusingData taskConvAbusingData;
	@Autowired
	private TaskIntgCntrConvData taskIntgCntrConvData;

	@Autowired
	private TaskNearData taskNearData;
//	@Autowired
//	private TaskAppTargetData taskAppTargetData;
	@Autowired
	private TaskIntgCntrActionData taskIntgCntrActionData;

	@Autowired
	private TaskConvABPcodeRecomData taskConvABPcodeRecomData;

	/////////////////////////////////////////////////
	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	@Qualifier("ClickViewDataWorkQueue")
	private WorkQueueTaskData workQueue;

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
			taskActionData.mongoToMariaActionDataV3();
			taskClickViewData.mongoToMariaClickViewV3();
			taskClickViewOpenrtbData.mongoToMariaClickViewOpenrtbV3();
			taskClickViewPcodeData.mongoToMariaClickViewPcodeV3();
			taskClientEnvirmentData.mongoToMariaClientEnvirmentV3();
			taskClickViewPointData.mongoToMariaClickViewPointV3();
			taskExternalData.mongoToMariaExternalV3();
			taskShopInfoData.mongoToMariaShopInfoDataV3();
			taskShopStatsInfoData.mongoToMariaShopStatsInfoDataV3();
			taskConvData.mongoToMariaConvDataV3();
			taskConvAbusingData.mongoToMariaConvAbusingDataV3();
			
			taskNearData.mongoToMariaNearData();
//			taskAppTargetData.mongoToMariaAppTargetData();
			taskIntgCntrConvData.mongoToMariaIntgCntrConvDataV3();
			taskIntgCntrActionData.mongoToMariaIntgCntrActionDataV3();

			taskConvABPcodeRecomData.mongoToMariaConvABPcodeRecomDataV3();

			// for kafka autocommit
			Thread.sleep(batchCloseDelayMs);

		} catch (Exception e) {
			logger.error("onApplicationEvent err", e);
		}
		
		// 처리중인게 있을경우 대기
		if ("Dev".equals(profileId)) {
			
		}else {
			while (!chkRetryProcess()) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
			
			// for kafka autocommit
			try {
				Thread.sleep(batchCloseDelayMs);
			} catch (InterruptedException e) {
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
	 * 재처리 숫자를 체크 합니다. 1) 파일처리 숫자를 체크 : fileRowCnt 2) queue에 들어 있는 파일 숫자를 체크 함 :
	 * workQueue.getQueueSize(); 3) retry 파일을 체크 한다 : logPath+"retry\" 4) queue에 한건도
	 * 없고 retry 폴더에 한건도 없을 경우 시스템을 나온다 5) 실행중인 Thread를 체크 한다.
	 */
	public boolean chkRetryProcess() {

		boolean result = false;
		// q size
		int qSize = workQueue.getQueueSize();

		// 처리 Thread 수
		int threadCnt = TaskRetryData.getThreadCnt();
		threadCnt = TaskClickViewData.getThreadCnt() + TaskClickViewPcodeData.getThreadCnt() + TaskClientEnvirmentData.getThreadCnt() 
				+ TaskClickViewPointData.getThreadCnt() + TaskConvData.getThreadCnt() + TaskActionData.getThreadCnt()
				+ TaskShopInfoData.getThreadCnt() + TaskShopStatsInfoData.getThreadCnt()
				+ TaskExternalData.getThreadCnt() + TaskConvABPcodeRecomData.getThreadCnt();


		// retry 파일 개수
		File file = new File(logPath + "retry");

		File[] fileArr = file.listFiles();

		///// sumObject check
		int sumObjCnt = 0;
		Map<String, BaseCVData> mapBaseCVData = sumObjectManager.getMapBaseCVData();
		Map<String, BaseCVData> mapClickViewPcodeData = sumObjectManager.getMapClickViewPcodeData();
		Map<String, BaseCVData> mapClientEnvirmentData = sumObjectManager.getMapClientEnvirmentData();
		Map<String, BaseCVData> mapBaseCVPointData = sumObjectManager.getMapBaseCVPointData();
		Map<String, ExternalInfoData> mapExternalInfoData = sumObjectManager.getMapExternalInfoData();
		Map<String, ShopStatsInfoData> mapShopStatsInfoData = sumObjectManager.getMapShopStatsInfoData();
		Map<String, ShopInfoData> mapShopInfoData = sumObjectManager.getMapShopInfoData();
//		Map<String, RTBDrcInfoData> mapRTBDrcInfoData = sumObjectManager.getMapRTBDrcInfoData();
//		Map<String, RTBReportInfoData> mapRTBReportInfoData = sumObjectManager.getMapRTBReportInfoData();
		List<ConvData> listConvData = sumObjectManager.getMapConvData();
		List<ActionLogData> listActionLogData = sumObjectManager.getListActionData();
		List<ConvData> listABPcodeRecomData = sumObjectManager.getListABPcodeConvData();

		sumObjCnt += mapBaseCVData.size() + mapClickViewPcodeData.size() + mapClientEnvirmentData.size() + mapBaseCVPointData.size() + mapExternalInfoData.size() + mapShopStatsInfoData.size()
				+ mapShopInfoData.size() + listConvData.size()
				+ listActionLogData.size() + listABPcodeRecomData.size();

		logger.info("###################################################");
		logger.info("TaskRetryData Exe Thread  Cnt =>{}", TaskRetryData.getThreadCnt());
		logger.info("Exe Thread  Cnt =>{}", threadCnt);
		logger.info("file path : {}", file.getPath());
		logger.info("qSize : {},sumObjCnt : {}", qSize, sumObjCnt);
		logger.info("retry file Cnt : {}", fileArr.length);
		logger.info("###################################################");

		if ((qSize == 0) && (threadCnt == 0) && (sumObjCnt == 0)) {

			// 처리 끝
			result = true;

		}

		return result;
	}

}