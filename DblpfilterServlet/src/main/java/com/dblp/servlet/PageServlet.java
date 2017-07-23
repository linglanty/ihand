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
import com.dblp.model.Documents;
import com.dblp.model.QueryResults;
import com.dblp.util.ConnectionUtil;
import com.dblp.util.LogTools;
import com.dblp.util.Tools;

public class PageServlet extends HttpServlet {

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
		LogTools.log.error(e1+"This is a error in Page not UnsupportedEncodingException --39");
		}
		response.setCharacterEncoding("GBK");
		String queryString=request.getParameter("query").trim();
		String pageStr=request.getParameter("pageindex").trim();
		String searchtype=request.getParameter("sortfuc").trim();
		String trend=request.getParameter("trend");
		String start=null;
		String end=null;
		if(!pageStr.matches("^[0-9]*$")||!searchtype.matches("^[0-9]*$"))
		{
			return;
		}
		if(queryString==null||queryString=="")
		{
			try {
				response.getWriter().println("<script>alert('Please input the search keywords!!')</scirpt>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Page IOException -56");
			}
			try {
				request.getRequestDispatcher("../index.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
			//	LogTools.log.error(e+"This is a error in Page ServletException -62");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Page IOException -65");
			}
			return;
		}
		
		QueryResults results=null;
		List<Documents> documents=null;
		
		long starttime= System.currentTimeMillis();
		int pageint= Integer.parseInt(pageStr)-1;
		Connection conn=ConnectionUtil.getConnection();

		if(searchtype.equals("1"))
		{
			String field=null;
			Cookie mycookies[] = request.getCookies();
			boolean islogin=false;
			boolean isAction=false;
			int userid=0;
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
			if(field==null)
			{
				try {
					results=SearcherDblp.search(true,queryString.toLowerCase(),pageint*25,25);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogTools.log.error(e+"This is a error in Page not having logined --76");
				}
			}
			else
			{
				try {
					results=SearcherDblp.search(conn,userid,true,queryString.toLowerCase(),pageint*25,25,field);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				//	LogTools.log.error(e+"This is a error in Page having logined --85");
				}
			}
			/*try {
				results=SearcherDblp.search(queryString, pageint*10, 10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		else if(searchtype.equals("2"))
		{
			try {
				results=SearcherDblp.searchByImporIndex(true,conn,queryString.toLowerCase(), pageint*25, 25);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Page Important Search --101");
			}
		}
		else if(searchtype.equals("3"))
		{
			try {
				results=SearcherDblp.searchByTime(true,conn,queryString.toLowerCase(),pageint*25, 25);
			} catch (Exception e) {
				LogTools.log.error(e+"This is a error in Page Time Search --109");
			}
		}
		else if(searchtype.equals("4"))
		{
			try {
				start=request.getParameter("start").trim();
				end=request.getParameter("end").trim();
				results=SearcherDblp.searchByRange(queryString.toLowerCase(),start,end,pageint*25, 25);
			} catch (Exception e) {
			//	LogTools.log.error(e+"This is a error in Page Time Search --109");
			}
		}
		else
		{
			return;
		}
		long endtime= System.currentTimeMillis()-starttime;
	//	int resultnum=results.getRecordCount();
		if(results!=null)
		{
			if(results.getRecordCount()>0)
			{
				documents=Tools.Transdoc(conn,results.getRecordList());
				request.setAttribute("pageindex", Integer.parseInt(pageStr));
				
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
				if(start==null)
				{
					request.setAttribute("start", 0);
				}
				else
				{
					request.setAttribute("start", start);
				}
				if(end==null)
				{
					request.setAttribute("end", 0);
				}
				else
				{
					request.setAttribute("end",end);
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
		//	LogTools.log.error(e+"This is a error in Page ServletException -181");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogTools.log.error(e+"This is a error in Page IOException -184");
		}
		ConnectionUtil.closeConnection(conn);
	}
		
}
