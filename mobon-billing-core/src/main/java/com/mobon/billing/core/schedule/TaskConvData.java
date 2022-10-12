package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.mobon.billing.core.service.ConvDataToMariaDB;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskConvData {

	private static final Logger		logger	= LoggerFactory.getLogger(TaskConvData.class);

	@Autowired
	private RetryConfig				retryConfig;
	@Autowired
	private RetryTemplate			retryTemplate;
	@Autowired
	private DataBuilder			dataBuilder;


	@Autowired
	@Qualifier("ConvDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ConvDataToMariaDB		convDataToMariaDB;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;
	
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection(); 
	
	private static int				threadCnt	= 0;

	public static void setThreadCnt(int threadCnt) {
		TaskConvData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskConvData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskConvData.threadCnt--;
	} 
	
	public static int getThreadCnt() {	
		return TaskConvData.threadCnt;
	}



	public void mongoToFileConvData() {
		List<ConvData> summeryConvData = (List<ConvData>) sumObjectManager.removeObjectList(new ConvData());
		
		ArrayList<ConvData> listWriteData = new ArrayList();
		if(summeryConvData!=null && summeryConvData.size()>0 ) {
			logger.info("Conv summeryConvData - {}", summeryConvData.size());
			
			try {
				for (ConvData item : summeryConvData) {
					listWriteData.add(item);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryConvData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				logger.info("Conv listWriteData - {}", listWriteData.size());
				
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.conv_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaConvDataV3() {
		logger.info(">> START mongoToMariaConvDataV3");

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ConvData> listConvData = (List<ConvData>) sumObjectManager.removeObjectList(new ConvData());
		
		String _id = "convData";

		logger.info("listConvData.size - {}", listConvData.size());
		
		if( listConvData.size()>0 ) {
			logger.info("listConvData.size - {}", listConvData.size());
			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ConvData row : listConvData ) {
					sumObjectManager.appendData(row, false);
				}
			} else {
				List<ConvData> intoFiltering = new ArrayList();
				
				for( ConvData row : listConvData ) {
					// actionlog 시간차이로 분 지나면 처리하기
					Date sendDate = new Date();
					try {
						sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( row.getSendDate() ).getTime() );
					} catch (ParseException e) {		}
					
					Date afterDate = new Date();
					afterDate.setTime( (new Date().getTime() + (1000L * 60 * (convDelayTimeMinute*-1) )));
					
					if( sendDate.getTime() < afterDate.getTime() ) {
						int result = dataBuilder.dumpConvLogData(row);
						if( result==1 ){
							//logger.info("Conv nothing actionlog row - {}", row);
							
						} else {
							intoFiltering.add(row);
						}

					}else {
						sumObjectManager.appendData(row, false);
					}
				}
				
				workingKey.add( _id, 3 );
				//workQueue.execute(new RetryTaskerV3(listConvData));
				workQueue.execute(new TaskData(G.conv_info, _id, intoFiltering));
			}
		}

		workingKey.remove("main");
	}
	
	public void mongoToMariaV3(TaskData taskData) {
		
		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}, maxRetrycnt-{}", taskData.getRetryCnt(), taskData.getId(), retryConfig.maxRetryCount);
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = convDataToMariaDB.intoMariaConvDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), false);
			} else {
				result = convDataToMariaDB.intoMariaConvDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), true);
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
