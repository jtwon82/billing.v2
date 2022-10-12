package com.mobon.billing.dev.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobon.billing.dev.schedule.TaskPollingData;
import com.mobon.exschedule.model.TaskData;

public class WorkQueueTaskData {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueueTaskData.class);
	
	private String				queueName;
	private int					nThreads = 5;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;

	@Autowired
	private TaskPollingData	TaskPollingData;
	
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
					while ( queue.isEmpty() ) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					logger.info("{} queue before - {}", queueName, queue.size() );
					r = (TaskData) queue.removeFirst();
					logger.info("{} queue after - {}", queueName, queue.size() );
				}

//				if (r.getTask().equals(G.near_info) ) {
//					TaskPollingData.mongoToMariaV3(r);
//				}
//				try {
////					this.sleep(100);
//				} catch (InterruptedException e) {
//				}
				TaskPollingData.mongoToMariaV3(r);
			}
		}
	}
}