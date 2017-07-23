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

public class RegServlet extends HttpServlet {

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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int isauto=0;
		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String fristname=request.getParameter("fristname");
		String lastname=request.getParameter("lastname");
		String username=fristname.trim().concat(" "+lastname);
		String pwd=request.getParameter("pwd");
		String email=request.getParameter("email");
		String[] res=request.getParameterValues("area");
		String[] areaOther=request.getParameterValues("areaOther");
		String resfield=null;
		String areaString=null;
		if(request.getParameter("isauto")==null)
			isauto=0;
		else
			isauto=1;
		
		if(areaOther!=null)
		{
			areaString=areaOther[0];
			for(int i=1;i<areaOther.length;i++)
			{
				areaString=areaString.concat("###"+areaOther[i]);
			}
		}
		
		if(res!=null)
		{
			resfield=res[0];
			for(int i=1;i<res.length;i++)
			{
				resfield=resfield.concat("#"+res[i]);
			}
		}
		User user=new User();
		user.setName(username);
		user.setEmail(email);
		user.setPwd(pwd);
		user.setAreaother(areaString);
		user.setResfield(resfield);
		user.setIsauto(isauto);
		UserDao userdao=new UserDao();
		Connection conn=ConnectionUtil.getConnection();
		if(userdao.saveUser(conn,user))
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
			response.getWriter().println("<script>alert('Congraulations! You have login up Successfully.')</scirpt>");
			request.getRequestDispatcher("../index.jsp").forward(request, response);
		}
		else
		{
			response.getWriter().println("<script>alert('Failure! You should try again!')</scirpt>");
			request.getRequestDispatcher("../index.jsp").forward(request, response);
		}
		ConnectionUtil.closeConnection(conn);
	}

	
}
