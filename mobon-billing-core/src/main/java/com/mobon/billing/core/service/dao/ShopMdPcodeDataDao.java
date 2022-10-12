package com.mobon.billing.core.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mobon.billing.model.v15.ShopInfoData; 

@Repository
public class ShopMdPcodeDataDao {

	private static final Logger	logger	= LoggerFactory.getLogger(ShopMdPcodeDataDao.class);

	public static final String	NAMESPACE	= "shopInfoDataMapper";

	@Resource(name="sqlSessionTemplateDream")
	private SqlSessionTemplate	sqlSessionTemplateDream;
	
	@Resource (name = "sqlSessionTemplateDreamShopData")
	private SqlSessionTemplate sqlSessionTemplateDreamShopData;

	@Autowired
	private TransactionTemplate transactionTemplate;

	
	
	
	
	public boolean transectionRuningV2(HashMap<String, ArrayList<ShopInfoData>> flushMap){
		boolean result = false;
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
			
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();

				try {
					int totalsize = 0;
					
					for(Entry<String, ArrayList<ShopInfoData>> item : flushMap.entrySet() ){
						String dataKey = item.getKey();
						ArrayList<ShopInfoData> data = item.getValue();
						int dataSize = item.getValue().size();

						logger.debug("ShopMdPcodeDATA dataKey - {}", dataKey);
						logger.debug("ShopMdPcodeDATA dataSize - {}", dataSize);
						
						if( dataSize>0 ) {
							sqlSessionTemplateDream.update(String.format("%s.%s", NAMESPACE, item.getKey()), data);
							totalsize += dataSize;
						}
					}
					
					if(totalsize>0) {
						long endTime = System.currentTimeMillis();
				        long resutTime = endTime - startTime;
				        sqlSessionTemplateDream.flushStatements();
						logger.info("TR Time (TBRT) Dream ShopMdPcode : " + resutTime + "(ms) totalsize - "+ totalsize);
					}
					
					res = true;
				}
				catch (MyBatisSystemException e) {
					logger.error("e utf8mb4", e);
					
				}catch (DataIntegrityViolationException e) {
					String errMsg = e.getMessage();
					logger.info("errMsg - {}", errMsg);
					
				}
				catch (Exception e) {
					logger.error("err transectionRuningV2 msg ", e);
					status.setRollbackOnly();
					res = false;
					
				} finally{
					logger.debug("succ transectionRuningV2 flush");
				}
				return res;
			}
		});
		return result;
	}
	
	public ShopInfoData selectShopData(Map param) {
		ShopInfoData result = sqlSessionTemplateDreamShopData.selectOne(String.format("%s.%s", NAMESPACE, "selectShopData"), param);
		return result;
	}
}
