package com.dblp.servlet;

import com.dblp.dao.FeebackDao;
import com.dblp.util.ConnectionUtil;
import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeebackServlet extends HttpServlet {

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
	 private static final String ALLOWEDFILESLIST = "jpg,gif,bmp,JPG,GIF,BMP";
	  
	    /** 
	     * 允许图片的大小 
	     */  
	private static final long MAXSIZE = 2 * 1024 * 1024; 
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
	//	response.setContentType("text/html");
        String messages = "";
        String forward = "";
        // 获取pageContext  
        PageContext pageContext = JspFactory.getDefaultFactory()  
                .getPageContext(this, request, response, null, true,  
                        8192, true);  
        SmartUpload upload = new SmartUpload();  
        try {  
            // 初始化  
            upload.initialize(pageContext);  
            // 限制上传文件的大小  
            upload.setMaxFileSize(MAXSIZE);  
            // 设置允许上传的文件类型  
            upload.setAllowedFilesList(ALLOWEDFILESLIST);  
            // 准备上传  
            upload.upload();  
            String title=upload.getRequest().getParameter("title");
    		String content=upload.getRequest().getParameter("content");
            // 系统时间做为文件名  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String sysDate = sdf.format(new Date());
            // 保存文件  
            for (int i = 0; i < upload.getFiles().getCount(); i++) {  
                File file = upload.getFiles().getFile(i);  
                // 如果选择了文件  
                if (!file.isMissing()) {  
                    // 获取图片的拓展名  
                    String ext = upload.getFiles().getFile(i).getFileExt();
                    // 为文件重新命名  
                    String fileName = sysDate + "." + ext;
                    String pathss="G:" + java.io.File.separator + "DBLP\\Feeback"+ java.io.File.separator + fileName;

                   /// System.out.println(pathss);
                    file.saveAs(pathss, File.SAVEAS_PHYSICAL);
                    Connection conn=ConnectionUtil.getConnection();
                    new FeebackDao().saveFeeback(conn, title, content, pathss);
                    ConnectionUtil.closeConnection(conn);
                }
                forward = "../index.jsp";
            }
        	response.getWriter().print("<script type=\"text/javascript\">" + "提交成功！" + "</script>");
        } catch (java.lang.SecurityException e) {
        	response.getWriter().print("<script type=\"text/javascript\">" + "提交失败！" + "</script>");
            forward = "../index.jsp";
        } catch (SmartUploadException e) {  
            messages = "上传文件失败！";
            response.getWriter().print("<script type=\"text/javascript\">" + "提交失败！" + "</script>");
            e.printStackTrace();  
        }  
     ///   request.setAttribute("messages", messages);  
		

        request.getRequestDispatcher(forward).forward(request, response); 
	}

}
