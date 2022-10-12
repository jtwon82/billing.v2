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
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.UniqueClickDataToMariaDB;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ArrayHelper;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskUniqueClickData {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskUniqueClickData.class);
	
	@Autowired
	private SelectDao selectDao;
	
	@Autowired
	private UniqueClickDataToMariaDB uniqueClickDataToMriaDB;
	
	@Autowired
	private RetryConfig retryConfig;
	
	@Autowired
	@Qualifier("UniqueClickDataWorkQueue")
	private WorkQueueTaskData workQueue;
	
	@Autowired
	private SumObjectManager sumObjectManager;
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public static void setThreadCnt(int threadCnt) {
		TaskUniqueClickData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskUniqueClickData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskUniqueClickData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskUniqueClickData.threadCnt;
	}
	
	
	
	public void mongoToFileUniqueClickView() {
		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeClickUniqueMap(new BaseCVData());
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
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.Unique_Click, listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void mongoToMariaClickUnique() {
		logger.info(">> START mongoToMariaClickUnique THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		Map<String, BaseCVData> summeryBaseCVData = (Map<String, BaseCVData>) sumObjectManager.removeClickUniqueMap(new BaseCVData() );
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
			
//			ArrayList<BaseCVData> list = selectDao.selectAdgubunKey2();
//			String [][] group_key = new String [list.size()][];
//			int i=0;
//			for( BaseCVData row : list ) {
//				group_key[i++] = new String [] { row.getAdGubun(), (row.getScriptNo()%10)+"" };
//			}
//			
//			for (String[] group : group_key) {
//				String _id = Arrays.asList(group).toString();
//				List<BaseCVData> filtering = (List<BaseCVData>) listBaseCVData.stream()
//						.filter(row -> _id.equals( row.getGrouping() ) )
//						.collect(Collectors.toList());

			List<List<BaseCVData>> ret= ArrayHelper.split(listBaseCVData, 300);
			try {
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					logger.info("Unique_Click Size ::: {}", list.size());
					workQueue.execute(new TaskData(G.Unique_Click, String.valueOf(i), list));
				}
			}catch (Exception e) {
				for (int i = 0 ; i < ret.size(); i++) {
					List<BaseCVData> list = ret.get(i);
					for( BaseCVData row : list ) {
						sumObjectManager.appendClickUniquekData(row);
					}
				}
				logger.error("err ", e);
			}
			workingKey.remove("main");
		}
	}
	
	
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("Unique_Click retryCnt - {}, _id - {}, size={}", taskData.getRetryCnt(), taskData.getId(), taskData.getFiltering().size());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = uniqueClickDataToMriaDB.intoMariaClickViewDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
			} else {
				result = uniqueClickDataToMriaDB.intoMariaClickViewDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
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

