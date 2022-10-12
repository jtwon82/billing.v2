package com.mobon.billing.report.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobon.billing.model.v15.NativeNonAdReportData;

@Service
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);

	private static SumObjectManager INSTANCE = null;

	@Value("${log.path}")
	private String	logPath;
	@Value("${summery.list.size}")
	private int	summeryListSize;

	
	public SumObjectManager() {
		logger.debug(">> SumObjectManager init ");
		init();
	}
	
	public void init() {
		mapNativeNonAdReportData = new ConcurrentHashMap<String, NativeNonAdReportData>();
	}
	
	public void clear() {
		this.mapNativeNonAdReportData.clear();
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}
	
	public SumObjectManager(SumObjectManager obj) {
		this.mapNativeNonAdReportData = obj.mapNativeNonAdReportData;
	}

	private Map<String, NativeNonAdReportData> mapNativeNonAdReportData = new ConcurrentHashMap<String, NativeNonAdReportData>();	// nativeNonAdReport

	public Map<String, NativeNonAdReportData> getMapNativeNonAdReportData() {
		return mapNativeNonAdReportData;
	}

	public void setMapNativeNonAdReportData(Map<String, NativeNonAdReportData> mapNativeNonAdReportData) {
		this.mapNativeNonAdReportData = mapNativeNonAdReportData;
	}

	public Map<String, ?> removeObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof NativeNonAdReportData) {
			synchronized(mapNativeNonAdReportData) {
				result = this.mapNativeNonAdReportData;
				mapNativeNonAdReportData = new ConcurrentHashMap<String, NativeNonAdReportData>();
			}
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}

	public void appendNativeNonAdReportData(NativeNonAdReportData record) {
		synchronized(mapNativeNonAdReportData) {
			NativeNonAdReportData sum = mapNativeNonAdReportData.get(record.generateKey());
			if (sum == null) {
				mapNativeNonAdReportData.put(record.generateKey(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
}
