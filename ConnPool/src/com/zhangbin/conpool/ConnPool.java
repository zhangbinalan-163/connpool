package com.zhangbin.conpool;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import com.zhangbin.conpool.config.PoolConfig;

public class ConnPool {
	//����
	private PoolConfig config;
	//���ӳ�ά���߳�ִ����
	private ExecutorService executorService;
	
	public ConnPool(PoolConfig config) {
		this.config = config;
	}
	/**
	 * �ͷ����ӣ��Żص����ӳ�
	 * @param connection
	 * @throws SQLException
	 */
	public void releaseConnection(ConnectionHandle connection) throws SQLException{
		
	}
	/**
	 * ������ӣ�����ʹ��
	 * @param connection
	 * @throws SQLException
	 */
	public void destroyConnection(ConnectionHandle connection) throws SQLException{
		
	}
	
}
