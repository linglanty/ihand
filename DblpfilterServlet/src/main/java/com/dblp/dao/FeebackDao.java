package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FeebackDao {

	public boolean saveFeeback(Connection conn, String title,String content,String path) {
		
		//Connection conn=DataLink.getConnection();
		String sql="insert into t_feeback(title,content,path) values(?,?,?)";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,title);
			pre.setString(2,content);
			pre.setString(3,path);
			pre.executeUpdate();
			pre.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		///DataLink.CloseConnection(conn);
		return false;
		
	}

}
