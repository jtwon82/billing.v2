package com.mobon.billing.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.dao.ClickViewDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ClickViewDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ClickViewDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private ClickViewDataDao	clickViewDataDao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

//	public void sp_clickviewx( ArrayList<BaseCVData> list ) {
//		HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(list);
//		clickViewDataDao.transectionRunning(flushMap);
//	}	
	
	public boolean intoMariaClickViewDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ClickViewData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = clickViewDataDao.transectionRuningV2( flushMap );
					
					if(result) {
						List<BaseCVData> pointList= new ArrayList();
						for (Entry<String, ArrayList<BaseCVData>> obj: flushMap.entrySet()) {
							ArrayList<BaseCVData> list= obj.getValue();
							for(BaseCVData vo:list) {
								if( vo.getHandlingPointData().equals("clickView") 
										&& (vo.getPoint() > 0 || vo.getMpoint() > 0)) {
									vo.setPointChargeAble("true");
									pointList.add(vo);
								}
							}
						}
						HashMap<String, ArrayList<BaseCVData>> pointFlushMap= makeFlushMap(pointList);
						boolean resultPoint= clickViewDataDao.transectionPointRuningV2( pointFlushMap );
						if(!resultPoint) {
							logger.info("pointFlushMap {}", pointFlushMap);
						}
					}
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
					if (vo.isNoExposureYN()){

						if("03".equals(vo.getProduct())) {
							add(flushMap, "unexposure_camp_media_ico", vo);
						} else {
							if (G.CLICK.equals(vo.getType())) {
								add(flushMap, "unexposure_camp_media_click_banner", vo);
							} else if (G.VIEW.equals(vo.getType())) {
								add(flushMap, "unexposure_camp_media_view_banner", vo);
							}
						}

					} else {
						if (G.CLICK.equals(vo.getType())) {
							if ("14".equals(vo.getAdGubun())) {
								logger.info("adgubun_14	sc-{}, s-{}, point-{} ", vo.getSiteCode(), vo.getScriptNo(), vo.getPoint());
							}
							switch (vo.getProduct()) {
								case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":
								case "04": case "c": case "scn":    // 쇼콘 0625 b로 잡히도록 수정했음
								case "05": case "m": case "nct": case "mct":    // 문맥
								case "06": case "p": case "pl": case "mpl":        // playlink
								case "07": case "t": case "pnt": case "mnt":  case "mnw":   // 네이티브
								case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
								case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
									
									add(flushMap, "sp_banner_click_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {    // UM 에대한 처리
										//add(flushMap, "sp_banner_click_NEW_K", vo);
									}
									if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) { // if(vo.getPoint()>0 || vo.getMpoint()>0 ){
										add(flushMap, "sp_point_NEW", vo);
									}
									break;
									
								case "02": case "s": case "sky": case "sky_m": case "mbb":
									add(flushMap, "sp_sky_click_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {
										//add(flushMap, "sp_sky_click_NEW_K", vo);
									}
									break;
									
								case "03": case "i": case "ico": case "ico_m": case "mbe":
									add(flushMap, "sp_ico_view_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {
										//add(flushMap, "sp_ico_view_NEW_K", vo);
									}
									if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) { //if(vo.getPoint()>0 || vo.getMpoint()>0 ){
										add(flushMap, "sp_point_NEW", vo);
									}
									break;
								default:
									logger.error("chking vo - {}", vo);
									break;
							}
						} else if (G.VIEW.equals(vo.getType())) {
							switch (vo.getProduct()) {
								case "01": case "b": case "nor": case "mba": case "mbw": case "floating": case "banner":
								case "04": case "c": case "scn":    // 쇼콘
								case "05": case "m": case "nct": case "mct":    // 문맥
								case "06": case "p": case "pl": case "mpl":
								case "07": case "t": case "pnt": case "mnt":  case "mnw":
								case "08": case "mpw":    // 플러스콜(pluscall(w)) 추가
								case "09": case "pf": case "pfw": case "pfm": // 퍼포먼스에드 추가
									// 옷잘남, 망고쇼핑에서 넘어오는 데이터처리
									if ("ST".equals(vo.getFlagST())) {
										Map nowCV = selectDao.mangoStyle(vo);
										if (nowCV != null) {
											//logger.error("into vo - {}", vo);
											logger.debug("nowCV - {}, s - {}, adverId - {}", nowCV, vo.getScriptNo(), vo.getAdvertiserId());
											vo.setViewCnt(vo.getViewCnt() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")));
											vo.setViewCnt2(vo.getViewCnt2() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt1"), "0")));
											vo.setViewCnt3(vo.getViewCnt3() - Integer.parseInt(StringUtils.trimToNull2(nowCV.get("ago_viewcnt3"), "0")));
											logger.debug("last v - {}, c - {}", vo.getViewCnt(), vo.getClickCnt());
										}
									}

									// 시간별 데이터
									add(flushMap, "sp_banner_view_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {
										//add(flushMap, "sp_banner_view_NEW_K", vo);
									}

									if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) { //if( ( vo.getPoint()>0 || vo.getMpoint()>0) ){
										add(flushMap, "sp_point_NEW", vo);
									}

									// playlink
									if ("06".equals(vo.getProduct())) {
										if (vo.getPlAdviewCnt() > 0 || vo.getPlMediaViewCnt() > 0 || vo.getPoint() > 0 || vo.getMpoint() > 0) {
											logger.debug("vo.getPlAdviewCnt()-{} && vo.getPlMediaViewCnt()-{} && vo.getPoint()-{} && vo.getMpoint()-{}"
													, vo.getPlAdviewCnt(), vo.getPlMediaViewCnt(), vo.getPoint(), vo.getMpoint());
											add(flushMap, "sp_banner_pl_view_NEW", vo);
										}
									}
									break;
									
								case "02": case "s": case "sky": case "sky_m": case "mbb":
									add(flushMap, "sp_sky_view_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {
										//add(flushMap, "sp_sky_view_NEW_K", vo);
									}
									if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) {//if(vo.getPoint()>0 || vo.getMpoint()>0 ){
										add(flushMap, "sp_point_NEW", vo);
									}
									break;
									
								case "03": case "i": case "ico": case "ico_m": case "mbe":
									add(flushMap, "sp_ico_view_NEW_NK", vo);
									if ("19".equals(vo.getAdGubun())) {
										//add(flushMap, "sp_ico_view_NEW_K", vo);
									}
									if (vo.getPointChargeAble().equals("true") && (vo.getPoint() > 0 || vo.getMpoint() > 0)) {//if(vo.getPoint()>0 || vo.getMpoint()>0 ){
										add(flushMap, "sp_point_NEW", vo);
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
