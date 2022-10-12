package com.mobon.billing.branchUM.schedule;

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
import com.mobon.billing.branchUM.config.RetryConfig;
import com.mobon.billing.branchUM.service.KnoUMSiteCodeDataToMariaDB;
import com.mobon.billing.branchUM.service.SumObjectManager;
import com.mobon.billing.branchUM.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskKnoUMSiteCodeData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskKnoUMSiteCodeData.class);

	@Autowired
	private KnoUMSiteCodeDataToMariaDB	KnoUMSiteCodeDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("KnoUMSiteCodeDataWorkQueue")
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
		TaskKnoUMSiteCodeData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskKnoUMSiteCodeData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskKnoUMSiteCodeData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskKnoUMSiteCodeData.threadCnt;
	}
	
	
	
	
	public void mongoToMariaKnoUMSiteCodeV3() {
		logger.info(">> START mongoToMariaKnoUMSiteCodeV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeKnoUMSiteCodeMap();
		ArrayList<BaseCVData> listKnoUMSiteCodeData = new ArrayList();
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
								continue;
							}
							listKnoUMSiteCodeData.add(item);
						}
					}
				} catch (ConcurrentModificationException e) {
					logger.error("ConcurrentModificationException ", e);
				}
			}
			
			String[] group_key = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19" };

			for (String group : group_key) {
				String _id = Arrays.asList(new String[] { group }).toString();

				List<BaseCVData> filtering = (List<BaseCVData>) listKnoUMSiteCodeData.stream()
						.filter(row -> _id.equals(row.getGroupingSeq())).collect(Collectors.toList());
				
				if( filtering.size()>0 ) {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					try {
						if ( workingKey.contains(_id) ) {
							logger.info("workingKey.contains - {}, listKnoUMSiteCodeData.size - {}", _id, listKnoUMSiteCodeData.size());
							
							for( BaseCVData row : filtering ) {
								sumObjectManager.appendKnoUMSiteCodeData(row);
							}
						} else {
							workingKey.add( _id, 3 );
							
							if( 10000>filtering.size() ) {
								workQueue.execute(new TaskData(G.KnoUMSiteCodeData, _id, filtering));
								
							} else {
								List<BaseCVData> intoFiltering = new ArrayList();
								int cnt=0;
								for( BaseCVData row : filtering ) {
									if( 10000>cnt++ ) {
										intoFiltering.add(row);
									}else {
										sumObjectManager.appendKnoUMSiteCodeData(row);
									}
								}
								workQueue.execute(new TaskData(G.KnoUMSiteCodeData, _id, intoFiltering));
							}
						}
					}catch(Exception e) {
						for( BaseCVData row : filtering ) {
							sumObjectManager.appendKnoUMSiteCodeData(row);
						}
						logger.error("err ", e);
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
				result = KnoUMSiteCodeDataToMariaDB.intoMariaKnoUMSiteCodeDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = KnoUMSiteCodeDataToMariaDB.intoMariaKnoUMSiteCodeDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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
