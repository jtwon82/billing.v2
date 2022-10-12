package com.mobon.billing.branchUM.service.dao;

import java.util.ArrayList;
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

import com.mobon.billing.branchUM.service.MongoUpdate;
import com.mobon.billing.model.v15.BaseCVData;

@Repository
public class KnoKpiDataDao {
	private static final Logger logger = LoggerFactory.getLogger(KnoKpiDataDao.class);

	String NAMESPACE= "knoKpiDataMapper";
	
	@Autowired
	private MongoUpdate MongoUpdate;

//	@Resource(name = "sqlSessionTemplateClickhouse")
//	private SqlSessionTemplate sqlSessionTemplateClickhouse;

	@Autowired
	private TransactionTemplate transactionTemplate;
	
//	public boolean transectionRuningV2(HashMap<String, ArrayList<BaseCVData>> flushMap) {
//		boolean result = false;
//
//		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
//			boolean res = false;
//			
//			public Object doInTransaction(TransactionStatus status) {
//				try {
//					
//					long startTime = 0;
//					long resutTime = 0;
//					for (Map.Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
//						String key = item.getKey();
//						ArrayList<BaseCVData> data = item.getValue();
//						
//						startTime = System.currentTimeMillis();
//						sqlSessionTemplateClickhouse.update(String.format("%s.%s", "knoKpiDataMapper", key), data);
//						sqlSessionTemplateClickhouse.flushStatements();
//						resutTime = System.currentTimeMillis() - startTime;
//						logger.info("resutTime size - {}, {}(ms)", data.size(), resutTime);
//					}
//					
//					res = true;
//				} catch (Exception e) {
//					logger.error("err transectionRuning ", e);
//					status.setRollbackOnly();
//					res = false;
//					
//				} finally {
//					logger.debug("succ transectionRuningV2 flush");
//				}
//				return res;
//			}
//		});
//		return result;
//	}
	
	public boolean transectionRuningV2Mongo(HashMap<String, ArrayList<BaseCVData>> flushMap) {
		boolean result = false;

		try {
			for (Map.Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
				String key = item.getKey();
				ArrayList<BaseCVData> data = item.getValue();
				//this.MongoUpdate.deleteAndInsertManyBaseCVData("um_stats", key, data);
				this.MongoUpdate.insertManyBaseCVData("um_stats", key, data);
			}
			result = true;
		} catch (Exception e) {
			logger.error("err ", e);
		}
		return result;
	}

}
