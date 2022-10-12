package com.mobon.billing.framertb.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class WorkQueue {
	
	private static final Logger		logger	= LoggerFactory.getLogger(WorkQueue.class);
	
	private int					nThreads = 1;
	private final PoolWorker[]	threads;
	private final LinkedList	queue;
	
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

	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread {
		public void run() {
			Runnable r;

			while (true) {
				synchronized (queue) {
					while ( queue.isEmpty() ) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}
					
					logger.debug("queue.size() remove before - {}", queue.size() );
					r = (Runnable) queue.removeFirst();
					logger.debug("queue.size() remove after - {}", queue.size() );
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				Thread T = null;
				try {
					T = new Thread(r);
					T.start();
				} catch (Exception e) {
				} finally {
				}
			}
		}
	}
}