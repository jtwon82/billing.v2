package com.mobon.billing.hhtodd.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
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
		this.listYmdKey = obj.listYmdKey;
	}

	public void init() {
		listYmdKey = Collections.synchronizedList(new ArrayList<Map>());
	}
	
	
	
	public synchronized List<?> removeYmdMapData() {
		List<?> result = null;
		result = this.listYmdKey;
		listYmdKey = Collections.synchronizedList(new ArrayList<Map>());
		return result;
	}
	public void appendYmdMapData(Map record) {
		synchronized (listYmdKey) {
			logger.info("append {}", record);
			if (listYmdKey.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jsonObject = JSONObject.fromObject(record);
				try {
					ConsumerFileUtils.writeLine(logPath+"retry/",writeFileName, G.YmdMapData, jsonObject);
				} catch (Exception e) {

				}
			} else  {
				if (listYmdKey == null) {
					listYmdKey = Collections.synchronizedList(new ArrayList<Map>());
				}
				listYmdKey.add(record);
			}
		}
	}
	

	private List<Map> listYmdKey= Collections.synchronizedList(new ArrayList<Map>());


}
