package com.mobon.billing.uniidstats.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobon.billing.uniidstats.service.WorkQueueTaskData;

import net.sf.json.JSONObject;

@Component
public class TaskCronwork {

	private static final Logger	logger	= LoggerFactory.getLogger(TaskCronwork.class);

	@Value("${log.cron.stats_file}")
	private String	logCronStats_file;
	
	@Autowired
	@Qualifier("UseridVoWorkQueue")
	private WorkQueueTaskData workQueue;
	
	public void chkingQueueSize() {
		logger.info(">> START chkingQueueSize");
		
		int workQueueSize = workQueue.getQueueSize();
		
		Map<String, Long> map = new HashMap();
		map.put("nativeWorkQueueSize", (long) 0);
		if( workQueueSize > 0 ) {
			map.put("nativeQueueSize", (long) workQueueSize);
		}
		map.put("regDate", new Date().getTime());
		
		try {
			JSONObject json = JSONObject.fromObject(map);

			File file = new File( logCronStats_file+"/stats.log" );
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		    out.println(json);
		    out.close();
		    
		}catch(Exception e) {
			
		}
	}

}
