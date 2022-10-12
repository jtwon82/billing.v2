package com.mobon.billing.hhtodd.schedule;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TaskAddtionData {
	
	private static final Logger		logger		= LoggerFactory.getLogger(TaskAddtionData.class);
	

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Resource(name="sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDreamSearch;

	@Autowired
	private TransactionTemplate transactionTemplate;
	
	
	public void runMEDIA_SCRIPT_STD_HIST() {
		boolean result = false;
		String NAMESPACE = "hHtoDDAddtionMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
	
			public Object doInTransaction(TransactionStatus status) {
				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
	
				try {
					Map param= new HashMap();
//					param.put("ADVRTS_TP_CODE", tpcode);
					
					sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "updateMEDIA_SCRIPT_STD_HIST"), param);
					sqlSessionTemplateBilling.flushStatements();
					logger.info("succ updateMEDIA_SCRIPT_STD_HIST");
					
					sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "updateMEDIA_RANK_WEIGHT_HIST"), param);
					sqlSessionTemplateBilling.flushStatements();
					logger.info("succ updateMEDIA_RANK_WEIGHT_HIST");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					status.setRollbackOnly();
					res = false;
					logger.error("runMEDIA_SCRIPT_STD_HIST Exception ==>", e);
	
				} finally {
					if (hisYN) {
						logger.info("runMEDIA_SCRIPT_STD_HIST 종료  : (TBRT) :{}, {}, {}", (startTime - System.currentTimeMillis()) +"(ms)");
					}
				}
				return res;
			}
		});
		if(result) {
			logger.info("result succ runMEDIA_SCRIPT_STD_HIST" );
		}
	}

}
