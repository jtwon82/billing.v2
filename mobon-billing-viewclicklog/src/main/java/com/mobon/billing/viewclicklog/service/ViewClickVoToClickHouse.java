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
import com.mobon.billing.model.v20.ViewClickVo;
import com.mobon.billing.viewclicklog.service.dao.ViewClickVoDao;
import com.mobon.billing.viewclicklog.util.ConsumerFileUtils;
import com.mobon.billing.viewclicklog.util.DateUtils;

@Service
public class ViewClickVoToClickHouse {

	private static final Logger logger = LoggerFactory.getLogger(ViewClickVoToClickHouse.class);

	@Value("${log.path}")
	private String logPath;
	
	@Autowired
	private ViewClickVoDao ViewClickVoDao;

	/**
	 * ViewClickVo into ClickHouse 처리 서비스
	 */
	public boolean intoClickHouseViewClickVoV3(String _id, List<ViewClickVo> aggregateList, boolean toMongodb) {
		boolean result = false;	
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			HashMap<String, ArrayList<ViewClickVo>> flushMap = makeFlushMap(aggregateList);
			
			if (flushMap.keySet().size() != 0) {
				if (toMongodb) {
					// insertIntoError 파일로 저장
					try {
						ConsumerFileUtils.writeLine(logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.ViewClickVo, aggregateList);
					} catch (IOException e) {
						logger.error("err - ", e);
					}
					
					logger.info("fail ViewClickVo fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
					
				} else {
					// ClickHouse에 저장
					result = ViewClickVoDao.transectionRuningV2(flushMap);
					logger.info("succ intoClickHouseViewClickVoV3 _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}

		return result;
	}

	/**
	 * sqlMapper 별로 리스트 정렬
	 */
	public HashMap<String, ArrayList<ViewClickVo>> makeFlushMap(List<ViewClickVo> aggregateList){
		HashMap<String, ArrayList<ViewClickVo>> flushMap = new HashMap<>();
		
		for (ViewClickVo vo : aggregateList) {
			try {
				if (vo != null) {
					// sqlMapper 세팅
					add(flushMap, "insertViewClickLog", vo);
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}

		return flushMap;
	}

	private void add(HashMap<String, ArrayList<ViewClickVo>> flushMap, String key, ViewClickVo vo) {
		if (flushMap.get(key) == null) {
			flushMap.put(key, new ArrayList<>());
		}
		ArrayList<ViewClickVo> l = flushMap.get(key);
		l.add(vo);
	}
	
}
