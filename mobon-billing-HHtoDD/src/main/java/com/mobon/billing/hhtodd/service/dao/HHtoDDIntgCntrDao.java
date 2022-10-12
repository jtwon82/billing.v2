package com.mobon.billing.hhtodd.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
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
public class HHtoDDIntgCntrDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoDDIntgCntrDao.class);

	public static double fixCount = 500000;
	public static int mediaIDCount = 400;
	
	@Value("${log.path}")
	private String logPath;
	

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;


	public boolean transectionRuningDDtoDDIntgCntr(Map param) {
		boolean result = false;
		String NAMESPACEIntgCntr = "hHtoDDIntgCntrMapper";

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;

				long startTimeT = System.currentTimeMillis();
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;

				try {
					List<Map> list2 = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACEIntgCntr, "sql_select_mod_date_dd"), param);

					if (list2.size() == 0) {
						logger.info("INTG_CNTR transectionRuningHHtoDD : 최종 수정된 날짜 데이타가 없습니다");
						return true;
					}

					logger.info("INTG_CNTR sql_SELECT_COM_CODE START");
					List<Map> list_com = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACEIntgCntr, "sql_SELECT_COM_CODE"));

					
					/* 지면단위 처리를 위한 지면 리스트 조회 */
					List<Map> mediaIDList = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACEIntgCntr, "sql_SELECT_MEDIA_ID_list"));
					logger.info("sql_SELECT_MEDIA_ID_list  " + mediaIDList.size());
					
					
					
					for (Map m2 : list2) { // 일별
						for (Map map_com : list_com) {// 공통 코드별 : INTG_TP_CODE

							m2.putAll(map_com);

							Map m3 = new HashMap();
							
							if(mediaIDList.size() <= mediaIDCount) {
								m2.put("MEDIA_ID", mediaIDList);
								
	
								m3 = sqlSessionTemplateBilling
										.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dd_select_tp_code_count"), m2);
								logger.info("INTG_CNTR m2 - {}, list3 - {}", m2, m3);
								
								
							} else {
								List<Map> mediaIDCountList = new ArrayList<Map>();
								for(int mediacnt = 0; mediacnt < mediaIDList.size() ; mediacnt ++) {
									
									if((mediacnt % mediaIDCount) == (mediaIDCount - 1) || (mediacnt == mediaIDList.size() - 1)) {
										mediaIDCountList.add(mediaIDList.get(mediacnt));
												
										m2.put("MEDIA_ID", mediaIDCountList);
										
										
										Map countResult = sqlSessionTemplateBilling
												.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dd_select_tp_code_count"), m2);
										
										if (countResult != null && !countResult.isEmpty()) {
											if(m3 == null || m3.isEmpty()) {
												m3.putAll(countResult);
											} else {
												m3.put("TP_CODE_COUNT", (Long) m3.get("TP_CODE_COUNT") + (Long)countResult.get("TP_CODE_COUNT"));
											}
										}
										
										mediaIDCountList.clear();
										
									} else {
										mediaIDCountList.add(mediaIDList.get(mediacnt));
										
									}
								}
							}
							

							if(m3 == null || m3.isEmpty()) {
								logger.info("m3 is Empty");
								continue;
							}
							
							// 임시 테이블 생성
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_DD_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_DD_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_PAR_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_PAR_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_MEDIA_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_MEDIA_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_MEDIA_ADVER_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_MEDIA_ADVER_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_CAMP_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_CAMP_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_ADVER_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_ADVER_STATS_TABLE", resutTime / 1000 + "(sec)");
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(
									String.format("%s.%s", NAMESPACEIntgCntr, "sql_CREATE_INTG_CNTR_ADVER_PAR_STATS_TABLE"), m3);
							sqlSessionTemplateBilling.flushStatements();
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_INTG_CNTR_ADVER_PAR_STATS_TABLE", resutTime / 1000 + "(sec)");
							
															
							
							
							// 총 수
							long TP_CODE_COUNT = m3.get("TP_CODE_COUNT") == null ? 0 : (long) m3.get("TP_CODE_COUNT");
							double ROW_COUNT_D = Double.parseDouble(Long.toString(TP_CODE_COUNT));
							double loopD = Math.ceil(ROW_COUNT_D / fixCount);
							int loopInt = (int) loopD;

							logger.info("loopInt ==> {}, ROW_COUNT_D - {}, fixCount - {}", loopInt, ROW_COUNT_D, fixCount);

							// 마지막 row
							m3.put("END_POINT", (int) fixCount);

							// 임시테이블(INTG_CNTR_CAMP_PAR_STATS)에 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  : {}, {}",
										"sql_DUMP_INTGCNTR_TEMP", resutTime / 1000 + "(sec)");

							}

							// 임시테이블 데이터 저장하기 (INTG_CNTR_CAMP_PAR_STATS -> SUB_TEMP_TABLE)
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_ADVER_PAR_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_ADVER_PAR_STATS_TEMP", resutTime / 1000 + "(sec)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_ADVER_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_ADVER_STATS_TEMP", resutTime / 1000 + "(sec)");
								
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_CAMP_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_CAMP_STATS_TEMP", resutTime / 1000 + "(sec)");
								
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_MEDIA_ADVER_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_MEDIA_ADVER_STATS_TEMP", resutTime / 1000 + "(sec)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_MEDIA_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_MEDIA_STATS_TEMP", resutTime / 1000 + "(sec)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_PAR_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_PAR_STATS_TEMP", resutTime / 1000 + "(sec)");

								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_STATS_TEMP"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_STATS_TEMP", resutTime / 1000 + "(sec)");
							}
							
							
							
							
							
							
							/* 데이터 저장 SUB_TEMP_TABLE -> SUB_TABLE */
							Map countMap = null;
							long List_COUNT = 0;
							logger.info("INTG_CNTR sql_INTG_CNTR_ADVER_PAR_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_ADVER_PAR_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장(
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_ADVER_PAR_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_ADVER_PAR_STATS", resutTime / 1000 + "(sec)");
							
							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_ADVER_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_ADVER_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
								
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_ADVER_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_ADVER_STATS", resutTime / 1000 + "(sec)");

							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_CAMP_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_CAMP_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
								
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_CAMP_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_CAMP_STATS", resutTime / 1000 + "(sec)");

							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_MEDIA_ADVER_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_MEDIA_ADVER_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_MEDIA_ADVER_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_MEDIA_ADVER_STATS", resutTime / 1000 + "(sec)");

							
							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_MEDIA_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_MEDIA_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_MEDIA_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_MEDIA_STATS", resutTime / 1000 + "(sec)");
							
							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_PAR_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_PAR_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
								
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_PAR_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_PAR_STATS", resutTime / 1000 + "(sec)");

							}
							
							
							
							
							logger.info("INTG_CNTR sql_INTG_CNTR_STATS_TEMP_count START");
							countMap = sqlSessionTemplateBilling
									.selectOne(String.format("%s.%s", NAMESPACEIntgCntr, "sql_INTG_CNTR_STATS_TEMP_count"));
							
							List_COUNT = countMap.get("LIST_COUNT") == null ? 0 : (long) countMap.get("LIST_COUNT");
							ROW_COUNT_D = Double.parseDouble(Long.toString(List_COUNT));
							loopD = Math.ceil(ROW_COUNT_D / fixCount);
							loopInt = (int) loopD;
							
							// 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));
							
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling
										.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_DUMP_INTGCNTR_STATS"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("INTG_CNTR {} Transaction BATCH Running Time (TBRT)  :{}, {}",
										"sql_DUMP_INTGCNTR_STATS", resutTime / 1000 + "(sec)");
							
							}
							// 끝
							
						}
					}

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("INTG_CNTR transectionRuningHHtoHHDDIntgCntr Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					// TEMP_TABLE DROP
					if (hisYN) {
						startTime = System.currentTimeMillis();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropDDTempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_PAR_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_MEDIA_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_MEDIA_ADVER_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_CAMP_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_ADVER_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEIntgCntr, "sql_dropINTG_CNTR_ADVER_PAR_STATS_TempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTimeT;
						logger.info("INTG_CNTR 일데이타 배치 종료  : Transaction BATCH Running Time (TBRT)  :{}, {}",
								"sql_dropDDTempTable", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}
	
	public boolean transectionRuningDDtoDDIntgCntrConv(Map param) {
		boolean result = false;
		String convDataMapper = "convDataMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				boolean hisYN = true;
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				try {
					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", convDataMapper, "insertCnvrsIntgStatsSub"));
					sqlSessionTemplateBilling.flushStatements();
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("IntgCntrConv {} Transaction BATCH Running Time (TBRT)  :{}, {}",
							"insertCnvrsIntgStatsSub", resutTime / 1000 + "(sec)");
					
				} catch (Exception e) {
					hisYN = false;
					logger.error("IntgCntrConv transectionRuningDDtoDDIntgCntrConv Exception ==>", e);
					
					status.setRollbackOnly();
					res = false;
					
				} finally {
					
					if (hisYN) {
					}
				}
				
				return res;
			}
		});
		return result;
	}
}
