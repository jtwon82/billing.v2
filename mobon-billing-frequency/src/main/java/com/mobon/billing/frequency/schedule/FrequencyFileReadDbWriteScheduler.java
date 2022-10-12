package com.mobon.billing.frequency.schedule;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mobon.billing.frequency.service.FileReadDbWriteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
@ConditionalOnProperty(	prefix = "spring.scheduler.cron.freq-file-read-db-write-batch"
, name = "use-yn"
, havingValue= "Y"
, matchIfMissing = true )
public class FrequencyFileReadDbWriteScheduler {
	
	
	@Resource( name="FileReadDbWrite" )
	FileReadDbWriteService fileReadDbWriteService;
	
	@Scheduled( cron="${spring.scheduler.cron.freq-file-read-db-write-batch.cron-cycle}")
	public void scheduler() {
		
		try {
			
			fileReadDbWriteService.execute();
			
		} catch (Exception e) {
			log.info("[FrequencyFileReadDbWriteScheduler] scheduler() Error: ",e);
		}
		
		
	}
	
}
