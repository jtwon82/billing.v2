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
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.ClickViewPoint2DataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ClickViewPoint2DataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ClickViewPoint2DataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private ClickViewPoint2DataDao	clickViewDataPoint2Dao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

//	public void sp_clickviewx( ArrayList<BaseCVData> list ) {
//		HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(list);
//		clickViewDataDao.transectionRunning(flushMap);
//	}	
	
	public boolean intoMariaClickViewPoint2DataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_point2, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ClickViewPoint2Data fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
//					ContextHolder.setDataSourceType(DataSourceType.Dream);
					result = clickViewDataPoint2Dao.transectionRuningV2( flushMap );
					//logger.info("succ intoMariaClickViewData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList){
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
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));
					//if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(selectDao.selectMobonComCodeAdvrtsPrdtCode(vo.getProduct()));
					//if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(selectDao.convertAdvrtsTpCode(vo.getAdGubun()));

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					// 아이커버의경우 C 로넘어오는경우 C는 V와도 같다.
					logger.debug("vo - {}", vo);
					
					if (G.CLICK.equals(vo.getType())){
						if("14".equals(vo.getAdGubun())) {
							logger.info("adgubun_14	sc-{}, s-{}, point-{} ", vo.getSiteCode(), vo.getScriptNo(), vo.getPoint());
						}
						switch(vo.getProduct()){
							case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":
							case "04": case "c": case "scn":	// 쇼콘 0625 b로 잡히도록 수정했음
							case "05": case "m": case "nct": case "mct":	// 문맥
							case "06": case "p": case "pl": case "mpl":		// playlink
							case "07": case "t": case "pnt": case "mnt": case "mnw":	// 네이티브
							case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
							case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
								
								if(vo.getPoint()>0 || vo.getMpoint()>0 ){
									if( vo.isbHandlingStatsPointMobon() ) {
										//add(flushMap, "sp_banner_click_point_NEW", vo);
									} else {
										add(flushMap, "update_admember_chrg_point", vo);
									}
								}
								break;

							case "03": case "i": case "ico": case "ico_m": case "mbe":

								if(vo.getPoint()>0 || vo.getMpoint()>0 ){
									if(vo.isbHandlingStatsPointMobon()) {
										//add(flushMap, "sp_ico_view_point_NEW", vo);
									} else {
										add(flushMap, "update_admember_chrg_point", vo);
									}
								}
								break;
								
							default:
								logger.error("chking vo - {}", vo);
								break;
						}
						
					} else if (G.VIEW.equals(vo.getType())){
						switch(vo.getProduct()){
							case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":
							case "04": case "c": case "scn":	// 쇼콘
							case "05": case "m": case "nct": case "mct":	// 문맥
							case "06": case "p": case "pl": case "mpl":
							case "07": case "t": case "pnt": case "mnt": case "mnw":
							case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
							case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
								if( ( vo.getPoint()>0 || vo.getMpoint()>0) ){
									if(vo.isbHandlingStatsPointMobon()) {
										//add(flushMap, "sp_banner_click_point_NEW", vo);
									} else {
										add(flushMap, "update_admember_chrg_point", vo);
									}
								}
								break;
								
							case "02": case "s": case "sky": case "sky_m": case "mbb": 
								if(vo.getPoint()>0 || vo.getMpoint()>0 ){
									if(vo.isbHandlingStatsPointMobon()) {
										//add(flushMap, "sp_sky_view_point_NEW", vo);
									} else {
										add(flushMap, "update_admember_chrg_point", vo);
									}
								}
								break;
								
							case "03": case "i": case "ico": case "ico_m": case "mbe":
								if(vo.getPoint()>0 || vo.getMpoint()>0 ){
									if(vo.isbHandlingStatsPointMobon()) {
										//add(flushMap, "sp_ico_view_point_NEW", vo);
									} else {
										add(flushMap, "update_admember_chrg_point", vo);
									}
								}
								break;
								
							default:
								logger.error("chking vo - {}", vo);
								break;
						}
					} else {
						logger.info("type nothing vo - {}", vo);
					}
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
