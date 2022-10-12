package com.mobon.billing.subjectCopy.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.subjectCopy.config.RetryConfig;
import com.mobon.billing.subjectCopy.service.ClickViewDataToClickHouse;
import com.mobon.billing.subjectCopy.service.SumObjectManager;
import com.mobon.billing.subjectCopy.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;


@Component
public class TaskSubjectCopyClickViewData {

	private static final Logger logger = LoggerFactory.getLogger(TaskSubjectCopyClickViewData.class);

	@Autowired
	private ClickViewDataToClickHouse clickViewDataToClickHouse;
	
	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("SubjectCopyClickViewWorkQueue")
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
		TaskSubjectCopyClickViewData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskSubjectCopyClickViewData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskSubjectCopyClickViewData.threadCnt--;
	}

	public static int getThreadCnt() {	
		return TaskSubjectCopyClickViewData.threadCnt;
	}

	public void mongoToFileCopyCateClickView() {
		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeSubjectCopyClickViewData();
		ArrayList<BaseCVData> listWriteData = new ArrayList();
		if(summeryBaseCVData!=null && summeryBaseCVData.entrySet()!=null) {
			try {
				for (Entry<String, BaseCVData> item : summeryBaseCVData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryBaseCVData.toString());
			}

			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, listWriteData);
				} catch (IOException e) {
				}
			}
		}

	}

	public void mongoToClickHouseSubjectCopyClickViewData() {
		logger.info(">> START mongoToClickHouseCopyCateView V3 THREAD COUNT - {}", threadCnt);

		if ( workingKey.contains("main")) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, BaseCVData> summarySubjectCopyClickViewData = (Map<String, BaseCVData>) sumObjectManager.removeSubjectCopyClickViewData();
		ArrayList <BaseCVData> listSUbjectCopyClickViewData = new ArrayList<BaseCVData>();

		if (summarySubjectCopyClickViewData != null && 
				summarySubjectCopyClickViewData.entrySet() != null) {
			try {
				for (Entry <String, BaseCVData> item : summarySubjectCopyClickViewData.entrySet()) {

					if( listSUbjectCopyClickViewData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendSubjectCopyClickViewData(item.getValue());
					} else {
						listSUbjectCopyClickViewData.add(item.getValue());
					}
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summarySubjectCopyClickViewData.toString());
			}
			logger.debug("listSUbjectCopyClickViewData size {}", listSUbjectCopyClickViewData.size());

		}
		String _id = Math.random()+"";
		if (listSUbjectCopyClickViewData.size() > 0 ) {
			logger.info("filtering {}, {}", _id, listSUbjectCopyClickViewData.size());
			workQueue.execute(new TaskData(G.SubjectCopyClickView, _id, listSUbjectCopyClickViewData));
			
		}
		workingKey.remove("main");

	}

	public void mongoToClickHouseV3(TaskData taskData) {
		
		boolean result = false; 
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = clickViewDataToClickHouse.intoClickHouseClickViewData(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = clickViewDataToClickHouse.intoClickHouseClickViewData(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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
