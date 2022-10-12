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

import com.mobon.billing.model.v15.ActionLogData; 

@Repository
public class IntgCntrActionDataDao {
	
	private static final Logger	logger	= LoggerFactory.getLogger(IntgCntrActionDataDao.class);
	
	private static final String	NAMESPACE	= "actionDataMapper";
	
	@Resource (name = "sqlSessionTemplateBillingSIMPLE")
	private SqlSessionTemplate sqlSessionTemplateBillingSIMPLE;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Value("${log.path}")
	private String	logPath;
	
	
	
	public boolean transectionRuningV2(HashMap<String, ArrayList<ActionLogData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				try {
					int totalBillingSIMPLESize = 0;
					for(Entry<String, ArrayList<ActionLogData>> item : flushMap.entrySet() ){
						String dataKey = item.getKey();
						ArrayList<ActionLogData> data = item.getValue();
						int itemSize = item.getValue().size();
						logger.debug("IntgCntrACTION_LOG dataKey - {}", dataKey);
						logger.debug("IntgCntrACTION_LOG dataSize - {}", itemSize);
						
						if( itemSize>0 ){
							if ( dataKey.indexOf("billing_billing") > 0 && itemSize>0 ) {
								logger.debug("data - {}", data);
								
								for( ActionLogData row : data ) {
									sqlSessionTemplateBillingSIMPLE.update(String.format("%s.%s", NAMESPACE, dataKey), row);
									sqlSessionTemplateBillingSIMPLE.update(String.format("%s.%s", NAMESPACE, "insertActIntgLog_billing_billing"), row);
									logger.debug("row.actSeq - {}", row.getActSeq());
								}
								
								totalBillingSIMPLESize += itemSize;
							}
						}
					}

			        if(totalBillingSIMPLESize>0) {
			        	long endTime = System.currentTimeMillis();
				        long resutTime = endTime - startTime;
				        sqlSessionTemplateBillingSIMPLE.flushStatements();
						logger.info("TR Time (TBRT) Billing IntgCntrActionData : " + resutTime + "(ms) totalsize - "+ totalBillingSIMPLESize);
			        }
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning msg - {}", e);
					status.setRollbackOnly();
					res = false;
					
				} finally {
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("Transaction BATCH Running Time (TBRT)  : " + resutTime / 1000 + "(sec)");
				}
				return res;
			}
		});
		logger.debug("result - {}", result);
		
		return result;
	}
}
