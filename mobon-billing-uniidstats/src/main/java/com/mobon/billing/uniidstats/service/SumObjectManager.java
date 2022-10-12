package com.mobon.billing.uniidstats.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mobon.billing.uniidstats.model.UseridVo;

@Service
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);
	private static SumObjectManager INSTANCE = null;
	private Map<String, UseridVo> mapUseridVo = Collections.synchronizedMap(new HashMap<String, UseridVo>());

	public SumObjectManager() {
		init();
	}

	public SumObjectManager(SumObjectManager obj) {
		this.mapUseridVo = obj.mapUseridVo;
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}

	public void init() {
		mapUseridVo = Collections.synchronizedMap(new HashMap<String, UseridVo>());
	}

	/**
	 * UniId
	 */
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

	/**
	 * Getter, Setter
	 */
	public Map<String, UseridVo> getMapUseridVo() {
		return mapUseridVo;
	}

	public void setMapUseridVo(Map<String, UseridVo> mapUseridVo) {
		this.mapUseridVo = mapUseridVo;
	}

}
