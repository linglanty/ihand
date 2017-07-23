package com.dblp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dblp.dao.UserDao;
import com.dblp.model.User;

public class UserServlet extends HttpServlet {

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
		String pathInfo = request.getPathInfo();
		if (pathInfo.contains("getUserByName")) {
			getUserByName(request,response);
		}

		if (pathInfo.contains("getUserByIdentifyCode")) {
			getUserByIdentifyCode(request,response);
		}
	}

	private void  getUserByName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		UserDao userDao = new UserDao();
		User user = userDao.GetUser(null, Integer.parseInt(userName));
		response.getWriter().write(user.toString());
	}

	private void  getUserByIdentifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String identifyCode = request.getParameter("identifyCode");
		UserDao userDao = new UserDao();
		User user = userDao.GetUserByIdentifyCode(null, identifyCode);
		response.getWriter().write(user.toString());
	}
}
