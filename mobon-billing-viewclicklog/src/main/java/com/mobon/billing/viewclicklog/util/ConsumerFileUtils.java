package com.mobon.billing.viewclicklog.util;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

public class ConsumerFileUtils {

	public static final void writeLine(String failDir, String fileName, String FREFIX, List<?> aggregateList) throws IOException {
		
		if( aggregateList!=null ) {
			for(Object vo : aggregateList){
				FileUtils.appendStringToFile(failDir, 
						fileName, 
						String.format("%s\t%s\t%s", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), FREFIX, JSONObject.fromObject(vo).toString() ));
			}
		}
	}
	public static final void writeLine(String failDir, String fileName, String topicName, Object vo) throws IOException {
		
		FileUtils.appendStringToFile(failDir, 
				fileName, 
				String.format("%s\t%s\t%s", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topicName, vo ));
	}
	
	public static final void writeLineNonDateTopic(String failDir, String fileName, Object vo) throws IOException {
		
		FileUtils.appendStringToFile(failDir,fileName,	String.format("%s",  vo ));
	}
	
}
