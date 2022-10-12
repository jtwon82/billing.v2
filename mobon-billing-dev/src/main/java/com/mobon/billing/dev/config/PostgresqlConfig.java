package com.mobon.billing.dev.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:hikari.properties")
@EnableTransactionManagement
public class PostgresqlConfig {

	@Bean (destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.datasource.postgresql")
	public DataSource dataSourcePostgresql() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}
	@Bean(name="sqlSessionFactoryPostgres")
	public SqlSessionFactory sqlSessionFactoryPostgres() throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSourcePostgresql());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean(name = "sqlSessionTemplatePostgres")
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactoryPostgres());
		return sqlSessionTemplate;
	}
}
