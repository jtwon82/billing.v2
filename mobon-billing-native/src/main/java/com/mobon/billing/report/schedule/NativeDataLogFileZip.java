package com.mobon.billing.report.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;

@Component
public class NativeDataLogFileZip {

	private static final Logger logger = LoggerFactory.getLogger(NativeDataLogFileZip.class);

	@Value("${log.path}")
	private String logPath;
		
	@Value("${log.data.file.date.format}")
	private String	logFileDateFormat;
	
	//데이타 파일 보관 시간 
	private String logKeepHour = "170";
	 

	// 압축 대상 파일
	private final String[] logFolder = { "log4j/" };

	private String logFilePreFix = G.NativeNonAdReport;
	private final int bufferSize = 2048;
	private String zipLogPath = "";
	
	

	/*
	 * 로그 파일을 압축합니다. 
	 */
	public void nativeDataLogFileZip() {

		
		logger.info("bidLogFileZip start~");
		
		boolean orgFileDelete = true;
		
		try {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -20);// 20분전
			String zipFileName = DateUtils.getDate(logFileDateFormat, new Date(cal.getTimeInMillis()));

			for (String folder : logFolder) {
				
				File file1 = new File(logPath + folder);
				
				if (file1.exists() && file1.isDirectory()) {
					
					File[] targetFileArr = file1.listFiles();

					if ((targetFileArr == null) || (targetFileArr.length == 0)) {
						continue;
					}

					String fileName = "";
					String exeStartTime = "";

					for (File targetFile : targetFileArr) {
						
						orgFileDelete = true;
						fileName = targetFile.getName();

						if (fileName.indexOf(".zip") > 0) {
							continue;
						}
						
						if (fileName.indexOf(logFilePreFix) < 0) {
							continue;
						}
						
						if (fileName.indexOf(zipFileName) < 0) {
							continue;
						}

						ZipOutputStream zipOut = null;
						BufferedInputStream instream = null;

						try {
							
							File mkFile = new File(logPath + folder + "/logZip/");
							
							if (!mkFile.exists()) {
								mkFile.mkdirs();
							}

							exeStartTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");

							ZipEntry entry = new ZipEntry(fileName);
							zipLogPath = logPath + folder + "/logZip/";
							zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipLogPath + fileName + ".zip"), bufferSize));
							zipOut.putNextEntry(entry);
							instream = new BufferedInputStream(new FileInputStream(targetFile));

							int read;
							byte[] data = new byte[bufferSize];
							while ((read = instream.read(data, 0, bufferSize)) != -1) {
								zipOut.write(data, 0, read);
							}
							zipOut.flush();

						} catch (Exception e) {
							
							orgFileDelete = false;
							logger.error("zip file Excepiton ==>{}", e);

						} finally {
							
							try {
								if (zipOut != null)		zipOut.close();
								if (instream != null)	instream.close();
							} catch (IOException e) {
								logger.error("zip file IOException ==>{}", e);
							}
							
							if (orgFileDelete) {
								if (targetFile.exists())	targetFile.delete();
							}

							String exeEndTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							long diff = 0;
							long sec = 0;
							try {
								Date startT = f.parse(exeStartTime);
								Date endT = f.parse(exeEndTime);
								diff = endT.getTime() - startT.getTime();
								sec = diff / 1000;
							} catch (ParseException e) {
								
								logger.error("ParseException error log => {}", e);
								
							}
							logger.info("파일 하나 걸린 시간 : {}", sec);

						}
					}
				}
			}
		} catch (Exception e) {
			
			logger.error("logFileZip Exception Log => {}", e);
			
		} finally {
			// 파일 삭제
			deleteLogFile();

		}

	}

	/*
	 * 로그 파일을 삭제 합니다. 
	 */
	public void deleteLogFile() {
		
		try {
			
			int delHour = Integer.parseInt(logKeepHour);
			
			Date beforDate = new Date();
			beforDate.setTime((new Date().getTime() + (1000L * 60 * 60 * delHour * -1)));// 1초*1분*1시간*beforHour 시간*이전
			String strBeforDate = DateUtils.getDate(logFileDateFormat, beforDate);
			
			for(String  folder: logFolder) {
				
				zipLogPath = logPath + folder + "/logZip/";
				
				File deleteFile = new File(zipLogPath   );
				
				String []  pathList = deleteFile.list();
				
				
				logger.info("delete file List =>{},{},{}",delHour,strBeforDate,pathList);
				
				
				if((pathList!=null)&&(pathList.length>0)) {
					
					for(String fname :  pathList) {
						
						
						if((fname.startsWith(logFilePreFix)) &&(fname.indexOf(strBeforDate)>0)) 
						{
							File dFile = new File(zipLogPath + fname);
							
							if (dFile.exists()) {
								dFile.delete();
							}
							
						}//end if fname 
						
					}// end for pathlist 
					
				}//end if chk pathList
				
			}//end for 			
			
		} catch (Exception e) {
			logger.error("deleteLogFile==> {}", e);
		}

	}// end deleteLogFile
	 
	

}// end class
