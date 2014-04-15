package com.zhangbin.conpool;

/**
 * 链接代理钩子的默认实现
 * @author zhangbinalan
 *
 */
public class DefaultConnectionHook implements ConnectionHook {

	@Override
	public void onCheckIn(ConnectionHandle connection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckOut(ConnectionHandle connection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(ConnectionHandle connection, Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClosed(ConnectionHandle connection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreated(ConnectionHandle connection) {
		// TODO Auto-generated method stub
		
	}

}
