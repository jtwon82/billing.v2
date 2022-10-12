package com.mobon.billing.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.util.old.DateUtils;
import com.mobon.billing.config.Sender;

@Component
public class TaskMobonBatchSendData {

	private static final Logger logger = LoggerFactory.getLogger(TaskMobonBatchSendData.class);

	@Autowired
	private Sender sender;

	@Value("${log.path}")
	private String logPath;
	
	@Value("${log.retry.path}")
	private String logRetryPath;
	@Value("${log.retryok.path}")
	private String logRetryokPath;
	
	

	public void sendingData() {
		logger.info("sendingData START");

		String topic = "";
		String lineDataSub = "";
		String fileName = "";
		String fileName_prepix = "sendError";
		String file_reName = "";
		try {
			
			// 로그 파일 위치
			File file = new File(logRetryPath);
			if (!file.exists()) {	file.mkdir();	}
			File fileRetryOk = new File(logRetryokPath);
			if (!fileRetryOk.exists()) {	fileRetryOk.mkdir();	}
			
			// 파일 리스트를 가져옴
			File[] fileArr = file.listFiles();
			if ((fileArr == null) || (fileArr.length == 0)) {
				return;
			}

			BufferedReader fr = null;
			File file_Tmp = null;
			int fileRowCnt = 0;

			for (File readFile : fileArr) {
				if (readFile != null) {

					// 파일이 존재 할경우
					if (readFile.exists()) {
						fileName = readFile.getName();

						// 파일 이름에 데이타 파일 프리픽스가 있을 경우만 처리
						if (fileName.indexOf(fileName_prepix) == -1) {
							continue;
						}
						if (fileName.indexOf("_ing") > 0) {
							continue;
						}
						logger.info("processing fileName - {}", fileName);
						
						file_reName = readFile.getAbsolutePath() +"_"+ DateUtils.getDate("yyyyMMdd_HHmmss") +"_ing";
						file_Tmp = new File(file_reName);
						readFile.renameTo(file_Tmp);

						if (file_Tmp.isFile()) {
							try {
								fr = new BufferedReader(new FileReader(file_Tmp));
								String lineData="";
								while ((lineData = fr.readLine()) != null) {
									if ("".equals(lineData.trim())) {
										continue;
									}
									if (lineData.length() < 41) {
										continue;
									}
									
									try {
										String []lines = lineData.split("\t");
										topic = lines[1];
										lineDataSub = lines[2];
										sender.send(topic, lineDataSub);
									}catch(Exception e) {
										logger.error("err split ", e);
										logger.info("err split data={}", lineDataSub);
									}
									
									fileRowCnt++;
									if ((fileRowCnt % 100000) == 0) {
										logger.info("topic file readCnt =>{}", new DecimalFormat("#,###").format(fileRowCnt));
									}
								}
							} catch (Exception e) {
								logger.error("findTargetFile Exception==>{}", e);
							} finally {
								if (fr != null) {	try {	fr.close(); } catch (IOException e) {}}
								logger.info("topic file finally readCnt =>{}", new DecimalFormat("#,###").format(fileRowCnt));
							}
						}
						
						// move retryfile to retryok
						File retryfile = new File(String.format("%s/%s", logRetryPath, file_Tmp.getName()));
						File retryokfile = new File(String.format("%s/%s_%s", logRetryokPath, file_Tmp.getName(), DateUtils.getDate("yyyyMMdd_HHmmss")));

						// 재처리 완료 retryok 로 이동
						retryfile.renameTo(retryokfile);
						logger.info("renameTo retryfile {} => retryokfile {}", retryfile, retryokfile);
					}
				}
			}
			

			// 과거 파일은 삭제
			File dir = new File(logRetryokPath);
			File [] files3 = dir.listFiles();
			
			ArrayList listKeepingDay = new ArrayList();
			for(int i=0; i>-62; i--) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis( new Date().getTime() );
				cal.add(Calendar.DAY_OF_MONTH, i);
				String t = DateUtils.getDate("yyyyMMdd", new Date( cal.getTimeInMillis() ) );
				listKeepingDay.add(t);
			}
			
			for(int i=0; i<files3.length; i++){
				if( files3[i].getName().indexOf("_ing")>0 ) {
					String lastDate = DateUtils.getDate("yyyyMMdd", new Date( files3[i].lastModified() ));
					if( listKeepingDay.indexOf( lastDate )<0 ) {
						logger.info(String.format("delete files3[i] - %s", files3[i].getName()));
						files3[i].delete();
					}
				}
			}
		
		}catch(Exception e1) {
			logger.error("err ", e1);
			logger.info("topic-{}, data-{}", topic, lineDataSub);
		}
	}
	
	public boolean chkRetryProcess() {
		boolean result = false;
		
		//retry 파일 개수 
		File file  =  new File (logPath +"testdata");
		File [] fileArr = file.listFiles();

		logger.info("###################################################");
		logger.info("file path : {}",file.getPath());
		logger.info("retry file Cnt : {}",fileArr.length);
		logger.info("###################################################");
		
		if( (fileArr.length==0) ){
			//처리 끝 
			result = true;
		}
		
		return result;
	}

}
