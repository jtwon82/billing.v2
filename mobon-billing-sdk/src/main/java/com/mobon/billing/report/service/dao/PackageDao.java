package com.mobon.billing.report.service.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PackageDao {

	private static final Logger	logger = LoggerFactory.getLogger(PackageDao.class);

	@Resource (name = "sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDream;

	public static final String NAMESPACE = "sdkMapper";
	
	public int selectAppCampInfoCount() {
		return sqlSessionTemplateDream.selectOne(NAMESPACE +".selectAppCampInfoCount", null);
	}
	
	public List<Map<String, String>> selectAppCampInfo(Map<String, Integer> parameter) {
		return sqlSessionTemplateDream.selectList(NAMESPACE +".selectAppCampInfo", parameter);
	}
	
	public int selectAppInfoCount() {
		return sqlSessionTemplateDream.selectOne(NAMESPACE +".selectAppInfoCount", null);
	}
	
	public List<Map<String, String>> selectAppInfo(Map<String, Integer> parameter) {
		return sqlSessionTemplateDream.selectList(NAMESPACE +".selectAppInfo", parameter);
	}
	
	public void insertAPP_INFO(Map<String, Object> parameter) {
		sqlSessionTemplateDream.insert(NAMESPACE +".insertAPP_INFO", parameter);
	}
	
	public void insertAPP_USER_TRML_INFO(Map<String, Object> parameter) throws Exception {
		sqlSessionTemplateDream.insert(NAMESPACE +".insertAPP_USER_TRML_INFO", parameter);
	}
	
}
