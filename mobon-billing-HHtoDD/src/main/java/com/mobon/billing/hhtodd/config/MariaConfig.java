package com.mobon.billing.hhtodd.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:hikari.properties")
@EnableTransactionManagement
public class MariaConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MariaConfig.class);

	@Bean (destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource.billing")
	@Primary
	public DataSource dataSourceBilling() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}
	@Bean (destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource.dream")
	public DataSource dataSourceDream() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	
	}
	@Bean (destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource.dream-shopdata")
	public DataSource dataSourceDreamShopData() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}
//	@Bean (destroyMethod = "close")
//	@ConfigurationProperties(prefix = "spring.datasource.dream-shopdata")
//	public DataSource dataSourceDreamShopData() {
//		DataSource result = DataSourceBuilder.create().build();
//		return result;
//	}
//	@Bean (destroyMethod = "close")
//	@ConfigurationProperties(prefix = "spring.datasource.dream-action-select")
//	public DataSource dataSourceDreamActionSelect() {
//		DataSource result = DataSourceBuilder.create().build();
//		return result;
//	}
	
	
	
	
	
	
	@Bean(name="sqlSessionFactoryBilling")
	@Primary
	public SqlSessionFactory sqlSessionFactoryBilling() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSourceBilling());
		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));

		// default TypeHandler 등록.
		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
		typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);

		return sqlSessionFactory.getObject();
	}
	@Bean(name="sqlSessionFactoryDream")
	public SqlSessionFactory sqlSessionFactoryDream() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSourceDream());
		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));

		// default TypeHandler 등록.
		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
		typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);

		return sqlSessionFactory.getObject();
	}
	@Bean(name="sqlSessionFactoryDreamShopData")
	public SqlSessionFactory sqlSessionFactoryDreamShopData() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSourceDreamShopData());
		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));
		
		// default TypeHandler 등록.
		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
		typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);
		
		return sqlSessionFactory.getObject();
	}
//	@Bean(name="sqlSessionFactoryDreamShopData")
//	public SqlSessionFactory sqlSessionFactoryDreamShopData() throws Exception {
//		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
//		sqlSessionFactory.setDataSource(dataSourceDreamShopData());
//		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
//		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));
//		
//		// default TypeHandler 등록.
//		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
//		typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
//		typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
//		typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);
//		
//		return sqlSessionFactory.getObject();
//	}
//	@Bean(name="sqlSessionFactoryDreamActionSelect")
//	public SqlSessionFactory sqlSessionFactoryDreamActionSelect() throws Exception {
//		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
//		sqlSessionFactory.setDataSource(dataSourceDreamActionSelect());
//		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
//		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));
//
//		// default TypeHandler 등록.
//		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
//		typeHandlerRegistry.register(java.sql.Timestamp.class, org.apache.ibatis.type.DateTypeHandler.class);
//		typeHandlerRegistry.register(java.sql.Time.class, org.apache.ibatis.type.DateTypeHandler.class);
//		typeHandlerRegistry.register(java.sql.Date.class, org.apache.ibatis.type.DateTypeHandler.class);
//
//		return sqlSessionFactory.getObject();
//	}

	
	
	
	
	
	@Bean(name = "sqlSessionTemplateBilling", destroyMethod = "clearCache")
	@Primary
	public SqlSessionTemplate sqlSessionTemplateBilling() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryBilling(), ExecutorType.BATCH);
	}
	@Bean(name = "sqlSessionTemplateDream", destroyMethod = "clearCache")
	public SqlSessionTemplate sqlSessionTemplateDream() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryDream(), ExecutorType.BATCH);
	}
	@Bean(name = "sqlSessionTemplateDreamShopData", destroyMethod = "clearCache")
	public SqlSessionTemplate sqlSessionTemplateDreamShopData() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryDreamShopData(), ExecutorType.SIMPLE);
	}
	
//	@Bean(name = "sqlSessionTemplateDreamShopData", destroyMethod = "clearCache")
//	public SqlSessionTemplate sqlSessionTemplateDreamShopData() throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactoryDreamShopData(), ExecutorType.SIMPLE);
//	}
//	@Bean(name = "sqlSessionTemplateDreamSimpleActionSelect", destroyMethod = "clearCache")
//	public SqlSessionTemplate sqlSessionTemplateDreamSimpleActionSelect() throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactoryDreamActionSelect(), ExecutorType.SIMPLE);
//	}

}
