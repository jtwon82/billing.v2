package com.mobon.billing.report.service;

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
import com.mobon.billing.model.v15.NativeNonAdReportData;
import com.mobon.billing.report.service.dao.NativeNonAdReportDataDao;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class NativeNonAdReportDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(NativeNonAdReportDataToMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	@Value("${from.batch.server.scriptno}")
	private String	fromBatchServerScriptno;
	
	@Autowired
	private NativeNonAdReportDataDao nativeNonAdReportDataDao;
	
	public boolean intoMariaNativeNonAdReportDataV3(String _id, List<NativeNonAdReportData> aggregateList, boolean toMongodb) {
		boolean result = false;	
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {
			
			HashMap<String, ArrayList<NativeNonAdReportData>> flushMap = makeFlushMap(aggregateList);
			
			if ( flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.NativeNonAdReport, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("fail NativeNonAdReportData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					result = nativeNonAdReportDataDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaNativeNonAdReportData _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}
	
	public HashMap<String, ArrayList<NativeNonAdReportData>> makeFlushMap(List<NativeNonAdReportData> aggregateList){
		HashMap<String, ArrayList<NativeNonAdReportData>> flushMap = new HashMap<String, ArrayList<NativeNonAdReportData>>();
		NativeNonAdReportData cpVo = null;
		for (NativeNonAdReportData vo : aggregateList) {
			try {
				if (vo != null) {
					String type = vo.type;
					// 유효노출
					// 네이티브 노출
					if(G.VALID_VIEW.equals(type) || G.NATIVE_VIEW.equals(type)) {
						add(flushMap, "selectMobKwrdDitbStatsUpdate", vo);	
						//logger.info("*** G.VALID_VIEW.equals(type) || G.NATIVE_VIEW.equals(type) vo=====>>{}" , vo);
					//네이티브 기사 클릭
					}else if(G.CLICK.equals(type)) {
						add(flushMap, "selectMobKwrdDitbStatsUpdate", vo);	
						if (vo.clickCnt > 0 && !"".equals(vo.newsType) && vo.newsType != null){	
							vo.rptTpCd      	= vo.newsType;
							add(flushMap, "selectMobKwrdRptStatus", vo);	
						}
					//네이티브 기사별 노출수
					}else if(G.VIEW.equals(type)){
						add(flushMap, "selectMobKwrdDitbStatsUpdate", vo);	

						if (vo.reNewsCnt > 0) {	
							cpVo 				= new NativeNonAdReportData();
							cpVo.yyyymmdd   	= vo.yyyymmdd;
							cpVo.platform   	= vo.platform;
							cpVo.product    	= vo.product;
							cpVo.adGubun    	= vo.adGubun;
							cpVo.scriptNo   	= vo.scriptNo;
							cpVo.interlock  	= vo.interlock;
							cpVo.rptTpCd 		= "RE";
							cpVo.newsVewCnt 	= vo.reNewsCnt;
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 오디언스(1) : AU
						if ( vo.auNewsCnt > 0) {	
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "AU";
							cpVo.newsVewCnt = vo.auNewsCnt;
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 인기(1) : PO
						if ( vo.poNewsCnt > 0) {	
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "PO";
							cpVo.newsVewCnt = vo.poNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 최신(n) : LA
						if (vo.laNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "LA";
							cpVo.newsVewCnt = vo.laNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 카테고리(n) : CM
						if (vo.cmNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "CM";
							cpVo.newsVewCnt = vo.cmNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// AB테스트용 
						if (vo.abTestCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "AB";
							cpVo.newsVewCnt = vo.abTestCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 리턴기사(n) : RM
						if (vo.rmNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "RM";
							cpVo.newsVewCnt = vo.rmNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						if (vo.totalNewsCnt > 0 ) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "TT";
							cpVo.newsVewCnt = vo.totalNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
					}
				}		
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
		return flushMap;
	}
	
	/*
	public HashMap<String, ArrayList<NativeNonAdReportData>> makeFlushMap(List<NativeNonAdReportData> aggregateList){
		HashMap<String, ArrayList<NativeNonAdReportData>> flushMap = new HashMap<String, ArrayList<NativeNonAdReportData>>();
		NativeNonAdReportData cpVo = null;
		for (NativeNonAdReportData vo : aggregateList) {
			try {
				
				
				
				
				if (vo != null) {
					// sqlMapper
					add(flushMap, "selectMobKwrdDitbStatsUpdate", vo);	
					
					// 기사 클릭
					if (vo.clickCnt > 0 && vo.type.equals("C")){
						logger.info("＠@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
						logger.info("vo.yyyymmdd=====>>{}" , vo.yyyymmdd);
						logger.info("vo.platform=====>>{}" , vo.platform);
						logger.info("vo.product=====>>{}"  , vo.product);
						logger.info("vo.adGubun=====>>{}"  , vo.adGubun);
						logger.info("vo.scriptNo=====>>{}" , vo.scriptNo);
						logger.info("vo.interlock=====>>{}", vo.interlock);
						logger.info("vo.newsType=====>>{}" , vo.newsType);
						logger.info("vo.newsClickCnt=====>>{}", vo.newsClickCnt);
						if (!"".equals(vo.newsType) && vo.newsType != null) {	
							vo.rptTpCd      = vo.newsType;
							vo.newsClickCnt = 1;
							add(flushMap, "updateMobKwrdRptStatus", vo);	
						}
						logger.info("＠@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					// 기사 송출시.
					}else if(vo.type.equals("V")){			
						//뉴스 타입 => 추천(1) : RE, 오디언스(1) : AU, 인기(1) : PO, 최신(n) : LA
						// 추천(1) : RE
						if (vo.reNewsCnt > 0) {	
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "RE";
							cpVo.newsVewCnt = vo.reNewsCnt;
							cpVo.newsClickCnt = 0;
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 오디언스(1) : AU
						if ( vo.auNewsCnt > 0) {	
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "AU";
							cpVo.newsVewCnt = vo.auNewsCnt;
							cpVo.newsClickCnt = 0;
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 인기(1) : PO
						if ( vo.poNewsCnt > 0) {	
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "PO";
							cpVo.newsVewCnt = vo.poNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 최신(n) : LA
						if (vo.laNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "LA";
							cpVo.newsVewCnt = vo.laNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 카테고리(n) : CM
						if (vo.cmNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "CM";
							cpVo.newsVewCnt = vo.cmNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						// 리턴기사(n) : RM
						if (vo.rmNewsCnt > 0) {
							cpVo = new NativeNonAdReportData();
							cpVo.yyyymmdd   = vo.yyyymmdd;
							cpVo.platform   = vo.platform;
							cpVo.product    = vo.product;
							cpVo.adGubun    = vo.adGubun;
							cpVo.scriptNo   = vo.scriptNo;
							cpVo.interlock  = vo.interlock;
							cpVo.rptTpCd 	= "RM";
							cpVo.newsVewCnt = vo.rmNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", cpVo);	
						}
						if (vo.totalNewsCnt > 0 ) {	
							vo.rptTpCd = "TT";
							vo.newsVewCnt = vo.totalNewsCnt; 	
							add(flushMap, "selectMobKwrdRptStatus", vo);	
						}
					}
				}
			} catch (Exception e) {
				logger.error("err item - {}, msg - {}", vo, e);
			}
		}
//		logger.info("makeFlushMap() - {}", flushMap.toString());
		return flushMap;
	}
	*/

	
	private void add(HashMap<String, ArrayList<NativeNonAdReportData>> flushMap, String key, NativeNonAdReportData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList<NativeNonAdReportData>());
		}
		ArrayList<NativeNonAdReportData> l = flushMap.get(key);
		l.add(vo);
	}
	
}
