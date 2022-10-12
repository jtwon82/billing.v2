package com.mobon.billing.hhtodd.service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate; 

@Repository
public class HHtoMTHParGatrDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoMTHParGatrDao.class);

	public static double fixCount = 50000;
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;


	public boolean transectionRuning(Map param) {
		boolean result = false;
		String NAMESPACE = "parGatrDataMapper";

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("PAR_GATR sql_SELECT_COM_CODE START");
					
					List<Map> list_com = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACE, "sql_SELECT_COM_CODE"));
					
					for (Map map_com : list_com) {
						List<Map> listTpCode = sqlSessionTemplateBilling
								.selectList(String.format("%s.%s", NAMESPACE, "sql_dd_select_tp_code_count"), map_com);
						
						for (Map m3 : listTpCode) {
							// 총 수
							long TP_CODE_COUNT = m3.get("TP_CODE_COUNT") == null ? 0 : (long) m3.get("TP_CODE_COUNT");
							double ROW_COUNT_D = Double.parseDouble(Long.toString(TP_CODE_COUNT));
							double loopD = Math.ceil(ROW_COUNT_D / fixCount);
							int loopInt = (int) loopD;
							
							// 마지막 row
							m3.put("END_POINT", (int) fixCount);

							startTime = System.currentTimeMillis();
							// 임시테이블의 데이타 저장
							for (int i = 0; i < loopInt; i++) {
								m3.put("START_POINT", (int) ((i) * fixCount));

								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS1"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS2"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS3"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS4"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS5"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS6"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
								
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACE, "INSERT_TEMP_PARGATR_STATS7"), m3);
								sqlSessionTemplateBilling.flushStatements();
								resutTime = System.currentTimeMillis() - startTime;
								logger.info("PAR_GATR {} (TBRT)  : {}, {}, {}",
										"INSERT_TEMP_PARGATR_STATS1~7", resutTime / 1000 + "(sec)", m3);
							}
						}
					}

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("PAR_GATR transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("PAR_GATR 일데이타 배치 종료  : (TBRT)  :{}, {}",
								"update_TRGT_ADVER_MTH_HH_STATS", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}


	public boolean transectionRevisionRuning(String step, int STATS_MTH){
		boolean result = false;
		String NAMESPACE = "parGatrDataMapper";
		
		Map param = new HashMap();
		param.put("STATS_MTH", STATS_MTH);

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("MTH_REVISION  START");

					logger.info("MTH_REVISION  DELETE");
					List<Map> list_delete_code = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACE, "SELECT_DELETE_CODE"), param);
					for (Map code : list_delete_code) {
						if("step2".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_CAMP_PAR_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_CAMP_PAR_MTH", code, resutTime / 1000 + "(sec)");
						}

						if("step3".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_ADVER_PAR_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_ADVER_PAR_MTH", code, resutTime / 1000 + "(sec)");
						}
						
						if("step".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_CAMP_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_CAMP_MTH", code, resutTime / 1000 + "(sec)");
	
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_ADVER_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_ADVER_MTH", code, resutTime / 1000 + "(sec)");
	
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_MEDIA_PAR_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_MEDIA_PAR_MTH", code, resutTime / 1000 + "(sec)");
	
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_MEDIA_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_MEDIA_MTH", code, resutTime / 1000 + "(sec)");
	
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_COM_STATS_PAR_MTH"), code);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}", "DELETE_COM_STATS_PAR_MTH", code, resutTime / 1000 + "(sec)");
						}
					}

					logger.info("MTH_REVISION  UPDATE");
					List<Map> list_com = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACE, "SELECT_DTTM_ADVRTS_CODE"), param);
					for (Map map_com : list_com) {
						if("step2".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_CAMP_PAR_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_CAMP_PAR_MTH", resutTime / 1000 + "(sec)", map_com);
						}
						
						if("step3".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_ADVER_PAR_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_ADVER_PAR_MTH", resutTime / 1000 + "(sec)", map_com);
						}
						
						if("step".equals(step)) {
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_CAMP_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_CAMP_MTH", resutTime / 1000 + "(sec)", map_com);
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_ADVER_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_ADVER_MTH", resutTime / 1000 + "(sec)", map_com);
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_MEDIA_PAR_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_MEDIA_PAR_MTH", resutTime / 1000 + "(sec)", map_com);
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_MEDIA_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_MEDIA_MTH", resutTime / 1000 + "(sec)", map_com);
							
							startTime = System.currentTimeMillis();
							sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "UPDATE_COM_STATS_MTH"), map_com);
							sqlSessionTemplateBilling.flushStatements();
							resutTime = System.currentTimeMillis() - startTime;
							logger.info("MTH_REVISION {} (TBRT)  : {}, {}, {}",
									"UPDATE_COM_STATS_MTH", resutTime / 1000 + "(sec)", map_com);
						}
					}
					
					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("MTH_REVISION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("MTH_REVISION 종료  : (TBRT) :{}, {}", "MTH_STATS", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}

	public boolean transectionConversionRuning(Map param) {
		boolean result = false;
		String NAMESPACE = "convDataMapper";

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					
					logger.info("INSERT_MOB_CNVRS_RENEW_MTH_STATS START");

					sqlSessionTemplateBilling.update(
							String.format("%s.%s", NAMESPACE, "INSERT_MOB_CNVRS_RENEW_MTH_STATS"));
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CNVRS_RENEW_MTH_STATS {} (TBRT)  : {}, {}",
							"INSERT_MOB_CNVRS_RENEW_MTH_STATS", resutTime / 1000 + "(sec)");

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CNVRS_RENEW_MTH_STATS transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("INSERT_MOB_CNVRS_RENEW_MTH_STATS 일데이타 배치 종료  : (TBRT)  :{}, {}",
								"INSERT_MOB_CNVRS_RENEW_MTH_STATS", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}


	public boolean updateDiffCnvrsRenewMthStatsRevision(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "parGatrDataMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("DELETE_MOB_CNVRS_RENEW_MTH_STATS START");
					sqlSessionTemplateBilling.update(
							String.format("%s.%s", NAMESPACE, "DELETE_MOB_CNVRS_RENEW_MTH_STATS_REVISION"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("DELETE_MOB_CNVRS_RENEW_MTH_STATS {} (TBRT)  : {}, {}",
							"DELETE_MOB_CNVRS_RENEW_MTH_STATS", resutTime / 1000 + "(sec)");
					
					logger.info("UPDATE_MOB_CNVRS_RENEW_MTH_STATS START");

					sqlSessionTemplateBilling.update(
							String.format("%s.%s", NAMESPACE, "UPDATE_MOB_CNVRS_RENEW_MTH_STATS_REVISION"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("UPDATE_MOB_CNVRS_RENEW_MTH_STATS {} (TBRT)  : {}, {}",
							"UPDATE_MOB_CNVRS_RENEW_MTH_STATS", resutTime / 1000 + "(sec)");

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CNVRS_RENEW_MTH_STATS transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("INSERT_MOB_CNVRS_RENEW_MTH_STATS 월데이타 보정 배치 종료  : (TBRT)  :{}, {}",
								"INSERT_MOB_CNVRS_RENEW_MTH_STATS", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}

}
