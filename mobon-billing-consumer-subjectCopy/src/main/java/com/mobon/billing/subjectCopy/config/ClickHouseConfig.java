package com.mobon.billing.subjectCopy.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import ru.yandex.clickhouse.BalancedClickhouseDataSource;

@Configuration
@PropertySource("classpath:clickhouse.properties")
@EnableTransactionManagement
public class ClickHouseConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ClickHouseConfig.class);

	@Autowired
	private ClickHouseSetting clickHouseSetting;
	
	@Bean
//	@Primary
	public DataSource dataSourceClickhouse() {
		//DataSource datasource = DataSourceBuilder.create().build();
		//logger.info(url);
		Properties properties = new Properties();
		properties.setProperty("user", clickHouseSetting.getUsername());
		properties.setProperty("password", clickHouseSetting.getPassword());

		DataSource datasource = new BalancedClickhouseDataSource(clickHouseSetting.getJdbcUrl(), properties);
		return datasource;
	}

	@Bean(name="sqlSessionFactoryClickhouse")
//	@Primary
	public SqlSessionFactory sqlSessionFactoryClickhouse() throws Exception {
		//DataSource dataSource = dataSourceClickhouse();
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSourceClickhouse());
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
	
	@Bean(name = "sqlSessionTemplateClickhouse", destroyMethod = "clearCache")
//	@Primary
	public SqlSessionTemplate sqlSessionTemplateClickhouse() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryClickhouse(), ExecutorType.SIMPLE);
	}
}
