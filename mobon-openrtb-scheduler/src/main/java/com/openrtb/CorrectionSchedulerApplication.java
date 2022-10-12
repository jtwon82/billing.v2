package com.openrtb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CorrectionSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorrectionSchedulerApplication.class, args);
	}

}
