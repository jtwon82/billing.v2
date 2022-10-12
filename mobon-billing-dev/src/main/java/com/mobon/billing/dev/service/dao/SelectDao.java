package com.mobon.billing.dev.service.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SelectDao {

	private static final Logger	logger				= LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "selectMapper";

	public Map selectNow() {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectNow"),null);
	}

}
