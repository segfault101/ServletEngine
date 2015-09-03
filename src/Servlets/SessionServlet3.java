package Servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SessionServlet3 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Session Servlet 1</TITLE></HEAD><BODY>");
		if (session == null) {
			out.println("<P>Session successfully invalidated.</P>");
		} else {
			out.println("<P>Uh-oh, session still exists!</P>");
		}
		out.println("</BODY></HTML>");		
	}
}
