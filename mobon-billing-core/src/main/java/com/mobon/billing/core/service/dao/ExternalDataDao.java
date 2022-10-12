package com.mobon.billing.core.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ExternalInfoData; 

@Repository
public class ExternalDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(ExternalDataDao.class);

	public static final String	NAMESPACE	= "clickViewDataMapper";

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;

	
	
	
	
	
	

	public boolean transectionRuningV2(HashMap<String, ArrayList<ExternalInfoData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {
					int totalBillingSize = 0;
					int totalDreamSize = 0;
					
					for(Entry<String, ArrayList<ExternalInfoData>> item : flushMap.entrySet() ){
						ArrayList<ExternalInfoData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						if(item.getValue().size()>0){
							logger.debug("item.getKey() - {}, item.getValue().size() - {}", item.getKey(), item.getValue().size());
//							if(item.getKey().indexOf("point")>0){
//								if (dataKey.indexOf("billing")>0) {
//									//sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
//									totalDreamSize += dataSize;
//								} else {
//									//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
//								}
//								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
//								totalBillingSize += dataSize;
//							}
//							else{
//								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
//								totalBillingSize += dataSize;
//							}
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
							totalBillingSize += dataSize;
						}
					}
					
					long startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.flushStatements();
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Billing External : " + resutTime + "(ms) totalsize - "+ totalBillingSize);

//					startTime = System.currentTimeMillis();
//					//sqlSessionTemplateDream.flushStatements();
//					endTime = System.currentTimeMillis();
//					resutTime = endTime - startTime;
//					logger.info("TR Time (TBRT) Dream External : " + resutTime / 1000 + "(sec) totalsize - "+ totalDreamSize);
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuningV2 msg - {}", e);
					status.setRollbackOnly();
					res = false;
					
				} finally {
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}
		});
		return result;
	}

//	public void transectionRunningX(HashMap<String, ArrayList<ExternalInfoData>> flushMap) {
//		for (Entry<String, ArrayList<ExternalInfoData>> item : flushMap.entrySet()) {
//			if (item.getValue().size() > 0) {
//				logger.debug("item.getKey() - {}, item.getValue().size() - {}", item.getKey(), item.getValue().size());
//				if (item.getKey().indexOf("point") > 0) {
//					for( ExternalInfoData vo : item.getValue() ) {
//						try {
//							sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, item.getKey()), vo.toList());
//						}catch(Exception e) {
//							logger.error("err - {}, vo {}", e, vo);
//						}
//					}
//				} else {
//					for( ExternalInfoData vo : item.getValue() ) {
//						try {
//							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), vo.toList());
//						}catch(Exception e) {
//							logger.error("err - {}, vo {}", e, vo);
//						}
//					}
//				}
//			}
//		}
//	}
}
