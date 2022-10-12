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
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ShopMdPcodeDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskShopMdPcodeData {

	private static final Logger		logger	= LoggerFactory.getLogger(TaskShopMdPcodeData.class);

	@Autowired
	private ShopMdPcodeDataToMariaDB	ShopMdPcodeDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;
	
	@Autowired
	private RetryTemplate			retryTemplate;

	@Autowired
	@Qualifier("ShopMdPcodeDataWorkQueue")
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
		TaskShopMdPcodeData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskShopMdPcodeData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskShopMdPcodeData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskShopMdPcodeData.threadCnt;
	}
	
	

	public void mongoToFileShopMdPcode() {
		Map<String, ShopInfoData> summeryShopMdPcodeData = (Map<String, ShopInfoData>) sumObjectManager.removeShopInfoMdPcodeMap();
		ArrayList<ShopInfoData> listWriteData = new ArrayList();
		if(summeryShopMdPcodeData!=null && summeryShopMdPcodeData.entrySet()!=null) {
			try {
				for (Entry<String, ShopInfoData> item : summeryShopMdPcodeData.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryShopMdPcodeData.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shopMdPcode_info, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}

	public void mongoToMariaShopMdPcodeDataV3() {
		logger.info(">> START mongoToMariaShopMdPcodeDataV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}
		workingKey.add("main", 1);
		
		Map<String, ShopInfoData> summeryShopMdPcodeData = (Map<String, ShopInfoData>) sumObjectManager.removeShopInfoMdPcodeMap();
		ArrayList<ShopInfoData> listShopMdPcodeData = new ArrayList();
		if(summeryShopMdPcodeData!=null && summeryShopMdPcodeData.entrySet()!=null) {
			try {
				for (Entry<String, ShopInfoData> item : summeryShopMdPcodeData.entrySet()) {
					if( listShopMdPcodeData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendMdPcodeData(item.getValue());
					} else {
						listShopMdPcodeData.add(item.getValue());
					}
				}
				logger.info("listShopMdPcodeData.size - {}", listShopMdPcodeData.size());
			}catch(ConcurrentModificationException e) {
				logger.debug("item - {}", summeryShopMdPcodeData.toString());
			}

//			String[][] group_key = new String[][] { { "W" }, { "M" }, { "D" } };
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
		
//			for (String[] group : group_key) {
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				
				List<ShopInfoData> filtering = (List<ShopInfoData>) listShopMdPcodeData.stream()
						.filter(row -> _id.equals( row.getGrouping() ) )
						.collect(Collectors.toList());
				
				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if (workingKey.contains(_id)) {
						logger.info("workingKey.contains - {}, listShopMdPcodeData.size {}", _id, listShopMdPcodeData.size());
						
						for( ShopInfoData row : filtering ) {
							sumObjectManager.appendMdPcodeData(row);
						}
					} else {
						workingKey.add( _id, 3 );

//						if( 1000>filtering.size() ) {
//							workQueue.execute(new TaskData(G.shopMdPcode_info, _id, filtering));
//							
//						} else {
							List<ShopInfoData> intoFiltering = new ArrayList();
							int cnt=0;
							for( ShopInfoData row : filtering ) {
								if( 1000>cnt++ ) {
									intoFiltering.add(row);
								}else {
									sumObjectManager.appendMdPcodeData(row);
								}
							}
							workQueue.execute(new TaskData(G.shopMdPcode_info, _id, intoFiltering));
//						}
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
			if ( taskData.increaseRetryCnt() > 100 ) {
				result = true;
				List<ShopInfoData> list = (List<ShopInfoData>)taskData.getFiltering();
				for ( ShopInfoData row : list ) {
					logger.info("retrycnt - {}, row - {}", taskData.getRetryCnt(), row.toString());
				}
				
			} else if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = ShopMdPcodeDataToMariaDB.intoMariaShopMdPcodeDataV3(taskData.getId(), (List<ShopInfoData>)taskData.getFiltering(), false);
			} else {
				result = ShopMdPcodeDataToMariaDB.intoMariaShopMdPcodeDataV3(taskData.getId(), (List<ShopInfoData>)taskData.getFiltering(), true);
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
