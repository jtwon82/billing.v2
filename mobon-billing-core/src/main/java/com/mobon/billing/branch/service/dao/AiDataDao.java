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

import com.mobon.billing.model.v15.BaseCVData; 

/**
 * AiDataDao
 * 캠페인 생성에 Ai 사용여부 관련 DB 트랜젝션 처리 클래스
 * 
 * @author  : sjpark3
 * @since   : 2021-12-15
 */
@Repository
public class AiDataDao {	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	public static final String NAMESPACE = "aiDataMapper";
	private static final Logger logger = LoggerFactory.getLogger(AiDataDao.class);

	/**
	 * transectionRuningV2
	 * 트랜젝션 러닝 메소드
	 * 
	 * @author  : sjpark3
	 * @since   : 2021-12-15
	 */
	public boolean transectionRuningV2(HashMap<String, ArrayList<BaseCVData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {
					int totalBillingSize = 0;
					long startTime = System.currentTimeMillis();
					long endTime = 0;
					long resutTime = 0;
					
					for (Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
						ArrayList<BaseCVData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						logger.debug("Ai dataKey - {}", dataKey);
						logger.debug("Ai dataSize - {}", dataSize);
						
						if (dataSize > 0) {
							logger.debug("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalBillingSize += dataSize;
						}
					}
					
					if (totalBillingSize>0) {						
						sqlSessionTemplateBilling.flushStatements();
						
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						
						logger.info("TR Time (TBRT) Billing IntgCntr : " + resutTime + "(ms) totalsize - "+ totalBillingSize);
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
