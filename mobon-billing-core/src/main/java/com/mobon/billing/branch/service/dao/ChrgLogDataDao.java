package com.mobon.billing.branch.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.v15.ChrgLogData; 

@Repository
public class ChrgLogDataDao {
	
	private static final Logger	logger	= LoggerFactory.getLogger(ChrgLogDataDao.class);
	
	private static final String	NAMESPACE	= "chrgLogDataMapper";
	

	@Resource (name = "sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDream;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Value("${log.path}")
	private String	logPath;	
	
	public boolean transectionRuningV2(HashMap<String, ArrayList<ChrgLogData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				
				try {
					int totalsize = 0;
					for (Entry<String, ArrayList<ChrgLogData>> item : flushMap.entrySet()) {
						String dataKey = item.getKey();
						ArrayList<ChrgLogData> data = item.getValue();
						int itemSize = item.getValue().size();
						
						logger.debug("CHRG_LOG dataKey - {}", dataKey);
						logger.debug("CHRG_LOG dataSize - {}", itemSize);
						
						if (itemSize>0) {
							if (dataKey.indexOf("billing") > 0) {
								sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, dataKey), data);
								totalsize += itemSize;
							} else {
								//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), item.getValue());
							}
						}
					}

			        if (totalsize>0) {
			        	long endTime = System.currentTimeMillis();
				        long resutTime = endTime - startTime;
				        
				        sqlSessionTemplateDream.flushStatements();
						logger.info("TR Time (TBRT) Dream ChrgLogData : " + resutTime + "(ms) totalsize - "+ totalsize);
			        }
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning msg - {}", e);
					status.setRollbackOnly();
					res = false;
					
				} finally {
					//long endTime = System.currentTimeMillis();
					//long resutTime = endTime - startTime;
					//logger.info("Transaction BATCH Running Time (TBRT)  : " + resutTime / 1000 + "(sec)");
				}
				return res;
			}
		});
		logger.debug("result - {}", result);
		
		return result;
	}
	
}
