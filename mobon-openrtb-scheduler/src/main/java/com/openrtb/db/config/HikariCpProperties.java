package com.openrtb.db.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

@Configuration
public class HikariCpProperties {
	@Bean
	public HikariConfig hikariCommonConfig() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		Resource resource = resolver.getResource("classpath:hikaricp.properties");
		Properties loadProperties = PropertiesLoaderUtils.loadProperties(resource);
		HikariConfig hikariConfig = new HikariConfig(loadProperties);

		return hikariConfig;
	}

}
