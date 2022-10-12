package org.mobon.billling.startpoint;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SendingKafka {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Properties configs = new Properties();
		configs.put("bootstrap.servers", "localhost:9092");
		configs.put("acks", "0");
		// configs.put("zookeeper.session.timeout.ms", "4000");
		// configs.put("zookeeper.connectiontimeout.ms", "4000");
		// configs.put("zookeeper.sync.time.ms", "200");
		configs.put("auto.commit.interval.ms", "1000");
//		configs.put("block.on.buffer.full", "true");
		configs.put("compression.type", "lz4");
		configs.put("batch.size", "1000"); // 튜닝포인트
		configs.put("linger.ms", "10"); // 배치 딜레이 타임(튜닝포인트)
		configs.put("buffer.memory", "500000"); // 커넉션 버퍼 사이즈(튜닝포인트)
		configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer= new KafkaProducer<>(configs);
		while (true) {
			String []ar = new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
			String msg= ar[Math.abs(new Random().nextInt()%20)]+"_"+configs.toString();
			
			producer.send(new ProducerRecord<>("test", msg), (m, e) -> {
				if (m != null) {
					// System.out.print(String.format("%s, %d, %d", m.topic(), m.offset(),
					// m.partition()));
				} else {
					e.printStackTrace();
				}
			});
			// TimeUnit.SECONDS.sleep(2);
			TimeUnit.MICROSECONDS.sleep(100);
		}
	}

}
