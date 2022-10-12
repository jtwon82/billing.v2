package com.mobon.dao.mongo;

import org.apache.log4j.Logger;
import org.bson.Document;

//import com.adgather.util.ErrorLog;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	private static Logger logger = Logger.getLogger(MongoDB.class);
	

	private MongoDatabase database;
	
	public MongoDB(MongoClient client, String dbName) {
		this.database = load(client, dbName);
	}
	
	private MongoDatabase load(MongoClient client , String dbName) {
		if(client == null || dbName == null )		return null;
		
		MongoDatabase database = null;
		try {
			database = client.getDatabase(dbName);
		} catch (Exception e) {
//			logger.error(ErrorLog.getStack(e));
		}
		
		return database;
	}
	
	public void destroy() {
		database = null;
	}
	
	public  MongoCollection<Document> getCollection(String collectionName) {
		if(database == null)		return null;
		
		return getCollection(this.database, collectionName);
	}
	
	private MongoCollection<Document> getCollection(MongoDatabase database, String collectionName) {
		if(database == null || collectionName == null) 	return null;
		
		MongoCollection<Document> collection = null;
		try {
			collection = database.getCollection(collectionName);
		} catch (Exception e) {
//			logger.error(ErrorLog.getStack(e));
		}
		return collection;
	}
}
