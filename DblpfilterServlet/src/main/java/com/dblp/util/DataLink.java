package com.dblp.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataLink {


	public static Connection conn = null;
	
	static {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/dblpfilter";
			conn= DriverManager.getConnection(url,"root","root");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void startConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/dblpfilter";
			conn= DriverManager.getConnection(url,"root","root");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		
		Connection conn = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/dblpfilter";
			conn= DriverManager.getConnection(url,"root","root");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void CloseConnection(Connection conns)
	{
		if(conns!=null)
		{
			try{
				conns.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	} 
}
