package com.zhangbin.connpool.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test2 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://121.199.58.223:3306/sms";
		String pass = "root";
		String user = "root";
		Connection conn = null;
		ResultSet rs = null;
		try {
			DriverManager.registerDriver(DriverManager.getDriver(url));
			
			conn = DriverManager.getConnection(url, user, pass);
			long start=System.currentTimeMillis();
			
			rs = conn.getMetaData().getTables(null, null, "notexsist",
					new String[] { "TABLE" });
			System.out.println("sucess,time="+(System.currentTimeMillis()-start));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// e.printStackTrace();
				}
			}
			try {
				DriverManager.deregisterDriver(DriverManager.getDriver(url));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
