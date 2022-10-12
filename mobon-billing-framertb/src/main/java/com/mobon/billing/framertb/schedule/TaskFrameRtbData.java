package com.mobon.billing.framertb.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.framertb.config.RetryConfig;
import com.mobon.billing.framertb.service.FrameRtbDataToMariaDB;
import com.mobon.billing.framertb.service.SumObjectManager;
import com.mobon.billing.framertb.service.WorkQueueTaskData;
//import com.mobon.billing.framertb.service.dao.FrameRtbDataDao;
//import com.mobon.billing.framertb.service.dao.SelectDao;
import com.mobon.billing.model.v15.FrameRtbData;
import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.exschedule.model.TaskData;

@Component
public class TaskFrameRtbData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskFrameRtbData.class);

	@Autowired
	private FrameRtbDataToMariaDB		FrameRtbDataToMariaDB;

	@Autowired
	private RetryConfig				retryConfig;

	@Autowired
	@Qualifier("FrameRtbType1DataWorkQueue")
	private WorkQueueTaskData		workQueue;

	@Autowired
	private SumObjectManager		sumObjectManager;
	
	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")
	private String	logPath;
	@Value("${batch.list.size}")
	private String	batchListSize;
	
	public static void setThreadCnt(int threadCnt) {
		TaskFrameRtbData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskFrameRtbData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskFrameRtbData.threadCnt--;
	}
	
	public static int getThreadCnt() {	
		return TaskFrameRtbData.threadCnt;
	}
	
	
	// 스케쥴러 FrameCycleLog
	public void mongoToMariaFrameCycleLog() {
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeCycleData = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeCycleLog();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrmeCycleData!=null && summeryFrmeCycleData.entrySet()!=null) {
//			System.out.println("8번 - task batch frame Rtb " + summeryFrmeCycleData.toString() );
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeCycleData.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameCycleLog(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeCycleData.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());

			
			String _id = "frmeCycleLog";
			
			if(listFrameData.size() > 0) {
				logger.info("filtering {}, {}", listFrameData.size());
			}
			
			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());
				
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeCycleLog"));
				
//				if( 10000>listFrameData.size() ) {
//					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeCycleLog"));
//					
//				} else {
//					List<FrameRtbData> intoFiltering = new ArrayList();
//					int cnt=0;
//					for( FrameRtbData row : listFrameData ) {
//						if( 10000>cnt++ ) {
//							intoFiltering.add(row);
//						}else {
//							sumObjectManager.appendFrameCycleLog(row);
//						}
//					}
//					workQueue.execute(new TaskData(G.framertb_info, _id, intoFiltering, "frmeCycleLog"));
//				}
			}
		}
		workingKey.remove("main");
	}
	
	// 스케쥴러 FrameDayStats
	public void mongoToMariaFrameDayData() {
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeDayData = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeDayData();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		
		if(summeryFrmeDayData!=null && summeryFrmeDayData.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeDayData.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameDayStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeDayData.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());

			String _id = "frmeDayStats";
			
			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());
				
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeDayStats"));
				
//				if( 10000>listFrameData.size() ) {
//					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeDayStats"));
//					
//				} else {
//					List<FrameRtbData> intoFiltering = new ArrayList();
//					int cnt=0;
//					for( FrameRtbData row : listFrameData ) {
//						if( 10000>cnt++ ) {
//							intoFiltering.add(row);
//						}else {
//							sumObjectManager.appendFrameDayStats(row);
//						}
//					}
//					workQueue.execute(new TaskData(G.framertb_info, _id, intoFiltering, "frmeDayStats"));
//				}
			}
		}
		workingKey.remove("main");
		
	}
	
	// 스케쥴러 FrameTrnData
	public void mongoToMariaFrameTrnData() {
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeTrnLog = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeTrnLog();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrmeTrnLog!=null && summeryFrmeTrnLog.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeTrnLog.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameTrnLog(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeTrnLog.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());

			String _id = "frmeTrnLog";
			
			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());
				
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeTrnLog"));
				
//				if( 10000>listFrameData.size() ) {
//					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeTrnLog"));
//					
//				} else {
//					List<FrameRtbData> intoFiltering = new ArrayList();
//					int cnt=0;
//					for( FrameRtbData row : listFrameData ) {
//						if( 10000>cnt++ ) {
//							intoFiltering.add(row);
//						}else {
//							sumObjectManager.appendFrameTrnLog(row);
//						}
//					}
//					workQueue.execute(new TaskData(G.framertb_info, _id, intoFiltering, "frmeTrnLog"));
//				}
			}
		}
		workingKey.remove("main");
	}
	
	// 스케쥴러 FrameCombiDayStats
	public void mongoToMariaFrameCombiDayStats() {
		
		if( workingKey.contains("main") ) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeCombiDayStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeCombiDayStats();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrmeCombiDayStats!=null && summeryFrmeCombiDayStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeCombiDayStats.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameCombiDatStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameData.size {}", listFrameData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeCombiDayStats.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());
			
			String _id = "frmeCombiDayStats";
			
			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());
				
				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeCombiDayStats"));
				
//				if( 10000>listFrameData.size() ) {
//					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeCombiDayStats"));
//					
//				} else {
//					List<FrameRtbData> intoFiltering = new ArrayList();
//					int cnt=0;
//					for( FrameRtbData row : listFrameData ) {
//						if( 10000>cnt++ ) {
//							intoFiltering.add(row);
//						}else {
//							sumObjectManager.appendFrameCombiDatStats(row);
//						}
//					}
//					workQueue.execute(new TaskData(G.framertb_info, _id, intoFiltering, "frmeCombiDayStats"));
//				}
			}
		}
		workingKey.remove("main");
	}

	// 스케쥴러 FrameAdverDayStats
	public void mongoToMariaFrameAdverDayStats() {

		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrmeAdverDayStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeAdverDayStats();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrmeAdverDayStats!=null && summeryFrmeAdverDayStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeAdverDayStats.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameAdverDayStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameData.size {}", listFrameData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeAdverDayStats.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());

			String _id = "frmeAdverDayStats";

			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeAdverDayStats"));

//				if( 10000>listFrameData.size() ) {
//					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeAdverDayStats"));
//
//				} else {
//					List<FrameRtbData> intoFiltering = new ArrayList();
//					int cnt=0;
//					for( FrameRtbData row : listFrameData ) {
//						if( 10000>cnt++ ) {
//							intoFiltering.add(row);
//						}else {
//							sumObjectManager.appendFrameAdverDatStats(row);
//						}
//					}
//					workQueue.execute(new TaskData(G.framertb_info, _id, intoFiltering, "frmeAdverDayStats"));
//				}
			}
		}
		workingKey.remove("main");
	}
	
	// 스케쥴러 FrameAdverDayAbStats
	public void mongoToMariaFrameAdverDayAbStats() {

		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrmeAdverDayAbStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeAdverDayAbStats();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrmeAdverDayAbStats!=null && summeryFrmeAdverDayAbStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeAdverDayAbStats.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameAdverDayStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameData.size {}", listFrameData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeAdverDayAbStats.toString());
			}
			logger.debug("listFrameRtbData size {}", listFrameData.size());

			String _id = "frmeAdverDayAbStats";

			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_ab_info, _id, listFrameData, "frmeAdverDayAbStats"));
			}
		}
		workingKey.remove("main");
	}

	// 스케쥴러 FrameMediaAdverStats
	public void mongoToMariaFrameMediaAdverStats() {

		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrmeMediaAdverStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeMediaAdverStats();
		ArrayList<FrameRtbData> listFrameMediaAdverData = new ArrayList();
		if(summeryFrmeMediaAdverStats!=null && summeryFrmeMediaAdverStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeMediaAdverStats.entrySet()) {
					if( listFrameMediaAdverData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameMediaAdverStats(item.getValue());
					} else {
						listFrameMediaAdverData.add(item.getValue());
					}
				}
				logger.info("listFrameMediaAdverData.size {}", listFrameMediaAdverData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeMediaAdverStats.toString());
			}
			logger.debug("listFrameMediaAdverData size {}", listFrameMediaAdverData.size());

			String _id = "frmeMediaAdverStats";

			if( listFrameMediaAdverData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameMediaAdverData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameMediaAdverData, "frmeMediaAdverStats"));

			}
		}
		workingKey.remove("main");
	}

	public void mongoToMariaFrameSizeStats () {
		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrameSizeStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeSizeStats();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrameSizeStats!=null && summeryFrameSizeStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrameSizeStats.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameSizeStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameSizeData.size {}", listFrameData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrameSizeStats.toString());
			}
			logger.debug("listFrameSizeData size {}", listFrameData.size());

			String _id = "frmeSizeStats";

			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeFrameSizeStats"));

			}
		}
		workingKey.remove("main");
	}
	
	public void mongoToMariaFrameActionLog () {
		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrameActionStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeActionLog();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		if(summeryFrameActionStats!=null && summeryFrameActionStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrameActionStats.entrySet()) {
					if( listFrameData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameActionLog(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameActionLog.size {}", listFrameData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrameActionStats.toString());
			}
			logger.debug("listFrameActionLog size {}", listFrameData.size());

			String _id = "frmeActionLog";

			if( listFrameData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frmeFrameActionLog"));

			}
		}
		workingKey.remove("main");
	}
	
	public void mongoToMariaFrameAdverPrdtCtgrDayStats() {
		if (workingKey.contains("main")) {
			return;			
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrameAdverPrdtCtgrDayStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrameAdverPrdtCtgrDayStats();
		ArrayList<FrameRtbData> listFrameData = new ArrayList();
		
		if (summeryFrameAdverPrdtCtgrDayStats != null && summeryFrameAdverPrdtCtgrDayStats.entrySet() != null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrameAdverPrdtCtgrDayStats.entrySet()) {
					if (listFrameData.size() >= Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameAdverPrdtCtgrDayStats(item.getValue());
					} else {
						listFrameData.add(item.getValue());
					}
				}
				logger.info("listFrameAdverPrdtCtgrDayStats.size  {}", listFrameData.size());
			} catch(ConcurrentModificationException e ) {
				logger.error("ConcurrentModificationException item - {}", summeryFrameAdverPrdtCtgrDayStats.toString());
			}
			logger.debug("listFrameAdverPrdtCtgrDayStats size {}", listFrameData.size());
			
			String _id = "frameAdverPrdtCtgrDayStats";
			
			if (listFrameData.size()>0) {
				logger.info("filtering {}, {}", _id, listFrameData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameData, "frameAdverPrdtCtgrDayStats"));
			}
		}
		workingKey.remove("main");
	}
	
	//스케줄러 frame_kaist_combi_day_stats
		public void mongoToMariaFrameKaistCombiDayStats() {
			if(workingKey.contains("main")) {
				return; 
			}
			
			workingKey.add("main", 1);
			
			Map<String, FrameRtbData> summeryFrameKaistCombiDayStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrameKaistCombiDayStats();
			ArrayList <FrameRtbData> listFrameKaistCombiDayData = new ArrayList<FrameRtbData> ();
			if (summeryFrameKaistCombiDayStats != null && summeryFrameKaistCombiDayStats.entrySet() != null) {
				try {
					for (Entry<String, FrameRtbData> item : summeryFrameKaistCombiDayStats.entrySet()) {
						if (listFrameKaistCombiDayData.size() >= Integer.parseInt(batchListSize)) {
							sumObjectManager.appendFrameKaistCombiDayStats(item.getValue());
						} else {
							listFrameKaistCombiDayData.add(item.getValue());
						}
					}
					logger.info("listFrameKaistCombiDayData.size-{}", listFrameKaistCombiDayData.size());
				} catch (ConcurrentModificationException e) {
					logger.error("listFrameKaistCombiDayData item - {}", summeryFrameKaistCombiDayStats.toString());
				}
				logger.debug("listFrameKaistCombiDayData size {}", listFrameKaistCombiDayData.size());
				
				String _id = "frameKaistCombiDayStats";
				
				if ( listFrameKaistCombiDayData.size() > 0 ) {
					logger.info("filtering {} {}", _id, listFrameKaistCombiDayData.size());
					workingKey.add(_id, 3);
					workQueue.execute(new TaskData(G.framertb_info, _id, listFrameKaistCombiDayData, "frameKaistCombiDayStats"));
				}
			}
			 workingKey.remove("main");
		}

	// 스케쥴러 FrameCodeStats
	public void mongoToMariaFrameCodeStats() {

		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);

		Map<String, FrameRtbData> summeryFrmeCodeStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrmeCodeStats();
		ArrayList<FrameRtbData> listFrameCodeData = new ArrayList();
		if(summeryFrmeCodeStats!=null && summeryFrmeCodeStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeCodeStats.entrySet()) {
					if( listFrameCodeData.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameCodeStats(item.getValue());
					} else {
						listFrameCodeData.add(item.getValue());
					}
				}
				logger.info("listFrameCodeData.size {}", listFrameCodeData.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeCodeStats.toString());
			}
			logger.debug("listFrameCodeData size {}", listFrameCodeData.size());

			String _id = "frmeCodeStats";

			if( listFrameCodeData.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameCodeData.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameCodeData, "frmeCodeStats"));

			}
		}
		workingKey.remove("main");
	}
	
	// 스케쥴러 FrameCtgrDayStats
	public void mongoToMariaFrameCtgrDayStats() {
		if( workingKey.contains("main") ) {
			return;
		}

		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeCtgrDayStats = (Map<String, FrameRtbData>) sumObjectManager.removeFrameCtgrDayStats();
		ArrayList<FrameRtbData> listFrameCtgrDayStats = new ArrayList();
		if(summeryFrmeCtgrDayStats!=null && summeryFrmeCtgrDayStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeCtgrDayStats.entrySet()) {
					if( listFrameCtgrDayStats.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameCtgrDayStats(item.getValue());
					} else {
						listFrameCtgrDayStats.add(item.getValue());
					}
				}
				logger.info("listFrameCtgrDayStats.size {}", listFrameCtgrDayStats.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeCtgrDayStats.toString());
			}
			logger.debug("listFrameCtgrDayStats size {}", listFrameCtgrDayStats.size());

			String _id = "frmeCtgrDayStats";

			if( listFrameCtgrDayStats.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameCtgrDayStats.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_info, _id, listFrameCtgrDayStats, "frmeCtgrDayStats"));

			}
		}
		workingKey.remove("main");
	}
	
	//스케줄러 FrameDayAbStats 
	public void mongoToMariaFrameDayAbData() {
		if (workingKey.contains("main")) {
			return;
		}
		
		workingKey.add("main", 1);
		
		Map<String, FrameRtbData> summeryFrmeDayAbStats = (Map<String , FrameRtbData>) sumObjectManager.removeFrameDayABStats();
		ArrayList<FrameRtbData> listFrameDayAbStats = new ArrayList();
		if(summeryFrmeDayAbStats!=null && summeryFrmeDayAbStats.entrySet()!=null) {
			try {
				for (Entry<String, FrameRtbData> item : summeryFrmeDayAbStats.entrySet()) {
					if( listFrameDayAbStats.size()>=Integer.parseInt(batchListSize) ) {
						sumObjectManager.appendFrameDayAbStats(item.getValue());
					} else {
						listFrameDayAbStats.add(item.getValue());
					}
				}
				logger.info("listFrameDayAbStats.size {}", listFrameDayAbStats.size());
			}catch(ConcurrentModificationException e) {
				logger.error("ConcurrentModificationException item - {}", summeryFrmeDayAbStats.toString());
			}
			logger.debug("listFrameDayAbStats size {}", listFrameDayAbStats.size());

			String _id = "frmeDayAbStats";

			if( listFrameDayAbStats.size()>0 ) {
				logger.info("filtering {}, {}", _id, listFrameDayAbStats.size());

				workingKey.add( _id, 3 );
				workQueue.execute(new TaskData(G.framertb_ab_info, _id, listFrameDayAbStats, "frmeDayAbStats"));

			}
		}
		workingKey.remove("main");
		
	}
	
	public void mongoToMariaV3(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = FrameRtbDataToMariaDB.intoMariaFrameRtbDataV3(taskData.getId(), (List<FrameRtbData>)taskData.getFiltering(), false, taskData.getDataType());
			} else {
				result = FrameRtbDataToMariaDB.intoMariaFrameRtbDataV3(taskData.getId(), (List<FrameRtbData>)taskData.getFiltering(), true, taskData.getDataType());
			}
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
		}
		
		decreaseThreadCnt();
		
		if (result) {
			int i=3;
			while(!workingKey.remove( taskData.getId() )) {
				if (--i<0) {
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
	public void mongoToMariaV3AB(TaskData taskData) {

		boolean result = false;
		
		increaseThreadCnt();
		
		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
		try {
			
			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
				result = FrameRtbDataToMariaDB.intoMariaFrameRtbDataV3(taskData.getId(), (List<FrameRtbData>)taskData.getFiltering(), false, taskData.getDataType());
			} else {
				result = FrameRtbDataToMariaDB.intoMariaFrameRtbDataV3(taskData.getId(), (List<FrameRtbData>)taskData.getFiltering(), true, taskData.getDataType());
			}
		} catch (Exception e) {
			logger.error("err _id - {}", taskData.getId(), e);
		}
		
		decreaseThreadCnt();
		
		if (result) {
			int i=3;
			while(!workingKey.remove( taskData.getId() )) {
				if (--i<0) {
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
