package com.mobon.billing.viewclicklog.service.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;

import com.adgather.constants.G;
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

import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.viewclicklog.service.SumObjectManager;

@Repository
public class ConversionVoDao {

	private static final Logger	logger = LoggerFactory.getLogger(ConversionVoDao.class);

	public static final String NAMESPACE = "conversionMapper";

	@Resource (name = "sqlSessionTemplateClickhouse")
	private SqlSessionTemplate sqlSessionTemplateClickhouse;

	@Autowired
	private ViewClickVoDao viewClickVoDao;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Autowired
	private SumObjectManager sumObjectManager;

	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;

	/**
	 * 트랜잭션 처리 메소드 콜 및 리턴처리
	 */
	public boolean transectionRuningV3(List<ConversionVo> list) {
		boolean result = false;
		
		long startTime = System.currentTimeMillis();
		int listSize = list.size();
		int successCnt = 0;
		int failCnt = 0;
		
		try {
			// 건 by 건으로 처리
			for (ConversionVo vo : list) {
				// 전환 인정여부 판단 (트랜잭션 처리때문에 DAO 에 위치)
				ConversionVo availalbeConversionVo = dumpConversionVo(vo);

				if (availalbeConversionVo == null) {
					logger.info("Conv nothing view log object - {}", vo);
					failCnt++;
				} else {
					// insert or update
					boolean tmpbool = transectionRunning(availalbeConversionVo);
					if (!tmpbool && vo.increaseRetryCnt() <= 3) {
						logger.info("Conv insert error object - {}", availalbeConversionVo);
						sumObjectManager.appendConversionVo(availalbeConversionVo);
					}

					successCnt++;
				}
			}

			result = true;
		} catch (Exception e) {
			logger.error("err transectionRuningV3 ", e);
			result = false;
		} finally {
			logger.debug("succ sqlSession flush");
			
			long endTime = System.currentTimeMillis();
	        long resutTime = endTime - startTime;
			logger.info("PERFORMANCE_TEST Transaction BATCH Running Time (TBRT)  : " + resutTime / 1000 + "(sec)");
			logger.info("PERFORMANCE_TEST Transaction BATCH listSize : " + listSize + " / successCnt : " + successCnt
					+ " / failCnt : " + failCnt);
		}
		
		return result;
	}

	/**
	 * insert or update 트랜잭션 처리
	 * @param vo - 인증처리된 ConversionVo
	 * @return true:정상처리, false:처리오류
	 */
	public boolean transectionRunning(ConversionVo vo) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			long startTime = System.currentTimeMillis();

			public Object doInTransaction(TransactionStatus status) {
				try {

					if (StringUtils.isBlank(vo.getDuplicatedOrderNo())) {
						// 중복되는 주문번호 없을시
						boolean approval_conversion = true;		// 어뷰징용 (어뷰징데이터라면 false 처리)

						if (approval_conversion) {
							// INSERT MOB_CNVRS_RENEW_NCL
							sqlSessionTemplateClickhouse.insert(String.format("%s.%s",NAMESPACE, "insertConversionLog"), vo);
							sqlSessionTemplateClickhouse.flushStatements();

							logger.info("TR Time (TBRT) Conv : " + (System.currentTimeMillis() - startTime) + "(ms) "+ vo.getIp());
							logger.info("Conv succ object - {}", vo);

							res = true;
						}
					} else {
						// 중복되는 주문번호여도 트래커 전환이면 적재
						if ("90".equals(vo.getTrkTpCode())) {
							logger.info("Conv overlap orderno object - {}", vo);
						} else {
							// INSERT MOB_CNVRS_RENEW_NCL
							sqlSessionTemplateClickhouse.insert(String.format("%s.%s", NAMESPACE, "insertConversionLog"), vo);
							sqlSessionTemplateClickhouse.flushStatements();

							logger.info("TR Time (TBRT) TRK Conv : " + (System.currentTimeMillis() - startTime) + "(ms) "+ vo.getIp());
							logger.info("TRK Conv succ object - {}", vo);
						}

						res = true;
					}

				} catch (Exception e) {
					logger.error("err transectionRuning object -{}", vo);
					status.setRollbackOnly();
					res = false;

				} finally {
					logger.debug("succ transectionRunning flush");
				}
				return res;
			}
		});

		return result;
	}

	/**
	 * 인정된 ConversionVo 를 전달
	 * @param conversionVo - 미인증 상태의 전환 데이터
	 * @return null : 인정X, !null : 인정O
	 */
	private ConversionVo dumpConversionVo(ConversionVo conversionVo) {
		// 강제 컨버전 이외에는 모두 로직 수행
		if (!conversionVo.isConversionDirect()) {
			// 최근 30일 혹은 indirect 이내의 최신 노출 데이터 조회
			Map<String, Object> viewData = getViewData(conversionVo);

			if (viewData == null) {
				// 노출이력 없으면 전환처리 중지
				return null;
			} else {
				// 노출이력 있으면 전환처리 계속
				// 브랜딩전환 노출관련 정보 입력
				conversionVo.setPlatform(viewData.get("PLTFOM_TP_CODE").toString());
				conversionVo.setProduct(viewData.get("ADVRTS_PRDT_CODE").toString());
				conversionVo.setAdGubun(viewData.get("ADVRTS_TP_CODE").toString());
				conversionVo.setCtgrSeq(viewData.get("CTGR_SEQ").toString());
				conversionVo.setKpiNo(viewData.get("KPI_NO").toString());
				conversionVo.setSiteCode(viewData.get("SITE_CODE").toString());
				conversionVo.setMediaTpCode(viewData.get("MEDIA_TP_CODE").toString());
				conversionVo.setScriptNo(viewData.get("MEDIA_SCRIPT_NO").toString());
				conversionVo.setInterlock(viewData.get("ITL_TP_CODE").toString());
				conversionVo.setLogDttm(viewData.get("LOG_DTTM").toString());
				conversionVo.setNoExposureYN(viewData.get("UNEXPOSURE_YN").toString());
				conversionVo.setScriptUserId(viewData.get("MEDIA_ID").toString());

				if (Integer.parseInt(viewData.get("POS_IP_INFO").toString()) > 0) {
					// 쇼플 자사전환은 간접으로 처리 (?)
					/* 이부분 모비온 전환에서는 CHRG_TP_CODE 어떻게 처리하는지 확인이 필요하나
					현재 해당 값으로 데이터가 없음. 추 후, 데이터가 생기면 확인 후, 마이그레이션 필요할 수 있음 */
					logger.info("Conv manual indirect");
					conversionVo.setCnvrsTpCode("03");
					conversionVo.setInDirectChargeValue(true);
				} else {
					// 프리퀀시 체크하여 인정처리된 전환만 BRAND_CNVRS_LOG 적재로직으로 넘긴다
					if (!checkFrequency(conversionVo, viewData))
						return null;

					// 중복주문번호 셋
					conversionVo.setDuplicatedOrderNo(selectDuplicatedOrderNo(conversionVo));
					logger.debug("Conv duplicatedOrderNo {}", conversionVo.getDuplicatedOrderNo());
				}
			}
		}

		return conversionVo;
	}

	/**
	 * ConversionVo 와 매칭되는 convLimitDay 일 이내의 노출 데이터 조회
	 * @param conversionVo - 미인증 상태의 전환 데이터
	 * @return conversionVo 와 매칭되는 노출 데이터
	 */
	private HashMap<String, Object> getViewData(ConversionVo conversionVo) {
		HashMap<String, Object> result = null;
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<String> ipList = new ArrayList<>();

		try {
			String indirectDttm = LocalDateTime
					.parse(conversionVo.getSendDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
					// .minusDays(conversionVo.getCookieInDirect())
					.minusDays(10)	// 노출 데이터 서칭기간 10일로 임시 변경
					.minusMinutes(convDelayTimeMinute)
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

			// crossLoginIp 처리
			if (conversionVo.getCrossLoginIp() != null) {
				if (conversionVo.getCrossLoginIp().size() > 0) {
					ipList.addAll(conversionVo.getCrossLoginIp());
				}
			}

			// ipInfoList 처리
			if (!StringUtils.isBlank(conversionVo.getIpInfoList())) {
				ipList.addAll(Arrays.asList(conversionVo.getIpInfoList().split("\\|")));
			}

			// ip 처리
			ipList.add(conversionVo.getIp());

			map.put("indirectDttm", indirectDttm);
			map.put("yyyymmdd", conversionVo.getYyyymmdd());
			map.put("sendDate", conversionVo.getSendDate());
			map.put("ipList", ipList);
			map.put("ipInfoList", conversionVo.getIpInfoList());
			map.put("auId", conversionVo.getAuId());
			map.put("advertiserId", conversionVo.getAdvertiserId());
			map.put("convDelayTimeMinute", convDelayTimeMinute);
			map.put("cookieInDirect", conversionVo.getCookieInDirect());

			/* 요청으로 인해 아래 데이터는 노출데이터 조회에서 사용하지 않습니다.
			다만 전환 로그 조회시 편의성을 위해 남겨둡니다.*/
			map.put("platformCode", conversionVo.getPlatformCode());
			map.put("productCode", conversionVo.getProductCode());
			map.put("adGubunCode", conversionVo.getAdGubunCode());
			map.put("siteCode", conversionVo.getSiteCode());
			map.put("scriptNo", conversionVo.getScriptNo());
			map.put("interlock", conversionVo.getInterlock());

			result = (HashMap<String, Object>) viewClickVoDao.selectMatchedLog(map);
			// result = (HashMap<String, Object>) viewClickVoDao.selectMatchedLogLoof(map);
		} catch (Exception e) {
			logger.error("Conv getViewData err - {} / map - {}", e, map);
			result = null;
		}

		if (result != null) {
			logger.info("Conv getViewData succ map - {}", map);
		}

		return result;
	}

	/**
	 * 전환빈도 체크 (브랜딩ROAS 용 전환 정책)
	 *
	 * <pre>
	 * 브라우저 세션 전환 : 노출 후, 30분 이내의 전환
	 * 세션 전환 : 노출 후, 6시간 이내의 전환
	 * 직접 전환 : 노출 후, 광고주 cookieDirect 설정시간 이내의 전환
	 * 간접 전환 : 노출 후, 30일 이내의 전환
	 * </pre>
	 *
	 * @param conversionVo - 미인증 상태의 전환 데이터
	 * @param viewData - conversionVo 와 매칭되는 노출 데이터
	 * @return true:인증O, false:인증X
	 */
	private boolean checkFrequency(ConversionVo conversionVo, Map<String, Object> viewData) {
		boolean result = true;

		try {
			int diffDttm =
					Integer.parseInt(viewData.get("DIFF_SEND_DTTM").toString());	// 노출이력과 전환전송일시의 시간차 (초)
			int browserLimit = 1800;												// 브라우저 세션 매출 상한선 (30분 이내 초)
			int sessionLimit = 21600;												// 세션 매출 상한선 (6시간 이내 초)
			int directLimit = (3600 * conversionVo.getCookieDirect());				// 직접 매출 상한선 (cookieDirect 시간 이내 초)
			int inDirectLimit = (86400 * conversionVo.getCookieInDirect());			// 간접 매출 상한선 (cookieInDirect 일 이내 초)

			if (diffDttm > inDirectLimit) {
				conversionVo.setInvalidChargeValue();           // 무효
				result = false;									// 무효는 적재 안함
			}

			if (diffDttm <= inDirectLimit)
				conversionVo.setInDirectChargeValue(true);		// 간접 전환

			if (diffDttm <= directLimit)
				conversionVo.setDirectChargeValue(true);		// 직접 전환

			if (diffDttm <= sessionLimit)
				conversionVo.setSessionChargeValue(true);		// 세션 전환

			if (diffDttm <= browserLimit)
				conversionVo.setBrowserChargeValue(true);		// 브라우저 세션 전환

			// MariaDB는 중복키로 적재가 안될탠데 클릭하우스는 키가 없으므로 수동으로 필터링 합니다.
			if (selectFrequencyDuplicateData(conversionVo) == 0) {
				insertFrequency(conversionVo);        // 프리퀀시 인서트
				logger.info("Conv insertFrequency succ object - {}", conversionVo);
			} else {
				result = false;    // 중복 키 데이터 존재시 처리하지 않음
				logger.info("Conv insertFrequency duplicated orderNo object - {}", conversionVo);
			}
		} catch (Exception e) {
			logger.error("Conv checkFrequency err - {} / object - {}", e, conversionVo);
			result = false;
		}

		return result;
	}

	/**
	 * 전환빈도 체크 (모비온과 동일한 전환 인정 정책)
	 * @param conversionVo - 미인증 상태의 전환 데이터
	 * @param viewData - conversionVo 와 매칭되는 노출 데이터
	 * @return true:인증O, false:인증X
	 */
	private boolean checkFrequencyVerMobon(ConversionVo conversionVo, Map<String, Object> viewData) {
		boolean result = true;

		try {
			int diffDttm =
					Integer.parseInt(viewData.get("DIFF_SEND_DTTM").toString());	// 노출이력과 전환전송일시의 시간차 (초)
			int browserLimit = 1800;												// 브라우저 세션 매출 상한선 (30분 이내 초)
			int sessionLimit = 21600;												// 세션 매출 상한선 (6시간 이내 초)
			int directLimit = (60 * 60 * conversionVo.getCookieDirect());			// 직접 매출 상한선 (cookieDirect시간 이내 초)
			String beforeChrgTpCode = "";											// 변경 이전상태의 매출 타입 저장
			boolean isDirect = (diffDttm <= directLimit);							// true:직접O, false:직접X
			boolean isSession =
					isDirect && "Y".equals(conversionVo.getSesionSelngYn());		// true:세션O, false:세션X
			boolean isAvailalbe = true;												// true:인정O, false:인정X

			// 서비스코드 세팅 - CONV_BRANDING_ROAS = '13'
			conversionVo.setSvcTpCode("13");

			if (isDirect && isSession) {
				beforeChrgTpCode = StringUtils.isBlank(beforeChrgTpCode) ? "11" : beforeChrgTpCode;
				conversionVo.setSessionChargeValueVerMobon(true);	// 세션전환O
				isSession = (selectFrequencyCount(conversionVo) < 11);	// 11건 초과:세션전환X, 직접전환O
			}

			if (isDirect && !isSession) {
				beforeChrgTpCode = StringUtils.isBlank(beforeChrgTpCode) ? "12" : beforeChrgTpCode;
				conversionVo.setDirectChargeValueVerMobon(true);	// 직접전환O
				isDirect = (selectFrequencyCount(conversionVo) < 15);		// 15건 초과:직접전환X, 간접전환O
			}

			if (!isDirect && !isSession) {
				beforeChrgTpCode = StringUtils.isBlank(beforeChrgTpCode) ? "13" : beforeChrgTpCode;
				conversionVo.setInDirectChargeValueVerMobon(true);	// 간접전환O
				isAvailalbe = (selectFrequencyCount(conversionVo) < 19);	// 19건 초과:간접전환X, 무효
			}

			if (!isAvailalbe) {
				if ("11".equals(beforeChrgTpCode)) {
					conversionVo.setSessionChargeValueVerMobon(false);	// 세션무효
					result = false;
				} else if ("12".equals(beforeChrgTpCode)) {
					conversionVo.setDirectChargeValueVerMobon(false);	// 직접무효
					result = false;
				} else if ("13".equals(beforeChrgTpCode)) {
					conversionVo.setInDirectChargeValueVerMobon(false);	// 간접무효
					result = false;
				} else {
					conversionVo.setInvalidChargeValueVerMobon();    		// 무효
					result = false;
				}
			}

			// 최종 시간으로 세션 전환 처리
			if (diffDttm <= sessionLimit) {
				// 최종 시간으로 브라우저 세션 전환 처리
				conversionVo.setBrowserChargeValueVerMobon(diffDttm <= browserLimit);

				/* 시간 > 건수 순으로 정리된 데이터를 마지막에 최종으로 시간 순으로 플래그를
				다시 세팅한다. 따라서 chrgTpCode = '12' 로 직접매출이지만 시간상 세션전환으로
				sesionSelngYn = 'Y' 가 존재할 수 있다.*/

				/* 워에서 사용하는 setValue 를 사용하면 intervalHour, svcTpCode, chrgTpCode가
				변경되기때문에 직접 sesionSelngYn과 directYn을 수정한다.*/
				conversionVo.setSesionSelngYn("Y");
				conversionVo.setDirectYn("Y");
			}

			// 중복 키 데이터 체크
			if (selectFrequencyDuplicateData(conversionVo) == 0) {
				insertFrequency(conversionVo);		// 프리퀀시 인서트
			} else {
				result = false;	// 중복 키 데이터 존재시 처리하지 않음
			}

		} catch (Exception e) {
			logger.error("Conv checkFrequencyVerMobon err - {} / object - {}", e, conversionVo);
			result = false;
		}

		return result;
	}

	/**
	 * 같은 타입의 전환 건수 조회
	 * @param conversionVo - 미인증 상태의 전환 관련 데이터
	 * @return
	 */
	public int selectFrequencyCount(ConversionVo conversionVo) {
		return sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectFrequencyCount"), conversionVo);
	}

	/**
	 * 같은 키를 가진 데이터의 건수 조회
	 * @param conversionVo - 인증처리 완료된 전환 관련 데이터
	 * @return
	 */
	public int selectFrequencyDuplicateData(ConversionVo conversionVo) {
		return sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectFrequencyDuplicateData"), conversionVo);
	}

	/**
	 * 전환 프리퀀시 저장
	 * @param conversionVo - 인정 정보를 가지고있는 전환 데이터
	 */
	public void insertFrequency(ConversionVo conversionVo) {
		sqlSessionTemplateClickhouse.insert(String.format("%s.%s", NAMESPACE, "insertFrequency"), conversionVo);
	}

	/**
	 * 중복 주문번호 조회
	 * @param conversionVo - 인증 상태의 전환 데이터
	 * @return 중복 주문번호
	 */
	public String selectDuplicatedOrderNo(ConversionVo conversionVo) {
		return sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s", NAMESPACE, "selectDuplicatedOrderNo"), conversionVo);
	}

}
