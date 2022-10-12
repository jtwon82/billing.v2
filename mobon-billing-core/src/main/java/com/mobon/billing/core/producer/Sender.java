package com.mobon.billing.core.producer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
	private static final Logger logger = LoggerFactory.getLogger(Sender.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String topic, String payload) {
		kafkaTemplate.send(topic, UUID.randomUUID().toString(), payload);
	}
}
