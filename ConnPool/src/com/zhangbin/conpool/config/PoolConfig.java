package com.zhangbin.conpool.config;

public class PoolConfig {
	// ���������
	private int maxCon;
	// ��С������
	private int minCon;
	// ��ʼ��ʱ��ʼ������
	private int initCon;
	// ��������ʱ��λ������
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
