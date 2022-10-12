package com.mobon.billing.pargatr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.pargatr.service.dao.ParGatrDataDao;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ParGatrDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ParGatrDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	@Value("${from.batch.server.scriptno}")
	private String	fromBatchServerScriptno;
	
	@Autowired
	private ParGatrDataDao	ParGatrDataDao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

	
	public boolean intoMariaParGatrDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ParGatrData, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ParGtrData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = ParGatrDataDao.transectionRuningV2( flushMap );
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
					
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));
					logger.debug("vo - {}", vo);

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					if (G.CLICK.equals(vo.getType())){
						switch(vo.getProduct()){
							case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":
							case "04": case "c": case "scn":	// 쇼콘 0625 b로 잡히도록 수정했음
							case "05": case "m": case "nct": case "mct":	// 문맥
							case "06": case "p": case "pl": case "mpl":		// playlink
							case "07": case "t": case "pnt": case "mnt": case "mnw":	// 네이티브
							case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
							case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
								break;
							case "02": case "s": case "sky": case "sky_m": case "mbb":
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
								break;
							case "03": case "i": case "ico": case "ico_m": case "mbe":
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
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
								// 옷잘남, 망고쇼핑에서 넘어오는 데이터처리
								if( "ST".equals(vo.getFlagST()) ) {
									Map nowCV = selectDao.mangoStyle(vo);
									if( nowCV!=null ) {
										//logger.error("into vo - {}", vo);
										logger.debug("nowCV - {}, s - {}, adverId - {}", nowCV, vo.getScriptNo(), vo.getAdvertiserId());
										vo.setViewCnt( vo.getViewCnt() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")) );
										vo.setViewCnt2( vo.getViewCnt2() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")) );
										vo.setViewCnt3( vo.getViewCnt3() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt3"), "0")) );
										logger.debug("last v - {}, c - {}", vo.getViewCnt(), vo.getClickCnt());
									}
								}
								// 시간별 데이터
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
								break;
							case "02": case "s": case "sky": case "sky_m": case "mbb": 
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
								break;
							case "03": case "i": case "ico": case "ico_m": case "mbe":
								add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
								break;
							default:
								logger.error("chking vo - {}", vo);
								break;
						}
					} else if (G.VIEWCLICK.equals(vo.getType())){
						add(flushMap, "MOB_CAMP_PAR_GATR_STATS", vo);
					} else {
						logger.info("chking type nothing vo - {}", vo);
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
		l.add(vo);
	}
	
}
