package com.mobon.billing.report.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import net.sf.ehcache.Element;
import net.sf.json.JSONObject;

import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.disk.DiskSpace;

public class CountTask {

	static Logger logger = Logger.getLogger(CountTask.class);
	
	public void execute() {
		String fname = "./status/count.json";
		BufferedWriter bw = null;
		FileWriter fw = null;
		File file = new File(fname);
		try {
			JSONObject root = new JSONObject();
			boolean isExist = file.exists();
			if(isExist) {
				int i;  
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				StringBuffer buffer = new StringBuffer(); 
				byte[] b = new byte[4096]; 
				while( (i = is.read(b)) != -1) { 
					buffer.append(new String(b, 0, i)); 
				} 
				String str = buffer.toString().trim(); 
				JSONObject before = JSONObject.fromObject(str);
				
				if(CacheConfig.getCacheCount().get("count") != null) {
					if(CacheConfig.getCacheCount().get("count").getObjectValue() != null) {
						int sum = 0;
						if(before.containsKey("count")) {
							sum += before.getInt("count");
						}
						sum += ((int) CacheConfig.getCacheCount().get("count").getObjectValue());
						root.put("count", sum);
						CacheConfig.getCacheCount().put(new Element("count", 0));
					} else {
						root.putAll(before);
					}
				} else {
					root.putAll(before);
				}
//				System.out.println("package count :: " + CacheConfig.getCacheCount().get("count").getObjectValue());
			} else {
				root.put("count", "0");
			}
			
			fw = new FileWriter(fname);
			bw = new BufferedWriter(fw);
			bw.write(root.toString());	
			if(CacheConfig.getCacheCount().get("count") != null) {
				logger.info("package count :: " + root.toString() + ", disk space :: " + DiskSpace.space() + " %");
			} else {
				logger.info("package count :: " + root.toString() + ", disk space :: " + DiskSpace.space() + " %");
			}
			
			CacheConfig.getCacheCount().put(new Element("space", DiskSpace.isWrite()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		
	}
}
