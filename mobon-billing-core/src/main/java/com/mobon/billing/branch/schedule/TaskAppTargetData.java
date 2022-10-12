//package com.mobon.billing.branch.schedule;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.ConcurrentModificationException;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.adgather.constants.G;
//import com.adgather.util.old.DateUtils;
//import com.mobon.billing.core.config.RetryConfig;
//import com.mobon.billing.branch.service.AppTargetDataToMariaDB;
//import com.mobon.billing.core.service.SumObjectManager;
//import com.mobon.billing.core.service.WorkQueueTaskData;
//import com.mobon.billing.core.service.dao.SelectDao;
//import com.mobon.billing.model.v15.AppTargetData;
//import com.mobon.billing.model.v15.BaseCVData;
//import com.mobon.billing.util.ConsumerFileUtils;
//import com.mobon.billing.util.TimeToLiveCollection;
//import com.mobon.exschedule.model.TaskData;
//
//@Component
//public class TaskAppTargetData {
//
//	private static final Logger		logger		= LoggerFactory.getLogger(TaskAppTargetData.class);
//
//	@Autowired
//	private SelectDao				selectDao;
//	
//	@Autowired
//	private AppTargetDataToMariaDB	appTargetDataToMariaDB;
//
//	@Autowired
//	private RetryConfig				retryConfig;
//
//	@Autowired
//	@Qualifier("AppTargetDataWorkQueue")
//	private WorkQueueTaskData		workQueue;
//
//	@Autowired
//	private SumObjectManager		sumObjectManager;
//	
//	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
//	
//	private static int				threadCnt	= 0;
//
//	@Value("${log.path}")
//	private String	logPath;
//	@Value("${batch.list.size}")
//	private String	batchListSize;
//	
//	public static void setThreadCnt(int threadCnt) {
//		TaskAppTargetData.threadCnt = threadCnt;
//	}
//	public static synchronized void increaseThreadCnt() {
//		TaskAppTargetData.threadCnt++;
//	}
//	public static synchronized void decreaseThreadCnt() {
//		TaskAppTargetData.threadCnt--;
//	}
//	
//	public static int getThreadCnt() {	
//		return TaskAppTargetData.threadCnt;
//	}
//	
//	
//	
//	public void mongoToFileAppTargetData() {
//		Map<String, AppTargetData> summeryAppTargetData = (Map<String, AppTargetData>) sumObjectManager.removeAppTargetData();
//		ArrayList<AppTargetData> listWriteData = new ArrayList();
//		if(summeryAppTargetData!=null && summeryAppTargetData.entrySet()!=null) {
//			try {
//				for (Entry<String, AppTargetData> item : summeryAppTargetData.entrySet()) {
//					listWriteData.add(item.getValue());
//				}
//			}catch(ConcurrentModificationException e) {
//				logger.error("ConcurrentModificationException item - {}", summeryAppTargetData.toString());
//			}
//			
//			if( listWriteData.size()>0 ) {
//				long millis = Calendar.getInstance().getTimeInMillis();
//				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
//				try {
//					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.AppTarget_info, listWriteData);
//				} catch (IOException e) {
//				}
//			}
//		}
//	}
//	
//	
//	public void mongoToMariaAppTargetData() {
//		logger.info(">> START mongoToMariaAppTargetDataV3 THREAD COUNT - {}", threadCnt);
//		
//		if( workingKey.contains("main") ) {
//			return;
//		}
//		
//		workingKey.add("main", 1);
//		
//		Map<String, AppTargetData> summeryAppTargetData = (Map<String, AppTargetData>) sumObjectManager.removeAppTargetData();
//		ArrayList<AppTargetData> listAppTargetData = new ArrayList();
//		if(summeryAppTargetData!=null && summeryAppTargetData.entrySet()!=null) {
//			try {
//				int maxSize = summeryAppTargetData.entrySet().size();
//				for (Entry<String, AppTargetData> item : summeryAppTargetData.entrySet()) {
//					// 파티션때문에 
//					Calendar c = Calendar.getInstance();
//					c.setTime(new Date());
//					c.add(Calendar.DATE, -14);
//					String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
//					String yyyymmdd = item.getValue().getYyyymmdd();
//					if( Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) ) {
//						continue;
//					}
//					
//					
//					if( listAppTargetData.size()>=Integer.parseInt(batchListSize) ) {
//						sumObjectManager.appendAppTargetData(item.getValue());
//					} else {
//						listAppTargetData.add(item.getValue());
//					}
//				}
//			}catch(ConcurrentModificationException e) {
//				logger.error("ConcurrentModificationException item - {}", summeryAppTargetData.toString());
//			}
//			
//			ArrayList<BaseCVData> list = selectDao.selectAdgubunKey();
//			String [][] group_key = new String [list.size()][];
//			int i=0;
//			for( BaseCVData row : list ) {
//				group_key[i++] = new String [] { row.getAdGubun(), row.getPlatform() };
//			}
//			
//			for (String[] group : group_key) {
//				String _id = Arrays.asList(group).toString();
//				
//				List<AppTargetData> filtering = (List<AppTargetData>) listAppTargetData.stream()
//						.filter(row -> _id.equals( row.getGrouping() ) )
//						.collect(Collectors.toList());
//				
//				if( filtering.size()>0 ) {
//					logger.info("filtering {}, {}", _id, filtering.size());
//					
//					if ( workingKey.contains(_id) ) {
//						logger.info("workingKey.contains - {}, listAppTargetData.size - {}", _id, listAppTargetData.size());
//						
//						for( AppTargetData row : filtering ) {
//							sumObjectManager.appendAppTargetData(row);
//						}
//					} else {
//						workingKey.add( _id, 3 );
//						
//						if( 10000>filtering.size() ) {
//							workQueue.execute(new TaskData(G.AppTarget_info, _id, filtering));
//							
//						} else {
//							List<AppTargetData> intoFiltering = new ArrayList();
//							int cnt=0;
//							for( AppTargetData row : filtering ) {
//								if( 10000>cnt++ ) {
//									intoFiltering.add(row);
//								}else {
//									sumObjectManager.appendAppTargetData(row);
//								}
//							}
//							workQueue.execute(new TaskData(G.AppTarget_info, _id, intoFiltering));
//						}
//					}
//				}
//			}
//		}
//		workingKey.remove("main");
//	}
//	
//	public void mongoToMariaV3(TaskData taskData) {
//
//		boolean result = false;
//		
//		increaseThreadCnt();
//		
//		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
//		try {
//			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
//				result = appTargetDataToMariaDB.intoMariaAppTargetDataV3(taskData.getId(), (List<AppTargetData>)taskData.getFiltering(), false);
//			} else {
//				result = appTargetDataToMariaDB.intoMariaAppTargetDataV3(taskData.getId(), (List<AppTargetData>)taskData.getFiltering(), true);
//			}
//		} catch (Exception e) {
//			logger.error("err _id - {}", taskData.getId(), e);
//		}
//		
//		decreaseThreadCnt();
//		
//		if (result) {
//			int i=3;
//			while(!workingKey.remove( taskData.getId() )) {
//				if(--i<0) {
//					logger.error("while(!workingKey.remove( _id )) 1 _id - {}", taskData.getId());
//					break;
//				}
//			}
//			
//		} else {
//			workQueue.execute(taskData);
//			logger.info("retry size {}", taskData.getFiltering().size());
//		}
//	}
//	
//}
