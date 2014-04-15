package com.zhangbin.conpool.config;

import static com.zhangbin.conpool.common.Constants.*;

public class PoolConfig {
	// 最大连接数
	private int maxCon = DEFAULT_MAXCON;
	// 最小连接数
	private int minCon = DEFAULT_MINCON;
	// 初始化时初始连接数
	private int initCon = DEFAULT_INITCON;
	// 增加链接时单位增加量
	private int incrementNum = DEFAULT_INCREMENT;
	// x%
	private int poolThreshold = DEFAULT_POOLTHRESHOLD;
	// 获取链接超时时间限制
	private long getConnectionTimeThreshold = DEFAULT_CONGET_TIMEOUT;
	// 链接测试sql语句
	private String keepAliveSql;
	// keepalive线程执行时间间隔，毫秒
	private long keepAliveTimeInterval = DEFAULT_INTERVAL_KEEPALIVE;
	// 监控最大生存时间线程执行时间间隔，毫秒
	private long maxAgeTimeInterval = DEFAULT_INTERVAL_MAXAGE;
	// 监控连接数量的线程执行时间间隔，毫秒
	private long poolNumberTimeInterval = DEFAULT_INTERVAL_POOLNUM;
	// 新建连接时是否需要检查链接有效性
	private boolean sendInitialSQLOnCreate = DEFAULT_CHECK_ONCREATE;
	// 数据库驱动
	private String driver;
	// 数据url
	private String url;
	// 数据库登录名
	private String username;
	// 登录密码
	private String password;

	// 关闭连接池是是否卸载驱动
	private boolean unregisterDriverOnShutdown = DEFAULT_UNREG_DRIVER;

	public boolean isUnregisterDriverOnShutdown() {
		return unregisterDriverOnShutdown;
	}

	public void setUnregisterDriverOnShutdown(boolean unregisterDriverOnShutdown) {
		this.unregisterDriverOnShutdown = unregisterDriverOnShutdown;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public long getKeepAliveTimeInterval() {
		return keepAliveTimeInterval;
	}

	public void setKeepAliveTimeInterval(long keepAliveTimeInterval) {
		this.keepAliveTimeInterval = keepAliveTimeInterval;
	}

	public long getMaxAgeTimeInterval() {
		return maxAgeTimeInterval;
	}

	public void setMaxAgeTimeInterval(long maxAgeTimeInterval) {
		this.maxAgeTimeInterval = maxAgeTimeInterval;
	}

	public long getPoolNumberTimeInterval() {
		return poolNumberTimeInterval;
	}

	public void setPoolNumberTimeInterval(long poolNumberTimeInterval) {
		this.poolNumberTimeInterval = poolNumberTimeInterval;
	}

	public String getKeepAliveSql() {
		return keepAliveSql;
	}

	public void setKeepAliveSql(String keepAliveSql) {
		this.keepAliveSql = keepAliveSql;
	}

	public int getMaxCon() {
		return maxCon;
	}

	public void setMaxCon(int maxCon) {
		this.maxCon = maxCon;
	}

	public int getMinCon() {
		return minCon;
	}

	public void setMinCon(int minCon) {
		this.minCon = minCon;
	}

	public int getInitCon() {
		return initCon;
	}

	public void setInitCon(int initCon) {
		this.initCon = initCon;
	}

	public int getIncrementNum() {
		return incrementNum;
	}

	public void setIncrementNum(int incrementNum) {
		this.incrementNum = incrementNum;
	}

	public int getPoolThreshold() {
		return poolThreshold;
	}

	public void setPoolThreshold(int poolThreshold) {
		this.poolThreshold = poolThreshold;
	}

	public long getGetConnectionTimeThreshold() {
		return getConnectionTimeThreshold;
	}

	public void setGetConnectionTimeThreshold(long getConnectionTimeThreshold) {
		this.getConnectionTimeThreshold = getConnectionTimeThreshold;
	}

	public boolean isSendInitialSQLOnCreate() {
		return sendInitialSQLOnCreate;
	}

	public void setSendInitialSQLOnCreate(boolean sendInitialSQLOnCreate) {
		this.sendInitialSQLOnCreate = sendInitialSQLOnCreate;
	}

	@Override
	public String toString() {
		return "PoolConfig [maxCon=" + maxCon + ", minCon=" + minCon
				+ ", initCon=" + initCon + ", incrementNum=" + incrementNum
				+ ", poolThreshold=" + poolThreshold
				+ ", getConnectionTimeThreshold=" + getConnectionTimeThreshold
				+ ", keepAliveSql=" + keepAliveSql + ", keepAliveTimeInterval="
				+ keepAliveTimeInterval + ", maxAgeTimeInterval="
				+ maxAgeTimeInterval + ", poolNumberTimeInterval="
				+ poolNumberTimeInterval + ", sendInitialSQLOnCreate="
				+ sendInitialSQLOnCreate + ", driver=" + driver + ", url="
				+ url + ", username=" + username + ", password=" + password
				+ ", unregisterDriverOnShutdown=" + unregisterDriverOnShutdown
				+ "]";
	}
}
