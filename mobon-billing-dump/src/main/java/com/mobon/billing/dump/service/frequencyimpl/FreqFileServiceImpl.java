package com.mobon.billing.dump.service.frequencyimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.frequency.FreqCampDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqMediaScriptDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqSdkDayStats;
import com.mobon.billing.dump.file.mobon.FrequencySummary;
import com.mobon.billing.dump.file.mobon.vo.MobonFileDataVO;
import com.mobon.billing.dump.service.DumpFileService;
import com.mobon.billing.dump.utils.CommonUtils;
import com.mobon.billing.dump.utils.DateUtils;
import com.mobon.billing.dump.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import me.saro.commons.Files;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @FileName : FreqFileServiceImpl.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 23.
 * @Author dkchoi
 * @Comment : 처리할 Frequency파일을 찾아 라인별로 읽어 Summary시키는 서비스.
 */
@Slf4j
@Service("freqFileService")
public class FreqFileServiceImpl implements DumpFileService {

	@Value("${MOBON_LOG_DIR}")
	private String FrequencyLogPath;

	@Value("${MOBON_RETRY_DIR}")
	private String FrequencyRetryPath;

	@Value("${MOBON_RETRY_SUCC_DIR}")
	private String FrequencyRetrySuccPath;

	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.frequency.DumpFileReaderService@FileReadProcess(java.lang.String)
	 */
	public Map<String, Object> FileReadProcess(String AnHourAgo) throws Exception {

		File dir = new File(FrequencyLogPath);

		if(!dir.exists() && !dir.isDirectory()) {
			throw new Exception("Not Found Log Directory[" + FrequencyLogPath + "]");
		}

		File[] files = dir.listFiles();
		FrequencySummary stats = new FrequencySummary();

		for (File file : files) {
			if(!file.isFile())
				continue;

			if(!(file.getName().startsWith(GlobalConstants.MOBON_CLICKVIEW_FILE_NAME_PREFIX)
					|| file.getName().startsWith(GlobalConstants.MOBON_CONVERSION_FILE_NAME_PREFIX)) ||
					!file.getName().endsWith(GlobalConstants.MOBON_FILE_EXTENSION) ||
					!file.getName().contains(AnHourAgo))		//형식이 맞이 않는 파일 제외
			{
				log.info("@ search exception fileName : " + file.getName());
				continue;
			}

			log.info("@ search fileName : " + file.getName());

			loadFile(file, file.getName(), stats);
		}

		stats.getFrequencyDataVOMapSize(); // 전처리 된 프리퀀시 summary 데이터 크기 출력
		stats.setFrequencyData(); // 전처리 된 프리퀀시 summary 데이터 각각의 도메인에 set

		return stats.getFrequencyData();
	}

	/**
	 * @Method Name : RetryFileReadProcess
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 데이터 등록 오류 시 해당 데이터를 파일로 남기는 서비스.
	 * @param
	 * @return Map<String, Object> 재처리 파일 데이터
	 */
	public Map<String, Object> RetryFileReadProcess() throws Exception {

		File dir = new File(FrequencyRetryPath);

		if(!dir.exists() && !dir.isDirectory())
			return null;

		File[] files = dir.listFiles();
		FrequencySummary stats = new FrequencySummary();

		for (File file : files) {
			if(!file.isFile())
				continue;

			log.info("$ search retry fileName : " + file.getName());

			retryLoadFile(file, stats);

		}

		return stats.getFrequencyData();

	}

	/**
	 * @Method Name : makeRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @param futures
	 * @Comment : 데이터 처리 시 에러 발생하면 파일로 남기는 로직.
	 */
	public void makeRetryFile(Collection<Future<Object>> futures) {

		// 병렬 처리된 스레드 들의 결과(return)를 받기 위함. 해당 로직이 없으면 스케쥴러가 우선 종료 됨.
		for (Future<Object> future : futures) {
			try {

				Object returnObj = future.get();
				RetryFileWriteProcess(returnObj);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("@@@ Make Retry File Error ");
				log.error(e.getMessage());
			}
		}
	}


	/**
	 * @Method Name : moveRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @param futures
	 * @Comment : 데이터 재처리 성공 시. 성공 폴더로 이동시키는 서비스.
	 */
	public void moveRetryFile(Collection<Future<Object>> futures) {

		List<String> filesPrefix = new ArrayList<String>();

		for (Future<Object> future : futures) {
			try {

				Object returnObj = future.get();
				filesPrefix.add(RetryFileFailList(returnObj));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info("$$$ Retry File Move Error ");
				log.error(e.getMessage());
			}
		}

		RetryFileMoveList(filesPrefix);

	}

	/**
	 * @Method Name : removeRetryFile
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @param removeDate 삭제 대상 기간.
	 * @Comment : 일정 기간이 지난 재처리 파일은 삭제 처리하는 스케쥴러.
	 */
	public void removeRetryFile(long removeDate){

		/// retrySucc 경로에 모든 파일을 가져옴
		Files.listFilesStream(FrequencyRetrySuccPath)
				// removeDate 보다 오래된 데이터만 가져옴
				.filter(Files.attributesFilter(attr -> attr.creationTime().toMillis() < removeDate))
				// 삭제
				.forEach(File::delete);

	}

	/**
	 * @Method Name : loadFile
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 처리 대상이 된 파일을 라인별로 읽어 put 메소드에 전달.
	 * @param file
	 * @param fileName
	 * @param stats
	 * @return
	 * @throws IOException
	 */
	private void loadFile(File file, String fileName, FrequencySummary stats) throws IOException {

		if(file == null)
			return;


		try(BufferedReader buffReader = new BufferedReader(new FileReader(file))) {

			String line = null;
			int counter = 0;

			while((line = buffReader.readLine()) != null) {
				if(++counter%1000000 == 0)
					log.info("@ Read File Line.. " + counter);

				if(StringUtils.isEmpty(line))
					continue;

				put(stats, line, fileName);
			}

			if(++counter%1000000 != 0)
				log.info("@ Read File Line.. " + counter);

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * @Method Name : put
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 전달받은 라인을 MobonFileDataVO를 통해 정제하여 Summary객체를 통해 합산.
	 * @param stats
	 * @param line
	 * @param fileName
	 * @return
	 */
	private void put(FrequencySummary stats, String line, String fileName) {

		stats.setPreProFrequencyData(new MobonFileDataVO(line, fileName));

	}

	/**
	 * @Method Name : RetryFileWriteProcess
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 데이터 등록 오류 시 해당 데이터를 파일로 남기는 서비스.
	 * @param object Object
	 * @return
	 */
	private void RetryFileWriteProcess(Object object) {

		// 재처리 파일 생성 로직.
		if(!ObjectUtils.isEmpty(object)){

			// summary 데이터 파일로 쓰기.
			if(((List)object).get(0) instanceof FreqDayStats) {

				log.info("@@@ Make FreqDayStats File Start @@@");
				List<FreqDayStats> retryFreqDayStats = (List<FreqDayStats>) object;

				for(FreqDayStats entry : retryFreqDayStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(FrequencyRetryPath, GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("@@@ Make FreqDayStats File End @@@");

			} else if(((List)object).get(0) instanceof FreqSdkDayStats) {

				log.info("@@@ Make FreqSdkDayStats File Start @@@");
				List<FreqSdkDayStats> retryFreqSdkDayStats = (List<FreqSdkDayStats>) object;

				for(FreqSdkDayStats entry : retryFreqSdkDayStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(FrequencyRetryPath, GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("@@@ Make FreqSdkDayStats File End @@@");

			} else if(((List)object).get(0) instanceof FreqCampDayStats) {

				log.info("@@@ Make FreqCampDayStats File Start @@@");
				List<FreqCampDayStats> retryFreqCampDayStats = (List<FreqCampDayStats>) object;

				for(FreqCampDayStats entry : retryFreqCampDayStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(FrequencyRetryPath, GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("@@@ Make FreqCampDayStats File End @@@");

			} else if(((List)object).get(0) instanceof FreqMediaScriptDayStats) {

				log.info("@@@ Make FreqMediaScriptDayStats File Start @@@");
				List<FreqMediaScriptDayStats> retryFreqMediaScriptDayStats = (List<FreqMediaScriptDayStats>) object;

				for(FreqMediaScriptDayStats entry : retryFreqMediaScriptDayStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(FrequencyRetryPath, GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("@@@ Make FreqMediaScriptDayStats File End @@@");

			}

		} // 재처리 데이터 존재 여부 확인

	}

	/**
	 * @Method Name : loadFile
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 처리 대상이 된 파일을 라인별로 읽어 put 메소드에 전달.
	 * @param file
	 * @param stats
	 * @return
	 * @throws IOException
	 */
	private void retryLoadFile(File file, FrequencySummary stats) throws IOException {

		if(file == null)
			return;


		try(BufferedReader buffReader = new BufferedReader(new FileReader(file))) {

			String line = null;

			if(file.getName().startsWith(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					FreqDayStats freqDayStats = null;
					freqDayStats = mapper.readValue(line, FreqDayStats.class);

					stats.putFreqDayStatsMap(freqDayStats);

				}

			} else if(file.getName().startsWith(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					FreqSdkDayStats freqSdkDayStats = null;
					freqSdkDayStats = mapper.readValue(line, FreqSdkDayStats.class);

					stats.putFreqSdkDayStatsMap(freqSdkDayStats);

				}

			} else if(file.getName().startsWith(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					FreqCampDayStats freqCampDayStats = null;
					freqCampDayStats = mapper.readValue(line, FreqCampDayStats.class);

					stats.putFreqCampDayStatsMap(freqCampDayStats);

				}

			} else if(file.getName().startsWith(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					FreqMediaScriptDayStats freqMediaScriptDayStats = null;
					freqMediaScriptDayStats = mapper.readValue(line, FreqMediaScriptDayStats.class);

					stats.putFreqMediaScriptDayStatsMap(freqMediaScriptDayStats);

				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * @Method Name : RetryFileFailList
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 재처리 실패파일을 리스트.
	 * @param object Object
	 * @return
	 */
	private String RetryFileFailList(Object object) {

		String result = null;

		// 재처리 실패 체크
		if(!ObjectUtils.isEmpty(object)){

			// summary 데이터 파일로 쓰기.
			if(((List)object).get(0) instanceof FreqDayStats) {
				result = GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof FreqSdkDayStats) {
				result = GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof FreqCampDayStats) {
				result = GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof FreqMediaScriptDayStats) {
				result = GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT;
			}
		} // 재처리 데이터 존재 여부 확인

		return result;
	}


	/**
	 * @Method Name : RetryFileMoveProcess
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 재처리 성공파일 체크.
	 * @param filePrefixList List
	 * @return
	 */
	private void RetryFileMoveList(List<String> filePrefixList) {

		File dir = new File(FrequencyRetryPath);

		if(!dir.exists() && !dir.isDirectory())
			return;

		File[] files = dir.listFiles();

		for (File file : files) {

			boolean retrySucc = true;

			if(!file.isFile())
				continue;

			for(String filePrefix : filePrefixList)		//재처리 대상 파일 제외
			{
				if(StringUtils.isNotEmpty(filePrefix) && file.getName().startsWith(filePrefix)) {
					log.info("$ Retry Fail fileName : " + file.getName());
					retrySucc = false;
				}
			}

			if(retrySucc) {
				moveFile(file);
			} else {
				this.makeReNameRetryFile(file);
			}

		}
	}
	/**
	 * @Method Name : makeReNameRetryFile
	 * @Date : 2020. 10. 20.
	 * @Author : dhlim
	 * @Comment : 재처리 실패파일이름 변경.
	 * @param file File
	 * @return
	 */
	private void makeReNameRetryFile(File file) {
		String retryFailPath = FrequencyRetryPath+"/fail/";
		file.renameTo(new File(retryFailPath+ file.getName()));
	}

	/**
	 * @Method Name : RetryFileMoveProcess
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 재처리 성공파일을 이동.
	 * @param file File
	 * @return
	 */
	private void moveFile(File file){
		file.renameTo(new File(FrequencyRetrySuccPath + file.getName()));
	}

}
