package com.mobon.billing.base.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mobon.billing.core.schedule.TaskCronwork;

@Controller
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	@Autowired
	private TaskCronwork taskCronwork;
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/consumerInfo", method = RequestMethod.GET)
	public @ResponseBody Map kafka(Model model) {
		Map result= new HashMap();
		
		result.put("queue", taskCronwork.getQueueInfo());
		result.put("retry", taskCronwork.getRetryFileInfo());
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}
	
}
