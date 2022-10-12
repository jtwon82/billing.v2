package com.mobon.billing.subjectCopy.service;

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
import com.mobon.billing.subjectCopy.service.dao.ClickHouseDao;
import com.mobon.billing.subjectCopy.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ClickViewDataToClickHouse {
	private static final Logger logger = LoggerFactory.getLogger(ClickViewDataToClickHouse.class);

	@Autowired
	private SelectDao  selectDao;

	@Autowired
	private ClickHouseDao clickhouseDao;
	

	@Value("${log.path}")
	private String	logPath;


	public boolean intoClickHouseClickViewData(String _id, List<BaseCVData> aggregateList, boolean toClickHouseDb) {
		boolean result = false; 
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			HashMap<String , ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toClickHouseDb) {
					try {
						ConsumerFileUtils.writeLine( logPath +"retryfail/", String.format("insertIntoError_intoClick_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.SubjectCopyClickView, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("fail SubjectCopyClickViewData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = clickhouseDao.transectionRuningV2( flushMap );
					logger.info("succ SubjectCopyClickViewData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
					if (!result) {
						try {
							ConsumerFileUtils.writeLine( logPath +"retryOk/", String.format("DB_insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.SubjectCopyClickView, aggregateList);
						} catch (IOException e) {
							logger.error("err - {}", e);
						}
					}
				}
			} else {
				result = true;
			}
		}

		return result;
	}

	private HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList) {
		HashMap<String, ArrayList<BaseCVData>> flushMap = new HashMap();
		for (BaseCVData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd()==null || vo.getPlatform()==null || vo.getAdGubun()==null 
							|| vo.getSiteCode()==null || vo.getScriptNo()==0 || vo.getAdvertiserId()==null || vo.getType()==null) {
						logger.error("Missing required, vo - {}", vo.toString());
						continue;
					}

					if( StringUtils.isEmpty(vo.getProduct()) || StringUtils.isEmpty(vo.getScriptUserId()) ) {
						BaseCVData minfo = selectDao.selectMediaInfo(vo);
						if( minfo!=null ) {
							logger.error("map - {}", minfo);

							if( StringUtils.isEmpty(vo.getProduct()) ) {
								vo.setProduct( StringUtils.trimToNull2(minfo.getProduct()));	// 모비온에서 넘어온값으로 써야됨
								logger.error("chking vo - {}", vo);
							}
							if( StringUtils.isEmpty(vo.getScriptUserId()) ) {
								vo.setScriptUserId( StringUtils.trimToNull2(minfo.getScriptUserId())); //map.get("scriptUserId")) );
								logger.error("chking vo - {}", vo);
							}
						
						}
					}
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(selectDao.convertAdvrtsTpCode(vo.getAdGubun()));
					
					// KPINO 셋팅 
					 BaseCVData kpiInfo = selectDao.selectKpiInfo(vo);
					 if (kpiInfo != null) {
						 vo.setKpiNo(kpiInfo.getKpiNo());
					 }
					 
					add(flushMap, "Insert_Subject_Copy",vo);
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}



		return flushMap;
	}

	private void add(HashMap<String, ArrayList<BaseCVData>> flushMap, String key, BaseCVData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<BaseCVData> l = flushMap.get(key);
		logger.debug("s - {}, statYn - {}", vo.getScriptNo(), vo.getStatYn());
		if("N".equals(vo.getStatYn())) {
			if(key.indexOf("point")>0) {
				l.add(vo);
			}
		}
		else {
			l.add(vo);
		}
	}

}
