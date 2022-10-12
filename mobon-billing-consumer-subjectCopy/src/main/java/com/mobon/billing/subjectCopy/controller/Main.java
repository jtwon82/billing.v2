package com.mobon.billing.subjectCopy.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.adgather.constants.G;
import com.mobon.billing.subjectCopy.schedule.TaskClickConvMigration;
import com.mobon.billing.subjectCopy.schedule.TaskCronWork;


@Controller
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	@Autowired
	private TaskCronWork taskCronwork;
	
	@Value("${log.recovery}")
	private String recoveryLogPath;
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/consumerInfo", method = RequestMethod.GET)
	public @ResponseBody Map kafka(Model model) {
		Map result= new HashMap();
		
		result.put("queue", taskCronwork.getQueueInfo());
		result.put("retry", taskCronwork.getRetryFileInfo());
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/retryStatus", method = RequestMethod.GET)
	public @ResponseBody Map retryCheck(Model model) {
		Map result= new HashMap();
		
		result.put("retry", taskCronwork.getRetryFileInfo());
		result.put("retry status", !G.retryYN);
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/retry", method = RequestMethod.GET)
	public @ResponseBody Map retryChange(Model model) {
		Map result= new HashMap();
		
		if (G.retryYN) {
			G.retryYN = false;
		} else {
			G.retryYN = true;
		}
		
		result.put("retry", taskCronwork.getRetryFileInfo());
		result.put("retry status",!G.retryYN);
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/topicStatus", method = RequestMethod.GET)
	public @ResponseBody Map topicCheck(Model model) {
		Map result= new HashMap();
		
		result.put("topic status", !G.topicYN);
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/topic", method = RequestMethod.GET)
	public @ResponseBody Map topicChange(Model model) {
		Map result= new HashMap();
		
		if (G.topicYN) {
			G.topicYN = false;
		} else {
			G.topicYN = true;
		}
		
		result.put("topic status",!G.topicYN);
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
}
