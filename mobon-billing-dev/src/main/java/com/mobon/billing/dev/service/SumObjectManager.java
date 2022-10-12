package com.mobon.billing.dev.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobon.billing.dev.model.PollingData;


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

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}

	public SumObjectManager(SumObjectManager obj) {
		this.mapPollingData = obj.mapPollingData;
	}
	
	public synchronized Map<String, ?> removePollingDataMap() {
		Map<String, ?> result = null;
		
		result = this.mapPollingData;
		mapPollingData = (new ConcurrentHashMap<String, PollingData>());
		
		return result;
	}

	public void init() {
		mapPollingData = (new ConcurrentHashMap<String, PollingData>());
	}
	
	public synchronized void appendPollingData(PollingData record) {
		PollingData sum = mapPollingData.get(record.getSiteCodeScriptNo());
		if (sum == null) {
			mapPollingData.put(record.getSiteCodeScriptNo(), record);
		} else {
			sum.sumGethering(record);
		}
	}
	
	private Map<String, PollingData> mapPollingData= (new ConcurrentHashMap<String, PollingData>());


	public Map<String, PollingData> getMapPollingData() {
		return mapPollingData;
	}

	public void setMapPollingData(Map<String, PollingData> mapPollingData) {
		this.mapPollingData = mapPollingData;
	}

}
