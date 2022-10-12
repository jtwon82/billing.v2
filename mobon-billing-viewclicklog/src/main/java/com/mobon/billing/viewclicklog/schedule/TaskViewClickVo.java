package com.mobon.billing.viewclicklog.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
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
import com.mobon.billing.model.v20.TaskData;
import com.mobon.billing.model.v20.ViewClickVo;
import com.mobon.billing.viewclicklog.config.RetryConfig;
import com.mobon.billing.viewclicklog.service.SumObjectManager;
import com.mobon.billing.viewclicklog.service.ViewClickVoToClickHouse;
import com.mobon.billing.viewclicklog.service.WorkQueueTaskData;
import com.mobon.billing.viewclicklog.util.ConsumerFileUtils;
import com.mobon.billing.viewclicklog.util.DateUtils;
import com.mobon.billing.viewclicklog.util.TimeToLiveCollection;

@Component
public class TaskViewClickVo {

	private static final Logger logger = LoggerFactory.getLogger(TaskViewClickVo.class);
	
	@Autowired
	private ViewClickVoToClickHouse ViewClickVoToClickHouse;

	@Autowired
	private RetryConfig retryConfig;

	@Autowired
	@Qualifier("ViewClickVoWorkQueue")
	private WorkQueueTaskData workQueue;

	@Autowired
	private SumObjectManager sumObjectManager;

	@Value("${log.path}")
	private String logPath;

	@Value("${batch.list.size}")
	private String batchListSize;
	
	private static TimeToLiveCollection	workingKey = new TimeToLiveCollection();
	
	private static int threadCnt = 0;

	public static int getThreadCnt() {
		return TaskViewClickVo.threadCnt;
	}
	
	public static void setThreadCnt(int threadCnt) {
		TaskViewClickVo.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskViewClickVo.threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		TaskViewClickVo.threadCnt--;
	}

	/**
	 * 데이터를 파일로 저장
	 */
	public void mongoToFileViewClickVo() {
		Map<String, ViewClickVo> summeryViewClickVo = (Map<String, ViewClickVo>) sumObjectManager.removeViewClickVo();
		ArrayList<ViewClickVo> listWriteData = new ArrayList();

		if (summeryViewClickVo != null) {
			try {
				for (Entry<String, ViewClickVo> item : summeryViewClickVo.entrySet()) {
					listWriteData.add(item.getValue());
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryViewClickVo.toString());
			}
			
			if (listWriteData.size()>0) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);

				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ViewClickVo, listWriteData);
				} catch (IOException e) {
					logger.error("insertIntoError file write err - ", e);
				}
			}
		}
	}

	/**
	 * 데이터를 메인 Queue 로 이동
	 */
	public void mongoToClickHouseViewClickVo() {
		logger.debug(">> START mongoToClickHouseViewClickVo THREAD COUNT - {}", threadCnt);
		
		if (workingKey.contains("main")) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, ViewClickVo> summeryViewClickVo = (Map<String, ViewClickVo>) sumObjectManager.removeViewClickVo();
		ArrayList<ViewClickVo> listViewClickVo = new ArrayList<>();

		if (summeryViewClickVo != null) {
			try {
				// 지정해둔 크기보다 크지 않도록 처리할 데이터를 list 에 넣는다
				for (Entry<String, ViewClickVo> item : summeryViewClickVo.entrySet()) {
					if (listViewClickVo.size() >= Integer.parseInt(batchListSize)) {
						sumObjectManager.appendViewClickVo(item.getValue());
					} else {
						listViewClickVo.add(item.getValue());
					}
				}

				logger.info("listViewClickVo.size {}", listViewClickVo.size());
				
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryViewClickVo);
			}
			
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9"
					, "10","11","12","13","14","15","16","17","18","19"};

			// 그룹명에따라 데이터 묶음
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				
				List<ViewClickVo> filtering = (List<ViewClickVo>) listViewClickVo.stream()
						.filter(row -> _id.equals(row.getGrouping()))
						.collect(Collectors.toList());
				
				if (filtering.size()==0) {
					logger.debug("filtering empty _id {}", _id);
					
				} else {
					logger.debug("filtering {}, {}", _id, filtering.size());
					
					if (workingKey.contains(_id)) {
						logger.debug("workingKey.contains - {}, listViewClickVo.size - {}", _id, listViewClickVo.size());
						
						for (ViewClickVo row : filtering) {
							sumObjectManager.appendViewClickVo(row);
						}
					} else {
						// TTL 설정
						workingKey.add(_id, 3);
						workQueue.execute(new TaskData(G.ViewClickVo, _id, filtering));
					}

				}
			}
		}

		workingKey.remove("main");
	}

	/**
	 * 데이터를 ClickHouse에 저장하는데 재처리 한도 초과한 데이터는 파일로 저장
	 */
	public void mongoToClickHouseV3(TaskData taskData) {
		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if (taskData.increaseRetryCnt() < retryConfig.maxRetryCount) {
				// ClickHouse에 저장
				result = ViewClickVoToClickHouse.intoClickHouseViewClickVoV3(taskData.getId(), (List<ViewClickVo>)taskData.getFiltering(), false);
			} else {
				// insertError 파일로 저장
				result = ViewClickVoToClickHouse.intoClickHouseViewClickVoV3(taskData.getId(), (List<ViewClickVo>)taskData.getFiltering(), true);
			}
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
		}
		
		decreaseThreadCnt();
		
		if (result) {
			int i=3;

			while (!workingKey.remove(taskData.getId())) {
				if (--i<0) {
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
