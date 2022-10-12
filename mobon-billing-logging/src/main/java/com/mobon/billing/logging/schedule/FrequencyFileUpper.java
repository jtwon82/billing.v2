package com.mobon.billing.logging.schedule;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobon.billing.logging.frequency.FrequencyLogBillingZip;
import com.mobon.billing.util.ShellUtils;

@Component
public class FrequencyFileUpper {

	private static final Logger logger = LoggerFactory.getLogger(FrequencyFileUpper.class);
	
	@Value("${log.path.frequency}")
	public String logDirPath;
	@Value("${log.path.convsucc}")
	public String convSuccPath;
	@Value("${logging.path}")
	public String loggingPath;
	
	public void doing() {
		
		try {
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -2);
			String TwoHourAgo = sdfDate.format(calendar.getTime());
			
			ShellUtils.shellCmd(loggingPath + "make_conversion_log.sh");
			
			
			final String CLICKVIEW_FILE_NAME_PREFIX = "collect_freq_clickview_";
			final String CONVERSION_FILE_NAME_PREFIX = "collect_freq_conversion_";
			final String CONVSUCC_FILE_NAME_PREFIX = "kafka-consumer.logging.log.";
			
			logger.info("# Click View File Zip , Move , Delete begine");
			FrequencyLogBillingZip frequencyLogBillingZip = new FrequencyLogBillingZip(logDirPath, CLICKVIEW_FILE_NAME_PREFIX, TwoHourAgo);
			frequencyLogBillingZip.logFileZip();
			logger.info("# Click View File Zip , Move , Delete end");
			
			logger.info("# Conversion File Zip , Move , Delete begine");
			frequencyLogBillingZip = new FrequencyLogBillingZip(logDirPath, CONVERSION_FILE_NAME_PREFIX, TwoHourAgo);
			frequencyLogBillingZip.logFileZip();
			logger.info("# Conversion File Zip , Move , Delete end");
			
			logger.info("# convsucc File Zip , Move , Delete begine");
			frequencyLogBillingZip = new FrequencyLogBillingZip(convSuccPath, CONVSUCC_FILE_NAME_PREFIX, TwoHourAgo);
			frequencyLogBillingZip.deleteLogFile();
			logger.info("# convsucc File Zip , Move , Delete end");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		
		
		
	}
	
}
