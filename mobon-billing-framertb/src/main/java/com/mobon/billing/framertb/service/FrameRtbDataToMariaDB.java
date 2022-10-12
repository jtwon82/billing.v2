package com.mobon.billing.framertb.service;

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
import com.mobon.billing.framertb.service.dao.FrameRtbDataDao;
import com.mobon.billing.framertb.service.dao.SelectDao;
import com.mobon.billing.model.v15.FrameRtbData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class FrameRtbDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(FrameRtbDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private FrameRtbDataDao			FrameRtbDataDao;
	

	
	public boolean intoMariaFrameRtbDataV3(String _id, List<FrameRtbData> aggregateList, boolean toMongodb, String dataType) {
		boolean result = false;	
		long start_millis = System.currentTimeMillis();
		
		if (aggregateList != null) {
			
			HashMap<String, ArrayList<FrameRtbData>> flushMap = makeFlushMap(aggregateList, dataType);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.framertb_info, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("fail FrameRtbData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = FrameRtbDataDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaFrameRtbData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<FrameRtbData>> makeFlushMap(List<FrameRtbData> aggregateList, String dataType){
		HashMap<String, ArrayList<FrameRtbData>> flushMap = new HashMap();
		
		for (FrameRtbData vo : aggregateList) {
			try {
				if (vo != null) {
					String stateName = "";
					if("frmeCycleLog".equals(dataType)) {
						stateName = "insertFrameCycleLog";
					} else if ("frmeDayStats".equals(dataType)) {
						stateName = "insertFrameDayStats";
					} else if("frmeTrnLog".equals(dataType)) {
						stateName = "insertFrameTrnLog";
					} else if("frmeCombiDayStats".equals(dataType)) {
						stateName = "insertFrameCombiDayStats";
					} else if("frmeAdverDayStats".equals(dataType)) {
						stateName = "insertFrameAdverDayStats";
					} else if("frmeAdverDayAbStats".equals(dataType)) {
						stateName = "insertFrameAdverDayAbStats";
					} else if ("frmeFrameSizeStats".equals(dataType)) {
						stateName = "insertFrameSizeDataStats";
					} else if ("frmeFrameActionLog".equals(dataType)) {
						stateName = "insertFrameActionLog";
					} else if("frmeMediaAdverStats".equals(dataType)) {
						if("W02".equals(vo.getBnrCode())) {
							stateName = "insertFrameMediaAdverStats_02";
						} else if("W06".equals(vo.getBnrCode())) {
							stateName = "insertFrameMediaAdverStats_06";
						} else if("W12".equals(vo.getBnrCode())) {
							stateName = "insertFrameMediaAdverStats_12";
						}
					} else if ("frameAdverPrdtCtgrDayStats".equals(dataType)) {
						stateName = "insertFrmeAdverCtgrDayStats";
					} else if ("frameKaistCombiDayStats".equals(dataType)) {
						stateName = "insertFrameKaistCombiDayStats";
					} else if ("frmeCodeStats".equals(dataType)) {
						stateName = "insertFrameCodeStats";
					} else if ("frmeCtgrDayStats".equals(dataType)) {
						stateName = "insertFrmeCtgrDayStats";
					} else if ("frmeDayAbStats".equals(dataType)) {
						stateName = "insertFrmeDayAbStats";
					}
					
					// sqlMapper μΈν
					add(flushMap, stateName, vo);
					
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<FrameRtbData>> flushMap, String key, FrameRtbData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<FrameRtbData> l = flushMap.get(key);
		l.add(vo);
	}
	
}
