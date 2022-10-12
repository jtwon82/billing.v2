package com.mobon.billing.branch.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.mobon.billing.branch.service.ChrgLogDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ChrgLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskChrgLogData {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskChrgLogData.class);
	
	@Autowired
	private ChrgLogDataToMariaDB chrgLogDataToMariaDB;
	
	@Autowired
	private RetryConfig retryConfig;
	
	@Autowired
	@Qualifier("ChrgLogDataWorkQueue")
	private WorkQueueTaskData workQueue;
	
	@Autowired
	private SumObjectManager sumObjectManager;
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public static void setThreadCnt(int threadCnt) {
		TaskChrgLogData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskChrgLogData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskChrgLogData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskChrgLogData.threadCnt;
	}
	
	public void mongoToFileChrgLogData() {
		List<ChrgLogData> listChrgLogData = (List<ChrgLogData>) sumObjectManager.removeObjectList(new ChrgLogData());
		ArrayList<ChrgLogData> listWriteData = new ArrayList();
		
		if ( listChrgLogData!=null && listChrgLogData.size()>0 ) {
			try {
				for (ChrgLogData row : listChrgLogData) {
					listWriteData.add(row);
				}
			} catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", listChrgLogData.toString());
			}
			
			if (listWriteData.size()>0) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ChrgLogData, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaChrgLogData() {
		logger.info(">> START mongoToMariaChrgLogData THREAD COUNT - {}", threadCnt);
		
		if ( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		List<ChrgLogData> sumListChrgLogData = (List<ChrgLogData>) sumObjectManager.removeObjectList(new ChrgLogData());
		List<ChrgLogData> listChrgLogData = new ArrayList<ChrgLogData>();
		
		String _id = Math.random()+"";
		
		if (sumListChrgLogData.size() > 0) {
			logger.info("sumListChrgLogData.size - {}", sumListChrgLogData.size());
			
			try {
				for (ChrgLogData row : sumListChrgLogData) {
					// 파티션관련 리미트 설정 (2주 = 14일)
					LocalDate rowDate = LocalDate.parse(row.getYyyymmdd(), DateTimeFormatter.ofPattern("yyyyMMdd"));
					
					if (rowDate.isBefore(LocalDate.now().minusWeeks(2))) {
						logger.info("chrgLog over date - {}", row);
						continue;
					}
					
					if (listChrgLogData.size() >= Integer.parseInt(batchListSize)) {
						sumObjectManager.appendChrgLogData(row);
					} else {
						listChrgLogData.add(row);
						logger.debug("item.getValue() {}", row);
					}
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", sumListChrgLogData.size(), e);
			}
			
			if (workingKey.contains(_id)) {
				logger.info("workingKey.contains - {}", _id);
				
				for (ChrgLogData row : sumListChrgLogData) {
					sumObjectManager.appendChrgLogData(row);
				}
			} else {
				workingKey.add(_id, 3);
				workQueue.execute(new TaskData(G.ChrgLogData, _id, listChrgLogData));
			}	
		}
		
		workingKey.remove("main");
	}
	
	
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("ChrgLogData retryCnt - {}, _id - {}, size={}", taskData.getRetryCnt(), taskData.getId(), taskData.getFiltering().size());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = chrgLogDataToMariaDB.intoMariaChrgLogDataV3(taskData.getId(), (List<ChrgLogData>)taskData.getFiltering(), false);
			} else {
				result = chrgLogDataToMariaDB.intoMariaChrgLogDataV3(taskData.getId(), (List<ChrgLogData>)taskData.getFiltering(), true);
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

