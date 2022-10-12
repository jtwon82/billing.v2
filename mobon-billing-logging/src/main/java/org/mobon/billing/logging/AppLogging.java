package org.mobon.billing.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml" })
@ComponentScan(basePackages = "com.mobon.billing")

public class AppLogging {
	private static final Logger logger = LoggerFactory.getLogger(AppLogging.class);

	public static void main(String[] args) {
		logger.debug("AppLogging");
		new SpringApplicationBuilder().sources(AppLogging.class).run(args);
	}
}
