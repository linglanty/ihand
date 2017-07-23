package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelevanceDao {

	public static boolean InsertRelevance(Connection conn,int uid,int Docid) throws SQLException
	{
		String sql="replace into t_prelevance(Uid,Docid) values(?,?)";//Id, Iname, Pwd, email, resfield, isauto
		
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1,uid);
			pre.setInt(2,Docid);
			if(pre.executeUpdate()>0)
			{
				pre.close();
				return true;
			}
		return false;
	}
	
	public static boolean IsExit(Connection conn,int Uid,int Docuid)
	{
		String sql="select * from t_prelevance where Uid=? and Docid=?";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, Uid);
			pre.setInt(2, Docuid);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{
				pre.close();
				set.close();
				return true;
			}
			pre.close();
			set.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	DataLink.CloseConnection(conn);
		return false;
		
	}
}
