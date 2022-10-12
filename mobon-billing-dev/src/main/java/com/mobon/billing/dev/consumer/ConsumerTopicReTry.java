package com.mobon.billing.dev.consumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dev.model.PollingData;
import com.mobon.billing.dev.service.SumObjectManager;

@Component
public class ConsumerTopicReTry{

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerTopicReTry.class);

	@Value("${log.path}")
	private String	logPath;
	
	private static final String topicFilePath = "topicFile";

	@Autowired
	SumObjectManager sumObjectManager;
	
	@Scheduled(fixedDelay = 1000)
	public void  processTopicFile() {
		
		//로그 파일 위치 
		File file  =  new File (logPath+"../");

		if(!file.exists()){
			file.mkdir();
		}
		
		//파일 리스트를 가져옴
		File [] fileArr = file.listFiles();
		
		if((fileArr == null)||(fileArr.length==0)) {
			return;
		}
		
		
		DecimalFormat df = new DecimalFormat("#,###");
		
		BufferedReader fr=null;
		File file_Tmp =null;
		
		String topic = "";		
		String lineData = "";
		String lineDataSub="";
		String fileName= "";
		String fileName_prepix = "kafka-consumer.logging.log.topic";
		String file_reName = "";
		
		boolean fileReadSuc = true;
		
		int fileRowCnt = 0;
		int loc = 0;
		
		for (File readFile : fileArr) {

			if (readFile != null) {

				// 파일이 존재 할경우
				if (readFile.exists()) {
					
					//파일 이름을 가져옴
					fileName = readFile.getName(); 

					//파일 이름에 데이타 파일 프리픽스가 있을 경우만 처리  
					if(fileName.indexOf(fileName_prepix) == -1) {
						fileReadSuc = false;
						continue;
					}
					
					if(fileName.indexOf("_ing")>0) {
						fileReadSuc = false;
						continue;
					}
					 
					long millis = Calendar.getInstance().getTimeInMillis();
					
					file_reName = readFile.getAbsolutePath() +"_"+ millis +"_ing";
					file_Tmp = new File( file_reName );
					readFile.renameTo( file_Tmp );

					if (file_Tmp.isFile()) {
						try {
							
							logger.info("file_Tmp.getPath()==>"+ file_Tmp.getPath());

							fr = new BufferedReader(new FileReader(file_Tmp));
							
							while ((lineData = fr.readLine()) != null) {

								if ("".equals(lineData.trim())) {
									continue;
								}
								
								if (lineData.length() < 41) {
									continue;
								}
								
								String [] row = lineData.split("\t");
								topic = row[1];
								lineDataSub = row[2];
								
								try {
									PollingData a= new ObjectMapper().readValue(lineDataSub, PollingData.class);
									sumObjectManager.appendPollingData(a);
								}catch(Exception e) {
									logger.error("err", e);
								}
								
								fileRowCnt++;
								
								if((fileRowCnt % 100000)==0) {
									logger.info("TOPIC FILE READCNT => {}",df.format(fileRowCnt));
								}
								
							} // end file read while

						} catch (Exception e) {
							fileReadSuc = false;
							logger.error("findTargetFile Exception==>{}, {}", e, lineDataSub);

						} finally {
							if (fr != null) {
								try {fr.close();} catch (IOException e) {
									logger.error("fr close Exception==>{}", e);
								}
							}
							if(fileReadSuc) {
								if(file_Tmp.exists()) {file_Tmp.delete();}										
							}
							logger.info("topic file finally readCnt =>{}",df.format(fileRowCnt));
						}
					}
				}
			}
		}
	}
}
