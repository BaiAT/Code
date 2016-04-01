package Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloadServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FileDownloadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取下载的文件名
		String filename=request.getParameter("filename");
		filename= new String (filename.getBytes("iso8859-1"),"utf-8");
		System.out.println(filename);
		//获取文件保存的路径
		String filesavePath=this.getServletContext().getRealPath("/files");
		System.out.println(filesavePath);
		File file=new File(filesavePath+"\\"+filename);
		if(!file.exists())
		{
			request.setAttribute("message", "你要下载的文件不存在！");
			request.getRequestDispatcher("../message.jsp").forward(request, response);
			return;
		}
		
		//设置响应头控制浏览器下载文件
		response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(filename,"utf-8"));
		//读取要下载的文件，保存文件到输入流
		FileInputStream in=new FileInputStream(filesavePath+"\\"+filename);
		//创建输出流
		OutputStream out=response.getOutputStream();
		//创建缓冲区
		byte buffer[]=new byte[1024];
		int len=0;
		//循环将输入流中的内容读取到缓冲区中
		while((len=in.read(buffer))>0)
		{
			//输入缓冲区的内容到浏览器，实现下载
			out.write(buffer,0,len);
		}
		
		//关闭
		in.close();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
