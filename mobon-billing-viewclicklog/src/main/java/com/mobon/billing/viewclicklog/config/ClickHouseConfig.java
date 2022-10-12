package com.mobon.billing.viewclicklog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.yandex.clickhouse.BalancedClickhouseDataSource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

@Configuration
@PropertySource(value = "classpath:clickhouse.properties", ignoreResourceNotFound = true)
@EnableTransactionManagement
public class ClickHouseConfig {
	
	@Autowired
	private ClickHouseSetting clickHouseSetting;
	
	@Bean
	public DataSource dataSourceClickhouse() {
		
		DataSource datasource = DataSourceBuilder.create().build();
		
		if (clickHouseSetting.isValid()) {
			Properties properties = new Properties();			
			properties.setProperty("user", clickHouseSetting.getUsername());
			properties.setProperty("password", clickHouseSetting.getPassword());
			datasource = null;
			datasource = new BalancedClickhouseDataSource(clickHouseSetting.getJdbcUrl(), properties);			
		}
		
		return datasource;
	}
	
	@Bean(name="sqlSessionFactoryClickhouse")
	public SqlSessionFactory sqlSessionFactoryClickhouse() throws Exception {		
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
	public SqlSessionTemplate sqlSessionTemplateClickhouse() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryClickhouse(), ExecutorType.SIMPLE);
	}

}
