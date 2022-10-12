package org.mobon.billing.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.adgather.util.old.DateUtils;
import com.google.gson.Gson;

public class KafkaSend {
	private static final String KAFKA_PARAM_BOOTSTRAP_SERVERS = "bootstrap.servers";
	private static final String KAFKA_PARAM_KEY_SERIALIZER = "key.serializer";
	private static final String KAFKA_PARAM_VALUE_SERIALIZER = "value.serializer";
	private static final String KAFKA_PARAM_ACKS = "acks";
	private static final String KAFKA_PARAM_BUFFER_MEMORY = "buffer.memory";
	private static final String KAFKA_PARAM_COMPRESSION_TYPE = "compression.type";
	private static final String KAFKA_PARAM_RETRIES = "retries";
	private static final String KAFKA_PARAM_BATCH_SIZE = "batch.size";
	private static final String KAFKA_PARAM_LINGER_MS = "linger.ms";
	private static final String KAFKA_PARAM_MAX_REQUEST_SIZE = "max.request.size";
	private static final String KAFKA_PARAM_enable_idempotence = "enable.idempotence";

	private static final String KAFKA_PARAM_MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = "max.in.flight.requests.per.connection";
	private static final String KAFKA_SERVICE_min_insync_replicas = "min.insync.replicas";
	private static final String KAFKA_SERVICE_request_timeout_ms = "request.timeout.ms";
	private static final String KAFKA_SERVICE_max_block_ms = "max.block.ms";
	
	public static final void main(String[] ar) throws Exception{
		
//		File f = new File(ar[0]);
//		String brokerIp = ar[1];
//		if("".equals(brokerIp))brokerIp="172.20.0.106:9092";
		File f = new File("C:\\1\\20191121\\kafka-consumer.logging.log.topic.1118.04.log");
		String brokerIp = "localhost:9092";
		
		BufferedReader fr = null;
		try {
			Properties props = new Properties();
			props.put(KAFKA_PARAM_BOOTSTRAP_SERVERS, brokerIp);
			props.put(KAFKA_PARAM_ACKS, "all");
			props.put(KAFKA_PARAM_BUFFER_MEMORY, "10485760"); // 10485760 33554432
			props.put(KAFKA_PARAM_COMPRESSION_TYPE, "gzip"); // gzip, lz4
			props.put(KAFKA_PARAM_RETRIES, "5");
			props.put(KAFKA_PARAM_BATCH_SIZE, "8192"); // 8192 16384 24576 32768 49152 65536
			props.put(KAFKA_PARAM_LINGER_MS, "100");
			props.put(KAFKA_PARAM_MAX_REQUEST_SIZE, "1048576");
			props.put(KAFKA_PARAM_enable_idempotence, "true");
			props.put(KAFKA_SERVICE_request_timeout_ms, "1000");
			props.put(KAFKA_SERVICE_max_block_ms, "400");
			props.put(KAFKA_PARAM_KEY_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");
			props.put(KAFKA_PARAM_VALUE_SERIALIZER, "org.apache.kafka.common.serialization.StringSerializer");
			
			KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
			if (f.isFile()) {
				
				fr = new BufferedReader(new FileReader(f));
				String line;
				int rowCnt=0;
				while ((line = fr.readLine()) != null) {
					String[] lines = line.split("\t");
					String topic = lines[1];
					final String msg = lines[2];
					
//					if("ClickViewData".equals(topic)) {
//						try {
//							Map map = new Gson().fromJson(msg, Map.class);
//							
//							double point = map.get("point")!=null ? (double) map.get("point") : 0;
//							double mpoint = map.get("mpoint")!=null ? (double) map.get("mpoint") : 0;
//							if ( point>0 || mpoint>0 ) {
//								topic = "ClickViewPointData";
//							}
//						}catch(Exception e) {
//						}
//					}
					
					producer.send(new ProducerRecord<>(topic, msg));
					
					if(++rowCnt % 100000==0) {
						System.out.println( DateUtils.getDate("yyyy-MM-dd HH:mm:ss") +" SENDING rowCnt "+ rowCnt );
					}
				}
			}
			producer.flush();
			producer.close();
		} catch (Exception e) {
			System.out.println(String.format("err - %s", e.getMessage()));
		}
	}
}
