package com.mobon.billing.branchUM.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
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
import com.mobon.billing.branchUM.config.RetryConfig;
import com.mobon.billing.branchUM.service.KnoKpiDataToMariaDB;
import com.mobon.billing.branchUM.service.SumObjectManager;
import com.mobon.billing.branchUM.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskKnoKpiData {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskKnoKpiData.class);
	
	@Autowired
	private KnoKpiDataToMariaDB  knoKpiDataTOMariaDB;
	
	@Autowired
	private RetryConfig				retryConfig;
	
	@Autowired
	@Qualifier("KnoKpiDataWorkQueue")
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
		TaskKnoKpiData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskKnoKpiData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskKnoKpiData.threadCnt--;
	}	
	public static int getThreadCnt() {	
		return TaskKnoKpiData.threadCnt;
	}
	
	public void mongoToMariaKnoKpiNoV3() {
		logger.info(">> START mongoToMariaKnoKpiNoV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeKnoKpiMap();
		ArrayList<BaseCVData> listKnoKpiData = new ArrayList();
		if(summeryBaseCVData!=null && summeryBaseCVData.entrySet()!=null) {
			Iterator<Entry<String, BaseCVData>> it = summeryBaseCVData.entrySet().iterator();
			while (it.hasNext()) {
				try {
					Entry<String, BaseCVData> Titem = it.next();
					if (Titem != null) {
						BaseCVData item = Titem.getValue();
						if (item != null) {
							 //파티션때문에
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.DATE, -14);
							String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
							String yyyymmdd = item.getYyyymmdd();
							if (Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd)) {
								continue;
							}
							/*if(item.getLoopCnt()<3) {
								sumObjectManager.appendKnoKpiData(item);
							}
							else {*/
								logger.debug("loopCnt {}", item.getLoopCnt());
								listKnoKpiData.add(item);
						//	}
						}
					}
				} catch (ConcurrentModificationException e) {
					logger.error("ConcurrentModificationException ", e);
				}
			}
			
//			String[] group_key = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
//					"16", "17", "18", "19", "20", "21", "22","23","24","25", "26","27","28","29"  };
			
			List<List<BaseCVData>> ret = this.split(listKnoKpiData, 500);

			try {
				
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					logger.info("KpiDatalist Size ::: {}", list.size());
					workQueue.execute(new TaskData(G.KnoKpiData, String.valueOf(i), list));
				}
			}catch (Exception e) {
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					for( BaseCVData row : list ) {
						sumObjectManager.appendKnoKpiData(row);
					}
				}
				logger.error("err ", e);
			}
			
//			
//			for (String group : group_key) {
//				String _id = Arrays.asList(new String[] { group }).toString();
//
//				List<BaseCVData> filtering = (List<BaseCVData>) listKnoKpiData.stream()
//						.filter(row -> _id.equals(row.getKpiGroupingSeq() ))
//						.collect(Collectors.toList());
//				
//				if( filtering.size()>0 ) {
//					logger.info("filtering {}, {}", _id, filtering.size());
//					
//					try {
//						if ( workingKey.contains(_id) ) {
//							logger.info("workingKey.contains - {}, listKnoKpiData.size - {}", _id, listKnoKpiData.size());
//							
//							for( BaseCVData row : filtering ) {
//								sumObjectManager.appendKnoKpiData(row);
//							}
//						} else {
//							workingKey.add( _id, 3 );
//
//							workQueue.execute(new TaskData(G.KnoKpiData, _id, filtering));
//							
////							if( 10000>filtering.size() ) {
////								workQueue.execute(new TaskData(G.KnoKpiData, _id, filtering));
////								
////							} else {
////								List<BaseCVData> intoFiltering = new ArrayList();
////								int cnt=0;
////								for( BaseCVData row : filtering ) {
////									if( 10000>cnt++ ) {
////										intoFiltering.add(row);
////									}else {
////										sumObjectManager.appendKnoKpiData(row);
////									}
////								}
////								workQueue.execute(new TaskData(G.KnoKpiData, _id, intoFiltering));
////							}
//						}
//					}catch(Exception e) {
//						for( BaseCVData row : filtering ) {
//							sumObjectManager.appendKnoKpiData(row);
//						}
		//				logger.error("err ");
				//	}
		//		}
//			}
		}
		workingKey.remove("main");
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
	
	public void mongoToMariaV3(TaskData taskData) {
		boolean result = false;
		
		increaseThreadCnt();
		
		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = knoKpiDataTOMariaDB.intoMariaKnoKpiDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = knoKpiDataTOMariaDB.intoMariaKnoKpiDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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
			logger.info("retry size {}", taskData.getFiltering().size());
		}
		
	}
	
	
}
