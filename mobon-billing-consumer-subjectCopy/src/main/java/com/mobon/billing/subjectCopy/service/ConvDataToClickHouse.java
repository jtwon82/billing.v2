package com.mobon.billing.subjectCopy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.subjectCopy.service.dao.ClickHouseDao;
import com.mobon.billing.subjectCopy.service.dao.SelectDao;
import com.mobon.billing.subjectCopy.vo.ConversionPolling;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ConvDataToClickHouse {
	private static final Logger logger = LoggerFactory.getLogger(ConvDataToClickHouse.class);
	
	@Autowired
	private ClickHouseDao clickhouseDao;
	
	@Autowired
	private SelectDao selectDao;
	
	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public boolean intoClickHouseConvDataV3(String _id, List<ConversionPolling> aggregateList, boolean toClickHouse) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		
		

		if (aggregateList != null && aggregateList.size() != 0) {
			if (toClickHouse) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.SuccConversion, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("chking fail SubjectCopyConversionData fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
				result = true;
			} else {
				Map<String, List<ConversionPolling>> data = new HashMap<String , List<ConversionPolling>>();
				//KpiNo 가지고 오는 부분 
				List<ConversionPolling> convList = new ArrayList<ConversionPolling>();
				for (ConversionPolling vo : aggregateList) {
					BaseCVData item = new BaseCVData();
					item.setSiteCode(vo.getSiteCode());
					item = selectDao.selectKpiInfo(item);
					if (item != null) {
						vo.setKpiNo(item.getKpiNo());						
					}
					convList.add(vo);
				}
				
				data.put(_id, convList);
				result = clickhouseDao.transectionRuningConversion( data );
				logger.info("succ intoClickHouseConvDataV3 _id - {}, size - {}, during - {}", _id, aggregateList.size(), System.currentTimeMillis() - start_millis );
				if (!result) {
					try {
						ConsumerFileUtils.writeLine( logPath +"retryOk/", String.format("DB_insertIntoError_Conversion_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.SuccConversion, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
				}
			}
		} else {
			result = true;
		}
		return result;
	}

}
