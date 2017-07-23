package com.dblp.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

//import java.util.logging.Logger;

public class ConnectionUtil {
	
//	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	private static ComboPooledDataSource ds = null;
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static int initialPoolSize = 3;
	private static int idleConnectionTestPeriod = 60;
	private static int maxPoolSize = 30;
	private static int acquireIncrement = 1;
	private static int maxIdleTime = 30000;
	private static int acquireRetryAttempts = 30;
	private static int acquireRetryDelay = 1000;
	static {
		Properties props = new Properties();
		try {
			props.load(ConnectionUtil.class.getClassLoader()
					.getResourceAsStream("db.properties"));
			if (props != null) {
				driver = props.getProperty("driver");
				url = props.getProperty("url");
				username = props.getProperty("username");
				password = props.getProperty("password");
				try{
				initialPoolSize = Integer.parseInt(props.getProperty("pool.initialPoolSize", "3"));
				maxPoolSize = Integer.parseInt(props.getProperty("pool.maxPoolSize", "15"));
				acquireIncrement = Integer.parseInt(props.getProperty("pool.acquireIncrement", "1"));
				idleConnectionTestPeriod = Integer.parseInt(props.getProperty("pool.idleConnectionTestPeriod", "60"));
				maxIdleTime = Integer.parseInt(props.getProperty("pool.maxIdleTime", "30000"));
				acquireRetryAttempts = Integer.parseInt(props.getProperty("pool.acquireRetryAttempts", "30"));
				acquireRetryDelay = Integer.parseInt(props.getProperty("pool.acquireRetryDelay", "1000"));
				}catch(Exception e){
			//		logger.error(e);
				}
				
				ds = new ComboPooledDataSource();
				try {
					ds.setDriverClass(driver);
//					if (logger.isDebugEnabled()) {
//						logger.debug("initliaze database successful.");
//					}
				} catch (PropertyVetoException e) {
//					logger.error(e);
				}
				
				ds.setJdbcUrl(url);
				ds.setUser(username);
				ds.setPassword(password);
				ds.setInitialPoolSize(initialPoolSize);
				ds.setMaxPoolSize(maxPoolSize);
				ds.setAcquireIncrement(acquireIncrement);
				ds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
				ds.setMaxIdleTime(maxIdleTime);
				ds.setAutoCommitOnClose(true);
				ds.setAcquireRetryAttempts(acquireRetryAttempts);
				ds.setAcquireRetryDelay(acquireRetryDelay);
				ds.setBreakAfterAcquireFailure(true);
			}
		} catch (IOException e) {
//			logger.error(e);
		}
	}
	
	/**
	 * 获取数据库链接
	 * 
	 * @return 返回数据库链接
	 */
	public synchronized static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
//			logger.error(e);
		}
		return null;
	}

	/**
	 * 关闭数据库链接
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
//				logger.error(e);
			}
		}
	}
	
	/**
	 * 关闭 PreparedStatement
	 * 
	 * @param pstmt
	 */
	public static void closeStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
//				logger.error(e);
			}
		}

	}

	/**
	 * 关闭结果集
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
	//			logger.error(e);
			}
		}
	}

	/**
	 * 关闭datasource
	 */
	protected void finalize() throws Throwable {
		DataSources.destroy(ds);
		super.finalize();
		
	}
}
