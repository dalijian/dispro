package com.lijian.dispro.quartz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.dispro")
public class DisproDatabase {
	private String username;
	private String password;
	private String driverClassName;
	private String url;

	public DisproDatabase() {
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

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DisproDatabase(String username, String password, String driverClassName, String url) {
		this.username = username;
		this.password = password;
		this.driverClassName = driverClassName;
		this.url = url;
	}
}
