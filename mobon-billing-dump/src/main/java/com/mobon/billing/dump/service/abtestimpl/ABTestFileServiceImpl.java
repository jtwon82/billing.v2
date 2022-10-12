package com.mobon.billing.dump.service.abtestimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.abtest.ABAdverStatsMobile;
import com.mobon.billing.dump.domainmodel.abtest.ABAdverStatsWeb;
import com.mobon.billing.dump.domainmodel.abtest.ABComFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABComStats;
import com.mobon.billing.dump.domainmodel.abtest.ABCombiFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABParStats;
import com.mobon.billing.dump.file.mobon.ABTestSummary;
import com.mobon.billing.dump.file.mobon.vo.MobonFileDataVO;
import com.mobon.billing.dump.service.DumpFileService;
import com.mobon.billing.dump.utils.CommonUtils;
import com.mobon.billing.dump.utils.DateUtils;
import com.mobon.billing.dump.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;
import me.saro.commons.Files;

/**
 * @FileName : ABTestFileReaderImpl.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2.
 * @Author dkchoi
 * @Comment : 처리할 ABTEST파일을 찾아 라인별로 읽어 Summary시키는 서비스.
 */
@Slf4j
@Service("abTestFileService")
public class ABTestFileServiceImpl implements DumpFileService {

	@Value("${MOBON_LOG_DIR}")
	private String ABTESTLogPath;

	@Value("${MOBON_RETRY_DIR}")
	private String ABTESTRetryPath;

	@Value("${MOBON_RETRY_SUCC_DIR}")
	private String ABTESTRetrySuccPath;

	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.abtest.ABTestFileReaderService#FileReadProcess(java.lang.String)
	 */
	public Map<String, Object> FileReadProcess(String AnHourAgo) throws Exception {

		File dir = new File(ABTESTLogPath);

		if(!dir.exists() && !dir.isDirectory()) {
			throw new Exception("Not Found Log Directory[" + ABTESTLogPath + "]");
		}

		File[] files = dir.listFiles();
		ABTestSummary stats = new ABTestSummary();

		for (File file : files) {
			if(!file.isFile())
				continue;

			if(!(file.getName().startsWith(GlobalConstants.MOBON_CLICKVIEW_FILE_NAME_PREFIX)
					|| file.getName().startsWith(GlobalConstants.MOBON_CONVERSION_FILE_NAME_PREFIX)) ||
					!file.getName().endsWith(GlobalConstants.MOBON_FILE_EXTENSION) ||
					!file.getName().contains(AnHourAgo))		//형식이 맞이 않는 파일 제외
			{
				log.info("# search exception fileName : " + file.getName());
				continue;
			}

			log.info("# search fileName : " + file.getName());

			loadFile(file, file.getName(), stats);
		}

		return stats.getABTestData();
	}

	/**
	 * @Method Name : FileWriteProcess
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 데이터 등록 오류 시 해당 데이터를 파일로 남기는 서비스.
	 * @param
	 * @return Map<String, Object> 재처리 파일 데이터
	 */
	public Map<String, Object> RetryFileReadProcess() throws Exception {

		File dir = new File(ABTESTRetryPath);

		if(!dir.exists() && !dir.isDirectory())
			return null;

		File[] files = dir.listFiles();
		ABTestSummary stats = new ABTestSummary();

		for (File file : files) {
			if(!file.isFile())
				continue;

			log.info("$ search retry fileName : " + file.getName());

			retryLoadFile(file, stats);

		}

		return stats.getABTestData();

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
				log.info("### Make Retry File Error ");
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
		Files.listFilesStream(ABTESTRetrySuccPath)
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
	private void loadFile(File file, String fileName, ABTestSummary stats) throws IOException {

		if(file == null)
			return;


		try(BufferedReader buffReader = new BufferedReader(new FileReader(file))) {

			String line = null;
			int counter = 0;

			while((line = buffReader.readLine()) != null) {
				if(++counter%1000000 == 0)
					log.info("# Read File Line.. " + counter);

				if(StringUtils.isEmpty(line))
					continue;

				put(stats, line, fileName);
			}

			if(++counter%1000000 != 0)
				log.info("# Read File Line.. " + counter);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	/**
	 * @Method Name : put
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 전달받은 라인을 ABTestFileDataVO를 통해 정제하여 Summary객체를 통해 합산.
	 * @param stats
	 * @param line
	 * @param fileName
	 * @return
	 */
	private void put(ABTestSummary stats, String line, String fileName) {

		stats.setABTestData(new MobonFileDataVO(line, fileName));

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
			if(((List)object).get(0) instanceof ABComStats) {

				log.info("### Make ABComStats File Start ###");
				List<ABComStats> retryABComStats = (List<ABComStats>) object;

				for(ABComStats entry : retryABComStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABCOMSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABComStats File End ###");

			} else if(((List)object).get(0) instanceof ABParStats) {

				log.info("### Make ABParStats File Start ###");
				List<ABParStats> retryABParStats = (List<ABParStats>) object;

				for(ABParStats entry : retryABParStats) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABPARSTATS_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABParStats File End ###");

			} else if(((List)object).get(0) instanceof ABAdverStatsWeb) {

				log.info("### Make ABAdverStatsWeb File Start ###");
				List<ABAdverStatsWeb> retryABAdverStatsWeb = (List<ABAdverStatsWeb>) object;

				for(ABAdverStatsWeb entry : retryABAdverStatsWeb) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABAdverStatsWeb File End ###");

			} else if(((List)object).get(0) instanceof ABAdverStatsMobile) {

				log.info("### Make ABAdverStatsMobile File Start ###");
				List<ABAdverStatsMobile> retryABAdverStatsMobile = (List<ABAdverStatsMobile>) object;

				for(ABAdverStatsMobile entry : retryABAdverStatsMobile) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABAdverStatsMobile File End ###");

			} else if(((List)object).get(0) instanceof ABComFrameSize) {

				log.info("### Make ABComFrameSize File Start ###");
				List<ABComFrameSize> retryABComFrameSize = (List<ABComFrameSize>) object;

				for(ABComFrameSize entry : retryABComFrameSize) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABComFrameSize File End ###");

			} else if(((List)object).get(0) instanceof ABCombiFrameSize) {

				log.info("### Make ABCombiFrameSize File Start ###");
				List<ABCombiFrameSize> retryABCombiFrameSize = (List<ABCombiFrameSize>) object;

				for(ABCombiFrameSize entry : retryABCombiFrameSize) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABCombiFrameSize File End ###");

			} else if(((List)object).get(0) instanceof ABFrameSize) {

				log.info("### Make ABFrameSize File Start ###");
				List<ABFrameSize> retryABFrameSize = (List<ABFrameSize>) object;

				for(ABFrameSize entry : retryABFrameSize) {
					String jsonString = CommonUtils.objectToJsonString(entry);
					FileUtils.appendStringToFile(ABTESTRetryPath, GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT + "_" + DateUtils.getAnHourAgo(), jsonString);
				}
				log.info("### Make ABFrameSize File End ###");

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
	private void retryLoadFile(File file, ABTestSummary stats) throws IOException {

		if(file == null)
			return;


		try(BufferedReader buffReader = new BufferedReader(new FileReader(file))) {

			String line = null;

			if(file.getName().startsWith(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABComStats abComStats = null;
					abComStats = mapper.readValue(line, ABComStats.class);

					stats.putAbComStatsMap(abComStats);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABPARSTATS_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABParStats abParStats = null;
					abParStats = mapper.readValue(line, ABParStats.class);

					stats.putAbParStatsMap(abParStats);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABAdverStatsWeb abAdverStatsWeb = null;
					abAdverStatsWeb = mapper.readValue(line, ABAdverStatsWeb.class);

					stats.putAbAdverStatsWebMap(abAdverStatsWeb);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABAdverStatsMobile abAdverStatsMobile = null;
					abAdverStatsMobile = mapper.readValue(line, ABAdverStatsMobile.class);

					stats.putAbAdverStatsMobileMap(abAdverStatsMobile);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABComFrameSize abComFrameSize = null;
					abComFrameSize = mapper.readValue(line, ABComFrameSize.class);

					stats.putAbComFrameSizeMap(abComFrameSize);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABCombiFrameSize abCombiFrameSize = null;
					abCombiFrameSize = mapper.readValue(line, ABCombiFrameSize.class);

					stats.putAbCombiFrameSizeMap(abCombiFrameSize);

				}

			} else if(file.getName().startsWith(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT)) {

				while((line = buffReader.readLine()) != null) {

					ObjectMapper mapper = new ObjectMapper();
					ABFrameSize abFrameSize = null;
					abFrameSize = mapper.readValue(line, ABFrameSize.class);

					stats.putAbFrameSizeMap(abFrameSize);

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
			if(((List)object).get(0) instanceof ABComStats) {
				result = GlobalConstants.ABCOMSTATS_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABParStats) {
				result = GlobalConstants.ABPARSTATS_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABAdverStatsWeb) {
				result = GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABAdverStatsMobile) {
				result = GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABComFrameSize) {
				result = GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABCombiFrameSize) {
				result = GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT;
			} else if(((List)object).get(0) instanceof ABFrameSize) {
				result = GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT;
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

		File dir = new File(ABTESTRetryPath);

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
//    	String[] retryCountArr = file.getName().split("_");
//    	String retryCountStr = retryCountArr[retryCountArr.length-1];
//    	int retryCount = 0;
//
//    	if (retryCountStr.length() < 2) {
//    		retryCount = Integer.parseInt(retryCountStr);
//    	}

//    	if (retryCount == 5) {
		String retryFailPath = ABTESTRetryPath+"/fail/";
		file.renameTo(new File(retryFailPath+ file.getName()));
//    		return;
//    	}

//    	StringBuffer reNameFile = new StringBuffer().append(file.getName()).append("_").append(retryCount+1);

//    	file.renameTo(new File(ABTESTRetryPath + reNameFile.toString()));

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
		file.renameTo(new File(ABTESTRetrySuccPath + file.getName()));
	}

}
