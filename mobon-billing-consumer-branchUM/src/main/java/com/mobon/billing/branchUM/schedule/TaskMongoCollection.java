package com.mobon.billing.branchUM.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

@Component
public class TaskMongoCollection {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskMongoCollection.class);
	
	@Value("${spring.data.mongodb.host}")
	private String mongoHost;
	@Value("${spring.data.mongodb.port}")
	private int port;
	@Value("${spring.data.mongodb.database}")
	private String databaseName;
	
	@SuppressWarnings("deprecation")
	public void taskCreateDropCollection() {
		logger.info("CreateDropCollection Start");
		MongoClient  mongoClient = new MongoClient(new ServerAddress(mongoHost, port));
		
		MongoDatabase database = mongoClient.getDatabase(databaseName);
		DB db = mongoClient.getDB(databaseName);
		Set<String> getCollectionList = db.getCollectionNames();
		
		String [] collectionNames = {"SITECODE","SCRIPTNO","KPI"};
		
		ArrayList<Object> getDropDateList  = this.getDropDate(); 
		ArrayList<String> getDropCollectionList = new ArrayList<String>();
		
		getDropCollectionList = this.changeDateFormat(collectionNames, getDropDateList);
		logger.info("DropCollection Start");
		for (String dropCollectionName : getDropCollectionList) {	
			for (String exitCollection : getCollectionList) {
				if (dropCollectionName.equals(exitCollection)) {
					database.getCollection(dropCollectionName).drop();
				}
			}
		}
		
		logger.info("DropCollection End DropCollection List " + getDropDateList.toString());	
		
		ArrayList<Object> getCreateDateList = this.getCreateDate();
		ArrayList<String> getCreateCollectionList = this.changeDateFormat(collectionNames, getCreateDateList);
		
		logger.info("CreateCollection List " + getCreateDateList.toString());
			for (String collectionName : getCreateCollectionList) {
				try {
					logger.info("Collection Create ::: "+ collectionName);		
					database.createCollection(collectionName);
				} catch (Exception e) {
					logger.info("collection exit ::: "+ collectionName);
					continue;
				}
			}			
		
		for (String collectionName : getCreateCollectionList) {
					//index 설정 		
					if (collectionName.indexOf("SITECODE") >= 0) {
						database.getCollection(collectionName).createIndex(new Document("STATS_DTTM", 1).append("SITE_CODE", 1).append("PLTFOM_TP_CODE", 1));
					} else if (collectionName.indexOf("SCRIPTNO") >= 0) {
						database.getCollection(collectionName).createIndex(new Document("STATS_DTTM", 1).append("MEDIA_SCRIPT_NO", 1).append("PLTFOM_TP_CODE", 1));
					} else if (collectionName.indexOf("KPI") >= 0) {
						database.getCollection(collectionName).createIndex(new Document("STATS_DTTM", 1).append("ADVER_ID", 1).append("MEDIA_ID", 1).append("PLTFOM_TP_CODE", 1));					
			}
		}
		logger.info("CreateDropCollection End");
		
	}

	private ArrayList<String> changeDateFormat(String[] collectionNames, ArrayList<Object> dateList) {
		ArrayList<String> result = new ArrayList<String>();
		
		for ( String collectionName : collectionNames) {
			for (Object date : dateList) {
				String collection = collectionName+"_"+date;
				result.add(collection);
			}	
		}
		
		return result;
	}

	private ArrayList<Object> getDropDate() {
		ArrayList <Object> result = new ArrayList<Object>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(new Date());
		String dateFormat = format.format(cal.getTime());
		
		for (int i = -20; i >= -50 ; i--) {
			cal.setTime(new Date());
			cal.add(Calendar.DATE, i);
			dateFormat = format.format(cal.getTime());
			result.add(dateFormat);
		}	
		
		return result;
	}
	
	private ArrayList<Object> getCreateDate() {
		ArrayList <Object> result = new ArrayList<Object>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(new Date());
		String dateFormat = format.format(cal.getTime());
		
		for (int i = 0; i <= 2 ; i++) {
			cal.setTime(new Date());
			cal.add(Calendar.DATE, i);
			dateFormat = format.format(cal.getTime());
			result.add(dateFormat);
		}
		
		return result;
	}
	

}
