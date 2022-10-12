package com.mobon.billing.hhtodd.service.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.hhtodd.schedule.TaskHHtoDD;
import com.mobon.billing.hhtodd.service.SumObjectManager;
import com.mobon.billing.util.ConsumerFileUtils; 

@Repository
public class HHtoDDDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoDDDao.class);

	public static final String	NAMESPACE	= "hHtoDDMapper";
	
	public static double fixCount = 100000;
	
	@Value("${log.path}")
	private String logPath;
	

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	@Resource (name = "sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDream;

	@Autowired
	private TransactionTemplate transactionTemplate;


	@Autowired
	private SumObjectManager		sumObjectMapper;
	
	public boolean transectionRuningV2(String [] _group, HashMap<String, String> flushMap) {
		boolean result = false;
		
		Map param = new HashMap();
		param.put("adGubun", G.convertTP_CODE(_group[0]));
		param.put("platform", G.convertPLATFORM_CODE(_group[1]));
		param.put("yyyymmdd", _group[2]);
		param.put("hh", _group[3]);

		logger.debug("param - {}", param);
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				try {
					for (Entry<String, String> item : flushMap.entrySet()) {
						logger.debug("item.getKey() - {}, param - {}", item.getKey(), param);
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, item.getKey()), param);
					}
					
					sqlSessionTemplateBilling.flushStatements();
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning group - {}", Arrays.toString(_group).toString(), e);
					status.setRollbackOnly();
					res = false;

				} finally {
					long endTime = System.currentTimeMillis();
					long resutTime = endTime - startTime;
					logger.info("Transaction BATCH Running Time (TBRT)  : " + resutTime + "(ms)");
				}
				return res;
			}
		});
		return result;
	}
	
	public ArrayList selectGroupKey() {
		return (ArrayList) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectGroupKey"));
	}
	
	public Map selectHistory(String []_group) {
//		{"AD","W","ymd","hh"}
		Map param = new HashMap();
		//param.put("adGubun", _group[0]);
		//param.put("platform", _group[1]);
		param.put("adGubun", G.convertTP_CODE(_group[0]));
		param.put("platform", G.convertPLATFORM_CODE(_group[1]));
		param.put("yyyymmdd", _group[2]);
		param.put("hh", _group[3]);
		logger.info("param - {}", param);
		
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectHistory"), param);
	}
	public void insertHistory(String []_group) {
		Map param = new HashMap();
		//param.put("adGubun", _group[0]);
		//param.put("platform", _group[1]);
		param.put("adGubun", G.convertTP_CODE(_group[0]));
		param.put("platform", G.convertPLATFORM_CODE(_group[1]));
		param.put("yyyymmdd", _group[2]);
		param.put("hh", _group[3]);
		logger.info("param - {}", param);
		
		sqlSessionTemplateBilling.insert(String.format("%s.%s", NAMESPACE, "insertHistory"), param);
	}
	
	/**
	 * 시간별 배치 대상 내역을 조회합니다. 
	 * 1) 히스토리 테이블 마지막 내역 조회및 현재 시간과 차이 구함
	 * 2) 현재 차이가 5분 이상일 경우  현재 시간과 마지막 조회 시간사이에 다른 날자 시간이 잇는지 확인
	 * 3) 다른 날자기 없을경우 현재 시간에서 -2분을 함 
	 * 4) 조회된 해당 날자에 일자별 시간별 광고구분 코드를 조회 하여 리턴 함 
	 */
	public List selectDateList(Map<String, Object> param) {
		
		List resultList = new ArrayList();
		/*
		 * 시간배치 : H
		 * 보정배치 : R
		 */
		String exeType = param.get("exeType")==null?"H":(String)param.get("exeType");

		if("H".equals(exeType)) {
			/*
			 * 시간 배치 
			 */
			

			Map map1 = new HashMap();
			
			//최종 실행 시간을 가져옴
			List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
			
			logger.info("HH==>최종 실행 시간 조회  =>{}",list_t);
			
			map1.putAll((HashMap) list_t.get(0));
			
			//최종 실행 시간 
			String STATS_ALT_DT = map1.get("LAST_EXE_TIME")==null?"0":(String)map1.get("LAST_EXE_TIME");
			//최종 실행 시간과 현재 시간 차이 
			long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
			
			if(TIME_DIFF<=100) {
				return resultList;
			}
			
			/*
			 * 5분 이상 차이나면 현시간에서 1분 이전 시간으로 세팅 
			 */
			if(TIME_DIFF>500) {

				List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOB_CAMP_MEDIA_HH_DDTM_HH"), map1);					
				
				String NEW_LAST_EXE_TIME = "";
				if(list3.size() == 1){ 
					
					NEW_LAST_EXE_TIME = list3.get(0).get("NEW_LAST_EXE_TIME")==null?"0":(String)list3.get(0).get("NEW_LAST_EXE_TIME");
					
					STATS_ALT_DT = NEW_LAST_EXE_TIME;
					
				}
				
			} 

			param.put("STATS_ALT_DT", STATS_ALT_DT);	
			param.put("list", list_t);
			 
			
			//2)최종 수정시간 정보를 가지고 해당 데이타가 속하는 날짜와 시간을 가져옴 (STATS_DTTM, STATS_HH)
			List<Map> list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOD_DATE"), param);					
			
			if(list2.size()>0) {
				resultList.addAll(list2);
			}
			
			
			
		}else {
			/*
			 * 보정배치
			 */
			
			List<Map> list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_DATA_DIFF_LIST"), param);					
			
			if(list2.size()>0) {
				resultList.addAll(list2);
			}
			
		}
	
		return resultList;
		
	}
	

	/**
	 * 시간임시에서 시간테이블로 데이타 처리 합니다 
	  * 처리 순서 
	  * 1) 히스토리 테이블(MOB_BATCH_EXE_HIS)에서 마지막 처리 시간에 1분을 더한값을 가져옴
	  * 2)  1번에서 조회한 데이타를 사용하여 변경된 날자에 광고 구분별 카운트를 조회 
	  * 3) 2번에서 조회한 데이타를 사용하여 광고구분별로 데이타를 만들어 테이블에 저장.
	  *     MOB_ADVER_MEDIA_HH_STATS 는 임시 테이블에 저장 
	  * 4) MOB_ADVER_MEDIA_HH_STATS는 10만건 단위로 테이블에 저장
	  * 5)히스토리 테이블에 type를 'H'로 이력을 남기고,생성된 임시테이블 삭제  
	  * 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningHHtoHH(Map<String, Object> param) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				boolean hisYN = true;
				long startTimeR = System.currentTimeMillis(); 
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				String queryId = param.containsKey("queryId")?(String)param.get("queryId") : "";
				
				try {
					
					/*
					 * 시간배치 : H
					 * 보정배치 : R
					 */
					String exeType = param.get("exeType")==null?"H":(String)param.get("exeType");
					List<Map> list2 =  null;
					
					if("H".equals(exeType)) {
						
						Map map1 = new HashMap();
						
						//최종 실행 시간을 가져옴
						List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
						logger.info("HH==>최종 실행 시간 조회  =>{}",list_t);
						
						map1.putAll((HashMap) list_t.get(0));
						
						//최종 실행 시간 
						String LAST_EXE_TIME = map1.get("LAST_EXE_TIME")==null?"0":(String)map1.get("LAST_EXE_TIME");
						param.put("LAST_EXE_TIME", LAST_EXE_TIME);
						
						String STATS_ALT_DT = "";
						//최종 실행 시간과 현재 시간 차이 
						long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
						
						if(TIME_DIFF<=100) {
							hisYN = false;
							return true;
						}
						
						/*
						 * 5분 이상 차이나면 현시간에서 1분 이전 시간으로 세팅 
						 */
						if(TIME_DIFF>500) {
							List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOB_CAMP_MEDIA_HH_DDTM_HH"), map1);					
							String NEW_LAST_EXE_TIME = "";
//							if(list3!=null && list3.size() == 1){ 
								//NEW_LAST_EXE_TIME = list3.get(0).get("NEW_LAST_EXE_TIME")==null?"0":(String)list3.get(0).get("NEW_LAST_EXE_TIME");
								STATS_ALT_DT = (String) list3.get(0).get("NEW_LAST_EXE_TIME");
//						}
							
						} else {
							STATS_ALT_DT = LAST_EXE_TIME;
						}
	 
						param.put("STATS_ALT_DT", STATS_ALT_DT);	
						param.put("list", list_t);
						 
						logger.info("HH== param-{}", param);
						
						//2)최종 수정시간 정보를 가지고 해당 데이타가 속하는 날짜와 시간을 가져옴 (STATS_DTTM, STATS_HH)
						list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOD_DATE"), param);
						
						if((list2!=null)&&(list2.size()>0)) {
							String MAX_LAST_EXE_TIME = "";
							MAX_LAST_EXE_TIME = list2.get(0).get("MAX_LAST_EXE_TIME")==null?"0":(String)list2.get(0).get("MAX_LAST_EXE_TIME");
							param.put("STATS_ALT_DT", MAX_LAST_EXE_TIME);
						}
						
					}else {
						
						//보정배치 
						list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_DATA_DIFF_LIST"), param);
						hisYN = false;
						
						String MAX_LAST_EXE_TIME = "";
						if((list2!=null)&&(list2.size()>0)) {
							MAX_LAST_EXE_TIME = list2.get(0).get("MAX_LAST_EXE_TIME")==null?"0":(String)list2.get(0).get("MAX_LAST_EXE_TIME");
							param.put("STATS_ALT_DT", MAX_LAST_EXE_TIME);
							
						}
						
						logger.debug("list2==>{}",list2);
						
						
						
					}
					
					  

					if(list2.size()<=0){
						return true;						
					}
					
					for(Map m : list2) {
						
						logger.debug("HH==> for(Map m : list2) m-{}", m);
						startTime = System.currentTimeMillis();						
						List<Map> countList = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOD_DATE_COUNT"), m);						
						resutTime = System.currentTimeMillis() - startTime;
						logger.debug("HH==> {} (TBRT)  :{} " ,"sql_SELECT_MOD_DATE_COUNT", resutTime + "(ms)");

						for(Map cMap : countList) {
							m.putAll(cMap);
							logger.info("HH==> for(Map cMap : countList) m-{}",m);

							//3) 테이블 생성 
							startTime = System.currentTimeMillis();		
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"hhTohhInsert"), m);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.debug("HH==> 임시 테이블 생성 {} (TBRT)  :{} " ,queryId, resutTime + "(ms)");
							//*******************************************************//
						  
							{
								//MOB_COM_HH_STATS_INFO
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_COM_HH_STATS_INFO"), m); 
								sqlSessionTemplateBilling.flushStatements();					
								resutTime = System.currentTimeMillis() - startTime;
								logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_COM_HH_STATS_INFO", resutTime + "(ms)");
						
								try {
									//MOB_ADVER_HH_STATS
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_ADVER_HH_STATS"), m); 
									sqlSessionTemplateBilling.flushStatements();					
									resutTime = System.currentTimeMillis() - startTime;
									logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_ADVER_HH_STATS", resutTime + "(ms)");
								}catch(Exception e) {
									logger.error("err ", e);
								}
								try {
									// MOB_CTGR_HH_STATS							
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_CTGR_HH_STATS"), m); 
									sqlSessionTemplateBilling.flushStatements();					
									resutTime = System.currentTimeMillis() - startTime;
									logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_CTGR_HH_STATS", resutTime + "(ms)");
								}catch(Exception e) {
									logger.error("err ", e);
								}
								
								//MOB_CAMP_HH_STATS							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_CAMP_HH_STATS"), m); 
								sqlSessionTemplateBilling.flushStatements();					
								resutTime = System.currentTimeMillis() - startTime;
								logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_CAMP_HH_STATS", resutTime + "(ms)");
								
								//MOB_MEDIA_HH_STATS							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_MEDIA_HH_STATS"), m); 
								sqlSessionTemplateBilling.flushStatements();					
								resutTime = System.currentTimeMillis() - startTime;
								logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_MEDIA_HH_STATS", resutTime + "(ms)");

								//MOB_MEDIA_SCRIPT_HH_STATS
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_MEDIA_SCRIPT_HH_STATS"), m); 
								sqlSessionTemplateBilling.flushStatements();					
								resutTime = System.currentTimeMillis() - startTime;
								logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_MEDIA_SCRIPT_HH_STATS", resutTime + "(ms)");
		 						
								///////문제 구간 
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_createTable_MOB_ADVER_MEDIA_HH_STATS_Temp"), m);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_createTable_MOB_ADVER_MEDIA_HH_STATS_Temp", resutTime + "(ms)");
		 
								/*						
								//MOB_ADVER_MEDIA_HH_STATS														
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_ADVER_MEDIA_HH_STATS"), m); 
								sqlSessionTemplateBilling.flushStatements();					
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_ADVER_MEDIA_HH_STATS", resutTime + "(ms)");							 
								 */

								//총 수 
								long ROW_COUNT = m.get("ROW_COUNT")==null?0:(long)m.get("ROW_COUNT");
								double ROW_COUNT_D = Double.parseDouble(Long.toString(ROW_COUNT));
								double cnt = Math.ceil(ROW_COUNT_D/fixCount);
								int cntT = (int)cnt;
								
								m.put("END_POINT", (int)fixCount);
								
								for(int i=0; i<cntT; i++) {
									
									m.put("START_POINT", (int)((i)*fixCount));
									
									logger.debug("HH==>m============>{}",m);
									
									//MOB_ADVER_MEDIA_HH_STATS														
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_ADVER_MEDIA_HH_STATS_T"), m); 
									sqlSessionTemplateBilling.flushStatements();					
									resutTime = System.currentTimeMillis() - startTime;
									logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_ADVER_MEDIA_HH_STATS_T", resutTime + "(ms)");
									
								}
							}
							
						}//endfor com_code
					}//end for list2 
					 
					
					res = true;
					
				} catch (Exception e) {	
					
					logger.error("HH==>transectionRuningHHtoHHDD Exception ==>{},{}",queryId,e);
					
					status.setRollbackOnly();
					res = false;
					
					hisYN = false;

				} finally {
					
					if(hisYN) {
												
						//임시테이블 삭제 
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_dropTampTable"), param); 
						sqlSessionTemplateBilling.flushStatements();					
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("HH==>{} (TBRT)  :{}, {} ,{}" ,"sql_dropTampTable_T", param, resutTime + "(ms)");
						
					}
					
					 
					long endTimeR = System.currentTimeMillis();
					long resutTimeR = endTimeR - startTimeR;
					logger.info("HH==>시간 테이블처리 완료 {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTimeR / 1000 + "(sec)");
					
				}
				
				return res;
			}
		});
		return result;
	}
	
	
	/**
	 * HH 집계 배치 지연이 있을 경우 COM_HH_STATS_INFO 데이터를 우선 처리합니다. (통계화면용)
	  * 처리 순서 
	  * 1) 히스토리 테이블(MOB_BATCH_EXE_HIS)에서 마지막 처리 시간에 1분을 더한값을 가져옴
	  * 2) 결과 값이 5분이 넘어갈 경우 COM_HH_STATS_INFO 데이터를 MOB_CAMP_MEDIA_HH_STATS 테이블로 부터 가져옴. 
	  * 3) 배치 수행 결과에 대한 업데이트 (마지막처리)를 하지 않음. (마지막 처리시간은 HH 집계 배치가 업데이트 하기 때문.)  
	  * 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningHHtoCOMINFODefHH() {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				long startTimeR = System.currentTimeMillis(); 
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
					
					List<Map> list2 =  null;
						
					Map map1 = new HashMap();
					
					//최종 실행 시간을 가져옴
					List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
					logger.info("HH==>최종 실행 시간 조회  =>{}",list_t);
					
					map1.putAll((HashMap) list_t.get(0));
					
					//최종 실행 시간과 현재 시간 차이 
					long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
					
					/*
					 * 5분 이상 차이나지 않으면 return 
					 */
					if(TIME_DIFF<500) {
						return true;
					}

					//MOB_COM_HH_STATS_INFO
					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_diff_MOB_COM_HH_STATS_INFO_def")); 
					sqlSessionTemplateBilling.flushStatements();					
					resutTime = System.currentTimeMillis() - startTime;
					logger.debug("HH==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_COM_HH_STATS_INFO_def", resutTime + "(ms)");
					 
					res = true;
					
				} catch (Exception e) {	
					
					logger.error("HH==>transectionRuningHHtoCOMINFODefHH Exception ==>{},{}","sql_diff_MOB_COM_HH_STATS_INFO_def",e);
					
					status.setRollbackOnly();
					res = false;
					

				} finally {
					
					long endTimeR = System.currentTimeMillis();
					long resutTimeR = endTimeR - startTimeR;
					logger.info("HH==>시간 테이블처리 완료 {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_COM_HH_STATS_INFO_def", resutTimeR / 1000 + "(sec)");
					
				}
				
				return res;
			}
		});
		return result;
	}
	
	
	
	
	/**
	 * 
	 * 시간에서 일별 데이타 처리 합니다 .
	 * 1) 히스토리 테이블에서 변경이력을 조회
	 * 2) 1번에서 조회된 데이타를 가지고 변경이 이루어진 날자를 조회 
	 * 3) 2번에서 조회된 날자를 기준으로 광고 구분으로 건수를 조회 
	 * 4) 3번에서 조회된 데이타를 기준으로 임시테이블을 생성하여 데이타를 임시테이블에 저장
	 * 5) 4번에서 생성된 데이타를 원본 테이블에 저장
	 * 6) 4번에서 생성된 데이타중 MOB_ADVER_MEDIA_STATS 은 따로 10만건씩 처리함
	 * 7) 히스토리 테이이블에  type 을 'D'로 해서 이력을 남기고, 생성된 임시테이블을 삭제함  
	 * @param param
	 * @return
	 */
	public boolean transectionRuningDDtoDD(Map row) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				boolean hisYN = true;
				
				long startTimeT = System.currentTimeMillis();
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
					TaskHHtoDD.hhtoddRuning= true;
					
					//Thread.sleep(5000);
					
					List<Map> list2= new ArrayList();
					list2.add(row);
					
					logger.info("DD==>temp table list2==>{}",list2);
					if(list2.size()==0) {
						logger.info("DD==>transectionRuningHHtoDD : 최종 수정된 날짜 데이타가 없습니다");
						return true;
					}
					for(Map m2: list2) { //일별
						String statsDttm = String.valueOf(m2.get("STATS_DTTM"));
						int year = Integer.parseInt(statsDttm.substring(0, 4));
						int month = Integer.parseInt(statsDttm.substring(4, 6));
						int day = Integer.parseInt(statsDttm.substring(6,8));
						Calendar cal = Calendar.getInstance();
						cal.set(year, month-1, day);
						String firstDate = String.valueOf(cal.get(Calendar.YEAR))+String.format("%02d", cal.get(Calendar.MONTH)+1)+String.format("%02d", cal.getMinimum(Calendar.DAY_OF_MONTH));
						logger.info("MTH==>param list2==>{}",firstDate);

						// 오늘이 아니면 차이나는 타게팅만
//						List<Map> list_com = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_COM_CODE"));
//						try {
//							if( !String.valueOf(m2.get("STATS_DTTM")).equals(String.valueOf(list_com.get(0).get("STATS_DTTM"))) ) {
//								list_com = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECTYYYYMMDDADVRTSTPCODE"), m2);
//							}
//						} catch(Exception e) {
//						}

//						for(Map map_com:list_com) {}//end for list_com//공통 코드별 : ADVRTS_TP_CODE
//						m2.putAll(map_com);

						List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_dd_select_tp_code_count"), m2);
						for(Map m3 : list3) {//
							logger.info("DD==>param m3==>{}", m3);

							m3.put("FIRST_DTTM", firstDate);

 							//임시 테이블 생성
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_TABLE_CAMP_MEDIA_TEMP"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_CREATE_TABLE_CAMP_MEDIA_TEMP", resutTime + "(ms)");


							//공테이블 생성 
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_DD_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();



							//총 수 
							long TP_CODE_COUNT = m3.get("TP_CODE_COUNT")==null?0:(long)m3.get("TP_CODE_COUNT");

							double ROW_COUNT_D = Double.parseDouble(Long.toString(TP_CODE_COUNT));
							//double fixCount = 50000;

							 double loopD = Math.ceil(ROW_COUNT_D/fixCount);
							 int loopInt = (int)loopD;

							 logger.debug("loopInt==>{}",loopInt,ROW_COUNT_D,fixCount);

							 m3.put("END_POINT", (int)fixCount);

							logger.debug("DD==>param m3 ==>{}",m3);

							 for(int i=0; i<loopInt; i++) {

								 //임시테이블에 데이타 저장
									m3.put("START_POINT", (int)((i)*fixCount));

									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_TABLE_CAMP_MEDIA_TEMP"), m3);
									sqlSessionTemplateBilling.flushStatements();
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_INSERT_TABLE_CAMP_MEDIA_TEMP", resutTime +"(ms)");

							 }


							for(int i=0; i<loopInt; i++) {

								m3.put("START_POINT", (int)((i)*fixCount));

							////////////////////////////////////////////////////////임시 테이블에 데이타 저장 ///////////////////////////////////////////////////////////////////////////

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_MEDIA_SCRIPT_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_MEDIA_SCRIPT_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_MEDIA_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_MEDIA_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_CAMP_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_CAMP_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_ADVER_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_ADVER_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_COM_STATS_INFO_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_COM_STATS_INFO_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_MOB_ADVER_MEDIA_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_INSERT_MOB_ADVER_MEDIA_STATS_TEMP", resutTime + "(ms)");

							}//end for loopCnt


							for (int i = 0; i < loopInt; i++) {

								m3.put("START_POINT", (int) ((i) * fixCount));

								//////////////////////////////////////////////////////// 원본 테이블에 데이타 저장
								//////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////////////////////

								logger.debug("DD2==>param m3 ==>{}", m3);

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_MEDIA_SCRIPT_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_MEDIA_SCRIPT_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_MEDIA_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_MEDIA_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_CAMP_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_CAMP_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_ADVER_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_ADVER_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_COM_STATS_INFO"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_COM_STATS_INFO", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_CTGR_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_CTGR_STATS", resutTime + "(ms)");


							} // end for loopCnt

							loopD = Math.ceil(ROW_COUNT_D/fixCount);
							loopInt = (int)loopD;

							for (int i = 0; i < loopInt; i++) {

								m3.put("START_POINT", (int) ((i) * fixCount));

								try {
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_ADVER_MEDIA_STATS"), m3);
									sqlSessionTemplateBilling.flushStatements();
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("DD==>{} (TBRT)  :{} ,{}"
											,"sql_MOB_ADVER_MEDIA_STATS", resutTime + "(ms)");

								}catch(Exception eee) {
									logger.error("err {}, {}, during-{}", eee, "sql_MOB_ADVER_MEDIA_STATS", m3, System.currentTimeMillis()-startTime);
								}
							}



							for (int i = 0; i < loopInt; i++) {

								m3.put("START_POINT", (int) ((i) * fixCount));

								try {
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_MOB_KPI_MEDIA_STATS_TEMP"), m3);
									sqlSessionTemplateBilling.flushStatements();
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_MOB_KPI_MEDIA_STATS_TEMP", resutTime + "(ms)");

								}catch(Exception eee) {
									logger.error("err {}, {}, during-{}", eee, "sql_MOB_KPI_MEDIA_STATS_TEMP", m3, System.currentTimeMillis()-startTime);
								}
							}
							for (int i = 0; i < loopInt; i++) {

								m3.put("START_POINT", (int) ((i) * fixCount));

								try {
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_KPI_MEDIA_STATS"), m3);
									sqlSessionTemplateBilling.flushStatements();
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("DD==>{} (TBRT)  :{} ,{}","sql_MOB_KPI_MEDIA_STATS", resutTime + "(ms)");

								}catch(Exception eee) {
									logger.error("err {}, {}, during-{}", eee, "sql_MOB_KPI_MEDIA_STATS", m3, System.currentTimeMillis()-startTime);
								}
							}


							for (int i = 0; i < loopInt; i++) {

								m3.put("START_POINT", (int) ((i) * fixCount));

								try {
									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_MOB_KPI_STATS"), m3);
									sqlSessionTemplateBilling.flushStatements();
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("DD==>{} (TBRT)  :{} ,{}"
											,"sql_MOB_KPI_STATS", resutTime + "(ms)", m3);

								}catch(Exception eee) {
									logger.error("err {}, {}, during-{}", eee, "sql_MOB_KPI_STATS", m3, System.currentTimeMillis()-startTime);
								}
							}

						}//end for m3: list3)

					



						logger.debug("DD==> m2 {}",m2);
						// MOB_MEDIA_SCRIPT_CHRG_STATS 테이블 실시간 미처리 데이터 등록 시작
						{
							// MOB_MEDIA_SCRIPT_CHRG_STATS 100%지면과 외부연동은 이동.
							try {
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_MEDIA_SCRIPT_CHRG_RATE100"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("MEDIA_CHRG {} (TBRT) : {}, {}, {}"
									, "UPDATE_MEDIA_SCRIPT_CHRG_RATE100", resutTime + "(ms)", m2);
							} catch(Exception eee) {
								logger.error("err {}, {}, during-{}", eee, "UPDATE_MEDIA_SCRIPT_CHRG_RATE100", m2, System.currentTimeMillis()-startTime);
							}

							// MOB_MEDIA_SCRIPT_CHRG_STATS click_cnt 없는거
							try {
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_MEDIA_SCRIPT_CHRG_NONECLICK"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("MEDIA_CHRG {} (TBRT) : {}, {}, {}"
									, "UPDATE_MEDIA_SCRIPT_CHRG_NONECLICK", resutTime + "(ms)", m2);
							} catch(Exception eee) {
								logger.error("err {}, {}, during-{}", eee, "UPDATE_MEDIA_SCRIPT_CHRG_NONECLICK", m2, System.currentTimeMillis()-startTime);
							}

							// MOB_MEDIA_SCRIPT_CHRG_STATS click_cnt 다른것
							try {
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_MEDIA_SCRIPT_CHRG_DIFFCLICK"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("MEDIA_CHRG {} (TBRT) : {}, {}, {}"
									, "UPDATE_MEDIA_SCRIPT_CHRG_DIFFCLICK", resutTime + "(ms)", m2);
							} catch(Exception eee) {
								logger.error("err {}, {}, during-{}", eee, "UPDATE_MEDIA_SCRIPT_CHRG_DIFFCLICK", m2, System.currentTimeMillis()-startTime);
							}
						}

						// 정산테이블 매체단위 SUMMARY
						try {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_MEDIA_CHRG"), m2);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.debug("MEDIA_CHRG {} (TBRT) : {}, {}"
								, "sql_INSERT_MEDIA_CHRG", resutTime + "(ms)");
						} catch(Exception eee) {
							logger.error("err {}, {}, during-{}", eee, "sql_INSERT_MEDIA_CHRG", m2, System.currentTimeMillis()-startTime);
						}
					}//end for m2: list2)  
					
					res = true;
					
				} catch (Exception e) {	
				 
					hisYN = false;
					
					logger.error("DD==>transectionRuningHHtoHHDD Exception ==>{},{}", e);
					
					status.setRollbackOnly();
					res = false;

				} finally {					
					if(hisYN) {
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_dropDDTempTable")); 
						sqlSessionTemplateBilling.flushStatements();					
						endTime = System.currentTimeMillis();
						resutTime =  endTime- startTimeT;
						logger.info("DD==>일데이타 배치 종료  : {} (TBRT)  :{} ,{}" ,"sql_dropDDTempTable", resutTime + "(ms)");
					}
					// 안맞는 정산 테이블 통계 기준으로 업데이트 
					/*
					try {
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG"));
						sqlSessionTemplateBilling.flushStatements();
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("MEDIA_SCRIPT_CHRG {} (TBRT) : {}, {}"
							, "sql_UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG", resutTime + "(ms)");
						
					} catch (Exception eee) {
						logger.error("err {}, {}, during-{}", eee, "sql_UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG", System.currentTimeMillis()-startTime);
					}
					*/
					TaskHHtoDD.hhtoddRuning= false;
				}
				
				return res;
			}
		});
		return result;
	}
	 
	
	/**
	 * 컨버젼 데이타 처리 합니다 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningConv(Map param) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				String queryId = param.containsKey("queryId")?(String)param.get("queryId") : "";
				
				try {
					
					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,queryId), param);
					sqlSessionTemplateBilling.flushStatements();
					
					res = true;
					
				} catch (Exception e) {	
				 
					logger.error("transectionRuningHHtoHHDD Exception ==>{},{}",queryId,e);
					
					status.setRollbackOnly();
					res = false;

				} finally {					
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTime + "(ms)");
				}
				
				return res;
			}
		});
		return result;
	}
	
	/**
	 * 지역데이타 처리
	 *  */
	public boolean transectionRuningNear(Map<String, Object> param) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;

				long startTimeR = System.currentTimeMillis();

				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;

				try {

					List<Map> list_com = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_COM_CODE"), param);

					for (Map m1 : list_com) {

						List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_NEAR_PRDT_CODE_COUNT"), m1);

						for (Map m2 : list3) {

							// 임시 테이블 생성
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_CREATE_TEMP_TABLE_NEAR"), m1);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_CREATE_TEMP_TABLE_NEAR", resutTime + "(ms)");

							// 총 수
							long ROW_COUNT = m2.get("ROW_COUNT") == null ? 0 : (long) m2.get("ROW_COUNT");

							double ROW_COUNT_D = Double.parseDouble(Long.toString(ROW_COUNT));
							// double fixCount = 50000;

							double loopD = Math.ceil(ROW_COUNT_D / fixCount);
							int loopInt = (int) loopD;

							logger.info("NEAR==>loopInt==>{}", loopInt, ROW_COUNT_D, fixCount);

							m2.put("END_POINT", (int) fixCount);

							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));

								logger.debug("NEAR==>m2==>{}", m2);

								// 임시테이블에 데이타 생성시작
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_TRGT_STATS_TEMP"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_TRGT_STATS_TEMP", resutTime + "(ms)");

							} // end 원테이블 임시테이블 생성

							// 각 테이블 임시테이블에 데이타 저장
							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));

								// 임시테이블에 데이타 INSERT 시작
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_TABLE_RGN_ADSTRD_ADVER_STATS_TEMP"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_ADVER_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_TABLE_RGN_ADSTRD_CAMP_STATS_TEMP"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_CAMP_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_TABLE_RGN_ADSTRD_MEDIA_STATS_TEMP"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_MEDIA_STATS_TEMP", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_ADSTRD_STATS_TEMP"),	m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_STATS_TEMP", resutTime + "(ms)");

							} // end for 임시 테이블 데이타 적제

							// 각 테이블 임시테이블에 데이타 저장
							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));

								// 임시테이블에 데이타 INSERT 시작
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_ADSTRD_ADVER_STATS"),m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_ADVER_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_ADSTRD_CAMP_STATS"),m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_CAMP_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_ADSTRD_MEDIA_STATS"),m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_MEDIA_STATS", resutTime + "(ms)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_ADSTRD_STATS"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_ADSTRD_STATS", resutTime + "(ms)");

							} // end for 원테이블 데이타 적제

						} // end m2 list3

					}

				} catch (Exception e) {

					logger.error("NEAR==> Exception =>{}", e);

				} finally {

					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_HIS_DORP_TEMP_TABLE_NEAR"), param);
					sqlSessionTemplateBilling.flushStatements();

					endTime = System.currentTimeMillis();
					resutTime = endTime - startTimeR;
					logger.info("NEAR==> 지역 끝 {} Transaction BATCH Running Time (TBRT)  :{} ,{}",resutTime + "(ms)");

				} // end try finally

				return res;
			}
		});
		return result;
	}// end method near
	
	/**
	 * 플레이 링크 데이타 생성 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningPLINK(Map<String, Object> param) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				
				boolean hisYN = true;
				
				long startTimeR = System.currentTimeMillis(); 
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
						 
						List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_PLINK_AVAL_STATS_INS"));
						
						for(Map m2 : list3) {
							
							
							logger.info("m2=>{},{}",m2,((m2.get("DAY_DIFF") instanceof Long) ));
							logger.info("m2=>{},{}",m2,((m2.get("DAY_DIFF") instanceof Integer) ));
							
							
							//총 수 
							long ROW_COUNT = m2.get("ROW_COUNT")==null?0:(long)m2.get("ROW_COUNT");
							
							double ROW_COUNT_D = Double.parseDouble(Long.toString(ROW_COUNT));

							 
							double loopD = Math.ceil(ROW_COUNT_D/fixCount);
							int loopInt = (int)loopD;
							
							logger.info("PLINK==>loopInt==>{}",loopInt,ROW_COUNT_D,fixCount);
							 
							m2.put("END_POINT", (int)fixCount);
							String queryId ="";
							int  DAY_DIFF = 0;
							long  DAY_DIFF_L = 0;
							String DAY_DIFF_Str = "";
							
							String day_diff = "";
							
							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));
								
								if(((m2.get("DAY_DIFF") instanceof Long) )) {
									
									DAY_DIFF_L = m2.get("DAY_DIFF")==null?0:(long)m2.get("DAY_DIFF");
									
									DAY_DIFF_Str = Long.toString(DAY_DIFF_L);
									
									DAY_DIFF = Integer.parseInt(DAY_DIFF_Str);
									
								}else {
									
									DAY_DIFF = m2.get("DAY_DIFF")==null?0:(int)m2.get("DAY_DIFF");
									
								}
								
								logger.info("PLINK==>m2==>{}",m2);
								
								if(DAY_DIFF<2){
									
									queryId = "sql_INSERT_PLINK_AVAL_STATS";
									
								}else {
									
									queryId = "sql_INSERT_PLINK_AVAL_STATS_2DAY_DIFF";
									
								}
								
								//임시테이블에 데이타 생성시작 
								startTime = System.currentTimeMillis();

									
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,queryId), m2);
								sqlSessionTemplateBilling.flushStatements();					
								
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("PLINK==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTime + "(ms)");
								 
							}//end 원테이블 임시테이블 생성 
							 
							
						}//end m2 list3
	 
					
				}catch(Exception e ) {
					
					logger.error("PLINK==> Exception =>{}",e);
					
				}finally {
					
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("PLINK==> PLINK {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,resutTime + "(ms)");
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_HIS_DORP_TEMP_TABLE_PLINK"), param); 
					sqlSessionTemplateBilling.flushStatements();					
					 
					
		 
					
				}// end try finally
				
				
				
				return res;
			}
		});
		return result;
	}//end method near
	
	/**
	 *  드림컨버젼 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningDreamConv(Map<String, Object> param) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				
				long startTimeR = System.currentTimeMillis(); 
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
						//기존데이타 삭제 
						sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE,"sql_DELETE_STATUS_CONVERSION"));
						sqlSessionTemplateDream.flushStatements();
					
						//총 카운트 
						List<Map> list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_COUNT_DATA"));
						
						
						Map m2  =new HashMap();
						m2.putAll(list2.get(0));
						
						long ROW_COUNT = m2.get("ROW_COUNT")==null?0:(long)m2.get("ROW_COUNT");
						
						double ROW_COUNT_D = Double.parseDouble(Long.toString(ROW_COUNT));
						int fixCountT  = 3000;
						
						double loopD = Math.ceil(ROW_COUNT_D/fixCountT);
						
						int loopInt = (int)loopD;
						
						 
						for (int i = 0; i < loopInt; i++) {

							m2.put("START_POINT", (int) ((i) * fixCountT));
							m2.put("END_POINT",fixCountT);
 
							//임시테이블에 데이타 생성시작 
							startTime = System.currentTimeMillis();
							
							//대상 
							List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_DATA"),m2);

							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("Conv==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_SELECT_DATA", resutTime + "(ms)");
							
							int loopCnt = list3.size();
							
							Map insMap = null;
							String ADGUBUN = "";
							
							for (int j = 0; j < loopCnt; j++) { 
								
								insMap = list3.get(j);
								ADGUBUN = insMap.get("ADGUBUN")==null?"-":(String)insMap.get("ADGUBUN");
								insMap.put("ADGUBUN", ADGUBUN);
								
								try {
									
									sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE,"sql_INSERT_STATUS_CONVERSION"), insMap);
									sqlSessionTemplateDream.flushStatements();
									
								}catch(Exception e) {
									
									logger.error("dreamConv Exception ==>{}",e);
									
									try {
										ConsumerFileUtils.writeLine( logPath +"log4j/", String.format("%s._%s","dreamConv_Insert_Err", DateUtils.getDate("yyyy-MM-dd_HH")), "",insMap);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									continue;
									
								} 
								 
							}//end 원테이블 임시테이블 생성 							
							
						}//end 원테이블 임시테이블 생성 
						 
					
				}catch(Exception e ) {
					
					logger.error("Conv==> Exception =>{}",e);
					
				}finally {
					 
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTimeR;
					logger.info("Conv==> Conv {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,resutTime + "(ms)");
		 
					
				}// end try finally
				
				
				
				return res;
			}
		});
		return result;
	}//end method near
	
	/**
	 * 지역 보정 배치 
	 * @param param
	 * @return
	 */
	public boolean transectionRuningNearRevision(Map<String, Object> param) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				 
				long startTimeR = System.currentTimeMillis(); 
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
					
					//지역보정 데이타 보관 temp 테이블 생성 
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_TABLE_RGN_TRGT_STATS_TEMP"));
					sqlSessionTemplateBilling.flushStatements();

					//지역 보정 데이타 날짜를  조회 해야함
					//임시테이블에 데이타 생성시작
					
					startTime = System.currentTimeMillis();					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_UPDATE_RGN_TRGT_STATS_REVISION"));
					sqlSessionTemplateBilling.flushStatements();
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("NEARRevision==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_UPDATE_RGN_TRGT_STATS_REVISION", resutTime + "(ms)");	
					
					//임시테이블에 데이타 생성시작 
					startTime = System.currentTimeMillis();
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_UPDATE_NEAR_OTHER_REVISION"));
					sqlSessionTemplateBilling.flushStatements();
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("NEARRevision==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_UPDATE_NEAR_OTHER_REVISION", resutTime + "(ms)");
 
										
				}catch(Exception e ) {
					
					logger.error("NEARRevision==> Exception =>{}",e);
					
				}finally {
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTimeR;
					logger.info("NEARRevision==> NEARRevision {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,resutTime + "(ms)");
					
				}// end try finally
				
				
				
				return res;
			}
		});
		return result;
	}//end method near
	
	/*
	 * 매체디비 스냅샷
	 * 
	 * */
	public List<Map<String, String>> snapShotMediaScript() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", "hHtoDDMapper", "snapShotMediaScript"));
	}

	/*
	 * 매체과금디비 스냅샷
	 *
	 * */
	public List<Map<String, String>> snapShotMediaScriptChrg() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", "hHtoDDMapper", "snapShotMediaScriptChrg"));
	}
	
	
	/*
	 * 주차별 MEDIA 별 통계 insert 
	 * */
	public void insertMediaWeekStats(Map param) {
		
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		long resutTime = endTime - startTime;
		
		try {
			logger.info("CTRWEEKSTATS START");
			sqlSessionTemplateBilling.insert(String.format("%s.%s",NAMESPACE, "INSERT_TABLE_MOB_MEDIA_PAR_WK_STATS"));			
			logger.info("INSERT CTRWEEKSTATS {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		} catch (Exception e) {
			logger.error("InsertMediaWeekStats err => {}", e.toString());
		}		
	}
	
	/*12시 30분 이후에 들어오는 데이터 Renew ncl 테이블에 Insert*/
	public void insertBeforeRenewData(Map param) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		long resutTime = endTime - startTime;
		
		try {
			logger.info("BEFORERENEWDATA START");
			sqlSessionTemplateBilling.insert(String.format("%s.%s", NAMESPACE, "INSERT_BEFORE_M0B_RENEW_NCL"), param);
			logger.info("INSERT BEFORE_RENEW_DATA {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		} catch (Exception e) {
			logger.error("INSERT BEFORE_RENEW_DATA err => {}", e.toString());
		}
	}

	public void updateDiffMobMediaScriptChrgStats(Map param) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		long resutTime = endTime - startTime;
		try {
		sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG_STATS"));
		sqlSessionTemplateBilling.flushStatements();

		logger.info("UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG_STATS {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		} catch (Exception e) {
			logger.error("UPDATE_DIFF_MOB_MEDIA_SCRIPT_CHRG_STATS err => {}", e.toString());
		}
		startTime = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
		resutTime = endTime - startTime;		
		try {
			//3. 해당 차이나는 데이터 리스트를 매치아이디 기준으로 MOB_MEDIA_CHRG_STATS UPDATE 하기 
			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"UPDATE_DIFF_MOB_MEDIA_CHRG_STATS"));
			sqlSessionTemplateBilling.flushStatements();
			logger.info("UPDATE_DIFF_MOB_MEDIA_CHRG_STATS {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		}catch (Exception e) {
			logger.error("UPDATE_DIFF_MOB_MEDIA_CHRG_STATS err => {}", e.toString());
		}
		
		
		
	}
	
}//end class
