package com.mobon.billing.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeToLiveCollection {

	private static final Logger		logger		= LoggerFactory.getLogger(TimeToLiveCollection.class);
	
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
		try {
			if( contents.get(item)!=null ) {
				if( contents.get(item)>0 ) {
					contents.put(item, contents.get(item)-1 );
					bool = true;
				}
			}
		} catch(Exception e) {
			logger.error("err ", e);
			bool= true;
		}
		return bool;
	}

}