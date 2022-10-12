package com.mobon.billing.report.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
public class PackageTask {

	@Autowired
	private PackageDao packageDao;
	static Logger logger = Logger.getLogger(PackageTask.class);
	
	public void execute() {
		
		Element e = CacheConfig.getCachePackage().get("package");
		if(e != null) {
			Map _map = (Map) ((HashMap) e.getObjectValue()).clone();
			CacheConfig.getCachePackage().put(new Element("package", new HashMap()));
			if(_map != null) {
				logger.info("package : " + _map.size());
				for(Iterator it = _map.keySet().iterator(); it.hasNext();) {
					Object keys = it.next();
					if(keys != null) {
						String __key = String.valueOf(keys);
						String[] _key = __key.split("_");
						if(_key.length == 2) {
							String os = _key[0];
							String key = _key[1];
							Object _pkg = CacheConfig.getCachePackageKey().get(key.substring(0, 50));
							if(_pkg == null) {							
								Map<String, Object> parameters = new HashMap<String, Object>();
								parameters.put("OS_TP_CODE", os);
								if(key.length() <= 50) {
									parameters.put("APP_PKG_NM", key);
								} else {
									parameters.put("APP_PKG_NM", key.substring(0, 50));
								}
								parameters.put("APP_TITLE_NM", "");
								parameters.put("APP_LAST_ALT_DT", new Date());
								parameters.put("APP_CTGR_SEQ", null);
								parameters.put("REG_USER_ID", "");
								parameters.put("REG_DTTM", new Date());
								parameters.put("ALT_USER_ID", "");
								parameters.put("ALT_DTTM", new Date());
								
//								if(false) {
									packageDao.insertAPP_INFO(parameters);
//								}
								
									
							}
							CacheConfig.getCachePackageFinished().put(new Element(__key, true));
//							CacheConfig.getCachePackageFinished().put(new Element(__key, true));
						}
					}
					
				}
			}
			logger.info("CacheConfig.getCachePackageFinished() + package : " + CacheConfig.getCachePackageFinished().getKeys().size());
//			System.out.println("package : null"+_map.toString());
		}
	}
}
