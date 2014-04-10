package com.zhangbin.connpool.utils;

import java.util.UUID;

public class ConnectionIdGenerator {

	public static void main(String[] args) {
		for(int i=0;i<2;i++){
			UUID uuid = UUID.randomUUID();
			System.out.println(uuid);
		}
	}
}
