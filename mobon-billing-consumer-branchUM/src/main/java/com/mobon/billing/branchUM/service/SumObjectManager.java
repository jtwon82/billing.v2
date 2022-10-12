package com.mobon.billing.branchUM.service;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

import net.sf.json.JSONObject;

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
		this.mapKnoUMSiteCodeData = obj.mapKnoUMSiteCodeData;
		this.mapKnoUMScriptNoData = obj.mapKnoUMScriptNoData;
		this.mapKnoKpiData = obj.mapKnoKpiData;
	}

	public void init() {
		mapKnoUMSiteCodeData = (new ConcurrentHashMap<String, BaseCVData>());
		mapKnoUMScriptNoData = (new ConcurrentHashMap<String, BaseCVData>());
		mapKnoKpiData = (new ConcurrentHashMap<String, BaseCVData>());
	}


	public synchronized void appendKnoUMSiteCodeData(BaseCVData record) {
		if( mapKnoUMSiteCodeData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.KnoUMSiteCodeData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapKnoUMSiteCodeData.get(record.getKeyCodeUMSiteCode());
			if (sum == null) {
				mapKnoUMSiteCodeData.put(record.getKeyCodeUMSiteCode(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendKnoUMScriptNoData(BaseCVData record) {
		if( mapKnoUMScriptNoData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.KnoUMScriptNoData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapKnoUMScriptNoData.get(record.getKeyCodeUMScriptNo());
			if (sum == null) {
				mapKnoUMScriptNoData.put(record.getKeyCodeUMScriptNo(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendKnoKpiData(BaseCVData record) {
		if( mapKnoKpiData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.KnoKpiData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapKnoKpiData.get(record.getKeyCodeKpiNo());
			if (sum == null) {
				mapKnoKpiData.put(record.getKeyCodeKpiNo(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}
	
	public synchronized Map<String, ?> removeKnoUMSiteCodeMap() {
		Map<String, ?> result = null;
		result = this.mapKnoUMSiteCodeData;
		mapKnoUMSiteCodeData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeKnoUMScriptNoMap() {
		Map<String, ?> result = null;
		result = this.mapKnoUMScriptNoData;
		mapKnoUMScriptNoData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeKnoKpiMap() {
		Map<String, ?> result = null;
		result = this.mapKnoKpiData;
		mapKnoKpiData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	
	
	
	
	
	private Map<String, BaseCVData> mapKnoUMSiteCodeData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapKnoUMScriptNoData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapKnoKpiData = (new ConcurrentHashMap<String, BaseCVData>());

	public Map<String, BaseCVData> getMapKnoUMSiteCodeData() {
		return mapKnoUMSiteCodeData;
	}

	public void setMapKnoUMSiteCodeData(Map<String, BaseCVData> mapKnoUMData) {
		this.mapKnoUMSiteCodeData = mapKnoUMData;
	}

	public Map<String, BaseCVData> getMapKnoUMScriptNoData() {
		return mapKnoUMScriptNoData;
	}

	public void setMapKnoUMScriptNoData(Map<String, BaseCVData> mapKnoUMScriptNoData) {
		this.mapKnoUMScriptNoData = mapKnoUMScriptNoData;
	}
	
	public Map<String, BaseCVData> getMapKnoKpiData() {
		return mapKnoKpiData;
	}

	public void setMapKnoKpiData(Map<String, BaseCVData> mapKnoKpiData) {
		this.mapKnoKpiData = mapKnoKpiData;
	}



	





}
