package com.mobon.billing.framertb.schedule;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.mobon.billing.framertb.service.WorkQueueTaskData;
//import com.mobon.billing.util.FileUtils;
//
//import net.sf.json.JSONObject;

//@Component
public class TaskCronwork {
//
//	private static final Logger	logger	= LoggerFactory.getLogger(TaskCronwork.class);
//
//	@Value("${profile.id}")
//	private String	profileId;
//	@Value("${log.path}")
//	private String	logPath;
//	
//	@Autowired
//	@Qualifier("FrameRtbType1DataWorkQueue")
//	private WorkQueueTaskData		frameRtbDataWorkQueue;
//	
//	public void chkingQueueSize() {
//		logger.info(">> START chkingQueueSize");
//		
//		Map map = getQueueInfo();
//		try {
//			JSONObject json = JSONObject.fromObject(map);
//			String filePath = String.format("%s", logPath +"cron/");
//			
//			FileUtils.mkFolder(filePath);
//			
//			File file = new File( filePath + profileId +".txt" );
//			
//			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
//		    out.println(json);
//		    out.close();
//		    
//		}catch(Exception e) {
//			
//		}
//	}
//	
//	public Map getQueueInfo() {
//		int frameDataWorkQueue = frameRtbDataWorkQueue.getQueueSize();
//
//		
//		Map<String, Integer> map = new HashMap();
//		map.put("frameDataWorkQueue", 0);
//		
//		if( frameDataWorkQueue > 0 ) {
//			map.put("frameDataWorkQueue", frameDataWorkQueue);
//		}
//		
//		return map;
//	}
//	
//	public Object getRetryFileInfo() {
//		File []files = new File( logPath +"retry/" ).listFiles();
//		Map result= new HashMap();
//		//result.put("fileLength", files.length);
//		//result.put("fileList", Arrays.asList(files));
//		
//		if(files.length>1) {
//			result.put("fileLength", files.length);
//			result.put("fileList", Arrays.asList(files));
//		} else {
//			result.put("fileLength", 0);
//			result.put("fileList", Arrays.asList(""));
//		}
//		
//		return result;
//	}

}
