package com.mobon.billing.core.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class TaskMonitor {

	private static final Logger logger = LoggerFactory.getLogger(TaskMonitor.class);

	private static int threadCnt = 0;

	public static void setThreadCnt(int threadCnt) {
		TaskMonitor.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskMonitor.threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		TaskMonitor.threadCnt--;
	}

	public void mobonKafkaSendError() {
		logger.info(">> START mobonKafkaSendError");

		if (threadCnt > 0) {
			return;
		}
		increaseThreadCnt();

//		 String [] iplist = new String[]
//		 {"03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19"
//				 ,"32","33","34","35","36","37","38","39","40","41","42","43"};
//
////		String[] iplist = new String[] { "03" };
//		 
//		try {
//			for (String ip : iplist) {
//				URL url = new URL(String.format("http://10.251.0.%s/cronwork/monitoring/kafka_error_retrycnt.txt", ip));
//				
//				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream() ));
//				String inputLine;
//				while ((inputLine = in.readLine()) != null)
//					logger.debug("url - {}, body - {}", url.toString(), inputLine);
//				
//				in.close();
//
//			}
//		} catch (Exception e) {
//		}

		decreaseThreadCnt();
	}

}
