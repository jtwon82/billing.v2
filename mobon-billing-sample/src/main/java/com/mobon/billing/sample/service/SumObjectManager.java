package com.mobon.billing.sample.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mobon.billing.sample.model.SampleVo;

@Service
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);

	private static SumObjectManager INSTANCE = null;


	public SumObjectManager() {
		logger.debug(">> SumObjectManager init ");
		init();
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}

	public void appendSampleVo(SampleVo record) {
		synchronized(mapSampleVo) {
			SampleVo sum = mapSampleVo.get(record.generateKey());
			if (sum == null) {
				mapSampleVo.put(record.generateKey(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public Map<String, ?> removeSampleVo() {
		Map<String, ?> result = null;
		synchronized(mapSampleVo) {
			result = this.mapSampleVo;
			mapSampleVo = Collections.synchronizedMap(new HashMap<String, SampleVo>());
		}
		return result;
	}
	
	
	
	
	
	

	public SumObjectManager(SumObjectManager obj) {
		this.mapSampleVo = obj.mapSampleVo;
	}

	public void init() {
		mapSampleVo = Collections.synchronizedMap(new HashMap<String, SampleVo>());
	}

	public void clear() {
		this.mapSampleVo.clear();
	}
	
	
	
	private Map<String, SampleVo> mapSampleVo = Collections.synchronizedMap(new HashMap<String, SampleVo>());	// near

	public Map<String, SampleVo> getMapSampleVo() {
		return mapSampleVo;
	}

	public void setMapSampleVo(Map<String, SampleVo> mapSampleVo) {
		this.mapSampleVo = mapSampleVo;
	}



}
