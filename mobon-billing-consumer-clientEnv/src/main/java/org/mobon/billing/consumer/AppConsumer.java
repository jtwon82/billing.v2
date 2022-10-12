package org.mobon.billing.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = {"com.mobon.billing.base" , "com.mobon.billing.env"
		, "com.mobon.billing.core", "com.mobon.billing.branch", "com.mobon.billing.pargatr"})

public class AppConsumer {
	private static final Logger logger = LoggerFactory.getLogger(AppConsumer.class);

	public static void main(String[] args) {
		logger.debug("AppConsumer");
		new SpringApplicationBuilder().sources(AppConsumer.class).run(args);
	}
}
