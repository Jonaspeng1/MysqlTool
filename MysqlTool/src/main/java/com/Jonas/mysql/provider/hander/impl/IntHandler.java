package com.Jonas.mysql.provider.hander.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import com.Jonas.mysql.provider.hander.IResultSetHandler;

public class IntHandler implements IResultSetHandler {

	public Object handler(ResultSet rs) {
		 Object coulmnData = null;
		 try{
           if(!rs.next()){
               return null;
           }
           //得到结果集元数据
           ResultSetMetaData metadata = rs.getMetaData();
           int columnCount = metadata.getColumnCount();//得到结果集中有几列数据
           for(int i=0;i<columnCount;i++){
                coulmnData = rs.getObject(i+1);   
           }
           return coulmnData; 
	            
	        }catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	
	}


}
