package com.mobon.billing.viewclicklog.handler;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.viewclicklog.schedule.TaskConversionVo;
import com.mobon.billing.viewclicklog.service.dao.ConversionVoDao;
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

import com.mobon.billing.model.v20.ViewClickVo;
import com.mobon.billing.viewclicklog.schedule.TaskRetryData;
import com.mobon.billing.viewclicklog.schedule.TaskViewClickVo;
import com.mobon.billing.viewclicklog.service.SumObjectManager;
import com.mobon.billing.viewclicklog.service.WorkQueueTaskData;

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware, BeanPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);
	private static final int TIME_WAIT = 30 * 1000;
	public boolean closed = false;

	private ApplicationContext context;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@Autowired
	private SumObjectManager sumObjectManager;
	
	@Autowired
	private TaskViewClickVo	taskViewClickVo;

	@Autowired
	private TaskConversionVo taskConversionVo;

	@Autowired
	private ConversionVoDao conversionVoDao;

	@Autowired
	@Qualifier("ViewClickVoWorkQueue")
	private WorkQueueTaskData workQueue;

	@Value("${log.path}")
	private String logPath;

	@Value("${batch.close.delay.ms}")
	int batchCloseDelayMs;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {

		try {
			closed = true;

			logger.info("countDown start");

			// for consumer stop
			kafkaListenerEndpointRegistry.stop();

			// for
			taskViewClickVo.mongoToFileViewClickVo();
			taskConversionVo.changeFlag(closed);
			Thread.sleep(2000);
			taskConversionVo.mongoToFileConversionVo();

			// for kafka autocommit
			Thread.sleep(batchCloseDelayMs);

		} catch (Exception e) {
			logger.error("onApplicationEvent err", e);
		}
		
		// ??????????????? ???????????? ??????
		while (!chkRetryProcess()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				logger.error("onApplicationEvent Thread.sleep err", e);
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
	 * ????????? ????????? ?????? ?????????.
	 * 1) ???????????? ????????? ?????? : fileRowCnt
	 * 2) queue??? ?????? ?????? ?????? ????????? ?????? ??? : workQueue.getQueueSize();
	 * 3) retry ????????? ?????? ?????? : logPath+"retry\"
	 * 4) queue??? ??????????????? retry ????????? ????????? ?????? ?????? ???????????? ?????????
	 * 5) ???????????? Thread??? ?????? ??????.
	 */
	public boolean chkRetryProcess() {

		boolean result = false;
		// q size
		int qSize = workQueue.getQueueSize();

		// ?????? Thread ???
		int threadCnt = 0;
		threadCnt += TaskViewClickVo.getThreadCnt();
		threadCnt += TaskConversionVo.getThreadCnt();

		// retry ?????? ??????
		File file = new File(logPath + "retry");

		File[] fileArr = file.listFiles();

		// sumObject check
		int sumObjCnt = 0;
		Map<String, ViewClickVo> mapViewClickVo = sumObjectManager.getMapViewClickVo();
		Map<String, ConversionVo> mapConversionVo = sumObjectManager.getMapConversionVo();

		sumObjCnt += mapViewClickVo.size() + mapConversionVo.size();

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