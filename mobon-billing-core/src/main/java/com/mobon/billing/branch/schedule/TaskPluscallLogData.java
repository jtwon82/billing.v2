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
import com.mobon.billing.branch.service.PluscallLogDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.PluscallLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

/**
 * TaskPluscallLogData
 * 플러스콜 유효콜 관련 스케쥴러 클래스
 * 
 * @author  : sjpark3
 * @since   : 2022-01-04
 */
@Component
public class TaskPluscallLogData {
	@Autowired
	private PluscallLogDataToMariaDB pluscallLogDataToMariaDB;	
	@Autowired
	private RetryConfig retryConfig;
	@Autowired
	private SumObjectManager sumObjectManager;
	@Autowired
	@Qualifier("PluscallLogDataWorkQueue")
	private WorkQueueTaskData workQueue;
	
	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	private static int threadCnt = 0;
	private static TimeToLiveCollection	workingKey = new TimeToLiveCollection();
	private static final Logger logger = LoggerFactory.getLogger(TaskPluscallLogData.class);
	
	public static int getThreadCnt() {	
		return TaskPluscallLogData.threadCnt;
	}
	
	public static void setThreadCnt(int threadCnt) {
		TaskPluscallLogData.threadCnt = threadCnt;
	}
	
	public static synchronized void increaseThreadCnt() {
		TaskPluscallLogData.threadCnt++;
	}
	
	public static synchronized void decreaseThreadCnt() {
		TaskPluscallLogData.threadCnt--;
	}	
	
	/**
     * mongoToFilePluscallLogData
     * 데이터 파일 출력 메소드
     * 
     * @author  : sjpark3
     * @since   : 2022-01-04
     */
	public void mongoToFilePluscallLogData() {
		List<PluscallLogData> listPluscallLogData = (List<PluscallLogData>) sumObjectManager.removeObjectList(new PluscallLogData());
		ArrayList<PluscallLogData> listWriteData = new ArrayList();
		
		if (listPluscallLogData!=null && listPluscallLogData.size()>0) {
			try {
				for (PluscallLogData row : listPluscallLogData) {
					listWriteData.add(row);
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", listPluscallLogData.toString());
			}
			
			if (listWriteData.size()>0) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.PluscallLogData, listWriteData);
				} catch (IOException e) {
					logger.error("ConcurrentModificationException write file - {}", e);
				}
			}
		}
	}
	
	/**
     * mongoToMariaPluscallLogDataV3
     * 데이터 메모리 적재 메소드
     * 
     * @author  : sjpark3
     * @since   : 2022-01-04
     */
	public void mongoToMariaPluscallLogDataV3() {
		logger.info(">> START mongoToMariaPluscallLogData THREAD COUNT - {}", threadCnt);
		
		if ( workingKey.contains("main") ) {
			return;
		}		
		workingKey.add("main", 1);

		List<PluscallLogData> sumListPluscallLogData = (List<PluscallLogData>) sumObjectManager.removeObjectList(new PluscallLogData());
		List<PluscallLogData> listPluscallLogData = new ArrayList<PluscallLogData>();
		
		String _id = Math.random()+"";
		
		if (sumListPluscallLogData.size() > 0) {
			logger.info("sumListChrgLogData.size - {}", sumListPluscallLogData.size());
			
			try {
				for (PluscallLogData row : sumListPluscallLogData) {
					// 파티션관련 리미트 설정 (2주 = 14일)
					LocalDate rowDate = LocalDate.parse(row.getYyyymmdd(), DateTimeFormatter.ofPattern("yyyyMMdd"));
					
					if (rowDate.isBefore(LocalDate.now().minusWeeks(2))) {
						logger.info("pluscallLog over date - {}", row);
						continue;
					}
					
					if (listPluscallLogData.size() >= Integer.parseInt(batchListSize)) {
						sumObjectManager.appendPluscallLogData(row);
					} else {
						listPluscallLogData.add(row);
						logger.debug("item.getValue() {}", row);
					}
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", sumListPluscallLogData.size(), e);
			}
			
			if (workingKey.contains(_id)) {
				logger.info("workingKey.contains - {}", _id);
				
				for (PluscallLogData row : sumListPluscallLogData) {
					sumObjectManager.appendPluscallLogData(row);
				}
			} else {
				workingKey.add(_id, 3);
				workQueue.execute(new TaskData(G.PluscallLogData, _id, listPluscallLogData));
			}	
		}
		
		workingKey.remove("main");
	}
	
	/**
     * mongoToMariaV3
     * 데이터 MariaDB 적재 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-14
     */
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();		
		logger.info("PluscallLogData retryCnt - {}, _id - {}, size={}", taskData.getRetryCnt(), taskData.getId(), taskData.getFiltering().size());
		
		try {
			if (taskData.increaseRetryCnt() < retryConfig.maxRetryCount) {
				result = pluscallLogDataToMariaDB.intoMariaPluscallLogDataV3(taskData.getId(), (List<PluscallLogData>)taskData.getFiltering(), false);
			} else {
				result = pluscallLogDataToMariaDB.intoMariaPluscallLogDataV3(taskData.getId(), (List<PluscallLogData>)taskData.getFiltering(), true);
			}
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
		}
		
		decreaseThreadCnt();
		
		if (result) {
			int i = 3;
			while (!workingKey.remove( taskData.getId())) {
				if (--i < 0) {
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

