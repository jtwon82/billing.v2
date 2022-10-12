package com.mobon.billing.core.service.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ClickHouseDao {
    private static final Logger logger = LoggerFactory.getLogger(ClickHouseDao.class);

    public static final String	NAMESPACE	= "actionRenewLogDataMapper";

    @Resource(name = "sqlSessionTemplateClickhouse")
    private SqlSessionTemplate sqlSessionTemplateClickhouse;

    @Resource (name = "sqlSessionTemplateDream")
    private SqlSessionTemplate sqlSessionTemplateDream;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public Map<String, Object> selectActionLogData(HashMap<String, Object> data) {
        Map<String ,Object> result = new HashMap<String , Object>();
        try {
            result = sqlSessionTemplateClickhouse.selectOne(String.format("%s.%s",NAMESPACE, "selectActionRenewLog"), data);
        } catch (Exception e ) {
            logger.error("Error To Select ClickHouse ActionLog- {} ", data);
        }
        return result;
    }
}
