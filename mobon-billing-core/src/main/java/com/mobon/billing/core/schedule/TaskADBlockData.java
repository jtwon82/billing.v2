package com.mobon.billing.core.schedule;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ADBlockDataToMariaDB;
import com.mobon.billing.core.service.ClickViewDataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class TaskADBlockData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskADBlockData.class);

	@Autowired
	private SelectDao				selectDao;
	
	@Autowired
	private ADBlockDataToMariaDB adblockDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("ADBlockDataWorkQueue")
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
		TaskADBlockData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskADBlockData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskADBlockData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskADBlockData.threadCnt;
	}

	public void mongoToMariaADBlockV3() {
		logger.info(">> START mongoToMariaADBlockV3 THREAD COUNT - {}", threadCnt);

		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeADBlockObjectMap(new BaseCVData() );
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

			String _id = "ADBlock";
			if( listBaseCVData.size()>0 ) {
				logger.info("filtering {}, {}", listBaseCVData.size());

//					if ( workingKey.contains(_id) ) {
//						logger.info("workingKey.contains - {}, listBaseCVData.size - {}", _id, listBaseCVData.size());
//
//						for( BaseCVData row : filtering ) {
//							sumObjectManager.appendClientEnvirmentData(row);
//						}
//					} else {
				workingKey.add( _id, 3 );
				//workQueue.execute(new RetryTaskerV3(filtering, group));

				if( 10000>listBaseCVData.size() ) {
					workQueue.execute(new TaskData(G.click_view_adblock, _id, listBaseCVData));

				} else {
					List<BaseCVData> intoFiltering = new ArrayList();
					int cnt=0;
					for( BaseCVData row : listBaseCVData ) {
						if( 10000>cnt++ ) {
							intoFiltering.add(row);
						}else {
							sumObjectManager.appendADBlockData(row);
						}
					}
					workQueue.execute(new TaskData(G.click_view_adblock, _id, intoFiltering));
				}
			}
//				}
		}
		workingKey.remove("main");
	}

	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;

		increaseThreadCnt();

		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = adblockDataToMariaDB.intoMariaADBlockDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = adblockDataToMariaDB.intoMariaADBlockDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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
