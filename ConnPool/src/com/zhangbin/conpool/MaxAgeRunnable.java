package com.zhangbin.conpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ������ʱ������Ŀ�������/��������ʱ�����������
 * 
 * @author zhangbinalan
 * 
 */
public class MaxAgeRunnable implements Runnable {
	private Logger log = LoggerFactory.getLogger(MaxAgeRunnable.class);

	private ConnPool pool;

	public MaxAgeRunnable(ConnPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		while (true) {
			if (pool.isClosed()) {
				log.debug("MaxAgeRunnable stop to run,pool is shutdown,poolname="
						+ pool.getPoolName());
				return;
			}
			// TODO
			try {
				Thread.sleep(pool.getConfig().getKeepAliveTimeInterval());
			} catch (InterruptedException e) {
				// no op
			}
			log.debug("MaxAgeRunnable check pool,poolname="
					+ pool.getPoolName());
		}
	}
}
