package com.mobon.billing.sample.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.util.old.DateUtils;
import com.mobon.billing.sample.model.UseridVo;
import com.mobon.billing.sample.service.dao.UseridVoDao;
import com.mobon.billing.sample.util.ConsumerFileUtils;

@Service
public class UseridVoToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(UseridVoToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private UseridVoDao			UseridVoDao;
	
	
	

	
	public boolean intoMariaUseridVoV3(String _id, List<UseridVo> aggregateList, boolean toMongodb) {
		boolean result = false;	
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<UseridVo>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), "UseridVo", aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("fail UseridVo fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = UseridVoDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaUseridVo _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<UseridVo>> makeFlushMap(List<UseridVo> aggregateList){
		HashMap<String, ArrayList<UseridVo>> flushMap = new HashMap();
		
		for (UseridVo vo : aggregateList) {
			try {
				if (vo != null) {
					
					// sqlMapper 세팅
					add(flushMap, "insertUseridStats", vo);
					
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<UseridVo>> flushMap, String key, UseridVo vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<UseridVo> l = flushMap.get(key);
		l.add(vo);
	}
	
}
