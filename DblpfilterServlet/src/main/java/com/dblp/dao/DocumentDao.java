package com.dblp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dblp.model.Documents;
import com.dblp.model.Svm;
import com.dblp.util.DataLink;
import com.dblp.util.Tools;

public class DocumentDao {

	public static Documents GetDocument(Connection conn,int id)
	{
		Documents docu=null;
		int jourac=1;
		int booktitle=1;
		int type=0;
		String googlelink=null;
		
		String sql="select Mdate,Title,School,Booktitle, IYear, Url, Ee, Publisher, Jourac, Citenum, Itype, School, JourOther from t_doc where id=?";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);//Mdate,Title,Booktitle,Author,Iyear,Url,Ee,Publisher,Journal,Citenum,Itype,JourOther.
			pre.setInt(1, id);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{			
				docu=new Documents();
				docu.setYear(set.getString("IYear"));
				docu.setMdate(set.getDate("Mdate"));
				docu.setUrl(set.getString("Url"));
				docu.setEe(set.getString("Ee"));
				docu.setCitenum(set.getInt("Citenum"));
				docu.setSchool(set.getString("School"));
				type=set.getInt("Itype");
				docu.setType(Tools.GetType(type));
				jourac=set.getInt("Jourac");
				booktitle=set.getInt("Booktitle");
				docu.setPublisher(set.getString("Publisher"));
				googlelink="http://scholar.google.com.hk/scholar?hl=zh-CN&q="+set.getString("Title").replace(' ', '+');
				docu.setGooglelink(googlelink);
				if(jourac==1&&booktitle==1)
				{
					docu.setGrade(0);
					if(type==1)
					{
						docu.setJourac(set.getString("JourOther"));
					}
					else if(type==2)
					{
						docu.setConf(set.getString("JourOther"));
					}
					else
					{
						docu.setConf(set.getString("JourOther"));
					}
				}
				else if(jourac!=1)
				{
					pre.close();
					Tools.Setpearconf(conn,docu, 1,jourac);
				}
				else
				{
					pre.close();
					Tools.Setpearconf(conn,docu,2, booktitle);
				}
//				pre.close();
				
			}
//			set.close();
			return docu;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docu;
	}
	
	public static Documents GetOneDocument(Connection conn,int id)
	{
		Documents docu=null;
		int jourac=1;
		int booktitle=1;
	
		String sql="select Booktitle,Jourac, Citenum,Hits from t_doc where id=?";
		try {
			
			PreparedStatement pre=conn.prepareStatement(sql);//Mdate,Title,Booktitle,Author,Iyear,Url,Ee,Publisher,Journal,Citenum,Itype,JourOther.
			pre.setInt(1, id);
			ResultSet set=pre.executeQuery();
			if(set.next())
			{			
				docu=new Documents();
				docu.setCitenum(set.getInt("Citenum"));
				docu.setHits(set.getInt("Hits"));
				jourac=set.getInt("Jourac");
				booktitle=set.getInt("Booktitle");
				if(jourac==1&&booktitle==1)
				{
					docu.setGrade(0);
				}
				else if(jourac!=1)
				{
					docu.setGrade(PerconfDao.GetGradeByid(conn,jourac));
				}
				else
				{
					docu.setGrade(PerconfDao.GetGradeByid(conn,booktitle));
				}
				pre.close();
				//DataLink.CloseConnection(conn);
				
			}
			set.close();
			return docu;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docu;
	}
	
	public int MaxId()
	{
		
		Connection conn=DataLink.getConnection();
		String sql="select max(id) from t_redoc";
		try{
			Statement statement=conn.createStatement();
			ResultSet set= statement.executeQuery(sql);
			if(set.next())
			{
				statement.close();
				int id=set.getInt(1);
				set.close();
				return id;
			}
			statement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		DataLink.CloseConnection(conn);
		return 0;
	}
	
	public void UpdateDocu(Connection conn,int id) throws SQLException
	{
		String sql="update t_doc set Hits=Hits+1 where id=?";
		PreparedStatement pre=conn.prepareStatement(sql);//Mdate,Title,Booktitle,Author,Iyear,Url,Ee,Publisher,Journal,Citenum,Itype,JourOther.
		pre.setInt(1, id);
		pre.executeUpdate();
		pre.close();
		
		//DataLink.CloseConnection(conn);
	}
	
	public boolean CreateSvm(Connection conn, Svm svm) throws SQLException
	{
		
		String sql="replace into t_svme(query,docid) values(?,?)";//Id, Iname, Pwd, email, resfield, isauto
		PreparedStatement pre=conn.prepareStatement(sql);
		pre.setString(1,svm.getQuery());
		pre.setInt(2,svm.getDocid());
		if(pre.executeUpdate()>0)
		{
			pre.close();
			return true;
		}
		return false;
	}

}
