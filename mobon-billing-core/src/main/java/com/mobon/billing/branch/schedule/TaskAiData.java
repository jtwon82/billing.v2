package com.mobon.billing.branch.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branch.service.AiDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

/**
 * TaskAiData
 * 캠페인 생성에 Ai 사용여부 관련 스케쥴러 클래스
 * 
 * @author  : sjpark3
 * @since   : 2021-12-14
 */
@Component
public class TaskAiData {
	@Autowired
	private SelectDao selectDao;
	@Autowired
	private AiDataToMariaDB	aiDataToMariaDB;
	@Autowired
	private RetryConfig	retryConfig;
	@Autowired
	private SumObjectManager sumObjectManager;
	@Autowired 
	@Qualifier("AiDataWorkQueue")
	private WorkQueueTaskData workQueue;
	
	@Value("${log.path}")
	private String logPath;
	@Value("${batch.list.size}")
	private String batchListSize;
	
	private static int threadCnt = 0;
	private static TimeToLiveCollection	workingKey = new TimeToLiveCollection();
	private static final Logger	logger = LoggerFactory.getLogger(TaskAiData.class);
	
	public static int getThreadCnt() {
		return TaskAiData.threadCnt;
	}
	
	public static void setThreadCnt(int threadCnt) {
		TaskAiData.threadCnt = threadCnt;
	}
	
	public static synchronized void increaseThreadCnt() {
		TaskAiData.threadCnt++;
	}
	
	public static synchronized void decreaseThreadCnt() {
		TaskAiData.threadCnt--;
	}
	
	/**
     * mongoToFileAi
     * 데이터 파일 출력 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-14
     */
	public void mongoToFileAi() {
		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeAiData();
		ArrayList<BaseCVData> listWriteData = new ArrayList<>();
		
		if (summeryBaseCVData!=null && summeryBaseCVData.entrySet()!=null) {
			try {
				for (Entry<String, BaseCVData> item : summeryBaseCVData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryBaseCVData.toString());
			}
			
			if (listWriteData.size()>0) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.AiData, listWriteData);
				} catch (IOException e) {
					logger.error("ConcurrentModificationException write file - {}", e);
				}
			}
		}
	}
	
	/**
     * mongoToMariaAiV3
     * 데이터 필터링 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-14
     */
	public void mongoToMariaAiV3() {
		logger.info(">> START mongoToMariaAiV3 THREAD COUNT - {}", threadCnt);
		
		if (workingKey.contains("main")) {
			return;
		}		
		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeAiData();
		ArrayList<BaseCVData> listAiData = new ArrayList<>();
		
		if (summeryBaseCVData!=null && summeryBaseCVData.entrySet()!=null) {
			Iterator<Entry<String, BaseCVData>> it = summeryBaseCVData.entrySet().iterator();
			
			// 파티션 체크
			while (it.hasNext()) {
				try {
					Entry<String, BaseCVData> Titem = it.next();
					
					if (Titem != null) {
						BaseCVData item = Titem.getValue();
						
						if (item != null) {							
							// 파티션관련 리미트 설정 (2주)
							LocalDate itemDate = LocalDate.parse(item.getYyyymmdd(), DateTimeFormatter.ofPattern("yyyyMMdd"));
							
							if (itemDate.isBefore(LocalDate.now().minusWeeks(2))) {
								logger.info("aiData over date - {}", item);
								continue;
							}
							
							listAiData.add(item);
						}
					}
				} catch (ConcurrentModificationException e) {
					logger.error("ConcurrentModificationException ", e);
				}
			}
			
			// 필터링
			ArrayList<BaseCVData> list = selectDao.selectAdgubunKey2();
			String [][] group_key = new String [list.size()][];
			int i=0;
			
			for (BaseCVData row : list) {
				group_key[i++] = new String [] { row.getAdGubun(), (row.getScriptNo()%10)+"" };
			}
			
			for (String[] group : group_key) {
				String _id = Arrays.asList(group).toString();
				List<BaseCVData> filtering = (List<BaseCVData>) listAiData.stream()
						.filter(row -> _id.equals( row.getGrouping()))
						.collect(Collectors.toList());
				
				if (filtering.size()>0) {
					logger.info("filtering {}, {}", _id, filtering.size());
										
					if (workingKey.contains(_id)) {
						// 처리가능 상태 확인
						logger.info("workingKey.contains - {}, listAiData.size - {}", _id, listAiData.size());
						
						for (BaseCVData row : filtering) {
							sumObjectManager.appendAiData(row);
						}
					} else {
						workingKey.add(_id, 3);
						
						// 처리가능 크기 확인 및 처리
						if (10000>filtering.size()) {
							workQueue.execute(new TaskData(G.AiData, _id, filtering));								
						} else {
							int cnt=0;
							List<BaseCVData> intoFiltering = new ArrayList<>();
							
							for (BaseCVData row : filtering) {
								if (10000>cnt++) {
									intoFiltering.add(row);
								} else {
									sumObjectManager.appendAiData(row);
								}
							}
							
							workQueue.execute(new TaskData(G.AiData, _id, intoFiltering));
						}
					}
				}
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
		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = aiDataToMariaDB.intoMariaAiDataV3(taskData.getId(), (List<BaseCVData>) taskData.getFiltering(), false);
			} else {
				result = aiDataToMariaDB.intoMariaAiDataV3(taskData.getId(), (List<BaseCVData>) taskData.getFiltering(), true);
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
		} else {
			workQueue.execute(taskData);
			logger.info("retry size {}", taskData.getFiltering().size());
		}
	}
}
