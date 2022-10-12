package com.javasampleapproach.springbatch.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<String, String>{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String process(String content) throws Exception {
		logger.info("processor");
		
		return content.toUpperCase();
	}
}