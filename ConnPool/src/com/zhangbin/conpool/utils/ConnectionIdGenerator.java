package com.zhangbin.conpool.utils;

import java.util.UUID;

/**
 * ����������ӵ�ID
 * @author zhangbinalan
 *
 */
public class ConnectionIdGenerator {

	public static String getId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
