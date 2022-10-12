package com.mobon.billing.subjectCopy.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.subjectCopy.config.RetryConfig;
import com.mobon.billing.subjectCopy.service.ConvDataToClickHouse;
import com.mobon.billing.subjectCopy.service.SumObjectManager;
import com.mobon.billing.subjectCopy.service.WorkQueueTaskData;
import com.mobon.billing.subjectCopy.vo.ConversionPolling;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;


@Component
public class TaskSubjectCopyConvData {
	private static final Logger logger = LoggerFactory.getLogger(TaskSubjectCopyConvData.class);

	@Autowired
	private ConvDataToClickHouse convDataToClickHouse;

	@Autowired
	private RetryConfig retryConfig;

	@Autowired
	@Qualifier("SubjectCopyConvWorkQueue")
	private WorkQueueTaskData workQueue;

	@Autowired
	private SumObjectManager sumObjectManager;

	private static TimeToLiveCollection workingKey = new TimeToLiveCollection();


	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;

	private static int 			threadCnt = 0;

	public static void setThreadCnt(int threadCnt) {
		TaskSubjectCopyConvData.threadCnt= threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskSubjectCopyConvData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskSubjectCopyConvData.threadCnt--;
	} 

	public static int getThreadCnt() {	
		return TaskSubjectCopyConvData.threadCnt;
	}


	public void mongoToFileSubjectCopyConvData() {
		List<ConversionPolling> summerySubjectCopyConvData = (List<ConversionPolling>) sumObjectManager.removeSubjectCopyConvData();

		ArrayList <ConversionPolling> listWriteData = new ArrayList();
		if (summerySubjectCopyConvData != null && summerySubjectCopyConvData.size() > 0) {
			logger.info("Conversion summerySubjectCopyConvData - {}", summerySubjectCopyConvData.size());
			try {
				for (ConversionPolling item : summerySubjectCopyConvData) {
					listWriteData.add(item);
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summerySubjectCopyConvData.toString());
			}

			if ( listWriteData.size() > 0) {
				logger.info("Conversion listWriteData  - {} ", listWriteData.size());

				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.SuccConversion, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}

	public void mongoToClickHouseSubjectCopyConvData() {
		logger.info(">> START mongoToClickHouseConvData");

		if (workingKey.contains("main")) {
			return ;
		}
		workingKey.add("main", 1);

		List<ConversionPolling> listSubjectCopyConvData = (List<ConversionPolling>) sumObjectManager.removeSubjectCopyConvData();

		logger.info("listSubjectCopyConvData size - {}", listSubjectCopyConvData.size());

		String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
		for (String group : group_key) {
			String _id = Arrays.asList(group).toString();
			List<ConversionPolling> filtering = (List<ConversionPolling>) listSubjectCopyConvData.stream()
					.filter(row -> _id.equals( String.format("[%s]", Math.abs(row.getAdverId().hashCode() % 20))  ) )
					.collect(Collectors.toList());
			if (filtering.size() > 0 ) {
				logger.info("filtering {}, {}", _id , filtering.size());

				if (workingKey.contains(_id)) {
					logger.info("workingKey contains - {}", _id);
					for (ConversionPolling row : filtering) {
						sumObjectManager.appendSubjectCopyConvData(row);
					}
				} else {
					List<ConversionPolling> intoFiltering = new ArrayList<ConversionPolling>();
					for (ConversionPolling row : filtering) {
						if (row != null) {
							logger.debug("SubjectCopyConversion {} ", row);
							intoFiltering.add(row);
						}
					}
					workingKey.add(_id, 3);
					workQueue.execute(new TaskData(G.SuccConversion, _id , intoFiltering));
				}
			}

		}
		workingKey.remove("main");
	}

	public void mongoToClickHouseV3(TaskData taskData) {
		boolean result = false;

		increaseThreadCnt();

		logger.info("retryCnt - {} , _id - {}, maxRetrycnt - {}", taskData.getRetryCnt(), taskData.getId(), retryConfig.maxRetryCount);

		try {
			if (taskData.increaseRetryCnt() < retryConfig.maxRetryCount) {
				result = convDataToClickHouse.intoClickHouseConvDataV3(taskData.getId(), (List<ConversionPolling>) taskData.getFiltering(), false);
			} else {
				result = convDataToClickHouse.intoClickHouseConvDataV3(taskData.getId(), (List<ConversionPolling>) taskData.getFiltering(), true);
			}

		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId() , e);
		}

		decreaseThreadCnt();

		if (result) {
			int i = 3;
			while (!workingKey.remove(taskData.getId())) {
				if (--i < 0) {
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
