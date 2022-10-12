package com.mobon.billing.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.dao.ConvABPcodeRecomDataDao;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

@Service
public class ConvABPcodeRecomDataToMariaDB {
    private static final Logger logger = LoggerFactory.getLogger(ConvABPcodeRecomDataToMariaDB.class);

    @Autowired
    private ConvABPcodeRecomDataDao convABPcodeRecomDataDao;

    @Value("${log.path}")
    private String	logPath;
    @Value("${batch.list.size}")
    private String	batchListSize;

    public boolean intoMariaConvABPcodeRecomData(String _id, List<ConvData> aggregateList, boolean toMongodb) {
        boolean result = false;
        long start_millis = System.currentTimeMillis();

        if (aggregateList != null && aggregateList.size() != 0) {
            if (toMongodb) {
                try {
                    ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.ABPcodeRecomConvData, aggregateList);
                } catch (IOException e) {
                    logger.error("err - {}", e);
                }

                logger.info("chking fail CONVABPCODERECOM fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
                result = true;
            } else {
                result = convABPcodeRecomDataDao.transectionRuningV3( aggregateList );
                logger.info("succ intoMariaConvABPcodeRecomData _id - {}, size - {}, during - {}", _id, aggregateList.size(), System.currentTimeMillis() - start_millis );
            }
        } else {
            result = true;
        }
        return result;
    }
}
