package com.lijian.dispro.quartz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceParams {

	private int timeBetweenEvictionRunsMillis;// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，如果小于等于0，不会启动检查线程，单位是毫秒
	private int minEvictableIdleTimeMillis;// 连接在池中保持空闲而不被空闲连接回收器线程
	private int maxActive;

	private String validationQuery;
	private boolean testOnBorrow;// 是否在从池中取出连接前进行检验，如果检验失败，则从池中去除连接并尝试取出另一个.默认为false
	private boolean testWhileIdle;// 空闲时是否进行验证，检查对象是否有效，默认为false
	private boolean testOnConnect;
	private int maxIdle;// 最大空闲数，数据库连接的最大空闲时间。超过空闲时间，数据库连接将被标记为不可用，然后被释放。设为0表示无限制。
	private int maxWait;// 最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示 无限制。单位毫秒
	private int minIdle;// 最小空闲连接数
	private int initialSize;// 初始化连接数量

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public boolean isTestOnConnect() {
		return testOnConnect;
	}

	public void setTestOnConnect(boolean testOnConnect) {
		this.testOnConnect = testOnConnect;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

}
