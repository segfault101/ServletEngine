package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class CookieServlet3 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		Cookie c = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; ++i) {
				if (cookies[i].getName().equals("TestCookie")) {
					c = cookies[i];
				}
			}
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Cookie Servlet 3</TITLE></HEAD><BODY>");
		if (c == null) {
			out.println("<P>Cookie 'TestCookie' had been sucessfully deleted by client.</P>");
		} else {
			out.println("<P>Uh-oh, cookie 'TestCookie' is still on client!</P>");
		}
		out.println("</BODY></HTML>");
	}
}
