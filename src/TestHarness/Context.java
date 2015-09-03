package TestHarness;
import javax.servlet.*;

import java.io.File;
import java.util.*;

class Context implements ServletContext {
	
	private HashMap<String,Object> attributes;	//Are set in an application, so you don't need to initialise these
	
	private HashMap<String,String> initParams;	//Context param - set when createContext(handler) method is called
	
	public Context() {
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	}
	
	public Object getAttribute(String name) {					//DONE
		return attributes.get(name);
	}
	
	public Enumeration getAttributeNames() {					//DONE
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public ServletContext getContext(String name) {				//DONE - we don't have a context, only one app
		return null;
	}
	
	public String getInitParameter(String name) {				//DONE
		return initParams.get(name);
	}
	
	public Enumeration getInitParameterNames() {				//DONE
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public int getMajorVersion() {
		return 1;												//DONE
	}
	
	
	public String getMimeType(String file) {					//DONE
		return null;
	}
	
	public int getMinorVersion() {								//DONE
		return 0;
	}
	
	public RequestDispatcher getNamedDispatcher(String name) {	//NOT REQUIRED
		return null;
	}
	
	
	//TODO								//NOT IMPLEMENTED DUE TO SECURITY CONCERN
	// Could've done a hack but I rather not until I really need this
	//converts a web content path (the path in the expanded WAR folder structure 
	//on the server's disk file system) to an absolute disk file system path	
	public String getRealPath(String path) {				
		return null;
	}
	
	public RequestDispatcher getRequestDispatcher(String name) { //NOT REQUIRED
		return null;
	}
	
	public java.net.URL getResource(String path) {				//NOT REQUIRED
		return null;
	}
	
	public java.io.InputStream getResourceAsStream(String path) { //NOT REQUIRED
		return null;
	}
	
	public java.util.Set getResourcePaths(String path) {		 //NOT REQUIRED
		return null;
	}
	
	public String getServerInfo() {								// DONE
		return "Rahul's server 1.0b";
	}
	
	public Servlet getServlet(String name) {					//NOT REQUIRED
		return null;
	}
	
	public String getServletContextName() {						//DONE
		return "";
	}
	
	public Enumeration getServletNames() {						//NOT REQUIRED
		return null;
	}
	
	public Enumeration getServlets() {							//NOT REQUIRED
		return null;
	}

	
	public void log(Exception exception, String msg) {		// NOT REQUIRED
		log(msg, (Throwable) exception);
	}
	
	public void log(String msg) {							// NOT REQUIRED
		System.err.println(msg);
	}
	
	public void log(String message, Throwable throwable) {	//NOT REQUIRED
		System.err.println(message);
		throwable.printStackTrace(System.err);
	}
	
	public void removeAttribute(String name) {					//DONE
		attributes.remove(name);
	}
	
	public void setAttribute(String name, Object object) {		//DONE
		if(object != null)
			attributes.put(name, object);
		else
			removeAttribute(name);
	}
	
	void setInitParam(String name, String value) {				//DONE
		initParams.put(name, value);
	}
}
