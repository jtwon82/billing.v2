package com.mobon.billing.core.schedule;

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
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ExternalDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskExternalData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskExternalData.class);

	@Autowired
	private SelectDao				selectDao;
	
	@Autowired
	private ExternalDataToMariaDB	externalDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	private RetryTemplate			retryTemplate;

	@Autowired
	@Qualifier("ExternalDataWorkQueue")
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
		TaskExternalData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskExternalData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskExternalData.threadCnt--;
	}
	public static int getThreadCnt() {	
		return TaskExternalData.threadCnt;
	}




	public void mongoToFileExternal() {
		Map<String, ExternalInfoData> summeryExternalInfoData = (Map<String, ExternalInfoData>) sumObjectManager.removeObjectMap(new ExternalInfoData());
		ArrayList<ExternalInfoData> listWriteData = new ArrayList();
		if(summeryExternalInfoData!=null && summeryExternalInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ExternalInfoData> item : summeryExternalInfoData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryExternalInfoData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.external_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}

	public void mongoToMariaExternalV3() {
		logger.info(">> START mongoToMariaExternalV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		

		Map<String, ExternalInfoData> summeryExternalInfoData = (Map<String, ExternalInfoData>) sumObjectManager.removeObjectMap(new ExternalInfoData());
		
		ArrayList<ExternalInfoData> listExternalInfoData = new ArrayList();
		if(summeryExternalInfoData!=null && summeryExternalInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ExternalInfoData> item : summeryExternalInfoData.entrySet()) {
					try {
						// 파티션때문에 
						Calendar c = Calendar.getInstance();
						c.setTime(new Date());
						c.add(Calendar.DATE, -14);
						String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
						String yyyymmdd = item.getValue().getYyyymmdd();
						if( Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) ) {
//							logger.info("continue message - {}", jSONObject);
							continue;
						}

						if( listExternalInfoData.size()>=Integer.parseInt(batchListSize) ) {
							sumObjectManager.appendData(item.getValue(), false);
							logger.info("listExternalInfoData.size() - {}, batchListSize - {}", listExternalInfoData.size(), batchListSize);
						}else {
							listExternalInfoData.add(item.getValue());
						}
						//logger.debug("item - {}", item);
					
					}catch(Exception e) {
						logger.error("err data-{}",item.getValue(), e);
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.debug("item - {}", summeryExternalInfoData.toString());
			}
			
			logger.debug("listExternalInfoData.size {}", listExternalInfoData.size());
			
////			String [][] group_key = new String[][]{
////				{"AD", G.EB},{"CW", G.EB},{"HU", G.EB},{"KL", G.EB},{"PB", G.EB},{"RC", G.EB},{"RM", G.EB},{"SA", G.EB},{"SH", G.EB},{"SP", G.EB},{"SR", G.EB},{"ST", G.EB},{"UM", G.EB},{"HB", G.EB},{"MM", G.EB}
////				,{"AD", G.EC},{"CW", G.EC},{"HU", G.EC},{"KL", G.EC},{"PB", G.EC},{"RC", G.EC},{"RM", G.EC},{"SA", G.EC},{"SH", G.EC},{"SP", G.EC},{"SR", G.EC},{"ST", G.EC},{"UM", G.EC},{"HB", G.EC},{"MM", G.EB}
////			};
//			String [][] group_key = new String[][]{
//				{"AD", G.EB}
//				,{"AD", G.EC},{"HB", G.EC}
//			};
//			
//			for (String[] group : group_key) {
//				String _id = Arrays.asList(group).toString();
//				
//				List<ExternalInfoData> filtering = (List<ExternalInfoData>) listExternalInfoData.stream()
//						.filter(row -> _id.equals( row.getGrouping() ) )
//						.collect(Collectors.toList());
			
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				List<ExternalInfoData> filtering = (List<ExternalInfoData>) listExternalInfoData.stream()
						.filter(row -> _id.equals( row.getGroupingExternal() ) )	// scriptNo % 10
						.collect(Collectors.toList());

				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if (workingKey.contains(_id)) {
						logger.info("workingKey.contains - {}, listExternalInfoData.size {}", _id, listExternalInfoData.size());

						for( ExternalInfoData row : filtering ) {
							sumObjectManager.appendData(row, false);
						}
					} else {
						workingKey.add( _id, 3 );
						//workQueue.execute(new RetryTaskerV3(filtering, group));
						//workQueue.execute(new TaskData(G.external_info, _id, filtering));

						if( 1000>filtering.size() ) {
							workQueue.execute(new TaskData(G.external_info, _id, filtering));
							
						} else {
							List<ExternalInfoData> intoFiltering = new ArrayList();
							int cnt=0;
							for( ExternalInfoData row : filtering ) {
								if( 1000>cnt++ ) {
									intoFiltering.add(row);
								}else {
									sumObjectManager.appendData(row, false);
								}
							}
							workQueue.execute(new TaskData(G.external_info, _id, intoFiltering));
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
		
		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = externalDataToMariaDB.intoMariaExternalDataV3(taskData.getId(), (List<ExternalInfoData>)taskData.getFiltering(), false);
			} else {
				result = externalDataToMariaDB.intoMariaExternalDataV3(taskData.getId(), (List<ExternalInfoData>)taskData.getFiltering(), true);
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
			
			logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());
			
		} else {
			workQueue.execute(taskData);
		}
	}

}
