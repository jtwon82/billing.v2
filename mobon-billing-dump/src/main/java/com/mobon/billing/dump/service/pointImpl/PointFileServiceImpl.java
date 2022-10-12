package com.mobon.billing.dump.service.pointImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.file.point.PointSummary;
import com.mobon.billing.dump.file.point.data.PointData;
import com.mobon.billing.dump.repository.PointStatsRepository;
import com.mobon.billing.dump.service.PointFileService;
import com.mobon.billing.dump.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class PointFileServiceImpl implements PointFileService{

	@Autowired
	PointStatsRepository pointStatsRepository;
	
	@Value("${POINT_RETRY_LOG_DIR}")
	private String POINTRetryLogPath;
	
	@Value("${POINT_RESULT_LOG_DIR}")
	private String POINTResultLogPath;
	
	@Override
	public Map<String, PointDataStats> loadFile(File file, PointSummary stats) {
		Map<String, PointDataStats> resultData = new HashMap<String, PointDataStats>();
		ArrayList<PointData> pointDataList = new ArrayList<PointData>();
		if (file == null) {
			return new HashMap<String, PointDataStats>();
		}
		
		FileReader reader = null;
		BufferedReader buffReader = null;
		
		try {
			reader = new FileReader(file);
			buffReader = new BufferedReader(reader);
			String line = null;
			
			while ((line = buffReader.readLine()) != null) {
				
				String [] row = line.split("\t");
				String lineData = row[2];
				PointData pointDataVo = new PointData(lineData);
				pointDataList.add(pointDataVo);				
			}

			log.info("#### PointLogData Low #### " + pointDataList.size());
			
			resultData = stats.PointDataListToMap(pointDataList);
						
		} catch (Exception e) {
			log.error("####Point loadFile fail#### " +e);
			
		} finally {
			try {
				reader.close();
				buffReader.close();
			} catch (Exception e) {
				log.error("####BufferReader doesn't Close ####");
			}
		}
		
		
		return resultData;
	}
	
	@Override
	public Map<String, PointDataStats> RetryLoadFile(File[] files, PointSummary stats) {
		List <PointData> pointVoList = new ArrayList<PointData>();
		Map<String, PointDataStats> resultData = new HashMap<String, PointDataStats>();
		for (File file : files) {
			FileReader reader;
			BufferedReader buffReader;
			try {
				reader = new FileReader(file);
				buffReader = new BufferedReader(reader);
				String line = null;
				
				while ((line = buffReader.readLine()) != null) {

					String [] row = line.split("\t");
					String lineData = row[2];
					PointData pointDataVo = new PointData(lineData);

					pointVoList.add(pointDataVo);			
				}
				log.info("#### FileName : "+ file.getName()+" ####");
				log.info("#### FileSize : "+ pointVoList.size()+" ####");
			} catch (Exception  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		resultData = stats.PointDataListToMap(pointVoList);
		
		return resultData;
	}


	@Override
	public void makeRetryFile(File retryFile) {
		
		String retryFileName = retryFile.getName();
		StringBuffer sb = new StringBuffer()
				.append(POINTRetryLogPath)
				.append("Retry_")
				.append(retryFileName);
		if (FileUtils.makeDir(POINTRetryLogPath)) {
			log.debug("####Create Retry File Directory ####");		
		}
		
		File copyOriginFile = new File(sb.toString());
		
		
		try {
			FileInputStream fis = new FileInputStream(retryFile);
			FileOutputStream fos = new FileOutputStream(copyOriginFile);
			
			int fileByte = 0;
			
			while ((fileByte = fis.read()) != -1) {
				fos.write(fileByte);
			}
			fis.close();
			fos.close();			
		} catch(Exception e) {
			log.error("#### Retry File Create Error #### " + e);
		}
		
	}

	@Override
	public void makeDiffResultFile(JSONArray diffSiteCodeList) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cal2 = Calendar.getInstance();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String yesterDay = format.format(cal.getTime());

		StringBuffer sbFileName = new StringBuffer()
				.append("kafka-consumer.logging.log.topic")
				.append(yesterDay)
				.append(".log");

		if (FileUtils.makeDir(POINTResultLogPath)) {
			log.debug("#### Create Result File Directory ####");
		}

		try {
			StringBuffer sbComment = new StringBuffer();			
			for (int i = 0; i < diffSiteCodeList.size(); i++) {
				JSONObject comment = (JSONObject) diffSiteCodeList.get(i);				 
				sbComment.append(format2.format(cal2.getTime())).append("\t")
				.append("ClickViewData").append("\t")
				.append(comment.toString()).append("\n");					
			}
			FileOutputStream fOut = new FileOutputStream(new File(POINTResultLogPath+sbFileName));
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(sbComment.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			log.error("#### Diff File Write is fail ####");
		}
		
	}




}
