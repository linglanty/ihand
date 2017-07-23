package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dblp.model.Pearconf;
import com.dblp.util.DataLink;

public class PerconfDao {

	
	public List<Pearconf> Getperconf(Connection conn, int field)//获取各个领域的期刊会议排名
	{
		
		String sql="select id,Abbr,Area,Dblpabbr,Fullname,grade from t_peraconf where Area=? order by grade desc";
		try{//Id, Abbr, Dblpabbr, Fullname, Area, grade, Itype, Publisher
			List<Pearconf> perconfs=new ArrayList<Pearconf>();
			Pearconf perconf=null;
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, field);
			ResultSet set=pre.executeQuery();
			while(set.next())
			{
				perconf=new Pearconf();;
				perconf.setPid(set.getInt("id"));
				perconf.setArea(set.getInt("Area"));
				perconf.setAbbr(set.getString("Abbr"));
				perconf.setDblpabbr(set.getString("Dblpabbr"));
				perconf.setFullname(set.getString("Fullname"));
				perconf.setGrade(set.getInt("grade"));
				perconfs.add(perconf);
			}
			pre.close();
			//DataLink.CloseConnection(conn);
			set.close();
			return perconfs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return null;
	}

	public boolean Updatperconf(Connection conn, Pearconf per) {

		String sql="update t_peraconf set Abbr=?,Dblpabbr=?,Fullname=?,grade=? where id=?";//Id, Iname, Pwd, email, resfield, isauto
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,per.getAbbr());
			pre.setString(2,per.getDblpabbr());
			pre.setString(3,per.getFullname());
			pre.setInt(4,per.getGrade());
			pre.setInt(5, per.getPid());
			if(pre.executeUpdate()>0)
			{
				pre.close();
				//DataLink.CloseConnection(conn);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return false;
	}

	public boolean Deleteperconf(Connection conn, int id)
	{
		//delete from t_user where id=2
		//Connection conn=DataLink.getConnection();
		String sql="delete from t_peraconf where id=?";//Id, Iname, Pwd, email, resfield, isauto
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, id);
			if(pre.executeUpdate()>0)
			{
				pre.close();
				//DataLink.CloseConnection(conn);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return false;
	}
	
	public Pearconf GetperconfByid(Connection conn, int pid)
	{
		///Connection conn=DataLink.getConnection();
		String sql="select id,Abbr,Dblpabbr,Fullname,grade from t_peraconf where id=?";
		try{//Id, Abbr, Dblpabbr, Fullname, Area, grade, Itype, Publisher
			Pearconf perconf=null;
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, pid);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{
				perconf=new Pearconf();;
				perconf.setPid(set.getInt("id"));
				perconf.setAbbr(set.getString("Abbr"));
				perconf.setDblpabbr(set.getString("Dblpabbr"));
				perconf.setFullname(set.getString("Fullname"));
				perconf.setGrade(set.getInt("grade"));
			}
			pre.close();
			set.close();
			//DataLink.CloseConnection(conn);
			return perconf;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		DataLink.CloseConnection(conn);
		return null;
	}
	
	public static int GetGradeByid(Connection conn, int pid)
	{
		
		///Connection conn=DataLink.conn;
		String sql="select grade from t_peraconf where id=?";
		try{
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, pid);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{
			//	pre.close();
				return set.getInt("grade");
				
			}
			//DataLink.CloseConnection(conn);
			return 0;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return 0;
	}
	
	public static int GetImportantByid(Connection conn, int pid)
	{
		
		///Connection conn=DataLink.conn;
		String sql="select grade from t_peraconf where id=?";
		try{
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, pid);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{
			//	pre.close();
				return 1;
			}
			//DataLink.CloseConnection(conn);
			return 0;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return 0;
	}
	
	public boolean Createperconf(Connection conn, Pearconf pear)
	{
		///Connection conn=DataLink.getConnection();
		String sql="replace into t_peraconf(Abbr,Dblpabbr,Fullname,Area,grade,Itype,Publisher) values(?,?,?,?,?,?,?)";//Id, Iname, Pwd, email, resfield, isauto
		try {
			
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,pear.getAbbr());
			pre.setString(2,pear.getDblpabbr());
			pre.setString(3,pear.getFullname());
			pre.setInt(4,pear.getArea());
			pre.setInt(5,pear.getGrade());
			pre.setInt(6, pear.getType());
			pre.setString(7, pear.getPublisher());
			if(pre.executeUpdate()>0)
			{
				pre.close();
				//DataLink.CloseConnection(conn);
				return true;
			}
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return false;
	}


}
