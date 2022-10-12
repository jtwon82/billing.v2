package com.mobon.billing.report.config;

import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 캐쉬 설정..
 * @author Administrator
 *
 */
public class CacheConfig {

	private static CacheManager cm = CacheManager.newInstance();
	
	private static Map campaign = new HashMap();
	private static Map key = new HashMap();
	
	public static Cache getCache(String name) {
		return cm.getCache(name);
	}
	
	public static Cache getCacheCount() {
		return cm.getCache("count");

	}
	
	public static Cache getCachePackage() {
		return cm.getCache("package");
	}
	
	public static Cache getCachePackageFinished() {
		return cm.getCache("package_finished");
	}
	
	/**
	 * adid 캐쉬 
	 * @return
	 */
	public static Cache getCacheADID() {
		return cm.getCache("adid");
	}
	
	public static Map getCachePackageKey() {
//		return cm.getCache("package_key");
		return key;
	}
	
	/**
	 * 앱캠페인정보 캐쉬
	 * @return
	 */
	public static Map getCachePackageCampaign() {
//		return cm.getCache("package_campaign");
		return campaign;
	}

	// @Bean
	// public EhCacheManagerFactoryBean ehCacheCacheManager() {
	// EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
	// cmfb.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
	// cmfb.setShared(true);
	// cmfb.setCacheManagerName("manager");
	// return cmfb;
	// }
}