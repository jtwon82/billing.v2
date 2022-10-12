package com.mobon.billing.uniidstats.consumer;

import java.util.List;
import java.util.Map;

import com.mobon.billing.model.v20.PollingData;
import com.mobon.billing.model.v20.CommonVo;
import com.mobon.billing.uniidstats.service.dao.SelectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.uniidstats.model.UseridVo;
import com.mobon.billing.uniidstats.service.SumObjectManager;
import com.mobon.billing.uniidstats.util.StringUtils;

@Configuration
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

	public void processMain(String topic, String message) {
		PollingData obj = null;

		try {
			obj = new ObjectMapper().readValue(message, PollingData.class);

			// 현재 컨슈머가 해당 토픽 처리하는지 (방어로직)
			if (topics.contains(topic)) {
				UseridVo vo = UseridVo.create(obj);
				if (vo != null) {
					setAdvrtsInfo(vo);		// 추가 광고주 데이터 세팅
					setMediaInfo(vo);		// 비어있는 매체 데이터 세팅

					sumObjectManager.appendUseridVo(vo);
				}
			}

		} catch (Exception e) {
			logger.error("err ", e);
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
