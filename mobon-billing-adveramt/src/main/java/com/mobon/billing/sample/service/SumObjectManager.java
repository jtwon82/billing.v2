package com.mobon.billing.sample.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mobon.billing.sample.model.SampleVo;
import com.mobon.billing.sample.model.UseridVo;

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
	

	public void appendUseridVo(UseridVo record) {
		synchronized(mapUseridVo) {
			UseridVo sum = mapUseridVo.get(record.getKey());
			if (sum == null) {
				mapUseridVo.put(record.getKey(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public Map<String, ?> removeUseridVo() {
		Map<String, ?> result = null;
		synchronized(mapUseridVo) {
			result = this.mapUseridVo;
			mapUseridVo = Collections.synchronizedMap(new HashMap<String, UseridVo>());
		}
		return result;
	}
	
	
	
	
	

	public SumObjectManager(SumObjectManager obj) {
		this.mapSampleVo = obj.mapSampleVo;
		this.mapUseridVo = obj.mapUseridVo;
	}

	public void init() {
		mapSampleVo = Collections.synchronizedMap(new HashMap<String, SampleVo>());
		mapUseridVo = Collections.synchronizedMap(new HashMap<String, UseridVo>());
	}

	public void clear() {
		this.mapSampleVo.clear();
		this.mapUseridVo.clear();
	}
	
	
	
	private Map<String, SampleVo> mapSampleVo = Collections.synchronizedMap(new HashMap<String, SampleVo>());	// near
	private Map<String, UseridVo> mapUseridVo = Collections.synchronizedMap(new HashMap<String, UseridVo>());	// near

	public Map<String, SampleVo> getMapSampleVo() {
		return mapSampleVo;
	}

	public void setMapSampleVo(Map<String, SampleVo> mapSampleVo) {
		this.mapSampleVo = mapSampleVo;
	}

	public Map<String, UseridVo> getMapUseridVo() {
		return mapUseridVo;
	}

	public void setMapUseridVo(Map<String, UseridVo> mapUseridVo) {
		this.mapUseridVo = mapUseridVo;
	}



}
