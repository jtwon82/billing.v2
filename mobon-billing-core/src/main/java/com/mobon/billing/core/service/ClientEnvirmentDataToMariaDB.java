package com.mobon.billing.core.service;

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
import com.mobon.billing.core.service.dao.ClientEnvirmentDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ClientEnvirmentDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(ClientEnvirmentDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private ClientEnvirmentDataDao	clientEnvirmentDataDao;
	@Autowired
	private SelectDao			selectDao;
	
	
	

//	public void sp_clickviewx( ArrayList<BaseCVData> list ) {
//		HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(list);
//		clickViewDataDao.transectionRunning(flushMap);
//	}	
	
	public boolean intoMariaClientEnvirmentDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
						ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.client_environment, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ClientEnvirmentData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = clientEnvirmentDataDao.transectionRuningV2( flushMap );
					//logger.info("succ intoMariaClientEnvirmentData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
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
						switch(vo.getAdGubun()) {
							case "01": add(flushMap, "client_env_click_ad_insert", vo); break;
							case "04": add(flushMap, "client_env_click_cw_insert", vo); break;
							case "05": add(flushMap, "client_env_click_hu_insert", vo); break;
							case "06": add(flushMap, "client_env_click_kl_insert", vo); break;
							case "10": add(flushMap, "client_env_click_rc_insert", vo); break;
							case "11": add(flushMap, "client_env_click_rm_insert", vo); break;
							case "17": add(flushMap, "client_env_click_sr_insert", vo); break;
							case "19": add(flushMap, "client_env_click_um_insert", vo); break;
							case "26": add(flushMap, "client_env_click_cm_insert", vo); break;
							case "28": add(flushMap, "client_env_click_au_insert", vo); break;
							
							
							case "16": case "34": case "37": case "40":	case "41": case "42": case "47": case "49": case "50": case "54": case "55": case "56": case "57":
								add(flushMap, "client_env_click_etc_pd_insert", vo); break;
							
							case "08": case "13": case "14": case "18": case "29": case "30": case "31": case "32": case "35": case "36": case "43": case "44": case "46": case "48": case "51": case "52": case "53": case "58":
								add(flushMap, "client_env_click_etc_npd_insert", vo); break;
							
						}
					} else if (G.VIEW.equals(vo.getType())){
						/*
						if( "ST".equals(vo.getFlagST()) && !"02".equals(vo.getProduct()) && !"03".equals(vo.getProduct())) {
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
						*/
						switch(vo.getAdGubun()) {
							case "01": add(flushMap, "client_env_view_ad_insert", vo); break;
							case "04": add(flushMap, "client_env_view_cw_insert", vo); break;
							case "05": add(flushMap, "client_env_view_hu_insert", vo); break;
							case "06": add(flushMap, "client_env_view_kl_insert", vo); break;
							case "10": add(flushMap, "client_env_view_rc_insert", vo); break;
							case "11": add(flushMap, "client_env_view_rm_insert", vo); break;
							case "17": add(flushMap, "client_env_view_sr_insert", vo); break;
							case "19": add(flushMap, "client_env_view_um_insert", vo); break;
							case "26": add(flushMap, "client_env_view_cm_insert", vo); break;
							case "28": add(flushMap, "client_env_view_au_insert", vo); break;

							
							case "16": case "34": case "37": case "40":	case "41": case "42": case "47": case "49": case "50": case "54": case "55": case "56": case "57":
								add(flushMap, "client_env_view_etc_pd_insert", vo); break;
							
							case "08": case "13": case "14": case "18": case "29": case "30": case "31": case "32": case "35": case "36": case "43": case "44": case "46": case "48": case "51": case "52": case "53": case "58":
								add(flushMap, "client_env_view_etc_npd_insert", vo); break;
						}
					} else if (G.RETRNAVAL.equals(vo.getType())){
						/*
						if( "ST".equals(vo.getFlagST()) && !"02".equals(vo.getProduct()) && !"03".equals(vo.getProduct())) {
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
						*/
						switch(vo.getAdGubun()) {
							case "01": add(flushMap, "client_env_retrn_aval_ad_insert", vo); break;
							case "04": add(flushMap, "client_env_retrn_aval_cw_insert", vo); break;
							case "05": add(flushMap, "client_env_retrn_aval_hu_insert", vo); break;
							case "06": add(flushMap, "client_env_retrn_aval_kl_insert", vo); break;
							case "10": add(flushMap, "client_env_retrn_aval_rc_insert", vo); break;
							case "11": add(flushMap, "client_env_retrn_aval_rm_insert", vo); break;
							case "17": add(flushMap, "client_env_retrn_aval_sr_insert", vo); break;
							case "19": add(flushMap, "client_env_retrn_aval_um_insert", vo); break;
							case "26": add(flushMap, "client_env_retrn_aval_cm_insert", vo); break;
							case "28": add(flushMap, "client_env_retrn_aval_au_insert", vo); break;

							
							case "16": case "34": case "37": case "40":	case "41": case "42": case "47": case "49": case "50": case "54": case "55": case "56": case "57":
								add(flushMap, "client_env_retrn_aval_etc_pd_insert", vo); break;
							
							case "08": case "13": case "14": case "18": case "29": case "30": case "31": case "32": case "35": case "36": case "43": case "44": case "46": case "48": case "51": case "52": case "53": case "58":
								add(flushMap, "client_env_retrn_aval_etc_npd_insert", vo); break;
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
