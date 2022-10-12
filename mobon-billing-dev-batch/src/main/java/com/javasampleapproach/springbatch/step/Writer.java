package com.javasampleapproach.springbatch.step;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class Writer implements ItemWriter<String> {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void write(List<? extends String> messages) throws Exception {
		logger.info("writer");
		
		for(String msg : messages){
			logger.info("#Writer Step: " + msg);
		}
	}
	
}