package com.zhangbin.conpool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangbin.conpool.common.exception.ConnectionClosedException;
import com.zhangbin.conpool.utils.StringUtils;

/**
 * Connection�İ�װ��
 * 
 * @author zhangbinalan
 * 
 */
public class ConnectionHandle implements Connection {

	private static Logger logger = LoggerFactory
			.getLogger(ConnectionHandle.class);
	// ���ӵ�ID���
	private String connectionId;
	// ���ӳ�
	private ConnPool pool;
	// ���ӹ���
	private ConnectionHook hook;
	// ʵ�ʵ����ݿ�����
	private Connection internalConnection;
	// ����ʱ��,�ڲ�connection������ʱ��
	private long createTimeInMils;
	// ���һ��ʹ��ʱ�䣬��һ��ʹ������ͷŵ����ӳ��е�ʱ��
	private long lastUsedTimeInMils;
	// �Ƿ��߼��ر�
	private boolean isLogicalClosed;
	// �Ƿ�������ר��һ���̹߳رճ��ִ��������
	private boolean possibleInException;
	// ��
	private Lock lock;

	public ConnectionHandle(String connectionId, ConnPool pool,
			ConnectionHook hook, Connection internalConnection) {
		this.connectionId = connectionId;
		this.pool = pool;
		this.hook = hook;
		this.internalConnection = internalConnection;
		this.createTimeInMils = System.currentTimeMillis();
		this.lock = new ReentrantLock();
		postCreated();
	}

	private void postCreated() {
		if (this.hook != null) {
			hook.onCreated(this);
		}
	}

	@Override
	public void close() throws SQLException {
		//
		lock.lock();
		try {
			if (isLogicalClosed) {
				logger.warn("connectionhandle has been closed logical,connectionid="
						+ connectionId);
				return;
			}
			// ����Ĭ���Զ��ύ
			boolean isAutoCommit = getAutoCommit();
			if (!isAutoCommit) {
				rollback();
				setAutoCommit(true);
			}

			isLogicalClosed = true;

			// �Ż����ӳ�
			this.lastUsedTimeInMils = System.currentTimeMillis();
			//TODO ��Ҫ�ڱ����������ӳ��е��ã����в����޸�list������
			pool.releaseConnection(this);

			if (this.hook != null) {
				hook.onCheckIn(this);
			}
		} finally {
			lock.unlock();
		}
	}

	// �ڲ�ʵ�ʹر�����
	public void internalClose() {

		lock.lock();
		try {
			isLogicalClosed = true;
			if (internalConnection != null) {
				closeQuietly();
				internalConnection = null;
				// TODO �������в����޸�list������
				// this.pool.destroyConnection(this);
				if (this.hook != null) {
					hook.onClosed(this);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	private void closeQuietly() {
		try {
			internalConnection.close();
			logger.debug("ConnectionHandle internalconnection close success,connectionid="
					+ connectionId);
		} catch (SQLException e) {
			logger.error("internalconnection close occur fail,connectionid="
					+ connectionId, e);
		}
	}

	public boolean isPossibleInException() {
		return possibleInException;
	}

	private SQLException makePossibleException(SQLException e) {

		possibleInException = true;
		if (this.hook != null) {
			hook.onException(this, e);
		}
		return e;
	}

	protected void checkClosed() throws SQLException {
		lock.lock();
		try {
			if (isLogicalClosed) {
				throw new ConnectionClosedException();
			}
		} finally {
			lock.unlock();
		}
	}

	protected void sendCheckSQL() throws SQLException {

		Statement statement = null;
		ResultSet rs = null;
		try {
			String keepAliveSQL = pool.getConfig().getKeepAliveSql();
			if (StringUtils.isEmpty(keepAliveSQL)) {
				rs = internalConnection.getMetaData().getTables(null, null,
						"connpool_keepalive", new String[] { "TABLE" });
			} else {
				statement = internalConnection.createStatement();
				statement.execute(keepAliveSQL);
			}
			logger.debug("ConnectionHandle sendCheckSQL success,connectionId="
					+ connectionId);
		} catch (SQLException e) {
			logger.error("ConnectionHandle sendCheckSQL fail,connectionId="
					+ connectionId, e);
			throw e;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				// ignore
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public long getLastUsedTimeInMils() {
		return lastUsedTimeInMils;
	}

	public void setLastUsedTimeInMils(long lastUsedTimeInMils) {
		this.lastUsedTimeInMils = lastUsedTimeInMils;
	}

	public long getCreateTimeInMils() {
		return createTimeInMils;
	}

	public String getConnectionId() {
		return connectionId;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return internalConnection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return internalConnection.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		checkClosed();
		Statement result;
		try {
			result = internalConnection.createStatement();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection.prepareStatement(sql);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		checkClosed();
		CallableStatement result;
		try {
			result = internalConnection.prepareCall(sql);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		checkClosed();
		String result;
		try {
			result = internalConnection.nativeSQL(sql);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		checkClosed();
		try {
			internalConnection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		checkClosed();
		boolean result;
		try {
			result = internalConnection.getAutoCommit();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void commit() throws SQLException {
		checkClosed();
		try {
			internalConnection.commit();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public void rollback() throws SQLException {
		checkClosed();
		try {
			internalConnection.rollback();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public boolean isClosed() throws SQLException {
		checkClosed();
		boolean result;
		try {
			result = internalConnection.isClosed();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		checkClosed();
		DatabaseMetaData result;
		try {
			result = internalConnection.getMetaData();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		checkClosed();
		try {
			internalConnection.setReadOnly(readOnly);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		checkClosed();
		boolean result;
		try {
			result = internalConnection.isReadOnly();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		checkClosed();
		try {
			internalConnection.setCatalog(catalog);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}

	}

	@Override
	public String getCatalog() throws SQLException {
		checkClosed();
		String result;
		try {
			result = internalConnection.getCatalog();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		checkClosed();
		try {
			internalConnection.setTransactionIsolation(level);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		checkClosed();
		int result;
		try {
			result = internalConnection.getTransactionIsolation();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		checkClosed();
		SQLWarning result;
		try {
			result = internalConnection.getWarnings();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void clearWarnings() throws SQLException {
		checkClosed();
		try {
			internalConnection.clearWarnings();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		checkClosed();
		Statement result;
		try {
			result = internalConnection.createStatement(resultSetType,
					resultSetConcurrency);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection.prepareStatement(sql, resultSetType,
					resultSetConcurrency);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		checkClosed();
		CallableStatement result;
		try {
			result = internalConnection.prepareCall(sql, resultSetType,
					resultSetConcurrency);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		checkClosed();
		Map<String, Class<?>> result;
		try {
			result = internalConnection.getTypeMap();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		checkClosed();
		try {
			internalConnection.setTypeMap(map);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		checkClosed();
		try {
			internalConnection.setHoldability(holdability);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public int getHoldability() throws SQLException {
		checkClosed();
		int result;
		try {
			result = internalConnection.getHoldability();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		checkClosed();
		Savepoint result;
		try {
			result = internalConnection.setSavepoint();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		checkClosed();
		Savepoint result;
		try {
			result = internalConnection.setSavepoint(name);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		checkClosed();
		try {
			internalConnection.rollback(savepoint);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		checkClosed();
		try {
			internalConnection.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		checkClosed();
		Statement result;
		try {
			result = internalConnection.createStatement(resultSetType,
					resultSetConcurrency, resultSetHoldability);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection.prepareStatement(sql, resultSetType,
					resultSetConcurrency, resultSetHoldability);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		checkClosed();
		CallableStatement result;
		try {
			result = internalConnection.prepareCall(sql, resultSetType,
					resultSetConcurrency, resultSetHoldability);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection
					.prepareStatement(sql, autoGeneratedKeys);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection.prepareStatement(sql, columnIndexes);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		checkClosed();
		PreparedStatement result;
		try {
			result = internalConnection.prepareStatement(sql, columnNames);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Clob createClob() throws SQLException {
		checkClosed();
		Clob result;
		try {
			result = internalConnection.createClob();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Blob createBlob() throws SQLException {
		checkClosed();
		Blob result;
		try {
			result = internalConnection.createBlob();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public NClob createNClob() throws SQLException {
		checkClosed();
		NClob result;
		try {
			result = internalConnection.createNClob();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		checkClosed();
		SQLXML result;
		try {
			result = internalConnection.createSQLXML();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		checkClosed();
		boolean result;
		try {
			result = internalConnection.isValid(timeout);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		internalConnection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		internalConnection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		String result = null;
		checkClosed();
		try {
			result = internalConnection.getClientInfo(name);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Properties result = null;
		checkClosed();
		try {
			result = internalConnection.getClientInfo();
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		Array result = null;
		checkClosed();
		try {
			result = internalConnection.createArrayOf(typeName, elements);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		Struct result = null;
		checkClosed();
		try {
			result = internalConnection.createStruct(typeName, attributes);
		} catch (SQLException e) {
			throw makePossibleException(e);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[ConnectionHandle,connectionId=" + connectionId
				+ ",createTime=" + createTimeInMils + ",lastUseTime="
				+ lastUsedTimeInMils + ",isPhysicalclosed="
				+ (internalConnection == null ? true : false) + "]";
	}
}
