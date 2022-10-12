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
import com.mobon.billing.branch.service.dao.AppTargetDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.AppTargetData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class AppTargetDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(AppTargetDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private AppTargetDataDao	AppTargetDataDao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

	
	public boolean intoMariaAppTargetDataV3(String _id, List<AppTargetData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<AppTargetData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.AppTarget_info, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail AppTargetData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = AppTargetDataDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaAppTargetData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<AppTargetData>> makeFlushMap(List<AppTargetData> aggregateList){
		HashMap<String, ArrayList<AppTargetData>> flushMap = new HashMap<String, ArrayList<AppTargetData>>();
		
		for (AppTargetData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd()==null || vo.getPlatform()==null || vo.getAdGubun()==null 
						|| vo.getSiteCode()==null || vo.getScriptNo()==0 || vo.getAdvertiserId()==null || vo.getType()==null) {
						logger.error("Missing required, vo - {}", vo.toString());
						continue;
					}
					
					if( StringUtils.isEmpty(vo.getProduct()) || StringUtils.isEmpty(vo.getScriptUserId()) ) {
						ClickViewData minfo = selectDao.selectMediaInfo(vo);
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
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					// sqlMapper 세팅
					add(flushMap, "insertAppTargetData_Day", vo);
					add(flushMap, "insertAppTargetData_Adver", vo);
					add(flushMap, "insertAppTargetData_Media", vo);
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<AppTargetData>> flushMap, String key, AppTargetData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList<AppTargetData>());
		}
		ArrayList<AppTargetData> l = flushMap.get(key);
		l.add(vo);
	}
	
}
