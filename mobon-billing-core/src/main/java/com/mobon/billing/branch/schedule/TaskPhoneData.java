package com.mobon.billing.branch.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branch.service.PhoneDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskPhoneData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskPhoneData.class);

	@Autowired
	private SelectDao				selectDao;
	
	@Autowired
	private PhoneDataToMariaDB		PhoneDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("PhoneDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public static void setThreadCnt(int threadCnt) {
		TaskPhoneData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskPhoneData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskPhoneData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskPhoneData.threadCnt;
	}
	
	
	
	public void mongoToFilePhoneData() {
		Map<String, BaseCVData> summeryPhoneData = (Map<String, BaseCVData>) sumObjectManager.removePhoneData();
		ArrayList<BaseCVData> listWriteData = new ArrayList();
		if(summeryPhoneData!=null && summeryPhoneData.entrySet()!=null) {
			try {
				for (Entry<String, BaseCVData> item : summeryPhoneData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryPhoneData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.Phone_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	public void mongoToMariaPhoneData() {
		logger.info(">> START mongoToMariaPhoneDataV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, BaseCVData> summeryPhoneData = (Map<String, BaseCVData>) sumObjectManager.removePhoneData();
		ArrayList<BaseCVData> listPhoneData = new ArrayList();
		if(summeryPhoneData!=null && summeryPhoneData.entrySet()!=null) {
			try {
				int maxSize = summeryPhoneData.entrySet().size();
				for (Entry<String, BaseCVData> item : summeryPhoneData.entrySet()) {
					// 파티션때문에 
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.DATE, -14);
					String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
					String yyyymmdd = item.getValue().getYyyymmdd();
					if( Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) ) {
						continue;
					}
					
					
					if( listPhoneData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendPhoneData(item.getValue());
					} else {
						listPhoneData.add(item.getValue());
					}
				}
				logger.info("listPhoneData.size {}", listPhoneData.size());
				
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryPhoneData.toString());
			}
			
//			ArrayList<BaseCVData> list = selectDao.selectAdgubunKey();
//			String [][] group_key = new String [list.size()][];
//			int i=0;
//			for( BaseCVData row : list ) {
//				//logger.debug("row - {}", row);
//				group_key[i++] = new String [] { row.getAdGubun(), row.getPlatform() };
//			}
//			
//			for (String[] group : group_key) {
			
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				
				List<BaseCVData> filtering = (List<BaseCVData>) listPhoneData.stream()
						.filter(row -> _id.equals( row.getGroupingS() ) )
						.collect(Collectors.toList());
				
				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if ( workingKey.contains(_id) ) {
						logger.info("workingKey.contains - {}, listPhoneData.size - {}", _id, listPhoneData.size());
						
						for( BaseCVData row : filtering ) {
							sumObjectManager.appendPhoneData(row);
						}
					} else {
						workingKey.add( _id, 3 );
						//workQueue.execute(new RetryTaskerV3(filtering, group));
						
						if( 10000>filtering.size() ) {
							workQueue.execute(new TaskData(G.Phone_info, _id, filtering));
							
						} else {
							List<BaseCVData> intoFiltering = new ArrayList();
							int cnt=0;
							for( BaseCVData row : filtering ) {
								if( 10000>cnt++ ) {
									intoFiltering.add(row);
								}else {
									sumObjectManager.appendPhoneData(row);
								}
							}
							workQueue.execute(new TaskData(G.Phone_info, _id, intoFiltering));
						}
					}
				}
			}
		}
		workingKey.remove("main");
	}
	
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = PhoneDataToMariaDB.intoMariaPhoneDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = PhoneDataToMariaDB.intoMariaPhoneDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
			}
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
		}
		
		decreaseThreadCnt();
		
		if (result) {
			int i=3;
			while(!workingKey.remove( taskData.getId() )) {
				if(--i<0) {
					logger.error("while(!workingKey.remove( _id )) 1 _id - {}", taskData.getId());
					break;
				}
			}
			
			//logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());
			
		} else {
			workQueue.execute(taskData);
			logger.info("retry size {}", taskData.getFiltering().size());
		}
	}
	
}
