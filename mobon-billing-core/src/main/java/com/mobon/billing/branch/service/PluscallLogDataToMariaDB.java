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
import com.adgather.util.old.StringUtils;
import com.mobon.billing.branch.service.dao.PluscallLogDataDao;
import com.mobon.billing.model.v15.PluscallLogData;
import com.mobon.billing.util.ConsumerFileUtils;

/**
 * PluscallLogDataToMariaDB
 * 플러스콜 유효콜 관련 데이터 정재 클래스
 * 
 * @author  : sjpark3
 * @since   : 2022-01-04
 */
@Service
public class PluscallLogDataToMariaDB {
	@Autowired
	private PluscallLogDataDao pluscallLogDataDao;
	
	@Value("${log.path}")
	private String logPath;
	
	private static final Logger logger = LoggerFactory.getLogger(PluscallLogDataToMariaDB.class);
	
	/**
     * intoMariaPluscallLogDataV3
     * mariaDB 인서트 메소드
     * 
     * @author  : sjpark3
     * @since   : 2022-01-04
     */
	public boolean intoMariaPluscallLogDataV3(String _id, List<PluscallLogData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		HashMap<String, ArrayList<PluscallLogData>> flushMap = makeFlushMap(aggregateList);
		
		if (aggregateList != null) {
			if (toMongodb) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.PluscallLogData, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("chking fail PluscallLogData fileWriteOk flushMap.keySet() - {}", aggregateList);
				result = true;
			} else {
				result = pluscallLogDataDao.transectionRuningV2( flushMap );
				
				logger.info("succ intoMariaPluscallLogDataV3 during - {}", System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}
		return result;
	}
	
	/**
     * makeFlushMap
     * 데이터 정재 메소드
     * 
     * @author  : sjpark3
     * @since   : 2022-01-04
     */
	public HashMap<String, ArrayList<PluscallLogData>> makeFlushMap(List<PluscallLogData> aggregateList){
		HashMap<String, ArrayList<PluscallLogData>> flushMap = new HashMap<String, ArrayList<PluscallLogData>>();
		
		for (PluscallLogData vo : aggregateList) {
			try {
				logger.debug("PluscallLogData {}", vo);
				
				if (vo != null) {
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));
					
					add(flushMap, "insertPluscallLogData_billing", vo);
				}
			} catch (Exception e) {
				logger.error("err msg - {}, item - {}", e.getMessage(), vo);
			}
		}
		return flushMap;
	}

	/**
     * makeFlushMap
     * Map에 실행 쿼리 추가하는 메소드
     * 
     * @author  : sjpark3
     * @since   : 2022-01-04
     */
	private void add(HashMap<String, ArrayList<PluscallLogData>> flushMap, String key, PluscallLogData vo) {
		if (flushMap.get(key)==null) {
			flushMap.put(key, new ArrayList<PluscallLogData>());
		}
		
		ArrayList<PluscallLogData> l = flushMap.get(key);
		l.add(vo);
	}
	
}
