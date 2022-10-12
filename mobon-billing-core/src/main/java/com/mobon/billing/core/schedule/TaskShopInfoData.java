package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ShopInfoDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.util.ArrayHelper;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskShopInfoData {

	private static final Logger		logger	= LoggerFactory.getLogger(TaskShopInfoData.class);

	@Autowired
	private ShopInfoDataToMariaDB	shopInfoDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;
	
	@Autowired
	private RetryTemplate			retryTemplate;

	@Autowired
	@Qualifier("ShopInfoDataWorkQueue")
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
		TaskShopInfoData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskShopInfoData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskShopInfoData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskShopInfoData.threadCnt;
	}
	
	

	public void mongoToFileShopInfo() {
		Map<String, ShopInfoData> summeryShopInfoData = (Map<String, ShopInfoData>) sumObjectManager.removeObjectMap(new ShopInfoData());
		ArrayList<ShopInfoData> listWriteData = new ArrayList();
		if(summeryShopInfoData!=null && summeryShopInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ShopInfoData> item : summeryShopInfoData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryShopInfoData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shop_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}

	public void mongoToMariaShopInfoDataV3() {
		logger.info(">> START mongoToMariaShopInfoDataV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		Map<String, ShopInfoData> summeryShopInfoData = (Map<String, ShopInfoData>) sumObjectManager.removeObjectMap(new ShopInfoData());
		ArrayList<ShopInfoData> listShopInfoData = new ArrayList();
		if(summeryShopInfoData!=null && summeryShopInfoData.entrySet()!=null) {
			try {
				for (Entry<String, ShopInfoData> item : summeryShopInfoData.entrySet()) {
					if( listShopInfoData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendShopInfoData(item.getValue());
					} else {
						listShopInfoData.add(item.getValue());
					}
				}
				logger.info("listShopInfoData.size - {}", listShopInfoData.size());
			}catch(ConcurrentModificationException e) {
				logger.debug("item - {}", summeryShopInfoData.toString());
			}

			List<List<ShopInfoData>> ret= ArrayHelper.split(listShopInfoData, 50);
			try {
				for (int i = 0 ; i < ret.size(); i++) {
					List<ShopInfoData> list = ret.get(i);
					logger.info("ArrayHelper.split Size ::: {}", list.size());
					workQueue.execute(new TaskData(G.shop_info, String.valueOf(i), list));
				}
			}catch (Exception e) {
				for (int i = 0 ; i < ret.size(); i++) {
					List<ShopInfoData> list = ret.get(i);
					for( ShopInfoData row : list ) {
						sumObjectManager.appendShopInfoData(row);
					}
				}
				logger.error("err ", e);
			}
			
//			String [][] group_key = new String[][]{ {"W"},{"M"},{"D"} };
//			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
		
//			for (String[] group : group_key) {
//			for (String group : group_key) {
//				String _id = Arrays.asList(group).toString();
//
//				List<ShopInfoData> filtering = (List<ShopInfoData>) listShopInfoData.stream()
//						.filter(row -> _id.equals( row.getGrouping() ) )
//						.collect(Collectors.toList());
//				
//				if( filtering.size()>0 ) {
//					logger.info("filtering {}, {}", _id, filtering.size());
//					
//					if (workingKey.contains(_id)) {
//						logger.info("workingKey.contains - {}, listShopInfoData.size {}", _id, listShopInfoData.size());
//						
//						for( ShopInfoData row : filtering ) {
//							sumObjectManager.appendShopInfoData(row);
//						}
//					} else {
//						workingKey.add( _id, 3 );
//						//workQueue.execute(new RetryTaskerV3(filtering, group));
//						workQueue.execute(new TaskData(G.shop_info, _id, filtering));
//
////						if( 500>filtering.size() ) {
////							workQueue.execute(new TaskData(G.shop_info, _id, filtering));
////							
////						} else {
////							List<ShopInfoData> intoFiltering = new ArrayList();
////							int cnt=0;
////							for( ShopInfoData row : filtering ) {
////								if( 500>cnt++ ) {
////									intoFiltering.add(row);
////								}else {
////									sumObjectManager.appendShopInfoData(row);
////								}
////							}
////							workQueue.execute(new TaskData(G.shop_info, _id, intoFiltering));
////						}
//					}
//				}
//			}
		}
		workingKey.remove("main");
	}

	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() > 100 ) {
				result = true;
				List<ShopInfoData> list = (List<ShopInfoData>)taskData.getFiltering();
				for ( ShopInfoData row : list ) {
					logger.info("retrycnt - {}, row - {}", taskData.getRetryCnt(), row.toString());
				}
				
			} else if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = shopInfoDataToMariaDB.intoMariaShopInfoDataV3(taskData.getId(), (List<ShopInfoData>)taskData.getFiltering(), false);
			
			} else {
				//result = shopInfoDataToMariaDB.intoMariaShopInfoDataV3(taskData.getId(), (List<ShopInfoData>)taskData.getFiltering(), true);
				
				logger.info("ShopInfo _id - {}, slice list.size() - {}", taskData.getId(), taskData.getFiltering().size());
				for(ShopInfoData row : (List<ShopInfoData>)taskData.getFiltering()) {
					ArrayList list= new ArrayList();
					list.add(row);
					workQueue.execute(new TaskData(G.shop_info, String.valueOf(new Random(10000).nextInt()), list));
				}
				result = true;
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
			
			logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}, size - {}", taskData.getId(), taskData.getRetryCnt(), taskData.getFiltering().size());
			
		} else {
			logger.info("fail offsetModifyV3 _id - {}, retryCnt - {}, size - {}", taskData.getId(), taskData.getRetryCnt(), taskData.getFiltering().size());
			
			workQueue.execute(taskData);
		}
	}
}
