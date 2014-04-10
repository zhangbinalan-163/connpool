package com.zhangbin.conpool;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import com.zhangbin.conpool.config.PoolConfig;

public class ConnPool {
	//配置
	private PoolConfig config;
	//连接池维护线程执行器
	private ExecutorService executorService;
	
	public ConnPool(PoolConfig config) {
		this.config = config;
	}
	/**
	 * 释放链接，放回到连接池
	 * @param connection
	 * @throws SQLException
	 */
	public void releaseConnection(ConnectionHandle connection) throws SQLException{
		
	}
	/**
	 * 清除链接，不再使用
	 * @param connection
	 * @throws SQLException
	 */
	public void destroyConnection(ConnectionHandle connection) throws SQLException{
		
	}
	
}
