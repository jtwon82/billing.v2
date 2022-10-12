package com.mobon.billing.branchAlgo.handler;

import java.io.File;

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

import com.mobon.billing.branchAlgo.schedule.TaskRetryData;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;


@Component
public class ContextClosedHandler 
	implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware, BeanPostProcessor{
	
	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);

	private static final int TIME_WAIT = 30 * 1000;

	private ApplicationContext context;

	public boolean closed = false;
	
	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@Autowired
	private SumObjectManager sumObjectManager;
	
	@Autowired
	@Qualifier("ClickViewPcodeDataWorkQueue")
	private WorkQueueTaskData  taskClickViewPcodeData;
	
	@Autowired
	@Qualifier("ViewPcodeDataWorkQueue")
	private WorkQueueTaskData  taskViewPcodeData;
	
	@Value("${log.path}")
	private String logPath;
	@Value("${batch.close.delay.ms}")
	int batchCloseDelayMs;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			this.closed = true;

			logger.info("countDown start");

			this.kafkaListenerEndpointRegistry.stop();

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

	private boolean chkRetryProcess() {
		boolean result = false;

		int qSize = 0;
		qSize += this.taskClickViewPcodeData.getQueueSize();
		qSize += this.taskViewPcodeData.getQueueSize();
		qSize += this.taskClickViewPcodeData.getQueueSize();

		int threadCnt = 0;

		threadCnt += TaskRetryData.getThreadCnt();

		File file = new File(this.logPath + "retry");

		File[] fileArr = file.listFiles();

		int sumObjCnt = 0;


		logger.info("###################################################");
		logger.info("TaskRetryData Exe Thread  Cnt =>{}", Integer.valueOf(TaskRetryData.getThreadCnt()));
		logger.info("Exe Thread  Cnt =>{}", Integer.valueOf(threadCnt));
		logger.info("file path : {}", file.getPath());
		logger.info("qSize : {},sumObjCnt : {}", Integer.valueOf(qSize), Integer.valueOf(sumObjCnt));
		logger.info("retry file Cnt : {}", Integer.valueOf(fileArr.length));
		logger.info("###################################################");

		if (qSize == 0 && threadCnt == 0 && sumObjCnt == 0) {

			result = true;
		}

		return result;
	}
	
	
	
}
