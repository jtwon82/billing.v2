package com.mobon.billing.uniidstats.service.dao;

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

import com.mobon.billing.uniidstats.model.UseridVo;


@Repository
public class UseridVoDao {

	private static final Logger	logger = LoggerFactory.getLogger(UseridVoDao.class);

	public static final String NAMESPACE = "useridVoMapper";


	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;

	/**
	 * insert or update 트랜잭션 처리
	 * @param flushMap - group 처리된 Map
	 * @return true:정상처리, false:처리오류
	 */
	public boolean transectionRuningV2(HashMap<String, ArrayList<UseridVo>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			long startTime= System.currentTimeMillis();
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					
					for (Entry<String, ArrayList<UseridVo>> item : flushMap.entrySet()) {
						ArrayList<UseridVo> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						if (dataSize > 0) {
							logger.info("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalBillingSize += dataSize;
						}
					}

					sqlSessionTemplateBilling.flushStatements();
					long resutTime = System.currentTimeMillis() - startTime;
					logger.info("TR Time (TBRT) Billing UseridVo : " + resutTime +"(ms) totalsize - "+ totalBillingSize);
					
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
