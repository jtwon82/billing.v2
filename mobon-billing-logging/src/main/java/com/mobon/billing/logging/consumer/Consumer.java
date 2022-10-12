package com.mobon.billing.logging.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * Consumer
 * Kafka Listener 클래스
 *
 * POLL > COMMIT > PROCESSING 의 순서로 진행되며
 * consumerProcess 는 POLL 해온 데이터를 반드시 다 처리하여야함
 *
 * @author  : sjpark3
 * @since   : 2022-4-20
 */
@Component
public class Consumer implements BatchAcknowledgingMessageListener<String, String> {

	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
	private ConsumerCollector collector;

	@Value("${log.path}")
	private String logPath;
	@Value("${log.path.shop}")
	private String logPathShop;
	@Value("${profile.id}")
	private String profileId;

	@Override
	@KafkaListener(topics = "#{T(org.springframework.util.CollectionUtils).arrayToList('${KAFKA_SERVICE_TOPIC}'.split(','))}")
	public void onMessage(List<ConsumerRecord<String, String>> data, Acknowledgment acknowledgment) {
		// commit
		try {
			acknowledgment.acknowledge();
		} catch (Exception e) {
			logger.error("listener offset commit error - {}", e.toString());
		}

		logger.debug("listener all polling list size - {}", data.size());

		// process
		for (ConsumerRecord<String, String> row : data) {
			String topic = row.topic();
			String message = row.value();
			String className = "";

			JSONObject jSONObject = JSONObject.fromObject(message);
			className = (String) jSONObject.get("className");
			
			/////////////////////////////////////////////////////
			//토픽별로 로그파일 쌓는곳 
			//collector.makeTopicLogFile(topic, jSONObject);
			////////////////////////////////////////////////////

			/////////////////////////////////////////////////////
			// clickview 로그파일  쌓는곳
			////////////////////////////////////////////////////
			collector.clickview(topic, jSONObject);
//			collector.clickviewpoint(topic, jSONObject);
//			collector.point(topic, message);
			
			/////////////////////////////////////////////////////
			// advertiser 로그파일  쌓는곳
			////////////////////////////////////////////////////
			// 2021-11-22 advertiser 로그파일 쌓기 중지 - 박상재
			// collector.advertiser(topic, jSONObject);

			/////////////////////////////////////////////////////
			// 상품 로그쌓기 ("서버 부하로 인한 잠시 멈춤")
			////////////////////////////////////////////////////
			collector.shop(topic, jSONObject);

			/////////////////////////////////////////////////////
			// 컨버전 로그쌓기 
			////////////////////////////////////////////////////
			collector.conv(topic, jSONObject);

			/////////////////////////////////////////////////////
			// OPENRTB로그쌓기 
			////////////////////////////////////////////////////
//			collector.openRtbViewClickConvData(topic, jSONObject);
			
			/////////////////////////////////////////////////////
			// 도메인 로그쌓기 
			////////////////////////////////////////////////////
//			collector.rfdata(topic, jSONObject);
			
			
			/////////////////////////////////////////////////////
			// 장바구니 로그쌓기 
			////////////////////////////////////////////////////
//			collector.cart(topic, jSONObject);
			
			/////////////////////////////////////////////////////
			// 프리컨시 로그쌓기 
			////////////////////////////////////////////////////
			//collector.frequencyLog(topic, className, jSONObject);
			
			//////////////////////////////////////////////////////
			// 필요에 의한 로그파일 
//			collector.adDabagirl(topic, jSONObject);
//			collector.adUserLog(topic, jSONObject);
//			collector.adScriptNo(topic, jSONObject);
			
//			collector.adExternal(topic, jSONObject);
//			collector.adConversion(topic, jSONObject);
//			collector.adShopData(topic, jSONObject);;
			
			collector.frequencyLogClickView(topic, className, jSONObject);
			//알고리즘 ViewData 로그 파일 ("서버 부하로 인한 잠시 멈춤")
			//collector.algoLogViewData(topic, jSONObject);

			//성공 전환 로그 파일
			collector.convSuccData(topic, jSONObject);

			//반송률 로그 파일
			//collector.bounceRateData(topic, jSONObject);

			//장바구니 로그파일
			//collector.basketData(topic, jSONObject);

			//인사이트 클릭뷰
			//collector.insiteClickView(topic, jSONObject);

			// 인사이트 전환
			//collector.insiteConversion(topic, jSONObject);

			// uniid 로그 파일
			//collector.uniidData(topic, jSONObject);

		}
	}

}
