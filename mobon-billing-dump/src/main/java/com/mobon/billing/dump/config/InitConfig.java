package com.mobon.billing.dump.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobon.billing.dump.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitConfig {
	
	@Value("${MOBON_RETRY_DIR}")
	private String MOBONRetryPath;
	
	@Value("${MOBON_RETRY_SUCC_DIR}")
	private String MOBONRetrySuccPath;
	
	
	@PostConstruct
    public void makeRetryDir() {
		
		FileUtils.makeDir(MOBONRetryPath);
		
		FileUtils.makeDir(MOBONRetrySuccPath);
		
    }


}
