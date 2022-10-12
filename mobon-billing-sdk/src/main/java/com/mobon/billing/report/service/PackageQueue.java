package com.mobon.billing.report.service;

import java.util.HashMap;
import java.util.Map;

public class PackageQueue {

	public static void main(String args[]) {
		Map m = new HashMap(10000);
		for(int i=0; i<100000; i++) {
			m.put("com.daum.net"+i, "true"+i);
		}
		
		System.out.println(m.get("com.daum.net10000"));
	}
}
