package com.mobon.billing.core.service;

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
import com.mobon.billing.core.service.dao.ConvDataDao;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ConvDataToMariaDB {
	private static final Logger	logger	= LoggerFactory.getLogger(ConvDataToMariaDB.class);

	@Autowired
	ConvDataDao		convDataDao;
	
	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public boolean intoMariaConvDataV3(String _id, List<ConvData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		
		if (aggregateList != null && aggregateList.size() != 0) {
			if (toMongodb) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.conv_info, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("chking fail ConvData fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
				result = true;
			} else {
				result = convDataDao.transectionRuningV3( aggregateList );
				logger.info("succ intoMariaConvData _id - {}, size - {}, during - {}", _id, aggregateList.size(), System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<ConvData>> makeFlushMap(List<ConvData> aggregateList){
		return null;
	}
	
}
