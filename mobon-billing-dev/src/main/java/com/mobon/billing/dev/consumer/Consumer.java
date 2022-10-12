package com.mobon.billing.dev.consumer;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dev.model.PollingData;
import com.mobon.billing.dev.service.SumObjectManager;

@Component
public class Consumer{

	private static final Logger	logger	= LoggerFactory.getLogger(Consumer.class);

	@Value("${log.path}")
	private String	logPath;

	@Resource(name="sqlSessionTemplatePostgres")
	private SqlSessionTemplate	sqlSessionTemplatePostgres;
	
	@Autowired
	SumObjectManager sumObjectManager;
	
	@KafkaListener(topicPattern = "test" )
	public void listener(List<String> list) {

		logger.debug("listener all polling list size - {}", list.size());
		
		try {
			for( String msg : list ) {
//				PollingData a= new Gson().fromJson(msg, PollingData.class);
				PollingData a= new ObjectMapper().readValue(msg, PollingData.class);
				sumObjectManager.appendPollingData(a);
			}
		}catch(Exception e) {
			logger.error("err", e);
		}
	}
}
