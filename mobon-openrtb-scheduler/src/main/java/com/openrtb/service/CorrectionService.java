package com.openrtb.service;

import com.openrtb.db.billing.BillingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CorrectionService {

	@Autowired
	private BillingMapper billingMapper;

	public void dbConnTest() {
		try {
			log.info("DB Conn Test : " + billingMapper.getBillingTest());
		} catch (Exception e) {
			log.error("DB Conn Test Error!!", e);
		}
	}

	public void checkTimeData(Map<String, String> param) {
		log.info("============================== 시간대 체크 날짜 : " + param.get("date") + " ==============================");
		log.info("============================== 시간대 구분 코드 : " + param.get("itl_tp_code")
				+ " ==============================");
		List<Map<String, Object>> billingTimeDataList = null;
		try {
			billingTimeDataList = billingMapper.getBillingTimeDataList(param);
		} catch (Exception e) {
			log.error("빌링 시간대 데이터 리드 에러!!", e);
		}

		List<Map<String, Object>> openRtbTimeDataList = null;
		try {
			openRtbTimeDataList = billingMapper.getOepnRtbTimeDataList(param);
		} catch (Exception e) {
			log.error("OpenRTB 시간대 데이터 리드 에러!!", e);
		}

		if (billingTimeDataList != null && openRtbTimeDataList != null) {
			if (billingTimeDataList.size() == openRtbTimeDataList.size()) {
				log.info("================================ 시간대 체크 ================================");
				for (int i = 0; i < billingTimeDataList.size(); i++) {
					Map<String, Object> billingTimeData = billingTimeDataList.get(i);
					Map<String, Object> openRtbTimeData = openRtbTimeDataList.get(i);

					log.info("billingTimeData : " + billingTimeData.toString());
					log.info("openRtbTimeData : " + openRtbTimeData.toString());

					long billingData = Long.parseLong(billingTimeData.get("TOT_EPRS_CNT").toString());
					long openRtbData = Long.parseLong(openRtbTimeData.get("TOT_EPRS_CNT").toString());

					if (billingData != openRtbData) {
						String time = billingTimeData.get("STATS_HH").toString();
						log.info(time + "시 데이터 작업 시작!!");
						param.put("time", time);

						try {
							log.info("1.MOB_CAMP_HH_STATS 작업 시작!!");
							long startTime = System.currentTimeMillis();
							int count = billingMapper.del_MOB_CAMP_HH_STATS(param);
							long endTime = System.currentTimeMillis();
							log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
							startTime = System.currentTimeMillis();
							count = billingMapper.insert_MOB_CAMP_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

							log.info("2.MOB_ADVER_HH_STATS 작업 시작!!");
							startTime = System.currentTimeMillis();
							count = billingMapper.del_MOB_ADVER_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
							startTime = System.currentTimeMillis();
							count = billingMapper.insert_MOB_ADVER_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

							log.info("3.MOB_COM_HH_STATS_INFO 작업 시작!!");
							startTime = System.currentTimeMillis();
							count = billingMapper.del_MOB_COM_HH_STATS_INFO(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
							startTime = System.currentTimeMillis();
							count = billingMapper.insert_MOB_COM_HH_STATS_INFO(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

							log.info("4.MOB_MEDIA_SCRIPT_HH_STATS 작업 시작!!");
							startTime = System.currentTimeMillis();
							count = billingMapper.del_MOB_MEDIA_SCRIPT_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
							startTime = System.currentTimeMillis();
							count = billingMapper.insert_MOB_MEDIA_SCRIPT_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

							log.info("5.MOB_MEDIA_HH_STATS 작업 시작!!");
							startTime = System.currentTimeMillis();
							count = billingMapper.del_MOB_MEDIA_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
							startTime = System.currentTimeMillis();
							count = billingMapper.insert_MOB_MEDIA_HH_STATS(param);
							endTime = System.currentTimeMillis();
							log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

							log.info(billingTimeData.get("STATS_HH") + "시 데이터 작업 완료!!");
						} catch (Exception e) {
							log.error("시간대 작업 에러!!", e);
						}

					} else {
						log.info(billingTimeData.get("STATS_HH") + "시 데이터 이상 없음!!");
					}
				}
			} else {
				log.error("Time Data size error!!");
			}
		} else {
			log.error("Time Data error!!");
		}
		log.info("============================== 시간대 체크 완료 ==============================");
	}

	public void checkDailyData(Map<String, String> param) {
		log.info("============================== 일자별 체크 날짜 : " + param.get("date") + " ==============================");
		log.info("============================== 일자별 구분 코드 : " + param.get("itl_tp_code")
				+ " ==============================");

		Map<String, Object> billingDayData = null;
		try {
			billingDayData = billingMapper.getBillingDayDataList(param);
		} catch (Exception e) {
			log.error("빌링 일자별 데이터 리드 에러!!", e);
		}
		Map<String, Object> openRtbDayData = null;
		try {
			openRtbDayData = billingMapper.getOepnRtbDayDataList(param);
		} catch (Exception e) {
			log.error("OpenRTB 일자별 데이터 리드 에러!!", e);
		}

		log.info("================================ 일자별 체크 ================================");
		if (billingDayData != null && openRtbDayData != null) {

			log.info("billingDayData : " + billingDayData.toString());
			log.info("openRtbDayData : " + openRtbDayData.toString());

			long billingData = Long.parseLong(billingDayData.get("TOT_EPRS_CNT").toString());
			long openRtbData = Long.parseLong(openRtbDayData.get("TOT_EPRS_CNT").toString());

			if (billingData != openRtbData) {
				log.info("일자별 데이터 작업 시작!!");

				try {
					log.info("1.MOB_CAMP_MEDIA_STATS 작업 시작!!");
					long startTime = System.currentTimeMillis();
					int count = billingMapper.del_MOB_CAMP_MEDIA_STATS(param);
					long endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_CAMP_MEDIA_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("2.MOB_CAMP_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_CAMP_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_CAMP_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("3.MOB_ADVER_MEDIA_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_ADVER_MEDIA_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_ADVER_MEDIA_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("4.MOB_ADVER_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_ADVER_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_ADVER_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("5.MOB_MEDIA_SCRIPT_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_MEDIA_SCRIPT_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_MEDIA_SCRIPT_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("6.MOB_MEDIA_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_MEDIA_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_MEDIA_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("7.MOB_COM_STATS_INFO 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_COM_STATS_INFO(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_COM_STATS_INFO(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("8.MOB_MEDIA_SCRIPT_CHRG_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_MEDIA_SCRIPT_CHRG_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_MEDIA_SCRIPT_CHRG_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("9.MOB_MEDIA_CHRG_STATS 작업 시작!!");
					startTime = System.currentTimeMillis();
					count = billingMapper.del_MOB_MEDIA_CHRG_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 삭제 - 작업시간 : " + (endTime - startTime) + "ms");
					startTime = System.currentTimeMillis();
					count = billingMapper.insert_MOB_MEDIA_CHRG_STATS(param);
					endTime = System.currentTimeMillis();
					log.info(count + "건 등록 - 작업시간 : " + (endTime - startTime) + "ms");

					log.info("일자별 데이터 작업 완료!!");
				} catch (Exception e) {
					log.error("일자별 작업 에러!!", e);
				}

			} else {
				log.error(billingDayData.get("STATS_DTTM") + " 데이터 이상 없음!!");
			}

		} else {
			log.error("Day Data error!!");
		}

		log.info("============================== 일자별 체크 완료 ==============================");
	}
}
