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

public class UserManaServlet extends HttpServlet {

	
	/**
	 * 
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

		response.setContentType("text/html");			//显示用户信息页面！！
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String action=request.getParameter("action");
		String userid=request.getParameter("userid");
		if(action==null)
		{
			response.getWriter().print("Err");
			return;
		}
		if(action.equals("showuser"))
		{
			String usernam;
			int i;
			if(userid!=null)
			{
				if(!userid.matches("^[0-9]*$"))
				{
					response.getWriter().print("<script>alert('Warning!! Don't try any other userid')</scirpt>");
					request.getRequestDispatcher("../index.jsp").forward(request, response);
					return;
				}
				Cookie mycookies[] = request.getCookies();
				if(mycookies.length>0) 
				{
					for (i= 0; i < mycookies.length; i++) 
					{
						if ("dblpuser".equalsIgnoreCase(mycookies[i].getName().trim())) 
						{
							usernam=mycookies[i].getValue();
							String[] users=usernam.split("#");
							if(users[1].equals(userid))
							{
								break;
							}
							else
							{
								response.getWriter().println("<script>alert('Warning!! Don't try any other userid')</scirpt>");
								request.getRequestDispatcher("../index.jsp").forward(request, response);
								return;
							}
						}
					}
					if(i==mycookies.length)
					{
						response.getWriter().println("<script>alert('Warning!! Don't try any other userid!!')</scirpt>");
						request.getRequestDispatcher("../index.jsp").forward(request, response);
						return;
					}
				}
				UserDao userdao=new UserDao();
				Connection conn= ConnectionUtil.getConnection();
				User user=userdao.GetUser(conn, Integer.parseInt(userid));
				ConnectionUtil.closeConnection(conn);
				if(user!=null)
				{
					request.setAttribute("name", user.getName());
					request.setAttribute("isauto",user.getIsauto());
					if(user.getResfield()!=null)
					{
						String cookieName = "dblpArea";
						Cookie cookie = new Cookie(cookieName, user.getResfield());
						cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
						cookie.setPath(request.getContextPath());//设置路径
						response.addCookie(cookie);
					}
					else
					{
						String cookieName = "dblpArea";
						Cookie cookie = new Cookie(cookieName, null);
						cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
						cookie.setPath(request.getContextPath());//设置路径
						response.addCookie(cookie);
					}
					if(user.getAreaother()!=null)
					{
						String cookieName = "areaOther";
						Cookie cookie = new Cookie(cookieName, user.getAreaother());
						cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
						cookie.setPath(request.getContextPath());//设置路径
						response.addCookie(cookie);
					}
					else
					{
						String cookieName = "areaOther";
						Cookie cookie = new Cookie(cookieName, "nothing");
						cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
						cookie.setPath(request.getContextPath());//设置路径
						response.addCookie(cookie);
					}
					request.setAttribute("id", user.getId());
					request.setAttribute("email", user.getEmail());
					request.getRequestDispatcher("../Show.jsp").forward(request, response);
				}
			}
			else
			{
				response.getWriter().println("<script>alert('Failure!!')</scirpt>");
				request.getRequestDispatcher("../index.jsp").forward(request, response);

			}
		}
		else if(action.equals("changepwd"))
		{
			String newpassword=request.getParameter("pwd");
			if(userid.matches("^[0-9]*$"))
			{
				UserDao userdao=new UserDao();
				Connection conn= ConnectionUtil.getConnection();
				if(userdao.ChangePassword(conn, Integer.parseInt(userid), newpassword))
				{
					response.getWriter().print("Yes");
				}
				else
				{
					response.getWriter().print("No");
				}
				ConnectionUtil.closeConnection(conn);
			}
			else
			{
				response.getWriter().print("Err");
			} 
		}
		else
		{
			response.getWriter().print("Err");
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String userid=request.getParameter("userid");
		String username=request.getParameter("username");
		String radio=request.getParameter("selectpush");
		String checkggroup=request.getParameter("checkggroup").replaceAll(",", "#");
		String areaOther=request.getParameter("areaOther").replaceAll(",", "#");
		User user=new User();
		if(areaOther!=null&&areaOther!="")
		{
			user.setAreaother(areaOther);
		}
		
		if(checkggroup!=null&&checkggroup!="")
		{
			user.setResfield(checkggroup);
		}
		if(radio!=null)
		{
			if(radio.equals("yes"))
			{
				user.setIsauto(1);
			}
			else
			{
				user.setIsauto(0);
			}
		}
		if(username!=""&&username!=null)
		{
			user.setName(username);
		}
		user.setId(Integer.parseInt(userid));
		UserDao userdao=new UserDao();
		Connection conn=ConnectionUtil.getConnection();
		if(userdao.updateUser(conn,user))
		{
			String cookieName = "dblpuser";
			Cookie cookie = new Cookie(cookieName, user.getName()+"#"+user.getId());
			cookie.setMaxAge(14 * 24 * 3600);//设置Cookie的过期时间
			cookie.setPath(request.getContextPath());//设置路径
			response.addCookie(cookie);
			response.getWriter().print("Yes");
		}
		else
		{
			response.getWriter().print("No");		
		}
		ConnectionUtil.closeConnection(conn);
	}
	
}
