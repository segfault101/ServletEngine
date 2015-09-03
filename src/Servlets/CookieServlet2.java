package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class CookieServlet2 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		Cookie c = null;
		for (int i = 0; i < cookies.length; ++i) {
			if (cookies[i].getName().equals("TestCookie")) {
				c = cookies[i];
			}
		}
		if (c != null) {
			c.setMaxAge(0);
			response.addCookie(c);
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Cookie Servlet 2</TITLE></HEAD><BODY>");
		if (c == null) {
			out.println("<P>Couldn't retreive value for cookie with name 'TestCookie'.</P>");
		} else {
			out.println("<P>Retrieved value '" + c.getValue() + "' from cookie with name 'TestCookie'.</P>");
			out.println("<P>Deleted cookie (TestCookie,54321) in response.</P>");
		}
		out.println("<P>Continue to <A HREF=\"cookie3\">Cookie Servlet 3</A>.</P>");
		out.println("</BODY></HTML>");
	}
}
