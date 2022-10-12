package com.javasampleapproach.springbatch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	@RequestMapping("/launchjob")
	public String handle() throws Exception {

		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()+10000)
					.toJobParameters();
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return "Done";
	}
	
	@Scheduled(cron = "*/5 * * * * *")
	public void sc() {
		logger.info("@Scheduled(cron = \"*/5 * * * * *\")");
		
		try {
			handle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}