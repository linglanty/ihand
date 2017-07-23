package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.dblp.model.User;

public class UserDao {
	
	//检查邮箱是否已经存在
	public boolean userIsexit(Connection conn, String email) {
		
		///Connection conn=DataLink.getConnection();
		String sql="select * from t_user where email=?";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1, email);
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
	//	DataLink.CloseConnection(conn);
		return false;
	}
	
	//注册
	public boolean saveUser(Connection conn,User user) {
		
		///Connection conn=DataLink.getConnection();
		String sql="insert into t_user(Iname,Pwd,email,resfield,isauto,areaother) values(?,?,?,?,?,?)";
		try {
			PreparedStatement pre=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pre.setString(1,user.getName());
			pre.setString(2,user.getPwd());
			pre.setString(3,user.getEmail());
			pre.setString(4,user.getResfield());
			pre.setInt(5,user.getIsauto());
			pre.setString(6, user.getAreaother());
			int result=pre.executeUpdate();
			if(result>0)
			{
				ResultSet rs = pre.getGeneratedKeys();
				if(rs.next())
				user.setId(rs.getInt(1));
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
	//登录
	public User login(Connection conn, String email,String pwd) {
		User user=null;
		///Connection conn=DataLink.getConnection();
		String sql="select * from t_user where email=? and pwd=?";
		
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1, email);
			pre.setString(2, pwd);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{				//Id, Iname, Pwd, email, resfield, isauto
				user=new User();
				user.setId(set.getInt("Id"));
				user.setName(set.getString("Iname"));
				user.setResfield(set.getString("resfield"));
				user.setAreaother(set.getString("areaother"));
				pre.close();
				set.close();
				//DataLink.CloseConnection(conn);
				return user;
			}
			pre.close();
			set.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return null;
	}	

	public boolean updateUser(Connection conn, User user) {

		///Connection conn=DataLink.getConnection();
		String sql="update t_user set Iname=?,resfield=?,isauto=?,areaother=? where id=?";//Id, Iname, Pwd, email, resfield, isauto
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,user.getName());
			pre.setString(2,user.getResfield());
			pre.setInt(3,user.getIsauto());
			pre.setString(4, user.getAreaother());
			pre.setInt(5,user.getId());
			
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

	public User GetUserByIdentifyCode(Connection conn, String ic)
	{
		return null;
	}

	public User GetUser(Connection conn, int id)
	{
		User user=null;
		///Connection conn=DataLink.getConnection();
		String sql="select * from t_user where id=?";
		
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, id);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{				//Id, Iname, Pwd, email, resfield, isauto
				user=new User();
				user.setId(set.getInt("Id"));
				user.setName(set.getString("Iname"));
				user.setEmail(set.getString("email"));
				user.setIsauto(set.getInt("isauto"));
				user.setResfield(set.getString("resfield"));
				user.setAreaother(set.getString("areaother"));
				pre.close();
				set.close();
			//	DataLink.CloseConnection(conn);
				return user;
			}
			pre.close();
			set.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DataLink.CloseConnection(conn);
		return null;
		
	}
	
	public boolean ChangePassword(Connection conn, int id,String pwd) {
		
	///	Connection conn=DataLink.getConnection();
		String sql="update t_user set Pwd=? where id=?";//Id, Iname, Pwd, email, resfield, isauto
	
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setString(1,pwd);
			pre.setInt(2,id);
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
