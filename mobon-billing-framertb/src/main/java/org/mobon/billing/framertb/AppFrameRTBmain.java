package org.mobon.billing.framertb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = "com.mobon.billing")

public class AppFrameRTBmain {
	private static final Logger logger = LoggerFactory.getLogger(AppFrameRTBmain.class);

	public static void main(String[] args) {
		logger.debug("App");
		new SpringApplicationBuilder().sources(AppFrameRTBmain.class).run(args);
	}
	
}
