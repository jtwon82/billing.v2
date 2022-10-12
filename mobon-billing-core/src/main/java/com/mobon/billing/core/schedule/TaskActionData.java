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

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ActionDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskActionData {

	private static final Logger	logger	= LoggerFactory.getLogger(TaskActionData.class);

	@Autowired
	private RetryConfig				retryConfig;
	@Autowired
	private RetryTemplate			retryTemplate;

	@Autowired
	@Qualifier("ActionDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ActionDataToMariaDB		actionDataToMariaDB;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	

	private static int				threadCnt	= 0;
	public static void setThreadCnt(int threadCnt) {
		TaskActionData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskActionData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskActionData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskActionData.threadCnt;
	}


	public void mongoToFileActionData() {
		List<ActionLogData> listActionData = (List<ActionLogData>) sumObjectManager.removeObjectList(new ActionLogData());
		ArrayList<ActionLogData> listWriteData = new ArrayList();
		if( listActionData!=null && listActionData.size()>0 ) {
			try {
				for( ActionLogData row : listActionData ) {
					listWriteData.add(row);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", listActionData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.action_data, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaActionDataV3(){
		logger.info(">> START mongoToMariaActionDataV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ActionLogData> sumListActionData = (List<ActionLogData>) sumObjectManager.removeObjectList(new ActionLogData());
		List<ActionLogData> listActionData = new ArrayList<ActionLogData>(); 
		
//		String _id = "actionData";
		String _id=Math.random()+"";

		if( sumListActionData.size()>0 ) {
			logger.info("sumListActionData.size - {}", sumListActionData.size());
			
			try {
				for (ActionLogData row : sumListActionData) {
					// 액션로그는 날짜지나면 삭제
					Date sdate = null;
					try {
						sdate = new SimpleDateFormat("yyyyMMdd").parse(row.getYyyymmdd());
					} catch (ParseException e) {
					}
					Date edate = new Date();
					edate.setTime( ( new Date().getTime() + (1000L*60*60*24* (60 * -1)) ) );
					if( sdate.getTime() < edate.getTime() ) {
						logger.error("actionData over date - {}", row);
						continue;
					}
					
					if( listActionData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendData(row, false);
					} else {
						listActionData.add(row);
						logger.debug("item.getValue() {}", row);
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", sumListActionData.size(), e);
			}

			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ActionLogData row : listActionData ) {
					sumObjectManager.appendData(row, false);
				}
			} else {
				workingKey.add( _id, 3 );
				//workQueue.execute(new RetryTaskerV3(listActionData));
				workQueue.execute(new TaskData(G.action_data, _id, listActionData));
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
				result = actionDataToMariaDB.intoMariaActionDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), false);
			} else {
				result = actionDataToMariaDB.intoMariaActionDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), true);
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
