package com.zhangbin.conpool.config;

public class PoolConfig {
	// 最大连接数
	private int maxCon;
	// 最小连接数
	private int minCon;
	// 初始化时初始连接数
	private int initCon;
	// 增加链接时单位增加量
	private int incrementNum;
	// x% 
	private int poolThreshold;
	
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
}
