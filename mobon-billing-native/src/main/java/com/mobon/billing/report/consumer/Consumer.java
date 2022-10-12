package com.mobon.billing.report.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


@Component
public class Consumer{

	private static final Logger	logger	= LoggerFactory.getLogger(Consumer.class);

	@Autowired
	private ConsumerProcess		consumerProcess;

	@Value("${log.path}")
	private String	logPath;

	
	@KafkaListener(topics = "#{T(org.springframework.util.CollectionUtils).arrayToList('${KAFKA_SERVICE_TOPIC}'.split(','))}")
	public void listener(
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions
			, @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics
			, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys
			, @Header(KafkaHeaders.OFFSET) List<Long> offsets,
			List<String> list) {

		logger.debug("listener all polling list size - {}", list.size());
		for (int i = 0; i < list.size(); i++) {
			String topic = topics.get(i);
			Integer partition = partitions.get(i);
			Long offset = offsets.get(i);
			String message = list.get(i);
			String key = keys.get(i);

			consumerProcess.processMain(topic, message);
		}
	}
	
}
