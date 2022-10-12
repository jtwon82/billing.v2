package com.mobon.billing.report.service.dao;

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

import com.mobon.billing.model.v15.NativeNonAdReportData;

@Repository
public class NativeNonAdReportDataDao {

	private static final Logger	logger = LoggerFactory.getLogger(NativeNonAdReportDataDao.class);
	public static final String	NAMESPACE = "nativeDataMapper";

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;

	/**
	 * key list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList selectGroupKey() {
		return (ArrayList) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey"));
	}
	
	/**
	 * Non-ad stats
	 * @param flushMap
	 * @return
	 */
	public boolean transectionRuningV2(HashMap<String, ArrayList<NativeNonAdReportData>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalDreamSize = 0;
					
					for (Entry<String, ArrayList<NativeNonAdReportData>> item : flushMap.entrySet()) {
						ArrayList<NativeNonAdReportData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						if (dataSize > 0) {
							logger.debug("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalDreamSize += dataSize;
						}
					}

					long startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.flushStatements();
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("TR Time (TBRT) Billing NativeNonAdReportData : " + resutTime / 1000 + "(sec) totalsize - "+ totalDreamSize);

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
