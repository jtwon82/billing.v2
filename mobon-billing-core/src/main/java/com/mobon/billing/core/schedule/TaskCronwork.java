package com.mobon.billing.core.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

@Component
public class TaskCronwork {

	private static final Logger	logger	= LoggerFactory.getLogger(TaskCronwork.class);

	@Value("${profile.id}")
	private String	profileId;
	@Value("${log.path}")
	private String	logPath;
	
	@Autowired
	@Qualifier("ActionDataWorkQueue")
	private WorkQueueTaskData		actionDataWorkQueue;
	@Autowired
	@Qualifier("ClickViewDataWorkQueue")
	private WorkQueueTaskData		clickViewDataWorkQueue;
	@Autowired
	@Qualifier("ClientEnvirmentDataWorkQueue")
	private WorkQueueTaskData		clientEnvirmentDataWorkQueue;
	@Autowired
	@Qualifier("ClickViewPointDataWorkQueue")
	private WorkQueueTaskData		clickViewPointDataWorkQueue;
	@Autowired
	@Qualifier("ConvDataWorkQueue")
	private WorkQueueTaskData		convDataWorkQueue;
	@Autowired
	@Qualifier("ExternalDataWorkQueue")
	private WorkQueueTaskData		externalDataWorkQueue;
	@Autowired
	@Qualifier("ShopInfoDataWorkQueue")
	private WorkQueueTaskData		shopInfoDataWorkQueue;
	@Autowired
	@Qualifier("ShopStatsInfoDataWorkQueue")
	private WorkQueueTaskData		shopStatsInfoDataWorkQueue;
	
	@Autowired
	@Qualifier("NearDataWorkQueue")
	private WorkQueueTaskData		nearDataWorkQueue;
//	@Autowired
//	@Qualifier("AppTargetDataWorkQueue")
//	private WorkQueueTaskData		appTargetDataWorkQueue;
	
	public void chkingQueueSize() {
		logger.info(">> START chkingQueueSize");
		
		Map map = getQueueInfo();
		try {
			JSONObject json = JSONObject.fromObject(map);
			String filePath = String.format("%s", logPath +"cron/");
			
			FileUtils.mkFolder(filePath);
			
			File file = new File( filePath + profileId +".txt" );
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		    out.println(json);
		    out.close();
		    
		}catch(Exception e) {
			
		}
	}
	
	
	public Map getRetryFileInfo() {
		File []files = new File( logPath +"retry/" ).listFiles();
		Map result= new HashMap();
		//result.put("fileLength", files.length);
		//result.put("fileList", Arrays.asList(files));
		
		if(files.length>1) {
			result.put("fileLength", files.length);
			result.put("fileList", Arrays.asList(files));
		} else {
			result.put("fileLength", 0);
			result.put("fileList", Arrays.asList(""));
		}
		
		return result;
	}
	
	public Map getQueueInfo() {
		int actionQueueSize = actionDataWorkQueue.getQueueSize();
		int clickViewQueueSize = clickViewDataWorkQueue.getQueueSize();
		int clientEnvirmentQueueSize = clientEnvirmentDataWorkQueue.getQueueSize();
		int clickViewPointQueueSize = clickViewPointDataWorkQueue.getQueueSize();
		int convQueueSize = convDataWorkQueue.getQueueSize();
		int externalQueueSize = externalDataWorkQueue.getQueueSize();
		int shopInfoQueueSize = shopInfoDataWorkQueue.getQueueSize();
		int shopStatsQueueSize = shopStatsInfoDataWorkQueue.getQueueSize();
		int nearQueueSize = nearDataWorkQueue.getQueueSize();
//		int appTargetQueueSize = appTargetDataWorkQueue.getQueueSize();
		
		Map<String, Integer> map = new HashMap();
		map.put("actionQueueSize", 0);
		map.put("clickViewQueueSize", 0);
		map.put("clientEnvirmentQueueSize", 0);
		map.put("clickViewPointQueueSize", 0);
		map.put("convQueueSize", 0);
		map.put("externalQueueSize", 0);
		map.put("shopInfoQueueSize", 0);
		map.put("shopStatsQueueSize", 0);
		map.put("nearQueueSize", 0);
//		map.put("appTargetDataWorkQueue", 0);
		
		if( actionQueueSize > 0 ) {
			map.put("actionQueueSize", actionQueueSize);
		}
		if( clickViewQueueSize > 0 ) {
			map.put("clickViewQueueSize", clickViewQueueSize);
		}
		if( clientEnvirmentQueueSize > 0 ) {
			map.put("clientEnvirmentQueueSize", clientEnvirmentQueueSize);
		}
		if( clickViewPointQueueSize > 0 ) {
			map.put("clickViewQueueSize", clickViewPointQueueSize);
		}
		if( convQueueSize > 0 ) {
			map.put("convQueueSize", convQueueSize);
		}
		if( externalQueueSize > 0 ) {
			map.put("externalQueueSize", externalQueueSize);
		}
		if( shopInfoQueueSize > 0 ) {
			map.put("shopInfoQueueSize", shopInfoQueueSize);
		}
		if( shopStatsQueueSize > 0 ) {
			map.put("shopStatsQueueSize", shopStatsQueueSize);
		}
		if( nearQueueSize > 0 ) {
			map.put("nearQueueSize", nearQueueSize);
		}
//		if( nearQueueSize > 0 ) {
//			map.put("appTargetQueueSize", appTargetQueueSize);
//		}
		return map;
	}

}
