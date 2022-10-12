package com.mobon.billing.hhtodd.service.dao;

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
public class HHtoDDMediaChrgDao {

	private static final Logger	logger	= LoggerFactory.getLogger(HHtoDDMediaChrgDao.class);

	public static final String	NAMESPACE	= "hHtoDDMapper";
	
	public static double fixCount = 100000;
	
	@Value("${log.path}")
	private String logPath;
	

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Autowired
	private TransactionTemplate transactionTemplate;


	public boolean transectionRuningDDtoDDMediaChrgReBuild(Map param) {
		boolean result = false;
		String namespace = "mediaChrgDataMapper";

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;

				long startTime = System.currentTimeMillis();
				long resutTime = System.currentTimeMillis() - startTime;

				try {
					List<Map> listStatsDttm = sqlSessionTemplateBilling.selectList(String.format("%s.%s", namespace, "selectMediaChrgReBuildDate"));
					
					if( listStatsDttm!=null ) {
						for( Map STATS_DTTM : listStatsDttm ) {
							logger.info("STATS_DTTM -{}", STATS_DTTM);
							sqlSessionTemplateBilling.update(String.format("%s.%s", namespace, "updateMediaChrgStep1"), STATS_DTTM);
							sqlSessionTemplateBilling.update(String.format("%s.%s", namespace, "updateMediaChrgStep2"), STATS_DTTM);
						}
					}

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("MEDIA_CHRG ReBuild transectionRuningHHtoHHDDMediaChrg Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("MEDIA_CHRG ReBuild 배치 종료  : Transaction BATCH Running Time (TBRT)  : {}", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}
	
	public boolean transectionRuningDDtoDDMediaChrg(Map param) {
		boolean result = false;
		String NAMESPACEMEDIACHRG = "hHtoDDMediaChrgMapper";

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
							.selectList(String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_select_mod_date_dd"), param);

					if (list2.size() == 0) {
						logger.info("MEDIA_CHRG transectionRuningHHtoDD : 최종 수정된 날짜 데이타가 없습니다");
						return true;
					}

					logger.info("MEDIA_CHRG sql_SELECT_COM_CODE START");
					List<Map> list_com = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_SELECT_COM_CODE"));

					for (Map m2 : list2) { // 일별
						
						
						for (Map map_com : list_com) {// 공통 코드별 : ADVRTS_TP_CODE

							m2.putAll(map_com);

							List<Map> listTpCode = sqlSessionTemplateBilling
									.selectList(String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_dd_select_tp_code_count"), m2);
							logger.info("MEDIA_CHRG m2 - {}, list3 - {}", m2, listTpCode);

							for (Map m3 : listTpCode) {//

								// 임시 테이블 생성
								startTime = System.currentTimeMillis();
								sqlSessionTemplateBilling.update(
										String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_CREATE_DD_TABLE"), m3);
								sqlSessionTemplateBilling.flushStatements();
								endTime = System.currentTimeMillis();
								resutTime = endTime - startTime;
								logger.info("MEDIA_CHRG {} Transaction BATCH Running Time (TBRT) : {}, {}", "sql_CREATE_DD_TABLE", resutTime / 1000 + "(sec)");

								// 총 수
								long TP_CODE_COUNT = m3.get("TP_CODE_COUNT") == null ? 0 : (long) m3.get("TP_CODE_COUNT");
								double ROW_COUNT_D = Double.parseDouble(Long.toString(TP_CODE_COUNT));
								double loopD = Math.ceil(ROW_COUNT_D / fixCount);
								int loopInt = (int) loopD;

								logger.info("loopInt==>{}", loopInt, ROW_COUNT_D, fixCount);

								// 마지막 row
								m3.put("END_POINT", (int) fixCount);

								// 임시테이블에 데이타 저장
								for (int i = 0; i < loopInt; i++) {
									m3.put("START_POINT", (int) ((i) * fixCount));

									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling.update(
											String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_INSERT_MEDIASCRIPT_CHRG_TEMP"), m3);
									sqlSessionTemplateBilling.flushStatements();
									
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.debug("MEDIA_CHRG {} Transaction BATCH Running Time (TBRT)  : {}, {}",
											"sql_INSERT_MEDIASCRIPT_CHRG_TEMP", resutTime + "(ms)");

								}

								// 임시테이블 데이터 저장하기
								for (int i = 0; i < loopInt; i++) {
									m3.put("START_POINT", (int) ((i) * fixCount));

									startTime = System.currentTimeMillis();
									sqlSessionTemplateBilling
											.update(String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_INSERT_MEDIA_CHRG_TEMP"), m3);
									sqlSessionTemplateBilling.flushStatements();
									
									endTime = System.currentTimeMillis();
									resutTime = endTime - startTime;
									logger.info("MEDIA_CHRG {} Transaction BATCH Running Time (TBRT)  :{}, {}",
											"sql_INSERT_MEDIA_CHRG_TEMP", resutTime + "(ms)");
								}
							}
						}
					}

					res = true;

				} catch (Exception e) {
					hisYN = false;
					logger.error("MEDIA_CHRG transectionRuningHHtoHHDDMediaChrg Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {

					if (hisYN) {
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACEMEDIACHRG, "sql_dropDDTempTable"), param);
						sqlSessionTemplateBilling.flushStatements();
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTimeT;
						logger.info("MEDIA_CHRG 일데이타 배치 종료  : Transaction BATCH Running Time (TBRT)  :{}, {}",
								"sql_dropDDTempTable", resutTime / 1000 + "(sec)");
					}
				}

				return res;
			}
		});
		return result;
	}
	
}
