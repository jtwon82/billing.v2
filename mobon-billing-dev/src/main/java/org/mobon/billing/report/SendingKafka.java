package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.MAP;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import net.sf.json.JSONObject;

public class SendingKafka {
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		SendingKafka.proc2();
	}

	public static void proc1() throws InterruptedException, ExecutionException, IOException {

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
		
		while(true) {
			
			String []ar = new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
			String msg= ar[Math.abs(new Random().nextInt()%20)]+"_"+configs.toString();
//			Map map= new HashMap();
//			map.put("msg", configs.toString());
			
			producer.send(new ProducerRecord<>("springboot-kafka-elk", msg), (m, e) -> {
				if (m != null) {
				} else {
					e.printStackTrace();
				}
			});
			TimeUnit.MICROSECONDS.sleep(5000);
		}
	}
	
	public static void proc2() throws InterruptedException, ExecutionException, IOException {

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
		
		BufferedReader fr = new BufferedReader(new FileReader(new File( "C:\\workset\\1sender\\kafka-consumer.logging.log.2021-03-29_05" )));
		String lineData;
		while ((lineData = fr.readLine()) != null) {

			String [] row = lineData.split("\t");
			String topic = row[1];
			String lineDataSub = row[2];
			
//			String []ar = new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
//			String msg= ar[Math.abs(new Random().nextInt()%20)]+"_"+configs.toString();
//			Map map= new HashMap();
//			map.put("msg", configs.toString());
			
			producer.send(new ProducerRecord<>("springboot-kafka-elk", JSONObject.fromObject(lineDataSub).toString()), (m, e) -> {
				if (m != null) {
					// System.out.print(String.format("%s, %d, %d", m.topic(), m.offset(),
					// m.partition()));
				} else {
					e.printStackTrace();
				}
			});
			// TimeUnit.SECONDS.sleep(2);
			TimeUnit.MICROSECONDS.sleep(5000);
		}
	}

}
