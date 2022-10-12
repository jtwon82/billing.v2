package com.mobon.billing.subjectCopy.schedule;

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

import com.mobon.billing.subjectCopy.service.WorkQueueTaskData;
import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

@Component
public class TaskCronWork 
{
	private static final Logger logger = LoggerFactory.getLogger(TaskCronWork.class);

	@Value("${profile.id}")
	private String profileId;
	@Value("${log.path}")
	private String logPath;

	@Autowired
	@Qualifier("SubjectCopyClickViewWorkQueue")
	private WorkQueueTaskData subjectCopyClickViewDataWorkQueue;

	@Autowired
	@Qualifier("SubjectCopyConvWorkQueue")
	private WorkQueueTaskData subjectCopyConvDataWorkQueue;

	public void chkingQueueSize() {
		logger.info(">> START chkingQueueSize");

		Map map = getQueueInfo();
		try {
			JSONObject json = JSONObject.fromObject(map);
			String filePath = String.format("%s", new Object[] { this.logPath + "cron/" });

			FileUtils.mkFolder(filePath);

			File file = new File(filePath + this.profileId + ".txt");

			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			out.println(json);
			out.close();
		}
		catch (Exception exception) {}
	}

	public Map getQueueInfo() {
		int workQueueSize = 0;
		workQueueSize += this.subjectCopyClickViewDataWorkQueue.getQueueSize();
		workQueueSize += this.subjectCopyConvDataWorkQueue.getQueueSize();

		Map<String, Integer> map = new HashMap<>();
		map.put("SubjectCopyWorkQueue", Integer.valueOf(0));

		if (workQueueSize > 0) {
			map.put("KnoUMDataWorkQueueSize", Integer.valueOf(workQueueSize));
		}
		return map;
	}

	public Map getRetryFileInfo() {
		File []files = new File( logPath +"retry/" ).listFiles();
		Map result= new HashMap();
	
		if(files.length>1) {
			result.put("fileLength", files.length);
			result.put("fileList", Arrays.asList(files));
		} else {
			result.put("fileLength", 0);
			result.put("fileList", Arrays.asList(""));
		}
		
		return result;
	}

}
