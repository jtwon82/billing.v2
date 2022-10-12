package com.adgather.user.data.mongodb;

import com.mobon.dao.mongo.MongoManager;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.joda.time.DateTime;

import java.util.*;

/**
 * 탈쿠키 몽고정보관리자
 * 
 * @date 2017. 6. 30.
 * @param
 * @exception @see
 */
public class CIPartitionMDao {
	private static final int COLLECTION_COUNT = 20;
	private static final String COLLECTION_PREFIX = "CIData_";
	private static final Map<Integer, String> COLLECTION_MAP = new HashMap<Integer, String>();
	static {
		for (int idx = 0; idx < COLLECTION_COUNT; idx++) {
			String suffix = String.format("%02d", idx);
			COLLECTION_MAP.put(idx, COLLECTION_PREFIX + suffix);
		}
	}

	public static int getCollectionCnt() {
		return COLLECTION_COUNT;
	}

	public static int getPartition(String key) {
		return Math.abs(key.hashCode() % COLLECTION_COUNT);
	}

	/** collection single instance (메소드로만 접근) **/
	private static List<MongoCollection<Document>> getCollections() {
		List<MongoCollection<Document>> list = new ArrayList<>();
		for (Map.Entry<Integer, String> entry : COLLECTION_MAP.entrySet()) {
			String collectionName = entry.getValue();
			if (StringUtils.isEmpty(collectionName))
				continue;

			MongoCollection<Document> collection = MongoManager.getUserLogCollection(collectionName);
			list.add(collection);
		}

		return list;
	}

	private static MongoCollection<Document> getCollection(String key) {
		if (StringUtils.isEmpty(key))
			return null;

		int partition = getPartition(key);
		return getCollection(partition);
	}

	private static MongoCollection<Document> getCollection(int partition) {
		if (partition < 0 || partition >= COLLECTION_COUNT)
			return null;

		String collectionName = COLLECTION_MAP.get(partition);
		return MongoManager.getUserLogCollection(collectionName);
	}

	/** 탈쿠키 데이터 조회 **/
	public static Document get(String key) {
		if (StringUtils.isEmpty(key))
			return null;

		Document queryDoc = new Document("_id", key);

		CIMDaoLogger collecter = new CIMDaoLogger();
		MongoCollection<Document> collection = getCollection(key);
		FindIterable<Document> findIter = getCollection(key).find(queryDoc);
  		if (findIter == null)
			return null;

		Document doc = findIter.first();

		collecter.findLog(collection, queryDoc, doc);

		if (collection != null && doc != null) {
			try {
				doc.put("collectionName", collection.getNamespace().getCollectionName());
			} catch (Exception e) {
			}
		}

		return doc;
	}

	public static Map<String, Document> gets(String[] keys) {
		if (ArrayUtils.isEmpty(keys))
			return null;

		Map<String, Document> map = new HashMap<String, Document>();
		for (String key : keys) {
			Document doc = get(key);
			map.put(key, doc);
		}
		return map;
	}

	/** 탈쿠키 데이터 저장 **/
	public static void set(String key, Document setDoc, Document unsetDoc) {
		if (key == null)
			return;
		if (setDoc == null || setDoc.size() == 0)
			return;

		// 변경 대상 선택
		Document queryDoc = new Document("_id", key);

		// 변경일 추가
		Date date = new DateTime().toDate();
		setDoc.append("last_update_datetime", date);

		// 변경 데이터 구성
		Document set = new Document();
		set.append("$set", setDoc);
		if (unsetDoc != null && unsetDoc.size() > 0) {
			set.append("$unset", unsetDoc);
		}
		Document setOnInsert = new Document();
		setOnInsert.put("key", key);
		setOnInsert.put("regist_datetime", date);
		set.append("$setOnInsert", setOnInsert);

		UpdateOptions options = new UpdateOptions();
		options.upsert(true);

		CIMDaoLogger collecter = new CIMDaoLogger();
		// Update 처리

		MongoCollection<Document> collection = getCollection(key);
		collection.updateOne(queryDoc, set, options);

		collecter.updateLog(collection, queryDoc, set, true);
	}

	/** 탈쿠키 데이터 삭제 **/
	public static void del(String key) {
		if (key == null)
			return;

		Document queryDoc = new Document("_id", key);

		CIMDaoLogger collecter = new CIMDaoLogger();

		MongoCollection<Document> collection = getCollection(key);
		collection.deleteOne(queryDoc);

		
		collecter.deleteLog(collection, queryDoc);
	}

	/** 객체 생성 차단 **/
	private CIPartitionMDao() {
	}
}
