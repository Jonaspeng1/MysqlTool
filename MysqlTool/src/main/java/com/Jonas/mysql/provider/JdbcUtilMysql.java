package com.Jonas.mysql.provider;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.Jonas.mysql.provider.hander.IResultSetHandler;





public class JdbcUtilMysql {
	private static Logger logger = Logger.getLogger(JdbcUtilMysql.class);
	
	// 连接管理器
	private static ConnectionProvider connectionProvider;

	// 每个线程共享一个连接
	private static ThreadLocal<Connection> connWrapper = new ThreadLocal<Connection>();
	
	public static ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		JdbcUtilMysql.connectionProvider = connectionProvider;
	}
	
	public static Connection getConnection() throws Exception {

		Connection connection = connWrapper.get();
		if (connection != null && !connection.isClosed()) {
			return connection;
		}

		// 取连接并放入线程属性域中
		connection = connectionProvider.getConnection();
		if (connection == null) {
			throw new SQLException("无法获取数据库连接");
		}
		connWrapper.set(connection);
		return connection;
	}

	// 如果当前线程有连接则调用该方法关闭掉
	public static void closeConnection() {

		Connection conn = connWrapper.get();
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Can not close database connection", e);
			}
			// 释放掉保存的对象
			connWrapper.remove();
		}

	}
	

    /**
    * @Method: release
    * @Description: 释放资源，
    * 释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
    *
    * @param conn
    * @param st
    * @param rs
    */ 
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(conn!=null){
            try{
                //关闭Connection数据库连接对象
            	closeConnection();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
   
    

    /**
    * @Method: update
    * @Description: 万能更新
    * 所有实体的CUD操作代码基本相同，仅仅发送给数据库的SQL语句不同而已，
    * 因此可以把CUD操作的所有相同代码抽取到工具类的一个update方法中，并定义参数接收变化的SQL语句
    * @param sql 要执行的SQL
    * @param params 执行SQL时使用的参数
     * @throws Exception 
    */ 
    public static int update(String sql,Object params[]) throws Exception{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            st = conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                st.setObject(i+1, params[i]);
            }
            int result = st.executeUpdate();
            return result;
            
        }finally{
            release(conn, st, rs);
        }
    }
    
    /**
    * @Method: query
    * @Description:万能查询
    * 实体的R操作，除SQL语句不同之外，根据操作的实体不同，对ResultSet的映射也各不相同，
    * 因此可义一个query方法，除以参数形式接收变化的SQL语句外，可以使用策略模式由qurey方法的调用者决定如何把ResultSet中的数据映射到实体对象中。
    * @param sql 要执行的SQL
    * @param params 执行SQL时使用的参数
    * @param rsh 查询返回的结果集处理器
    * @return
     * @throws Exception 
    */ 
    public static Object query(String sql,Object params[],IResultSetHandler rsh) throws Exception{
        
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try{
            conn = getConnection();
            st =conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                st.setObject(i+1, params[i]);
            }
            rs = st.executeQuery();
            /**
             * 对于查询返回的结果集处理使用到了策略模式，
             * 在设计query方法时，query方法事先是无法知道用户对返回的查询结果集如何进行处理的，即不知道结果集的处理策略，
             * 那么这个结果集的处理策略就让用户自己提供，query方法内部就调用用户提交的结果集处理策略进行处理
             * 为了能够让用户提供结果集的处理策略，需要对用户暴露出一个结果集处理接口ResultSetHandler
             * 用户只要实现了ResultSetHandler接口，那么query方法内部就知道用户要如何处理结果集了
             */
            return rsh.handler(rs);
            
        }finally{
            release(conn, st, rs);
        }
    }
    
	   
	    
	    
	    
}
