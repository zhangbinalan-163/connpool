package com.zhangbin.conpool.common;

/**
 * 常量
 * 
 * @author zhangbinalan
 * 
 */
public class Constants {
	// 监控线程的执行器最多运行的线程数目
	public static int THREAD_NUM = 3;
	// 监控线程在连接池初始化后延迟多少毫秒开始执行
	public static long WATCHTREAD_DELAY = 5 * 1000L;
	// 默认连接池的名字
	public static String POOLNAME_DEFAULT = "default";

	// 默认最大连接数
	public static int DEFAULT_MAXCON = 10;
	// 默认最小连接数
	public static int DEFAULT_MINCON = 2;
	// 默认初始连接数
	public static int DEFAULT_INITCON = 2;
	// 默认一次增加链接数量
	public static int DEFAULT_INCREMENT = 2;
	// 默认需要增加 的比率
	public static int DEFAULT_POOLTHRESHOLD = 75;
	// 获取链接超时时间限制
	public static long DEFAULT_CONGET_TIMEOUT = 10 * 1000L;
	// keepalive线程执行时间间隔，毫秒
	public static long DEFAULT_INTERVAL_KEEPALIVE = 5 * 1000L;
	// 监控最大生存时间线程执行时间间隔，毫秒
	public static long DEFAULT_INTERVAL_MAXAGE = 5 * 1000L;
	// 监控连接数量的线程执行时间间隔，毫秒
	public static long DEFAULT_INTERVAL_POOLNUM = 5 * 1000L;
	// 默认关闭连接池时卸载驱动
	public static boolean DEFAULT_UNREG_DRIVER = false;
	// 新建链接时默认检查链接有效性
	public static boolean DEFAULT_CHECK_ONCREATE = true;
}
