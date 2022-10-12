package com.mobon.dao.mongo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.adgather.util.DateUtils;
import com.adgather.util.PropertyHandler;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

public class MongoStatus {
	static Logger logger = Logger.getLogger(MongoStatus.class);
	
	private static MongoCollection<Document> getCollection() {
		return MongoManager.getUserLogCollection("PingTest");
	}
	
	/** 몽고 상태
	 * Map	time	:	20170901011211
	 * 		res		:	true / false
	 * 		msg		:	(결과 메시지; 알림 메시지)
	 * 		notice	: 	(true; 알림 대상만)
	 *  **/
	public static Map<String, Object> getCollectionStatus() {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("time", DateUtils.getToday("yyyyMMddHHmmss"));
		boolean bAllSuccess = true;
		try {
			String objIds[] = PropertyHandler.getSplitString("MONGO_STATUS_OBJIDS", ",");
			
			int minFailCnt = PropertyHandler.getInt("MONGO_STATUS_MIN_FAIL", 1);
			
			
			if(objIds == null) {
				resMap.put("res", false);
				resMap.put("msg", "Property Is Not Found[MONGO_STATUS_OBJIDS]");
				return resMap;
			}

			int rsSyncWaitTime = PropertyHandler.getInt("MONGO_STATUS_RS_SYNC_WAITTIME", 100);
			
			List<String> objIdList = Arrays.asList(objIds);

			resMap.put("notice", true);		// 알림 대상 여부(이라인 이하는 알림 대상임)
			
			Map<String, Object> putData = new HashMap<String, Object>();
			putData.put("curTime", System.currentTimeMillis());
			
			int failCnt = 0;
			
			StringBuffer failMsg = new StringBuffer();
			for(int idx = 0; idx < objIdList.size(); idx++) {
				Map<String, Object> setRes = setStatus(objIdList.get(idx), putData);
				Object bNotice = setRes.get("notice");
				Object bSetSuccess = setRes.get("res");
				Object sMsg = setRes.get("msg");
				
				if(bNotice != null && (Boolean)bNotice
					&& bSetSuccess != null && !(Boolean)bSetSuccess) {
					failCnt++;
					if(failMsg.length() > 0) {
						failMsg.append(",");
					}
					failMsg.append("RS").append(idx + 1).append("-PRI[").append(sMsg).append("]");
				}
				resMap.put("RS" + (idx+1) + "-PRI" , sMsg);
			}
			
			Thread.sleep(rsSyncWaitTime);
			
			for(int idx = 0; idx < objIdList.size(); idx++) {
				Map<String, Object> setRes = getStatus(objIdList.get(idx), putData, rsSyncWaitTime);
				Object bNotice = setRes.get("notice");
				Object bSetSuccess = setRes.get("res");
				Object sMsg = setRes.get("msg");
				
				if(bNotice != null && (Boolean)bNotice
					&& bSetSuccess != null && !(Boolean)bSetSuccess) {
					failCnt++;
					if(failMsg.length() > 0) {
						failMsg.append(",");
					}
					failMsg.append("RS").append(idx + 1).append("-SEC[").append(sMsg).append("]");
				}
				resMap.put("RS" + (idx+1) + "-SEC" , sMsg);
			}
			
			if(failCnt >= minFailCnt) {
				bAllSuccess = false;
			}
			
			
			resMap.put("res", bAllSuccess);			// 결과
			if(bAllSuccess) {
				resMap.put("msg", "Success.");
			} else {
				resMap.put("msg", failMsg.toString());
			}
		} catch (Exception e) {
			resMap.put("res", false);			// 결과
			resMap.put("msg", e.getMessage());
		}
		return resMap;
	}
	
	private static Map<String, Object> setStatus(String objId, Map<String, Object> putData) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(StringUtils.isEmpty(objId)) {
			resMap.put("res", false);
			resMap.put("msg", "ObjectId Is Null");
			return resMap;
		}
		if(MapUtils.isEmpty(putData)) {
			resMap.put("res", false);
			resMap.put("msg", "PushData Is Empty.");
			return resMap;
		}
		
		try {
			Document queryDoc = new Document();
			queryDoc.put("_id", new ObjectId(objId));
			
			Document setDoc = new Document();
			setDoc.putAll(putData);
			
			TimeChecker timeChecker = TimeChecker.start();
			UpdateResult updRes = getCollection().updateOne(queryDoc, new Document("$set", setDoc));
			long elapseTime = timeChecker.end();
			
			if(updRes == null) {
				resMap.put("res", false);
				resMap.put("msg", "UpdateResult Is Null.(ElapseTime : " + elapseTime + " ms)");
				resMap.put("notice", true);
				return resMap;
			}
			
			boolean b = updRes.getMatchedCount() > 0 || updRes.getModifiedCount() > 0;
			resMap.put("res", b);
			resMap.put("msg", "Write "+ (b ? "Success" : "Fail") + ".(ElapseTime : " + elapseTime + " ms)");
			resMap.put("notice", true);

		} catch (MongoTimeoutException e) {
			resMap.put("res", false);
			resMap.put("msg", "Write Time Out.");
			resMap.put("notice", true);
		} catch (Exception e) {
			resMap.put("res", false);
			resMap.put("msg", "Write Fail. " + e.getMessage());
			resMap.put("notice", true);
		}

		return resMap;
	}
	
	
	private static Map<String, Object> getStatus(String objId, Map<String, Object> putData, int syncWaitTime) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(StringUtils.isEmpty(objId)) {
			resMap.put("res", false);
			resMap.put("msg", "ObjectId Is Null");
			return resMap;
		}
		if(MapUtils.isEmpty(putData)) {
			resMap.put("res", false);
			resMap.put("msg", "PushData Is Empty.");
			return resMap;
		}
		
		try {
			Document queryDoc = new Document();
			queryDoc.put("_id", new ObjectId(objId));

			TimeChecker timeChecker = TimeChecker.start();
			FindIterable<Document> findIter = getCollection().find(queryDoc);
			long elapseTime = timeChecker.end();
			
			if(findIter == null) {
				resMap.put("res", false);
				resMap.put("msg", "FindResult Is Null.(ElapseTime : " + elapseTime + " ms)");
				resMap.put("notice", true);
				return resMap;
			}
			
			Document resDoc = findIter.first();
			if(resDoc == null) {
				resMap.put("res", false);
				resMap.put("msg", "Result Document Is Null.(ElapseTime : " + elapseTime + " ms)");
				resMap.put("notice", true);
				return resMap;
			}
			
			Long putCurTime = (Long)putData.get("curTime");
			Long getCurTime = resDoc.getLong("curTime");
			if(putCurTime != null && getCurTime != null) {
				if(putCurTime.longValue() == getCurTime.longValue()) {
					resMap.put("res", true);
					resMap.put("msg", "Read Success.(ElapseTime : " + elapseTime + " ms)");
					resMap.put("notice", true);
				} else {
					resMap.put("res", true);
					resMap.put("msg", "Delayed Data Sync.(SyncWaitTime : " + syncWaitTime + " ms)");
					resMap.put("notice", true);
				}
			} else {
				resMap.put("res", false);
				resMap.put("msg", "Read Fail.(ElapseTime : " + elapseTime + " ms)");
				resMap.put("notice", true);
			}
			
		} catch (MongoTimeoutException e) {
			resMap.put("res", false);
			resMap.put("msg", "Read Time Out.");
			resMap.put("notice", true);
			
		} catch (Exception e) {
			resMap.put("res", false);
			resMap.put("msg", "Read Fail. " + e.getMessage());
			resMap.put("notice", true);
		}
		
		return resMap;
	}
}
