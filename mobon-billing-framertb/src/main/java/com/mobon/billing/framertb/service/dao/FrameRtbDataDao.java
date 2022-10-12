package com.mobon.billing.framertb.service.dao;

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

import com.mobon.billing.model.v15.FrameRtbData; 

@Repository
public class FrameRtbDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(FrameRtbDataDao.class);

	public static final String	NAMESPACE	= "frameDataMapper";


	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;

	public boolean transectionRuningV2(HashMap<String, ArrayList<FrameRtbData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					
					for (Entry<String, ArrayList<FrameRtbData>> item : flushMap.entrySet()) {
						ArrayList<FrameRtbData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						if (dataSize > 0) {
							if(!"".equals(dataKey) & !dataKey.equals(null) ) {
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
								totalBillingSize += dataSize;
							}
						}
					}

					long startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.flushStatements();
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Billing FrameRtbData : " + resutTime / 1000 + "(sec) totalsize - "+ totalBillingSize);

					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning {}", e, flushMap.entrySet());
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
