package com.mobon.billing.hhtodd.service.dao;

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

@Repository
public class HHtoMTHAdverMTHhhDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoMTHAdverMTHhhDao.class);

	public static double fixCount = 100000;
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;


	public boolean transectionRuningDDtoDDAdverMTHhh(Map param) {
		boolean result = false;
		String ADVERMThh = "hHtoDDAdverMTHhhMapper";

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {

					logger.info("ADVER_HTM_HH sql_dd_select_tp_code_count START");
					List<Map> listTpCode = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", ADVERMThh, "sql_SELECT_COM_CODE"));
					
					for (Map tpCode : listTpCode) {
						logger.info("ADVER_HTM_HH update_TRGT_ADVER_MTH_HH_STATS START, m3 - {}", tpCode);
						
						// 데이터 저장하기
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", ADVERMThh, "update_TRGT_ADVER_MTH_HH_STATS"), tpCode);
						sqlSessionTemplateBilling.flushStatements();
						
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("ADVER_HTM_HH {} Transaction BATCH Running Time (TBRT)  : {}, {}", tpCode, resutTime / 1000 + "(sec)");
					
					}
				
					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("ADVER_MTH_HH transectionRuningDDtoDDAdverMTHhh Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("ADVER_MTH_HH 일데이타 배치 종료  : Transaction BATCH Running Time (TBRT)  :{}, {}",
								"update_TRGT_ADVER_MTH_HH_STATS", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}
}
