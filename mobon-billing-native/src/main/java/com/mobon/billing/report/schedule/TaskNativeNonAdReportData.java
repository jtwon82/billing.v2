package com.mobon.billing.report.schedule;

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
import com.mobon.billing.model.v15.NativeNonAdReportData;
import com.mobon.billing.report.config.RetryConfig;
import com.mobon.billing.report.service.NativeNonAdReportDataToMariaDB;
import com.mobon.billing.report.service.SumObjectManager;
import com.mobon.billing.report.service.WorkQueueTaskData;
import com.mobon.billing.report.service.dao.NativeNonAdReportDataDao;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskNativeNonAdReportData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskNativeNonAdReportData.class);

	@Autowired
	private NativeNonAdReportDataDao				nativeNonAdReportDataDao;
	
	@Autowired
	private NativeNonAdReportDataToMariaDB		nativeNonAdReportDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("NativeNonAdReportDataWorkQueue")
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
		TaskNativeNonAdReportData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskNativeNonAdReportData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskNativeNonAdReportData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskNativeNonAdReportData.threadCnt;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void mongoToFileNativeNonAdReportData() {
		Map<String, NativeNonAdReportData> summeryNativeNonAdReportData = (Map<String, NativeNonAdReportData>) sumObjectManager.removeObjectMap(new NativeNonAdReportData());
		ArrayList<NativeNonAdReportData> listWriteData = new ArrayList<NativeNonAdReportData>();
		if(summeryNativeNonAdReportData!=null && summeryNativeNonAdReportData.entrySet()!=null) {
			try {
				for (Entry<String, NativeNonAdReportData> item : summeryNativeNonAdReportData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryNativeNonAdReportData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.NativeNonAdReport, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void mongoToMariaNativeNonAdReportData() {
		logger.debug(">> START mongoToMariaNativeNonAdReportDataV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, NativeNonAdReportData> summeryNativeNonAdReportData = (Map<String, NativeNonAdReportData>) sumObjectManager.removeObjectMap(new NativeNonAdReportData());
		ArrayList<NativeNonAdReportData> listNativeNonAdReportData = new ArrayList();
		if(summeryNativeNonAdReportData!=null && summeryNativeNonAdReportData.entrySet()!=null) {
			try {
				//int maxSize = summeryNativeNonAdReportData.entrySet().size();
				for (Entry<String, NativeNonAdReportData> item : summeryNativeNonAdReportData.entrySet()) {
					// 파티션때문에 
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.DATE, -14);
					String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
					String yyyymmdd = item.getValue().getYyyymmdd();
					if( Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) ) {
						continue;
					}
					
					
					if( listNativeNonAdReportData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendNativeNonAdReportData(item.getValue());
					} else {
						listNativeNonAdReportData.add(item.getValue());
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryNativeNonAdReportData.toString());
			}
			
			ArrayList<Map> list = nativeNonAdReportDataDao.selectGroupKey();
			String [][] group_key = new String [list.size()][];
			int i=0;
			for( Map row : list ) {
				//logger.debug("row - {}", row);
				group_key[i++] = new String [] { row.get("CODE_ID").toString(), row.get("w").toString() };
			}
			
			for (String[] group : group_key) {
				String _id = Arrays.asList(group).toString();
				List<NativeNonAdReportData> filtering = (List<NativeNonAdReportData>) listNativeNonAdReportData.stream()
						.filter(row -> _id.equals( row.getGrouping() ) )
						.collect(Collectors.toList());
				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if ( workingKey.contains(_id) ) {
						logger.info("workingKey.contains - {}, listNativeNonAdReportData.size - {}", _id, listNativeNonAdReportData.size());
						
						for( NativeNonAdReportData row : filtering ) {
							sumObjectManager.appendNativeNonAdReportData(row);
						}
					} else {
						workingKey.add( _id, 3 );
						
						if( 10000>filtering.size() ) {
							workQueue.execute(new TaskData(G.NativeNonAdReport, _id, filtering));
							
						} else {
							List<NativeNonAdReportData> intoFiltering = new ArrayList<NativeNonAdReportData>();
							int cnt=0;
							for( NativeNonAdReportData row : filtering ) {
								if( 10000>cnt++ ) {
									intoFiltering.add(row);
								}else {
									sumObjectManager.appendNativeNonAdReportData(row);
								}
							}
							workQueue.execute(new TaskData(G.NativeNonAdReport, _id, intoFiltering));
						}
					}
				}
			}
		}
		workingKey.remove("main");
	}
	
	@SuppressWarnings("unchecked")
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = nativeNonAdReportDataToMariaDB.intoMariaNativeNonAdReportDataV3(taskData.getId(), (List<NativeNonAdReportData>)taskData.getFiltering(), false);
			} else {
				result = nativeNonAdReportDataToMariaDB.intoMariaNativeNonAdReportDataV3(taskData.getId(), (List<NativeNonAdReportData>)taskData.getFiltering(), true);
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
