package com.mobon.billing.branch.handler;

import java.io.File;

import com.mobon.billing.branch.schedule.*;
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

//import com.mobon.billing.branch.schedule.TaskAppTargetData;
import com.mobon.billing.core.service.WorkQueueTaskData;

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
	private TaskNearData taskNearData;

//	@Autowired
//	private TaskAppTargetData taskAppTargetData;

	@Autowired
	private TaskIntgCntrData taskIntgCntrData;

	@Autowired
	private TaskIntgCntrKgrData taskIntgCntrKgrData;

	@Autowired
	private TaskMediaChrgData taskMediaChrgData;

	@Autowired
	private TaskIntgCntrTtimeData taskIntgCntrTtimeData;

	@Autowired
	private TaskActionData taskActionData;

	@Autowired
	private TaskActionPcodeData taskActionPcodeData;

	@Autowired
	private TaskClickViewPointData taskClickViewPointData;

	@Autowired
	private TaskClickViewPoint2Data taskClickViewPoint2Data;

	@Autowired
	private TaskIntgCntrActionData taskIntgCntrActionData;
	
	@Autowired
	private TaskChrgLogData taskChrgLogData;
	
	@Autowired
	private TaskPluscallLogData taskPluscallLogData;

	@Autowired
	private TaskActionABPcodeData taskActionABPcodeData;
	@Autowired
	private TaskBasketData taskBasketData;
	
//	@Autowired
//	private SumObjectManager sumObjectManager;

	@Autowired
	@Qualifier("NearDataWorkQueue")
	private WorkQueueTaskData nearDataWorkQueue;
//	@Autowired
//	@Qualifier("AppTargetDataWorkQueue")
//	private WorkQueueTaskData appTargetDataWorkQueue;
	@Autowired
	@Qualifier("IntgCntrDataWorkQueue")
	private WorkQueueTaskData intgCntrDataWorkQueue;
	@Autowired
	@Qualifier("IntgCntrKgrDataWorkQueue")
	private WorkQueueTaskData intgCntrKgrDataWorkQueue;
	@Autowired
	@Qualifier("MediaChrgDataWorkQueue")
	private WorkQueueTaskData mediaChargeworkQueue;
	@Autowired
	@Qualifier("IntgCntrTtimeDataWorkQueue")
	private WorkQueueTaskData intgCntrTtimeDataWorkQueue;
	@Autowired
	@Qualifier("ActionDataWorkQueue")
	private WorkQueueTaskData actionDataWorkQueue;
	@Autowired
	@Qualifier("ActionPcodeDataWorkQueue")
	private WorkQueueTaskData actionPcodeDataWorkQueue;
	@Autowired
	@Qualifier("ClickViewPointDataWorkQueue")
	private WorkQueueTaskData clickViewPointDataWorkQueue;
	@Autowired
	@Qualifier("ClickViewPoint2DataWorkQueue")
	private WorkQueueTaskData clickViewPoint2DataWorkQueue;
	@Autowired
	@Qualifier("IntgCntrActionDataWorkQueue")
	private WorkQueueTaskData intgCntrActionDataWorkQueue;
	@Autowired
	@Qualifier("ChrgLogDataWorkQueue")
	private WorkQueueTaskData chrgLogDataWorkQueue;
	@Autowired
	@Qualifier("PluscallLogDataWorkQueue")
	private WorkQueueTaskData pluscallLogDataWorkQueue;
	@Autowired
	@Qualifier("ActionABPcodeDataWorkQueue")
	private WorkQueueTaskData actionABPcodeDataWorkQueue;
	@Autowired
	@Qualifier("BasketDataWorkQueue")
	private WorkQueueTaskData basketDataWorkQueue;
	
	@Value("${log.path}")
	private String logPath;
	@Value("${batch.close.delay.ms}")
	int batchCloseDelayMs;

	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			this.closed = true;

			logger.info("countDown start");

			this.kafkaListenerEndpointRegistry.stop();

			this.taskNearData.mongoToMariaNearData();
//			this.taskAppTargetData.mongoToMariaAppTargetData();
			this.taskIntgCntrData.mongoToMariaIntgCntrV3();
			this.taskIntgCntrKgrData.mongoToMariaIntgCntrKgrV3();
			this.taskMediaChrgData.mongoToMariaMediaChrgV3();
			this.taskIntgCntrTtimeData.mongoToMariaIntgCntrTtimeV3();
			this.taskActionData.mongoToMariaActionDataV3();
			this.taskActionPcodeData.mongoToMariaActionPcodeDataV3();
			this.taskClickViewPointData.mongoToMariaClickViewPointV3();
			this.taskClickViewPoint2Data.mongoToMariaClickViewPoint2V3();
			this.taskIntgCntrActionData.mongoToMariaIntgCntrActionDataV3();
			this.taskChrgLogData.mongoToMariaChrgLogData();
			this.taskPluscallLogData.mongoToMariaPluscallLogDataV3();
			this.taskActionABPcodeData.mongoToMariaActionABPcodeDataV3();
			this.taskBasketData.mongoToMariaBasketData();

			Thread.sleep(this.batchCloseDelayMs);
		} catch (Exception e) {
			logger.error("onApplicationEvent err", e);
		}

		while (!chkRetryProcess()) {
			try {
				Thread.sleep(500L);
			} catch (Exception exception) {
			}
		}

		logger.info("countDown end");
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public Object postProcessAfterInitialization(Object object, String arg1) throws BeansException {
		return object;
	}

	public Object postProcessBeforeInitialization(Object object, String arg1) throws BeansException {
		if (object instanceof ThreadPoolTaskScheduler) {
			((ThreadPoolTaskScheduler) object).setAwaitTerminationSeconds(30000);
			((ThreadPoolTaskScheduler) object).setWaitForTasksToCompleteOnShutdown(true);
		}
		if (object instanceof ThreadPoolTaskExecutor) {
			((ThreadPoolTaskExecutor) object).setWaitForTasksToCompleteOnShutdown(true);
		}
		return object;
	}

	public boolean chkRetryProcess() {
		boolean result = false;

		int qSize = 0;
		qSize = this.nearDataWorkQueue.getQueueSize()
//				+ this.appTargetDataWorkQueue.getQueueSize()
				+ this.intgCntrDataWorkQueue.getQueueSize() + this.intgCntrKgrDataWorkQueue.getQueueSize()
				+ this.mediaChargeworkQueue.getQueueSize() + this.intgCntrTtimeDataWorkQueue.getQueueSize()
				+ this.actionDataWorkQueue.getQueueSize() + this.actionPcodeDataWorkQueue.getQueueSize()
				+ this.clickViewPointDataWorkQueue.getQueueSize() + this.clickViewPoint2DataWorkQueue.getQueueSize()
				+ this.intgCntrActionDataWorkQueue.getQueueSize() +  this.chrgLogDataWorkQueue.getQueueSize()
				+ this.pluscallLogDataWorkQueue.getQueueSize() + this.actionABPcodeDataWorkQueue.getQueueSize()
				+ this.basketDataWorkQueue.getQueueSize();

		int threadCnt = 0;

		threadCnt = TaskRetryData.getThreadCnt() + TaskNearData.getThreadCnt() 
//		+ TaskAppTargetData.getThreadCnt()
				+ TaskIntgCntrData.getThreadCnt() + TaskIntgCntrKgrData.getThreadCnt()
				+ TaskMediaChrgData.getThreadCnt() + TaskIntgCntrTtimeData.getThreadCnt()
				+ TaskActionData.getThreadCnt() + TaskActionPcodeData.getThreadCnt()
				+ TaskClickViewPointData.getThreadCnt() + TaskClickViewPoint2Data.getThreadCnt()
				+ TaskIntgCntrActionData.getThreadCnt() + TaskChrgLogData.getThreadCnt()
				+ TaskPluscallLogData.getThreadCnt() + TaskActionABPcodeData.getThreadCnt()
				+ TaskBasketData.getThreadCnt();

		File file = new File(this.logPath + "retry");
		File[] fileArr = file.listFiles();

		logger.info("###################################################");
		logger.info("TaskRetryData Exe Thread  Cnt =>{}", Integer.valueOf(TaskRetryData.getThreadCnt()));
		logger.info("Exe Thread  Cnt =>{}", Integer.valueOf(threadCnt));
		logger.info("file path : {}", file.getPath());
		logger.info("qSize : {} ", Integer.valueOf(qSize));
		logger.info("retry file Cnt : {}", Integer.valueOf(fileArr.length));

		logger.info("###################################################");

		if (qSize == 0 && threadCnt == 0) {

			result = true;
		}

		return result;
	}
}
