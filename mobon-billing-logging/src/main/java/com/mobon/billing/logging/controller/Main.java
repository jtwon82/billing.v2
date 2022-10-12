package com.mobon.billing.logging.controller;

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
import org.springframework.web.client.RestTemplate;

import com.mobon.billing.logging.schedule.BillingService;

@Controller
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	@Autowired
	private BillingService BillingService;

    @Autowired
    private RestTemplate restTemplate;
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/consumerInfo", method = RequestMethod.GET)
	public @ResponseBody Map kafka(Model model) {
		Map<String, String> result= new HashMap();
		
//		result.put("queue", taskCronwork.getQueueInfo());
//		result.put("retry", taskCronwork.getRetryFileInfo());
		
		result.put("BeforeHourData", BillingService.chkingBeforeHourData().toString());
		result.put("BatchRunningTime", BillingService.buildBatchRunningTime().toString());
		result.put("ReportCtr", BillingService.buildReportCtr().toString());
		result.put("MediaChrgMonitor", BillingService.buildMediaChrgMonitor().toString());
		result.put("UserCtgrMonitor", BillingService.buildUserCtgrMonitor().toString());
		result.put("ChkingZeroViewClickConv", BillingService.selectChkingZeroViewClickConv().toString());
		
		result.put("FrameMonitor", BillingService.buildFrameMonitor().toString());
		result.put("RebuildConversionMonitor", BillingService.buildRebuildConversionMonitor().toString());
		result.put("DailyMonitor", BillingService.buildDailyMonitor().toString());
		


//		String[][] info = new String[][] {
//			{ "branchAction", "http://192.168.2.78:8070/consumerInfo", "" }
//			,{ "branchConv", "http://192.168.2.78:8071/consumerInfo", "" }
//			,{ "clickview", "http://192.168.2.112:8070/consumerInfo", "" }
//			,{ "clickviewHA", "http://192.168.2.79:8070/consumerInfo", "" }
//			,{ "branch2", "http://192.168.2.117:8070/consumerInfo", "" }
////			,{ "subjectCopy", "http://192.168.2.70:8075/consumerInfo", "" }
////			,{ "subjectCopyHA", "http://192.168.2.74:8075/consumerInfo", "" }
//			};
//
//		for (String[] row : info) {
//			logger.info("row[1] {}", row[1]);
//			try {
//				String response = restTemplate.getForObject(row[1], String.class);
//				JsonObject root = new JsonParser().parse(response).getAsJsonObject();
//				consumerInfo data= new Gson().fromJson(root, consumerInfo.class);
////				result.put(row[0], data.toString());
//			}catch(Exception e) {
//			}
//		}
		
		logger.info("consumerInfo : {}", result);
		
		return result;
	}

}
