package com.zhangbin.conpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 维持连接池中链接的数目在规定范围内
 * 
 * @author zhangbinalan
 * 
 */
public class ConnNumberRunnable implements Runnable {

	private Logger log = LoggerFactory.getLogger(KeepAliveRunnable.class);

	private ConnPool pool;

	public ConnNumberRunnable(ConnPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		while (true) {
			if (pool.isClosed()) {
				log.debug("ConnNumberRunnable stop to run,pool is shutdown,poolname="
						+ pool.getPoolName());
				return;
			}
			// TODO
			try {
				Thread.sleep(pool.getConfig().getKeepAliveTimeInterval());
			} catch (InterruptedException e) {
				// no op
			}
			log.debug("ConnNumberRunnable check pool,poolname="
					+ pool.getPoolName());
		}
	}
}
