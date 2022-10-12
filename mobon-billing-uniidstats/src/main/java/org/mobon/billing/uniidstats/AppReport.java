package org.mobon.billing.uniidstats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = "com.mobon.billing.uniidstats")

public class AppReport {
	private static final Logger logger = LoggerFactory.getLogger(AppReport.class);

	public static void main(String[] args) {
		logger.debug("AppReport");
		new SpringApplicationBuilder().sources(AppReport.class).run(args);
	}
	
}
