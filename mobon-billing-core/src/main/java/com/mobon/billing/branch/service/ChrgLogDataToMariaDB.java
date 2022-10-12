package com.mobon.billing.branch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branch.service.dao.ChrgLogDataDao;
import com.mobon.billing.model.v15.ChrgLogData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ChrgLogDataToMariaDB {
	
	private static final Logger logger = LoggerFactory.getLogger(ChrgLogDataToMariaDB.class);

	@Value("${log.path}")
	private String logPath;
	
	@Autowired
	private ChrgLogDataDao chrgLogDataDao;
	
	public boolean intoMariaChrgLogDataV3(String _id, List<ChrgLogData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		HashMap<String, ArrayList<ChrgLogData>> flushMap = makeFlushMap(aggregateList);
		
		if (aggregateList != null) {
			if (toMongodb) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.ChrgLogData, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("chking fail ChrgLogData fileWriteOk flushMap.keySet() - {}", aggregateList);
				result = true;
			} else {
				result = chrgLogDataDao.transectionRuningV2( flushMap );
				logger.info("succ intoMariaChrgLogDataV3 during - {}", System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<ChrgLogData>> makeFlushMap(List<ChrgLogData> aggregateList){
		HashMap<String, ArrayList<ChrgLogData>> flushMap = new HashMap<String, ArrayList<ChrgLogData>>();
		
		for (ChrgLogData vo : aggregateList) {
			try {
				logger.debug("ChrgLogData {}", vo);
				
				if (vo != null) {
					add(flushMap, "inserChrgLog_billing", vo);
				}
			} catch (Exception e) {
				logger.error("err msg - {}, item - {}", e.getMessage(), vo);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<ChrgLogData>> flushMap, String key, ChrgLogData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList<ChrgLogData>());
		}
		ArrayList<ChrgLogData> l = flushMap.get(key);
		l.add(vo);
	}
	
}
