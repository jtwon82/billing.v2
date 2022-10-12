package com.mobon.billing.viewclicklog.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.model.v20.ViewClickVo;

@Component
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);

	private static SumObjectManager INSTANCE = null;
	private Map<String, ViewClickVo> mapViewClickVo = Collections.synchronizedMap(new HashMap<String, ViewClickVo>());
	private Map<String, ConversionVo> mapConversionVo = Collections.synchronizedMap(new HashMap<String, ConversionVo>());

	public SumObjectManager() {
		init();
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}

	public void init() {
		mapViewClickVo = Collections.synchronizedMap(new HashMap<String, ViewClickVo>());
		mapConversionVo = Collections.synchronizedMap(new HashMap<String, ConversionVo>());
	}

	/**
	 * ViewClick
	 */
	public void appendViewClickVo(ViewClickVo record) {
		synchronized(mapViewClickVo) {
			ViewClickVo sum = mapViewClickVo.get(record.getKey());
			if (sum == null) {
				mapViewClickVo.put(record.getKey(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public Map<String, ?> removeViewClickVo() {
		Map<String, ?> result = null;
		synchronized(mapViewClickVo) {
			result = this.mapViewClickVo;
			mapViewClickVo = Collections.synchronizedMap(new HashMap<String, ViewClickVo>());
		}
		return result;
	}

	/**
	 * Conversion
	 */
	public void appendConversionVo(ConversionVo record) {
		synchronized (mapConversionVo) {
			ConversionVo sum = mapConversionVo.get(record.getKey());

			if (sum == null) {
				mapConversionVo.put(record.getKey(), record);
			} else {
				// 키가 중복될경우 트래커 전환일때만 처리
				if (!"90".equals(sum.getChrgTpCode())) {
					sum.sumGethering(record);
				}
			}
		}
	}

	public Map<String, ?> removeConversionVo() {
		Map<String, ?> result = null;
		synchronized(mapConversionVo) {
			result = this.mapConversionVo;
			mapConversionVo = Collections.synchronizedMap(new HashMap<String, ConversionVo>());
		}
		return result;
	}

	/**
	 * Getter, Setter
	 */
	public Map<String, ViewClickVo> getMapViewClickVo() {
		return mapViewClickVo;
	}

	public void setMapViewClickVo(Map<String, ViewClickVo> mapViewClickVo) {
		this.mapViewClickVo = mapViewClickVo;
	}

	public Map<String, ConversionVo> getMapConversionVo() {
		return mapConversionVo;
	}

	public void setMapConversionVo(Map<String, ConversionVo> mapConversionVo) {
		this.mapConversionVo = mapConversionVo;
	}

}
