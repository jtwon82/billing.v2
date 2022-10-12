package com.mobon.billing.core.service.dao;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.SerializationUtils;
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
import com.mobon.billing.core.producer.Sender;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ArrayHelper;
import com.mobon.code.CNVRS_ABUSING_TP_CODE;
import com.mobon.conversion.domain.old.ConversionInfo;
import com.mobon.conversion.domain.old.ConversionInfoFilter;

import net.sf.json.JSONObject;

@Repository
public class ConvDataDao {

	private static final Logger	logger				= LoggerFactory.getLogger(ConvDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";

	@Value("${log.path.convsucc}")
	private String	logPathConvsucc;
	
	@Value("${ai.adverids:@null}")
	private String	adverids;

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
	
	@Autowired
	private Sender sender;
	
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

				//상품 타겟팅 여부 확인용 메소드
				if (vo.getAdGubun() != null) {
					vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
				}

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
					logger.info("Conv insert error - {}", vo);
					sumObjectManager.appendData(vo, false);
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
					ConvData ordcode = selectCNVRS_NCL(vo);
					String ORDCODE = ""; //vo.getChkingOrdCode();
					if(ordcode != null) ORDCODE= ordcode.getOrdCode();
					if (StringUtils.isEmpty(ORDCODE)) {
						
						{
							boolean approval_conversion = true;
							for(Entry<String, String> key : vo.getAbusingMap().entrySet() ) {
								if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.INDIRECT.getValue())) {
									approval_conversion= false;
									
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue())) {
									approval_conversion= false;
									
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.SEC_5.getValue())) {
									approval_conversion= false;
									
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.SEC_10.getValue())) {
									approval_conversion= false;
									
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue())) {
									approval_conversion= false;
									
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.ETC.getValue())) {
									approval_conversion= false;
								}
							}
							
							if (approval_conversion) {
								logger.debug("Conv Data insert before 1 - {}", vo );

								if("90".equals(vo.getTrkTpCode())) { // postback 데이터들 제외.
									// CONVERSION_LOG, CONVACT_LOG, coupon_serial, coupon_manager
									sqlSessionTemplateConvDream.update(String.format("%s.%s", NAMESPACE, "insertCONVERSION_LOG_billing"), vo);		//CONVERSION_LOG
									sqlSessionTemplateConvDream.update(String.format("%s.%s", NAMESPACE, "insertConvDataConvAct_billing"), vo);		//CONVACT_LOG_TEST

									// 전체 컨버전 
									logger.debug("Conv Data insert before 2 - {}", vo );
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvData"), vo);			//MOB_CNVRS_NCL

									logger.debug("Conv update cnvrs_new - {}, {}, {}", vo.getYyyymmdd(), vo.getAdvertiserId(), vo.getKeyIp() );
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "updateMOB_CNVRS_HH_NCL_NEW"), vo);		//MOB_CNVRS_HH_NCL_NEW
									
									// 세션 60분 abtest
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvDataABTEST"), vo);		// MOB_CNVRS_NCL_ABTEST

								}

								//MOB_CNVRS_RENEW_NCL
								sqlSessionTemplateConvBilling.insert(String.format("%s.%s",NAMESPACE,"insertMobCnvrsRenewNcl"), vo);		//MOB_CNVRS_RENEW_NCL

								// 미노출 전환데이터
								if(vo.isNoExposureYN()){
									logger.debug("insert unexposure data.");
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvUnexposureData"), vo);			//MOB_CNVRS_NCL
								}

								// ADBLOCK 전환데이터
								if("N".equals(vo.getMainDomainYN())){
									logger.debug("insert ADBLOCK CONV data.");
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvADBlockData"), vo);			//MOB_CNVRS_NCL
								}

								// ADBLOCK 전환데이터
								if(vo.isCrossAuIdYN()){
									logger.debug("insert ConvCrossAuId CONV data.");
									sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvCrossAuIdData"), vo);			//MOB_CNVRS_NCL
								}
								
								// 특정 광고주만 AI 생성 데이터 적재
	                            if (adverids != null) {
		                            List<String> aiAdverIds = Arrays.asList(adverids.split(","));
		                            
		                            if (aiAdverIds.contains(vo.getAdvertiserId())) {
		                            	// Ai 캠페인 전환데이터
										if (!vo.getAiType().isEmpty()) {
											logger.debug("insert Ai CONV data.");
											sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvAiData"), vo);				// MOB_CNVRS_RENEW_AI_HH_STATS
										}
		                            }
	                            }

								sqlSessionTemplateConvDream.flushStatements();
								sqlSessionTemplateConvBilling.flushStatements();
								
								logger.info("TR Time (TBRT) Conversion : " + (System.currentTimeMillis() - startTime) + "(ms) "+ vo.getKeyIp());
								logger.info("Conv conversion succ vo - {}", vo);
								
								res = true;

								// ---------------------------------------------------------------
								// 종합통계 직열화
								// ---------------------------------------------------------------
								sumObjectManager.appendIntgCntrConvData(vo);

								// ---------------------------------------------------------------
								// pcode통계 컨버전용
								// ---------------------------------------------------------------
								if("01".equals(vo.getProductCode())
										&& vo.isTargetYn()) {
									sumObjectManager.appendConvPcodeData(vo);
								}
								
								//----------------------------------------------------------------
								// 기여 전환 데이터 쌓는 로직 									
								// 강제 전환인 shoppual 과 social 은 제외 
								//----------------------------------------------------------------
								if ( !"social".equals(vo.getScriptUserId()) 
										&& !"shoppul".equals(vo.getScriptUserId())) {
									ConvData contriButeConvData = SerializationUtils.clone(vo);
									sumObjectManager.appendContributeConversionData(contriButeConvData);
								}
								//-----------------------------------------------------------------
								//소재 카피 전환 다시 카프카로 던지기 
								// 미노출 제외 , 해당 데이터가 있는경우에만 send 
								//-----------------------------------------------------------------
									ConvData convData = SerializationUtils.clone(vo);									
									JSONObject message = this.buildConversionJSON(convData);
									message.put("orderCnt", "1");									
									//logger.info("Succ Conversion Data - {}", message.toString());
									sender.send(G.SuccConversion, message.toString());
								
							}


						}
						
						{
							// ---------------------------------------------------------------
							// 어뷰징데이터 쌓기
							// ---------------------------------------------------------------
							for(Entry<String, String> key : vo.getAbusingMap().entrySet() ) {
								logger.info("ConvAbusing : keyIp:{}, ordNo:{}, abusingCode:{}", vo.getKeyIp(), vo.getOrdCnt(), key);
								
								ConvData abusingData= SerializationUtils.clone(vo);
								if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.INDIRECT.getValue())) {
//					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.INDIRECT.getValue());
//					                logger.info("ConvAbusing INDIRECT, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue())) {
					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue());
					                logger.info("ConvAbusing ONE_HUNDRED_MILLION, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.SEC_5.getValue())) {
					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SEC_5.getValue());
					                logger.info("ConvAbusing SEC_5, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.SEC_10.getValue())) {
					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SEC_10.getValue());
					                logger.info("ConvAbusing SEC_10, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue())) {
					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue());
					                logger.info("ConvAbusing UNDER_1MIN, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								} else if(key.getKey().equals(CNVRS_ABUSING_TP_CODE.ETC.getValue())) {
					                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.ETC.getValue());
					                logger.info("ConvAbusing ETC, keyIp:{}, ordCode:{}", vo.getKeyIp(), vo.getOrdCode());
					                
								}
							}

							// ---------------------------------------------------------------
							// 로그쌓기
							// ---------------------------------------------------------------
							String fileName = String.format("%s%s.%s", logPathConvsucc, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
							if( !"".equals(fileName) ) {

								ConvData voClone= SerializationUtils.clone(vo);
								
								if( !StringUtils.isNumeric(voClone.getPlatform()) ) voClone.setPlatform( G.convertPLATFORM_CODE(voClone.getPlatform()));
								if( !StringUtils.isNumeric(voClone.getProduct()) ) voClone.setProduct(G.convertPRDT_CODE(voClone.getProduct()));
								if( !StringUtils.isNumeric(voClone.getAdGubun()) ) voClone.setAdGubun( G.convertTP_CODE(voClone.getAdGubun()));
								
								JSONObject jSONObject = JSONObject.fromObject(voClone);
								org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
										, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), G.ConversionSuccData, jSONObject), "UTF-8", true);
							}
							
						}
					}
					else {
						if("90".equals(vo.getTrkTpCode())) {
							logger.info("Conv conversion overlab orderno vo - {}", vo);
						} else { // 트래커 전환의 경우 주문번호가 존재할 시 전환금액 및 수량을 append함.
							logger.info("TRK CONV DUP ORDER DATA UPDATE - {}", vo);
							sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "updateTRKDupOrdData"), vo);
							sqlSessionTemplateConvBilling.flushStatements();
							
							//트래커 주문번호가 존재하는 경우 orderCnt를 제외하고 전환금액 및 수량을 append함.
							ConvData convData = SerializationUtils.clone(vo);
							JSONObject message = this.buildConversionJSON(convData);
							message.put("orderCnt", "0");
							sender.send(G.SuccConversion, message.toString());
							
							res = true;

						}
					}
					
				} catch (Exception e) {
					logger.error("err331 transectionRuning ", e);
					logger.info("err331 vo-{}", vo);
					status.setRollbackOnly();
					res = false;
					
				} finally {
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}

			private JSONObject buildConversionJSON(ConvData convData) {
				JSONObject message = new JSONObject();
				message.put("key", convData.getKey());
				message.put("className", convData.getClassName());
				message.put("abtest", convData.getAbTests());
				message.put("sendDate", convData.getSendDate());
				message.put("scriptUserId", convData.getScriptUserId());
				message.put("site_code", convData.getSiteCode());
				message.put("media_code", convData.getScriptNo());
				message.put("type", "CONV");
				message.put("ordCode", convData.getOrdCode());
				message.put("direct", convData.getDirect());
				message.put("userId", convData.getAdvertiserId());
				message.put("frameCombiKey", convData.getFrameCombiKey());
				message.put("frameId", convData.getFrameId());
				message.put("frameCycleNum", convData.getFrameCycleNum());
				message.put("frameSelector", convData.getFrameSelector());
				message.put("prdtTpCode", convData.getPrdtTpCode());
				message.put("price", convData.getPrice());
				String advrtsTpCode = convData.getAdvrtsTpCode();
				if (advrtsTpCode == null || "".equals(advrtsTpCode)) {
					advrtsTpCode = convData.getAdGubunCode();
				}
				message.put("advrtsTpCode", advrtsTpCode);
				message.put("adverProdData", convData.getAdverProdData());
				message.put("advrtsStleTpCode", convData.getAdvrtsStleTpCode());
				message.put("frameSize",convData.getFrameSize());
				message.put("keyIp", convData.getKeyIp());
				message.put("cookieDirect", convData.getCookieDirect());
				message.put("noExposureYN", convData.isNoExposureYN());
				message.put("cookieInDirect", convData.getCookieInDirect());
				message.put("frameSendTpCode", convData.getFrameSendTpCode());
				message.put("ctgrSeq", convData.getCtgrSeq());									
				message.put("frameKaistRstCode", convData.getFrameKaistRstCode());
				message.put("frameRtbTypeCode", convData.getFrameRtbTypeCode());
				message.put("browserDirect", convData.getBrowserDirect());
				message.put("inHour", convData.getInHour());
				message.put("yyyymmdd", convData.getYyyymmdd());
				message.put("platformTpCode", convData.getPlatformCode());
				message.put("advrtsPrdtCode", convData.getProductCode());
				message.put("advrtsTpCode", convData.getAdvrtsTpCode());
				message.put("itlTpCode", convData.getInterlock());
				message.put("siteCode", convData.getSiteCode());
				message.put("adverId", convData.getAdvertiserId());
				message.put("mobAdGrpData", convData.getMobAdGrpData());
				message.put("mediaScriptNo", convData.getScriptNo());
				message.put("directYn", convData.getInHour().equals("0") ? "N" : "Y");
				message.put("sesionSelngYn",  convData.getDirect() == 0 ? "N" : "Y");
				message.put("sesionSelng2Yn",convData.getBrowserDirect());
				message.put("mobOrderYn", convData.getMobonYn());
				message.put("orderAmt", convData.getPrice());
				message.put("orderQy", convData.getOrdQty());
				return message;
			}
		});
		return result;
	}


	public ConvData convLogActData_C(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("Conv convLogActData_C : {}", data);
			result= sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "convLogActData_C"), data);
//			if( tmp.size()>0 ) {
//				result = tmp.get(0);  
//			}
		}catch(Exception e) {
			logger.error("err data - {}, msg - {}", data, e);
		}
		return result;
	}

	public ConvData convLogUnexposureActData_C(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("Conv convLogActData_C : {}", data);
			result= sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "convLogUnexposureActData_C"), data);
//			if( tmp.size()>0 ) {
//				result = tmp.get(0);
//			}
		}catch(Exception e) {
			logger.error("err data - {}, msg - {}", data, e);
		}
		return result;
	}

	public ConvData convLogActData_CPcode(HashMap<String, Object> data) throws SQLException {
		ConvData result = null;
		try {
			logger.debug("Conv convLogActData_CPcode : {}", data);
			result= sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "convLogActData_CPcode"), data);
//			logger.info("tmp:{}", tmp);
//			
//			if( tmp.size()>0 ) {
//				result = tmp.get(0);  
//			}
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
	
	public ConvData selectCONVERSION_LOG_IPCNT(ConvData data) {
		ConvData result = null;
		try {
			result = sqlSessionTemplateConvDream.selectOne(String.format("%s.%s", NAMESPACE, "selectCONVERSION_LOG_IPCNT"), data);
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

	public ConvData convTrkLogActData(HashMap<String, Object> data) {
		ConvData result = null;
		try {
			result = sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "selectTrkConvActionLog"), data);
		} catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}

	public ConvData convTrkLogActData_Pcode(HashMap<String, Object> data) {
		ConvData result = null;
		try {
			result = sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "selectTrkConvActionLog_Pcode"), data);
		} catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}

	public ConvData convTrkLogActData_Retry(HashMap<String, Object> data) {
		ConvData result = null;
		try {
			result = sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "selectTrkConvActionLog_Retry"), data);
		} catch(Exception e) {
			logger.error("err data - {}, msg -", data, e);
		}
		return result;
	}

	public ConvData convLogActData_REBUILD(HashMap<String, Object> data) {
		ConvData result = null;
		try {
			result = sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "convLogActData_REBUILD"),data);			
		} catch (Exception e) {
			logger.error("err data - {} , msg - ", data, e);
		}
		return result;
	}
	public ConvData ConvLogActData_Test2_C(HashMap<String, Object> data) {
		ConvData result = null;
		try {
			List<String> listIp= (List<String>) data.get("keyIp");
			for(String ip: listIp)
			{
				String []ips= ip.split("\\.");
				String []t= ArrayHelper.pop(ips);
				data.put("keyIpLikeValue", StringUtils.join(t,"."));
				
				result = sqlSessionTemplateDreamSimpleActionSelect.selectOne(String.format("%s.%s", NAMESPACE, "convLogActData_Test2_C"),data);
				if(result!=null) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("err data - {} , msg - ", data, e);
		}
		return result;
	}
	
}
