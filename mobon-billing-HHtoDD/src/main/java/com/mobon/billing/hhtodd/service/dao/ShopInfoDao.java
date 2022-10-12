package com.mobon.billing.hhtodd.service.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.BillingVo; 

@Repository
public class ShopInfoDao {

	private static final Logger	logger	= LoggerFactory.getLogger(ShopInfoDao.class);

	public static final String	NAMESPACE	= "shopInfoDataMapper";
	
	public static double fixCount = 100000;
	
	@Value("${log.path}")
	private String logPath;

	@Resource (name = "sqlSessionTemplateDreamShopData")
	private SqlSessionTemplate sqlSessionTemplateDreamShop;

	@Autowired
	private TransactionTemplate transactionTemplate;

	
	
	/**
	 * 일반광고주의 상품 카테고리 정보를 적재합니다.
	 * @param param
	 * @return param
	 */
	public BillingVo transectionRuningPrdtCtgrInfo(BillingVo param) {
		boolean result = false;

		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = endTime - startTime;
				
				String queryId = "insAdverPrdtCtgrInfo";
				try {
					
					startTime = System.currentTimeMillis();
					
					sqlSessionTemplateDreamShop.update(String.format("%s.%s", NAMESPACE,queryId), param);
					sqlSessionTemplateDreamShop.flushStatements();
					
					logger.info("{}", queryId);
					  
					res = true;
					
				} catch (Exception e) {	
				 
					logger.error("transectionRuningPrdtCtgrInfo Exception ==>{},{}",queryId,e);
					
					status.setRollbackOnly();
					res = false;

				} finally {					
					
					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;
					logger.info("{} Transaction BATCH Running Time (TBRT)  :{} ,{}" ,queryId, resutTime / 1000 + "(sec)");
				}
				
				return res;
			}
		});
		
		
		return param;
	}
	
	
	/**
	 * 이전 스케쥴의 배치 처리 실패로 인한 SHOP_DATA의 no 값이 변경 되었을 경우.
	 * 새로 조회하여 스케쥴을 시작
	 * @param param
	 * @return param
	 */
	public BillingVo transectionRuningSelMaxShopNo() {
		String queryId = "selMaxShopNo";
		BillingVo result = sqlSessionTemplateDreamShop.selectOne(String.format("%s.%s", NAMESPACE,queryId));
		return result;
	}
	
}//end class
