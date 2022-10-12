package com.mobon.billing.framertb.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mobon.billing.model.v15.FrameRtbData;
import com.mobon.billing.model.v15.NearData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);

	public SumObjectManager() {
		logger.debug(">> SumObjectManager init ");
		
//		mapNearData = Collections.synchronizedMap(new HashMap<String, NearData>());
		mapFrmeCodeStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeCycleLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeTrnLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeCombiDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeAdverDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrmeAdverDayAbStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrameActionLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrameSizeStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		mapFrameAdverPrdtCtgrDayStats = Collections.synchronizedMap(new HashMap<String ,FrameRtbData>());
		mapFrameKaistCombiDayStats = Collections.synchronizedMap(new HashMap<String ,FrameRtbData>());
		mapFrameCtgrDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		
	}


	public void clear() {
		
//		this.mapNearData.clear();
		this.mapFrmeCycleLog.clear();
		this.mapFrmeDayStats.clear();
		this.mapFrmeTrnLog.clear();
		this.mapFrmeCombiDayStats.clear();
		this.mapFrmeAdverDayStats.clear();
		this.mapFrmeAdverDayAbStats.clear();
		this.mapFrameActionLog.clear();
		this.mapFrameSizeStats.clear();
		this.mapFrameAdverPrdtCtgrDayStats.clear();
		this.mapFrameKaistCombiDayStats.clear();
		this.mapFrmeCodeStats.clear();
		this.mapFrameCtgrDayStats.clear();
	}

//	public void appendNearData(NearData record) {
//		synchronized(mapNearData) {
//			NearData sum = mapNearData.get(record.generateKey());
//			if (sum == null) {
//				mapNearData.put(record.generateKey(), record);
//			} else {
//				sum.sumGethering(record);
//			}
//		}
//	}
	
	// Table : FRME_CYCLE_LOG
	public void appendFrameCycleLog(FrameRtbData record) {
		
		synchronized(mapFrmeCycleLog) {
			FrameRtbData sum = mapFrmeCycleLog.get(record.keyCycleLog());
			if (sum == null) {
				mapFrmeCycleLog.put(record.keyCycleLog(), record);
			} else {
				sum.sumGethering(record);
			}
			
		}
	}
	
	// Table : FRME_DAY_STATS
	public void appendFrameDayStats(FrameRtbData record) {
		
		synchronized(mapFrmeDayStats) {
			FrameRtbData sum = mapFrmeDayStats.get(record.keyDayStats());
			if (sum == null) {
				mapFrmeDayStats.put(record.keyDayStats(), record);
			} else {
				sum.sumGethering(record);
			}
			
		}
	}
	
	// Table : FRME_TRN_LOG
	public void appendFrameTrnLog(FrameRtbData record) {
		
		synchronized(mapFrmeTrnLog) {
			FrameRtbData sum = mapFrmeTrnLog.get(record.keyTrnLog());
			if (sum == null) {
				mapFrmeTrnLog.put(record.keyTrnLog(), record);
			} else {
				sum.sumGethering(record);
			}
			
		}
	}
	
	// Table : FRME_COMBI_DAY_STATS
	public void appendFrameCombiDatStats(FrameRtbData record) {
		
		synchronized(mapFrmeCombiDayStats) {
			FrameRtbData sum = mapFrmeCombiDayStats.get(record.keyCombiDayStats());
			if (sum == null) {
				mapFrmeCombiDayStats.put(record.keyCombiDayStats(), record);
			} else {
				sum.sumGethering(record);
			}

		}
	}

		// Table : FRME_ADVER_DAY_STATS
	public void appendFrameAdverDayStats(FrameRtbData record) {

		synchronized(mapFrmeAdverDayStats) {
			FrameRtbData sum = mapFrmeAdverDayStats.get(record.keyAdverDayStats());
			if (sum == null) {
				mapFrmeAdverDayStats.put(record.keyAdverDayStats(), record);
			} else {
				sum.sumGethering(record);
			}

		}
	}
	public void appendFrameAdverDayAbStats(FrameRtbData record) {

		synchronized(mapFrmeAdverDayAbStats) {
			FrameRtbData sum = mapFrmeAdverDayAbStats.get(record.keyAdverDayAbStats());
			if (sum == null) {
				mapFrmeAdverDayAbStats.put(record.keyAdverDayAbStats(), record);
			} else {
				sum.sumGethering(record);
			}

		}
	}

	// Table : FRME_MEDIA_ADVER_STATS
	public void appendFrameMediaAdverStats(FrameRtbData record) {

		synchronized(mapFrmeMediaAdverStats) {
			FrameRtbData sum = mapFrmeMediaAdverStats.get(record.keyMediaAdverStats());
			if (sum == null) {
				mapFrmeMediaAdverStats.put(record.keyMediaAdverStats(), record);
			} else {
				sum.sumGethering(record);
			}

		}
	}

	// Table : FRME_CODE_STATS
	public void appendFrameCodeStats(FrameRtbData record) {

		synchronized(mapFrmeCodeStats) {
			FrameRtbData sum = mapFrmeCodeStats.get(record.keyFrmeCodeStats());
			if (sum == null) {
				mapFrmeCodeStats.put(record.keyFrmeCodeStats(), record);
			} else {
				sum.sumGethering(record);
			}

		}
	}

	//Table : FRME_IMG_DAY_STATS
	public void appendFrameSizeStats(FrameRtbData record) {
		
		synchronized(mapFrameSizeStats) {		
			FrameRtbData sum = mapFrameSizeStats.get(record.keyFrameSizeStats());
			if ( sum == null) {
				mapFrameSizeStats.put(record.getKeyFrameSizeStats(), record);
			} else {
				sum.sumGethering(record);
			}
		
		}
		
	}

	//Table : FRME_ACTION_LOG
	public void appendFrameActionLog(FrameRtbData record) {
		synchronized(mapFrameActionLog) {
			FrameRtbData sum = mapFrameActionLog.get(record.keyActionLog());
			if ( sum == null) {
				mapFrameActionLog.put(record.keyActionLog(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}
	
	//Table : FRME_ADVER_PRDT_CTGR_DAY_STATS
	public void appendFrameAdverPrdtCtgrDayStats(FrameRtbData record) {
		synchronized(mapFrameAdverPrdtCtgrDayStats) {
			FrameRtbData sum = mapFrameAdverPrdtCtgrDayStats.get(record.keyAdverCtgrStats());
			if (sum == null) {
				mapFrameAdverPrdtCtgrDayStats.put(record.keyAdverCtgrStats(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	//Table : FRME_KAIST_COMBI_DAY_STATS
	public void appendFrameKaistCombiDayStats(FrameRtbData record) {
		synchronized(mapFrameKaistCombiDayStats) {
			FrameRtbData sum = mapFrameKaistCombiDayStats.get(record.keyFrameKaistCombiDayStats());
			if ( sum == null ) {
				mapFrameKaistCombiDayStats.put(record.getKeyFrameKaistCombiDayStats(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}
	//Table : FRME_CTGR_DAY_STATS
	public void appendFrameCtgrDayStats(FrameRtbData record) {
		synchronized(mapFrameCtgrDayStats) {
			FrameRtbData sum = mapFrameCtgrDayStats.get(record.keyFrameCtgrDayStats());
			if ( sum == null ) {
				mapFrameCtgrDayStats.put(record.getKeyFrameCtgrDayStats(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}
	//Table : FRME_DAY_AB_STATS
	public void appendFrameDayAbStats(FrameRtbData record) {
		synchronized (mapFrameDayAbStats) {
			FrameRtbData sum = mapFrameDayAbStats.get(record.keyFrameDayABStats());
			if ( sum == null) {
				mapFrameDayAbStats.put(record.getKeyFrameDayABStats(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	// FrameCycleLog
	public Map<String, ?> removeFrmeCycleLog() {
		Map<String, ?> result = null;
		synchronized(mapFrmeCycleLog) {
			result = this.mapFrmeCycleLog;
			mapFrmeCycleLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	// FrameDayStats
	public Map<String, ?> removeFrmeDayData() {
		Map<String, ?> result = null;
		synchronized(mapFrmeDayStats) {
			result = this.mapFrmeDayStats;
			mapFrmeDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	// FrameTrnLog
	public Map<String, ?> removeFrmeTrnLog() {
		Map<String, ?> result = null;
		synchronized(mapFrmeTrnLog) {
			result = this.mapFrmeTrnLog;
			mapFrmeTrnLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	// FrameCombiDayStats
	public Map<String, ?> removeFrmeCombiDayStats() {
		Map<String, ?> result = null;
		synchronized(mapFrmeCombiDayStats) {
			result = this.mapFrmeCombiDayStats;
			mapFrmeCombiDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}

	// FrameAdverDayStats
	public Map<String, ?> removeFrmeAdverDayStats() {
		Map<String, ?> result = null;
		synchronized(mapFrmeAdverDayStats) {
			result = this.mapFrmeAdverDayStats;
			mapFrmeAdverDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	public Map<String, ?> removeFrmeAdverDayAbStats() {
		Map<String, ?> result = null;
		synchronized(mapFrmeAdverDayAbStats) {
			result = this.mapFrmeAdverDayAbStats;
			mapFrmeAdverDayAbStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}

	// FrameMediaAdverStats
	public Map<String, ?> removeFrmeMediaAdverStats() {
		Map<String, ?> result = null;
		synchronized(mapFrmeMediaAdverStats) {
			result = this.mapFrmeMediaAdverStats;
			mapFrmeMediaAdverStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}

	// FrameCodeStats
	public Map<String, ?> removeFrmeCodeStats() {
		Map<String, ?> result = null;
		synchronized(mapFrmeCodeStats) {
			result = this.mapFrmeCodeStats;
			mapFrmeCodeStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	public Map<String, ?> removeFrmeSizeStats() {
		Map<String, ?> result = null;
		synchronized(mapFrameSizeStats) {
			result = this.mapFrameSizeStats;
			mapFrameSizeStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	

	public Map<String, ?> removeFrmeActionLog() {
		Map<String, ?> result = null;
		synchronized(mapFrameActionLog) {
			result = this.mapFrameActionLog;
			mapFrameActionLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	//FrameAdverPrdtCtgrDayStats
	public Map<String, ?> removeFrameAdverPrdtCtgrDayStats() {
		Map<String, ?> result = null;
		synchronized(mapFrameAdverPrdtCtgrDayStats) {
			result = this.mapFrameAdverPrdtCtgrDayStats;
			mapFrameAdverPrdtCtgrDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
		}
		return result;
	}
	
	//FRME_KAIST_COMBI_DAY_STATS
		public Map<String, ?> removeFrameKaistCombiDayStats() {
			Map<String, ?> result = null;
			synchronized(mapFrameKaistCombiDayStats) {
				result = this.mapFrameKaistCombiDayStats;
				mapFrameKaistCombiDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
			}
			return result;
		}
		
		//FRME_CTGR_DAY_STATS
		public Map<String, ?> removeFrameCtgrDayStats() {
			Map<String, ?> result = null;
			synchronized(mapFrameCtgrDayStats) {
				result = this.mapFrameCtgrDayStats;
				mapFrameCtgrDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());
			}
			return result;
		}
		
		//FRME_DAY_AB_STATS
		public Map<String, ?> removeFrameDayABStats() {
			Map <String , ?> result = null;
			synchronized (mapFrameDayAbStats) {
				result = this.mapFrameDayAbStats;
				mapFrameDayAbStats = Collections.synchronizedMap(new HashMap<String , FrameRtbData>());
			}
			return result;
		}
	
//	private Map<String, NearData> mapNearData = Collections.synchronizedMap(new HashMap<String, NearData>());	// near
	private Map<String, FrameRtbData> mapFrmeCycleLog = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameCyclelog
	private Map<String, FrameRtbData> mapFrmeDayStats = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameDayStats
	private Map<String, FrameRtbData> mapFrmeTrnLog= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameTrnLog
	private Map<String, FrameRtbData> mapFrmeCombiDayStats= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameCombiDayStats
	private Map<String, FrameRtbData> mapFrmeAdverDayStats= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameAdverDayStats
	private Map<String, FrameRtbData> mapFrmeAdverDayAbStats= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameAdverDayStats
	private Map<String, FrameRtbData> mapFrmeMediaAdverStats= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameMediaAdverStats
	private Map<String, FrameRtbData> mapFrmeCodeStats= Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// FrameCodeStats
//	private Map<String, FrameRtbData> mapFrameRtbData = Collections.synchronizedMap(new HashMap<String, FrameRtbData>());	// near
	private Map<String, FrameRtbData> mapFrameSizeStats = Collections.synchronizedMap(new HashMap <String,FrameRtbData>()); //FramSizeStats
	private Map<String, FrameRtbData> mapFrameActionLog = Collections.synchronizedMap(new HashMap <String,FrameRtbData>());	//FramActionLog
	private Map<String, FrameRtbData> mapFrameAdverPrdtCtgrDayStats = Collections.synchronizedMap(new HashMap <String, FrameRtbData>()); //FrameAdverPrdtCtgrDayStats
	private Map<String, FrameRtbData> mapFrameKaistCombiDayStats = Collections.synchronizedMap(new HashMap <String, FrameRtbData>()); //FRME_KAIST_COMBI_DAY_STATS
	private Map<String, FrameRtbData> mapFrameCtgrDayStats = Collections.synchronizedMap(new HashMap<String , FrameRtbData>()); //FRME_CTGR_DAY_STATS
	private Map<String, FrameRtbData> mapFrameDayAbStats =Collections.synchronizedMap(new HashMap<String , FrameRtbData>()); //FRME_DAY_AB_STATS

//	public Map<String, NearData> getMapNearData() {
//		return mapNearData;
//	}
//
//	public void setMapNearData(Map<String, NearData> mapNearData) {
//		this.mapNearData = mapNearData;
//	}

	public Map<String, FrameRtbData> getMapFrmeCycleLog() {
		return mapFrmeCycleLog;
	}

	public void setMapFrmeCycleLog(Map<String, FrameRtbData> mapFrmeCycleLog) {
		this.mapFrmeCycleLog = mapFrmeCycleLog;
	}

	public Map<String, FrameRtbData> getMapFrmeDayStats() {
		return mapFrmeDayStats;
	}

	public void setMapFrmeDayStats(Map<String, FrameRtbData> mapFrmeDayStats) {
		this.mapFrmeDayStats = mapFrmeDayStats;
	}

	public Map<String, FrameRtbData> getMapFrmeTrnLog() {
		return mapFrmeTrnLog;
	}

	public void setMapFrmeTrnLog(Map<String, FrameRtbData> mapFrmeTrnLog) {
		this.mapFrmeTrnLog = mapFrmeTrnLog;
	}


	public Map<String, FrameRtbData> getMapFrmeCombiDayStats() {
		return mapFrmeCombiDayStats;
	}

	public void setMapFrmeCombiDayStats(Map<String, FrameRtbData> mapFrmeCombiDayStats) {
		this.mapFrmeCombiDayStats = mapFrmeCombiDayStats;
	}

	public Map<String, FrameRtbData> getMapFrmeAdverDayStats() {
		return mapFrmeCombiDayStats;
	}

	public void setMapFrmeAdverDayStats(Map<String, FrameRtbData> mapFrmeAdverDayStats) {
		this.mapFrmeAdverDayStats = mapFrmeAdverDayStats;
	}

	public void setMapFrmeAdverDayAbStats(Map<String, FrameRtbData> mapFrmeAdverDayAbStats) {
		this.mapFrmeAdverDayAbStats = mapFrmeAdverDayAbStats;
	}


	public Map<String, FrameRtbData> getMapFrameSizeStats() {
		return mapFrameSizeStats;
	}


	public void setMapFrameSizeStats(Map<String, FrameRtbData> mapFrameSizeStats) {
		this.mapFrameSizeStats = mapFrameSizeStats;
	}


	public Map<String, FrameRtbData> getMapFrameActionLog() {
		return mapFrameActionLog;
	}


	public void setMapFrameActionLog(Map<String, FrameRtbData> mapFrameActionLog) {
		this.mapFrameActionLog = mapFrameActionLog;
	}

	public Map<String, FrameRtbData> getMapFrmeMediaAdverStats() {
		return mapFrmeMediaAdverStats;
	}

	public void setMapFrmeMediaAdverStats(Map<String, FrameRtbData> mapFrmeMediaAdverStats) {
		this.mapFrmeMediaAdverStats = mapFrmeMediaAdverStats;
	}


	public Map<String, FrameRtbData> getMapFrameAdverPrdtCtgrDayStats() {
		return mapFrameAdverPrdtCtgrDayStats;
	}


	public void setMapFrameAdverPrdtCtgrDayStats(Map<String, FrameRtbData> mapFrameAdverPrdtCtgrDayStats) {
		this.mapFrameAdverPrdtCtgrDayStats = mapFrameAdverPrdtCtgrDayStats;
	}


	public Map<String, FrameRtbData> getMapFrameKaistCombiDayStats() {
		return mapFrameKaistCombiDayStats;
	}


	public void setMapFrameKaistCombiDayStats(Map<String, FrameRtbData> mapFrameKaistCombiDayStats) {
		this.mapFrameKaistCombiDayStats = mapFrameKaistCombiDayStats;
	}


	public Map<String, FrameRtbData> getMapFrameCtgrDayStats() {
		return mapFrameCtgrDayStats;
	}


	public void setMapFrameCtgrDayStats(Map<String, FrameRtbData> mapFrameCtgrDayStats) {
		this.mapFrameCtgrDayStats = mapFrameCtgrDayStats;
	}


	public Map<String, FrameRtbData> getMapFrameDayABStats() {
		return mapFrameDayAbStats;
	}


	public void setMapFrameDayABStats(Map<String, FrameRtbData> mapFrameDayAbStats) {
		this.mapFrameDayAbStats = mapFrameDayAbStats;
	}
	
	
}
