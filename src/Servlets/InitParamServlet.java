package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class InitParamServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		ServletConfig config = getServletConfig();
		ServletContext context = config.getServletContext();
		out.println("<HTML><HEAD><TITLE>Simple Servlet</TITLE></HEAD><BODY>");
		out.println("<P>The value of 'webmaster' is: " + 
				context.getInitParameter("webmaster") + "</P>");
		out.println("<P>The value of 'TestParam' is: " + 
				config.getInitParameter("TestParam") + "</P>");
		out.println("</BODY></HTML>");		
	}
}
