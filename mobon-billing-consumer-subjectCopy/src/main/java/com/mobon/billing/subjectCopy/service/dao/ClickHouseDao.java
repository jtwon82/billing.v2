package com.mobon.billing.subjectCopy.service.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mobon.billing.subjectCopy.vo.ConversionPolling;

@Repository
public class ClickHouseDao {
	private static final Logger logger = LoggerFactory.getLogger(ClickHouseDao.class);

	public static final String NAMESPACE = "ClickHouseMapper";

	@Resource(name = "sqlSessionTemplateClickhouse")
	private SqlSessionTemplate sqlSessionTemplateBillingClickHouse;


	@Autowired
	private TransactionTemplate transactionTemplate;

	public String getTestDate() {
		Map <String, Object> result = null;
		SimpleDateFormat transFormat = new SimpleDateFormat("YYYY-MM-DD");
		String date = null;
		try {
			result = sqlSessionTemplateBillingClickHouse.selectOne(String.format("%s.%s", NAMESPACE,"selectNow123"));
			date = transFormat.format(result.get("NOW"));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return date;
	}

	public boolean transectionRuningV2(HashMap<String, ArrayList<BaseCVData>> flushMap) {
		boolean result = false ; 
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {
				try {

					long startTime = 0;
					long resutTime = 0;

					for (Map.Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
						ArrayList<BaseCVData> data = item.getValue();
						if (data == null) {
							logger.info("data is null - {}" ,data);
						}
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBillingClickHouse.update(String.format("%s.%s", NAMESPACE, "viewClickDataInsert"), data);
						sqlSessionTemplateBillingClickHouse.flushStatements();							
						resutTime = System.currentTimeMillis() - startTime;						
						logger.info("ClickViewResutTime size - {}, {}(ms)", data.size(), resutTime);
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

	public boolean transectionRuningConversion(Map<String, List<ConversionPolling>> aggregateList) {
		logger.info("SUbjectConversion Data insert START");
		boolean result = false;
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;		

			public Object doInTransaction(TransactionStatus status) { 
				try {
					long startTime = 0;
					long resutTime = 0;
					for (Map.Entry<String, List<ConversionPolling>> item : aggregateList.entrySet()) {
						List <ConversionPolling> data = item.getValue();
						if (data == null) {
							logger.info("convData is null");
						}
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBillingClickHouse.update(String.format("%s.%s", NAMESPACE, "conversionInsert"), data);
						sqlSessionTemplateBillingClickHouse.flushStatements();	
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("ConversionresutTime size - {}, {}(ms)", data.size(), resutTime);
						
					}
					res = true;
				} catch (Exception e) {
					logger.error("err transactionRunning Conversion ", e);
					status.setRollbackOnly();
					res = false;
				} finally {
					logger.debug("succ transectionRunningV3 flush");
				}
				return res;
			}
		});
		return result;
	}

	public void migrationClickHouseData(Map<String, Object> param) {
		logger.info("Migration ClickHouse");
		long startTime = 0;
		long resultTime = 0;
		try {
			startTime = System.currentTimeMillis();
			sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "migrationClickHouse"), param);
			sqlSessionTemplateBillingClickHouse.flushStatements();	
			resultTime = System.currentTimeMillis() - startTime;
			logger.info("End Migration ClickHouse resultTime - {}(ms)", resultTime);			
		} catch (Exception e) {
			logger.error("err Migration DB ERROR - {}", e);
		}		
	}

	public void migrationClickHouseSumData(Map<String, Object> param) {
		logger.info("SUM TABLE MIGRATION START");
		long startTime = 0;
		long resultTime = 0;
		try {

			startTime = System.currentTimeMillis();
			sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "migrationClickConvSumMigration"), param);
			sqlSessionTemplateBillingClickHouse.flushStatements();
			resultTime = System.currentTimeMillis() - startTime;
			logger.info("End SumMigration ClickHouse resultTime - {}(ms)", resultTime);
			
		} catch (Exception e) {
			logger.error("err SumMigration DB ERROR - {}", e);
		}
		
	}

	public void migrationclickHouseFrameData(Map<String, Object> param) {
		logger.info("SUM TABLE FRAME MIGRATION START");
		long startTime = 0;
		long resultTime = 0;
		try {
			startTime = System.currentTimeMillis();
			sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "migrationFrmeClickHouse"), param);
			sqlSessionTemplateBillingClickHouse.flushStatements();	
			resultTime = System.currentTimeMillis() - startTime;
			logger.info("End Migration FRAMEClickHouse resultTime - {}(ms)", resultTime);			
		} catch (Exception e) {
			logger.error("err Migration FRAME DB ERROR - {}", e);
		}		
		
	}
	
	public void insertUniqueClickData(List<Map<String, Object>> result) {
	logger.info("SUM TABLE Unique MIGRATION START");
	long startTime = 0;
	long resultTime = 0;
	try {
		startTime = System.currentTimeMillis();
		sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "migrationUniqueClickHouse"), result);
		sqlSessionTemplateBillingClickHouse.flushStatements();	
		resultTime = System.currentTimeMillis() - startTime;
		logger.info("End Migration UniqueClickHouse resultTime - {}(ms)", resultTime);			
	} catch (Exception e) {
		logger.error("err Migration Unique DB ERROR - {}", e);
	}		
	
}

	public void adverGrpSumStats() {
		logger.info("AdverGrpSumStats Table Insert START");
		long startTime = 0;
		long resultTime = 0;
		try {
			startTime = System.currentTimeMillis();
			sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "adverGrpSumStats"));
			sqlSessionTemplateBillingClickHouse.flushStatements();
			resultTime = System.currentTimeMillis() - startTime;
			logger.info("End  AdverGrpSumStats resultTime - {}(ms)", resultTime);
		} catch (Exception e) {
			logger.error("err AdverGrpSumStats DB ERROR - {}", e);
		}
	}

	public void adverGrpSumStatsMigration(Map<String, Object> param) {

		logger.info("AdverGrpSumStatsMigration Table Insert START");
		long startTime = 0;
		long resultTime = 0;
		try {
			startTime = System.currentTimeMillis();
			sqlSessionTemplateBillingClickHouse.insert(String.format("%s.%s", NAMESPACE, "adverGrpSumStatsMigration"),param);
			sqlSessionTemplateBillingClickHouse.flushStatements();
			resultTime = System.currentTimeMillis() - startTime;
			logger.info("End  AdverGrpSumStatsMigration resultTime - {}(ms) param - {}", resultTime, param.get("diffDay"));
		} catch (Exception e) {
			logger.error("err AdverGrpSumStatsMigration DB ERROR - {}", e);
		}
	}
}

