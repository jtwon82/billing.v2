package com.mobon.billing.core.service.dao;

import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.BaseCVData;
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
import java.util.Map;

@Repository
public class ActionRenewLogDataDao {
    private static final Logger logger = LoggerFactory.getLogger(ActionRenewLogDataDao.class);

    public static final String	NAMESPACE	= "actionRenewLogDataMapper";

    @Resource(name = "sqlSessionTemplateClickhouse")
    private SqlSessionTemplate sqlSessionTemplateClickhouse;

    @Resource (name = "sqlSessionTemplateDream")
    private SqlSessionTemplate sqlSessionTemplateDream;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public boolean transectionRuningV2(HashMap<String, ArrayList<ActionLogData>> flushMap) {
        boolean result = false;

        result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
            boolean res = false;

            public Object doInTransaction(TransactionStatus status) {
                try {

                    int totalBillingSize = 0;
                    long startTime = 0;
                    long endTime = 0;
                    long resutTime = 0;

                    for (Map.Entry<String, ArrayList<ActionLogData>> item : flushMap.entrySet()) {
                        ArrayList<ActionLogData> data = item.getValue();
                        String dataKey = item.getKey();
                        int dataSize = data.size();

                        if (dataSize > 0) {
                            sqlSessionTemplateClickhouse.update(String.format("%s.%s", NAMESPACE, dataKey), data);
                            totalBillingSize += dataSize;
                        }
                    }

                    if(totalBillingSize>0) {
                        startTime = System.currentTimeMillis();
                       sqlSessionTemplateClickhouse.flushStatements();
                        endTime = System.currentTimeMillis();
                        resutTime = endTime - startTime;
                        logger.info("TR Time (TBRT) Clickhouse ActionRenewLogData : " + resutTime + "(ms) totalsize - "+ totalBillingSize);
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
