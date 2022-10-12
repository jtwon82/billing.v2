package com.mobon.billing.branch.consumer;

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

/**
 * ConsumerBranch
 * Kafka Listener 클래스
 * 
 * POLL > COMMIT > PROCESSING 의 순서로 진행되며
 * consumerProcess 는 POLL 해온 데이터를 반드시 다 처리하여야함
 * 
 * @author  : sjpark3
 * @since   : 2022-1-20
 */
@Component
public class ConsumerBranch implements BatchAcknowledgingMessageListener<String, String> {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerBranch.class);

	@Autowired
	private ConsumerProcessBranch consumerProcess;

	@Value("${log.path}")
	private String logPath;

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

			consumerProcess.processMain(topic, message);
		}
	}
	
}
