package com.mobon.billing.branch.schedule;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branch.service.BasketDataToMariaDB;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.BasketData;
import com.mobon.billing.model.v15.ChrgLogData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;

@Component
public class TaskBasketData {
    private static final Logger logger = LoggerFactory.getLogger(TaskBasketData.class);

    @Autowired
    private RetryConfig retryConfig;

    @Autowired
    @Qualifier("BasketDataWorkQueue")
    private WorkQueueTaskData workQueue;

    @Autowired
    private SumObjectManager sumObjectManager;

    @Autowired
    private BasketDataToMariaDB basketDataToMariaDB;

    private static TimeToLiveCollection workingKey	= new TimeToLiveCollection();

    private static int				threadCnt	= 0;

    @Value("${log.path}")
    private String	logPath;
    @Value("${batch.list.size}")
    private String	batchListSize;

    public static void setThreadCnt(int threadCnt) {
        TaskBasketData.threadCnt = threadCnt;
    }
    public static synchronized void increaseThreadCnt() {
        TaskBasketData.threadCnt++;
    }
    public static synchronized void decreaseThreadCnt() {
        TaskBasketData.threadCnt--;
    }

    public static int getThreadCnt() {
        return TaskBasketData.threadCnt;
    }

    public void mongoToFileBasketData(){
        List<BasketData> listBasketData = (List<BasketData>) sumObjectManager.removeBasketData(new BasketData());
        ArrayList <BasketData> listWriteData = new ArrayList<BasketData>();
        if ( listBasketData != null &&
        listBasketData.size() > 0) {
            try {
                for (BasketData row : listBasketData) {
                    listBasketData.add(row);
                }
            } catch (Exception e) {
                logger.error("ConcurrentModificationException item - {}" , listBasketData.toString());
            }
            if (listBasketData.size() > 0) {
                long millis = Calendar.getInstance().getTimeInMillis();
                String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
                try {
                    ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.BasketData, listWriteData);
                } catch (IOException e) {
                }
            }
        }
    }

    public void mongoToMariaBasketData() {
        logger.info(">> START mongoToMariaBasketData THREAD COUNT - {}", threadCnt);

        if ( workingKey.contains("main") ) {
            return;
        }
        workingKey.add("main", 1);
        List<BasketData> sumListBasketData =  (List<BasketData>) sumObjectManager.removeBasketData(new BasketData());
        List<BasketData> listBasketData = new ArrayList<>();

        String _id = Math.random()+ "";

        if (sumListBasketData.size() > 0) {
            logger.info("sumListBasketData.size - {}", sumListBasketData.size());
            try {
                for (BasketData row : sumListBasketData) {
                    if (listBasketData.size() >= Integer.parseInt(batchListSize)) {
                        sumObjectManager.appendBasketData(row);
                    } else {
                        listBasketData.add(row);
                        logger.debug("item.getValue() {}", row);
                    }
                }
            } catch (ConcurrentModificationException e) {
                logger.error("ConcurrentModificationException item - {}", sumListBasketData.size(), e);
            }

            if (workingKey.contains(_id)) {
                logger.info("workingKey.contains - {}", _id);

                for (BasketData row : sumListBasketData) {
                    sumObjectManager.appendBasketData(row);
                }
            } else {
                workingKey.add(_id, 3);
                workQueue.execute(new TaskData(G.BasketData, _id, listBasketData));
            }
        }
        workingKey.remove("main");
    }

    public void mongoToMariaV3(TaskData taskData) {
        boolean result = false;

        increaseThreadCnt();

        logger.info("BasketData retryCnt - {}, _id - {}, size={}", taskData.getRetryCnt(), taskData.getId(), taskData.getFiltering().size());
        try {
            if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
                result = basketDataToMariaDB.intoMariaBasketDataV3(taskData.getId(), (List<BasketData>)taskData.getFiltering(), false);
            } else {
                result = basketDataToMariaDB.intoMariaBasketDataV3(taskData.getId(), (List<BasketData>)taskData.getFiltering(), true);
            }
        } catch (Exception e) {
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

            //logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());

        } else {
            workQueue.execute(taskData);
            logger.info("retry size {}", taskData.getFiltering().size());
        }
    }
}
