package com.dblp.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dblp.Searcher.SearcherDblp;
import com.dblp.dao.QueryDao;
import com.dblp.model.Documents;
import com.dblp.model.QueryResults;
import com.dblp.util.ConnectionUtil;
import com.dblp.util.LogTools;
import com.dblp.util.Tools;

public class SearchServlet extends HttpServlet {

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");
		try {
			request.setCharacterEncoding("GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			LogTools.log.error(e1+"This is a error in Search UnsupportedEncodingException--39");
		}
		
		response.setCharacterEncoding("GBK");
		String queryString=request.getParameter("searchtext").trim();
		String queryCopy=request.getParameter("searchcopy");
		String trend=request.getParameter("trend");
		String change=request.getParameter("change");
		boolean IsCopy=false;
		if(queryString.equalsIgnoreCase(queryCopy))
			IsCopy=true;
		if(queryString==null||queryString=="")
		{
			
			try {
				response.getWriter().println("<script>alert('Please input the search keywords!!')</scirpt>");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in IOException--53");
			}
			try {
				request.getRequestDispatcher("../index.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in ServletException--59");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in IOException--62");
			}
			return;
		}
		
		int searchtype= Integer.parseInt(request.getParameter("sortfuction"));
		QueryResults results=null;
		List<Documents> documents=null;
		long starttime= System.currentTimeMillis();
		Connection conn=ConnectionUtil.getConnection();
		if(searchtype==1)
		{
			String field=null;
			boolean islogin=false;
			boolean isAction=false;
			int userid=0;
			Cookie mycookies[] = request.getCookies();
			if(mycookies.length>0)
			{
				for (int i= 0; i < mycookies.length; i++)
				{
					if(mycookies[i].getName().equals("dblpuser"))
					{
						String userinfo=mycookies[i].getValue();
						userid= Integer.parseInt(userinfo.substring(userinfo.lastIndexOf("#")+1));
						islogin=true;
						if(islogin&&isAction)
							break;
					}
					if (mycookies[i].getName().equals("dblpArea"))
					{
						if(!mycookies[i].getValue().equals("nothing"))
						{
							field=mycookies[i].getValue();
							isAction=true;
						}
						if(islogin&&isAction)
							break;
					}
				}
				if(!(islogin&&isAction))
				{
					field=null;
				}
			}
			// System.out.print("  ");
			if(field==null)
			{
				try {
					results=SearcherDblp.search(IsCopy,queryString.toLowerCase(), 0, 25);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogTools.log.error(e+"This is a error in Search having logined--73");
				//	LogTools.log.error(e+"This is a error in Search having logined--73");
				
				}
			}
			else
			{
				try {
					results=SearcherDblp.search(conn,userid,IsCopy,queryString.toLowerCase(), 0, 25,field);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogTools.log.error(e+"This is a error in Search not having logined--83");
				}
			}
		}
		else if(searchtype==2)
		{
			try {
				results=SearcherDblp.searchByImporIndex(IsCopy,conn,queryString.toLowerCase(), 0, 25);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in important Search--93");
			}
		}
		else
		{
			try {
				results=SearcherDblp.searchByTime(IsCopy,conn,queryString.toLowerCase(), 0, 25);
			} catch (Exception e) {
				LogTools.log.error(e+"This is a error in Time Search--101");
			}
		}
		long endtime= System.currentTimeMillis()-starttime;
		if(results!=null)
		{
			if(results.getRecordCount()>0)
			{
				if(change==null&&(trend==null)||((queryCopy==null)||(trend!=null&&!queryCopy.equals(queryString))))
				{
					if(QueryDao.IsExit(conn,queryString))
					{
						QueryDao.InsertQuery(conn, queryString);
					}
					StringBuffer Result=new StringBuffer();
					if(results.getCount()!=null)
					{
						int[] trends=results.getCount();
						for(int q=0;q<trends.length;q++)
						{
							Result.append(trends[q]);
							Result.append(",");
						}
						//System.out.print(trends.length);
						trend=Result.toString().substring(0,Result.length()-1);
					}
					else
					{
						trend="0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
					}
					/*for(int i=0;i<=15;i++)
					{
						try {
							Result.append(SearcherDblp.searchByRange2(queryString.toLowerCase(), String.valueOf(1998+i),String.valueOf(1998+i)));
							Result.append(",");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							LogTools.log.error(e+"This is a error in Range Image--118");
						}
					}*/
					
				}
				documents=Tools.Transdoc(conn,results.getRecordList());
				request.setAttribute("pageindex", 1);
				if(results.getMaxindex()%25==0)
				{
					request.setAttribute("maxindex", results.getMaxindex()/25);
				}
				else
				{
					request.setAttribute("maxindex", (results.getMaxindex()/25)+1);
				}
				request.setAttribute("sortfuc", searchtype);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("totalnum", results.getRecordCount());
				request.setAttribute("documentlist", documents);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("trend", trend);
				request.setAttribute("start", 0);
				request.setAttribute("end", 0);
			}
			else
			{
				request.setAttribute("totalnum",0);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("documentlist", null);
				request.setAttribute("trend", "");
				request.setAttribute("maxindex",0);
				request.setAttribute("pageindex", 0);
				request.setAttribute("sortfuc", 1);
				request.setAttribute("start", 0);
				request.setAttribute("end", 0);
			}
		}
		else
		{
			request.setAttribute("totalnum",0);
			request.setAttribute("searchtime", (endtime*1.0)/1000);
			request.setAttribute("queryStr", queryString);
			request.setAttribute("documentlist", null);
			request.setAttribute("trend", "");
			request.setAttribute("maxindex",0);
			request.setAttribute("pageindex", 0);
			request.setAttribute("sortfuc", 1);
			request.setAttribute("start", 0);
			request.setAttribute("end", 0);
		}
		try {
			
			request.getRequestDispatcher("../yhunfh2qvd3q423ewr.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			LogTools.log.error(e+"This is a error in ServletException--174");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogTools.log.error(e+"This is a error in ServletException--177");
		}
		ConnectionUtil.closeConnection(conn);
	}
}
