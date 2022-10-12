package com.mobon.billing.hhtodd.service.dao;

import java.util.List;
import java.util.Map;

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

@Repository
public class HHtoDDConvDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoDDConvDao.class);

	public static final String	NAMESPACE	= "hHtoDDConvMapper";
	
	@Value("${log.path}")
	private String logPath;

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;


	public boolean transectionRuningHHtoHHConv(Map param) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;

				long startTime = System.currentTimeMillis();
				long resutTime;

				try {
					
					List<Map> listStatsHh = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_ctgr_mod_date_dd"));
					
					if( listStatsHh!=null ) {
						for( Map STATS_HH : listStatsHh ) {
							logger.info("STATS_HH -{}", STATS_HH);
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "insertCtgrCnvrs"), STATS_HH);
						}
					}

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("CTGR_CONV transectionRuningHHtoHHConv Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("CTGR_CONV 배치 종료  : Transaction BATCH Running Time (TBRT)  : {}", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}
	
}
