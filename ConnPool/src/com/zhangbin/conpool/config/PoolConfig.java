package com.zhangbin.conpool.config;

import static com.zhangbin.conpool.common.Constants.*;

public class PoolConfig {
	// ���������
	private int maxCon = DEFAULT_MAXCON;
	// ��С������
	private int minCon = DEFAULT_MINCON;
	// ��ʼ��ʱ��ʼ������
	private int initCon = DEFAULT_INITCON;
	// ��������ʱ��λ������
	private int incrementNum = DEFAULT_INCREMENT;
	// x%
	private int poolThreshold = DEFAULT_POOLTHRESHOLD;
	// ��ȡ���ӳ�ʱʱ������
	private long getConnectionTimeThreshold = DEFAULT_CONGET_TIMEOUT;
	// ���Ӳ���sql���
	private String keepAliveSql;
	// keepalive�߳�ִ��ʱ����������
	private long keepAliveTimeInterval = DEFAULT_INTERVAL_KEEPALIVE;
	// ����������ʱ���߳�ִ��ʱ����������
	private long maxAgeTimeInterval = DEFAULT_INTERVAL_MAXAGE;
	// ��������������߳�ִ��ʱ����������
	private long poolNumberTimeInterval = DEFAULT_INTERVAL_POOLNUM;
	// �½�����ʱ�Ƿ���Ҫ���������Ч��
	private boolean sendInitialSQLOnCreate = DEFAULT_CHECK_ONCREATE;
	// ���ݿ�����
	private String driver;
	// ����url
	private String url;
	// ���ݿ��¼��
	private String username;
	// ��¼����
	private String password;

	// �ر����ӳ����Ƿ�ж������
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
