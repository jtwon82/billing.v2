package com.mobon.billing.report.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.mobon.billing.report.AppReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.service.sdk.gather.cat.shoplog.CatToShopLogBatch;
import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.disk.ReadADID;
import com.mobon.billing.report.service.dao.PackageDao;
import com.mobon.dao.mongo.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Configuration
@Service
public class MongoShopLog_7Task extends MongoShopLogTask {
	
	@Autowired
	private PackageDao packageDao;
	
	@Autowired
	private MongoClient mongoClient;
	
	CatToShopLogBatch batch = new CatToShopLogBatch();
	
	ReadADID read = new ReadADID();
	
	private static final Logger logger = LoggerFactory.getLogger(AppReport.class);
	
	public static String PARTITION = "../packaging/data";
	
	public static String[] DIR = new String[] {"7"};
//	public static String[] DIR = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
//	@Scheduled(cron="0 0 04 * * ?") 
	public void execute() {
		
		try {
			while(PackageKeyTask.START_SHOP_LOG) {
//			while(true) {
//				System.out.println("test");
				Thread.sleep(1000);
//				PackageKeyTask.START_SHOP_LOG = true;
			}
			logger.info("disk scan start.....");
			System.out.println(Thread.currentThread().getName());
			for(int i=0; i<DIR.length; i++) {
				read.read(MongoShopLog_7Task.PARTITION+"/"+DIR[i], new ReadADID.IADID() {
					@Override
					public void execute(JSONObject adid) {
						// TODO Auto-generated method stub
						try {
							addShopLog(adid);
							Thread.sleep(200);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error(e.getMessage(), e);
						}
					}
				});
			}
			logger.info(Thread.currentThread().getName()+"-disk scan end.....");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
	
}
