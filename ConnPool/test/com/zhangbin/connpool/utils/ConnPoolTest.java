package com.zhangbin.connpool.utils;

import java.sql.SQLException;

import com.zhangbin.conpool.ConnPool;
import com.zhangbin.conpool.config.PoolConfig;

public class ConnPoolTest {

	public static void main(String[] args) {
		PoolConfig config=new PoolConfig();
		config.setDriver("com.mysql.jdbc.Driver");
		config.setUrl("jdbc:mysql://127.0.0.1:3306/club?useUnicode=true&amp;characterEncoding=utf8");
		config.setUsername("root");
		config.setPassword("zhangbin");
		config.setUnregisterDriverOnShutdown(true);
		config.setSendInitialSQLOnCreate(true);
		config.setInitCon(4);
		
		ConnPool pool =new ConnPool(config);
		try {
			pool.init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(20*1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pool.shutdown();
	}
}
