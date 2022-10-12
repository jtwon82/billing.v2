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
 * Datasource 설정(billing)
 * 
 * @author ljs
 * @since 0.1
 */
@Configuration
@MapperScan(value="com.mobon.billing.frequency.mapper.billing", sqlSessionFactoryRef="billingSqlSessionFactory")
@EnableTransactionManagement
public class BillingDatabaseConfig {

	@Bean(name= "billingDataSource")
	@ConfigurationProperties(prefix = "spring.billing.datasource")
	@Primary
	public DataSource billingDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "billingSqlSessionFactory")
	@Primary
	public SqlSessionFactory billingSqlSessionFactory ( @Qualifier("billingDataSource") DataSource billingDataSource, ApplicationContext  applicationContent) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(billingDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContent.getResources("classpath:conf/db/maria/billing/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "billingSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate billingSqlSessionTemplate(SqlSessionFactory billingSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(billingSqlSessionFactory);
	}
	
}
