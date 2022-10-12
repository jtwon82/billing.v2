package com.mobon.billing.hhtodd.service;

import java.io.IOException;
import java.util.Arrays;
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
import com.mobon.billing.hhtodd.service.dao.HHtoDDConvDao;
import com.mobon.billing.hhtodd.service.dao.HHtoDDDao;
import com.mobon.billing.hhtodd.service.dao.HHtoDDIntgCntrDao;
import com.mobon.billing.hhtodd.service.dao.HHtoDDMediaChrgDao;
import com.mobon.billing.hhtodd.service.dao.HHtoMTHAdverMTHhhDao;
import com.mobon.billing.hhtodd.service.dao.HHtoMTHParGatrDao;
import com.mobon.billing.hhtodd.service.dao.HHtoMigrationDao;
import com.mobon.billing.hhtodd.service.dao.ShopInfoDao;
import com.mobon.billing.model.BillingVo;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class HHtoDDMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(HHtoDDMariaDB.class);

	@Value("${log.path}")
	private String	logPath;
	@Value("${billing.table.list}")
	private String	billingTableList;
	
	@Autowired
	private HHtoDDDao			hHtoDDDao;
	
	@Autowired
	private HHtoDDConvDao			hHtoDDConv;
	
	@Autowired
	private HHtoDDMediaChrgDao			hHtoDDMediaChrg;
	
	@Autowired
	private HHtoDDIntgCntrDao			hHtoDDIntgCntr;
	
	@Autowired
	private HHtoMTHAdverMTHhhDao		hHtoMTHAdverMTHhhDao;
	
	@Autowired
	private HHtoMTHParGatrDao			hHtoMTHParGatrDao;
	
	@Autowired
	private HHtoMigrationDao			hHtoMigrationDao;
	
	@Autowired
	private ShopInfoDao		shopInfoDao;
	
	
	public boolean intoMariaHHtoDDMariaDBV3(String [] _group, List<?> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();
		
//		if (aggregateList != null) {
//			
//		} else {
//			result = true;
//		}
		HashMap<String, String> flushMap = makeFlushMap(aggregateList);
		
		if ( flushMap.keySet().size() != 0) {
			if (toMongodb) {
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.HHtoDD, aggregateList);
				} catch (IOException e) {
					logger.error("err - {}", e);
				}
				
				logger.info("fail ClickViewData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
				result = true;
				
			} else {
				result = hHtoDDDao.transectionRuningV2( _group, flushMap );
				logger.info("succ intoMariaClickViewData _id - {}, size - {}, during - {}", _group, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
			}
		} else {
			result = true;
		}
		
		return result;
	}

	public HashMap<String, String> makeFlushMap(List<?> aggregateList){
		HashMap<String, String> flushMap = new HashMap();

		if( aggregateList==null ) {
			List<String> tableList = Arrays.asList(billingTableList.split(","));
//			tableList.add("MOB_ADVER_MEDIA_STATS");
//			tableList.add("MOB_ADVER_STATS");
//			tableList.add("MOB_CAMP_MEDIA_STATS");
//			tableList.add("MOB_CAMP_STATS");
//			tableList.add("MOB_COM_STATS_INFO");
//			tableList.add("MOB_MEDIA_SCRIPT_STATS");
//			tableList.add("MOB_MEDIA_STATS");
//			tableList.add("MOB_CNVRS_STATS");
			
			for( String name : tableList ) {
				flushMap.put(name, name);
			}
		} else {
			for (Object vo : aggregateList) {
				try {
					if (vo != null) {
	//					if(flushMap.get(vo.toString())==null){
							flushMap.put(vo.toString(), vo.toString());
	//					}
	//					String table = flushMap.get(vo.toString());
	//					table.add(vo);
					}
				} catch (Exception e) {
					logger.error("err item - {}, msg - {}", vo, e);
				}
			}
		}
		return flushMap;
	}
	
	
	public boolean intoHHtoHHDB(Map param) {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningHHtoHH(param);
		
		return result;
	}	
	
	public boolean intoHHtoCOMINFODefHHDB() {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningHHtoCOMINFODefHH();
		
		return result;
	}
	
	public boolean intoHHtoDDDB(Map param) {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningDDtoDD(param);
		
		return result;
	}
	
	public boolean intoHHtoDDConv(Map param) {
		boolean result = false;
		
		result = hHtoDDConv.transectionRuningHHtoHHConv(param);
		
		return result;
	}
	
	
	public boolean intoHHtoDDMediaChrg(Map param) {
		boolean result = false;
		
		result = hHtoDDMediaChrg.transectionRuningDDtoDDMediaChrg(param);
		
		return result;
	}
	public boolean intoHHtoDDMediaChrgReBuild(Map param) {
		boolean result = false;
		
		result = hHtoDDMediaChrg.transectionRuningDDtoDDMediaChrgReBuild(param);
		
		return result;
	}
	
	public boolean intoHHtoDDIntgCntr(Map param) {
		boolean result = false;
		
		result = hHtoDDIntgCntr.transectionRuningDDtoDDIntgCntr(param);
		
		return result;
	}
	
	public boolean mongoToMariaDDIntgCntrConv(Map param) {
		boolean result = false;
		
		result = hHtoDDIntgCntr.transectionRuningDDtoDDIntgCntrConv(param);
		
		return result;
	}
	
	public boolean intoConvDB(Map param) {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningConv(param);
		
		return result;
	}
	
	
	public boolean intoNearDB(Map param) {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningNear(param);
		
		return result;
	}
	
	public boolean intoPlinkDB(Map param) {
		boolean result = false;
		
		result = hHtoDDDao.transectionRuningPLINK(param);
		
		return result;
	}
	
	
	public List selectHHDataList(Map param) {
		List result = null;
		
		result = hHtoDDDao.selectDateList(param);
		
		return result;
	}
	
	
	public boolean intoDreamConvDB(Map param) {
		boolean result = false;
		
		//result = hHtoDDDao.transectionRuningPLINK(param);
		result = hHtoDDDao.transectionRuningDreamConv(param)    ;//.transectionRuningPLINK(param);
		
		return result;
	}
	
	public BillingVo intoPrdtCtgrInfo(BillingVo param) {
		BillingVo result = new BillingVo();
		
		result = shopInfoDao.transectionRuningPrdtCtgrInfo(param);
		
		return result;
	}
	
	public BillingVo selectMaxShopNo() {
		BillingVo result = new BillingVo();
		
		result = shopInfoDao.transectionRuningSelMaxShopNo();
		
		return result;
	}
	
	public boolean intoNearRevision(Map param) {
		boolean result = false;
		
		//result = hHtoDDDao.transectionRuningPLINK(param);
		result = hHtoDDDao.transectionRuningNearRevision(param);//.transectionRuningPLINK(param);
		
		return result;
	}
	
	public boolean intoAdverMTHhhStats(Map param) {
		boolean result = false;
		result = hHtoMTHAdverMTHhhDao.transectionRuningDDtoDDAdverMTHhh(param);
		return result;
	}

	public boolean intoParGatrMTHStats(Map param) {
		boolean result = false;
		result = hHtoMTHParGatrDao.transectionRuning(param);
		return result;
	}

	public boolean intoCnvrsMTHStats(Map param) {
		boolean result = false;
		result = hHtoMTHParGatrDao.transectionConversionRuning(param);
		return result;
	}

	public boolean intoMTHRevision(String step, int STATS_MTH) {
		boolean result = false;
		result = hHtoMTHParGatrDao.transectionRevisionRuning(step, STATS_MTH);
		return result;
	}
  

	public boolean dataMigration(String stats_dttm) {
		boolean result= false;
		
		Map param = new HashMap();
		param.put("STATS_DTTM", stats_dttm);
		
		result = hHtoMigrationDao.transectionRevisionRuning(param);

		return result;
	}

	public boolean dataCNVRS_ADVERID() {
		boolean result = false;
		result = this.hHtoMigrationDao.transectionCNVRS_ADVERID();
		return result;
	}

	public List<Map<String, String>> snapShotMediaScript() {
		return hHtoDDDao.snapShotMediaScript();
	}

	public void intoMediaWeekStats(Map param) {
		hHtoDDDao.insertMediaWeekStats(param);
		
	}

	public void insertBeforeRenewData(Map param) {
		hHtoDDDao.insertBeforeRenewData(param);		
	}
	
	public void insertBeforeCtrData(HashMap<String, Object> param) {
		hHtoMigrationDao.insertBeforeCtrData(param);
		
	}

	public void updateDiffMobMediaScriptChrgStats(Map param) {
		hHtoDDDao.updateDiffMobMediaScriptChrgStats(param);
		
	}

	public void intoCnvrsRenewMTHStatsRevision(Map<String, Object> param) {
		hHtoMTHParGatrDao.updateDiffCnvrsRenewMthStatsRevision(param);
		
	}

	public List<Map<String, String>> snapShotMediaScriptChrg() {
		return hHtoDDDao.snapShotMediaScriptChrg();
	}

}
