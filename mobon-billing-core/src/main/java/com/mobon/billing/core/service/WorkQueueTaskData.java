package com.mobon.billing.core.service;

import java.util.LinkedList;

import com.mobon.billing.branch.schedule.*;
import com.mobon.billing.core.schedule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adgather.constants.G;
//import com.mobon.billing.branch.schedule.TaskAppTargetData;
import com.mobon.billing.env.schedule.TaskCampMediaRetrnAvalData;
import com.mobon.billing.pargatr.schedule.TaskParGatrData;
import com.mobon.exschedule.model.TaskData;

public class WorkQueueTaskData {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueueTaskData.class);
	
	private String				queueName;
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;

	@Autowired
	private TaskClickViewData	taskClickViewData;
	@Autowired
	private TaskADBlockData taskADBlockData;
	@Autowired
	private TaskCrossAUIDData taskCrossAUIDData;
	@Autowired
	private TaskClickViewOpenrtbData	taskClickViewOpenrtbData;
	@Autowired
	private TaskClickViewPcodeData	taskClickViewPcodeData;
	@Autowired
	private TaskClientEnvirmentData	taskClientEnvirmentData;
	@Autowired
	private TaskAdverClientEnvirmentData	taskAdverClientEnvirmentData;
	@Autowired
	private TaskCampMediaRetrnAvalData	taskCampMediaRetrnAvalData;
	@Autowired
	private TaskClientAgeGenderData	taskClientAgeGenderData;
	@Autowired
	private TaskClickViewPointData	taskClickViewPointData;
	@Autowired
	private TaskClickViewPoint2Data	taskClickViewPoint2Data;
	@Autowired
	private TaskExternalData	taskExternalData;
	@Autowired
	private TaskShopInfoData	taskShopInfoData;
	@Autowired
	private TaskShopMdPcodeData	taskShopMdPcodeData;
	@Autowired
	private TaskShopStatsInfoData	taskShopStatsInfoData;
	@Autowired
	private TaskActionData		taskActionData; 
	@Autowired
	private TaskActionPcodeData	taskActionPcodeData;
	@Autowired
	private TaskConvData		taskConvData;
	@Autowired
	private TaskConvAbusingData		taskConvAbusingData;
	@Autowired
	private TaskConvPcodeData		taskConvPcodeData;  
	@Autowired
	private TaskConvAllData		taskConvAllData; 
	@Autowired
	private TaskIntgCntrConvData		taskIntgCntrConvData; 

	@Autowired
	private TaskNearData		taskNearData;
//	@Autowired
//	private TaskAppTargetData	taskAppTargetData;
	@Autowired
	private TaskMediaChrgData	taskMediaChrgData;
	@Autowired
	private TaskIntgCntrData	taskIntgCntrData;
	@Autowired
	private TaskIntgCntrKgrData	taskIntgCntrKgrData;
	@Autowired
	private TaskIntgCntrTtimeData	taskIntgCntrTtimeData;
	@Autowired
	private TaskIntgCntrActionData	taskIntgCntrActionData;
	@Autowired
	private TaskParGatrData		taskParGatrData;
	@Autowired
	private TaskPhoneData		taskPhoneData;
	
	@Autowired
	private TaskViewPcodeData   taskViewPcodeData;
	
	@Autowired
	private TaskUniqueClickData taskUniqueClickData;

	@Autowired
	private TaskContributeConvData taskContributeConvData;
	
	@Autowired
	private TaskChrgLogData taskChrgLogData;
	
	@Autowired
	private TaskAiData taskAiData;
	
	@Autowired
	private TaskPluscallLogData taskPluscallLogData;

	@Autowired
	private TaskABPcodeRecomData taskABPcodeRecomData;

	@Autowired
	private TaskConvABPcodeRecomData taskConvABPcodeRecomData;

	@Autowired
	private TaskActionABPcodeData taskActionABPcodeData;

	@Autowired
	private TaskActionRenewLogData taskActionRenewLogData;

	@Autowired
	private TaskBasketData taskBasketData;
	
	public WorkQueueTaskData(String _queueName) {
		logger.debug("queueName - {} START", _queueName);
		queueName	= _queueName;
		
		queue = new LinkedList();

		threads = new PoolWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	
	public WorkQueueTaskData(String _queueName, int nThreads) {
		logger.debug("queueName - {} START", _queueName);
		queueName	= _queueName;
		
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(TaskData r) {
		for (int i = 0; i < nThreads; i++) {
			try {
				if (!threads[i].isAlive()) {
					// 쓰레드가 죽었을 경우 Sleep 후 Interrupt
					// stop() 메소드로 스레드를 갑자기 종료하게 되면, 스레드가 사용중이던 자원들이 불안전한 상태로 남겨짐
					threads[i].sleep(1);
					threads[i].interrupt();
					logger.info("WorkQueue {}, killd ", queueName);
					throw new InterruptedException();
				}
			} catch (InterruptedException e) {
				saveThread(i);
				continue;
			}
		}
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private void saveThread(int index) {
		String threadName= threads[index].getName();
		threads[index] = new PoolWorker();
		threads[index].setName(threadName);
		threads[index].start();
		logger.info("WorkQueue {}, revivald ", queueName);
	}
	
	public int getQueueSize() {
		int size = 0;
		synchronized (queue) {
			size = queue.size();
		}
		return size; 
	}
	public int getThreadsCount() {
		return threads.length;
	}


	

	private class PoolWorker extends Thread {
		public void run() {
			TaskData r;

			while (true) {
				synchronized (queue) {
					while ( queue.isEmpty() ) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					//logger.info("{} queue before - {}", queueName, queue.size() );
					r = (TaskData) queue.removeFirst();
					logger.info("{} queue after - {}", queueName, queue.size() );
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				if( r.getTask().equals(G.click_view) ) {
					if(taskClickViewData!=null)
						taskClickViewData.mongoToMariaV3(r);
					else
						logger.error("taskClickViewData nothing");

				} else if( r.getTask().equals(G.click_view_adblock) ){

					if(taskADBlockData!=null)
						taskADBlockData.mongoToMariaV3(r);
					else
						logger.error("taskADBlockData nothing");

				} else if( r.getTask().equals(G.click_view_crossAUID) ){

					if(taskCrossAUIDData!=null)
						taskCrossAUIDData.mongoToMariaV3(r);
					else
						logger.error("taskCrossAUIDData nothing");

				} else if( r.getTask().equals(G.click_view_openrtb) ) {
					if(taskClickViewOpenrtbData!=null)
						taskClickViewOpenrtbData.mongoToMariaV3(r);
					else
						logger.error("taskClickViewData nothing");
					
				} else if( r.getTask().equals(G.click_view_pcode) ) {
					if(taskClickViewPcodeData!=null)
						taskClickViewPcodeData.mongoToMariaV3(r);
					else
						logger.error("taskClickViewPcodeData nothing");
					
				} else if( r.getTask().equals(G.client_environment) ) {
					if(taskClientEnvirmentData!=null)
						taskClientEnvirmentData.mongoToMariaV3(r);
					else
						logger.error("taskClientEnvirmentData nothing");
					
				} else if( r.getTask().equals(G.adver_client_environment) ) {
					if(taskAdverClientEnvirmentData!=null)
						taskAdverClientEnvirmentData.mongoToMariaV3(r);
					else
						logger.error("taskAdverClientEnvirmentData nothing");
					
				} else if( r.getTask().equals(G.client_age_gender) ) {
					if(taskClientAgeGenderData!=null)
						taskClientAgeGenderData.mongoToMariaV3(r);
					else
						logger.error("taskClientAgeGenderData nothing");
					
				} else if( r.getTask().equals(G.camp_media_retrn_aval) ) {
					if(taskCampMediaRetrnAvalData!=null)
						taskCampMediaRetrnAvalData.mongoToMariaV3(r);
					else
						logger.error("taskCampMediaRetrnAvalDataelse nothing");
					
				} else if( r.getTask().equals(G.Phone_info) ) {
					if(taskPhoneData!=null)
						taskPhoneData.mongoToMariaV3(r);
					else
						logger.error("taskClientEnvirmentData nothing");
					
				} else if ( r.getTask().equals(G.click_view_point) ) {
					if(taskClickViewPointData!=null)
						taskClickViewPointData.mongoToMariaV3(r);
					else
						logger.error("taskClickViewData nothing");
					
				} else if ( r.getTask().equals(G.click_view_point2) ) {
					if(taskClickViewPoint2Data!=null)
						taskClickViewPoint2Data.mongoToMariaV3(r);
					else
						logger.error("taskClickViewPoint2Data nothing");
					
				} else if (r.getTask().equals(G.external_info) ) {
					if(taskExternalData!=null)
						taskExternalData.mongoToMariaV3(r);
					else
						logger.error("taskExternalData nothing");
					
				} else if (r.getTask().equals(G.shop_info) ) {
					if(taskShopInfoData!=null)
						taskShopInfoData.mongoToMariaV3(r);
					else
						logger.error("taskShopInfoData nothing");
					
				} else if (r.getTask().equals(G.shopMdPcode_info) ) {
					if(taskShopMdPcodeData!=null)
						taskShopMdPcodeData.mongoToMariaV3(r);
					else
						logger.error("taskShopMdPcodeData nothing");
					
				} else if (r.getTask().equals(G.shop_stats) ) {
					if(taskShopStatsInfoData!=null)
						taskShopStatsInfoData.mongoToMariaV3(r);
					else
						logger.error("taskShopStatsInfoData nothing");

				} else if (r.getTask().equals(G.action_data) ) {
					if(taskActionData!=null)
						taskActionData.mongoToMariaV3(r);
					else
						logger.error("taskActionData nothing");

				} else if (r.getTask().equals(G.action_pcode_data) ) {
					if(taskActionPcodeData!=null)
						taskActionPcodeData.mongoToMariaV3(r);
					else
						logger.error("taskActionPcodeData nothing");

				} else if (r.getTask().equals(G.conv_info) ) {
					if(taskConvData!=null)
						taskConvData.mongoToMariaV3(r);
					else
						logger.error("taskConvData nothing");

				} else if (r.getTask().equals(G.convAbusing_info) ) {
					if(taskConvAbusingData!=null)
						taskConvAbusingData.mongoToMariaV3(r);
					else
						logger.error("taskConvAbusingData nothing");

				} else if (r.getTask().equals(G.ConvPcode_info) ) {
					if(taskConvPcodeData!=null)
						taskConvPcodeData.mongoToMariaV3(r);
					else
						logger.error("taskConvData nothing");

				} else if (r.getTask().equals(G.convAll_info) ) {
					if(taskConvAllData!=null)
						taskConvAllData.mongoToMariaV3(r);
					else
						logger.error("taskConvAllData nothing");
					
				} else if (r.getTask().equals(G.IntgCntrconv_info) ) {
					if(taskIntgCntrConvData!=null)
						taskIntgCntrConvData.mongoToMariaV3(r);
					else
						logger.error("taskIntgCntrConvData nothing");
					
					
					
				} else if (r.getTask().equals(G.near_info) ) {
					if(taskNearData!=null)
						taskNearData.mongoToMariaV3(r);
					else
						logger.error("taskNearData nothing");

//				} else if (r.getTask().equals(G.AppTarget_info) ) {
//					if(taskAppTargetData!=null)
//						taskAppTargetData.mongoToMariaV3(r);
//					else
//						logger.error("taskAppTargetData nothing");

				} else if (r.getTask().equals(G.MediaChrgData) ) {
					if(taskMediaChrgData!=null)
						taskMediaChrgData.mongoToMariaV3(r);
					else
						logger.error("taskMediaChrgData nothing");

				} else if (r.getTask().equals(G.ParGatrData) ) {
					if(taskParGatrData!=null)
						taskParGatrData.mongoToMariaV3(r);
					else
						logger.error("taskParGatrData nothing");
					
				} else if (r.getTask().equals(G.IntgCntrData) ) {
					if(taskIntgCntrData!=null)
						taskIntgCntrData.mongoToMariaV3(r);
					else
						logger.error("taskIntgCntrData nothing");
					
				} else if (r.getTask().equals(G.IntgCntrKgrData) ) {
					if(taskIntgCntrKgrData!=null)
						taskIntgCntrKgrData.mongoToMariaV3(r);
					else
						logger.error("taskIntgCntrData nothing");

				} else if (r.getTask().equals(G.IntgCntrTtimeData) ) {
					if(taskIntgCntrTtimeData!=null)
						taskIntgCntrTtimeData.mongoToMariaV3(r);
					else
						logger.error("taskIntgCntrData nothing");

				} else if (r.getTask().equals(G.IntgCntrAction_data) ) {
					if(taskIntgCntrActionData!=null)
						taskIntgCntrActionData.mongoToMariaV3(r);
					else
						logger.error("taskIntgCntrActionData nothing");
					
				} else if (r.getTask().equals(G.view_pcode)) {
					if (taskViewPcodeData != null) {
						taskViewPcodeData.mongoToMariaV3(r);
					} else {
						logger.error("taskViewPcodeData nothing");
					}
					
				} else if (r.getTask().equals(G.Unique_Click)) {
					if (taskUniqueClickData != null) {
						taskUniqueClickData.mongoToMariaV3(r);
					} else {
						logger.error("taskUniqueClickData nothing");
						
					}
					
				} else if (r.getTask().equals(G.ContributeConvData)) {
					if( taskContributeConvData != null) {
						taskContributeConvData.mongoToMariaV3(r);
					} else {
						logger.error("taskContributeConvData nothing");
					}
				} else if (r.getTask().equals(G.ChrgLogData)) {
					if( taskChrgLogData != null) {
						taskChrgLogData.mongoToMariaV3(r);
					} else {
						logger.error("taskChrgLogData nothing");
					}
				} else if (r.getTask().equals(G.AiData)) {
					if( taskAiData != null) {
						taskAiData.mongoToMariaV3(r);
					} else {
						logger.error("taskAiData nothing");
					}
				} else if (r.getTask().equals(G.PluscallLogData)) {
					if( taskPluscallLogData != null) {
						taskPluscallLogData.mongoToMariaV3(r);
					} else {
						logger.error("taskPluscallLogData nothing");
					}
				} else if (r.getTask().equals(G.ABPcodeRecomData)) {
					if (taskABPcodeRecomData != null) {
						taskABPcodeRecomData.mongoToMariaV3(r);
					} else {
						logger.error("taskABPcodeRecomData nothing");
					}
				} else if (r.getTask().equals(G.ABPcodeRecomConvData)) {
					if(taskConvABPcodeRecomData != null ) {
						taskConvABPcodeRecomData.mongoToMariaV3(r);
 					} else {
						logger.error("taskConvABPcodeRecomData nothing");
					}
				} else if (r.getTask().equals(G.ActionABPcodeData)) {
					if (taskActionABPcodeData  != null) {
						taskActionABPcodeData.mongoToMariaV3(r);
					} else  {
						logger.error("taskActionABPcodeData nothing");
					}
				} else if (r.getTask().equals(G.ActionRenewLogData)) {
					if (taskActionRenewLogData != null) {
						taskActionRenewLogData.mongoToClickhouse(r);
					} else {
						logger.error("taskActionRenewLogData nothing");
					}
				} else if (r.getTask().equals(G.BasketData)) {
					if (taskBasketData != null) {
						taskBasketData.mongoToMariaV3(r);
					} else {
						logger.error("taskBasketData nothing");
					}
				}
			}
		}
	}
}