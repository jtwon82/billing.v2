package org.mobon.billing.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import com.adgather.resource.loader.PropertyLoader;
import com.mobon.billing.report.schedule.MongoShopLogTask;
import com.mobon.dao.mongo.MongoManager;

@SpringBootApplication
@ImportResource({ "classpath:batch-config_mongo.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = "com.mobon.billing")

/**
 * db.getCollection('CIData_00').find({_id:'9ea3cee6c316c159-418a27f4163a5f8fdcf-7fa4'})
 * @author Administrator
 *
 */
public class AppReportMongo {
	private static final Logger logger = LoggerFactory.getLogger(AppReportMongo.class);

	public static void main(String[] args) {
		logger.debug("AppReport");
		if(args.length > 0) {
			MongoShopLogTask.PARTITION = args[0];
		}
		PropertyLoader.getInstance().loadProperties(new String[]{"./mongo_mobon.properties"});
		PropertyLoader.getInstance().loadProperties(new String[]{"./cookie.properties"});
//		PropertyLoader.getInstance().loadProperties(new String[]{"C:/workset/workspace_consumer/mobon-billing-v1/mobon-billing-resource/src/main/resources-realSDK/cookie.properties"});
		MongoManager.load();
		new SpringApplicationBuilder().sources(AppReportMongo.class).run(args);
	}
	
	
}
