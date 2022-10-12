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
import com.mobon.billing.sample.model.TaskData;
import com.mobon.billing.sample.model.UseridVo;
import com.mobon.billing.sample.service.SumObjectManager;
import com.mobon.billing.sample.service.UseridVoToMariaDB;
import com.mobon.billing.sample.service.WorkQueueTaskData;
import com.mobon.billing.sample.util.ConsumerFileUtils;
import com.mobon.billing.sample.util.TimeToLiveCollection;

@Component
public class TaskUseridVo {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskUseridVo.class);

//	@Autowired
//	private SelectDao				selectDao;
	
	@Autowired
	private UseridVoToMariaDB		UseridVoToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("UseridVoWorkQueue")
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
		TaskUseridVo.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskUseridVo.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskUseridVo.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskUseridVo.threadCnt;
	}
	
	
	
	public void mongoToFileUseridVo() {
		Map<String, UseridVo> summeryUseridVo = (Map<String, UseridVo>) sumObjectManager.removeUseridVo();
		ArrayList<UseridVo> listWriteData = new ArrayList();
		if(summeryUseridVo!=null && summeryUseridVo.entrySet()!=null) {
			try {
				for (Entry<String, UseridVo> item : summeryUseridVo.entrySet()) {
					listWriteData.add(item.getValue());
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryUseridVo.toString());
			}
			
			if( listWriteData.size()>0 ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, "UseridVo", listWriteData);
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	public void mongoToMariaUseridVo() {
		logger.debug(">> START mongoToMariaUseridVoV3 THREAD COUNT - {}", threadCnt);
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, UseridVo> summeryUseridVo = (Map<String, UseridVo>) sumObjectManager.removeUseridVo();
		ArrayList<UseridVo> listUseridVo = new ArrayList();
		if(summeryUseridVo!=null && summeryUseridVo.entrySet()!=null) {
			try {
				int maxSize = summeryUseridVo.entrySet().size();
				for (Entry<String, UseridVo> item : summeryUseridVo.entrySet()) {
					
					if( listUseridVo.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendUseridVo(item.getValue());
					} else {
						listUseridVo.add(item.getValue());
					}
				}
				logger.info("listUseridVo.size {}", listUseridVo.size());
				
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryUseridVo.toString());
			}
			
			String [] group_key = new String [] {"0","1","2","3","4","5","6","7","8","9", "10","11","12","13","14","15","16","17","18","19"};
			for (String group : group_key) {
				String _id = Arrays.asList(group).toString();
				
				List<UseridVo> filtering = (List<UseridVo>) listUseridVo.stream()
						.filter(row -> _id.equals( row.getGrouping() ) )
						.collect(Collectors.toList());
				
				if( filtering.size()==0 ) {
					logger.debug("filtering empty _id {}", _id);
					
				} else {
					logger.info("filtering {}, {}", _id, filtering.size());
					
					if ( workingKey.contains(_id) ) {
						logger.info("workingKey.contains - {}, listUseridVo.size - {}", _id, listUseridVo.size());
						
						for( UseridVo row : filtering ) {
							sumObjectManager.appendUseridVo(row);
						}
					} else {
						workingKey.add( _id, 3 );
						
						workQueue.execute(new TaskData("UseridVo", _id, filtering));
						
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
				result = UseridVoToMariaDB.intoMariaUseridVoV3(taskData.getId(), (List<UseridVo>)taskData.getFiltering(), false);
			} else {
				result = UseridVoToMariaDB.intoMariaUseridVoV3(taskData.getId(), (List<UseridVo>)taskData.getFiltering(), true);
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
