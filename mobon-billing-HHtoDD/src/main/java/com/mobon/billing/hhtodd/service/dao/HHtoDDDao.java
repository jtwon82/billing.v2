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
	 * ????????? ?????? ?????? ????????? ???????????????. 
	 * 1) ???????????? ????????? ????????? ?????? ????????? ?????? ????????? ?????? ??????
	 * 2) ?????? ????????? 5??? ????????? ??????  ?????? ????????? ????????? ?????? ??????????????? ?????? ?????? ????????? ????????? ??????
	 * 3) ?????? ????????? ???????????? ?????? ???????????? -2?????? ??? 
	 * 4) ????????? ?????? ????????? ????????? ????????? ???????????? ????????? ?????? ?????? ?????? ??? 
	 */
	public List selectDateList(Map<String, Object> param) {
		
		List resultList = new ArrayList();
		/*
		 * ???????????? : H
		 * ???????????? : R
		 */
		String exeType = param.get("exeType")==null?"H":(String)param.get("exeType");

		if("H".equals(exeType)) {
			/*
			 * ?????? ?????? 
			 */
			

			Map map1 = new HashMap();
			
			//?????? ?????? ????????? ?????????
			List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
			
			logger.info("HH==>?????? ?????? ?????? ??????  =>{}",list_t);
			
			map1.putAll((HashMap) list_t.get(0));
			
			//?????? ?????? ?????? 
			String STATS_ALT_DT = map1.get("LAST_EXE_TIME")==null?"0":(String)map1.get("LAST_EXE_TIME");
			//?????? ?????? ????????? ?????? ?????? ?????? 
			long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
			
			if(TIME_DIFF<=100) {
				return resultList;
			}
			
			/*
			 * 5??? ?????? ???????????? ??????????????? 1??? ?????? ???????????? ?????? 
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
			 
			
			//2)?????? ???????????? ????????? ????????? ?????? ???????????? ????????? ????????? ????????? ????????? (STATS_DTTM, STATS_HH)
			List<Map> list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOD_DATE"), param);					
			
			if(list2.size()>0) {
				resultList.addAll(list2);
			}
			
			
			
		}else {
			/*
			 * ????????????
			 */
			
			List<Map> list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_DATA_DIFF_LIST"), param);					
			
			if(list2.size()>0) {
				resultList.addAll(list2);
			}
			
		}
	
		return resultList;
		
	}
	

	/**
	 * ?????????????????? ?????????????????? ????????? ?????? ????????? 
	  * ?????? ?????? 
	  * 1) ???????????? ?????????(MOB_BATCH_EXE_HIS)?????? ????????? ?????? ????????? 1?????? ???????????? ?????????
	  * 2)  1????????? ????????? ???????????? ???????????? ????????? ????????? ?????? ????????? ???????????? ?????? 
	  * 3) 2????????? ????????? ???????????? ???????????? ?????????????????? ???????????? ????????? ???????????? ??????.
	  *     MOB_ADVER_MEDIA_HH_STATS ??? ?????? ???????????? ?????? 
	  * 4) MOB_ADVER_MEDIA_HH_STATS??? 10?????? ????????? ???????????? ??????
	  * 5)???????????? ???????????? type??? 'H'??? ????????? ?????????,????????? ??????????????? ??????  
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
					 * ???????????? : H
					 * ???????????? : R
					 */
					String exeType = param.get("exeType")==null?"H":(String)param.get("exeType");
					List<Map> list2 =  null;
					
					if("H".equals(exeType)) {
						
						Map map1 = new HashMap();
						
						//?????? ?????? ????????? ?????????
						List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
						logger.info("HH==>?????? ?????? ?????? ??????  =>{}",list_t);
						
						map1.putAll((HashMap) list_t.get(0));
						
						//?????? ?????? ?????? 
						String LAST_EXE_TIME = map1.get("LAST_EXE_TIME")==null?"0":(String)map1.get("LAST_EXE_TIME");
						param.put("LAST_EXE_TIME", LAST_EXE_TIME);
						
						String STATS_ALT_DT = "";
						//?????? ?????? ????????? ?????? ?????? ?????? 
						long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
						
						if(TIME_DIFF<=100) {
							hisYN = false;
							return true;
						}
						
						/*
						 * 5??? ?????? ???????????? ??????????????? 1??? ?????? ???????????? ?????? 
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
						
						//2)?????? ???????????? ????????? ????????? ?????? ???????????? ????????? ????????? ????????? ????????? (STATS_DTTM, STATS_HH)
						list2 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_MOD_DATE"), param);
						
						if((list2!=null)&&(list2.size()>0)) {
							String MAX_LAST_EXE_TIME = "";
							MAX_LAST_EXE_TIME = list2.get(0).get("MAX_LAST_EXE_TIME")==null?"0":(String)list2.get(0).get("MAX_LAST_EXE_TIME");
							param.put("STATS_ALT_DT", MAX_LAST_EXE_TIME);
						}
						
					}else {
						
						//???????????? 
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

							//3) ????????? ?????? 
							startTime = System.currentTimeMillis();		
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"hhTohhInsert"), m);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.debug("HH==> ?????? ????????? ?????? {} (TBRT)  :{} " ,queryId, resutTime + "(ms)");
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
		 						
								///////?????? ?????? 
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

								//??? ??? 
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
												
						//??????????????? ?????? 
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_dropTampTable"), param); 
						sqlSessionTemplateBilling.flushStatements();					
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("HH==>{} (TBRT)  :{}, {} ,{}" ,"sql_dropTampTable_T", param, resutTime + "(ms)");
						
					}
					
					 
					long endTimeR = System.currentTimeMillis();
					long resutTimeR = endTimeR - startTimeR;
					logger.info("HH==>?????? ??????????????? ?????? {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTimeR / 1000 + "(sec)");
					
				}
				
				return res;
			}
		});
		return result;
	}
	
	
	/**
	 * HH ?????? ?????? ????????? ?????? ?????? COM_HH_STATS_INFO ???????????? ?????? ???????????????. (???????????????)
	  * ?????? ?????? 
	  * 1) ???????????? ?????????(MOB_BATCH_EXE_HIS)?????? ????????? ?????? ????????? 1?????? ???????????? ?????????
	  * 2) ?????? ?????? 5?????? ????????? ?????? COM_HH_STATS_INFO ???????????? MOB_CAMP_MEDIA_HH_STATS ???????????? ?????? ?????????. 
	  * 3) ?????? ?????? ????????? ?????? ???????????? (???????????????)??? ?????? ??????. (????????? ??????????????? HH ?????? ????????? ???????????? ?????? ??????.)  
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
					
					//?????? ?????? ????????? ?????????
					List list_t = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_exe_his_last_time"));
					logger.info("HH==>?????? ?????? ?????? ??????  =>{}",list_t);
					
					map1.putAll((HashMap) list_t.get(0));
					
					//?????? ?????? ????????? ?????? ?????? ?????? 
					long TIME_DIFF = map1.get("TIME_DIFF")==null?0:(long)map1.get("TIME_DIFF");
					
					/*
					 * 5??? ?????? ???????????? ????????? return 
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
					logger.info("HH==>?????? ??????????????? ?????? {} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_diff_MOB_COM_HH_STATS_INFO_def", resutTimeR / 1000 + "(sec)");
					
				}
				
				return res;
			}
		});
		return result;
	}
	
	
	
	
	/**
	 * 
	 * ???????????? ?????? ????????? ?????? ????????? .
	 * 1) ???????????? ??????????????? ??????????????? ??????
	 * 2) 1????????? ????????? ???????????? ????????? ????????? ???????????? ????????? ?????? 
	 * 3) 2????????? ????????? ????????? ???????????? ?????? ???????????? ????????? ?????? 
	 * 4) 3????????? ????????? ???????????? ???????????? ?????????????????? ???????????? ???????????? ?????????????????? ??????
	 * 5) 4????????? ????????? ???????????? ?????? ???????????? ??????
	 * 6) 4????????? ????????? ???????????? MOB_ADVER_MEDIA_STATS ??? ?????? 10????????? ?????????
	 * 7) ???????????? ???????????????  type ??? 'D'??? ?????? ????????? ?????????, ????????? ?????????????????? ?????????  
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
						logger.info("DD==>transectionRuningHHtoDD : ?????? ????????? ?????? ???????????? ????????????");
						return true;
					}
					for(Map m2: list2) { //??????
						String statsDttm = String.valueOf(m2.get("STATS_DTTM"));
						int year = Integer.parseInt(statsDttm.substring(0, 4));
						int month = Integer.parseInt(statsDttm.substring(4, 6));
						int day = Integer.parseInt(statsDttm.substring(6,8));
						Calendar cal = Calendar.getInstance();
						cal.set(year, month-1, day);
						String firstDate = String.valueOf(cal.get(Calendar.YEAR))+String.format("%02d", cal.get(Calendar.MONTH)+1)+String.format("%02d", cal.getMinimum(Calendar.DAY_OF_MONTH));
						logger.info("MTH==>param list2==>{}",firstDate);

						// ????????? ????????? ???????????? ????????????
//						List<Map> list_com = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_COM_CODE"));
//						try {
//							if( !String.valueOf(m2.get("STATS_DTTM")).equals(String.valueOf(list_com.get(0).get("STATS_DTTM"))) ) {
//								list_com = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECTYYYYMMDDADVRTSTPCODE"), m2);
//							}
//						} catch(Exception e) {
//						}

//						for(Map map_com:list_com) {}//end for list_com//?????? ????????? : ADVRTS_TP_CODE
//						m2.putAll(map_com);

						List<Map> list3 = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_dd_select_tp_code_count"), m2);
						for(Map m3 : list3) {//
							logger.info("DD==>param m3==>{}", m3);

							m3.put("FIRST_DTTM", firstDate);

 							//?????? ????????? ??????
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_TABLE_CAMP_MEDIA_TEMP"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.debug("DD==>{} (TBRT)  :{} ,{}" ,"sql_CREATE_TABLE_CAMP_MEDIA_TEMP", resutTime + "(ms)");


							//???????????? ?????? 
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_DD_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();



							//??? ??? 
							long TP_CODE_COUNT = m3.get("TP_CODE_COUNT")==null?0:(long)m3.get("TP_CODE_COUNT");

							double ROW_COUNT_D = Double.parseDouble(Long.toString(TP_CODE_COUNT));
							//double fixCount = 50000;

							 double loopD = Math.ceil(ROW_COUNT_D/fixCount);
							 int loopInt = (int)loopD;

							 logger.debug("loopInt==>{}",loopInt,ROW_COUNT_D,fixCount);

							 m3.put("END_POINT", (int)fixCount);

							logger.debug("DD==>param m3 ==>{}",m3);

							 for(int i=0; i<loopInt; i++) {

								 //?????????????????? ????????? ??????
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

							////////////////////////////////////////////////////////?????? ???????????? ????????? ?????? ///////////////////////////////////////////////////////////////////////////

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

								//////////////////////////////////////////////////////// ?????? ???????????? ????????? ??????
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
						// MOB_MEDIA_SCRIPT_CHRG_STATS ????????? ????????? ????????? ????????? ?????? ??????
						{
							// MOB_MEDIA_SCRIPT_CHRG_STATS 100%????????? ??????????????? ??????.
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

							// MOB_MEDIA_SCRIPT_CHRG_STATS click_cnt ?????????
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

							// MOB_MEDIA_SCRIPT_CHRG_STATS click_cnt ?????????
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

						// ??????????????? ???????????? SUMMARY
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
						logger.info("DD==>???????????? ?????? ??????  : {} (TBRT)  :{} ,{}" ,"sql_dropDDTempTable", resutTime + "(ms)");
					}
					// ????????? ?????? ????????? ?????? ???????????? ???????????? 
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
	 * ????????? ????????? ?????? ????????? 
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
	 * ??????????????? ??????
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

							// ?????? ????????? ??????
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_CREATE_TEMP_TABLE_NEAR"), m1);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_CREATE_TEMP_TABLE_NEAR", resutTime + "(ms)");

							// ??? ???
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

								// ?????????????????? ????????? ????????????
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_TABLE_RGN_TRGT_STATS_TEMP"), m2);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.debug("NEAR==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}","sql_INSERT_TABLE_RGN_TRGT_STATS_TEMP", resutTime + "(ms)");

							} // end ???????????? ??????????????? ??????

							// ??? ????????? ?????????????????? ????????? ??????
							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));

								// ?????????????????? ????????? INSERT ??????
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

							} // end for ?????? ????????? ????????? ??????

							// ??? ????????? ?????????????????? ????????? ??????
							for (int i = 0; i < loopInt; i++) {

								m2.put("START_POINT", (int) ((i) * fixCount));

								// ?????????????????? ????????? INSERT ??????
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

							} // end for ???????????? ????????? ??????

						} // end m2 list3

					}

				} catch (Exception e) {

					logger.error("NEAR==> Exception =>{}", e);

				} finally {

					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "sql_INSERT_HIS_DORP_TEMP_TABLE_NEAR"), param);
					sqlSessionTemplateBilling.flushStatements();

					endTime = System.currentTimeMillis();
					resutTime = endTime - startTimeR;
					logger.info("NEAR==> ?????? ??? {} Transaction BATCH Running Time (TBRT)  :{} ,{}",resutTime + "(ms)");

				} // end try finally

				return res;
			}
		});
		return result;
	}// end method near
	
	/**
	 * ????????? ?????? ????????? ?????? 
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
							
							
							//??? ??? 
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
								
								//?????????????????? ????????? ???????????? 
								startTime = System.currentTimeMillis();

									
								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,queryId), m2);
								sqlSessionTemplateBilling.flushStatements();					
								
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("PLINK==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTime + "(ms)");
								 
							}//end ???????????? ??????????????? ?????? 
							 
							
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
	 *  ??????????????? 
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
						//??????????????? ?????? 
						sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE,"sql_DELETE_STATUS_CONVERSION"));
						sqlSessionTemplateDream.flushStatements();
					
						//??? ????????? 
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
 
							//?????????????????? ????????? ???????????? 
							startTime = System.currentTimeMillis();
							
							//?????? 
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
								 
							}//end ???????????? ??????????????? ?????? 							
							
						}//end ???????????? ??????????????? ?????? 
						 
					
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
	 * ?????? ?????? ?????? 
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
					
					//???????????? ????????? ?????? temp ????????? ?????? 
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_CREATE_TABLE_RGN_TRGT_STATS_TEMP"));
					sqlSessionTemplateBilling.flushStatements();

					//?????? ?????? ????????? ?????????  ?????? ?????????
					//?????????????????? ????????? ????????????
					
					startTime = System.currentTimeMillis();					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"sql_UPDATE_RGN_TRGT_STATS_REVISION"));
					sqlSessionTemplateBilling.flushStatements();
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("NEARRevision==>{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,"sql_UPDATE_RGN_TRGT_STATS_REVISION", resutTime + "(ms)");	
					
					//?????????????????? ????????? ???????????? 
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
	 * ???????????? ?????????
	 * 
	 * */
	public List<Map<String, String>> snapShotMediaScript() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", "hHtoDDMapper", "snapShotMediaScript"));
	}

	/*
	 * ?????????????????? ?????????
	 *
	 * */
	public List<Map<String, String>> snapShotMediaScriptChrg() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", "hHtoDDMapper", "snapShotMediaScriptChrg"));
	}
	
	
	/*
	 * ????????? MEDIA ??? ?????? insert 
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
	
	/*12??? 30??? ????????? ???????????? ????????? Renew ncl ???????????? Insert*/
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
			//3. ?????? ???????????? ????????? ???????????? ??????????????? ???????????? MOB_MEDIA_CHRG_STATS UPDATE ?????? 
			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"UPDATE_DIFF_MOB_MEDIA_CHRG_STATS"));
			sqlSessionTemplateBilling.flushStatements();
			logger.info("UPDATE_DIFF_MOB_MEDIA_CHRG_STATS {} Transaction BATCH Running Time (TBRT)  :{} ,{}", resutTime + "(ms)");
		}catch (Exception e) {
			logger.error("UPDATE_DIFF_MOB_MEDIA_CHRG_STATS err => {}", e.toString());
		}
		
		
		
	}
	
}//end class
