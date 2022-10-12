package com.mobon.billing.logging.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class ContextClosedHandler
		implements ApplicationListener<ContextClosedEvent>, ApplicationContextAware, BeanPostProcessor {
    
	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);

	private static final int TIME_WAIT = 30 * 1000;
	
	private ApplicationContext context;
	
	public boolean closed = false;
	
	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
	
	@Value("${batch.close.delay.ms}") int batchCloseDelayMs;
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			closed = true;
			
			logger.info("countDown start");
			
			// for consumer stop
			kafkaListenerEndpointRegistry.stop();
			
			// for kafka autocommit
			Thread.sleep(batchCloseDelayMs);
			
			logger.info("countDown end");
			
		} catch (Exception e) {
			logger.error("onApplicationEvent err - {}", e.getMessage());
		}
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
}