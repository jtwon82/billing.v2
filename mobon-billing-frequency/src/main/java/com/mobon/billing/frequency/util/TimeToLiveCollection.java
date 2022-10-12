package com.mobon.billing.frequency.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeToLiveCollection {

	private static Map<String, Integer> contents = ( new ConcurrentHashMap<String, Integer>() );

	public void add(String item, int timeToLive) {
		contents.put(item, timeToLive);
	}
	
	public boolean remove(String item) {
		boolean bool = false; 
		if( contents.get(item)!=null ) {
			contents.remove(item);
		}
		bool = true;
		
		return bool;
	}
	
	public boolean contains(String item) {
		boolean bool = false;
		if( contents.get(item)!=null ) {
			if( contents.get(item)>0 ) {
				contents.put(item, contents.get(item)-1 );
				bool = true;
			}
		}
		
		return bool;
	}

}