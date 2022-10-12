package com.mobon.billing.subjectCopy.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.subjectCopy.vo.ConversionPolling;
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

	private Map<String, BaseCVData> mapSubjectCopyClickViewData = (new ConcurrentHashMap<String, BaseCVData>());
	private List< ConversionPolling> listSubjectCopyConvData = Collections.synchronizedList(new ArrayList<ConversionPolling>());
	
	
	public SumObjectManager(SumObjectManager obj) {
		this.mapSubjectCopyClickViewData = obj.mapSubjectCopyClickViewData;
		this.listSubjectCopyConvData = obj.listSubjectCopyConvData;
	}
	
	public Map<String, BaseCVData> getMapSubjectCopyClickViewData() {
		return mapSubjectCopyClickViewData;
	}

	public void setMapSubjectCopyClickViewData(Map<String, BaseCVData> mapSubjectCopyClickViewData) {
		this.mapSubjectCopyClickViewData = mapSubjectCopyClickViewData;
	}
	
	public List<ConversionPolling> getlistSubjectCopyConvData() {
		return listSubjectCopyConvData;
	}

	public void setlistSubjectCopyConvData(List<ConversionPolling> listSubjectCopyConvData) {
		this.listSubjectCopyConvData = listSubjectCopyConvData;
	}

	public Map<String, BaseCVData> getMapClickViewData() {
		return mapSubjectCopyClickViewData;
	}

	public SumObjectManager() {
		logger.debug(">> SumObjectManager init ");
		init();
	}

	private void init() {
		mapSubjectCopyClickViewData = (new ConcurrentHashMap<String, BaseCVData>());
		listSubjectCopyConvData = Collections.synchronizedList(new ArrayList<ConversionPolling>());
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}
	
	public synchronized void appendSubjectCopyClickViewData(BaseCVData record) {
		if ( mapSubjectCopyClickViewData.size() > summeryListSize) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError_append", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.SubjectCopyClickView, jJSONObject);
			}catch(Exception e) {				
				
			}
		} else {
			BaseCVData sum = mapSubjectCopyClickViewData.get(record.getKeyCodeSubjectCopy());
			if ( sum == null ) {
				mapSubjectCopyClickViewData.put(record.getKeyCodeSubjectCopy(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}
	public Map<String, ?> removeSubjectCopyClickViewData() {
		Map<String , ?> result = null;
		result = this.mapSubjectCopyClickViewData;
		mapSubjectCopyClickViewData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public List<?> removeSubjectCopyConvData(){
		List <?> result = null;
		result = this.listSubjectCopyConvData;
		listSubjectCopyConvData = Collections.synchronizedList(new ArrayList<ConversionPolling>());
		return result;
		
	}

	public void appendSubjectCopyConvData(ConversionPolling record) {
		if ( listSubjectCopyConvData.size() > summeryListSize) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.SuccConversion, jJSONObject);
			}catch(Exception e) {				
				
			}
		} else {
			if (listSubjectCopyConvData == null) {
				listSubjectCopyConvData =  Collections.synchronizedList(new ArrayList<ConversionPolling>());
			}	
			listSubjectCopyConvData.add(record);
			
		}
		
	}

}
