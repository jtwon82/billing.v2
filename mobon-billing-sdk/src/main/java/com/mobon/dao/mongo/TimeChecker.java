package com.mobon.dao.mongo;

public class TimeChecker {
	private long startTime;
	
	public static TimeChecker start() {
		return new TimeChecker();
	}
	
	private TimeChecker() {
		startTime = System.currentTimeMillis();
	}

	public long end() {
		return System.currentTimeMillis() - startTime;
	}
}
