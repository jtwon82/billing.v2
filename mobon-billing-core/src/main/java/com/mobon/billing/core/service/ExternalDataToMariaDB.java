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
import com.adgather.constants.old.GlobalConstants;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.ExternalDataDao;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ExternalDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ExternalDataToMariaDB.class);

	@Autowired
	private ExternalDataDao		externalDataDao;

	@Value("${log.path}")
	private String	logPath;
	
	@Value("${externaldata.except.advertisterid:''}")
	private String	externaldataExceptAdvertisterId;

	

//	public void sp_external_dataX(ExternalInfoData item) {
//		ArrayList<ExternalInfoData> list = new ArrayList<ExternalInfoData>();
//		list.add(item);
//		HashMap<String, ArrayList<ExternalInfoData>> flushMap = makeFlushMap(list);
//		externalDataDao.transectionRunning(flushMap);
//	}

	public boolean intoMariaExternalDataV3(String _id, List<ExternalInfoData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			HashMap<String, ArrayList<ExternalInfoData>> flushMap = makeFlushMap(aggregateList);
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					//logger.info("fail insert into maria ");
					try {
						ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.external_info, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ExternalData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					//logger.info("insert into maria Start");
				
					result = externalDataDao.transectionRuningV2(flushMap);
				logger.info("succ intoMariaClickViewData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		
		return result;
	}
	public HashMap<String, ArrayList<ExternalInfoData>> makeFlushMap(List<ExternalInfoData> aggregateList){
		HashMap<String, ArrayList<ExternalInfoData>> flushMap = new HashMap();
		for (ExternalInfoData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd()==null || vo.getAdType()==null || vo.getAdGubun()==null ) {
						logger.error("Missing required, vo - {}", vo.toString());
						continue;
					}
					
					if( vo.getProduct()==null ) {
						vo.setProduct("b");
					}

					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					// 포인트 아닌데 _point 키워드가 붙은것은 테이블의 위치가 다르기때문.
					if ( vo.getDumpType().equals(GlobalConstants.EXTERNALCHARGE) ){
						if( vo.getMediaSite()>0 /* && !"".equals(vo.getZoneId()) */ ){
							
							if("RCV".equals(vo.getType()) &&
									(vo.getTotalCall() != 0 || vo.getViewCntMobon() != 0 || vo.getViewCnt() != 0 || vo.getClickCnt() != 0 || vo.getPoint() != 0 || vo.getPassbackCnt() != 0)) {
								add(flushMap, "update_EXL_ITL_RCV_STATS", vo);
								
							}else if("SEND".equals(vo.getType()) &&
									(vo.getViewCnt() != 0 || vo.getClickCnt() != 0 || vo.getPassbackCnt() != 0)) {
								add(flushMap, "update_EXL_ITL_SEND_STATS", vo);
								
							}else if( "PV".equals(vo.getType()) ) {
								//add(flushMap, "sp_external_point_NEW_PV", vo);
								
							}
							else if( "C".equals(vo.getType()) ){
								//add(flushMap, "sp_external_point_NEW_C", vo);
								
							}
							else{
								logger.info("externalCharge else - {}", vo);
							}
							
						} else {
							logger.error("chking data - {}", vo);
						}
					}
					else if( vo.getDumpType().equals(GlobalConstants.EXTERNALBATCH) ){
						
						logger.debug("externalBatch vo - {}", vo);
						
						// 현재시간이 아닌디비가 들어오면 일자별에 같이 저장하자
						//if( !String.format("%s_%s", vo.getYyyymmdd(), vo.getHh()).equals( DateUtils.getDate("yyyyMMdd_HH") ) ) {
						//	add(flushMap, "sp_external_NEW_DAYLY", vo);
						//}

						// 190729 통계와 개편되는 테이블에 인서트
						// MOB_CAMP_MEDIA_STATS
						if (vo.getViewCnt() != 0 || vo.getViewCnt3() != 0 || vo.getClickCnt() != 0 || vo.getPoint() != 0 || vo.getMpoint() != 0) {
							add(flushMap, "update_CAMP_MEDIA_STATS", vo);
						}
						//add(flushMap, "sp_external_point_NEW_step2", vo);
						
						if(!externaldataExceptAdvertisterId.contains(vo.getAdvertiserId()) &&
								(vo.getTotalCall() != 0 || vo.getViewCntMobon() != 0 || vo.getViewCnt() != 0 || vo.getClickCnt() != 0 || vo.getPoint() != 0 || vo.getPassbackCnt() != 0)) {
							add(flushMap, "update_EXL_ITL_RCV_STATS", vo);
						}
						
						if(vo.getPoint()>0 || vo.getMpoint()>0 ){
							if(vo.isbHandlingStatsPointMobon()) {
								//add(flushMap, "sp_external_point_NEW", vo);
							} else {
								//add(flushMap, "sp_external_point_billing_NEW", vo);
							}
						}
					}
					else if( vo.getDumpType().equals(GlobalConstants.EXTERNALSSP) ){
						add(flushMap, "update_SSP_ITL_STATS", vo);
					}
					else if( vo.getDumpType().equals(GlobalConstants.EXTERNALVIEWCNTCHARGE) ){
						vo.setViewCnt3(vo.getViewCnt());

						add(flushMap, "update_CAMP_MEDIA_STATS", vo);

						add(flushMap, "update_EXL_ITL_RCV_STATS", vo);
					}
					else{
						logger.error("Missing required, vo - {}", vo);
					}
				}
			}catch(Exception e){
				logger.error("err item - {}", vo, e);
			}
		}
		
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<ExternalInfoData>> flushMap, String key, ExternalInfoData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<ExternalInfoData> l = flushMap.get(key);
		l.add(vo);
	}
}
