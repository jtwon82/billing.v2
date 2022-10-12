package com.mobon.billing.core.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ClickViewPoint2DataToMariaDB;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskClickViewPoint2Data {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskClickViewPoint2Data.class);

	@Autowired
	private ClickViewPoint2DataToMariaDB	ClickViewPoint2DataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("ClickViewPoint2DataWorkQueue")
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
		TaskClickViewPoint2Data.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskClickViewPoint2Data.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskClickViewPoint2Data.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskClickViewPoint2Data.threadCnt;
	}
	
	
	
	public void mongoToFileClickViewPoint2() {
		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removePoint2ObjectMap(new BaseCVData());
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
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	public void mongoToMariaClickViewPoint2V3() {
		logger.info(">> START mongoToMariaClickViewPoint2V3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVPoint2Data = (Map<String, BaseCVData>) sumObjectManager.removePoint2ObjectMap(new BaseCVData() );
		ArrayList<BaseCVData> listBaseCVData = new ArrayList();
		if(summeryBaseCVPoint2Data!=null && summeryBaseCVPoint2Data.entrySet()!=null) {
			Iterator<Entry<String, BaseCVData>> it = summeryBaseCVPoint2Data.entrySet().iterator();
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
			
			if(listBaseCVData.size()>0) {
				String _id=Math.random()+"";
				if ( workingKey.contains(_id) ) {
					logger.info("workingKey.contains - {}, listBaseCVData.size - {}", _id, listBaseCVData.size());
					
					for( BaseCVData row : listBaseCVData ) {
						sumObjectManager.appendPoint2Data(row);
					}
				} else {
					workingKey.add( _id, 3 );
					//workQueue.execute(new RetryTaskerV3(filtering, group));
					
					if( 10000>listBaseCVData.size() ) {
						workQueue.execute(new TaskData(G.click_view_point2, _id, listBaseCVData));
						
					} else {
						List<BaseCVData> intoFiltering = new ArrayList();
						int cnt=0;
						for( BaseCVData row : listBaseCVData ) {
							if( 10000>cnt++ ) {
								intoFiltering.add(row);
							}else {
								sumObjectManager.appendPoint2Data(row);
							}
						}
						workQueue.execute(new TaskData(G.click_view_point2, _id, intoFiltering));
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
				result = ClickViewPoint2DataToMariaDB.intoMariaClickViewPoint2DataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = ClickViewPoint2DataToMariaDB.intoMariaClickViewPoint2DataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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
