package com.mobon.billing.core.service.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.ConvData;

import net.sf.json.JSONObject;

@Repository
public class ConvAbusingDataDao {

	private static final Logger	logger				= LoggerFactory.getLogger(ConvAbusingDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";

	@Value("${log.path.convsucc}")
	private String	logPathConvsucc;


	@Resource(name="sqlSessionTemplateConvBilling")
	private SqlSessionTemplate	sqlSessionTemplateConvBilling;
	
	@Resource(name="sqlSessionTemplateConvDream")
	private SqlSessionTemplate	sqlSessionTemplateConvDream;
	
	@Resource(name="sqlSessionTemplateDreamSimpleActionSelect")
	private SqlSessionTemplate	sqlSessionTemplateDreamSimpleActionSelect;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private SumObjectManager		sumObjectManager;
	
	public boolean transectionRuningV3(List<ConvData> list){
		boolean result = false;
		
		long startTime = System.currentTimeMillis();
		
		try {
			for(ConvData vo : list){
				if ( vo.getOrdPcode()!=null && vo.getOrdPcode().length()>20 ) {
					vo.setOrdPcode( vo.getOrdPcode().substring(0, 20));
				}
				
				boolean tmpbool = transectionRunning(vo);
				if( !tmpbool && vo.increaseRetryCnt()<=3 ) {
					logger.info("ConvAbusing insert error - {}", vo);
					sumObjectManager.appendConvAbusingData(vo, null);
				}
			}
			result = true;
		} catch (Exception e) {
			logger.error("err transectionRuningV3 ", e);
			result = false;
		}
		finally{
			logger.debug("succ sqlSession flush");
			
			long endTime = System.currentTimeMillis();
	        long resutTime = endTime - startTime;
			logger.debug("Transaction BATCH Running Time (TBRT)  : " + resutTime / 1000 + "(sec)");
		}
		
		return result;
	}
	
	public boolean transectionRunning(ConvData vo){
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				try {
					
					sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvAbusingData"), vo);
					sqlSessionTemplateConvBilling.flushStatements();
					
					res = true;
					
				} catch (Exception e) {
					logger.error("err transectionRuning ", e);
					status.setRollbackOnly();
					res = false;
					
					logger.error("Conv insert rollback - {}", vo);
					
				} finally {
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}
		});
		return result;
	}

}
