package com.openrtb.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.openrtb.db.billing", sqlSessionFactoryRef = "billingSqlSessionFactory")
@EnableTransactionManagement
public class BillingDbConfig {
	@Bean(name = "billingDataSource")
	@ConfigurationProperties(prefix = "spring.billing.datasource")
	public DataSource db2DataSource() throws Exception {
		HikariDataSource hikariDataSource = new HikariDataSource();

		HikariConfig config = new HikariCpProperties().hikariCommonConfig(); // 공통 설정
		hikariDataSource.setPoolName(config.getPoolName());
		hikariDataSource.setMinimumIdle(config.getMinimumIdle());
		hikariDataSource.setMaximumPoolSize(config.getMaximumPoolSize());
		hikariDataSource.setIdleTimeout(config.getIdleTimeout());
		hikariDataSource.setMaxLifetime(config.getMaxLifetime());
		hikariDataSource.setConnectionTimeout(config.getConnectionTimeout());

		return hikariDataSource;
	}

	@Bean(name = "billingSqlSessionFactory")
	public SqlSessionFactory billingSqlSessionFactory(@Qualifier("billingDataSource") DataSource billingDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(billingDataSource);
		sqlSessionFactoryBean
				.setMapperLocations(applicationContext.getResources("classpath*:com/openrtb/db/billing/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "billingSqlSessionTemplate")
	public SqlSessionTemplate billingSqlSessionTemplate(SqlSessionFactory billingSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(billingSqlSessionFactory);
	}

}
