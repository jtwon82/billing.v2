package com.mobon.billing.subjectCopy.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adgather.constants.G;
import com.mobon.billing.subjectCopy.schedule.TaskSubjectCopyClickViewData;
import com.mobon.billing.subjectCopy.schedule.TaskSubjectCopyConvData;
import com.mobon.exschedule.model.TaskData;


public class WorkQueueTaskData {
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueueTaskData.class);
	
	private String				queueName;
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;
	
	@Autowired
	private TaskSubjectCopyClickViewData taskSubjectCopyClickViewData;
	
	@Autowired
	private TaskSubjectCopyConvData taskSubjectCopyConvData;
	
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
				if( r.getTask().equals(G.SubjectCopyClickView) ) {
					if(taskSubjectCopyClickViewData!=null)
						taskSubjectCopyClickViewData.mongoToClickHouseV3(r);
					else
						logger.error("taskSubjectCopyClickViewData nothing");
					
				} else if (r.getTask().equals(G.SuccConversion)){
					if (taskSubjectCopyConvData != null) {
						taskSubjectCopyConvData.mongoToClickHouseV3(r);
					} else {
						logger.error("taskSubjectCopyConvData nothing");
					}
				} 
			}
		}
	}
}
