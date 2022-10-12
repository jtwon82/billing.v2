package com.mobon.billing.viewclicklog.service.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.mobon.billing.viewclicklog.util.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.v20.ViewClickVo;


@Repository
public class ViewClickVoDao {

	private static final Logger	logger = LoggerFactory.getLogger(ViewClickVoDao.class);

	public static final String NAMESPACE = "viewClickMapper";

	@Resource (name = "sqlSessionTemplateClickhouse")
	private SqlSessionTemplate sqlSessionTemplateClickhouse;

	@Autowired
	private TransactionTemplate transactionTemplate;

	/**
	 * insert or update 트랜잭션 처리
	 * @param flushMap - group 처리된 Map
	 * @return true:정상처리, false:처리오류
	 */
	public boolean transectionRuningV2(HashMap<String, ArrayList<ViewClickVo>> flushMap) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			long startTime= System.currentTimeMillis();
			
			public Object doInTransaction(TransactionStatus status) {
				try {

					int totalBillingSize = 0;
					
					for (Entry<String, ArrayList<ViewClickVo>> item : flushMap.entrySet()) {
						ArrayList<ViewClickVo> data = item.getValue();
						String dataKey = item.getKey();
						int dataSize = data.size();

						if (dataSize > 0) {
							logger.info("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
							sqlSessionTemplateClickhouse.update(String.format("%s.%s", NAMESPACE, dataKey), data);
							totalBillingSize += dataSize;
						}
					}

					sqlSessionTemplateClickhouse.flushStatements();
					long resutTime = System.currentTimeMillis() - startTime;
					logger.info("TR Time (TBRT)  : " + resutTime +"(ms) totalsize - "+ totalBillingSize);
					
					res = true;
				} catch (Exception e) {
					logger.error("err transectionRuning ", e);
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

	/**
	 * 전환과 매칭되는 노출 데이터 조회. 1차 IP, 2차 AU_ID
	 * @param map - 로그와 매칭될 전환 데이터의 정보
	 * @return 매칭되는 노출 데이터의 정보 (없거나 에러는 null)
	 */
	public Map<String, Object> selectMatchedLog(HashMap<String, Object> map) {
		Map<String, Object> check = null;
		Map<String, Object> result = null;

		try {
			long startCheckTime = System.currentTimeMillis();

			check = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogCheck"), map);
			logger.info("TR Time Conv selectMatchedLogCheck - {}(ms)", System.currentTimeMillis() - startCheckTime);
		} catch (Exception e) {
			logger.error("Conv selectMatchedLog err - {} / map - {}", e, map);
		}

		try {
			if (check != null && check.size() > 0) {
				String ip = (check.get("IP") != null) ? (String) check.get("IP") : "";

				long startValueTime = System.currentTimeMillis();
				if (!StringUtils.isBlank(ip)) {
					try {
						result = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogByIp"), map);
						logger.info("TR Time Conv selectMatchedLogByIp - {}(ms)", System.currentTimeMillis() - startValueTime);
					} catch (Exception e) {
						logger.error("Conv selectMatchedLogByIp err - {} / map - {}", e, map);
					}

				} else {
					try {
						result = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogByAuid"), map);
						logger.info("TR Time Conv selectMatchedLogByAuid - {}(ms)", System.currentTimeMillis() - startValueTime);
					} catch (Exception e) {
						logger.error("Conv selectMatchedLogByAuid err - {} / map - {}", e, map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Conv selectMatchedLog err - {} / map - {}", e, map);
		}

		return result;
	}

	/**
	 * 전환과 매칭되는 노출 데이터 조회. 1차 IP, 2차 AU_ID
	 * @param map - 로그와 매칭될 전환 데이터의 정보
	 * @return 매칭되는 노출 데이터의 정보 (없거나 에러는 null)
	 */
	public Map<String, Object> selectMatchedLogLoof(HashMap<String, Object> map) {
		int maxDay = 30;
		int partition = 5;
		int roofLimit = maxDay / partition;
		String yyyymmdd = (String) map.get("yyyymmdd");
		Map<String, Object> check = null;
		Map<String, Object> result = null;

		for (int cnt = 0; cnt < roofLimit; cnt++) {
			LocalDate endDateTime = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"));

			String limitDttm = endDateTime.minusDays(30)
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

			String endDttm = endDateTime
					.minusDays((partition+1) * cnt)
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

			String startDttm = LocalDate.parse(endDttm, DateTimeFormatter.ofPattern("yyyyMMdd"))
					.minusDays(partition)
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

			map.put("indirectDttm", (Integer.parseInt(startDttm) <= Integer.parseInt(limitDttm)) ? limitDttm : startDttm);
			map.put("yyyymmdd", endDttm);

			try {
				long startCheckTime = System.currentTimeMillis();

				check = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogCheck"), map);
				logger.info("TR Time Conv selectMatchedLogCheck - {}(ms)", System.currentTimeMillis() - startCheckTime);
			} catch (Exception e) {
				logger.error("Conv selectMatchedLog err - {} / map - {}", e, map);
			}

			try {
				if (check != null && check.size() > 0) {
					String ip = (check.get("IP") != null) ? (String) check.get("IP") : "";

					long startValueTime = System.currentTimeMillis();
					if (!StringUtils.isBlank(ip)) {
						try {
							result = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogByIp"), map);
							logger.info("TR Time Conv selectMatchedLogByIp - {}(ms)", System.currentTimeMillis() - startValueTime);
							break;
						} catch (Exception e) {
							logger.error("Conv selectMatchedLogByIp err - {} / map - {}", e, map);
						}

					} else {
						try {
							result = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectMatchedLogByAuid"), map);
							logger.info("TR Time Conv selectMatchedLogByAuid - {}(ms)", System.currentTimeMillis() - startValueTime);
							break;
						} catch (Exception e) {
							logger.error("Conv selectMatchedLogByAuid err - {} / map - {}", e, map);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Conv selectMatchedLog err - {} / map - {}", e, map);
			}
		}

			return result;
		}

}
