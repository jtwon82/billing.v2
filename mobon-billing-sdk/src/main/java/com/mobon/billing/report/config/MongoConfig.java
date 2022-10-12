package com.mobon.billing.report.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource("classpath:mongo.properties")
public class MongoConfig {

	@Value("${spring.data.mongodb.host}")
	private String	host;
	@Value("${spring.data.mongodb.database}")
	private String	dbName;
	@Value("${spring.data.mongodb.port}")
	private int		port;
	@Value("${spring.data.mongodb.username}")
	private String	userName;
	@Value("${spring.data.mongodb.password}")
	private String	passWord;
	@Value("#{'${spring.data.mongodb.seeds}'.split(',')}") 
	private List<ServerAddress> seeds;

	/**
	 * MONGO_CONNECTION_PER_HOST=500
MONGO_CONN_IDLE_TIME=300000
MONGO_CONN_LIFE_TIME=86400000
MONGO_CONN_TIMEOUT=1000
MONGO_SOCKET_TIMEOUT=400
MONGO_MAXWAIT_TIMEOUT=400
MONGO_SELECTION_TIMEOUT=300
	 * @return
	 */
    private MongoClientOptions getMongoClientOption() {
        return MongoClientOptions.builder()
                .connectionsPerHost(500)
//                .connectTimeout(5000).maxWaitTime(10000)
                .socketKeepAlive(true)
                .maxConnectionLifeTime(86400000)
                .socketTimeout(400)
                .connectTimeout(1000)
                .serverSelectionTimeout(300)
                .maxWaitTime(400)
                .build();
    }

    private List<MongoCredential> getUserCredentials() {
        return Arrays.asList(MongoCredential.createCredential(userName, dbName, passWord.toCharArray()));
    }

    private ServerAddress getServerAddress() {
        return new ServerAddress(host, port);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
    
    @Bean
    public MongoClient mongoClient(){
//    	return new MongoClient(getServerAddress(), getUserCredentials(), getMongoClientOption());
    	return new MongoClient(seeds, getUserCredentials(), getMongoClientOption());
    }

    private MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), dbName);
    }
}
