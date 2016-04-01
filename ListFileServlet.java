package Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListFileServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ListFileServlet() {
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

		//获取上传文件的目录
		String uploadfilePath= this.getServletContext().getRealPath("/files");
		System.out.println(uploadfilePath);
		//存储要下载的文件名
		Map<String,String> filenameMap=new HashMap<String, String>();
		//递归遍历目录下的所有文件和目录，将文件的文件名存储到Map中
		listfile(new File(uploadfilePath),filenameMap);
		request.setAttribute("filenameMap", filenameMap);
		request.getRequestDispatcher("../listfile.jsp").forward(request, response);
	}

	private void listfile(File file, Map<String, String> filenameMap) {
		// TODO Auto-generated method stub
		if(!file.isFile())
		{
			File files[]=file.listFiles();
			for(File f:files)
			{
				listfile(f,filenameMap);
			}
		}
		else
		{
			filenameMap.put(file.getName(),file.getName());
		}
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
