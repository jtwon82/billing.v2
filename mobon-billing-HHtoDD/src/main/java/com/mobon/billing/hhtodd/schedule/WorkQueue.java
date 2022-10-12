package com.mobon.billing.hhtodd.schedule;

import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobon.billing.hhtodd.service.dao.HHtoDDDao;

public class WorkQueue {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueue.class);
	
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;
	
	@Autowired
	private HHtoDDDao				hHtoDDDao;
	
	public WorkQueue(String queueName) {
		logger.debug("queueName - {} START", queueName);
		
		queue = new LinkedList();

		threads = new PoolWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	
	public WorkQueue(String queueName, int nThreads) {
		logger.debug("queueName - {} START", queueName);
		
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(Map r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread {
		public void run() {
			Map r;

			while (true) {
				synchronized (queue) {
					while ( queue.isEmpty() ) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					logger.debug("queue.size() remove before - {}", queue.size() );
					r = (Map) queue.removeFirst();
					logger.info("queue.size() remove after - {}", queue.size() );
				}

				hHtoDDDao.transectionRuningDDtoDD(r);
			}
		}
	}
}