package com.mobon.billing.branchUM.service.dao;

import com.mobon.billing.branchUM.service.MongoUpdate;
import com.mobon.billing.branchUM.service.dao.KnoUMScriptNoDataDao;
import com.mobon.billing.model.v15.BaseCVData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KnoUMScriptNoDataDao {
	private static final Logger logger = LoggerFactory.getLogger(KnoUMScriptNoDataDao.class);

	@Autowired
	private MongoUpdate MongoUpdate;

	public boolean transectionRuningV2(HashMap<String, ArrayList<BaseCVData>> flushMap) {
		boolean result = false;

		try {
			for (Map.Entry<String, ArrayList<BaseCVData>> item : flushMap.entrySet()) {
				String key = item.getKey();
				ArrayList<BaseCVData> data = item.getValue();

				this.MongoUpdate.insertManyBaseCVData("um_stats", key, data);
			}
			result = true;
		} catch (Exception e) {
			logger.error("err ", e);
		}
		return result;
	}
}
