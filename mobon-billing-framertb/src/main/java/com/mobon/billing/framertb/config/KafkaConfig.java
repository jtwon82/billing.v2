package com.mobon.billing.framertb.config;

import java.util.HashMap;
import java.util.Map;

import com.mobon.billing.framertb.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import org.springframework.kafka.listener.ContainerProperties;

/**
 * KafkaConfig
 * Kafka Consumer 설정 클래스
 *
 * @author  : sjpark3
 * @since   : 2022-4-20
 */
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
	@Value("${KAFKA_SERVICE_TIMEOUT_MS}") String TIMEOUT_MS;
	@Value("${KAFKA_SERVICE_POLL_RECORDS}") String POLL_RECORDS;
	@Value("${KAFKA_SERVICE_FETCH_MIN_BYTES}") String FETCH_MIN_BYTES;
	@Value("${KAFKA_SERVICE_Concurrency}") int Concurrency;

	/**
	 * consumerConfigs
	 * Kafka Consumer 설정 프로퍼티 생성 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-4-20
	 */
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();

		/**
		 * ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG : 호스팅 IP
		 * ConsumerConfig.GROUP_ID_CONFIG : 컨슈머 그룹 ID
		 * ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG : deserializer key
		 * ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG : deserializer value
		 * ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG : 오토커밋 사용/미사용 (true/false)
		 * ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG : 세션 타입아웃 리미트
		 * ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG : 하트비트 송신 주기
		 * ConsumerConfig.MAX_POLL_RECORDS_CONFIG : poll 수행시 가져올 레코드 길이
		 * ConsumerConfig.FETCH_MIN_BYTES_CONFIG : 서버에서 패치될 최소 바이트 수
		 */

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, TIMEOUT_MS);
		props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, Integer.parseInt(TIMEOUT_MS)/4);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, POLL_RECORDS);
		props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, FETCH_MIN_BYTES);
		
		logger.debug("props - {}", props);
		return props;
	}

	/**
	 * consumerFactory
	 * Kafka Consumer 생성 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-4-20
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	/**
	 * kafkaListenerContainerFactory
	 * Kafka Factory 생성 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-4-20
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

		/**
		 * setConsumerFactory : 컨슈머 생성 팩토리 지정
		 * setConcurrency : 컨슈머 개수 지정
		 * setBatchListener : 리스너 배치 사용/미사용 지정 (true/false)
		 * setMessageListener : 리스터 객체 지정
		 * setPollTimeout : poll 타임 리미트 지정
		 * setAckMode : 오프셋 커밋 모드 지정
		 */

		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(Concurrency);
		factory.setBatchListener(true); // batch 여부
		factory.getContainerProperties().setMessageListener(new Consumer());
		factory.getContainerProperties().setPollTimeout(100);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		
		return factory;
	}

}
