package com.lijian.dispro.quartz.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

@Configuration
public class DataSourceConfig {


	@Resource
	private DataSourceParams dataSourceParams;
	@Resource
	private DisproDatabase disproDatabase;


	/**
	 * 电子档案数据源
	 */
	@Primary
	@Bean(name = "disproDataSource")
	@Qualifier("disproDataSource")
	public DataSource disproDataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(disproDatabase.getDriverClassName());
		dataSource.setUrl(disproDatabase.getUrl());
		dataSource.setUsername(disproDatabase.getUsername());
		dataSource.setPassword(disproDatabase.getPassword());
		// dataSource.setPoolProperties(poolConfiguration());
		return dataSource;
//		return setConnection(dataSource);
	}


	@Bean(name = "disproJdbcTemplate")
	public JdbcTemplate fireJdbcTemplate(@Qualifier("disproDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}




private DataSource setConnection(DataSource dataSource) {
	dataSource.setMaxIdle(dataSourceParams.getMaxIdle());
	dataSource.setMaxWait(dataSourceParams.getMaxWait());
	dataSource.setMinIdle(dataSourceParams.getMinIdle());
	dataSource.setInitialSize(dataSourceParams.getInitialSize());
	dataSource.setValidationQuery(dataSourceParams.getValidationQuery());
	dataSource.setTestOnBorrow(dataSourceParams.isTestOnBorrow());
	dataSource.setTestWhileIdle(dataSourceParams.isTestWhileIdle());
	dataSource.setTestOnConnect(dataSourceParams.isTestOnConnect());
	dataSource.setTimeBetweenEvictionRunsMillis(dataSourceParams.getTimeBetweenEvictionRunsMillis());
	dataSource.setMinEvictableIdleTimeMillis(dataSourceParams.getMinEvictableIdleTimeMillis());

	return dataSource;
}

}
