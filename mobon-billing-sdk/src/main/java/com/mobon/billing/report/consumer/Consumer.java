package com.mobon.billing.report.consumer;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.disk.Disk;
import com.mobon.billing.report.disk.json.AppInfo;
import com.mobon.billing.report.disk.json.ConvertAppInfotoJson;

//@Component
public class Consumer{
	private static final Logger	logger	= LoggerFactory.getLogger(Consumer.class);

	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	private Disk disk;
	
	@KafkaListener(topics = "#{T(org.springframework.util.CollectionUtils).arrayToList('${KAFKA_SERVICE_TOPIC}'.split(','))}")
	public void listener(
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions
			, @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics
			, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys
			, @Header(KafkaHeaders.OFFSET) List<Long> offsets,
			List<String> list) {
		

		ConvertAppInfotoJson convert = new ConvertAppInfotoJson();
		
//		Disk disk = new Disk();
		logger.info("listener all polling list size - {}", list.size());
		for (int i = 0; i < list.size(); i++) {
			String topic = topics.get(i); 
			Integer partition = partitions.get(i); 
			Long offset = offsets.get(i); 
			String message = list.get(i);  
			String key = keys.get(i); 

			//			logger.info(";;; " + JSONObject.fromObject(message).toString());
//			convert.appInfo(disk, new AppInfo(JSONObject.fromObject(message.replaceAll("&quot;", ""))));
//			System.out.println(key);
			
			if(CacheConfig.getCacheCount().get("space") != null) {
				boolean isWrite = Boolean.valueOf(CacheConfig.getCacheCount().get("space").getObjectValue().toString());
				logger.info("isWrite : " + isWrite);
				if(isWrite) {
					convert.appInfo(disk, new AppInfo(JSONObject.fromObject(message.replaceAll("&quot;", ""))));
				} else {
					logger.info("disk full.....");
				}
			} else {
				logger.info("CacheConfig.getCacheCount().get(space) == null");
			}
			
//			System.out.println(":: " + packageDao.selectNOW());
		}
	}
}



