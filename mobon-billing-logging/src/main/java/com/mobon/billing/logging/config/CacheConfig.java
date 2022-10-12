package com.mobon.billing.logging.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

@Component
@EnableCaching
public class CacheConfig {
    
    @Autowired
    private CacheManager cache;

    public boolean getCache(String key, String ckey) {
    	boolean bool = true;
		Cache c = cache.getCache(ckey);
		if(c.get(key)==null) {
			c.put(key, key);
			bool = false;
		}
		
		return bool;
    }
    public boolean getCacheDayOfpar(String key) {
    	boolean bool = true;
		Cache c = cache.getCache("DayOfPar");
		if(c.get(key)==null) {
			c.put(key, key);
			bool = false;
		}
		
		return bool;
    }
    
}