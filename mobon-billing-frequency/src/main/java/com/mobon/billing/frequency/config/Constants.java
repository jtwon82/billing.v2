package com.mobon.billing.frequency.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class Constants {

	public static enum freqType { CONV, CLICK_VIEW };
	
	public static final String LOGGING_LIVE = "liveTraceLog";
	
}
