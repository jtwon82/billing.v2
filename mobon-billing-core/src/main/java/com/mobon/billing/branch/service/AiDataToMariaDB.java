package com.mobon.billing.branch.service;

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
import com.mobon.billing.branch.service.dao.AiDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

/**
 * AiDataToMariaDB
 * 캠페인 생성에 Ai 사용여부 관련 summary 처리 클래스
 * 
 * @author  : sjpark3
 * @since   : 2021-12-15
 */
@Service
public class AiDataToMariaDB {
	@Autowired
	private AiDataDao aiDataDao;
	@Autowired
	private SelectDao selectDao;
	
	@Value("${log.path}")
	private String	logPath;
	
	private static final Logger logger = LoggerFactory.getLogger(AiDataToMariaDB.class);
	
	/**
     * intoMariaAiDataV3
     * mariaDB 인서트 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-15
     */
	public boolean intoMariaAiDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;

		if (aggregateList != null) {
			// 데이터 summary
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if (flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						// 파일로 출력
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.AiData, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail AiDataDao fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					// DB로 처리
					result = aiDataDao.transectionRuningV2(flushMap);
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
     * makeFlushMap
     * 데이터 summary 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-15
     */
	public HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList){
		HashMap<String, ArrayList<BaseCVData>> flushMap = new HashMap<>();
		
		for (BaseCVData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd()==null || vo.getPlatform()==null || vo.getAdGubun()==null 
						|| vo.getSiteCode()==null || vo.getScriptNo()==0 || vo.getAdvertiserId()==null || vo.getType()==null) {
						logger.error("Missing required, vo - {}", vo.toString());
						continue;
					}
					
					if (StringUtils.isEmpty(vo.getProduct()) || StringUtils.isEmpty(vo.getScriptUserId())) {
						BaseCVData minfo = selectDao.selectMediaInfo(vo);
						
						if (minfo!=null) {
							logger.error("map - {}", minfo);
							
							if (StringUtils.isEmpty(vo.getProduct())) {
								vo.setProduct( StringUtils.trimToNull2(minfo.getProduct()));
								logger.error("chking vo - {}", vo);
							}
							
							if (StringUtils.isEmpty(vo.getScriptUserId())) {
								vo.setScriptUserId( StringUtils.trimToNull2(minfo.getScriptUserId())); //map.get("scriptUserId")) );
								logger.error("chking vo - {}", vo);
							}
						}
					}
					
					if (!StringUtils.isNumeric(vo.getPlatform()))	vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if (!StringUtils.isNumeric(vo.getProduct()))	vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if (!StringUtils.isNumeric(vo.getAdGubun()))	vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));

					//상품 타겟팅 여부 확인용 메소드
					if (vo.getAdGubun() != null) {
						vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
					}

					logger.debug("vo - {}", vo);
					
					if (!vo.isNoExposureYN()) {
						if (G.CLICK.equals(vo.getType())) {						
							// ADVRTS_PRDT_CODE 에 따른 분기처리
							switch (vo.getProduct()) {
								case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":	// 배너
								case "04": case "c": case "scn":    														// 쇼콘
								case "05": case "m": case "nct": case "mct":												// 쇼셜링크
								case "06": case "p": case "pl": case "mpl":        											// 플레이링크
								case "07": case "t": case "pnt": case "mnt":  case "mnw":   								// 네이티브
								case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
								case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
									add(flushMap, "ai_camp_banner_click", vo);
									break;
									
								case "02": case "s": case "sky": case "sky_m": case "mbb":									// 브랜드커버
									add(flushMap, "ai_camp_sky_click", vo);
									break;
									
								case "03": case "i": case "ico": case "ico_m": case "mbe":									// 아이커버
									add(flushMap, "ai_camp_ico_view", vo);
									break;
									
								default:
									logger.error("chking vo - {}", vo);
									break;
							}
						} else if (G.VIEW.equals(vo.getType())) {
							// ADVRTS_PRDT_CODE 에 따른 분기처리
							switch (vo.getProduct()) {
								case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":	// 배너
								case "04": case "c": case "scn":    														// 쇼콘
								case "05": case "m": case "nct": case "mct":												// 쇼셜링크
								case "06": case "p": case "pl": case "mpl":        											// 플레이링크
								case "07": case "t": case "pnt": case "mnt":  case "mnw":   								// 네이티브
								case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
								case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
									// 옷잘남, 망고쇼핑에서 넘어오는 데이터처리
									if ("ST".equals(vo.getFlagST())) {
										Map nowCV = selectDao.mangoStyle(vo);
										if (nowCV != null) {
											logger.debug("nowCV - {}, s - {}, adverId - {}", nowCV, vo.getScriptNo(), vo.getAdvertiserId());
											vo.setViewCnt(vo.getViewCnt() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")));
											vo.setViewCnt2(vo.getViewCnt2() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")));
											vo.setViewCnt3(vo.getViewCnt3() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt3"), "0")));
											logger.debug("last v - {}, c - {}", vo.getViewCnt(), vo.getClickCnt());
										}
									}
	
									add(flushMap, "ai_camp_banner_view", vo);
									break;
									
								case "02": case "s": case "sky": case "sky_m": case "mbb":									// 브랜드커버
									add(flushMap, "ai_camp_sky_view", vo);
									break;
									
								case "03": case "i": case "ico": case "ico_m": case "mbe":									// 아이커버
									add(flushMap, "ai_camp_ico_view", vo);
									break;
									
								default:
									logger.error("chking vo - {}", vo);
									break;
							}
						}		
					}
				}
			} catch (Exception e) {
				logger.error("err item - {}", vo, e);
			}
		}
		return flushMap;
	}

	/**
     * makeFlushMap
     * Map에 실행 쿼리 추가하는 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-15
     */
	private void add(HashMap<String, ArrayList<BaseCVData>> flushMap, String key, BaseCVData vo) {
		if (flushMap.get(key)==null) {
			flushMap.put(key, new ArrayList<>());
		}
		
		ArrayList<BaseCVData> l = flushMap.get(key);
		l.add(vo);
	}	
}
