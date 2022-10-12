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
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ActionPcodeDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskActionPcodeData {

	private static final Logger	logger	= LoggerFactory.getLogger(TaskActionPcodeData.class);

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("ActionPcodeDataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	@Autowired
	private ActionPcodeDataToMariaDB		ActionPcodeDataToMariaDB;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	

	private static int				threadCnt	= 0;
	public static void setThreadCnt(int threadCnt) {
		TaskActionPcodeData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskActionPcodeData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskActionPcodeData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskActionPcodeData.threadCnt;
	}


	public void mongoToFileActionPcodeData() {
		List<ActionLogData> listActionPcodeData = (List<ActionLogData>) sumObjectManager.removeActionPcodeLogData();
		ArrayList<ActionLogData> listWriteData = new ArrayList();
		if( listActionPcodeData!=null && listActionPcodeData.size()>0 ) {
			try {
				for( ActionLogData row : listActionPcodeData ) {
					listWriteData.add(row);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", listActionPcodeData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.action_pcode_data, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaActionPcodeDataV3(){
		logger.info(">> START mongoToMariaActionPcodeDataV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		List<ActionLogData> sumListActionPcodeData = (List<ActionLogData>) sumObjectManager.removeActionPcodeLogData();
		List<ActionLogData> listActionPcodeData = new ArrayList<ActionLogData>(); 
		
//		String _id = "ActionPcodeData";
		String _id=Math.random()+"";

		if( sumListActionPcodeData.size()>0 ) {
			logger.info("sumListActionPcodeData.size - {}", sumListActionPcodeData.size());
			
			try {
				for (ActionLogData row : sumListActionPcodeData) {
					// 액션로그는 날짜지나면 삭제
					Date sdate = null;
					try {
						sdate = new SimpleDateFormat("yyyyMMdd").parse(row.getYyyymmdd());
					} catch (ParseException e) {
					}
					Date edate = new Date();
					edate.setTime( ( new Date().getTime() + (1000L*60*60*24* (60 * -1)) ) );
					if( sdate.getTime() < edate.getTime() ) {
						logger.error("ActionPcodeData over date - {}", row);
						continue;
					}
					
					if( listActionPcodeData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendData(row, false);
					} else {
						listActionPcodeData.add(row);
						logger.debug("item.getValue() {}", row);
					}
				}
				logger.info("listActionPcodeData.size {}", listActionPcodeData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", sumListActionPcodeData.size(), e);
			}

			
			if ( workingKey.contains(_id) ) {
				logger.info("workingKey.contains - {}", _id);
				
				for( ActionLogData row : listActionPcodeData ) {
					sumObjectManager.appendData(row, false);
				}
			} else {
				workingKey.add( _id, 3 );
				//workQueue.execute(new RetryTaskerV3(listActionPcodeData));
				workQueue.execute(new TaskData(G.action_pcode_data, _id, listActionPcodeData));
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
				result = ActionPcodeDataToMariaDB.intoMariaActionPcodeDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), false);
			} else {
				result = ActionPcodeDataToMariaDB.intoMariaActionPcodeDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), true);
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
