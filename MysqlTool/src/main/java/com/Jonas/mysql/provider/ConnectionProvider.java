package com.Jonas.mysql.provider;

import java.sql.Connection;

public interface ConnectionProvider {

	Connection getConnection()throws Exception;

}
