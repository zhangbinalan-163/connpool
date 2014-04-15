package com.zhangbin.conpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 保证链接的有效性
 * 
 * @author zhangbinalan
 * 
 */
public class KeepAliveRunnable implements Runnable {

	private Logger log = LoggerFactory.getLogger(KeepAliveRunnable.class);

	private ConnPool pool;

	public KeepAliveRunnable(ConnPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		while (true) {
			if (pool.isClosed()) {
				log.debug("KeepAliveRunnable stop to run,pool is shutdown,poolname="
						+ pool.getPoolName());
				return;
			}
			// TODO
			try {
				Thread.sleep(pool.getConfig().getKeepAliveTimeInterval());
			} catch (InterruptedException e) {
				// no op
			}
			log.debug("KeepAliveRunnable check pool,poolname="
					+ pool.getPoolName());
		}
	}

}
