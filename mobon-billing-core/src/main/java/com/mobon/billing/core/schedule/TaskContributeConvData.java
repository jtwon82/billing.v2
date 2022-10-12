package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ContributeConvDataToMariaDB;
import com.mobon.billing.core.service.ContributeConvDataToMariaDB;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskContributeConvData {
	private static final Logger		logger	= LoggerFactory.getLogger(TaskContributeConvData.class);

	@Autowired
	private RetryConfig				retryConfig;
	@Autowired
	private RetryTemplate			retryTemplate;
	@Autowired
	private DataBuilder			dataBuilder;


	@Autowired
	@Qualifier("ContributeConvDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ContributeConvDataToMariaDB		ContribueteConvDataToMariaDB;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;
	
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection(); 
	
	private static int				threadCnt	= 0;

	public static void setThreadCnt(int threadCnt) {
		TaskContributeConvData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskContributeConvData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskContributeConvData.threadCnt--;
	} 
	
	public static int getThreadCnt() {	
		return TaskContributeConvData.threadCnt;	
	}
	
	public void mongoToFileContributeConvData() {
		List<ConvData> summeryContributeConvData  = (List<ConvData>) sumObjectManager.removeContributeConvData();
		
		ArrayList<ConvData> listWriteData = new ArrayList();
		if(summeryContributeConvData!=null && summeryContributeConvData.size()>0 ) {
			logger.info("Conv summeryConvData - {}", summeryContributeConvData.size());
			
			try {
				for (ConvData item : summeryContributeConvData) {
					listWriteData.add(item);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryContributeConvData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				logger.info("ContributeConvData listWriteData - {}", listWriteData.size());
				
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ContributeConvData, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaContributeConvData() {
		logger.info(">> START mongoToMariaContributeConvDataV3");

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ConvData> listContributeConvData = (List<ConvData>) sumObjectManager.removeContributeConvData();
		
		String _id = "ContributeConvData";

		logger.info("listContributeConvData.size - {}", listContributeConvData.size());
		
		if( listContributeConvData.size()>0 ) {
			logger.info("listContributeConvData.size - {}", listContributeConvData.size());
			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ConvData row : listContributeConvData ) {
					sumObjectManager.appendContributeConversionData(row); //.appendData(row, false);
				}
				
			} else {
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.ContributeConvData, _id, listContributeConvData));
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
				result = ContribueteConvDataToMariaDB.intoMariaContributeConvData(taskData.getId(), (List<ConvData>)taskData.getFiltering(), false);
			} else {
				result = ContribueteConvDataToMariaDB.intoMariaContributeConvData(taskData.getId(), (List<ConvData>)taskData.getFiltering(), true);
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
