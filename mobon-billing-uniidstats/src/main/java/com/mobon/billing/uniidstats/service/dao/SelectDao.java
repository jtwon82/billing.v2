package com.mobon.billing.uniidstats.service.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SelectDao {

	private static final Logger	logger = LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	public static final String NAMESPACE = "selectMapper";

	/**
	 * 타게팅 여분 구분된 타게팅 리스트 조회
	 */
	public List<Map<String, String>> getAdvrtsTpCode() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsTpCode"));
	}

	/**
	 * 해당 사이트코드의 정보 조회
	 */
	public Map<String,String> selectAdvrtsInfo(String siteCode) {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectAdvrtsInfo"),siteCode);
	}

	/**
	 * 해당 매체스크립트 번호의 정보 조회
	 */
	public Map<String,String> selectMediaInfo(String scriptNo) {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectMediaInfo"),scriptNo);
	}

}
