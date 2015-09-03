package TestHarness;
import javax.servlet.*;
import java.util.*;

/**
 * @author Nick Taylor
 */
class Config implements ServletConfig {
	
	//All these data members are initialised when createServlets(handler, context) is called
	private String servletName;
	private Context context;
	private HashMap<String,String> initParams;
	
	public Config(String servletName, Context context) {
		this.servletName = servletName;
		this.context = context;
		initParams = new HashMap<String,String>();
	}

	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public ServletContext getServletContext() {
		return context;
	}
	
	public String getServletName() {
		return servletName;
	}

	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}
