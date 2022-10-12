package com.mobon.billing.dev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

	@Override
	public void configureTasks(ScheduledTaskRegistrar arg0) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
		threadPoolTaskScheduler.initialize();
		arg0.setTaskScheduler(threadPoolTaskScheduler);
		logger.info("SchedulerConfig INIT");
	}

}
