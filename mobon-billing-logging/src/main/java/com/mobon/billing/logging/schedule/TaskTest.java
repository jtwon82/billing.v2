package com.mobon.billing.logging.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class TaskTest {

	private static final Logger logger = LoggerFactory.getLogger(TaskTest.class);
	
	public void test() {
		logger.info("task test");
	}
	
}
