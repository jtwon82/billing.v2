package com.mobon.billing.core.service.dao;

import com.mobon.billing.model.v15.ConvData;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ConvABPcodeRecomDataDao {
    private static final Logger logger = LoggerFactory.getLogger(ConvABPcodeRecomDataDao.class);

    public static final String	NAMESPACE	= "viewPcodeDataMapper";

    @Resource(name="sqlSessionTemplateBilling")
    private SqlSessionTemplate sqlSessionTemplateConvBilling;

    @Resource(name="sqlSessionTemplateConvDream")
    private SqlSessionTemplate	sqlSessionTemplateConvDream;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public boolean transectionRuningV3(List<ConvData> aggregateList) {
        boolean result = false;
        for (ConvData vo :  aggregateList) {
            result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
                boolean res  = false;
                public Object doInTransaction(TransactionStatus status) {
                    long startTime = System.currentTimeMillis();
                    if (vo != null) {
                        sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertConvABPcodeRecom"), vo);
                        sqlSessionTemplateConvBilling.flushStatements();
                        logger.info("TR Time (TBRT) ABPcodeRecomConversion : " + (System.currentTimeMillis() - startTime) + "(ms) ");
                        res = true;
                    }
                    return res;
                }
            });

            if (!result) {
                return result;
            }
        }
        return result;
    }
}
