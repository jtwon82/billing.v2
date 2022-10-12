package com.mobon.billing.core.service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.dao.ActionABPcodeDataDao;
import com.mobon.billing.model.v15.ActionLogData;
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
public class ActionABPcodeDataToMariaDB {

    private static final Logger logger = LoggerFactory.getLogger(ActionABPcodeDataToMariaDB.class);

    @Value("${log.path}")
    private String	logPath;

    @Autowired
    private ActionABPcodeDataDao actionABPcodeDataDao;


    public boolean intoMariaActionABPcodeDataV3(String _id, List<ActionLogData> aggregateList, boolean toMongodb) {
        boolean result = false;
        long start_millis = System.currentTimeMillis();
        HashMap<String, ArrayList<ActionLogData>> flushMap = makeFlushMap(aggregateList);

        if (aggregateList != null) {
            if (toMongodb) {
                try {
                    ConsumerFileUtils.writeLine( logPath +"retry/", String.format("insertIntoError_%s", DateUtils.getDate("yyyyMMdd_HHmm")), G.ActionABPcodeData, aggregateList);
                } catch (IOException e) {
                    logger.error("err - {}", e);
                }

                logger.info("chking fail ActionPcodeData fileWriteOk flushMap.keySet() - {}", aggregateList.size() );
                result = true;
            } else {
                result = actionABPcodeDataDao.transectionRuningV2( flushMap );
                logger.info("succ intoMariaActionPcodeData during - {}", System.currentTimeMillis() - start_millis );
            }
        } else {
            result = true;
        }
        return result;
    }

    public HashMap<String, ArrayList<ActionLogData>> makeFlushMap(List<ActionLogData> aggregateList){
        HashMap<String, ArrayList<ActionLogData>> flushMap = new HashMap<String, ArrayList<ActionLogData>>();

        for (ActionLogData vo : aggregateList) {
            try {
                if (vo != null) {
                    if( "mba_no_script".equals(vo.getProduct()) ) {
                        vo.setProduct(G.MBW);
                    }
                    //상품 타겟팅 여부 확인용 메소드
                    if (vo.getAdGubun() != null) {
                        vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
                    }

                    if(vo.isbHandlingStatsPointMobon()) {
                        //add(flushMap, "sp_action_data_NEW", vo);
                    } else {
                        if((vo.getAdvertiserId() == null || "".equals(vo.getAdvertiserId())) ||
                                (vo.getAbTestTy() == null || "".equals(vo.getAbTestTy())) ||
                                (vo.getRecomAlgoCode() == null || "".equals(vo.getRecomAlgoCode()))) {
                            logger.info("vo:{}", vo);
                        } else {
                            add(flushMap, "sp_action_ab_pcode_data_billing_NEW", vo);
                        }
                    }
                }
            }catch(Exception e){
                logger.error("err msg - {}, item - {}", e.getMessage(), vo);
            }
        }
        return flushMap;
    }

    private void add(HashMap<String, ArrayList<ActionLogData>> flushMap, String key, ActionLogData vo) {
        if(flushMap.get(key)==null){
            flushMap.put(key, new ArrayList<ActionLogData>());
        }
        ArrayList<ActionLogData> l = flushMap.get(key);
        l.add(vo);
    }
}
