package com.mobon.billing.sample.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Consumer
 * Kafka Listener 클래스
 * 
 * POLL > COMMIT > PROCESSING 의 순서로 진행되며
 * consumerProcess 는 POLL 해온 데이터를 반드시 다 처리하여야함
 * 
 * @author  : sjpark3
 * @since   : 2021-1-20
 */
@Component
public class Consumer implements BatchAcknowledgingMessageListener<String, String> {

	@Autowired
	private ConsumerProcess	consumerProcess;

	@Override
	@KafkaListener(topics = "#{T(org.springframework.util.CollectionUtils).arrayToList('${KAFKA_SERVICE_TOPIC}'.split(','))}")
	public void onMessage(List<ConsumerRecord<String, String>> data, Acknowledgment acknowledgment) {
		try {
			// commit
			acknowledgment.acknowledge();

			for (ConsumerRecord<String, String> row : data) {
				String topic = row.topic();
				String message = row.value();

				consumerProcess.processMain(topic, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}