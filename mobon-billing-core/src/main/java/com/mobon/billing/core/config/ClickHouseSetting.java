package com.mobon.billing.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.clickhouse")
public class ClickHouseSetting {
	
	private String jdbcUrl;
	private String username;
	private String password;	
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
     * isValid
     * 프로퍼티 사용가능 여부 확인 메소드
     * 
     * @author  : sjpark3
     * @since   : 2021-12-28
     */
	public boolean isValid() {
		return jdbcUrl != null && username != null && password != null;
	}

}
