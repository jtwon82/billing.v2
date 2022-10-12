package com.adgather.user.data.mongodb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.adgather.servlet.ConfigServlet;
import com.adgather.util.PropertyHandler;
import com.mobon.dao.mongo.TimeChecker;
import com.mongodb.client.MongoCollection;

import net.sf.json.JSONArray;

public class CIMDaoLogger {
	static final Logger logger = LoggerFactory.getLogger(CIMDaoLogger.class);
	static final Logger collectLog = LoggerFactory.getLogger("CIMDaoLogger");

	private static SimpleDateFormat SDF= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	private static Random random = new Random();
	private TimeChecker timeChecker;
	
	/** 생성자(시작시간) **/
	public CIMDaoLogger() {
		timeChecker = TimeChecker.start();
	}
	
	/** 문자열 현재시간 확인 **/
	private String getCurTime() {
		return SDF.format(new Date());
	}
	
	private boolean isLogWrite() {
//		boolean b = PropertyHandler.contain("CIMDAO_COLLECTER_HOSTS", "\\|", ConfigServlet.hostId);
//		if(!b) {
//			return false;
//		}
//		
		float per = PropertyHandler.getFloat("CIMDAO_COLLECTER_PER", 0);
//		if(per <= 0f) {
//			return false;
//		}
		
		return (random.nextInt(100) <= 100*per);
	}
	
	/** 로그 쓰기(문자열 요청/결과) **/
	private void log(String queryDoc, String resultDoc) {
		try {
			long elapseTime = timeChecker.end();
			
			if(queryDoc == null) {
				queryDoc = "";
			}
			if(resultDoc == null) {
				resultDoc = "";
			}
			
			collectLog.info(String.format("%s\t%dms\t%s\t%s", getCurTime(), elapseTime, queryDoc, resultDoc));
		} catch (Exception e) {
			logger.error(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	/** find 로그 **/
	public void findLog(MongoCollection<Document> collection, Document queryDoc, Document resultDoc) {
		if(collection == null)	return;
		if(collection.getNamespace() == null)	return;
		
		findLog(collection.getNamespace().getCollectionName(), queryDoc, resultDoc);
	}
	public void findLog(String collectionName, Document queryDoc, Document resultDoc) {
		try {
			if(!isLogWrite())	return;
			
			String sQueryDoc = null;
			if(queryDoc != null) {
				sQueryDoc = queryDoc.toJson();
				sQueryDoc = "db."+collectionName+".find(" + sQueryDoc + ");";
			}
			
			String sResultDoc = null;
			if(resultDoc != null) {
				sResultDoc = resultDoc.toJson();
			}
			
			log(sQueryDoc, sResultDoc);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/** find 로그 **/
	public void findLog(String collectionName, Document queryDoc, Map<String, Document> resultMap) {
		try {
			if(!isLogWrite())	return;
			
			String sQueryDoc = null;
			if(queryDoc != null) {
				sQueryDoc = queryDoc.toJson();
				sQueryDoc = "db."+collectionName+".find(" + sQueryDoc + ");";
			}
			
			String sResultDoc = null;
			if(resultMap != null) {
				JSONArray jsonArray = JSONArray.fromObject(resultMap);
				sResultDoc = jsonArray.toString();
			}
			
			log(sQueryDoc, sResultDoc);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/** update 로그 **/
	public void insertLog(String collectionName, Document queryDoc, Document setDoc) {
		try {
			if(!isLogWrite())	return;
			
			String sSetDoc = "";
			if(setDoc != null) {
				sSetDoc = setDoc.toJson();
			}
			
			String sQueryDoc = null;
			if(queryDoc != null) {
				sQueryDoc = queryDoc.toJson();
				sQueryDoc = "db."+collectionName+".insert(" + sQueryDoc + ", " + sSetDoc + ");";
			}
			
			log(sQueryDoc, null);
		} catch (Exception e) {
			logger.error(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	/** update 로그 **/
	public void updateLog(MongoCollection<Document> collection, Document queryDoc, Document setDoc, boolean bUpsert) {
		if(collection == null)	return;
		if(collection.getNamespace() == null)	return;
		
		updateLog(collection.getNamespace().getCollectionName(), queryDoc, setDoc, bUpsert);
	}
	public void updateLog(String collectionName, Document queryDoc, Document setDoc, boolean bUpsert) {
		try {
			if(!isLogWrite())	return;
			
			String sSetDoc = "";
			if(setDoc != null) {
				sSetDoc = setDoc.toJson();
			}
			
			String sQueryDoc = null;
			if(queryDoc != null) {
				sQueryDoc = queryDoc.toJson();
				sQueryDoc = "db."+collectionName+".update(" + sQueryDoc + ", " + sSetDoc + (bUpsert ? ", {\"upsert\": true}" : "") + ");";
			}
			
			log(sQueryDoc, null);
		} catch (Exception e) {
			logger.error(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	/** update 로그 **/
	public void deleteLog(MongoCollection<Document> collection, Document queryDoc) {
		if(collection == null)	return;
		if(collection.getNamespace() == null)	return;
		deleteLog(collection.getNamespace().getCollectionName(), queryDoc);
	}
	public void deleteLog(String collectionName, Document queryDoc) {
		try {
			if(!isLogWrite())	return;
			
			String sQueryDoc = null;
			if(queryDoc != null) {
				sQueryDoc = queryDoc.toJson();
				sQueryDoc = "db."+collectionName+".delete(" + sQueryDoc + ");";
			}
			
			log(sQueryDoc, null);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
