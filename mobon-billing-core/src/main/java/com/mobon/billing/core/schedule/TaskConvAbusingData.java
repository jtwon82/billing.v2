package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ConvAbusingDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskConvAbusingData {

	private static final Logger		logger	= LoggerFactory.getLogger(TaskConvAbusingData.class);

	@Autowired
	private RetryConfig				retryConfig;


	@Autowired
	@Qualifier("ConvAbusingDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ConvAbusingDataToMariaDB		ConvAbusingDataToMariaDB;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;
	
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection(); 
	
	private static int				threadCnt	= 0;

	public static void setThreadCnt(int threadCnt) {
		TaskConvAbusingData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskConvAbusingData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskConvAbusingData.threadCnt--;
	} 
	
	public static int getThreadCnt() {	
		return TaskConvAbusingData.threadCnt;
	}



	public void mongoToFileConvAbusingData() {
		List<ConvData> summeryConvAbusingData = (List<ConvData>) sumObjectManager.removeConvAbusingData();
		
		ArrayList<ConvData> listWriteData = new ArrayList();
		if(summeryConvAbusingData!=null && summeryConvAbusingData.size()>0 ) {
			logger.info("Conv summeryConvAbusingData - {}", summeryConvAbusingData.size());
			
			try {
				for (ConvData item : summeryConvAbusingData) {
					listWriteData.add(item);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryConvAbusingData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				logger.info("Conv listWriteData - {}", listWriteData.size());
				
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.convAbusing_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaConvAbusingDataV3() {
		logger.info(">> START mongoToMariaConvAbusingDataV3");

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ConvData> listConvAbusingData = (List<ConvData>) sumObjectManager.removeConvAbusingData();
		
		String _id = "ConvAbusingData";

		if( listConvAbusingData.size()>0 ) {
			logger.info("listConvAbusingData.size - {}", listConvAbusingData.size());
			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ConvData row : listConvAbusingData ) {
					sumObjectManager.appendConvAbusingData(row, null);
				}
			} else {
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.convAbusing_info, _id, listConvAbusingData));
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
				result = ConvAbusingDataToMariaDB.intoMariaConvAbusingDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), false);
			} else {
				result = ConvAbusingDataToMariaDB.intoMariaConvAbusingDataV3(taskData.getId(), (List<ConvData>)taskData.getFiltering(), true);
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
