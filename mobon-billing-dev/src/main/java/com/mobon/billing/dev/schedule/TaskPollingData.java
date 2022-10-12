package com.mobon.billing.dev.schedule;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.mobon.billing.dev.model.PollingData;
import com.mobon.billing.dev.service.SumObjectManager;
import com.mobon.billing.dev.service.WorkQueueTaskData;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskPollingData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskPollingData.class);

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	@Autowired
	private SumObjectManager		sumObjectManager;
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	
	@Value("${batch.list.size}")
	private String	batchListSize;

	@Resource(name="sqlSessionTemplatePostgres")
	private SqlSessionTemplate	sqlSessionTemplatePostgres;

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;
	
	@Resource(name="workQueuePollingData")
	private WorkQueueTaskData workQueuePollingData;
	
	
	public static void setThreadCnt(int threadCnt) {
		TaskPollingData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskPollingData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskPollingData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskPollingData.threadCnt;
	}
	
	@Scheduled(fixedRate = 100)
	@Async
	public void mongoToMariaClickViewPoint2V3() {
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);

		Map<String, PollingData> summeryBaseCVPoint2Data = (Map<String, PollingData>) sumObjectManager.removePollingDataMap();
		ArrayList<PollingData> listPollingData = new ArrayList();
		if(summeryBaseCVPoint2Data!=null && summeryBaseCVPoint2Data.entrySet()!=null) {
			Iterator<Entry<String, PollingData>> it = summeryBaseCVPoint2Data.entrySet().iterator();
			while (it.hasNext()) {
				try {
					Entry<String, PollingData> Titem = it.next();
					if (Titem != null) {
						PollingData item = Titem.getValue();
						if (item != null) {
							listPollingData.add(item);
						}
					}
				} catch (ConcurrentModificationException e) {
					logger.error("ConcurrentModificationException ", e);
				}
			}
			
			if(listPollingData.size()>0) {
				workQueuePollingData.execute(new TaskData(G.click_view, ""+Math.abs(new Random().nextInt()), listPollingData));
			}
		}
		workingKey.remove("main");
	}
	
	public void mongoToMariaV3(TaskData taskData) {
		logger.info(">> START mongoToMariaV3 - {}", threadCnt);

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
//			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
//				result = clickviewDataToMariaDB.intoMariaClickViewDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), false);
//			} else {
//				result = clickviewDataToMariaDB.intoMariaClickViewDataV3(taskData.getId(), (List<BaseCVData>)taskData.getFiltering(), true);
//			}
			
			
			ArrayList listPollingData= (ArrayList)taskData.getFiltering();
//			logger.info("listPollingData - {}", listPollingData.toString());

//			sqlSessionTemplatePostgres.insert("updateMapper.insertNewtable", listPollingData);
			sqlSessionTemplateBilling.insert("updateMapper.insertNewtableMariaDB", listPollingData);
			logger.info("listPollingData.size {} commit ", listPollingData.size());
			
			result = true;
			
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
//
//			long millis = Calendar.getInstance().getTimeInMillis();
//			String writeFileName = String.format("%s_%s_%s", "insertIntoError", taskData.getId(), DateUtils.getDate("yyyyMMdd_HHmm"), millis);
//			try {
//				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, taskData.getFiltering());
//			}catch(Exception ee) {				}
			
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
		} else {
			workQueuePollingData.execute(taskData);
			logger.info("retry size {}", taskData.getFiltering().size());
		}
	}
	
}
