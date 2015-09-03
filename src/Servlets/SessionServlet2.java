package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SessionServlet2 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String val = (String) session.getAttribute("TestAttribute");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Session Servlet 1</TITLE></HEAD><BODY>");
		out.println("<P>TestAttribute value is '" + val + "'.</P>");
		session.invalidate();
		out.println("<P>Session invalidated.</P>");
		out.println("<P>Continue to <A HREF=\"session3\">Session Servlet 3</A>.</P>");
		out.println("</BODY></HTML>");		
	}
}
