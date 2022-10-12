package com.mobon.billing.viewclicklog.service;

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
import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.viewclicklog.service.dao.ConversionVoDao;
import com.mobon.billing.viewclicklog.util.ConsumerFileUtils;
import com.mobon.billing.viewclicklog.util.DateUtils;

@Service
public class ConversionVoToClickHouse {

	private static final Logger	logger = LoggerFactory.getLogger(ConversionVoToClickHouse.class);
	
	@Value("${log.path}")
	private String logPath;

	@Autowired
	private ConversionVoDao ConversionVoDao;

	/**
	 * ConversionVo into ClickHouse 처리 서비스
	 */
	public boolean intoClickHouseConversionVoV3(String _id, List<ConversionVo> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		
		if (aggregateList != null && aggregateList.size() != 0) {
			if (toMongodb) {
				// insertIntoError 파일로 저장
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.ConversionVo, aggregateList);
				} catch (IOException e) {
					logger.error("err - ", e);
				}
				
				logger.info("chking fail ConversionVo fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
				result = true;
			} else {
				// ClickHouse에 저장
				result = ConversionVoDao.transectionRuningV3(aggregateList);
				logger.info("succ intoClickHouseConversionVoV3 _id - {}, size - {}, during - {}", _id, aggregateList.size(), System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}

		return result;
	}

	/**
	 * sqlMapper 별로 리스트 정렬
	 */
	public HashMap<String, ArrayList<ConversionVo>> makeFlushMap(List<ConversionVo> aggregateList){
		return null;
	}
	
}
