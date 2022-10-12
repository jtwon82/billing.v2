package com.mobon.billing.frequency.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mobon.billing.frequency.config.Constants;
import com.mobon.billing.frequency.service.FileReadDbWriteService;

import lombok.extern.slf4j.Slf4j;

/**
 * 처리 : 프리퀀시 파일 읽어 DB INSERT/UPDATE 처리
 * 주기 : 1시간에 1번 
 * 대상파일 : 현재시간-6시간 파일
 * 
 * @author ljs
 * @since 0.1
 */
@Slf4j
@Service
@Component("FileReadDbWrite")
public class FileReadDbWriteServiceImpl implements FileReadDbWriteService{

	// 대상 로그파일 ROOT_PATH
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.read-file-path-root:''}")
	public String fileRoot;
	// 대상 PRODUCT
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-product:''}")
	private String targetProducts;
	// 대상 AD_GUBUN
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-adgubun:''}")
	private String targetAdgubun;
	// 제외 지면번호
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.except-ms-no:''}")
	private String exceptMsNo;
	// 대상파일 이름(Conv)
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-file-conv}")
	private String targetFileConv;
	// 대상파일 이름(Click/View)
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-file-clickview}")
	private String targetFileClickView;
	// 대상파일 확장자
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-file-extension}")
	private String targetFileExtension;
	// 대상파일 날짜
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-file-date}")
	private int targetFileDate;
	// 대상파일 시간
	@Value("${spring.scheduler.cron.freq-file-read-db-write-batch.target-file-hour}")
	private int targetFileHour;
	
	@Override
	public int execute() throws Exception {
		
		try {
			
			// CONV 로그파일처리
			executor( getfileFullPath( targetFileConv ), Constants.freqType.CONV.name() );
			// CLICK_VIEW 로그파일처리
			executor( getfileFullPath( targetFileClickView ), Constants.freqType.CLICK_VIEW.name() );

			
		} catch (Exception e) {
			log.error("[ERROR] ",e);
		}
		
		return 0;
	}

	/**
	 * 실제 파일 읽어서 처리하는 부분
	 *
	 * @param fileName
	 * @param freqType
	 *
	 * @author ljs
	 * @since 0.1
	 */
	public void executor( String fileName, String freqType ) {
		
		File file =  new File( fileName );
		
		if ( !file.exists() ) {
			log.error("[ERROR] file is not exists => {} / {}",fileName,freqType);
			return;
		}
		
		// 1. 압축파일 풀기

		
		// 현재시간 -6시간 파일 처리
		// 1. 압축파일 풀기
		// 2. 파일 읽어오는 로직 필요
		// 3. 파일 line마다 객체에 (+= or appand) 하는 로직 필요
		//    => 객체마다 map의 키기준값을 줘야함
		// 4. 객체 리스트 마다 디비에 insert/update 하는 로직 필요
		// 5. zip해제한 파일 및 zip파일 삭제로직?
		
		
	}
	
	/**
	 * 파일 FULL_PATH 리턴 
	 *
	 * @param freqFilaName
	 * @return
	 *
	 * @author ljs
	 * @since 0.1
	 */
	public String getfileFullPath(String freqFilaName) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, targetFileDate);
		cal.add(Calendar.HOUR, targetFileHour);
		String curDate = format.format(cal.getTime());
		String fileName = fileRoot + "/" + freqFilaName + "_" + curDate + targetFileExtension;
		return fileName;
	}


}
