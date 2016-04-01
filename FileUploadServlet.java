package Servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet {
	

	/**
	 * Constructor of the object.
	 */
	public FileUploadServlet() {
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
		
		request.setCharacterEncoding("utf-8");
		//创建文件处理工厂，用于生成fileitem对象
		DiskFileItemFactory factory=new DiskFileItemFactory();
		
		//设置缓存大小，如果超过大小，则使用临时目录存储
		factory.setSizeThreshold(1024*1024);
		
		//设置临时目录，此目录下的文件需要手动删除
		String dir=this.getServletContext().getRealPath("/");//当前项目在服务器的路径
		System.out.println(dir);
		File filedir=new File(dir+"filetemp");//临时文件夹
		if(!(filedir.exists()))
			filedir.mkdir();
		factory.setRepository(filedir);
		
		//设置文件实际保存的目录
		String userdir=dir+"files";
		File fudir=new File(userdir);
		if(!(fudir.exists()))
			fudir.mkdir();
		
		//创建request的解析器，将数据封装到fileitem对象中
		ServletFileUpload upload=new ServletFileUpload(factory);
		
		upload.setProgressListener(new ProgressListener() {
			
			long temp=-1;
			@Override
			public void update(long pBytesRead, long pContentLength, int pltems) {
				// TODO Auto-generated method stub
				long size=pBytesRead/1024*1024*10;
				if(temp==size)
					return;
				temp=size;
				if(pBytesRead!=-1)
					System.out.println("已上传："+pBytesRead+" 总大小："+pContentLength);
				else
				{
					System.out.println("上传完成");
				}
			}
		});
		//解析保存在request的数据并返回list集合
		List list=null;
		try
		{
			list=upload.parseRequest(request);
		}
		catch(FileUploadException e)
		{
			e.printStackTrace();
		}
		//遍历list集合，取出每一个输入项的fileitem对象，并分别获取数据
		Iterator iterator=list.iterator();
		while(iterator.hasNext())
		{
			FileItem fi=(FileItem)iterator.next();
			if(fi.isFormField())
			{
				System.out.println("表单名："+fi.getFieldName());
				System.out.println("表单："+fi.getString());
			}
			else if(fi.getName()!=null && !(fi.getName().equals("")))
			{
				//由于客户端向服务器发送的文件路径是全路径，因此只需要文件名即可
				String filename=fi.getName();
				System.out.println("文件名:"+filename);
				System.out.println("文件大小:"+fi.getSize());
				System.out.println("文件类型:"+fi.getContentType());
				
				File file=new File(fudir,filename);
				try {
					fi.write(file);
					request.setAttribute("upload.message", "上传文件成功！");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*
				int index=filename.lastIndexOf("\\");
				if(index!=-1)
				{
					filename=filename.substring(index+1);
				}
				
				//向服务器写出文件
				InputStream in=fi.getInputStream();
				FileOutputStream fos=new FileOutputStream(fudir+"/"+filename);
				byte[] buf=new byte[1024];
				int len=-1;
				while((len=in.read(buf))!=-1)
				{
					fos.write(buf, 0, len);
				}
				//关闭流
				if(in!=null)
				{
					try
					{
						in.close();
					}
					finally
					{
						if(fos!=null)
							fos.close();
					}
				}
				response.sendRedirect("../uploadResult.jsp");
				*/
			}
			else
				request.setAttribute("upload.message", "上传文件失败！");
				
		}
		request.getRequestDispatcher("../uploadResult.jsp").forward(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		
	}

}
