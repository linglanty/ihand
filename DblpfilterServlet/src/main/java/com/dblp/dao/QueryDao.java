package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryDao {

	public static void InsertQuery(Connection conn,String query)
	{
		String sql="replace into t_query(Query) values(?)";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,query);
			if(pre.executeUpdate()>0)
			{
				pre.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean IsExit(Connection conn,String query)
	{
		String sql="select * from t_query where Query=?";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1, query);
			ResultSet set=pre.executeQuery();
			if(!set.next())
			{
				pre.close();
				set.close();
				return true;
			}
			pre.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
