package com.mobon.billing.report.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.service.dao.PackageDao;

@Configuration
@Service
public class PackageKeyTask {

	static Logger logger = Logger.getLogger(PackageKeyTask.class);
	
	@Autowired
	private PackageDao packageDao;
	
	public static boolean START_SHOP_LOG = true;
	
	public void execute() {
		logger.info(PackageKeyTask.class.getName());
		campaign();
		
		int count = packageDao.selectAppInfoCount();
		int size = (count/1000);
		if(size == 0) {
			size = 1;
		}
		logger.info(count + " : " +  size);
		for(int i=0; i<size; i++) {
			Map<String, Integer> parameter = new HashMap();
			if(i == 0) {
				parameter.put("OFFSET", i);
			} else {
				parameter.put("OFFSET", i*1000);
			}
			List<Map<String, String>> rows = packageDao.selectAppInfo(parameter);
			for(int k=0; k<rows.size(); k++) {
				Map<String, String> row = rows.get(k);
//				if(row.get("APP_PKG_NM").equals("com.cocoa.cocoa_18439_57c7d4d996cfc")) {
//					logger.info(row.get("APP_PKG_NM"));
//				}
				if(CacheConfig.getCachePackageKey().get(row.get("APP_PKG_NM")) == null) {
					CacheConfig.getCachePackageKey().put(row.get("APP_PKG_NM"), row.get("APP_SEQ"));
				}
				
			}
			logger.info(i + " - rows.size() : " + rows.size());
//			System.out.println(rows.toString());
		}
//		logger.info(CacheConfig.getCachePackageKey().get("com.cocoa.cocoa_18439_57c7d4d996cfc"));
//		campaign();
		logger.info("[getCachePackageKey] - total() : " + CacheConfig.getCachePackageKey().size());
		START_SHOP_LOG = false;
//		START_SHOP_LOG = true;
	}
	
	private void campaign() {
		int count = packageDao.selectAppCampInfoCount();
		int size = (count/1000);
		if(size == 0) {
			size = 1;
		}
		logger.info(count + " : " +  size);
		for(int i=0; i<size; i++) {
			Map<String, Integer> parameter = new HashMap();
			if(i == 0) {
				parameter.put("OFFSET", i);
			} else {
				parameter.put("OFFSET", i*1000);
			}
			List<Map<String, String>> rows = packageDao.selectAppCampInfo(parameter);
			for(int k=0; k<rows.size(); k++) {
				Map<String, String> row = rows.get(k);
//				logger.info(row.get("APP_SEQ"));
				CacheConfig.getCachePackageCampaign().put("CAMPAIGN_"+String.valueOf(row.get("APP_SEQ")), "true");
			}
			logger.info(i + " [campaign] - rows.size() : " + rows.size());
//			System.out.println(rows.toString());
		}
		
		logger.info("[campaign] - total() : " + CacheConfig.getCachePackageCampaign().size());
//		logger.info("[campaign] - total() : " + CacheConfig.getCachePackageCampaign().getKeys().size());
	}
}
