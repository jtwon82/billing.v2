package com.mobon.billing.branchUM.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.branchUM.service.dao.KnoUMSiteCodeDataDao;
import com.mobon.billing.branchUM.service.dao.SelectDao;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.ConsumerFileUtils;

@Service
public class KnoUMSiteCodeDataToMariaDB {
	private static final Logger logger = LoggerFactory.getLogger(KnoUMSiteCodeDataToMariaDB.class);

	@Value("${log.path}")
	private String logPath;

	@Autowired
	private KnoUMSiteCodeDataDao KnoUMSiteCodeDataDao;

	@Autowired
	private SelectDao selectDao;

	public boolean intoMariaKnoUMSiteCodeDataV3(String _id, List<BaseCVData> aggregateList, boolean toMongodb) {
		boolean result = false;
		long start_millis = System.currentTimeMillis();

		if (aggregateList != null) {

			HashMap<String, ArrayList<BaseCVData>> flushMap = makeFlushMap(aggregateList);

			if (flushMap.keySet().size() != 0) {
				if (toMongodb) {
					try {
						String writeFileName = String.format("insertIntoError_%s_%s",
								new Object[] { Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") });
						ConsumerFileUtils.writeLine(this.logPath + "retry/", writeFileName, G.KnoUMSiteCodeData, aggregateList);
					} catch (IOException e) {
						logger.error("err - {}", e);
					}

					logger.info("chking fail KnoUMSiteCodeData fileWriteOk flushMap.keySet() - {}", flushMap.keySet());
					result = true;
				} else {
					result = this.KnoUMSiteCodeDataDao.transectionRuningV2(flushMap);
				}
			} else {

				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	public HashMap<String, ArrayList<BaseCVData>> makeFlushMap(List<BaseCVData> aggregateList) {
		HashMap<String, ArrayList<BaseCVData>> flushMap = new HashMap<>();

		for (BaseCVData vo : aggregateList) {
			try {
				if (vo != null) {
					if (vo.getYyyymmdd() == null || vo.getPlatform() == null || vo.getAdGubun() == null
							|| vo.getSiteCode() == null || vo.getScriptNo() == 0 || vo.getAdvertiserId() == null
							|| vo.getType() == null) {
						logger.error("Missing required, vo - {}", vo.toString());

						continue;
					}
					if (StringUtils.isEmpty(vo.getProduct()) || StringUtils.isEmpty(vo.getScriptUserId())) {
						BaseCVData minfo = this.selectDao.selectMediaInfo((ClickViewData) vo);
						if (minfo != null) {
							logger.error("map - {}", minfo);

							if (StringUtils.isEmpty(vo.getProduct())) {
								vo.setProduct(StringUtils.trimToNull2(minfo.getProduct()));
								logger.error("chking vo - {}", vo);
							}
							if (StringUtils.isEmpty(vo.getScriptUserId())) {
								vo.setScriptUserId(StringUtils.trimToNull2(minfo.getScriptUserId()));
								logger.error("chking vo - {}", vo);
							}
						}
					}

					if (!StringUtils.isNumeric(vo.getPlatform()))
						vo.setPlatform(G.convertPLATFORM_CODE(vo.getPlatform()));
					if (!StringUtils.isNumeric(vo.getProduct()))
						vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
					if (!StringUtils.isNumeric(vo.getAdGubun()))
						vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));

					if ("03".equals(vo.getProduct())) {
						vo.setClickCnt(vo.getViewCnt());
					}
					add(flushMap, String.format("SITECODE_%s", vo.getYyyymmdd()), vo);
				}

			} catch (Exception e) {
				logger.error("err item - {}", vo, e);
			}
		}
		return flushMap;
	}

	private void add(HashMap<String, ArrayList<BaseCVData>> flushMap, String key, BaseCVData vo) {
		if (flushMap.get(key) == null) {
			flushMap.put(key, new ArrayList<>());
		}
		ArrayList<BaseCVData> l = flushMap.get(key);
		l.add(vo);
	}
}
