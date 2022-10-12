package com.mobon.billing.core.service.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.adgather.constants.G;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.ConvData;

@Repository
public class IntgCntrConvDataDao {

	private static final Logger	logger				= LoggerFactory.getLogger(IntgCntrConvDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";


	@Resource(name="sqlSessionTemplateConvBilling")
	private SqlSessionTemplate	sqlSessionTemplateConvBilling;
	
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
				
//				if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform( G.convertPLATFORM_CODE(vo.getPlatform()));
//				if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
//				if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun( G.convertTP_CODE(vo.getAdGubun()));
				
				// 길이제한.
//				if( vo.get("ordPcode")!=null && vo.get("ordPcode").toString().length()>20 ) {
//					vo.put("ordPcode", vo.get("ordPcode").toString().substring(0,20) );
//				}
				if ( vo.getOrdPcode()!=null && vo.getOrdPcode().length()>20 ) {
					vo.setOrdPcode( vo.getOrdPcode().substring(0, 20));
				}
				
				boolean tmpbool = transectionRunning(vo);
				if( !tmpbool && vo.increaseRetryCnt()<=3 ) {
					logger.info("ConvV2 insert error - {}", vo);
					sumObjectManager.appendIntgCntrConvData(vo);
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
			logger.debug("Transaction BATCH Running Time (TBRT)  : " + resutTime + "(ms)");
		}
		
		return result;
	}
	
	public boolean transectionRunning(ConvData vo){
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				try {
					String ORDCODE = ""; //vo.getChkingOrdCode();
					
					ConvData ordcode = selectCnvrsLog(vo);
					if(ordcode != null) ORDCODE= ordcode.getOrdCode();
					if (StringUtils.isEmpty(ORDCODE)) {
						
						logger.debug("intgSeqs - {}", vo.getIntgSeqs());
						
						if(vo.isbHandlingStatsPointMobon()) {
						}
						else {
							if(vo.getIntgSeqs()!=null) {
								vo.setIntgLogCnt(vo.getIntgSeqs().size());
								
								for(Map.Entry<String, String> item : vo.getIntgSeqs().entrySet() ){
									vo.setIntgTpCode(item.getKey());
									vo.setIntgSeq(item.getValue());
									logger.debug("intgTpCode-{}, intgSeq-{}", vo.getIntgTpCode(), vo.getIntgSeq());
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertCnvrsIntgStats"), vo);	//MOB_CNVRS_INTG_LOG
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertCnvrsIntgDtlStats"), vo);		//MOB_CNVRS_DTL_STATS
									
									if("07".equals(vo.getIntgTpCode())) {
										logger.debug("ConvV2 vo -{}", vo);
										sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertCnvrsIntgTTime"), vo);
									}
								}
							}
							
							sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertCnvrsLog"), vo);			//MOB_CNVRS_LOG
						}
						
						sqlSessionTemplateConvBilling.flushStatements();
						
						logger.info("ConvV2 conversion succ vo - {}", vo);
					}
					else {
						logger.info("ConvV2 conversion overlab orderno vo - {}", vo);
					}
					
					res = true;
					
				} catch (Exception e) {
					logger.error("err transectionRuning ConvV2 insert rollback - {}", vo, e);
					status.setRollbackOnly();
					res = false;
					
					logger.info("ConvV2 insert rollback - {}", vo);
					
				} finally {
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}
		});
		return result;
	}
	

	public ConvData selectActLog(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("ConvV2 selectActLog : {}", data);
			ArrayList<ConvData> tmp = (ArrayList) sqlSessionTemplateConvBilling.selectList(String.format("%s.%s", NAMESPACE, "selectActLog"), data);
			logger.debug("tmp - {}", tmp);
			
			if( tmp.size()>0 ) {
				result = tmp.get(0);

				for( ConvData row : tmp ) {
					logger.debug("row {}", row);
					result.getIntgSeqs().put(row.getIntgTpCode(), row.getIntgSeq());
				}
				logger.debug("result.IntgSeqs - {}", result.getIntgSeqs());
			}
			else if ("Y".equals(data.get("crossbrYn"))){
				logger.debug("ConvV2 convLogActData_C : {}", data);
				tmp = (ArrayList) sqlSessionTemplateDreamSimpleActionSelect.selectList(String.format("%s.%s", NAMESPACE, "convLogActData_C"), data);
				if( tmp.size()>0 ) {
					result = tmp.get(0);
					result.setIntgTpCode("06");
				}

				if(result!=null) {
					result.getIntgSeqs().put(result.getIntgTpCode(), result.getIntgSeq());
					logger.debug("result.IntgSeqs - {}", result.getIntgSeqs());
				}
			}
		}catch(Exception e) {
			logger.error("err data - {}, msg - {}", data, e);
		}
		return result;
	}

	public ConvData selectCnvrsLog(ConvData data) {
		ConvData result = null;
		try {
			logger.debug("ConvV2 selectCnvrsLog : {}", data);
			result = (ConvData) sqlSessionTemplateConvBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectCnvrsLog"), data);
		}catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}
	
}
