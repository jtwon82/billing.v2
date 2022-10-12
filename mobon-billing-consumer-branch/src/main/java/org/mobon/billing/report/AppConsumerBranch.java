package org.mobon.billing.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = {"com.mobon.billing.branch"
		, "com.mobon.billing.core", "com.mobon.billing.pargatr", "com.mobon.billing.env"})

public class AppConsumerBranch {
	private static final Logger logger = LoggerFactory.getLogger(AppConsumerBranch.class);

	public static void main(String[] args) {
		logger.debug("AppConsumerBranch");
		new SpringApplicationBuilder().sources(AppConsumerBranch.class).run(args);
	}
	
}
