package com.mobon.billing.core.schedule;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConvsuccLogData {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvsuccLogData.class);
	
	@Value("${log.path.convsucc}")
	private String logPathConvsucc;
	
	private String logFrepix = "kafka-consumer.logging.log.";
	private final int bufferSize = 2048;
	
	public void deleteFile() {
		Date standardDate  = new Date();
		standardDate.setTime((new Date().getTime() + (1000L * 60 * 60 * 168 * -1)));// 1초*1분*1시간*24시간*이전
		
		
		File dir = new File(logPathConvsucc);
		File[] files = dir.listFiles();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh");
		
		for (File file : files) {
			String[] spFileName = file.getName().split(logFrepix);
			Date fileDate = null;
			
			try {
				fileDate = dateFormat.parse(spFileName[1]);
				
				int compare = standardDate.compareTo(fileDate);
				
				if (compare > 0) {
					logger.info("ConvSucc File Delete " + file.getName());
					file.delete();
				}
				
			} catch (ParseException e) {
				logger.error("Delete ConvSucc File Fail " + e);
			}
			
		}
		
		
	}
}
