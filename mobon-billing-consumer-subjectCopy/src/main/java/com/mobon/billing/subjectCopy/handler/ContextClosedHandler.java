package com.mobon.billing.subjectCopy.handler;

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

import com.mobon.billing.subjectCopy.schedule.TaskRetryData;
import com.mobon.billing.subjectCopy.schedule.TaskSubjectCopyClickViewData;
import com.mobon.billing.subjectCopy.schedule.TaskSubjectCopyConvData;
import com.mobon.billing.subjectCopy.service.SumObjectManager;
import com.mobon.billing.subjectCopy.service.WorkQueueTaskData;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware, BeanPostProcessor{
	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);

	private static final int TIME_WAIT = 30 * 1000;

	private ApplicationContext context;

	public boolean closed = false;
	
	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
	@Autowired
	private TaskSubjectCopyClickViewData taskSubjectCopyClickViewData;
	@Autowired
	private TaskSubjectCopyConvData taskSubjectCopyConvData;
	
	@Autowired
	@Qualifier("SubjectCopyClickViewWorkQueue")
	private WorkQueueTaskData workQueue;
	
	@Autowired
	private SumObjectManager sumObjectManager;
		
	@Value("${log.path}")
	private String logPath;
	
	@Value("${batch.close.delay.ms}")
	int batchCloseDelayMs;
	
	@Value("${profile.id}")
	private String	profileId;
	
	
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

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		// TODO Auto-generated method stub
		this.context = context;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			closed = true;

			logger.info("countDown start");
			// for consumer stop
			kafkaListenerEndpointRegistry.stop();

			Thread.sleep(1000);
			
			taskSubjectCopyClickViewData.mongoToClickHouseSubjectCopyClickViewData();
			
			taskSubjectCopyConvData.mongoToClickHouseSubjectCopyConvData();
			
			Thread.sleep(batchCloseDelayMs);
			
		} catch (Exception e) {
			logger.error("onApplicationEvent err", e);
		}
		
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

	private boolean chkRetryProcess() {
		boolean result = false;
		// q size
		int qSize = workQueue.getQueueSize();
		//처리 Thread 수 
		int threadCnt = TaskRetryData.getThreadCnt();
		threadCnt = TaskSubjectCopyClickViewData.getThreadCnt();
		
		//retry 파일 
		File file = new File(this.logPath + "retry");
		
		File[] fileArr = file.listFiles();
		
		int fileSize = 0;
		
		if (fileArr != null) {
			fileSize = fileArr.length;
		}
 
		int sumObjCnt = 0;


		logger.info("###################################################");
		logger.info("TaskRetryData Exe Thread  Cnt =>{}", Integer.valueOf(TaskRetryData.getThreadCnt()));
		logger.info("Exe Thread  Cnt =>{}", Integer.valueOf(threadCnt));
		logger.info("file path : {}", file.getPath());
		logger.info("qSize : {},sumObjCnt : {}", Integer.valueOf(qSize), Integer.valueOf(sumObjCnt));
		logger.info("retry file Cnt : {}", fileSize);
		logger.info("###################################################");

		if (qSize == 0 && threadCnt == 0 && sumObjCnt == 0) {

			result = true;
		}
		
		return result;
	}
}
