package org.mobon.billing.HHtoDD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mobon.billing.hhtodd.service.HHtoDDMariaDB;

@SpringBootApplication
@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = "com.mobon.billing")
@EnableScheduling
public class AppHHtoDD implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(AppHHtoDD.class);
	
	public static void main(String[] args) {
		logger.debug("AppHHtoDD");
		new SpringApplicationBuilder().sources(AppHHtoDD.class).run(args);
	}
	
	
	@Autowired
	private HHtoDDMariaDB			hHtoDDMariaDB;

	@Override
	public void run(String... strings) throws Exception {
		logger.info("start ");
	}
}
