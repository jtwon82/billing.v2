package com.mobon.billing.sample.schedule;

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

import com.adgather.util.old.DateUtils;
import com.mobon.billing.sample.config.RetryConfig;
import com.mobon.billing.sample.model.SampleVo;
import com.mobon.billing.sample.model.TaskData;
import com.mobon.billing.sample.service.SampleVoToMariaDB;
import com.mobon.billing.sample.service.SumObjectManager;
import com.mobon.billing.sample.service.WorkQueueTaskData;
import com.mobon.billing.sample.util.ConsumerFileUtils;
import com.mobon.billing.sample.util.TimeToLiveCollection;

@Component
public class TaskSampleVo {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskSampleVo.class);

//	@Autowired
//	private SelectDao				selectDao;
	
	@Autowired
	private SampleVoToMariaDB		SampleVoToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("SampleVoWorkQueue")
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
		TaskSampleVo.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskSampleVo.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskSampleVo.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskSampleVo.threadCnt;
	}
	
	
	
	public void mongoToFileSampleVo() {
		Map<String, SampleVo> summerySampleVo = (Map<String, SampleVo>) sumObjectManager.removeSampleVo();
		ArrayList<SampleVo> listWriteData = new ArrayList();
		if(summerySampleVo!=null && summerySampleVo.entrySet()!=null) {
			try {
				for (Entry<String, SampleVo> item : summerySampleVo.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summerySampleVo.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, "SampleVo", listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	public void mongoToMariaSampleVo() {
		logger.debug(">> START mongoToMariaSampleVoV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, SampleVo> summerySampleVo = (Map<String, SampleVo>) sumObjectManager.removeSampleVo();
		ArrayList<SampleVo> listSampleVo = new ArrayList();
		if(summerySampleVo!=null && summerySampleVo.entrySet()!=null) {
			try {
				for (Entry<String, SampleVo> item : summerySampleVo.entrySet()) {
					
					if( listSampleVo.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendSampleVo(item.getValue());
					} else {
						listSampleVo.add(item.getValue());
					}
				}
				logger.info("listSampleVo.size {}", listSampleVo.size());
				
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summerySampleVo.toString());
			}
			
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				
				List<SampleVo> filtering = (List<SampleVo>) listSampleVo.stream()
						.filter(row -> _id.equals( row.getGrouping() ) )
						.collect(Collectors.toList());
				
				
				if( filtering.size()==0 ) {
					logger.debug("filtering empty _id {}", _id);
					
				} else {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if ( workingKey.contains(_id) ) {
						logger.info("workingKey.contains - {}, listSampleVo.size - {}", _id, listSampleVo.size());
						
						for( SampleVo row : filtering ) {
							sumObjectManager.appendSampleVo(row);
						}
					} else {
						workingKey.add( _id, 3 );
						
						workQueue.execute(new TaskData("SampleVo", _id, filtering));
					}
				}
			}
		}
		workingKey.remove("main");
	}
	
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = SampleVoToMariaDB.intoMariaSampleVoV3(taskData.getId(), (List<SampleVo>)taskData.getFiltering(), false);
			} else {
				result = SampleVoToMariaDB.intoMariaSampleVoV3(taskData.getId(), (List<SampleVo>)taskData.getFiltering(), true);
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
			
		} else {
			workQueue.execute(taskData);
			logger.info("retry size {}", taskData.getFiltering().size());
		}
	}
	
}
