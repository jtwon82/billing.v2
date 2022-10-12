package com.mobon.billing.branch.service.dao;

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

import com.mobon.billing.model.v15.NearData; 

@Repository
public class NearDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(NearDataDao.class);

	public static final String	NAMESPACE	= "nearDataMapper";


	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Resource (name = "sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDream;

	@Autowired
	private TransactionTemplate transactionTemplate;

	

	
	public boolean transectionRuningV2(HashMap<String, ArrayList<NearData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					int totalDreamSize = 0;
					
					for (Entry<String, ArrayList<NearData>> item : flushMap.entrySet()) {
						ArrayList<NearData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						if (dataSize > 0) {
							logger.debug("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							if (dataKey.indexOf("point") > 0) {
								sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, dataKey), data);
								totalDreamSize += dataSize;
							}
							else {
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
								totalBillingSize += dataSize;
							}
						}
					}

					long startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.flushStatements();
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Billing NearData : " + resutTime / 1000 + "(sec) totalsize - "+ totalBillingSize);

					startTime = System.currentTimeMillis();
					sqlSessionTemplateDream.flushStatements();
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Dream NearData : " + resutTime / 1000 + "(sec) totalsize - "+ totalDreamSize);
					
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
