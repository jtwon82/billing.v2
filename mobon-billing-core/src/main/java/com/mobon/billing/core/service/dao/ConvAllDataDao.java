package com.mobon.billing.core.service.dao;

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
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.ConvData;

@Repository
public class ConvAllDataDao {

	private static final Logger	logger				= LoggerFactory.getLogger(ConvAllDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";

	@Value("${log.path.convsucc}")
	private String	logPathConvsucc;


	@Resource(name="sqlSessionTemplateConvBilling")
	private SqlSessionTemplate	sqlSessionTemplateConvBilling;
	
	@Resource(name="sqlSessionTemplateConvDream")
	private SqlSessionTemplate	sqlSessionTemplateConvDream;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Autowired
	private SumObjectManager		sumObjectManager;
	
	public boolean transectionRuningV3(List<ConvData> list){
		boolean result = false;
		
		long startTime = System.currentTimeMillis();
		
		try {
			for(ConvData vo : list){
				
				if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform( G.convertPLATFORM_CODE(vo.getPlatform()));
				if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
				if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun( G.convertTP_CODE(vo.getAdGubun()));

				if( !StringUtils.isNumeric(vo.getOsCode()) ) vo.setOsCode( G.convertOS_TP_CODE(vo.getOsCode()));
				if( !StringUtils.isNumeric(vo.getBrowserCode()) ) vo.setBrowserCode( G.convertBROWSER_TP_CODE(vo.getBrowserCode()));
				if( !StringUtils.isNumeric(vo.getDeviceCode()) ) vo.setDeviceCode( G.convertDEVICE_TP_CODE(vo.getDeviceCode()));
				vo.setBrowserCodeVersion( G.convertBROWSER_VERSION(vo.getBrowserCodeVersion()));
				
				boolean tmpbool = transectionRunning(vo);
				if( !tmpbool && vo.increaseRetryCnt()<=3 ) {
//					logger.info("ConvAll insert error retry - {}", vo);
					
					sumObjectManager.appendConvAllData(vo);
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
					sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvAllDataStats"), vo);		//MOB_CNVRS_HH_STATS
					sqlSessionTemplateConvBilling.flushStatements();

					// 전환키워드 수집
					if( !"".equals(vo.getKeywordType()) && !"".equals(vo.getKeywordValue()) && !"".equals(vo.getKeywordUrl())
							&& !"rebuild".equals(vo.getRegUserId()) ) {
						sqlSessionTemplateConvDream.update(String.format("%s.%s", NAMESPACE, "insertKwrdConvData"), vo);		// CNVRS_KWRD
						sqlSessionTemplateConvDream.flushStatements();
					}
					
					// continueConv 수집
					if( !"".equals(vo.getContinueConv()) ) {
						sqlSessionTemplateConvDream.update(String.format("%s.%s", NAMESPACE, "insertContinueCnvrs"), vo);		// CONTI_BUY_HIS
						sqlSessionTemplateConvDream.flushStatements();
					}
					if( !"".equals(vo.getLongContinueConv()) ) {
						sqlSessionTemplateConvDream.update(String.format("%s.%s", NAMESPACE, "insertLongContinueCnvrs"), vo);		// CONTI_BUY_HIS_LONG
						sqlSessionTemplateConvDream.flushStatements();
					}
					
					logger.debug("ConvAll conversion succ vo - {}", vo);
					
					res = true;
					
				} catch (Exception e) {
//					logger.error("err transectionRuning data-{}", vo, e);
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
