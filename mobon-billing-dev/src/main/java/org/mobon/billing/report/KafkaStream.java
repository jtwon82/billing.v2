package org.mobon.billing.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.mobon.billing.model.PollingData;

import scala.Tuple2;

//@Component
public class KafkaStream {

//	@Resource(name="sqlSessionTemplatePostgres")
//	private SqlSessionTemplate	sqlSessionTemplatePostgres;
	
	public static void main(String[] args)  {
		try {
			KafkaStream.proc4();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void proc4() throws InterruptedException {
        // Create a local StreamingContext and batch interval of 10 second
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Kafka Spark Integration");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(3));
 
        //Define a list of Kafka topic to subscribe
        Collection<String> topics = Arrays.asList("test");
        
		//Here we create a direct stream for kafka input data.
		final JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(jssc,
		        LocationStrategies.PreferConsistent(),
		        ConsumerStrategies.<String, String>Subscribe(topics, getParam()));


		JavaPairDStream<String, String> results = messages
		        .mapToPair(new PairFunction<ConsumerRecord<String, String>, String, String>() {
		            @Override
		            public Tuple2<String, String> call(ConsumerRecord<String, String> record) {
		                return new Tuple2<>(record.key(), record.value());
		            }
		        });

		JavaDStream<String> lines = results.map(new Function<Tuple2<String, String>, String>() {
		    @Override
		    public String call(Tuple2<String, String> tuple2) {
		        return tuple2._2();
		    }
		});

		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
		    @Override
		    public Iterator<String> call(String x) {
		        return Arrays.asList(" ".split(x)).iterator();
		    }
		});

		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
		    @Override
		    public Tuple2<String, Integer> call(String s) {
		        return new Tuple2<>(s, 1);
		    }
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
		    @Override
		    public Integer call(Integer i1, Integer i2) {
		        return i1 + i2;
		    }
		});

		//Here we iterrate over the JavaPairDStream to write words and their count into kafka
		wordCounts.foreachRDD(new VoidFunction<JavaPairRDD<String, Integer>>() {
		    @Override
		    public void call(JavaPairRDD<String, Integer> arg0) throws Exception {
		    	System.out.println(arg0);
		        Map<String, Integer> wordCountMap = arg0.collectAsMap();
		        List<String> topicList= new ArrayList<>();
		        for (String key : wordCountMap.keySet()) {
		             //Here we send event to kafka output topic
//		             publishToKafka(key, wordCountMap.get(key), outputTopic);
		        }
		        JavaRDD<String> WordOccurenceRDD = jssc.sparkContext().parallelize(topicList);
//		        CassandraJavaUtil.javaFunctions(WordOccurenceRDD)
//		                .writerBuilder(keyspace, table, CassandraJavaUtil.mapToRow(PollingData.class))
//		                .saveToCassandra();
		    }
		});

		jssc.start();
		jssc.awaitTermination();
	}
	public static void proc3() throws InterruptedException{
		
        // Create a local StreamingContext and batch interval of 10 second
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Kafka Spark Integration");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(3));
 
        //Define a list of Kafka topic to subscribe
        Collection<String> topics = Arrays.asList("test");
 
        //Create an input Dstream which consume message from Kafka topics
        JavaInputDStream<ConsumerRecord<String, String>> stream= KafkaUtils.createDirectStream(jssc,LocationStrategies.PreferConsistent(),ConsumerStrategies.Subscribe(topics, getParam()));
 
        // Read value of each message from Kafka
        JavaDStream<String> lines = stream.map((Function<ConsumerRecord<String, String>, String>) kafkaRecord -> kafkaRecord.value());
 
        // Split message into words
        JavaDStream<String> words = lines.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line).iterator());
 
        // Take every word and return Tuple with (word,1)
        JavaPairDStream<String,Integer> wordMap = words.mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word,1));
 
        // Count occurance of each word
        JavaPairDStream<String,Integer> wordCount = wordMap.reduceByKey((Function2<Integer, Integer, Integer>) (first, second) -> first+second);
 
        //Print the word count
        wordCount.print();
 
        // Start the computation
        jssc.start();
        jssc.awaitTermination();
	}

	public static void proc1() throws InterruptedException{
		System.setProperty("hadoop.home.dir", "C:\\workset\\bin\\hadoop-3.2.2"); // HADOOP_HOME 경로 잡아주기

		SparkConf conf = new SparkConf().setAppName("kafka-spark").setMaster("local[2]")
				.set("spark.driver.allowMultipleContexts", "true");

		Collection<String> topics = Arrays.asList("test"); // ""안에 kafka topic명 입력

		JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(5000));
		
		JavaInputDStream<ConsumerRecord<String, String>> stream= KafkaUtils.createDirectStream(
				ssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, getParam()));

		stream.mapToPair(new PairFunction<ConsumerRecord<String, String>, String, String>() {
			public Tuple2<String, String> call(ConsumerRecord<String, String> record) {
				return new Tuple2<String, String>(record.key(), record.value() );
			}
		});//.print();
		stream.map(raw -> raw.value()).print();

		ssc.start();
		ssc.awaitTermination();

	}
	public static Map getParam() {
		Map<String, Object> kafkaParams = new HashMap<String, Object>();
		kafkaParams.put("bootstrap.servers", "localhost:9092");
		kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaParams.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaParams.put("group.id", "spark_id");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", true);
		return kafkaParams;
	}

}
