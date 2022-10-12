package com.mobon.billing.branch.service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.branch.service.dao.BasketDataDao;
import com.mobon.billing.branch.service.dao.ChrgLogDataDao;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.BasketData;
import com.mobon.billing.model.v15.ChrgLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BasketDataToMariaDB {

    private static final Logger logger = LoggerFactory.getLogger(BasketDataToMariaDB.class);

    @Value("${log.path}")
    private String logPath;

    @Autowired
    private SelectDao selectDao;

    @Autowired
    private BasketDataDao basketDataDao;

    public boolean intoMariaBasketDataV3(String _id, List<BasketData> aggregateList, boolean toMongodb) {
        boolean result = false;
        long start_millis = System.currentTimeMillis();

        if (aggregateList != null) {
            HashMap<String, ArrayList<BasketData>> flushMap = makeFlushMap(aggregateList);
            if ( flushMap.keySet().size() != 0) {
                if (toMongodb) {
                    try {
                        String writeFileName = String.format("insertIntoError_%s_%s", Thread.currentThread().getName(), DateUtils.getDate("yyyyMMdd_HHmm") );
                        ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.BasketData, aggregateList);
                    } catch (IOException e) {
                        logger.error("err - {}", e);
                    }

                    logger.info("chking fail BasketData fileWriteOk flushMap.keySet() - {}", flushMap.keySet() );
                    result = true;
                } else {
                    result = basketDataDao.transectionRunningV2(flushMap);
                    logger.info("succ intoMariaBasketDataV3 _id - {}, size - {}, during - {}", _id, flushMap.keySet().size(), System.currentTimeMillis() - start_millis );
                }
            } else {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }

    private HashMap<String, ArrayList<BasketData>> makeFlushMap(List<BasketData> aggregateList) {
        HashMap<String ,ArrayList<BasketData>> flushMap = new HashMap<>();

        for (BasketData vo : aggregateList) {
            try {
                logger.debug("BasketData {}", vo);
                if (vo != null) {
                    vo.setMediaTpCode(selectDao.selectMediaTpCode(vo.getParNo()));

                    if( !StringUtils.isNumeric(vo.getPlatfromTpCode()) ) vo.setPlatfromTpCode(G.convertPLATFORM_CODE(vo.getPlatfromTpCode()));
                    if( !StringUtils.isNumeric(vo.getProduct()) ) vo.setProduct(G.convertPRDT_CODE(vo.getProduct()));
                    if( !StringUtils.isNumeric(vo.getAdGubun()) ) vo.setAdGubun(G.convertTP_CODE(vo.getAdGubun()));

                    add(flushMap, "insertBasketData", vo);
                }
            } catch (Exception e) {
                logger.info("err msg - {}, item - {}", e.getMessage(), vo);
            }
        }

        return flushMap;
    }

    private void add(HashMap<String, ArrayList<BasketData>> flushMap, String key, BasketData vo) {
        if(flushMap.get(key)==null){
            flushMap.put(key, new ArrayList<BasketData>());
        }
        ArrayList<BasketData> l = flushMap.get(key);
        l.add(vo);
    }
}
