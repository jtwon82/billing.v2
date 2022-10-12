package com.mobon.billing.frequency.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Datasource 설정
 * 
 * @author ljs
 * @since 0.1
 */
@Configuration
@MapperScan(value="com.mobon.billing.frequency.mapper.dreamsearch", sqlSessionFactoryRef="dreamsearchSqlSessionFactory")
@EnableTransactionManagement
public class DreamsearchDatabaseConfig {

	@Bean(name= "dreamsearchDataSource")
	@ConfigurationProperties(prefix = "spring.dreamsearch.datasource")
	public DataSource dreamsearchDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "dreamsearchSqlSessionFactory")
	public SqlSessionFactory dreamsearchSqlSessionFactory ( @Qualifier("dreamsearchDataSource") DataSource dreamsearchDataSource, ApplicationContext  applicationContent) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dreamsearchDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContent.getResources("classpath:conf/db/maria/dreamsearch/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "dreamsearchSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate dreamsearchSqlSessionTemplate(SqlSessionFactory dreamsearchSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(dreamsearchSqlSessionFactory);
	}
	
}
