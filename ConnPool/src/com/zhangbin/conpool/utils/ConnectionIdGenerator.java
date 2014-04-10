package com.zhangbin.conpool.utils;

import java.util.UUID;

/**
 * 随机产生链接的ID
 * @author zhangbinalan
 *
 */
public class ConnectionIdGenerator {

	public static String getId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
