package com.mobon.billing.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.dao.ShopStatsInfoDao;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class ShopStatsInfoDataToMariaDB {
	private static final Logger	logger	= LoggerFactory.getLogger(ShopStatsInfoDataToMariaDB.class);

	@Autowired
	private ShopStatsInfoDao	shopStatsInfoDao;

	@Value("${log.path}")
	private String	logPath;
	
	

//	public void sp_shopstats_info(ShopStatsInfoData item) {
//		ArrayList<ShopStatsInfoData> list = new ArrayList();
//		list.add(item);
//		HashMap<String, ArrayList<ShopStatsInfoData>> flushMap = makeFlushMap(list);
//		shopStatsInfoDao.transectionRunning(flushMap);
//	}
	
	public boolean intoMariaShopStatsInfoDataV3(String _id, List<ShopStatsInfoData> aggregateList, boolean toMongodb) {
		boolean result = false;
		
		long start_millis = System.currentTimeMillis();
		if (aggregateList != null){
			HashMap<String, ArrayList<ShopStatsInfoData>> flushMap = makeFlushMap(aggregateList);
						
			if(flushMap.keySet().size() != 0){
				if( toMongodb ){
					//logger.info("fail insert into maria ");
					try {
						ConsumerFileUtils.writeLine( logPath +"retryfail/", String.format("insertIntoError_%s_%s", G.shop_stats, DateUtils.getDate("yyyyMMdd_HHmm")), G.shop_stats, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}
					
					logger.info("chking fail ShopStatsInfoData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
					result = true;
				} else {
					//logger.info("insert into maria Start");
					
					result = shopStatsInfoDao.transectionRuningV2( flushMap );
					logger.info("succ intoMariaShopStatsInfoDataV3 _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
				}
			}else{
				result = true;
			}
		} else {
			result = true;
		}
		
		return result;
	}

	
	public HashMap<String, ArrayList<ShopStatsInfoData>> makeFlushMap(List<ShopStatsInfoData> aggregateList){
		HashMap<String, ArrayList<ShopStatsInfoData>> flushMap = new HashMap();
		
		for (ShopStatsInfoData vo : aggregateList) {
			try {
				if (vo != null) {
					if("".equals(vo.getPlatform())){
						logger.debug("not platform vo - {}", vo);
						continue;
					}
					
					if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					
					if("01".equals(vo.getPlatform())){
						if(vo.isbHandlingStatsMobon()) {
							//add(flushMap, "sp_shop_stats_NEW_W", vo);
						} else {
							add(flushMap, "sp_shop_stats_NEW_W_billing", vo);
						}
					}
					else{
						if(vo.isbHandlingStatsMobon()) {
							//add(flushMap, "sp_shop_stats_NEW_M", vo);
						} else {
							add(flushMap, "sp_shop_stats_NEW_M_billing", vo);
						}
					}
				}
			}catch(Exception e){
				logger.error("err msg - {}, item - {}", e.getMessage(), vo);
			}
		}
		return flushMap;
	}
	private void add(HashMap<String, ArrayList<ShopStatsInfoData>> flushMap, String key, ShopStatsInfoData vo) {
		if(flushMap.get(key)==null){
			flushMap.put(key, new ArrayList());
		}
		ArrayList<ShopStatsInfoData> l = flushMap.get(key);
		l.add(vo);
	}
}
