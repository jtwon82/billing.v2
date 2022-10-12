package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
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
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ClickViewPcodeDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.ViewPcodeDataToMariaDB;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskViewPcodeData {
	
	private static final Logger		logger		= LoggerFactory.getLogger(TaskViewPcodeData.class);

	@Autowired
	private SelectDao				selectDao;
	
	@Autowired
	private ViewPcodeDataToMariaDB 	viewPcodeDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("ClickViewPcodeDataWorkQueue")
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
		TaskViewPcodeData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskViewPcodeData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskViewPcodeData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskViewPcodeData.threadCnt;
	}
	
	//mongoTOFileViewPcode
	
	public void mongoToFileViewPcode() {
		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeViewPcodeDataObjectMap();
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
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_pcode, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaViewPcodeV3() {
		logger.info(">> START mongoToMariaViewV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, BaseCVData> summeryBaseCVData =  (Map<String, BaseCVData>) sumObjectManager.removeViewPcodeDataObjectMap();
		
		ArrayList<BaseCVData> listBaseCVData = new ArrayList();
		if(summeryBaseCVData!=null && summeryBaseCVData.entrySet()!=null) {
			Iterator<Entry<String, BaseCVData>> it = summeryBaseCVData.entrySet().iterator();
			while (it.hasNext()) {
				try {
					Entry<String, BaseCVData> Titem = it.next();
					if (Titem != null) {
						BaseCVData item = Titem.getValue();
						if (item != null) {
							// 파티션때문에
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.DATE, -14);
							String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
							String yyyymmdd = item.getYyyymmdd();
							if (Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd)) {
								logger.info("chking item have not partition - {}", item.generateKey());
								continue;
							}
							listBaseCVData.add(item);
						}
					}
				} catch (ConcurrentModificationException e) {
					logger.error("ConcurrentModificationException ", e);
				}
			}
			logger.info("listBaseCVData.size:{}", listBaseCVData.size());
		
//			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
//			
//			for (String group : group_key) {
//				String _id = Arrays.asList(group).toString();
//				
//				List<BaseCVData> filtering = (List<BaseCVData>) listBaseCVData.stream()
//						.filter(row -> _id.equals( row.getGroupingAlgo() ) )	// scriptNo % 10
//						.collect(Collectors.toList());
//				
//				if (filtering.size()> 0) {
//					logger.info("filtering {}, {}", _id, filtering.size());
//					
//					if ( workingKey.contains(_id) ) {
//						logger.info("workingKey.contains - {}, listBaseCVData.size - {}", _id, listBaseCVData.size());
//						
//						for( BaseCVData row : filtering ) {
//							sumObjectManager.appendViewPcodeData(row);
//						}
//					} else {
//						workingKey.add( _id, 3 );
//						workQueue.execute(new TaskData(G.view_pcode, _id, filtering));
//					}
//				}
//				
//			}

			List<List<BaseCVData>> ret = this.split(listBaseCVData, 300);
			try {
				
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					logger.info("view_pcodeList Size ::: {}", list.size());
					workQueue.execute(new TaskData(G.view_pcode, String.valueOf(i), list));
				}
			}catch (Exception e) {
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					for( BaseCVData row : list ) {
						sumObjectManager.appendViewPcodeData(row);
					}
				}
				logger.error("err ", e);
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
				result = viewPcodeDataToMariaDB.intoMariaViewPcodeDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = viewPcodeDataToMariaDB.intoMariaViewPcodeDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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

	public static <T> List<List<T>> split(List<T> resList, int count) {
		if (resList == null || count <1)
			return null;
		List<List<T>> ret = new ArrayList<List<T>>();
		int size = resList.size();
		if (size <= count) {
			// 데이터 부족 count 지정 크기
			ret.add(resList);
		} else {
			int pre = size / count;
			int last = size % count;
			// 앞 pre 개 집합, 모든 크기 다 count 가지 요소
			for (int i = 0; i <pre; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j <count; j++) {
					itemList.add(resList.get(i * count + j));
				}
				ret.add(itemList);
			}
			// last 진행이 처리
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i <last; i++) {
					itemList.add(resList.get(pre * count + i));
				}
				ret.add(itemList);
			}
		}
		return ret;
	}
}
