package com.mobon.billing.branchUM.handler;

import com.mobon.billing.branchUM.handler.KafkaErrorHandler;
import java.util.Iterator;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.BatchErrorHandler;

public class KafkaErrorHandler implements BatchErrorHandler {
	private static final Logger logger = LoggerFactory.getLogger(KafkaErrorHandler.class);

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	public void handle(Exception thrownException, ConsumerRecords<?, ?> datas) {
		logger.debug("KafkaErrorHandler msg - {}", thrownException);

		try {
			this.kafkaListenerEndpointRegistry.start();

			Iterator it = datas.iterator();
			while (it.hasNext()) {
				JSONObject jSONObject = JSONObject.fromObject(it.next());
				logger.error("it - {}", jSONObject);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		logger.info("KafkaErrorHandler ended");
	}
}
