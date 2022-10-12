package com.mobon.billing.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.vo.DataBaseVo;

@Repository
public class DataRecoveryDaoImpl implements DataRecoveryDao {
	
	private static final Logger	logger	= LoggerFactory.getLogger(DataRecoveryDao.class);

	public static final String	NAMESPACE	= "databaseInsertMapper";
	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@Override
	public Boolean setData(List<DataBaseVo> list, String queryName) {
		try {			
			 sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, queryName), list);			
		} catch (Exception e) {
			logger.error("Error DB INSERT" + e);
			return false;
		} finally {
			sqlSessionTemplateBilling.flushStatements();			
		}
		return true;
	}

}
