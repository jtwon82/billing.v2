package com.mobon.billing.pargatr.service.dao;

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

@Repository
public class ParGatrDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(ParGatrDataDao.class);

	public static final String	NAMESPACE	= "parGatrDataMapper";


	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
//	@Resource (name = "sqlSessionTemplateBillingKwrd")
//	private SqlSessionTemplate sqlSessionTemplateBillingKwrd;

	@Resource (name = "sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDream;

	@Autowired
	private TransactionTemplate transactionTemplate;

	

	
	public boolean transectionRuningV2(HashMap<String, ArrayList<BaseCVData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					int totalDreamSize = 0;
					long startTime = 0;
					long endTime = 0;
					long resutTime = 0;
					
					for (Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
						ArrayList<BaseCVData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						logger.debug("CLICKVIEW dataKey - {}", dataKey);
						logger.debug("CLICKVIEW dataSize - {}", dataSize);
						
						if (dataSize > 0) {
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalBillingSize += dataSize;
						}
					}
					
					if(totalBillingSize>0) {
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.flushStatements();
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("TR Time (TBRT) Billing ClickView : " + resutTime / 1000 + "(sec) totalsize - "+ totalBillingSize);
					}

					if( totalDreamSize>0 ) {
						startTime = System.currentTimeMillis();
						sqlSessionTemplateDream.flushStatements();
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("TR Time (TBRT) Dream ClickView : " + resutTime / 1000 + "(sec) totalsize - "+ totalDreamSize);
					}
					
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning ", e);
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
}
