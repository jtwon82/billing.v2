package com.mobon.billing.framertb.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mobon.billing.framertb.model.PollingData;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;

@Repository
public class SelectDao {

	private static final Logger	logger				= LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "selectMapper";


	public ArrayList selectGroupKey() {
		return (ArrayList) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey"));
	}

	public List getAdvrtsTpCode() {
		return (List) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsTpCode"));
	}

	public Map<String, Object> getActionLog(PollingData item) {
		List <Map<String,Object>> list = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectInhourActionLog"), item);
		Map<String, Object> result = new HashMap<String,Object>();
		try {			
			if (list.size() > 0) {
				result = list.get(0);				
			} else {
				result = null;
			}
		} catch (Exception e) {
			logger.error("####Conversion Data no ActionLog = " + item.getIp());
			result = null;
		}
		return result;
	}

	public List getDisCollectMediaNo() {
		
		return (List) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE,"selectDisCollectMediaNo"));
	}
}
