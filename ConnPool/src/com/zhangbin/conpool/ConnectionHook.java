package com.zhangbin.conpool;

public interface ConnectionHook {
	void onCheckIn(ConnectionHandle connection);
	void onCheckOut(ConnectionHandle connection);
	void onException(ConnectionHandle connection,Exception e);
	void onClosed(ConnectionHandle connection);
}
