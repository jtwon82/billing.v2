package com.mobon.billing.dev.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mobon.billing.dev.consumer.ConsumerTopicReTry;
import com.mobon.billing.dev.service.dao.SelectDao;

@Controller
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Autowired
	private SelectDao selectDao;
	@Autowired
	private ConsumerTopicReTry retry;
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/kafka", method = RequestMethod.GET)
	public void test(Model model) {
		Map map = selectDao.selectNow();
		logger.info("test {}", map);
		
//		retry.processTopicFile();
	}
	
}
