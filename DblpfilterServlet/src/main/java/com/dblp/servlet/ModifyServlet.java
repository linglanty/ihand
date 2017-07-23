package com.dblp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dblp.dao.PerconfDao;
import com.dblp.model.Pearconf;
import com.dblp.util.ConnectionUtil;

import net.sf.json.JSONArray;

public class ModifyServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * PerconfDao2
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/xml;character=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		request.setCharacterEncoding("UTF-8");
		Pattern pattern = Pattern.compile("[0-9]*");
		HttpSession session = request.getSession();
		if(session.getAttribute("dblpmanager")==null)
		{
			response.getWriter().write("~~~~");
			return;
		}
		String action=request.getParameter("action").trim();
		Connection conn=ConnectionUtil.getConnection();
		if(action.equals("show"))
		{
			String param=request.getParameter("param").trim();
		    List<Pearconf> pearconfs=null;
			if( pattern.matcher(param).matches())
			{
				int field= Integer.parseInt(param);
				if(field>0&&field<=10)
				{
					pearconfs=new PerconfDao().Getperconf(conn,field);
				}
			}
			
			JSONArray json=null;
			if(pearconfs!=null)
			{
				json=JSONArray.fromObject(pearconfs);
				 response.getWriter().write(json.toString());
			}
			else
			{
				response.getWriter().write("---+");
			}
		    
		  //  response.getWriter().write(json.toString());
		}
		else if(action.equals("showPer"))
		{
			String pid=request.getParameter("pid").trim();
			Pearconf pearconf=null;
			if( pattern.matcher(pid).matches())
			{
				int id= Integer.parseInt(pid);
				pearconf=new PerconfDao().GetperconfByid(conn,id);
			}
			else
			{
				request.getRequestDispatcher("../ModifyPerconf.html").forward(request, response);
			}
			JSONArray json=null;
			if(pearconf!=null)
			{
				json=JSONArray.fromObject(pearconf);
				response.getWriter().write(json.toString());
			}
			else
			{
				response.getWriter().write("---+");
			}
		}
		else if(action.equals("update"))
		{
			String pid=request.getParameter("pid").trim();
			String abbr=request.getParameter("abbr").trim();
			String dblpabbr=request.getParameter("dblpabbr").trim();
			String fullname=request.getParameter("fullname").trim();
			String grade=request.getParameter("grade").trim();
			Pearconf perconf=new Pearconf();
			perconf.setAbbr(abbr);
			perconf.setPid(Integer.parseInt(pid));
			perconf.setDblpabbr(dblpabbr);
			perconf.setFullname(fullname);
			perconf.setGrade(Integer.parseInt(grade));
			if(new PerconfDao().Updatperconf(conn,perconf))
			{
				response.getWriter().write("YES");
			}
			else
			{
				response.getWriter().write("NO");
			}
		}
		else if(action.equals("delete"))
		{
			String pid=request.getParameter("pid").trim();
			 
			Pearconf pearconf=null;
			if( pattern.matcher(pid).matches())
			{
				int id= Integer.parseInt(pid);
				if(new PerconfDao().Deleteperconf(conn, Integer.parseInt(pid)))
				{
					response.getWriter().write("YES");
				}
				else
				{
					response.getWriter().write("NO");
				}
			}
			else
			{
				response.getWriter().write("---+");
			}
		}
		else if(action.equals("addnew"))
		{
			String abbr=request.getParameter("abbr").trim();
			String dblpabbr=request.getParameter("dblpabbr").trim();
			String fullname=request.getParameter("fullname").trim();
			String grade=request.getParameter("grade").trim();
			String areaString=request.getParameter("field").trim();
			String typeString=request.getParameter("type").trim();
			String publisher=request.getParameter("publisher").trim();
			//Abbr,Dblpabbr,Fullname,Area,grade,Itype,Publisher
			Pearconf perconf=new Pearconf();
			perconf.setAbbr(abbr);
			perconf.setDblpabbr(dblpabbr);
			perconf.setFullname(fullname);
			if(grade!=null&&grade!=""&&pattern.matcher(grade).matches())
			{
				perconf.setGrade(Integer.parseInt(grade));
			}
			if(areaString!=null&&areaString!=""&&pattern.matcher(areaString).matches())
			{
				perconf.setArea(Integer.parseInt(areaString));
			}
			if(typeString!=null&&typeString!=""&&pattern.matcher(typeString).matches())
			{
				perconf.setType(Integer.parseInt(typeString));
			}
			perconf.setPublisher(publisher);
			if(new PerconfDao().Createperconf(conn,perconf))
			{
				response.getWriter().write("YES");
			}
			else
			{
				response.getWriter().write("NO");
			}
		}
		ConnectionUtil.closeConnection(conn);
	}
	
	

}
