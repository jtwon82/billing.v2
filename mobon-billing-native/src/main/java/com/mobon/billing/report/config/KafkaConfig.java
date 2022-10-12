package com.mobon.billing.report.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@PropertySource("classpath:kafka.properties")
@EnableKafka
public class KafkaConfig {
	
	private static final Logger	logger	= LoggerFactory.getLogger(KafkaConfig.class);

	@Autowired
	ApplicationArguments		appArgs;
	
	@Value("${application.service}") String APPLICATION_SERVICE;
	
	@Value("${KAFKA_SERVICE}") String KAFKA_SERVICE;
	@Value("${KAFKA_SERVICE_HOST}") String HOST;
	@Value("${KAFKA_SERVICE_GROUP_ID}") String GROUP_ID;
	@Value("${KAFKA_SERVICE_AUTO_COMMIT}") String AUTO_COMMIT;
	@Value("${KAFKA_SERVICE_INTERVAL_MS}") String INTERVAL_MS;
	@Value("${KAFKA_SERVICE_TIMEOUT_MS}") String TIMEOUT_MS;
	@Value("${KAFKA_SERVICE_POLL_RECORDS}") String POLL_RECORDS;
	@Value("${KAFKA_SERVICE_FETCH_MIN_BYTES}") String FETCH_MIN_BYTES;
	@Value("${KAFKA_SERVICE_Concurrency}") int Concurrency;
	
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put("bootstrap.servers", HOST);
		props.put("group.id", GROUP_ID);
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		props.put("enable.auto.commit", AUTO_COMMIT);
		props.put("auto.commit.interval.ms", INTERVAL_MS);
		props.put("session.timeout.ms", TIMEOUT_MS);
		props.put("heartbeat.interval.ms", Integer.parseInt(TIMEOUT_MS)/4);
//		props.put("max.poll.interval.ms", 10);
		props.put("max.poll.records", POLL_RECORDS);
		props.put("fetch.min.bytes", FETCH_MIN_BYTES);		//서버에서 패치될 최소 바이트 수
		
		logger.debug("props - {}", props);
		return props;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(Concurrency);
		factory.setBatchListener(true); // batch 여부
		factory.getContainerProperties().setPollTimeout(100);
		
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

}
