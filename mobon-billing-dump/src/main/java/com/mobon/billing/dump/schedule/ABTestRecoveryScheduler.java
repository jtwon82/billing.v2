package com.mobon.billing.dump.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.service.DumpFileService;
import com.mobon.billing.dump.service.DumpSaveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ABTestRecoveryScheduler {

	@Resource(name="abTestSaveService")
	private DumpSaveService abTestSaveService;

	@Resource(name="abTestFileService")
	private DumpFileService abTestFileService;

	@Value("${MOBON_LOG_DIR}")
	private String ABTESTLogPath;

	//@Async
	//@Scheduled(cron = "0 */30 * * * *")
	public void ABTestRecoveryInsertScheduler() {

		Map<String, Object> totResultData = new HashMap<String, Object>();
		Collection<Future<Object>> futures = new ArrayList<Future<Object>>();
		//String [] dateArr = {"2021-03-16_","2021-03-17_"};
		//String []dates = {"2021-03-15_","2021-03-16_","2021-03-17_","2021-03-18_"};
		//String [] dayArr = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};		
		//String [] dayArr = {"18","19","20","21","22","23"};		
		//String [] dayArr = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17"};
		//String date = "2021-03-16_";

		//this.unZipFile(recoveryDay);
		ArrayList<String> recoveryDays = this.getRecoveryDate();
		String recoveryDay = recoveryDays.get(0);
		
		this.removeFileLine(recoveryDays);
		
		try {
			totResultData = abTestFileService.FileReadProcess(recoveryDay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("#### ABTEST File Read End - {}" , recoveryDay );
		log.info("### Save ABTEST Data Start");

		for(Map.Entry<String, Object> resultData : totResultData.entrySet()) {
			futures.add(abTestSaveService.SaveDumpData(totResultData , resultData.getKey()));
		}


		abTestFileService.makeRetryFile(futures);

		log.info("### Save ABTEST Data End");
		log.info("##### ABTEST File Process End #####");

	}

	private void removeFileLine(ArrayList<String> recoveryDay) {
		String fileName = "FixDate.txt";
		File file = new File(ABTESTLogPath + fileName);
		BufferedWriter bw = null;
		try {
			bw  = new BufferedWriter(new FileWriter(file));
			for (int i = 1;i < recoveryDay.size(); i++) {
				bw.write(recoveryDay.get(i));
				bw.newLine();
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private ArrayList<String> getRecoveryDate() {
		String fileName = "FixDate.txt";
		File file = new File(ABTESTLogPath + fileName);
		ArrayList <String> dateArr = new ArrayList<String>();
		BufferedReader inFile = null;
		try {

			if (file.exists()) {
				inFile = new BufferedReader(new FileReader(file));
				String sLine = null;
				while ((sLine = inFile.readLine())!= null) {
					dateArr.add(sLine);
				}
				file.delete();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dateArr;
	}

	private void unZipFile(String recoveryDay) {
		String clickPrepix = GlobalConstants.MOBON_CLICKVIEW_FILE_NAME_PREFIX;
		String convPrepix = GlobalConstants.MOBON_CONVERSION_FILE_NAME_PREFIX;
		String zipFileName = "";

		File zipFile = new File(ABTESTLogPath + zipFileName);


		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipEntry = null;

		try {
			fis = new FileInputStream(zipFile);

			zis = new ZipInputStream(fis, Charset.forName("UTF-8"));

			while ((zipEntry =zis.getNextEntry()) != null) {
				String filename = zipEntry.getName();
				if (filename.indexOf(recoveryDay) > 0) {
					log.info("fileName - {} ", filename);
				}
			}
		} catch (Exception e) {

		}
	}
}
