package com.mobon.dao.mongo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.util.PropertyHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

public class MongoConnector {
	private static Logger logger = LoggerFactory.getLogger(MongoConnector.class);
	
	public static final String MONGO_CONNECTION_PER_HOST	= "MONGO_CONNECTION_PER_HOST";
	public static final String MONGO_CONN_IDLE_TIME			= "MONGO_CONN_IDLE_TIME";
	public static final String MONGO_CONN_LIFE_TIME			= "MONGO_CONN_LIFE_TIME";
	public static final String MONGO_CONN_TIMEOUT			= "MONGO_CONN_TIMEOUT";
	public static final String MONGO_SOCKET_TIMEOUT			= "MONGO_SOCKET_TIMEOUT";
	public static final String MONGO_MAXWAIT_TIMEOUT		="MONGO_MAXWAIT_TIMEOUT";
	public static final String MONGO_SELECTION_TIMEOUT		= "MONGO_SELECTION_TIMEOUT";
	
	public static final String MONGO_READ_PREFERENCE		= "MONGO_READ_PREFERENCE";
	public static final String READ_PRIMARY				= "PRIMARY";
	public static final String READ_PRIMARYPREFERRED	= "PRIMARYPREFERRED";
	public static final String READ_SECONDARY			= "SECONDARY";
	public static final String READ_SECONDARYPREFERRED	= "SECONDARYPREFERRED";
	public static final String READ_NEAREST				= "NEAREST";
	public static final String MONGO_NLP_READ_PREFERENCE = "MONGO_NLP_READ_PREFERENCE";
	
	private List<ServerAddress> servers;
/*
	private String host;
	private int port;
*/
	private String dbName;
	
	private MongoCredential credential;
	private MongoClientOptions option;

	private MongoClient client;
	private MongoDB db;

	protected MongoConnector(List<ServerAddress> servers, String id, String pw, String dbName, Map<String, Object> secondOption) throws Exception {
		this(servers, id, pw, dbName, null, secondOption);
	}
	
	protected MongoConnector(List<ServerAddress> servers, String id, String pw, String dbName, MongoClientOptions firstOption, Map<String, Object> secondOption) throws Exception {
		if(CollectionUtils.isEmpty(servers)) {
			throw new Exception("MongoDB host/port do not setting");
		}
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(pw)) {
			throw new Exception("MongoDB id/pw not found.");
		}
		if(StringUtils.isEmpty(dbName)) {
			throw new Exception("MongoDB dbName not found.");
		}
		
		logger.info("MongoDB Connect ServersInfo DBName[{}], Id[{}]", dbName, id);
		for(ServerAddress server : servers) {
			logger.info("MongoDB Connect Server : {}", server.getSocketAddress().toString());
		}
		
		if(firstOption != null) {
			this.option = firstOption;
			
		} else {
			// MongoConnector 생성
			int connPerHost 	= 500;			// 기본 500개
			int maxConnIdleTime = 300000;		// 기본 5*60초(5분)
			int maxConnLifeTime = 86400000;		// 기본 24*60분(24시간)
			int connTimeout 	= 500;			// 기본 500ms
			int socketTimeout 	= 500;			// 기본 500ms
			int maxWaitTimeout 	= 500;			// 기본 500ms
			int serverSelTimeout= 500;			// 기본 500ms

			if(secondOption != null) {
				connPerHost			= (Integer)getSecondValue(secondOption, MONGO_CONNECTION_PER_HOST,  PropertyHandler.getInt(MONGO_CONNECTION_PER_HOST, connPerHost));
				maxConnIdleTime		= (Integer)getSecondValue(secondOption, MONGO_CONN_IDLE_TIME,  		PropertyHandler.getInt(MONGO_CONN_IDLE_TIME, maxConnIdleTime));
				maxConnLifeTime		= (Integer)getSecondValue(secondOption, MONGO_CONN_LIFE_TIME,  		PropertyHandler.getInt(MONGO_CONN_LIFE_TIME, maxConnLifeTime));
				connTimeout			= (Integer)getSecondValue(secondOption, MONGO_CONN_TIMEOUT, 		PropertyHandler.getInt(MONGO_CONN_TIMEOUT, connTimeout));
				socketTimeout		= (Integer)getSecondValue(secondOption, MONGO_SOCKET_TIMEOUT, 		PropertyHandler.getInt(MONGO_SOCKET_TIMEOUT, socketTimeout));
				maxWaitTimeout		= (Integer)getSecondValue(secondOption, MONGO_MAXWAIT_TIMEOUT, 		PropertyHandler.getInt(MONGO_MAXWAIT_TIMEOUT, maxWaitTimeout));
				serverSelTimeout	= (Integer)getSecondValue(secondOption, MONGO_SELECTION_TIMEOUT, 	PropertyHandler.getInt(MONGO_SELECTION_TIMEOUT, serverSelTimeout));
				
			} else {
				connPerHost 	= PropertyHandler.getInt(MONGO_CONNECTION_PER_HOST, connPerHost);
				maxConnIdleTime = PropertyHandler.getInt(MONGO_CONN_IDLE_TIME, maxConnIdleTime);
				maxConnLifeTime = PropertyHandler.getInt(MONGO_CONN_LIFE_TIME, maxConnLifeTime);
				connTimeout 	= PropertyHandler.getInt(MONGO_CONN_TIMEOUT, connTimeout);
				socketTimeout 	= PropertyHandler.getInt(MONGO_SOCKET_TIMEOUT, socketTimeout);
				maxWaitTimeout 	= PropertyHandler.getInt(MONGO_MAXWAIT_TIMEOUT, maxWaitTimeout);
				serverSelTimeout= PropertyHandler.getInt(MONGO_SELECTION_TIMEOUT, serverSelTimeout);
			}
			
			String readPreference = PropertyHandler.getString(MONGO_READ_PREFERENCE);
			readPreference = (String)getSecondValue(secondOption, MONGO_READ_PREFERENCE, readPreference);
			
			logger.info("MongoDB Connect Set Option readPreference[{}], connPerHost[{}], macConnIdleTime[{}], maxConnLifeTime[{}], connTimeout[{}], socketTimeout[{}], maxWaitTimeout[{}], serverSelTimeout[{}]"
						,readPreference , connPerHost, maxConnIdleTime, maxConnLifeTime, connTimeout, socketTimeout, maxWaitTimeout, serverSelTimeout);
			
			
			MongoClientOptions.Builder builder = MongoClientOptions.builder();
			
			if(connPerHost > 0)			builder.connectionsPerHost(connPerHost);
			if(maxConnIdleTime > 0)		builder.maxConnectionIdleTime(maxConnIdleTime);
			if(maxConnLifeTime > 0)		builder.maxConnectionLifeTime(maxConnLifeTime);

			if(connTimeout > 0)			builder.connectTimeout(connTimeout);
			if(socketTimeout > 0)		builder.socketTimeout(socketTimeout);
			if(maxWaitTimeout > 0)		builder.maxWaitTime(maxWaitTimeout);
			if(serverSelTimeout > 0)	builder.serverSelectionTimeout(serverSelTimeout);
		
			if(StringUtils.isNotEmpty(readPreference)) {
				if(readPreference.toUpperCase().equals(READ_PRIMARY)){
					builder.readPreference(ReadPreference.primary());
					
				} else if(readPreference.toUpperCase().equals(READ_PRIMARYPREFERRED)){
					builder.readPreference(ReadPreference.primaryPreferred());
					
				} else if(readPreference.toUpperCase().equals(READ_SECONDARY)){
					builder.readPreference(ReadPreference.secondary());
					
				} else if(readPreference.toUpperCase().equals(READ_SECONDARYPREFERRED)){
					builder.readPreference(ReadPreference.secondaryPreferred());
					
				} else if(readPreference.toUpperCase().equals(READ_NEAREST)){
					builder.readPreference(ReadPreference.nearest());
				}
			}
			
			this.option = builder.build();
		}

		this.servers = servers;
		this.dbName = dbName;
		
		this.credential = MongoCredential.createCredential(id, dbName, pw.toCharArray());
		
		this.client = connect(this.servers, this.credential, this.option);
		this.db = new MongoDB(this.client, this.dbName);
		logger.info("MongoDB Connect ServersInfo DBName[{}], Id[{}]. Success", dbName, id);
	}
		
	private MongoClient connect(List<ServerAddress> servers, MongoCredential credential, MongoClientOptions option)  throws Exception {
		MongoClient client = null;

		if(option != null) {
			client = new MongoClient(servers, Arrays.asList(credential), option);
		} else {
			client = new MongoClient(servers, Arrays.asList(credential));
		}
		return client;
	}
	
	

	public void reconnect() throws Exception {
		close();
		this.client = connect(this.servers, this.credential, this.option);
		this.db = new MongoDB(this.client, this.dbName);
	}	

	public void close() {
		if(db != null) {
			db.destroy();
			db = null;
		}
		
		if(client != null) {
			client.close();
		}
		client = null;
	}
	
	public void destroy() {
		close();
		credential = null;
		option = null;
		dbName = null;
		servers.clear();
	}
	
	public MongoCollection<Document>  getCollection(String collectionName) {
		if(db == null)		return null;
		
		return db.getCollection(collectionName);
	}
	
	private Object getSecondValue(Map<String, Object> secondOption, String key, Object defaultValue) {
		if(secondOption == null) {
			return defaultValue;
		}
		
		Object secondValue = secondOption.get(key);
		if(secondValue == null) {
			return defaultValue;
		}
		
		return secondValue;
	}
	
	public static Map<String, Object> getDefaultTimeOfSecondOption() {
		Map<String, Object> secondOption = new HashMap<String, Object>();
		secondOption.put(MONGO_CONN_TIMEOUT, 0);
		secondOption.put(MONGO_SOCKET_TIMEOUT, 0);
		secondOption.put(MONGO_MAXWAIT_TIMEOUT, 0);
		secondOption.put(MONGO_SELECTION_TIMEOUT, 0);
		
		return secondOption;
	}
	
	public static Map<String, Object> getReadPrimaryOfSecondOption() {
		Map<String, Object> secondOption = new HashMap<String, Object>();
		secondOption.put(MONGO_READ_PREFERENCE, READ_PRIMARY);
		
		return secondOption;
	}
	
	public static Map<String, Object> getReadNlpPrimaryOfSecondOption() {
		Map<String, Object> secondOption = new HashMap<String, Object>();
		secondOption.put(MONGO_READ_PREFERENCE, PropertyHandler.getString(MONGO_NLP_READ_PREFERENCE));
		
		return secondOption;
	}
	
}
