package com.mobon.billing.viewclicklog.schedule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;

import com.mobon.billing.util.ArrayHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.model.v20.TaskData;
import com.mobon.billing.viewclicklog.config.RetryConfig;
import com.mobon.billing.viewclicklog.service.ConversionVoToClickHouse;
import com.mobon.billing.viewclicklog.service.SumObjectManager;
import com.mobon.billing.viewclicklog.service.WorkQueueTaskData;
import com.mobon.billing.viewclicklog.util.ConsumerFileUtils;
import com.mobon.billing.viewclicklog.util.DateUtils;
import com.mobon.billing.viewclicklog.util.TimeToLiveCollection;

@Component
public class TaskConversionVo {

	private static final Logger logger = LoggerFactory.getLogger(TaskConversionVo.class);
	
	@Autowired
	private ConversionVoToClickHouse ConversionVoToClickHouse;

	@Autowired
	private RetryConfig retryConfig;

	@Autowired
	@Qualifier("ConversionVoWorkQueue")
	private WorkQueueTaskData workQueue;

	@Autowired
	private SumObjectManager sumObjectManager;

	@Value("${log.path}")
	private String logPath;

	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;

	private static TimeToLiveCollection	workingKey = new TimeToLiveCollection();

	private static int threadCnt = 0;

	private boolean closedFlag = false;		// true : 어플리케이션 종료 신호, false : 어플리케이션 신행중 신호

	public static int getThreadCnt() {
		return TaskConversionVo.threadCnt;
	}
	
	public static void setThreadCnt(int threadCnt) {
		TaskConversionVo.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskConversionVo.threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		TaskConversionVo.threadCnt--;
	}

	/**
	 * 데이터를 파일로 저장
	 */
	public void mongoToFileConversionVo() {
		Map<String, ConversionVo> summeryConversionVo = (Map<String, ConversionVo>) sumObjectManager.removeConversionVo();
		ArrayList<ConversionVo> listWriteData = new ArrayList<>();

		if (summeryConversionVo != null) {
			try {
				for (Entry<String, ConversionVo> item : summeryConversionVo.entrySet()) {
					listWriteData.add(item.getValue());
				}
			} catch (ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryConversionVo.toString());
			}

			if (listWriteData.size()>0) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);

				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ConversionVo, listWriteData);
				} catch (IOException e) {
					logger.error("insertIntoError file write err - ", e);
				}
			}
		}
	}

	/**
	 * 데이터를 메인 Queue 로 이동
	 */
	public void mongoToClickHouseConversionVo() {
		long startTaskTime = System.currentTimeMillis();

		logger.debug(">> START mongoToClickHouseConversionMap THREAD COUNT - {}", threadCnt);

		if (workingKey.contains("main")) {
			return;
		}

		workingKey.add("main", 1);

		int singleReAppendSize = 0;
		int listReAppendSize = 0;
		long startRemoveTime = System.currentTimeMillis();
		Map<String, ConversionVo> conversionVoMap = (Map<String, ConversionVo>) sumObjectManager.removeConversionVo();
		logger.info("PERFORMANCE_TEST MAP remove {} (size) / {} (ms)", conversionVoMap.size(), System.currentTimeMillis() - startRemoveTime);

		if (!conversionVoMap.isEmpty()) {
			logger.info("PERFORMANCE_TEST MAP conversionVoMap.size - {}", conversionVoMap.size());

			// TTL 획득하면 데이터 처리
			List<ConversionVo> filtering = new ArrayList<>();
			long startTaskProcessTime = System.currentTimeMillis();

			for (ConversionVo vo : conversionVoMap.values()) {
				// 노출 보다 conversion 이 먼저 인서트되는 현상이 있어서 convDelayTimeMinute(분) 만큼 처리 딜레이
				LocalDateTime sendDateTime = LocalDateTime.now();
				try {
					sendDateTime = LocalDateTime.parse(vo.getSendDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				} catch (DateTimeParseException e) {
					logger.debug("Conv date ParseException");
				}

				LocalDateTime afterDateTime = LocalDateTime.now().minusMinutes(convDelayTimeMinute);

				if (sendDateTime.isBefore(afterDateTime)) {
					// 전송일시가 딜레이를 적용한 일시보다 이전이면 처리한다
					vo.setConvDelayTimeMinute(convDelayTimeMinute);
					filtering.add(vo);
				} else {
					// 전송일시가 딜레이를 적용한 일시보다 이후이면 처리하지 않는다
					singleReAppendSize++;
					sumObjectManager.appendConversionVo(vo);
				}
			}
			logger.info("PERFORMANCE_TEST MAP filtering.size - {}", filtering.size());

			List<List<ConversionVo>> ret = ArrayHelper.split(filtering, 400);
			for (List<ConversionVo> list : ret) {
				try {
					String _id = Math.random() + "";

					logger.info("PERFORMANCE_TEST MAP split list size - {}", list.size());
					workQueue.execute(new TaskData(G.ConversionVo, _id, list));
				} catch (Exception e) {
					for (ConversionVo vo : list) {
						sumObjectManager.appendConversionVo(vo);
					}
					listReAppendSize += list.size();
					logger.info("PERFORMANCE_TEST MAP reAppend size - {}", list.size());
				}
			}
			logger.info("PERFORMANCE_TEST MAP get workingKey singleReAppend {} (size) / listReAppend {} (size) / {} (ms)",
					singleReAppendSize, listReAppendSize, System.currentTimeMillis() - startTaskProcessTime);
		}

		workingKey.remove("main");
		logger.info("PERFORMANCE_TEST MAP task running time - {} (size) / {} (ms)",
				conversionVoMap.size(), System.currentTimeMillis() - startTaskTime);
	}

	/**
	 * 데이터를 ClickHouse에 저장하는데 재처리 한도 초과한 데이터는 파일로 저장
	 */
	public void mongoToClickHouseV3(TaskData taskData) {
		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if (taskData.increaseRetryCnt() < retryConfig.maxRetryCount && !closedFlag) {
				// ClickHouse에 저장
				result = ConversionVoToClickHouse.intoClickHouseConversionVoV3(taskData.getId(), (List<ConversionVo>)taskData.getFiltering(), false);
			} else {
				// insertIntoError 파일로 저장
				result = ConversionVoToClickHouse.intoClickHouseConversionVoV3(taskData.getId(), (List<ConversionVo>)taskData.getFiltering(), true);
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

	/**
	 *
	 * @param flag true : 어플리케이션 종료, false : 어플리케이션 실행중
	 */
	public void changeFlag(boolean flag) {
		closedFlag = flag;
	}


}
