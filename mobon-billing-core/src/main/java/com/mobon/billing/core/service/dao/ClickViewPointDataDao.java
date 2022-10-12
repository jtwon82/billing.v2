package com.mobon.billing.core.service.dao;

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
public class ClickViewPointDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(ClickViewPointDataDao.class);

	public static final String	NAMESPACE	= "clickViewPointDataMapper";

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

					int totalDreamSize = 0;
					long startTime = 0;
					long endTime = 0;
					long resutTime = 0;
					
					for (Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
						ArrayList<BaseCVData> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();
						
						logger.debug("CLICKVIEWPoint dataKey - {}", dataKey);
						logger.debug("CLICKVIEWPoint dataSize - {}", dataSize);
						
						if (dataSize > 0) {
							logger.debug("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							if (dataKey.indexOf("point") > 0) {
								if (dataKey.indexOf("billing") > 0) {
									sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, dataKey), data);
									totalDreamSize += dataSize;
								} 
							}
						}
							
					}
					
					if( totalDreamSize>0 ) {
						startTime = System.currentTimeMillis();
						sqlSessionTemplateDream.flushStatements();
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("TR Time (TBRT) Dream ClickViewPoint : " + resutTime + "(ms) totalsize - "+ totalDreamSize);
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
