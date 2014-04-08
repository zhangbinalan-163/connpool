package com.zhangbin.conpool.common.exception;

import java.sql.SQLException;

public class ConnectionClosedException extends SQLException {

	private static final long serialVersionUID = -8512382998454532335L;

	public ConnectionClosedException() {
		super("connection has been closed logical");
	}
}
