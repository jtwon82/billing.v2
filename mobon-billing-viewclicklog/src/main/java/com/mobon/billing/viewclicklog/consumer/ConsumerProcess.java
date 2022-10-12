package com.mobon.billing.viewclicklog.consumer;

import java.util.List;
import java.util.Map;

import com.mobon.billing.model.v20.CommonVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.model.v20.PollingData;
import com.mobon.billing.model.v20.ViewClickVo;
import com.mobon.billing.viewclicklog.service.SumObjectManager;
import com.mobon.billing.viewclicklog.service.dao.SelectDao;
import com.mobon.billing.viewclicklog.util.StringUtils;

@Service
@PropertySource("classpath:kafka.properties")
public class ConsumerProcess {

	private static final Logger	logger = LoggerFactory.getLogger(ConsumerProcess.class);

	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	private SelectDao selectDao;

	@Value("#{'${KAFKA_SERVICE_TOPIC}'.trim().split('\\s*,\\s*')}")
	private List<String> topics;		// 현재 컨슈머가 처리하는 토픽 목록

	/**
	 * Topic 에 따른 분기처리 및 각 객체별 데이터 append
	 */
	public void processMain(String topic, String message) {
		PollingData obj = null;

		try {
			obj = new ObjectMapper().readValue(message, PollingData.class);

			// 현재 컨슈머가 해당 토픽 처리하는지 (방어로직)
			if (topics.contains(topic)) {
				if (G.ClickViewData.equals(topic) || G.OpenRTBViewData.equals(topic)
						|| G.OpenRTBClickData.equals(topic)) {
					// 해당 토픽에 대한 공통 로직 처리 후 객체 생성
					ViewClickVo vo = ViewClickVo.create(obj);

					if (vo != null) {
						// 각 컨슈머별 별도 로직 처리
						if ((!"03".equals(vo.getProductCode()) && vo.getClickcnt() > 0)
								|| ("08".equals(vo.getProductCode()) && (vo.getAvalCallTime() >= 1 || vo.getDbCnvrsCnt() >= 1)))
						{
							// 브랜딩ROAS 에서는 아이커버를 제외한 클릭 데이터는 적재하지 않음
							// 플러스콜의 유효콜 및 디비전환은 노출도 클릭도 아니므로 적재 X
							logger.debug("BrandingROAS is not processed click data");
						} else {
							setAdvrtsInfo(vo);		// 추가 광고주 데이터 세팅
							setMediaInfo(vo);		// 비어있는 매체 데이터 세팅

							sumObjectManager.appendViewClickVo(vo);
						}
					}

				} else if (G.ConversionData.equals(topic)) {
					ConversionVo vo = ConversionVo.create(obj);

					if (vo != null) {
						// 각 컨슈머별 별도 로직 처리
						// 브랜딩ROAS 에서는 CookieInDirect 를 무조건 30일로 세팅
						vo.setCookieInDirect(30);

						sumObjectManager.appendConversionVo(vo);
					}

				}
			}

		} catch (Exception e) {
			logger.error("processMain err - {} / object - {}", e, message);
		}
	}

	/**
	 * DB 에서 조회한 광고주 데이터를 이용한 set 작업
	 */
	private void setAdvrtsInfo(CommonVo obj) {
		try {
			Map<String, String> advrtsInfo = selectDao.selectAdvrtsInfo(obj.getSiteCode());

			if (advrtsInfo != null) {
				obj.setKpiNo(String.format("%s", advrtsInfo.get("kpiNo")));
				obj.setCtgrSeq(String.format("%s", advrtsInfo.get("ctgrSeq")));
			}
		} catch (Exception e) {
			logger.error("setAdvrtsInfo err - {} / object - {}", e, obj);
		}
	}

	/**
	 * DB 에서 조회한 매체 데이터를 이용한 set 작업
	 */
	private void setMediaInfo(CommonVo obj) {
		try {
			Map<String, String> mediaInfo = selectDao.selectMediaInfo(obj.getScriptNo());

			if (mediaInfo != null) {
				obj.setMediaTpCode(String.format("%s", mediaInfo.get("mediaTpCode")));
				obj.setInterlock(String.format("%s", mediaInfo.get("itlTpCode")));
				obj.setScriptUserId((StringUtils.isBlank(obj.getScriptUserId())) ?
						String.format("%s", mediaInfo.get("scriptUserId")) : obj.getScriptUserId());
				obj.setPlatform((StringUtils.isBlank(obj.getPlatform())) ?
						String.format("%s", mediaInfo.get("platform")) : obj.getPlatform());
			}
		} catch (Exception e) {
			logger.error("setMediaInfo err - {} / object - {}", e, obj);
		}
	}

}
