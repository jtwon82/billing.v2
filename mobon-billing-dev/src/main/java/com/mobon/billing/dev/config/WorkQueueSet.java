package com.mobon.billing.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobon.billing.dev.service.WorkQueueTaskData;

@Configuration
public class WorkQueueSet {

	@Bean(name="workQueuePollingData")
	public WorkQueueTaskData workQueue1() {
		return new WorkQueueTaskData("workQueuePollingData");
	}
}
