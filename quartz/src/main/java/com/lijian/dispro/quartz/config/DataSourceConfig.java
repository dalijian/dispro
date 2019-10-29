package com.lijian.dispro.quartz.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


	@Resource
	private DataSourceParams dataSourceParams;
	@Resource
	private DisproDatabase disproDatabase;

	@Primary
	@Bean(name = "disproDataSource")
	@Qualifier("disproDataSource")
	public DataSource disproDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(disproDatabase.getDriverClassName());
		dataSource.setUrl(disproDatabase.getUrl());
		dataSource.setUsername(disproDatabase.getUsername());
		dataSource.setPassword(disproDatabase.getPassword());
		// dataSource.setPoolProperties(poolConfiguration());
//		return dataSource;
		return setConnection(dataSource);
	}


	@Bean(name = "disproJdbcTemplate")
	public JdbcTemplate fireJdbcTemplate(@Qualifier("disproDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(value = "transTemplate")
	public TransactionTemplate getTemplateTransaction(@Qualifier("disproDataSource") DataSource dataSource){

		TransactionTemplate transTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
		return  transTemplate;
	}


private DataSource setConnection(DruidDataSource dataSource) {
	dataSource.setMaxActive(dataSourceParams.getMaxActive());
	dataSource.setMaxWait(dataSourceParams.getMaxWait());
	dataSource.setMinIdle(dataSourceParams.getMinIdle());
	dataSource.setInitialSize(dataSourceParams.getInitialSize());
	dataSource.setValidationQuery(dataSourceParams.getValidationQuery());
	dataSource.setTestOnBorrow(dataSourceParams.isTestOnBorrow());
	dataSource.setTestWhileIdle(dataSourceParams.isTestWhileIdle());
	dataSource.setTimeBetweenEvictionRunsMillis(dataSourceParams.getTimeBetweenEvictionRunsMillis());

//	dataSource.setTestOnConnect(dataSourceParams.isTestOnConnect());
	dataSource.setTimeBetweenEvictionRunsMillis(dataSourceParams.getTimeBetweenEvictionRunsMillis());
	dataSource.setMinEvictableIdleTimeMillis(dataSourceParams.getMinEvictableIdleTimeMillis());

	return dataSource;
}

}
