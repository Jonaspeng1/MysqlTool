package com.Jonas.mysql.provider.hander.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.Jonas.mysql.provider.hander.IResultSetHandler;

public class BeanSetMapHandler implements IResultSetHandler {

	
	public Set<Map<String, String>> handler(ResultSet rs) {
		Set<Map<String, String>> result = Collections.emptySet();
		ResultSetMetaData RSMD;
		try {
			RSMD = rs.getMetaData();
			int colCount = RSMD.getColumnCount();
			result = new HashSet<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> bean = new HashMap<String, String>();
				for (int i = 1; i <= colCount; i++) {
					String value = rs.getObject(i).toString();
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
