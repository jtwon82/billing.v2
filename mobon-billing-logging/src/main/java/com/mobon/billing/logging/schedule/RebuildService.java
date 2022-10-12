package com.mobon.billing.logging.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mobon.billing.logging.config.CacheConfig;
import com.mobon.billing.logging.schedule.dao.BillingDao;
import com.mobon.billing.util.FileUtils;


//@Component
public class RebuildService {
	private static final Logger logger = LoggerFactory.getLogger(RebuildService.class);

	@Value("${cron.path}")
    private String cronPath;
    
    @Autowired
    private BillingDao billingDao;
    
    @Autowired
    private CacheConfig cache;
    
    public void buildRebuildConversionMonitor() {
    	List<Map<String, String>> list = billingDao.selectRebuildConversionMonitor();
    	logger.info("buildRebuildConversionMonitor list - {}", new Gson().toJson(list));
    	
    	fileWrite("rebuildConvCheck.txt", list);
    	
    }
    
    private void fileWrite(String filename, List list) {
		
		FileUtils.mkFolder(cronPath);
		File file = new File( cronPath + filename );
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		} catch (IOException e) {
			logger.error("err ", e);
		}
	    out.println( new Gson().toJson(list));
	    out.close();
    }

}
