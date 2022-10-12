package com.mobon.billing.branch.service.dao;

import com.mobon.billing.model.v15.BasketData;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

@Repository
public class BasketDataDao {

    private static final Logger logger = LoggerFactory.getLogger(BasketDataDao.class);

    public static final String	NAMESPACE	= "basketDataMapper";

    @Resource(name = "sqlSessionTemplateBilling")
    private SqlSessionTemplate sqlSessionTemplateBilling;

    @Autowired
    private TransactionTemplate transactionTemplate;
    public boolean transectionRunningV2(HashMap<String, ArrayList<BasketData>> flushMap) {
        boolean result = false;
        result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
            boolean res = false;
            public Object doInTransaction(TransactionStatus status) {
                try {
                    int totalBillingSize = 0;
                    int totalDreamSize = 0;
                    long startTime = 0;
                    long endTime = 0;
                    long resutTime = 0;

                    for (Entry<String, ArrayList<BasketData>> item : flushMap.entrySet()) {
                        ArrayList<BasketData> data = item.getValue();
                        String dataKey = item.getKey();
                        int dataSize = data.size();
                        if (dataSize > 0) {
                            logger.debug("item.getKey() - {}, item.getValue().size() - {}", dataKey, dataSize);
                            sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, dataKey), data);
                            totalBillingSize += dataSize;
                        }
                    }
                    if(totalBillingSize>0) {
                        startTime = System.currentTimeMillis();
                        sqlSessionTemplateBilling.flushStatements();
                        endTime = System.currentTimeMillis();
                        resutTime = endTime - startTime;
                        logger.info("TR Time (TBRT) Billing BaskeData : " + resutTime + "(ms) totalsize - "+ totalBillingSize);
                    }
                    res = true;
                } catch (Exception e) {
                    logger.error("err transectionRuning ", e);
                    status.setRollbackOnly();
                    res = false;
                } finally {
                    logger.debug("succ transectionRuningV2 flush");
                }
                return res;
            }
        });
        return result;
    }
}
