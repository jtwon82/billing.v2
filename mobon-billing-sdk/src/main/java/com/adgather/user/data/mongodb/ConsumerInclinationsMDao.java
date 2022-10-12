package com.adgather.user.data.mongodb;

import com.adgather.constants.GlobalConstants;
import com.adgather.util.PropertyHandler;
import jersey.repackaged.com.google.common.collect.Sets;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 탈쿠키 몽고정보관리자
 * @date 2017. 6. 30.
 * @param 
 * @exception
 * @see
*/
public class ConsumerInclinationsMDao {
	private static final Logger logger = LoggerFactory.getLogger(ConsumerInclinationsMDao.class);
	
	/** 탈쿠키 데이터 조회 **/
	public static Document get(String key) {
		if (key == null)				return null;
		
		return CIPartitionMDao.get(key);
	}
	
	public static Map<String, Document> gets(String[] keys) {
		if (keys == null || keys.length ==0)				return null;

		Map<String, Document> map = new HashMap<String, Document>();
		
		for(String key : keys) {
			if(StringUtils.isEmpty(key)) continue;
			
			Document doc = get(key);
			if(doc == null)	continue;
			
			map.put(key, doc);
		}
		
		return map;
	}
	
	/** 탈쿠키 데이터 저장 **/
	public static void set(String key, Document setDoc, Document unsetDoc) {
		if (key == null)	return;
		CIPartitionMDao.set(key, setDoc, unsetDoc);
	}
	
	/** 탈쿠키 데이터 삭제 **/
	public static void del(String key) {
		if (key == null)		return;

		CIPartitionMDao.del(key);
	}
	
	/** 기타 정보 확인용 ************************************************************************ **/

	private static Set<String> dontVisibleDataKeys = Sets.newHashSet("_id", "key", "regist_datetime", "last_update_datetime");
	public static Set<String> getDontVisibleDataKeys() {
		return dontVisibleDataKeys;
	}

	public static List<String> getDataKeys(String key) {
		Document doc = get(key);
		if(doc == null) return null;
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(doc.keySet());
		keys.removeAll(getDontVisibleDataKeys());
		Collections.sort(keys);
		return keys;
	}
	
	public static void removeDataKey(String key, String dataKey) {
		if (StringUtils.isEmpty(key))		return;
		if (StringUtils.isEmpty(dataKey))		return;
		
		Set<String> dataKeys = new HashSet<String>();
		dataKeys.add(dataKey);
		removeDataKey(key, dataKeys);
	}
	
	public static void removeDataKey(String key, Set<String> dataKeys) {
		if (StringUtils.isEmpty(key))		return;
		if (dataKeys == null || dataKeys.size() ==0)		return;
		
		Document setDoc = new Document();
		Date date = new DateTime().toDate();
		setDoc.append("last_update_datetime", date);
		
		Document unsetDoc = new Document();
		for (String dataKey : dataKeys) {
			unsetDoc.append(dataKey, "");
		}
		set(key, setDoc, unsetDoc);
	}

	/** 기타 정보 확인용 ************************************************************************ **/
	
	public static Map<String, Object> getInfoMap(String keyIp, String auId) {
		if(!PropertyHandler.getProperty("USER_LOG_STORAGE").equals(GlobalConstants.MONGODB))		return null;
		
		String key = auId; //PropertyHandler.isTrue("AUID_USE") ? auId : keyIp;
		Document doc  = ConsumerInclinationsMDao.get(key);
		if(doc == null)		return null;
		
		Set<String> notVisibleSet = ConsumerInclinationsMDao.getDontVisibleDataKeys();

		long length = 0;
		long cnt = 0;
		StringBuffer buf = new StringBuffer();
		buf.append("<STRONG>[MongoDB]</STRONG><BR>\r\n");
		List<String> dataKeys = new ArrayList<String>();
		for (String dataKey : doc.keySet()) {
			if(notVisibleSet.contains(dataKey))	continue;

			dataKeys.add(dataKey);
		}

		Collections.sort(dataKeys);
		
		for (String dataKey : dataKeys) {			
			Object valObj = doc.get(dataKey);
			String valStr = "";
			if(valObj instanceof String &&  "shoplog".equals(dataKey)) {
				valStr = (String)valObj;
				cnt = 0;
				try {
					JSONObject json = JSONObject.fromObject(valObj);
					JSONArray jsonArray = json.getJSONArray("data");
					cnt = jsonArray.size();
				} catch (Exception e) {}
				
			} else if (valObj instanceof Document) {
				valStr = ((Document)valObj).toJson();
				List<Document> list = (	List<Document>)((Document)valObj).get("data");
				if(list != null) {
					cnt = list.size();
				}
			} else {
			  if(valObj != null){
			    valStr = valObj.toString();
			  }
				cnt = 1;
			}
			length = valStr.length();
			valStr = StringUtils.replace(valStr, " ", "");
			valStr = StringUtils.replace(valStr, "},\"data\":[{", "},\r\n\t\"data\":[{");
			valStr = StringUtils.replace(valStr, "},{", " }\r\n\t\t,{");
			
			buf.append("[<B>").append(dataKey).append("</B>]:[<pre style='font-size: 16px; margin: 0px 0px;'>").append(valStr).append("</pre>]-(").append(cnt).append(" 개 - ").append(length).append(" Bytes)<BR>\r\n");
		}

		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("infoTag", buf.toString());
		resMap.put("dataKeys", dataKeys);
		resMap.put("key", key);
		return resMap;
	}

	/** 객체 생성 차단 **/
	private ConsumerInclinationsMDao() {}
}
