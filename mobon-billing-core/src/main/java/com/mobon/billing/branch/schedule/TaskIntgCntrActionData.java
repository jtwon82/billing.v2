package com.mobon.billing.branch.schedule;

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
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branch.service.IntgCntrActionDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskIntgCntrActionData {

	private static final Logger	logger	= LoggerFactory.getLogger(TaskIntgCntrActionData.class);

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("IntgCntrActionDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private IntgCntrActionDataToMariaDB		IntgCntrActionDataToMariaDB;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	

	private static int				threadCnt	= 0;
	public static void setThreadCnt(int threadCnt) {
		TaskIntgCntrActionData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskIntgCntrActionData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskIntgCntrActionData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskIntgCntrActionData.threadCnt;
	}


	public void mongoToFileIntgCntrActionData() {
		List<ActionLogData> listIntgCntrActionData = (List<ActionLogData>) sumObjectManager.removeIntgCntrActionLogData();
		ArrayList<ActionLogData> listWriteData = new ArrayList();
		if( listIntgCntrActionData!=null && listIntgCntrActionData.size()>0 ) {
			try {
				for( ActionLogData row : listIntgCntrActionData ) {
					listWriteData.add(row);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", listIntgCntrActionData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrAction_data, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaIntgCntrActionDataV3(){
		logger.info(">> START mongoToMariaIntgCntrActionDataV3");

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ActionLogData> sumListIntgCntrActionData = (List<ActionLogData>) sumObjectManager.removeIntgCntrActionLogData();
		List<ActionLogData> listIntgCntrActionData = new ArrayList<ActionLogData>(); 
		
//		String _id = "IntgCntrActionData";
		String _id=Math.random()+"";

		if( sumListIntgCntrActionData.size()>0 ) {
			logger.info("sumListIntgCntrActionData.size - {}", sumListIntgCntrActionData.size());
			
			try {
				for (ActionLogData row : sumListIntgCntrActionData) {
					// 액션로그는 날짜지나면 삭제
					Date sdate = null;
					try {
						sdate = new SimpleDateFormat("yyyyMMdd").parse(row.getYyyymmdd());
					} catch (ParseException e) {
					}
					Date edate = new Date();
					edate.setTime( ( new Date().getTime() + (1000L*60*60*24* (60 * -1)) ) );
					if( sdate.getTime() < edate.getTime() ) {
						logger.error("IntgCntrActionData over date - {}", row);
						continue;
					}
					
					if( listIntgCntrActionData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendIntgCntrActionLogData(row);
					} else {
						listIntgCntrActionData.add(row);
						logger.debug("item.getValue() {}", row);
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", sumListIntgCntrActionData.size(), e);
			}

			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ActionLogData row : listIntgCntrActionData ) {
					sumObjectManager.appendIntgCntrActionLogData(row);
				}
			} else {
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.IntgCntrAction_data, _id, listIntgCntrActionData));
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
				result = IntgCntrActionDataToMariaDB.intoMariaIntgCntrActionDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), false);
			} else {
				result = IntgCntrActionDataToMariaDB.intoMariaIntgCntrActionDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), true);
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
