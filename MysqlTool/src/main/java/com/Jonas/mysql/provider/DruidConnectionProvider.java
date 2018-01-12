package com.Jonas.mysql.provider;

import java.sql.Connection;

import javax.sql.DataSource;

/**
 * 基于Druid连接池的连接获取器
 * 
 * @author teddy
 * 
 */
public class DruidConnectionProvider implements ConnectionProvider {


	private DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws Exception {
		return dataSource.getConnection();
	}

}
