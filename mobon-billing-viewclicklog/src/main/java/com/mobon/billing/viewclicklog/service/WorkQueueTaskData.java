package com.mobon.billing.viewclicklog.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adgather.constants.G;
import com.mobon.billing.model.v20.TaskData;
import com.mobon.billing.viewclicklog.schedule.TaskConversionVo;
import com.mobon.billing.viewclicklog.schedule.TaskViewClickVo;

public class WorkQueueTaskData {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueueTaskData.class);
	
	private String				queueName;
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;

	@Autowired
	private TaskViewClickVo		taskViewClickVo;
	@Autowired
	private TaskConversionVo		taskConversionVo;
	
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
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}
	
	public int getQueueSize() {
		int size = 0;
		synchronized (queue) {
			size = queue.size();
		}
		return size; 
	}
	
	

	private class PoolWorker extends Thread {
		public void run() {
			TaskData r;

			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					logger.debug("{} queue before - {}", queueName, queue.size() );
					r = (TaskData) queue.removeFirst();
					logger.info("PERFORMANCE_TEST {} queue after - {}", queueName, queue.size() );
				}

				if (G.ViewClickVo.equals(r.getTask())) {
					taskViewClickVo.mongoToClickHouseV3(r);
				} else if (G.ConversionVo.equals(r.getTask())) {
					taskConversionVo.mongoToClickHouseV3(r);
				}
			}
		}
	}
}