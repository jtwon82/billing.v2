package com.mobon.billing.core.producer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@PropertySource("classpath:kafka.properties")
public class SenderConfig {

	private static final Logger logger = LoggerFactory.getLogger(SenderConfig.class);
	
	@Value("${KAFKA_SERVICE_HOST}")	private String KAFKA_SERVICE_HOST;
	@Value("${KAFKA_SERVICE_RETRIES}")	private String KAFKA_SERVICE_RETRIES;
	@Value("${KAFKA_SERVICE_BATCH_SIZE}")	private String KAFKA_SERVICE_BATCH_SIZE;
	@Value("${KAFKA_SERVICE_LINGER_MS}")	private String KAFKA_SERVICE_LINGER_MS;
	@Value("${KAFKA_SERVICE_BUFFER_MEMORY}")	private String KAFKA_SERVICE_BUFFER_MEMORY;
	@Value("${KAFKA_SERVICE_max_request_size}")	private String KAFKA_SERVICE_max_request_size;
	@Value("${KAFKA_SERVICE_enable_idempotence}")	private String KAFKA_SERVICE_enable_idempotence;
	@Value("${KAFKA_SERVICE_requests_per_connection}")	private String KAFKA_SERVICE_requests_per_connection;
	@Value("${KAFKA_SERVICE_min_insync_replicas}")	private String KAFKA_SERVICE_min_insync_replicas;
	@Value("${KAFKA_SERVICE_request_timeout_ms}")	private String KAFKA_SERVICE_request_timeout_ms;
	@Value("${KAFKA_SERVICE_max_block_ms}")	private String KAFKA_SERVICE_max_block_ms;

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		Map<String, Object> props = new HashMap<>();
		props.put("bootstrap.servers", KAFKA_SERVICE_HOST);
		props.put("retries", KAFKA_SERVICE_RETRIES);		// 재시도 횟수
		props.put("batch.size", KAFKA_SERVICE_BATCH_SIZE);	// 크기기반 일괄처리
		props.put("linger.ms", KAFKA_SERVICE_LINGER_MS);	// 시간기반 일괄처리
		props.put("buffer.memory", KAFKA_SERVICE_BUFFER_MEMORY); // 전송대기 버퍼
		props.put("max.request.size", KAFKA_SERVICE_max_request_size);	// 요청 최대크기
		props.put("min.insync.replicas", KAFKA_SERVICE_min_insync_replicas); // 최소 복제
		props.put("enable.idempotence", KAFKA_SERVICE_enable_idempotence); // 데이터 중복 개선.
		props.put("max.in.flight.requests.per.connection", KAFKA_SERVICE_requests_per_connection); // 수신 미확인 요청의 최대 수
		props.put("request.timeout.ms", KAFKA_SERVICE_request_timeout_ms);	// 전송후 request 타임아웃 시간 RETRY
		props.put("max.block.ms", KAFKA_SERVICE_max_block_ms);	// 전송후 응답시간
		props.put("compression.type", "gzip");
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		logger.debug("props - {}", props);
		
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
	}
}
