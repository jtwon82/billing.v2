package com.mobon.billing.core.schedule;

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
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ShopStatsInfoDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskShopStatsInfoData {

	private static final Logger			logger	= LoggerFactory.getLogger(TaskShopStatsInfoData.class);

	@Autowired
	private ShopStatsInfoDataToMariaDB	shopStatsInfoDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("ShopStatsInfoDataWorkQueue")
	private WorkQueueTaskData				workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;

	public static void setThreadCnt(int threadCnt) {
		TaskShopStatsInfoData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskShopStatsInfoData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskShopStatsInfoData.threadCnt--;
	}
	public static int getThreadCnt() {	
		return TaskShopStatsInfoData.threadCnt;
	}
	
	
	public void mongoToFileShopStatsInfo() {
		Map<String, ShopStatsInfoData> summeryShopStatsInfoData = (Map<String, ShopStatsInfoData>) sumObjectManager.removeObjectMap(new ShopStatsInfoData());
		ArrayList<ShopStatsInfoData> listWriteData = new ArrayList();
		if(summeryShopStatsInfoData!=null && summeryShopStatsInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ShopStatsInfoData> item : summeryShopStatsInfoData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryShopStatsInfoData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shop_stats, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}

	public void mongoToMariaShopStatsInfoDataV3() {
		logger.info(">> START mongoToMariaShopStatsInfoDataV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		

		Map<String, ShopStatsInfoData> summeryShopStatsInfoData = (Map<String, ShopStatsInfoData>) sumObjectManager.removeObjectMap(new ShopStatsInfoData());
		
		ArrayList<ShopStatsInfoData> listShopStatsInfoData = new ArrayList();
		if(summeryShopStatsInfoData!=null && summeryShopStatsInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ShopStatsInfoData> item : summeryShopStatsInfoData.entrySet()) {
					if( listShopStatsInfoData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendShopStatsData(item.getValue());
					} else {
						listShopStatsInfoData.add(item.getValue());
					}
				}
				logger.info("listShopStatsInfoData.size - {}", listShopStatsInfoData.size());
			}catch(ConcurrentModificationException e) {
				logger.debug("item - {}", summeryShopStatsInfoData.toString());
			}

//			String [][] group_key = new String[][]{ {"M"},{"W"}	};
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
			
//			for (String[] group : group_key) {
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();

				List<ShopStatsInfoData> filtering = (List<ShopStatsInfoData>) listShopStatsInfoData.stream()
						.filter(row -> _id.equals( row.getGrouping() ) )
						.collect(Collectors.toList());
				
				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if (workingKey.contains(_id)) {
						logger.info("workingKey.contains - {}, listShopStatsInfoData.size {}", _id, listShopStatsInfoData.size());
						
						for( ShopStatsInfoData row : filtering ) {
							sumObjectManager.appendShopStatsData(row);
						}
					} else {
						workingKey.add( _id, 3 );
//						workQueue.execute(new TaskData(G.shop_stats, _id, filtering));
						
//						if( 500>filtering.size() ) {
//							workQueue.execute(new TaskData(G.shop_stats, _id, filtering));
//						}
//						else {
//							List<ShopStatsInfoData> intoFiltering = new ArrayList();
//							int cnt=0;
//							for( ShopStatsInfoData row : filtering ) {
//								if( 500>cnt++ ) {
//									intoFiltering.add(row);
//								}else {
//									sumObjectManager.appendData(row, false);
//								}
//							}
//							workQueue.execute(new TaskData(G.shop_stats, _id, intoFiltering));
//						}
						
						List<ShopStatsInfoData> intoFiltering = new ArrayList();
						for( ShopStatsInfoData row : filtering ) {
//							if("smartstore".equals(row.getAppName())) {
//								intoFiltering.add(row);
//								
//							}else if( Math.random()<.6 ) {
//								intoFiltering.add(row);
//								
//							}else {
//								break;
//							}
							intoFiltering.add(row);
						}
						workQueue.execute(new TaskData(G.shop_stats, _id, intoFiltering));
					}
				}
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
				result = shopStatsInfoDataToMariaDB.intoMariaShopStatsInfoDataV3(taskData.getId(), (List<ShopStatsInfoData>)taskData.getFiltering(), false);
			} else {
				result = shopStatsInfoDataToMariaDB.intoMariaShopStatsInfoDataV3(taskData.getId(), (List<ShopStatsInfoData>)taskData.getFiltering(), true);
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
