package com.mobon.dao.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;

//import com.adgather.util.ErrorLog;
import com.adgather.util.PropertyHandler;
import com.google.common.net.HostAndPort;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

public class MongoManager {
	private static final Logger LOG = LoggerFactory.getLogger(MongoManager.class);
	
	/////////////////////////////////////////////////////////////////////////////////////
	// MongoManager 전체 관리
	/////////////////////////////////////////////////////////////////////////////////////
	private static Map<String, MongoConnector> connectors = new HashMap<>();
	private static final String CONN_FRAME = "FRAME";
	private static final String CONN_FRAME_PRIMARY = "FRAME_PRIMARY";
	private static final String CONN_FRAME_DEFAULT_TIMEOUT = "FRAME_DEF_TIMEOUT";
	private static final String CONN_REPORT = "REPORT";
	private static final String CONN_KAKAO_LOG = "KAKAO_LOG";
	private static final String CONN_USER_LOG = "USER_LOG";
	private static final String CONN_AUID = "AUID";
	private static final String CONN_NLP = "NLP";
	private static final String CONN_KEYWORDCENTER = "KEYWORD_CENTER";
	
	@Autowired
	private MongoClient mongoShopLogClient;
	
	/**
	 * MongoDB connector들 로딩 (WAS 로딩시 처리)
	 * [신규 connector 생성 3/4]
	 */
	public static  void load() {
//		loadFrameConnector();
//		loadFramePrimaryConnector();
//		loadFrameDefTimeoutConnector();
//		loadReportConnector();		
//		if(PropertyHandler.getBoolean("IS_MONGO_CONN_KAKAO_LOG",false)) {			
//			loadKakaoLogConnector();
//		}
		loadUserLogConnector();
		loadAuidConnector();
//		loadAuidDefaultTimeOutConnector();
//		loadNLPConnector();
//		if(PropertyHandler.getBoolean("KEYWORD_SVC_INFO_LOG_YN",false)) {			
//			loadKeywordCenterConnector();
//		}
	}
	
	/**
	 * MongoDB connector들 자원해지(WAS 중지시 처리)
	 * [신규 connector 생성 4/4.E]
	 */
	public static void destroy() {
		Set<String> connNames = connectors.keySet();
		if(connNames == null || connNames.size() == 0) {
			return;
		}
		connNames = new HashSet<String>(connNames);

		for (String connName : connNames) {
			destoryConnector(connName);
		}
	}
	
	public static void reload() {
		destroy();
		load();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	private static boolean loadConnector(String connectorName, String[] mongoHosts, String id, String pw, String dbName) {
		return loadConnector(connectorName, mongoHosts, id, pw, dbName, null);
	}
	private static boolean loadConnector(String connectorName, String[] mongoHosts, String id, String pw, String dbName, Map<String, Object> secondOption) {
		boolean bRes = false;
		LOG.info("Load MongoDB Connector [{}] - begine", connectorName);
		if(mongoHosts == null || mongoHosts.length == 0) {
			LOG.info("Load Mongo Connectoer [{}] - fail : hostAddress do not setting[{}]", connectorName, ArrayUtils.toString(mongoHosts));
			return false;
		}
		
		// 재 로딩 할 경우를 위해 기존 connector 자원해지
		destoryConnector(connectorName);
		
		try {
			List<ServerAddress> servers = toServers(mongoHosts);
//			if(CollectionUtils.isEmpty(servers)) {
//				LOG.info("Load Mongo Connectoer [{}] - fail : hostAddress do not allow [{}]", connectorName, ArrayUtils.toString(mongoHosts));
//				return false;
//			} 
			
			MongoConnector connector = new MongoConnector(servers, id, pw, dbName, secondOption);
			bRes = connector != null;
			if(bRes) {
				connectors.put(connectorName, connector);
			} else {
				throw new Exception();
			}
			
			LOG.info("Complete Load MongoDB Connector [{}] - end", connectorName);
		} catch (Exception e) {
			LOG.error("fail to load mongo connector!!!!!!!>>>>>connectorName:{}, hosts:{}, dbName:{}", connectorName, ArrayUtils.toString(mongoHosts), dbName );
//			LOG.error(ErrorLog.getStack(e,""));
		}
		
		return bRes;
	}
	
	private static boolean loadConnector(String connectorName, String[] mongoHosts, String id, String pw, String dbName, boolean bDefaultTimeout) {
		boolean bRes = false;
		LOG.info("Load MongoDB Connector [{}] - begine", connectorName);
		if(mongoHosts == null || mongoHosts.length == 0) {
			LOG.info("Load Mongo Connectoer [{}] - fail : hostAddress do not setting[{}]", connectorName, ArrayUtils.toString(mongoHosts));
			return false;
		}
		
		// 재 로딩 할 경우를 위해 기존 connector 자원해지
		destoryConnector(connectorName);
		
		try {
			List<ServerAddress> servers = toServers(mongoHosts);
			if(CollectionUtils.isEmpty(servers)) {
				LOG.info("Load Mongo Connectoer [{}] - fail : hostAddress do not allow [{}]", connectorName, ArrayUtils.toString(mongoHosts));
				return false;
			}
			
			Map<String, Object> secondOption = null;
			if(bDefaultTimeout) {
				secondOption = MongoConnector.getDefaultTimeOfSecondOption();
			}
			
			
			MongoConnector connector = new MongoConnector(servers, id, pw, dbName, null, secondOption);
			bRes = connector != null;
			if(bRes) {
				connectors.put(connectorName, connector);
			} else {
				throw new Exception();
			}
			
			LOG.info("Complete Load MongoDB Connector [{}] - end", connectorName);
		} catch (Exception e) {
			LOG.error("fail to load mongo connector!!!!!!!>>>>>connectorName:{}, hosts:{}, dbName:{}", connectorName, ArrayUtils.toString(mongoHosts), dbName );
//			LOG.error(ErrorLog.getStack(e,""));
		}
		
		return bRes;
	}

	private static void destoryConnector(String connectorName) {
		if(connectorName == null)		return;
		
		MongoConnector connector = connectors.remove(connectorName);
		if(connector == null)	return;
		
		LOG.info("Destroy MongoDB Connector [" + connectorName + "] - begine");
		connector.destroy();
		connector = null;
		LOG.info("Destroy MongoDB Connector [" + connectorName + "] - end");
	}

	private static MongoCollection<Document> getCollection(String connectorName, String collectionName) {
		if(connectorName == null  || collectionName == null) 	return null;

		MongoConnector connector = connectors.get(connectorName);
		if(connector == null){
			LOG.error("MongoDB [" + connectorName + "] Has Not Connect.");
			return null;
		}
		
		return connector.getCollection(collectionName);
	}
	
	private static List<ServerAddress> toServers(String[] asHosts) {
		if(ArrayUtils.isEmpty(asHosts))	return null;
		
		List<ServerAddress> list = new ArrayList<ServerAddress>();
		
		for(String sHost : asHosts) {
			if(StringUtils.isEmpty(sHost))	continue;
			try {
				HostAndPort hostAndPort = HostAndPort.fromString(sHost);
				ServerAddress serverAddress = new ServerAddress(hostAndPort.getHost(), hostAndPort.getPort());
				
				list.add(serverAddress);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
		
		
		return list;
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// MongoConnector 개별 관리
	/////////////////////////////////////////////////////////////////////////////////////
	// ----------------------------------------------------------------------
	// Frame 관련 
	// ----------------------------------------------------------------------
	/**
	 * Frame 처리를 위한 MongoConnector 로딩
	 * [신규 connector 생성 1/4]
	 */
	public static boolean loadFrameConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_USER");
			String pw = PropertyHandler.getProperty("MONGO_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_DB");
			
			
			bRes = loadConnector(CONN_FRAME, mongoHosts, id, pw, dbName);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * Frame의 Collection 반환
	 * [신규 connector 생성 2/4]
	 */
	public static MongoCollection<Document> getFrameCollection(String collectionName) {
		return getCollection(CONN_FRAME, collectionName);
	}
	
	// ----------------------------------------------------------------------
	// Frame 관련 
	// ----------------------------------------------------------------------
	/**
	 * Frame 처리를 위한 MongoConnector 로딩
	 * [신규 connector 생성 1/4]
	 */
	public static boolean loadFrameDefTimeoutConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_USER");
			String pw = PropertyHandler.getProperty("MONGO_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_DB");
			
			bRes = loadConnector(CONN_FRAME_DEFAULT_TIMEOUT, mongoHosts, id, pw, dbName, true);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * Frame의 Collection 반환
	 * [신규 connector 생성 2/4]
	 */
	public static MongoCollection<Document> getFrameDefTimeoutCollection(String collectionName) {
		return getCollection(CONN_FRAME_DEFAULT_TIMEOUT, collectionName);
	}
	
	// ----------------------------------------------------------------------
	// Frame 관련 
	// ----------------------------------------------------------------------
	/**
	 * Frame 처리를 위한 MongoConnector 로딩
	 * [신규 connector 생성 1/4]
	 */
	public static boolean loadFramePrimaryConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_USER");
			String pw = PropertyHandler.getProperty("MONGO_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_DB");
			Map<String, Object> secondOption = MongoConnector.getReadPrimaryOfSecondOption();
			bRes = loadConnector(CONN_FRAME_PRIMARY, mongoHosts, id, pw, dbName, secondOption);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * Frame의 Collection 반환
	 * [신규 connector 생성 2/4]
	 */
	public static MongoCollection<Document> getFramePrimaryCollection(String collectionName) {
		return getCollection(CONN_FRAME_PRIMARY, collectionName);
	}

	// ----------------------------------------------------------------------
	// Report 관련 
	// ----------------------------------------------------------------------
	/**
	 * Report 처리를 위한 MongoConnector 로딩
	 */
	public static boolean loadReportConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_REPORT_USER");
			String pw = PropertyHandler.getProperty("MONGO_REPORT_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_REPORT_DB");
			
			bRes = loadConnector(CONN_REPORT, mongoHosts, id, pw, dbName);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * Report의 Collection 반환
	 */
	public static MongoCollection<Document> getReportCollection(String collectionName) {
		return getCollection(CONN_REPORT, collectionName);
	}

	// ----------------------------------------------------------------------
	// KAKAO LOG 관련 
	// ----------------------------------------------------------------------
	/**
	 * KAKAO_LOG 몽고DB connector
	 */
	public static boolean loadKakaoLogConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_KAKAO_USER");
			String pw = PropertyHandler.getProperty("MONGO_KAKAO_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_KAKAO_LOG_DB");			
			
			bRes = loadConnector(CONN_KAKAO_LOG, mongoHosts, id, pw, dbName);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * KAKAO_LOG의 Collection 반환
	 */
	public static MongoCollection<Document> getKakaoLogCollection(String collectionName) {
		return getCollection(CONN_KAKAO_LOG, collectionName);
	}

	// ----------------------------------------------------------------------
	// USER LOG 관련 
	// ----------------------------------------------------------------------
	
	/**
	 * USER LOG 처리를 위한 MongoConnector 로딩
	 */
	public static boolean loadUserLogConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS", ",");
			String id = PropertyHandler.getProperty("MONGO_USERLOG_USER");
			String pw = PropertyHandler.getProperty("MONGO_USERLOG_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_USERLOG_DB");
			
			bRes = loadConnector(CONN_USER_LOG, mongoHosts, id, pw, dbName);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * USER LOG의 Collection 반환
	 */
	public static MongoCollection<Document> getUserLogCollection(String collectionName) {
		return getCollection(CONN_USER_LOG, collectionName);
	}
	

	// ----------------------------------------------------------------------
	// AUTH 관련 
	// ----------------------------------------------------------------------
	
	/**
	 * USER LOG 처리를 위한 MongoConnector 로딩
	 */
	public static boolean loadAuidConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS_OF_AUID", ",");
			String id = PropertyHandler.getProperty("MONGO_AUID_USER");
			String pw = PropertyHandler.getProperty("MONGO_AUID_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_AUID_DB");

			/*	auid 타임 처리 기본 0이면 몽고자체의 기본값 이용
			Map<String, Object> secondOption = null;
			secondOption = MongoConnector.getDefaultTimeOfSecondOption();
			secondOption.put(MongoConnector.MONGO_SELECTION_TIMEOUT, 1000);
			*/
			
			bRes = loadConnector(CONN_AUID, mongoHosts, id, pw, dbName);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * USER LOG의 Collection 반환
	 */
	public static MongoCollection<Document> getAuidCollection(String collectionName) {
		return getCollection(CONN_AUID, collectionName);
	}
	
	// ----------------------------------------------------------------------
	// AUTH 관련 
	// ----------------------------------------------------------------------
		
	/**
	 * USER LOG 처리를 위한 MongoConnector 로딩
	 */
	public static boolean loadAuidDefaultTimeOutConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS_OF_AUID", ",");
			String id = PropertyHandler.getProperty("MONGO_AUID_USER");
			String pw = PropertyHandler.getProperty("MONGO_AUID_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_AUID_DB");
				
			bRes = loadConnector(CONN_AUID, mongoHosts, id, pw, dbName, true);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
		
	/**
	 * USER LOG의 Collection 반환
	 */
	public static MongoCollection<Document> getAuidDefaultTimeOUtCollection(String collectionName) {
		return getCollection(CONN_AUID, collectionName);
	}
	
	/**
	 * NLP 처리를 위한 MongoConnector 로딩
	 * [신규 connector 생성 1/4]
	 */
	public static boolean loadNLPConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS_NLP", ",");
			String id = PropertyHandler.getProperty("MONGO_NLP_USER");
			String pw = PropertyHandler.getProperty("MONGO_NLP_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_NLP_DB");
			
			
			Map<String, Object> secondOption = MongoConnector.getReadNlpPrimaryOfSecondOption();
			bRes = loadConnector(CONN_NLP, mongoHosts, id, pw, dbName, secondOption);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
	
	/**
	 * NLP의 Collection 반환
	 * [신규 connector 생성 2/4]
	 */
	public static MongoCollection<Document> getNLPCollection(String collectionName) {
		return getCollection(CONN_NLP, collectionName);
	}
	
	// ----------------------------------------------------------------------
	// KEYWORD LOG 관련 
	// ----------------------------------------------------------------------
		
	/**
	 * KEYOWORD CENTER LOG 처리를 위한 MongoConnector 로딩
	 */
	public static boolean loadKeywordCenterConnector() {
		boolean bRes = false;
		try {
			String[] mongoHosts = PropertyHandler.getSplitString("MONGO_HOSTS_KEYWORD", ",");
			String id = PropertyHandler.getProperty("MONGO_KEYWORDCENTER_USER");
			String pw = PropertyHandler.getProperty("MONGO_KEYWORDCENTER_PWD");
			String dbName = PropertyHandler.getProperty("MONGO_KEYWORDCENTER_DB");
				
			bRes = loadConnector(CONN_KEYWORDCENTER, mongoHosts, id, pw, dbName, true);
		} catch (Exception e) {
//			LOG.error(ErrorLog.getStack(e));
		}
		return bRes;
	}
		
	/**
	 * KEYWORD CENTER LOG의 Collection 반환
	 */
	public static MongoCollection<Document> getKeywordCenterCollection(String collectionName) {
		return getCollection(CONN_KEYWORDCENTER, collectionName);
	}

	/////////////////////////////////////////////////////////////////////////////////////

}
