package com.adgather.user;

import java.text.DecimalFormat;


public class TestTimeChecker {
	private final static boolean IS_NOT_USE = true;
	private long startTime;
	private long endTime;
	public void testStart() {
		if(IS_NOT_USE)	return;
		
		startTime = _getTime();
		//System.out.println("START "+ startTime);
	}
	
	public void testEnd(String type) {
		if(IS_NOT_USE)	return;
		
		endTime = _getTime();
		testPrint(type);
		//System.out.println("END "+ endTime); 
	}
	
	private void testPrint(String type) {
		if(IS_NOT_USE)	return;
		
		System.out.println("[" + type + "]" +_getProgressTime());
	}
	
	private long _getTime() {
		//return System.currentTimeMillis();
		return System.nanoTime();
	}
	
	private String _getProgressTime() {
		//return (endTime - startTime);
		DecimalFormat df = new DecimalFormat("#,##0.000");
		return df.format((endTime - startTime)/1000000.0);
	}
}
