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
import com.mobon.billing.core.service.dao.ActionPcodeDataDao;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ActionPcodeDataToMariaDB {
	
	private static final Logger logger = LoggerFactory.getLogger(ActionPcodeDataToMariaDB.class);
	
	@Autowired
	private ActionPcodeDataDao			ActionPcodeDataDao;

	@Value("${log.path}")
	private String	logPath;
	
	
	public boolean intoMariaActionPcodeDataV3(String _id, List<ActionLogData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		HashMap<String, ArrayList<ActionLogData>> flushMap = makeFlushMap(aggregateList);

		if (aggregateList != null) {
			if (toMongodb) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.action_pcode_data, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("chking fail ActionPcodeData fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
				result = true;
			} else {
				result = ActionPcodeDataDao.transectionRuningV2( flushMap );
				logger.info("succ intoMariaActionPcodeData during - {}", System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}
		return result;
	}

	public HashMap<String, ArrayList<ActionLogData>> makeFlushMap(List<ActionLogData> aggregateList){
		HashMap<String, ArrayList<ActionLogData>> flushMap = new HashMap<String, ArrayList<ActionLogData>>();
		
		for (ActionLogData vo : aggregateList) {
			try {
				if (vo != null) {
					if( "mba_no_script".equals(vo.getProduct()) ) {
						vo.setProduct(G.MBW);
					}
					if(vo.isbHandlingStatsPointMobon()) {
						//add(flushMap, "sp_action_data_NEW", vo);
					} else {
						if(vo.getAdvertiserId()==null || "".equals(vo.getAdvertiserId())) {
							logger.info("vo:{}", vo);
						}
						else {
							add(flushMap, "sp_action_pcode_data_billing_NEW", vo);
						}
					}
				}
			}catch(Exception e){
				logger.error("err msg - {}, item - {}", e.getMessage(), vo);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<ActionLogData>> flushMap, String key, ActionLogData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList<ActionLogData>());
		}
		ArrayList<ActionLogData> l = flushMap.get(key);
		l.add(vo);
	}
}
