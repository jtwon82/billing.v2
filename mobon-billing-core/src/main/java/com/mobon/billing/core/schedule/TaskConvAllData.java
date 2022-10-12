package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
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
import com.mobon.billing.core.service.ConvAllDataToMariaDB;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskConvAllData {

	private static final Logger		logger	= LoggerFactory.getLogger(TaskConvAllData.class);

	@Autowired
	private RetryConfig				retryConfig;
	@Autowired
	private RetryTemplate			retryTemplate;
	@Autowired
	private DataBuilder			dataBuilder;


	@Autowired
	@Qualifier("ConvAllDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ConvAllDataToMariaDB		ConvAllDataToMariaDB;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;
	
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection(); 
	
	private static int				threadCnt	= 0;

	public static void setThreadCnt(int threadCnt) {
		TaskConvAllData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskConvAllData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskConvAllData.threadCnt--;
	} 
	
	public static int getThreadCnt() {	
		return TaskConvAllData.threadCnt;
	}



	public void mongoToFileConvAllData() {
		List<ConvData> summeryConvAllData = (List<ConvData>) sumObjectManager.removeConvAllData();
		
		ArrayList<ConvData> listWriteData = new ArrayList();
		if(summeryConvAllData!=null && summeryConvAllData.size()>0 ) {
			logger.info("ConvAll summeryConvAllData - {}", summeryConvAllData.size());
			
			try {
				for (ConvData item : summeryConvAllData) {
					listWriteData.add(item);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryConvAllData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				logger.info("ConvAll listWriteData - {}", listWriteData.size());
				
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.convAll_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaConvAllDataV3() {
		logger.info(">> START mongoToMariaConvAllDataV3");

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ConvData> listConvAllData = (List<ConvData>) sumObjectManager.removeConvAllData();
		
		logger.info("listConvAllData.size:{}", listConvAllData.size());
		
		
		String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
		for (String group : group_key) {
			String _id = Arrays.asList(group).toString();
			List<ConvData> filtering = (List<ConvData>) listConvAllData.stream()
					.filter(row -> _id.equals( String.format("[%s]", Math.abs(row.getAdvertiserId().hashCode() % 20))  ) )
					.collect(Collectors.toList());

			if( filtering.size()>0 ) {
				logger.info("filtering {}, {}", _id, filtering.size());

				if ( workingKey.contains(_id) ) {
					logger.info("workingKey.contains - {}", _id);
					
					for( ConvData row : filtering ) {
						sumObjectManager.appendConvAllData(row);
					}
				} else {
					List<ConvData> intoFiltering = new ArrayList();
					for( ConvData row : filtering ) {
						// actionlog 시간차이로 분 지나면 처리하기
						Date sendDate = new Date();
						try {
							sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( row.getSendDate() ).getTime() );
							
							if ( row.getOrdPcode()!=null && row.getOrdPcode().length()>20 ) {
								row.setOrdPcode( row.getOrdPcode().substring(0, 20));
							}
						} catch (Exception e) {		}
						
						if(row!=null) {
							logger.debug("ConvAll {}", row);
							
							intoFiltering.add(row);
						}
					}
					
					workingKey.add( _id, 3 );
					workQueue.execute(new TaskData(G.convAll_info, _id, intoFiltering));
				}
			}
		}

//		if( listConvAllData.size()>0 ) {
//			logger.info("listConvAllData.size - {}", listConvAllData.size());
//			
//			if ( workingKey.contains(_id) ) {
//				logger.info("workingKey.contains - {}", _id);
//				
//				for( ConvData row : listConvAllData ) {
//					sumObjectManager.appendConvAllData(row);
//				}
//			} else {
//				List<ConvData> intoFiltering = new ArrayList();
//				for( ConvData row : listConvAllData ) {
//					// actionlog 시간차이로 분 지나면 처리하기
//					Date sendDate = new Date();
//					try {
//						sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( row.getSendDate() ).getTime() );
//						
//						if ( row.getOrdPcode()!=null && row.getOrdPcode().length()>20 ) {
//							row.setOrdPcode( row.getOrdPcode().substring(0, 20));
//						}
//					} catch (Exception e) {		}
//					
//					if(row!=null) {
//						logger.debug("ConvAll {}", row);
//						
//						intoFiltering.add(row);
//					}
//				}
//				
//				workingKey.add( _id, 3 );
//				workQueue.execute(new TaskData(G.convAll_info, _id, intoFiltering));
//			}
//		}

		workingKey.remove("main");
	}
	
	public void mongoToMariaV3(TaskData taskData) {
		
		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}, maxRetrycnt-{}", taskData.getRetryCnt(), taskData.getId(), retryConfig.maxRetryCount);
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = ConvAllDataToMariaDB.intoMariaConvAllDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), false);
			} else {
				result = ConvAllDataToMariaDB.intoMariaConvAllDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), true);
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
