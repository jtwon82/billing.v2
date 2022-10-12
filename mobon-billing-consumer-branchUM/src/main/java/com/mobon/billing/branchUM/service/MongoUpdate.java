package com.mobon.billing.branchUM.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.util.FileUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;

import net.sf.json.JSONObject;

@Service
public class MongoUpdate {

	private Logger			logger	= LoggerFactory.getLogger(MongoUpdate.class);

	@Autowired
	private MongoClient		mongoClient;
	
	@Value("${log.mongo.failpath}")
	private String			mongo_failpath;


	public void insertOne(String dbName, String FREFIX, Document doc) {
		String lastDate = DateUtils.getDate("yyyyMMdd");
		String collectionName = String.format("%s_%s", FREFIX, lastDate);
		
		doc.append("update_date", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

		getCollection(dbName, collectionName).insertOne(doc);
	}
	public void insertMany(String dbName, String FREFIX, List<Document> list) {
		String lastDate = DateUtils.getDate("yyyyMMdd");
		String collectionName = String.format("%s_%s", FREFIX, lastDate);

		getCollection(dbName, collectionName).insertMany(list);
	}
	public void insertDocumentData(String frefix, Document data) {
		String dbName = G.kafka_dbname;
		String FREFIX = frefix; 
		String lastDate = DateUtils.getDate("yyyyMMdd");
		String collectionName = String.format("%s_%s", FREFIX, lastDate);
		
		data.append("insertDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		getCollection(dbName, collectionName).insertOne(data);
	}
	
	public void insertBaseCVData(String dbName, String frefix, ArrayList<BaseCVData> list) {
		boolean result = false;
		
		String collectionName = frefix;
		logger.debug("collection Name ::: "+ collectionName);
		
		try {
			if (list.size() > 0) {
				for (BaseCVData vo : list) {
					Document setData = vo.toDocument(collectionName);
					setData.append("insertDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
					setData.append("_id", Math.random());
					
					getCollection(dbName, collectionName).insertOne(setData);
				}
			} else {
				logger.info("Collection Name -{} listSize is Zero", collectionName);
			}
			result = true;
		}catch (Exception e) {
			logger.error("err insertBase");
		} finally {
			if( !result ){
				FileUtils.mkFolder( mongo_failpath +"retry/" );
				
				for(BaseCVData vo : list){
					try {
						FileUtils.appendStringToFile(mongo_failpath +"retry/", 
								String.format("insertManyError_%s", DateUtils.getDate("yyyyMMdd_HH")), 
								String.format("%s\t%s\t%s", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), frefix, JSONObject.fromObject(vo).toString() ));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}
	
	public boolean insertManyBaseCVData(String dbName, String frefix, List<BaseCVData> list){
		boolean result = false;
		
		String collectionName = frefix; //String.format("%s_%s", frefix, DateUtils.getDate("yyyyMMdd"));
		logger.debug("collection Name :::" + collectionName);
		List<Document> datas = new ArrayList<Document>();
		try{
			if(list.size()>0){
				for(BaseCVData vo : list){
					
					Document setData = vo.toDocument(collectionName);
					setData.append("insertDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
					setData.append("_id", Math.random());
					
					datas.add(setData);
				}
				getCollection(dbName, collectionName).insertMany(datas, new InsertManyOptions().ordered(false));
			}
			else{
				//logger.debug("list.size() = {}", list.size());
			}
			
			result = true;
		}catch(Exception e){
			logger.error("err insertManyBaseCVData - {}", e.getMessage());
		}finally{
			if( !result ){
				FileUtils.mkFolder( mongo_failpath +"retry/" );
				
				for(BaseCVData vo : list){
					try {
						FileUtils.appendStringToFile(mongo_failpath +"retry/", 
								String.format("insertManyError_%s", DateUtils.getDate("yyyyMMdd_HH")), 
								String.format("%s\t%s\t%s", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), frefix, JSONObject.fromObject(vo).toString() ));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return result;
	}
	
	
	private MongoCollection getCollection(String dbName, String collectionName){
		MongoCollection collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
		return collection;
	}
	
	private boolean chkingMissingRequired(Map msg){
		boolean result = false;
		if( "".equals(msg.get("yyyymmdd")) || "".equals(msg.get("platform")) || "".equals(msg.get("product")) || "".equals(msg.get("adGubun")) || 
				"".equals(msg.get("type")) || "".equals(msg.get("siteCode")) || "".equals(msg.get("scriptNo")) || "".equals(msg.get("advertiserId")) || 
				"".equals(msg.get("scriptUserId")) || "".equals(msg.get("kno")) ){
			logger.info("Missing required, value - {}", msg);
			result = true;
		}
		return result;
	}
	
    public MongoUpdate() {}
    
	@SuppressWarnings("unused")
	public void deleteAndInsertManyBaseCVData(String dbName, String frepix, ArrayList<BaseCVData> data) {
		for (BaseCVData vo : data) {
			Document setData = new Document();
			setData.append("STATS_DTTM", vo.getYyyymmdd())
				.append("PLTFOM_TP_CODE", vo.getPlatform())
				.append("ADVRTS_PRDT_CODE", vo.getProduct())
				.append("ADVER_ID", vo.getAdvertiserId())
				.append("MEDIA_ID", vo.getScriptUserId())
				.append("ITL_TP_CODE", vo.getItlTpCode());
			Document findItem = (Document) this.getCollection(dbName, frepix).find(setData).first();

			if (findItem != null) {					
				vo.setPoint((float) ((Double) findItem.get("ADVRTS_AMT") + vo.getPoint()));
				vo.setViewCnt( (Integer) findItem.get("TOT_EPRS_CNT") + vo.getViewCnt());
				vo.setViewCnt3((Integer) findItem.get("PAR_EPRS_CNT") + vo.getViewCnt3());
				vo.setClickCnt((Integer) findItem.get("CLICK_CNT") + vo.getClickCnt());
				this.getCollection(dbName, frepix).deleteOne(setData);
			}
			
		}
		this.insertManyBaseCVData(dbName, frepix, data);								
		
	}

}
