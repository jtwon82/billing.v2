//package com.mobon.billing.hhtodd.schedule;
//
//import java.util.LinkedList;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.mobon.billing.hhtodd.service.HHtoDDMariaDB;
//
//@Component
//public class BatchTread   { 
//
//	private static final Logger		logger	= LoggerFactory.getLogger(BatchTread.class);
//	
//	@Autowired
//	private HHtoDDMariaDB			hHtoDDMariaDB;
//	
//	@Autowired
//	private TaskHHtoHHDD			taskHHtoHHDD;
//	
//	private final LinkedList	queue;
//	
//	private int threadCnt = 3;
//	
//	private final BatchTreadInner[]	threads;
//	
//	//private Map paramMap;
//	public BatchTread() {
//		
//		queue = new LinkedList();
//		 
//		threads = new BatchTreadInner[threadCnt];
//		for (int i = 0; i < threadCnt; i++) {
//			threads[i] = new BatchTreadInner();
//			threads[i].start();
//		}
//	}
//	
//	public BatchTread(int cnt) {
//		
//		queue = new LinkedList(); 
//
//		threads = new BatchTreadInner[threadCnt];
//		for (int i = 0; i < threadCnt; i++) {
//			threads[i] = new BatchTreadInner();
//			threads[i].start();
//		}
//		
//	}//end  생성자
//	
//	public int getThreadCnt() {
//		
//		return this.threadCnt;
//	}
//	
//	
//	
//	
//	
//	public void addQue(Map map) {
//		synchronized (queue) {
//			queue.add(map);
//			queue.notify();
//		}
//	}//end  생성자 
//	
//	 
//	
//	
//	private class BatchTreadInner extends Thread {
//
//		public void run() {
//			
//			Map paramMap  =null;
//			String batchType = "";
//			
//			while(true) {
//			
//				synchronized (queue) {
//					
//					while ( (queue.isEmpty()==true) || (threadCnt == 0) ) {
//						
//						try {
//							queue.wait();
//						} catch (InterruptedException ignored) {
//						}
//					}
//					paramMap = (Map) queue.removeFirst();
//					
//					//Thread  수 제한 
//					threadCnt--;
//				}
//				
//				 logger.info("threadCnt -- ==>{}",threadCnt);
//				batchType = paramMap.get("batchType")==null?"":(String)paramMap.get("batchType");
//				
//				if("".equals(batchType)) {
//					return ;
//				}
//				try {
//					
//					if("HH".equals(batchType)) {
//						//시간 
//						hHtoDDMariaDB.intoHHtoHHDB(paramMap);
//					}else if("DD".equals(batchType)) {
//						//일자
//						hHtoDDMariaDB.intoHHtoDDDB(paramMap);
//					}else if("NEAR".equals(batchType)) {
//						//지역
//						hHtoDDMariaDB.intoNearDB(paramMap);
//					}else if("CONV".equals(batchType)) {
//						//컨버젼 
//						hHtoDDMariaDB.intoConvDB(paramMap);
//					}
//					
//
//					
//				}catch(Exception e) {
//					e.printStackTrace();
//				}
//				
//				threadCnt++;
//				 
//				logger.info("threadCnt ++ ==>{}",threadCnt);
//
//				
//				
//			}//end while 		
//			
//		}//end run
//	}
//	
//}//end class
