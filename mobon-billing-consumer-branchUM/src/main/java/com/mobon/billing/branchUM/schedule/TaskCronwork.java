package com.mobon.billing.branchUM.schedule;

import com.mobon.billing.branchUM.schedule.TaskCronwork;
import com.mobon.billing.branchUM.service.WorkQueueTaskData;
import com.mobon.billing.util.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class TaskCronwork
{
  private static final Logger logger = LoggerFactory.getLogger(TaskCronwork.class);
  
  @Value("${profile.id}")
  private String profileId;
  @Value("${log.path}")
  private String logPath;
  
  @Autowired
  @Qualifier("KnoUMSiteCodeDataWorkQueue")
  private WorkQueueTaskData KnoUMSiteCodeDataWorkQueue;
  
  @Autowired
  @Qualifier("KnoUMScriptNoDataWorkQueue")
  private WorkQueueTaskData KnoUMScriptNoDataWorkQueue;
  
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
    int KnoUMDataWorkQueueSize = 0;
    KnoUMDataWorkQueueSize += this.KnoUMSiteCodeDataWorkQueue.getQueueSize();
    KnoUMDataWorkQueueSize += this.KnoUMScriptNoDataWorkQueue.getQueueSize();
    
    Map<String, Integer> map = new HashMap<>();
    map.put("KnoUMDataWorkQueue", Integer.valueOf(0));
    
    if (KnoUMDataWorkQueueSize > 0) {
      map.put("KnoUMDataWorkQueueSize", Integer.valueOf(KnoUMDataWorkQueueSize));
    }
    return map;
  }
}
