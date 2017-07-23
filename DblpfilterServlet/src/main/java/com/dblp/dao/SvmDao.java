package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.dblp.model.Svm;

public class SvmDao {

	public boolean CreateSvm(Connection conn, Svm svm)
	{
		String sql="replace into t_peraconf(query,id) values(?,?)";//Id, Iname, Pwd, email, resfield, isauto
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,svm.getQuery());
			pre.setInt(2,svm.getDocid());
			if(pre.executeUpdate()>0)
			{
				pre.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
