package com.mobon.billing.subjectCopy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:retry.properties")
@Component
public class RetryConfig {
@Value("${max.retry.count}") public int maxRetryCount;
	

	@Bean
	RetryTemplate retryTemplate() {

//		RetryTemplate template = new RetryTemplate();
//
//		TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
//		policy.setTimeout(30000L);
//
//		template.setRetryPolicy(policy);
//
//		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//		template.setBackOffPolicy(backOffPolicy);
		
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxRetryCount);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1500); // 1.5 seconds

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

		return template;
	}
}
