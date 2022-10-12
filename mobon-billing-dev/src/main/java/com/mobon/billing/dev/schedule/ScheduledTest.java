package com.mobon.billing.dev.schedule;

import java.util.Random;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mobon.billing.dev.model.PollingData;
import com.mobon.billing.dev.service.SumObjectManager;

@Component
public class ScheduledTest {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTest.class);

	@Resource(name="sqlSessionTemplatePostgres")
	private SqlSessionTemplate	sqlSessionTemplatePostgres;
	
	@Autowired
	SumObjectManager sumObjectManager;

	@Autowired
	StringRedisTemplate redisTemplate;
	
//	@Scheduled(fixedRate = 3000)
	public void redisProc1() {
		String key = "sabarada";
		
		ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
		stringStringValueOperations.set(key, "1"); // redis set 명령어
		
		String result_1 = stringStringValueOperations.get(key); // redis get 명령어
		System.out.println("result_1 = " + result_1);
		
		stringStringValueOperations.increment(key); // redis incr 명령어
		String result_2 = stringStringValueOperations.get(key);
		System.out.println("result_2 = " + result_2);
	}
	
	@Scheduled(fixedRate = 100)
	@Async
	public void proc1() {
		for(int i=0; i< Math.abs(new Random().nextInt())%100000; i++) {
			PollingData a= new PollingData();
			sumObjectManager.appendPollingData(a);
		}
	}
}
