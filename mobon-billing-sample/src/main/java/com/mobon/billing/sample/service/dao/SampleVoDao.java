package com.mobon.billing.sample.service.dao;

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

import com.mobon.billing.sample.model.SampleVo;


@Repository
public class SampleVoDao {

	private static final Logger	logger	= LoggerFactory.getLogger(SampleVoDao.class);

	public static final String	NAMESPACE	= "sampleVoMapper";


	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

//	@Resource (name = "sqlSessionTemplateDream")
//	private SqlSessionTemplate sqlSessionTemplateDream;

	@Autowired
	private TransactionTemplate transactionTemplate;

	
	public String selectNOW() {
		return sqlSessionTemplateBilling.selectOne(NAMESPACE +".selectNOW", null);
	}
	
	public boolean transectionRuningV2(HashMap<String, ArrayList<SampleVo>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					
					for (Entry<String, ArrayList<SampleVo>> item : flushMap.entrySet()) {
						ArrayList<SampleVo> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						if (dataSize > 0) {
							logger.info("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalBillingSize += dataSize;
						}
					}

					long startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.flushStatements();
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Billing SampleVo : " + resutTime +"(ms) totalsize - "+ totalBillingSize);
					
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
