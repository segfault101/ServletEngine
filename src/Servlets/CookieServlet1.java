package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class CookieServlet1 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Cookie c = new Cookie("TestCookie", "54321");
		c.setMaxAge(36000);
		response.addCookie(c);
		
		/*Cookie d = new Cookie("TestCookie2", "12345");
		c.setMaxAge(36000);
		response.addCookie(d);*/

		//response.setStatus(408);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Cookie Servlet 1</TITLE></HEAD><BODY>");
		out.println("<P>Added cookie (TestCookie,54321) to response.</P>");
		out.println("<P>Continue to <A HREF=\"cookie2\">Cookie Servlet 2</A>.</P>");
		out.println("</BODY></HTML>");	
		
		//response.reset();
		//response.flushBuffer();
	}
}