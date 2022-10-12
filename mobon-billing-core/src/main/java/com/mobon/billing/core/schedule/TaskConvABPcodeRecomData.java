package com.mobon.billing.core.schedule;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ConvABPcodeRecomDataToMariaDB;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class TaskConvABPcodeRecomData {
    private static final Logger logger  = LoggerFactory.getLogger(TaskConvABPcodeRecomData.class);

    @Autowired
    private SelectDao selectDao;


    @Autowired
    private ConvABPcodeRecomDataToMariaDB convABPcodeRecomDataToMariaDB;

    @Autowired
    @Qualifier("ConvABPcodeRecomDataWorkQueue")
    private WorkQueueTaskData workQueue;

    @Autowired
    private RetryConfig retryConfig;
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private DataBuilder dataBuilder;

    @Autowired
    private SumObjectManager sumObjectManager;

    private static TimeToLiveCollection workingKey	= new TimeToLiveCollection();

    private static int				threadCnt	= 0;

    @Value("${log.path}")
    private String	logPath;
    @Value("${batch.list.size}")
    private String	batchListSize;
    @Value("${conversion.delay.time.minute}")
    private int convDelayTimeMinute;

    public static void setThreadCnt(int threadCnt) {
        TaskConvABPcodeRecomData.threadCnt = threadCnt;
    }
    public static synchronized void increaseThreadCnt() {
        TaskConvABPcodeRecomData.threadCnt++;
    }
    public static synchronized void decreaseThreadCnt() {
        TaskConvABPcodeRecomData.threadCnt--;
    }

    public static int getThreadCnt() {
        return TaskConvABPcodeRecomData.threadCnt;
    }

    public void mongoToFileABPcodeRecomConv() {
        List<ConvData> summeryConvData = (List<ConvData>) sumObjectManager.removeABPcodeRecomData();

        ArrayList<ConvData> listWriteData = new ArrayList();
        if(summeryConvData!=null && summeryConvData.size()>0 ) {
            logger.info("Conv summeryConvData - {}", summeryConvData.size());

            try {
                for (ConvData item : summeryConvData) {
                    listWriteData.add(item);
                }
            }catch(ConcurrentModificationException e) {
                logger.error("ConcurrentModificationException item - {}", summeryConvData.toString());
            }

            if( listWriteData.size()>0 ) {
                logger.info("ABPcodeRecomConv listWriteData - {}", listWriteData.size());

                long millis = Calendar.getInstance().getTimeInMillis();
                String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
                try {
                    ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ABPcodeRecomConvData, listWriteData);
                } catch (IOException e) {
                }
            }
        }
    }

    public void mongoToMariaConvABPcodeRecomDataV3() {
        logger.info(">> START mongoToMariaConvABPcodeRecomDataV3");

        if( workingKey.contains("main") ) {
            return;
        }

        workingKey.add("main", 1);

        List<ConvData> listConvABPcodeRecomData = (List<ConvData>) sumObjectManager.removeListABPcodeConvData();

        logger.info("listABPcodeRecomData.size - {}", listConvABPcodeRecomData.size());

        String _id  = "ConvABPcodeRecomData";

        if (listConvABPcodeRecomData.size() > 0) {
            logger.info("listConvABPcodeRecomData.size - {}", listConvABPcodeRecomData.size());

            if (workingKey.contains(_id)) {
                logger.info("workingKey.contains - {}", _id);
                for (ConvData row : listConvABPcodeRecomData) {
                    sumObjectManager.appendConvABPcodeRecom(row);
                }

            } else {
                workingKey.add(_id,3);
                workQueue.execute(new TaskData(G.ABPcodeRecomConvData, _id, listConvABPcodeRecomData));
            }
        }
        workingKey.remove("main");
    }

    public void mongoToMariaV3 (TaskData taskData) {
        boolean result = false;

        increaseThreadCnt();

        logger.info("retryCnt - {}, _id - {}, maxRetryCnt - {}", taskData.getRetryCnt(), taskData.getId(), retryConfig.maxRetryCount);
        try {
            if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount) {
                result = convABPcodeRecomDataToMariaDB.intoMariaConvABPcodeRecomData(taskData.getId(), (List<ConvData>) taskData.getFiltering(), false);
            } else {
                result = convABPcodeRecomDataToMariaDB.intoMariaConvABPcodeRecomData(taskData.getId(), (List<ConvData>) taskData.getFiltering(), true);
            }
        } catch ( Exception e) {
            logger.error("err _id - {}", taskData.getId(), e);
        }

        decreaseThreadCnt();

        if (result) {
            int i=3;
            while(!workingKey.remove( taskData.getId() )) {
                if(--i<0) {
                    logger.error("while(!workingKey.remove( _id )) 1 _id - {}", taskData.getId());
                    break;
                }
            }

            logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());

        } else {
            workQueue.execute(taskData);
        }
    }
}
