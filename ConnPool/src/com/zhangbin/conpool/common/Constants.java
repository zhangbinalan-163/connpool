package com.zhangbin.conpool.common;

/**
 * ����
 * 
 * @author zhangbinalan
 * 
 */
public class Constants {
	// ����̵߳�ִ����������е��߳���Ŀ
	public static int THREAD_NUM = 3;
	// ����߳������ӳس�ʼ�����ӳٶ��ٺ��뿪ʼִ��
	public static long WATCHTREAD_DELAY = 5 * 1000L;
	// Ĭ�����ӳص�����
	public static String POOLNAME_DEFAULT = "default";

	// Ĭ�����������
	public static int DEFAULT_MAXCON = 10;
	// Ĭ����С������
	public static int DEFAULT_MINCON = 2;
	// Ĭ�ϳ�ʼ������
	public static int DEFAULT_INITCON = 2;
	// Ĭ��һ��������������
	public static int DEFAULT_INCREMENT = 2;
	// Ĭ����Ҫ���� �ı���
	public static int DEFAULT_POOLTHRESHOLD = 75;
	// ��ȡ���ӳ�ʱʱ������
	public static long DEFAULT_CONGET_TIMEOUT = 10 * 1000L;
	// keepalive�߳�ִ��ʱ����������
	public static long DEFAULT_INTERVAL_KEEPALIVE = 5 * 1000L;
	// ����������ʱ���߳�ִ��ʱ����������
	public static long DEFAULT_INTERVAL_MAXAGE = 5 * 1000L;
	// ��������������߳�ִ��ʱ����������
	public static long DEFAULT_INTERVAL_POOLNUM = 5 * 1000L;
	// Ĭ�Ϲر����ӳ�ʱж������
	public static boolean DEFAULT_UNREG_DRIVER = false;
	// �½�����ʱĬ�ϼ��������Ч��
	public static boolean DEFAULT_CHECK_ONCREATE = true;
}
