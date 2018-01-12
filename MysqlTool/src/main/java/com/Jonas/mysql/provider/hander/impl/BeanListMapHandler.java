package com.Jonas.mysql.provider.hander.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Jonas.mysql.provider.hander.IResultSetHandler;

public class BeanListMapHandler implements IResultSetHandler{
	
	public List<Map<String, Object>> handler(ResultSet rs) {
		List<Map<String, Object>> result = Collections.emptyList();
		ResultSetMetaData RSMD;
		try {
			RSMD = rs.getMetaData();
			int colCount = RSMD.getColumnCount();
			result = new LinkedList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> bean = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; i++) {
					Object value = rs.getObject(i);
					String name = RSMD.getColumnLabel(i);
					bean.put(name, value);
				}
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	

}
