package com.mobon.billing.dump.service.apptrgtimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobon.billing.dump.domainmodel.apptrgt.AppTrgtAdverMediaStats;
import com.mobon.billing.dump.file.apptrgt.AppTrgtSummary;
import com.mobon.billing.dump.file.apptrgt.data.AppTrgtAdverMediaFileDataVO;
import com.mobon.billing.dump.repository.AppTrgtAdverMediaStatsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @FileName : 
 * @Project : 
 * @Date :  
 * @Author 
 * @Comment : 
 */
@Service
@Slf4j
public class AppTrgtFileProcessImpl {

	@Autowired
	AppTrgtAdverMediaStatsRepository appTrgtAdverMediaStatsRepository;

	/**
	 * @Method Name : loadFile
	 * @Date : 
	 * @Author : 
	 * @Comment : 
	 * @param 
	 * @return
	 * @throws IOException
	 */
	public Map<String, AppTrgtAdverMediaStats> loadFile(File file, AppTrgtSummary stats) throws IOException {

		Map<String, AppTrgtAdverMediaStats> resultData = new HashMap<String, AppTrgtAdverMediaStats>();
		
		if(file == null)
			return new HashMap<String, AppTrgtAdverMediaStats>();

		FileReader reader  = null;
		BufferedReader buffReader = null;
		
		try {
			reader = new FileReader(file);
			buffReader = new BufferedReader(reader);
			String line = null;
			int counter = 0;
			
			while((line = buffReader.readLine()) != null) {
				if(++counter%1000000 == 0)
					log.info("# Read File Line.. " + counter);
				
				if(StringUtils.isEmpty(line))
					continue;
				
				String [] row = line.split("\t");
				String lineDataSub = row[2];
				
				resultData = stats.AppTrgtAdverMediaDataset(new AppTrgtAdverMediaFileDataVO(lineDataSub)); //put(stats, lineDataSub, fileName);
			}
			
			if(++counter%1000000 != 0)
				log.info("# Read File Line.. " + counter);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(buffReader != null)	try {	buffReader.close();	buffReader = null;	} catch (Exception e2) {}
			if(reader != null)	try {	reader.close();	reader = null;	} catch (Exception e2) {}
		}
		
		return resultData;
	}


	
	
	@Transactional
	public void SaveAppTrgtData(Map<String, AppTrgtAdverMediaStats> resultData) {
		
		// APP_TRGT_ADVER_MEDIA_STATS 테이블에 변경되는 일자를 저장해 놓기 위한 HashSet
		Set statsDttmSet = new HashSet();

		
		List<AppTrgtAdverMediaStats> saveEntities = new ArrayList<AppTrgtAdverMediaStats>(); 
		
		resultData.forEach((summaryKey, summaryAppAdverMediaStats) -> {
        	saveEntities.add(summaryAppAdverMediaStats);
        	statsDttmSet.add(summaryAppAdverMediaStats.getId().getStatsDttm());
        });
        
        appTrgtAdverMediaStatsRepository.saveAll(saveEntities);
        
		log.info("# APP_TRGT_ADVER_MEDIA_STATS Insert Update End");
		

		
		
		
		// 변경된 일자는 재작업.
		Iterator it = statsDttmSet.iterator();
		while(it.hasNext()){
		
			int statsDttm = (int)it.next();
			log.info("# STATS_DTTM == " + statsDttm);
			SaveAppTrgtSubTable(statsDttm);
		
		}
		
	}

	
	@Transactional
	public void SaveAppTrgtSubTable(int statsDttm) {
		
		int resultAdver = appTrgtAdverMediaStatsRepository.insertAppTrgtAdverStats(statsDttm);
		
		log.info("# APP_TRGT_ADVER_STATS Insert Update End = " + resultAdver);
		
		int resultDay = appTrgtAdverMediaStatsRepository.insertAppTrgtDayStats(statsDttm);
		
		log.info("# APP_TRGT_DAY_STATS Insert Update End = " + resultDay);
		
		int resultMedia = appTrgtAdverMediaStatsRepository.insertAppTrgtMediaStats(statsDttm);
		
		log.info("# APP_TRGT_MEDIA_STATS Insert Update End = " + resultMedia);
		
	}
	
}
