package com.zhangbin.conpool;

import static com.zhangbin.conpool.common.Constants.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangbin.conpool.config.PoolConfig;
import com.zhangbin.conpool.utils.ConnectionIdGenerator;
import com.zhangbin.conpool.utils.StringUtils;

public class ConnPool {
	private static Logger logger = LoggerFactory.getLogger(ConnPool.class);

	private String poolName = POOLNAME_DEFAULT;
	// 配置
	private PoolConfig config;
	private ConnectionHook connectionHook;
	// 连接池维护线程执行器
	private ScheduledExecutorService executorService;
	// 空闲的链接
	private Queue<ConnectionHandle> avaliablePool;
	// 已分配的链接
	private List<ConnectionHandle> busyPool;
	// 连接池的锁
	private Lock lock;
	//
	private int avaliableNum;
	private int busyNum;
	// 是否关闭连接池
	private volatile boolean isClosed;
	// 是否已经初始化连接池
	private volatile boolean isInitialized;

	public ConnPool(PoolConfig config) {
		this(POOLNAME_DEFAULT, config, null);
	}

	public ConnPool(PoolConfig config, ConnectionHook connectionHook) {
		this(POOLNAME_DEFAULT, config, connectionHook);
	}

	public ConnPool(String poolName, PoolConfig config,
			ConnectionHook connectionHook) {
		if (StringUtils.isEmpty(poolName)) {
			poolName = POOLNAME_DEFAULT;
		}
		this.poolName = poolName;
		this.connectionHook = connectionHook;

		checkConfig(config);
		this.config = config;
		this.lock = new ReentrantLock();

		this.avaliablePool = new LinkedList<ConnectionHandle>();
		this.busyPool = new ArrayList<ConnectionHandle>();
	}

	/**
	 * 初始化连接池
	 * 
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		lock.lock();
		try {
			if (this.isInitialized) {
				logger.warn("connpool has been initialized,poolname="
						+ this.poolName);
				return;
			}
			this.isInitialized = true;

			logger.debug("[ConnPool:" + poolName + ",start to init]");
			// 加载数据库驱动
			registerDriver();
			// 初始化链接
			try {
				for (int i = 0; i < this.config.getInitCon(); i++) {
					ConnectionHandle handle = createConnectionHandle();

					this.avaliablePool.offer(handle);
					avaliableNum++;
					logger.debug("[ConnPool:"
							+ poolName
							+ ", initial create Connection and put into avaliablePool,connectionId="
							+ handle.getConnectionId() + "]");
				}
			} catch (SQLException e) {
				logger.error("[ConnPool:" + poolName
						+ ",initial create Connection error", e);
				isClosed = true;
				// 关闭已经创建成功的链接
				for (ConnectionHandle con : avaliablePool) {
					con.internalClose();
				}
				avaliableNum = 0;
				// 是否需要unRegisterDriver()
				throw e;
			}
			//
			KeepAliveRunnable keepAlive = new KeepAliveRunnable(this);
			MaxAgeRunnable maxAgeRunnable = new MaxAgeRunnable(this);
			ConnNumberRunnable connNumberRunnable = new ConnNumberRunnable(this);

			executorService = Executors.newScheduledThreadPool(THREAD_NUM);
			executorService.schedule(keepAlive, WATCHTREAD_DELAY,
					TimeUnit.MILLISECONDS);
			executorService.schedule(maxAgeRunnable, WATCHTREAD_DELAY + 2000,
					TimeUnit.MILLISECONDS);
			executorService.schedule(connNumberRunnable,
					WATCHTREAD_DELAY + 2000, TimeUnit.MILLISECONDS);

			logger.debug("[ConnPool:" + poolName + ", init successfully],"
					+ this.config.toString());
		} finally {
			lock.unlock();
		}
	}

	// 检查config是否合法
	private void checkConfig(PoolConfig poolConfig) {
		if (poolConfig == null) {
			throw new IllegalArgumentException(
					"conpool config error,can not be null!");
		}
		String driver = poolConfig.getDriver();
		String password = poolConfig.getPassword();
		String username = poolConfig.getUsername();
		String url = poolConfig.getUrl();
		if (StringUtils.isEmpty(driver)) {
			throw new IllegalArgumentException("conpool config error,driver");
		}
		if (StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException("conpool config error,password");
		}
		if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("conpool config error,username");
		}
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("conpool config error,url");
		}
	}

	/**
	 * 关闭连接池
	 * 
	 * @throws SQLException
	 */
	public void shutdown() {
		logger.debug("[ConnPool:" + poolName + ",start to shutdown]");
		long start = System.currentTimeMillis();
		// 关闭所有链接
		lock.lock();
		try {
			isClosed = true;
			//遍历关闭所有空闲链接
			Iterator<ConnectionHandle> iter = avaliablePool.iterator();
			while (iter.hasNext()) {
				ConnectionHandle con = iter.next();
				con.internalClose();

				iter.remove();
				avaliableNum--;
			}
			//遍历关闭所有忙碌链接
			Iterator<ConnectionHandle> iterBusy = busyPool.iterator();
			while (iterBusy.hasNext()) {
				ConnectionHandle con = iterBusy.next();
				con.internalClose();

				iter.remove();
				busyNum--;
			}
			// 关闭监控线程
			try {
				executorService.shutdownNow();// 会中断执行线程
				executorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// do nothing
			}
			// 卸载数据库驱动
			if (this.config.isUnregisterDriverOnShutdown()) {
				unRegisterDriver();
			}
			logger.debug("[ConnPool:" + poolName
					+ ",shutdown successfully,time(ms)="
					+ (System.currentTimeMillis() - start) + "]");
		} finally {
			lock.unlock();
		}
	}

	protected void removeHandleFromPool(ConnectionHandle connection,
			List<ConnectionHandle> list) {

	}

	// 加载数据库驱动
	private void registerDriver() throws SQLException {
		try {
			DriverManager.registerDriver(DriverManager.getDriver(this.config
					.getUrl()));
			logger.debug("Registering driver success");
		} catch (SQLException e) {
			logger.error("Registering driver failed", e);
			throw e;
		}
	}

	// 卸载数据库驱动
	private void unRegisterDriver() {
		try {
			DriverManager.deregisterDriver(DriverManager.getDriver(this.config
					.getUrl()));
			logger.debug("Unregistering driver success");
		} catch (SQLException e) {
			logger.info("Unregistering driver failed.", e);
		}
	}

	/**
	 * 创建一个链接代理
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected ConnectionHandle createConnectionHandle() throws SQLException {
		// 链接编号
		try {
			String connectionId = ConnectionIdGenerator.getId();
			Connection internalConnection = obtainInternalConnection();

			ConnectionHandle handle = new ConnectionHandle(connectionId, this,
					this.connectionHook, internalConnection);
			if (this.config.isSendInitialSQLOnCreate()) {
				handle.sendCheckSQL();
			}
			return handle;
		} catch (SQLException e) {
			logger.error("[ConnPool:" + poolName
					+ ",create ConnectionHandle fail", e);
			throw e;
		}
	}

	/**
	 * 获得底层数据库链接
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Connection obtainInternalConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.config.getUrl(),
					this.config.getUsername(), this.config.getPassword());
			return conn;
		} catch (SQLException e) {
			logger.warn("obtainInternalConnection fail");
			throw e;
		}
	}

	/**
	 * 阻塞式获取链接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return null;
	}

	/**
	 * 释放链接，放回到连接池
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	protected void releaseConnection(ConnectionHandle connection)
			throws SQLException {
		lock.lock();
		try {
			// 连接池已经关闭
			if (isClosed()) {
				logger.warn("conn pool has been shutdown,connectionhandle try to close,connectionId="
						+ connection.getConnectionId());
				connection.internalClose();
				return;
			}
			// 链接可能处于异常状态，不回收
			if (connection.isPossibleInException()) {
				logger.warn("conn may be in exception,try to close,connectionId="
						+ connection.getConnectionId());
				connection.internalClose();
				busyNum--;
				return;
			}
			// 放回链接到可用队列
			busyPool.remove(connection);
			busyNum--;

			avaliablePool.offer(connection);
			avaliableNum++;

			logger.debug("conn remove in busy list,put back to avaliable list,connectionId="
					+ connection.getConnectionId());
		} finally {
			lock.unlock();
		}
	}

	// 获取空闲链接数目
	public int getAvaliableNum() {
		return avaliableNum;
	}

	// 获取已经分配的链接数目
	public int getBusyNum() {
		return busyNum;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public String getPoolName() {
		return poolName;
	}

	public PoolConfig getConfig() {
		return config;
	}
}
