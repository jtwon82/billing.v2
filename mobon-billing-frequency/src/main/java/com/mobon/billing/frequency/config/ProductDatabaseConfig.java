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
@MapperScan(value="com.mobon.billing.frequency.mapper.product", sqlSessionFactoryRef="productSqlSessionFactory")
@EnableTransactionManagement
public class ProductDatabaseConfig {

	@Bean(name= "productDataSource")
	@ConfigurationProperties(prefix = "spring.dreamsearch.product.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "productSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory ( @Qualifier("productDataSource") DataSource productDataSource, ApplicationContext  applicationContent) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource( productDataSource );
		sqlSessionFactoryBean.setMapperLocations( applicationContent.getResources("classpath:conf/db/maria/product/*.xml") );
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "productSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate( SqlSessionFactory productSqlSessionFactory ) throws Exception {
		return new SqlSessionTemplate( productSqlSessionFactory );
	}
	
}
