package com.mobon.billing.core.service.dao;
//
//import java.util.List;
//import java.util.Map;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
//
//import com.mobon.billing.model.v15.BaseCVData;
//
@Repository
public class CommonDao {

	private static final Logger	logger				= LoggerFactory.getLogger(CommonDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "commonMapper";
	
	public List getAdvrtsTpCode() {
		// retrun (List) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsTpCode"));
		return (List) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectNewAdvrtsTpCode"));
	}
	
	
//	public Map getCurrentData(){
//		return (Map) sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "getCurrentData"));
//	}
//	
//	public List<String> getDateBetween(Map map){
//		List<String> result = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "select_date_between"), map);
//		return result;
//	}
//	
//	public int getDateDiff(Map map){
//		int result = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "select_date_diff"), map);
//		return result;
//	}
//	
//	
//	
//	
//	
//
//	public void updateAdsiteTable(Map map){
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep1"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep2"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep3"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep4"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep7"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
//	public void updateIAdsiteTable(Map map){
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep1"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep2"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep3"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep4"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep7"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
//	public void updateAdsiteRtbInfoTable(Map map){
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep1"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep2"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep3"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep4"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			//sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep7"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
//	
//	
//	
//	
//	
//	
//	public void updateAdmemberTable(Map map){
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep1"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep2"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep3"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep4"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
//
//	public void deleteTableData(Map map) {
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep5"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdmemberTableStep6"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep5"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteTableStep6"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep5"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateIAdsiteTableStep6"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep5"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateAdsiteRtbInfoTableStep6"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
//	
//	public void actionLogManage(Map map){
//		try {
//			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "actionLogManage1"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//		try {
////			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "actionLogManage2"), map);
//		}catch(Exception e) {
//			logger.error("err ", e);
//		}
//	}
}
