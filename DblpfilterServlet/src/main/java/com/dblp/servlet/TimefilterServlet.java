package com.dblp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dblp.Searcher.SearcherDblp;
import com.dblp.model.Documents;
import com.dblp.model.QueryResults;
import com.dblp.util.ConnectionUtil;
import com.dblp.util.LogTools;
import com.dblp.util.Tools;

public class TimefilterServlet extends HttpServlet {
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String queryString=request.getParameter("query").trim();
		String fucString=request.getParameter("fuc");
		String trend=request.getParameter("trend");
		//String pageStr=request.getParameter("pageindex").trim();
		QueryResults results=null;
		List<Documents> documents=null;
		long starttime= System.currentTimeMillis();
		int stars=0;
		int ends=0;
		if(fucString.equals("13"))
		{
			try {
				results=SearcherDblp.searchByRange(queryString, "2012","2088",0, 25);
				stars=2012;
				ends=2088;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search --48");
			}
		}
		else if(fucString.equals("12"))
		{
			try {
				results=SearcherDblp.searchByRange(queryString, "2011","2088",0, 25);
				stars=2011;
				ends=2088;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search --58");
			}
		}
		else if(fucString.equals("09"))
		{
			try {
				results=SearcherDblp.searchByRange(queryString, "2008","2088", 0, 25);
				stars=2008;
				ends=2088;
			} catch (Exception e) {
				LogTools.log.error(e+"This is a error in Timefilter Search --66");
			}
		}
		else
		{
			response.getWriter().println("<script>alert('Please input the search keywords!!')</scirpt>");
			request.getRequestDispatcher("../index.jsp").forward(request, response);
			return;
		}
		long endtime= System.currentTimeMillis()-starttime;
		if(results!=null)
		{
			if(results.getRecordCount()>0)
			{
				Connection conn=ConnectionUtil.getConnection();
				documents=Tools.Transdoc(conn,results.getRecordList());
				ConnectionUtil.closeConnection(conn);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("totalnum", results.getRecordCount());
				request.setAttribute("documentlist", documents);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("pageindex", 1);
				if(results.getRecordCount()%25==0)
				{
					request.setAttribute("maxindex", results.getRecordCount()/25);
				}
				else
				{
					request.setAttribute("maxindex", (results.getRecordCount()/25)+1);
				}
				request.setAttribute("sortfuc", 4);
				request.setAttribute("trend", trend);
				request.setAttribute("start", stars);
				request.setAttribute("end", ends);
			}
			else
			{
				request.setAttribute("totalnum",0);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("pageindex", 0);
				request.setAttribute("maxindex", 0);
				request.setAttribute("sortfuc", 4);
				request.setAttribute("trend", trend);
				request.setAttribute("start", 0);
				request.setAttribute("end", 0);
			}
		}
		else
		{
			request.setAttribute("totalnum",0);
			request.setAttribute("searchtime", (endtime*1.0)/1000);
			request.setAttribute("queryStr", queryString);
			request.setAttribute("pageindex", 0);
			request.setAttribute("maxindex", 0);
			request.setAttribute("sortfuc", 4);
			request.setAttribute("trend", trend);
			request.setAttribute("start", 0);
			request.setAttribute("end", 0);
		}
		request.getRequestDispatcher("../yhunfh2qvd3q423ewr.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String queryString=request.getParameter("query").trim();
		String lowval=request.getParameter("lowval");
		String upval=request.getParameter("upval");
		String trend=request.getParameter("trend");
		QueryResults results=null;
		List<Documents> documents=null;
		long starttime= System.currentTimeMillis();
		if(lowval==null&&upval==null)
		{
			try {
				results=SearcherDblp.search(true,queryString, 0, 25);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search from a time to a time  --128");
			}
		}
		else if(lowval==null&&upval!=null)
		{
			try {
				
				results=SearcherDblp.searchByRange(queryString, "0000",Tools.convertTime(upval,2) ,0, 25);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search all --139");
			}
			
		}
		else if(lowval!=null&&upval==null)
		{
			try {
				results=SearcherDblp.searchByRange(queryString, Tools.convertTime(lowval,1),"2088" ,0, 25);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search from a time to the lastest --149");
			}
		}
		else
		{
			try {
				results=SearcherDblp.searchByRange(queryString, Tools.convertTime(lowval,1),Tools.convertTime(upval,2),0, 25);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTools.log.error(e+"This is a error in Timefilter Search from a time a time --158");
			}
		}
		long endtime= System.currentTimeMillis()-starttime;
		if(results!=null)
		{
			if(results.getRecordCount()>0)
			{
				Connection conn= ConnectionUtil.getConnection();
				documents=Tools.Transdoc(conn,results.getRecordList());
				ConnectionUtil.closeConnection(conn);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("totalnum", results.getRecordCount());
				request.setAttribute("documentlist", documents);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("pageindex", 1);
				if(results.getRecordCount()%25==0)
				{
					request.setAttribute("maxindex", results.getRecordCount()/25);
				}
				else
				{
					request.setAttribute("maxindex", (results.getRecordCount()/25)+1);
				}
				request.setAttribute("sortfuc", 4);
				request.setAttribute("trend", trend);
				request.setAttribute("start", lowval);
				request.setAttribute("end", upval);
			}
			else
			{
				request.setAttribute("totalnum",0);
				request.setAttribute("searchtime", (endtime*1.0)/1000);
				request.setAttribute("queryStr", queryString);
				request.setAttribute("pageindex", 0);
				request.setAttribute("maxindex", 0);
				request.setAttribute("sortfuc", 4);
				request.setAttribute("trend", trend);
				request.setAttribute("start", 0);
				request.setAttribute("end", 0);
			}
		}
		else
		{
			request.setAttribute("totalnum",0);
			request.setAttribute("searchtime", (endtime*1.0)/1000);
			request.setAttribute("queryStr", queryString);
			request.setAttribute("pageindex", 0);
			request.setAttribute("maxindex", 0);
			request.setAttribute("sortfuc", 4);
			request.setAttribute("trend", trend);
			request.setAttribute("start", 0);
			request.setAttribute("end", 0);
			
		}
		request.getRequestDispatcher("../yhunfh2qvd3q423ewr.jsp").forward(request, response);
	}

}
