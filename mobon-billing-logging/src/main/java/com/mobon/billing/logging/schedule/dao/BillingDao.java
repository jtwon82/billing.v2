package com.mobon.billing.logging.schedule.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BillingDao {
	private static final Logger	LOG	= LoggerFactory.getLogger(BillingDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "billingMapper";
	
	
	
	public Map getCurrentData(){
		return (Map) sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "getCurrentData"));
	}
	
	public List chkingBeforeHourData() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "chkingBeforeHourData"));
	}
	
	public List selectChkingBatchRuningTime() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectChkingBatchRuningTime"));
	}
	
	public List selectReportCtr() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectReportCtr"));
	}

	public List<Map> selectMediaChrgMonitor() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectMediaChrgMonitor"));
	}
	
	public List<Map> selectFrameMonitor() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectFrameMonitor"));
	}
	
	public List<Map<String, String>> selectRebuildConversionMonitor() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectRebuildConversionMonitor"));
	}
	
	public List<Map> selectUserCtgrMonitor() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectUserCtgrMonitor"));
	}
	
	public List<Map> selectChkingZeroViewClickConv() {
		return sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectChkingZeroViewClickConv"));
	}

	public Map<String, Object> selectEmptyAdverId() {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectEmptyAdverId"));
	}

	public Map<String, Object> selectInvalidItlTpCodeForKakao() {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectInvalidItlTpCodeForKakao"));
	}

	public Map<String, Object> selectInvalidPerformanceAd() {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectInvalidPerformanceAd"));
	}

	public Map<String, Object> selectInvalidEmptyValue() {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectInvalidEmptyValue"));
	}
}
