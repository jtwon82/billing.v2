package com.mobon.billing.core.schedule;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.config.RetryConfig;
import com.mobon.billing.core.service.ActionRenewLogDataToClickHouse;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueueTaskData;
import com.mobon.billing.model.v15.ActionLogData;
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

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TaskActionRenewLogData {
    private static final Logger logger = LoggerFactory.getLogger(TaskActionRenewLogData.class);

    @Autowired
    private RetryConfig retryConfig;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    @Qualifier("ActionRenewLogDataWorkQueue")
    private WorkQueueTaskData workQueue;

    @Autowired
    private SumObjectManager sumObjectManager;

    @Autowired
    private ActionRenewLogDataToClickHouse actionRenewLogDataToClickHouse;

    private static TimeToLiveCollection workingKey	= new TimeToLiveCollection();

    @Value("${log.path}")
    private String	logPath;
    @Value("${batch.list.size}")
    private String	batchListSize;

    private static int				threadCnt	= 0;

    public static void setThreadCnt(int threadCnt) {TaskActionRenewLogData.threadCnt = threadCnt;}
    public static synchronized void increaseThreadCnt() {TaskActionRenewLogData.threadCnt++;}
    public static synchronized void decreaseThreadCnt() {TaskActionRenewLogData.threadCnt--;}

    public static int getThreadCnt () { return TaskActionRenewLogData.threadCnt;}

    public void mongoToFileActionRenewLogData() {
        List<ActionLogData> listActionRenewLogData = (List <ActionLogData>) sumObjectManager.removeActionRenewLogData();
        ArrayList<ActionLogData> listWriteData = new ArrayList<>();
        if (listActionRenewLogData != null && listActionRenewLogData.size() > 0) {
            try {
                for( ActionLogData row : listActionRenewLogData ) {
                    listWriteData.add(row);
                }
            }catch(ConcurrentModificationException e) {
                logger.error("ConcurrentModificationException item - {}", listActionRenewLogData.toString());
            }

            if( listWriteData.size()>0 ) {
                long millis = Calendar.getInstance().getTimeInMillis();
                String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
                try {
                    ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ActionRenewLogData, listWriteData);
                } catch (IOException e) {
                }
            }
        }
    }
    public void mongoToMariaActionRenewLogDataV3(){
        logger.info(">> START mongoToMariaActionRenewLogDataV3 THREAD COUNT - {}", threadCnt);

        if( workingKey.contains("main") ) {
            return;
        }
        workingKey.add("main", 1);

        List<ActionLogData> sumListActionRenewLogData = (List<ActionLogData>) sumObjectManager.removeActionRenewLogData();
        List<ActionLogData> listActionRenewLogData = new ArrayList<ActionLogData>();

//		String _id = "actionData";
        String _id=Math.random()+"";

        if( sumListActionRenewLogData.size()>0 ) {
            logger.info("sumListActionRenewLogData.size - {}", sumListActionRenewLogData.size());

            try {
                for (ActionLogData row : sumListActionRenewLogData) {
                    // 액션로그는 날짜지나면 삭제
                    Date sdate = null;
                    try {
                        sdate = new SimpleDateFormat("yyyyMMdd").parse(row.getYyyymmdd());
                    } catch (ParseException e) {
                    }
                    Date edate = new Date();
                    edate.setTime( ( new Date().getTime() + (1000L*60*60*24* (60 * -1)) ) );
                    if( sdate.getTime() < edate.getTime() ) {
                        logger.error("actionData over date - {}", row);
                        continue;
                    }

                    if( listActionRenewLogData.size()>=Integer.parseInt(batchListSize) ) {
                        sumObjectManager.appendActionRenewLog(row);
                    } else {
                        listActionRenewLogData.add(row);
                        logger.debug("item.getValue() {}", row);
                    }
                }
            }catch(ConcurrentModificationException e) {
                logger.error("ConcurrentModificationException item - {}", sumListActionRenewLogData.size(), e);
            }


            if ( workingKey.contains(_id) ) {
                logger.info("workingKey.contains - {}", _id);

                for( ActionLogData row : listActionRenewLogData ) {
                    sumObjectManager.appendActionRenewLog(row);
                }
            } else {
                workingKey.add( _id, 3 );
                //workQueue.execute(new RetryTaskerV3(listActionData));
                workQueue.execute(new TaskData(G.ActionRenewLogData, _id, listActionRenewLogData));
            }
        }

        workingKey.remove("main");
    }


    public void mongoToClickhouse(TaskData taskData) {
        boolean result = false;

        increaseThreadCnt();

        logger.debug("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
        try {
            if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
                result = actionRenewLogDataToClickHouse.intoClickhouseActionRenewLogDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), false);
            } else {
                result = actionRenewLogDataToClickHouse.intoClickhouseActionRenewLogDataV3(taskData.getId(), (List<ActionLogData>)taskData.getFiltering(), true);
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

            logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());

        } else {
            workQueue.execute(taskData);
        }
    }


}
