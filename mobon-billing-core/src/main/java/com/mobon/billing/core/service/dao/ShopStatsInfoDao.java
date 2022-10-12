package com.mobon.billing.core.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;

@Repository
public class ShopStatsInfoDao {

	private static final Logger	logger				= LoggerFactory.getLogger(ShopStatsInfoDao.class);

	public static final String	NAMESPACE	= "shopInfoDataMapper";
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	@Resource (name = "sqlSessionTemplateDreamShopData")
	private SqlSessionTemplate sqlSessionTemplateDreamShopData;

	@Autowired
	private TransactionTemplate	transactionTemplate;
	
	
	
	public boolean transectionRuningV2(HashMap<String, ArrayList<ShopStatsInfoData>> flushMap){
		boolean result = false;
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				try {
					int totalsize = 0;
					for(Entry<String, ArrayList<ShopStatsInfoData>> item : flushMap.entrySet() ){
						String dataKey = item.getKey();
						ArrayList<ShopStatsInfoData> data = item.getValue();
						int itemSize = item.getValue().size();
						
						logger.debug("SHOPSTATSINFODATA dataKey - {}", dataKey);
						logger.debug("SHOPSTATSINFODATA dataSize - {}", itemSize);
						
						if( itemSize>0 ){
							if (dataKey.indexOf("billing") > 0) {
								sqlSessionTemplateDreamShopData.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
								totalsize += itemSize;
							} else {
								//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
							}
						}
					}
					
					res = true;
				} 
				catch (DataIntegrityViolationException e) {
					String errMsg = e.getMessage();
					
					logger.info("errMsg - {}", errMsg);
					
				}
				catch (Exception e) {
					logger.error("err transectionRuningV2 msg ", e);
					status.setRollbackOnly();
					res = false;
				}
				finally{
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}
		});
		return result;
	}
	
//	public void transectionRunning(HashMap<String, ArrayList<ShopStatsInfoData>> flushMap) {
//		
//		logger.debug("flushMap - {}", flushMap);
//		
//		for(Entry<String, ArrayList<ShopStatsInfoData>> item : flushMap.entrySet() ){
//			if(item.getValue().size()>0){
//				for( ShopStatsInfoData vo : item.getValue() ) {
//					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), vo.toList());
//					sqlSessionTemplateBilling.flushStatements();
//				}
//			}
//		}
//	}
}
