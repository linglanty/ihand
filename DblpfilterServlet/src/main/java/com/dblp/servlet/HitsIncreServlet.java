package com.dblp.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dblp.dao.DocumentDao;
import com.dblp.dao.RelevanceDao;
import com.dblp.model.Svm;
import com.dblp.util.ConnectionUtil;

public class HitsIncreServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response){

		response.setContentType("text/html");
		try {
			request.setCharacterEncoding("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setCharacterEncoding("GBK");
		String Sid=request.getParameter("Sid");
		String query=request.getParameter("queryCopy");
		boolean IsLogin=false;
		int userid=0;
		if(Sid==null&&!Sid.matches("^[0-9]*$"))
		{
			return;
		}
		Cookie mycookies[] = request.getCookies();
		
		DocumentDao dao=new DocumentDao();
		Svm svm=new Svm();
		int docid= Integer.parseInt(Sid);
		svm.setDocid(docid);
		svm.setQuery(query);
		Connection conn=ConnectionUtil.getConnection();//记录用户的查阅过的东西
		boolean autoCommit;
		try {
			
			if(mycookies.length>0)
			{
				for (int i= 0; i < mycookies.length; i++)
				{
					if(mycookies[i].getName().equals("dblpuser"))
					{
						String userinfo=mycookies[i].getValue();
						IsLogin=true;
						userid= Integer.parseInt(userinfo.substring(userinfo.lastIndexOf("#")+1));
					}
				}
			}
			
			autoCommit=conn.getAutoCommit();
			conn.setAutoCommit(false);
			if(IsLogin&&userid!=0)
			{
				RelevanceDao.InsertRelevance(conn, userid, docid);
			}
			dao.UpdateDocu(conn,docid);
			dao.CreateSvm(conn, svm);
			conn.commit();
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		ConnectionUtil.closeConnection(conn);
	}

}
