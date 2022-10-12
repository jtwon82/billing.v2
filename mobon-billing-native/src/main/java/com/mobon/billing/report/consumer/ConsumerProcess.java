package com.mobon.billing.report.consumer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.NativeNonAdReportData;
import com.mobon.billing.report.service.SumObjectManager;
import com.mobon.billing.util.ConsumerFileUtils;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcess {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcess.class);

	@Autowired
	private SumObjectManager	sumObjectManager;
	
	@Value("${log.path}")
	private String	logPath;
	
	
	@Value("${log.data.file.date.format}")
	private String	logFileDateFormat;
	
	
	public void processMain(String topic, String message) {
		try {
			JSONObject jSONObject = JSONObject.fromObject(message);
			logger.debug("topic - {}, msg - {}", topic, message);
			
			if ( G.NativeNonAdReport.equals(topic) ) {
				
				NativeNonAdReportData obj = NativeNonAdReportData.fromHashMap(jSONObject);
				
				sumObjectManager.appendNativeNonAdReportData(obj);
				
				
				//////////////////데이타 내역을 파일로 남김//////////////////
				String dataFilePath = logPath +"log4j/";
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				String dateFormat = DateUtils.getDate(logFileDateFormat, new Date(cal.getTimeInMillis()));
				
				String writeFileName = String.format("%s_%s", G.NativeNonAdReport, dateFormat);
				
				
				try {
					
					File dataFile = new File(dataFilePath);
					
					if(!dataFile.exists()) {
						dataFile.mkdirs();
					}
					
					//ConsumerFileUtils.writeLine( dataFilePath, writeFileName, G.NativeNonAdReport, message);					
					ConsumerFileUtils.writeLineNonDateTopic( dataFilePath, writeFileName, message);					
					
				} catch (IOException ioe) {
					
				}
				
				//////////////////데이타 내역을 파일로 남김//////////////////				
				
			} else {
				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
			}
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

}
