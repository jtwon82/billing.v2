package com.mobon.billing.branchUM.config;

import com.mobon.billing.branchUM.config.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@PropertySource({ "classpath:mongo.properties" })
public class MongoConfig {
	@Value("${spring.data.mongodb.host}")
	private String host;
	@Value("${spring.data.mongodb.database}")
	private String dbName;
	@Value("${spring.data.mongodb.port}")
	private int port;
	@Value("${spring.data.mongodb.username}")
	private String userName;
	@Value("${spring.data.mongodb.password}")
	private String passWord;
	@Value("#{'${spring.data.mongodb.seeds}'.split(',')}")
	private List<ServerAddress> seeds;

	private MongoClientOptions getMongoClientOption() {
		return MongoClientOptions.builder()

				.maxConnectionLifeTime(3600000)

				.build();
	}

	private MongoCredential getUserCredentials() {
		return MongoCredential.createCredential(this.userName, this.dbName, this.passWord.toCharArray());
	}

	private ServerAddress getServerAddress() {
		return new ServerAddress(this.host, this.port);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoDbFactory());
	}

	@Bean
	public MongoClient mongoClient() {
		return new MongoClient(this.host, this.port);
	}

	private MongoDbFactory mongoDbFactory() {
		return (MongoDbFactory) new SimpleMongoDbFactory(mongoClient(), this.dbName);
	}
}
