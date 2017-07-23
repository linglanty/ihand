package com.dblp.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dblp.dao.UserDao;
import com.dblp.model.User;
import com.dblp.util.ConnectionUtil;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String email=request.getParameter("email");
		String pwd=request.getParameter("pwd");
		String item=request.getParameter("remb");
		UserDao userdao=new UserDao();
		Connection conn=ConnectionUtil.getConnection();
		User user=userdao.login(conn,email, pwd);
		ConnectionUtil.closeConnection(conn);
		if(user!=null)
		{
			if(item!=null)//是否记住登录状态
			{
				String cookieName = "dblpuser";
				Cookie cookie = new Cookie(cookieName, user.getName()+"#"+user.getId());
				cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
				cookie.setPath(request.getContextPath());//设置路径
				response.addCookie(cookie);
				
				if(user.getResfield()!=null)
				{
					Cookie cookiearea = new Cookie("dblpArea",user.getResfield());
					cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
				else
				{
					Cookie cookiearea = new Cookie("dblpArea",null);
					cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
					
				}
			}
			else
			{
				String cookieName = "dblpuser";
				Cookie cookie = new Cookie(cookieName, user.getName()+"#"+user.getId());
				cookie.setMaxAge(3600);//设置Cookie的过期时间
				cookie.setPath(request.getContextPath());//设置路径
				response.addCookie(cookie);
				
				if(user.getResfield()!=null)		//用户所在领域cookie
				{
					Cookie cookiearea = new Cookie("dblpArea",user.getResfield());
					cookie.setMaxAge(3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
				else
				{
					Cookie cookiearea = new Cookie("dblpArea",null);
					cookie.setMaxAge(3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
			}
			response.getWriter().print("Yes");
		}
		else
		{
			response.getWriter().print("No");
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String email=request.getParameter("email");
		String pwd=request.getParameter("pwd");
		String item=request.getParameter("remb");
		UserDao userdao=new UserDao();
		Connection conn=ConnectionUtil.getConnection();
		User user=userdao.login(conn,email, pwd);
		ConnectionUtil.closeConnection(conn);
		if(user!=null)
		{
			if(item!=null)//是否记住登录状态
			{
				String cookieName = "dblpuser";
				Cookie cookie = new Cookie(cookieName, user.getName()+"#"+user.getId());
				cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
				cookie.setPath(request.getContextPath());//设置路径
				response.addCookie(cookie);
				
				if(user.getResfield()!=null)
				{
					Cookie cookiearea = new Cookie("dblpArea",user.getResfield());
					cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
				if(user.getAreaother()!=null)
				{
					String areaOther = "areaOther";
					Cookie CookieareaOther = new Cookie(areaOther, user.getAreaother());
					cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(CookieareaOther);
				}
				else
				{
					String areaOther = "areaOther";
					Cookie CookieareaOther = new Cookie(areaOther, "nothing");
					cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(CookieareaOther);
				}
			}
			else
			{//'areaOther'
				String cookieName = "dblpuser";
				Cookie cookie = new Cookie(cookieName, user.getName()+"#"+user.getId());
				cookie.setMaxAge(3600);//设置Cookie的过期时间
				cookie.setPath(request.getContextPath());//设置路径
				response.addCookie(cookie);
				if(user.getAreaother()!=null)
				{
					String areaOther = "areaOther";
					Cookie CookieareaOther = new Cookie(areaOther, user.getAreaother());
					cookie.setMaxAge(3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(CookieareaOther);
				}
				else
				{
					String areaOther = "areaOther";
					Cookie CookieareaOther = new Cookie(areaOther, "nothing");
					cookie.setMaxAge(0);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(CookieareaOther);
				}
				if(user.getResfield()!=null)		//用户所在领域cookie
				{
					Cookie cookiearea = new Cookie("dblpArea",user.getResfield());
					cookie.setMaxAge(3600);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
				else
				{
					Cookie cookiearea = new Cookie("dblpArea","nothing");
					cookie.setMaxAge(0);//设置Cookie的过期时间
					cookie.setPath(request.getContextPath());//设置路径
					response.addCookie(cookiearea);
				}
			}
			response.sendRedirect("../index.jsp");
		}
		else
		{
			response.sendRedirect("../signin.html");
		}
	
	}
	
	
}
