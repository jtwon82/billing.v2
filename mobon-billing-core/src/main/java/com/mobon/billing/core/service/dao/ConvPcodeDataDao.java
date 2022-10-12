package com.mobon.billing.core.service.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.mobon.conversion.domain.old.ConversionInfo;
import com.mobon.conversion.domain.old.ConversionInfoFilter;

@Repository
public class ConvPcodeDataDao {

	private static final Logger	logger				= LoggerFactory.getLogger(ConvPcodeDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";

	@Value("${log.path.convsucc}")
	private String	logPathConvsucc;


	@Resource(name="sqlSessionTemplateBilling")
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
				
//				if( !StringUtils.isNumeric(vo.getPlatform()) ) vo.setPlatform( G.convertPLATFORM_CODE(vo.getPlatform()));
//				if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
//				if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun( G.convertTP_CODE(vo.getAdGubun()));
				
				if( !StringUtils.isNumeric(vo.getOsCode()) ) vo.setOsCode( G.convertOS_TP_CODE(vo.getOsCode()));
				if( !StringUtils.isNumeric(vo.getBrowserCode()) ) vo.setBrowserCode( G.convertBROWSER_TP_CODE(vo.getBrowserCode()));
				if( !StringUtils.isNumeric(vo.getDeviceCode()) ) vo.setDeviceCode( G.convertDEVICE_TP_CODE(vo.getDeviceCode()));
				vo.setBrowserCodeVersion( G.convertBROWSER_VERSION(vo.getBrowserCodeVersion()));
				
				// 길이제한.
//				if( vo.get("ordPcode")!=null && vo.get("ordPcode").toString().length()>20 ) {
//					vo.put("ordPcode", vo.get("ordPcode").toString().substring(0,20) );
//				}
				if ( vo.getOrdPcode()!=null && vo.getOrdPcode().length()>20 ) {
					vo.setOrdPcode( vo.getOrdPcode().substring(0, 20));
				}
				
				boolean tmpbool = transectionRunning(vo);
				if( !tmpbool && vo.increaseRetryCnt()<=3 ) {
					logger.info("ConvPcode insert error - {}", vo);
					sumObjectManager.appendData(vo, false);
				}
				//추천 알고리즘 AB TEST 통계 데이터 적재 로직
				String [] algoCodeArr = {"00","04","06","09","10","11","12","13","14","15","16","17","18","19","22","25","33","34"};

				if (tmpbool) {
					for (String algoCode : algoCodeArr) {
						if (vo.getRecomAlgoCode().equals(algoCode)) {
							String abTestTy =  this.getAbTestResult(vo.getAbTests());
							if (! "".equals(abTestTy)) {
								vo.setAbTestTy(abTestTy);
								sumObjectManager.appendConvABPcodeRecom(vo);

							}
						}
					}
				}
				//////////////////////////////////////

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

	private String getAbTestResult(String abTests) {
		//abTests 값에 대한 null 처리
		if (abTests == null || "".equals(abTests)) {
			return "";
		}
		String[] spAbTest = abTests.split("[|]");

		for (String abTestData : spAbTest) {
			if (abTestData.startsWith("BI")) {
				return abTestData;
			}
		}
		return "";
	}


	public boolean transectionRunning(ConvData vo){
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				try {
					ConvData ordcode = selectCNVRS_PCODE_RECOM_NCL(vo);
					String ORDCODE = ""; //vo.getChkingOrdCode();
					if(ordcode != null) ORDCODE= ordcode.getOrdCode();
					if (StringUtils.isEmpty(ORDCODE)) {
						
						// ConvPcode 컨버전 
						sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvPcodeData"), vo);			//MOB_CNVRS_NCL
						sqlSessionTemplateConvBilling.flushStatements();
						
						logger.info("TR Time (TBRT) Conversion : " + (System.currentTimeMillis() - startTime) + "(ms) "+ vo.getKeyIp());
						logger.info("ConvPcode conversion succ vo - {}", vo);
					}
					else {
						logger.info("ConvPcode conversion overlab orderno vo - {}", vo);
					}
					
					res = true;
//					try {
//						String fileName = String.format("%s%s.%s", logPathConvsucc, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//						if( !"".equals(fileName) ) {
//							JSONObject jSONObject = JSONObject.fromObject(vo);
//							org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//									, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), G.ConversionSuccData, jSONObject), "UTF-8", true);
//							
//							//sender.send(G.ConversionSuccData, jSONObject.toString());
//						}
//					} catch (IOException e) {
//						logger.error("logging err", e);
//					}
					
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


	public ConvData convLogActData_C(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("Conv convLogActData_C : {}", data);
			ArrayList<ConvData> tmp = (ArrayList) sqlSessionTemplateDreamSimpleActionSelect.selectList(String.format("%s.%s", NAMESPACE, "convLogActData_C"), data);
			if( tmp.size()>0 ) {
				result = tmp.get(0);  
			}
		}catch(Exception e) {
			logger.error("err data - {}, msg - {}", data, e);
		}
		return result;
	}
	public ConvData convLogActData_CRetry(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("Conv convLogActData_CRetry : {}", data);
			ArrayList<ConvData> tmp = (ArrayList) sqlSessionTemplateDreamSimpleActionSelect.selectList(String.format("%s.%s", NAMESPACE, "convLogActData_CRetry"), data);
			if( tmp.size()>0 ) {
				result = tmp.get(0);  
			}
		}catch(Exception e) {
			logger.error("err data - {}, msg - {}", data, e);
		}
		return result;
	}

	public ConvData selectCNVRS_NCL(ConvData data) {
		ConvData result = null;
		try {
			logger.debug("selectCNVRS_NCL : {}", data);
			result = (ConvData) sqlSessionTemplateConvBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectCNVRS_NCL"), data);
		}catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}

	public ConvData selectCNVRS_PCODE_RECOM_NCL(ConvData data) {
		ConvData result = null;
		try {
			logger.debug("selectCNVRS_PCODE_RECOM_NCL : {}", data);
			result = (ConvData) sqlSessionTemplateConvBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectCNVRS_PCODE_RECOM_NCL"), data);
		}catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}
	
	public void insConvLog(ConversionInfo convInfo, ConvData data) {
		if(data.isbHandlingStatsPointMobon()) {
			//sqlSessionTemplateBilling.insert(String.format("%s.%s", NAMESPACE, "insConvLog"), convInfo);
		} else {
			sqlSessionTemplateConvDream.insert(String.format("%s.%s", NAMESPACE, "insConvLog_billing"), convInfo);
		}
	}
	
	public List<ConversionInfo> selConvLogs(ConversionInfoFilter filter, ConvData data) {
		List<ConversionInfo> result = null;
		
		if( data.isbHandlingStatsPointMobon() ) {
			//sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selConvLog"), filter);
		} else {
			result = sqlSessionTemplateConvDream.selectList(String.format("%s.%s", NAMESPACE, "selConvLog_billing"), filter);
		}
		
		return result;
	}
	
}
