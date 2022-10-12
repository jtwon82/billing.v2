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

import com.mobon.billing.model.v15.PluscallLogData; 

/**
 * PluscallLogDataDao
 * 플러스콜 유효콜 관련 DB 트랜젝션 처리 클래스
 * 
 * @author  : sjpark3
 * @since   : 2022-01-04
 */
@Repository
public class PluscallLogDataDao {
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Value("${log.path}")
	private String logPath;	

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	private static final String	NAMESPACE	= "pluscallLogDataMapper";
	private static final Logger	logger	= LoggerFactory.getLogger(PluscallLogDataDao.class);
	
	/**
	 * transectionRuningV2
	 * 트랜젝션 러닝 메소드
	 * 
	 * @author  : sjpark3
	 * @since   : 2022-01-04
	 */
	public boolean transectionRuningV2(HashMap<String, ArrayList<PluscallLogData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {				
				try {
					int totalsize = 0;
					long startTime = System.currentTimeMillis();
					long endTime = 0;
					long resutTime = 0;
					
					for (Entry<String, ArrayList<PluscallLogData>> item : flushMap.entrySet()) {
						int itemSize = item.getValue().size();
						String dataKey = item.getKey();
						ArrayList<PluscallLogData> data = item.getValue();
						
						logger.debug("PluscallLogData dataKey - {}", dataKey);
						logger.debug("PluscallLogData dataSize - {}", itemSize);
						
						if (itemSize>0) {
							if (dataKey.indexOf("billing") > 0) {
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
								totalsize += itemSize;
							}
						}
					}

			        if (totalsize>0) {
			        	endTime = System.currentTimeMillis();
				        resutTime = endTime - startTime;
				        
				        sqlSessionTemplateBilling.flushStatements();
						logger.info("TR Time (TBRT) Billing PluscallLogData : " + resutTime + "(ms) totalsize - "+ totalsize);
			        }
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning msg - {}", e);
					status.setRollbackOnly();
					res = false;
					
				} finally {
					
				}
				return res;
			}
		});
		
		logger.debug("result - {}", result);
		
		return result;
	}
	
}
