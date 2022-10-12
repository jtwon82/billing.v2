package com.mobon.billing.hhtodd.service.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate; 

@Repository
public class HHtoMigrationDao {
	
	private static final Logger	logger	= LoggerFactory.getLogger(HHtoMigrationDao.class);
	
	public static double fixCount = 50000;
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	public boolean transectionRevisionRuning(Map param){
		
		boolean result = false;
		String NAMESPACE = "hhtoMigrationhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;
				
				try {
					logger.info("dataMigration  START");
					
					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_CAMPMEDIA_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_CAMPMEDIA_STATS", param, resutTime / 1000 + "(sec)");
					
					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_CAMP_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_CAMP_STATS", param, resutTime / 1000 + "(sec)");

					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_ADVERMEDIA_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_ADVERMEDIA_STATS", param, resutTime / 1000 + "(sec)");

					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_ADVER_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_ADVER_STATS", param, resutTime / 1000 + "(sec)");

					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_MEDIASCRIPT_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_MEDIASCRIPT_STATS", param, resutTime / 1000 + "(sec)");

					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_CAMPMEDIA2_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("dataMigration {} (TBRT)  : {}, {}, {}", "DELETE_CAMPMEDIA2_STATS", param, resutTime / 1000 + "(sec)");
					
					res = true;
					
				} catch (Exception e) {
					hisYN = false;
					logger.error("dataMigration transectionRuning Exception ==>", e);
					
					status.setRollbackOnly();
					res = false;
					
				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("dataMigration 종료  : (TBRT) :{}, {}", "MTH_STATS", resutTime / 1000 + "(sec)");
					}
				}
				
				return res;
			}
		});
		return result;
	}

	public boolean transectionCNVRS_ADVERID() {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";

		try {
			Map map = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectCNVRS_ADVERID_TOP1"));

			if(map!=null) {
				long startTime = System.currentTimeMillis();
				long resutTime = 0;
				
				startTime = System.currentTimeMillis();
				sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateCNVRS_ADVERID"), map);
				sqlSessionTemplateBilling.flushStatements();
				resutTime = System.currentTimeMillis() - startTime;
				
				logger.info("updateCNVRS_ADVERID (TBRT) : {}, {} ", map, resutTime + "(ms)");
			}

		}catch(Exception e) {
			logger.error("err ", e);
		}
		
		return result;
	}

	public void insertBeforeCtrData(HashMap<String, Object> param) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		long resutTime = endTime - startTime;
		String NAMESPACE = "hhtoMigrationhhMapper";
		try {
			logger.info("DATA Migration START");
			sqlSessionTemplateBilling.insert(String.format("%s.%s", NAMESPACE, "INSERT_BEFORE_MOB_MEDIA_PAR_WK_STATS"), param);
			sqlSessionTemplateBilling.flushStatements();
			logger.info("DATA Migration BEFORE_MOB_MEDIA_PAR_WK_STATS {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		} catch (Exception e) {
			logger.error("INSERT BEFORE_MOB_MEDIA_PAR_WK_STATS err => {}", e.toString());
		}
		
	}
}
