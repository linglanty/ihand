package com.dblp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PerconMana extends HttpServlet {

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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("GBK");
		String admin=request.getParameter("admin");
		String pwd=request.getParameter("pwd");
		
		if(admin.equals("admin")&&pwd.equals("130110"))
		{
			request.getSession().setAttribute("dblpmanager","dblpadmin");
			PrintWriter out = response.getWriter();
			out.print("<script>alert(\"Login Success!\"); </script>");
			response.sendRedirect("../ebrwqqrerhwhqwrqh12.html");
		}
		else
		{
			response.getWriter().println("<script>alert('Failure!!')</scirpt>");
			response.sendRedirect("../Signin.html");
		}
	}
}
