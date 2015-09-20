package TestHarness;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class Container 
{	

	final String ROOT;	//Server root, not the webapp root
	
	final String pathtowebxml;

	final saxEventHandler handler;
	
	final Context context;	//my server can run only a single app
							//so when the servlets usually map to: http://host:port/context/servletname
							//here they map to: http://host:port/servletname		i.e. context = null
	
	final HashMap<String,HttpServlet> servlets; //and only one app means only one web.xml

	private HashMap<String, Session> sessionIDtoSessionMap = new HashMap<String, Session>();				

	
	public final int BAD_REQUEST	= 2;
	public final int SERVER_ERROR 	= 1;
	public final int SUCCESS		= 0;
	
	//initialize: 
	public Container(String pathtowebxml, String ROOT) throws Exception
	{		
		this.pathtowebxml = pathtowebxml;
		
		this.ROOT = ROOT;
		
		this.handler = parseWebdotxml(pathtowebxml);
		
		this.context = createContext(handler);
		
		this.servlets = createServlets(handler, context);		
		
	}
	
	
	public String isRequestToAServlet(String requestedResource){
						
		//check if a servlet exists matching the url given
		//and return true or false
		String requestUrl = requestedResource.split("\\?|&|=")[0];
		String servletName = getServletNameForUrl(requestUrl);
		
		return servletName;

	}
	
	private String getServletNameForUrl(String requestUrl) {
		
		/*
		 * Here we have two cases and a sub-case to handle:
		 * 	1) servlet-url is an exact match to requested resource.
		 * 		mapping starts with a '/'
		 * 
		 *  2) servlet-url is path mapping
		 *  	mapping starts with a '/' and ends with '*'
		 *  
		 * 		- subcase: as a special case "/foo/*" must match "/foo" 
		*/

		final String ASTERISK = "*";
		final String SLASH_ASTERISK = "/" + ASTERISK;
		
		String longest_match = null;	//to keep track of longest path match
		String servletName = null;		
		
		
		// CASE 1: Exact Match		
		//	Check if a SERVLET_NAME to SERVLET_URL mapping exists
		for(String d : handler.servletNameToUrl_map.keySet())
		{
			if(handler.servletNameToUrl_map.get(d).equals(requestUrl))
				return d;
		}
		
		// CASE 2: There is no exact match so try path mapping		
		for(String key: handler.servletNameToUrl_map.keySet())
		{
			String servlet_url = handler.servletNameToUrl_map.get(key);

			if(servlet_url.endsWith(ASTERISK))
			{
				if(servlet_url.endsWith(SLASH_ASTERISK))
				{			
					// Special sub case: /foo/* should also match to /foo
					int i2 = servlet_url.lastIndexOf(SLASH_ASTERISK);
					String strings2[] = {servlet_url.substring(0, i2), servlet_url.substring(i2)};

					if(requestUrl.startsWith(strings2[0]))
					{
						
						if(longest_match!=null)
						{
							if(longest_match.length() < servlet_url.length())
							{
								longest_match = servlet_url;
								servletName = key;
							}
						}
						else
						{
							longest_match = servlet_url;
							servletName = key;
						}
					}

				}

				// Run through all the servlet-urls with * ending and
				// keep track of the longest one
				int i3 = servlet_url.lastIndexOf(ASTERISK);
				String strings3[] = {servlet_url.substring(0, i3), servlet_url.substring(i3)};

				if(requestUrl.startsWith(strings3[0]))
				{
					if(longest_match!=null)
					{
						if(longest_match.length() < servlet_url.length())
						{
							longest_match = servlet_url;
							servletName = key;
						}
					}
					else
					{
						longest_match = servlet_url;
						servletName = key;
					}
				}

			}
		}

		if(longest_match!=null)
		{
			return servletName;
		}

		
		return null;
	}

	public int runServlet(String servletName, String requestMethod, String requestHttpVersion, String requestQueryString, HashMap<String, String> requestHeaders, OutputStream output)
	{
		
		//Check if there is actually an associated class with the servlet name
		if(handler.servletNameToClass_map.get(servletName).equals(null))
			return SERVER_ERROR;	//send server error if class not found
		
		Session session = null;
		String cookieVal;

		if((cookieVal = requestHeaders.get("Cookie")) != null)
		{
			if(cookieVal.contains("JSESSIONID"))
			{
				String[] cookieTokens = cookieVal.split(";[\\s]");
				
				int index =0;
				for(; !cookieTokens[index].startsWith("JSESSIONID="); index++);
				
				String sessionID = cookieTokens[index].split("=", 2)[1];
				
				//TODO
				if(cookieVal.contains("Expiry"))	//if the cookie contains expiry time,  
				{
					//load it 
					for(; !cookieTokens[index].startsWith("Expiry="); index++);	
					
					String sessionExpiry = cookieTokens[index].split("=", 2)[1];
					
					//convert it to milliseconds
					
					//and compare it with server side session timeout
					
					//pick whichever is the least
				}
				else

				if((session = sessionIDtoSessionMap.get(sessionID)) != null)
				{
					//if session expired, invalidate it and create new session
					if(session.)
						
					//else use old session
				}
			}
		}
		
		//TODO if the request method is "HEAD", just send headers AND content-length
		if(!requestMethod.equalsIgnoreCase("GET") && !requestMethod.equalsIgnoreCase("POST"))
			return BAD_REQUEST;
		
		Request request = new Request(session, requestHeaders);
		Response response = new Response();
		
		request.setMethod(requestMethod);			
		
		String strings[] = requestQueryString.split("&|=");	// '|' is the alternation symbol in regex
		
		for (int j = 0; j < strings.length - 1; j += 2) //empty queries handled implicitly
		{
			String parVal;
			
			//http spec allows mutliple values for single param name: 
			if((parVal = request.getParameter(strings[j])) == null)
				request.setParameter(strings[j], strings[j+1]);
			//if the param name and value pair already exists concat the value
			//valid delimiters i can use are the invalid characters in queries :http://bit.ly/1UxfEl2
			else
			{
				//using , as a delimiter
				parVal = parVal + "," + strings[j+1];
				request.setParameter(strings[j], parVal);
			}
		}
		
		HttpServlet servlet = servlets.get(servletName);
		
		try
		{
			servlet.service(request, response);
		} 
		catch (ServletException | IOException e) 
		{
			e.printStackTrace();
			System.out.println("Error trying to run servlet.");
			return BAD_REQUEST;
		}
		
		session = (Session) request.getSession(false);
		return SUCCESS;
		
	}
	
	
	/*
	 * TODO: Session Manager to check and remove expired sessions
	 * TODO: Alternate ways to store sessions, database or files?
	 */
	private void storeSession(Session session)
	{
			sessionIDtoSessionMap.put(session.getId(), session);
	}
		
	private Session loadSession(String sessionID)
	{
			return sessionIDtoSessionMap.get(sessionID);
	}
	
	

	// http://docs.oracle.com/javase/7/docs/api/org/xml/sax/ContentHandler.html
	static class saxEventHandler extends DefaultHandler 
	{
		
		// hash map of: 
		//	SERVLET_NAME -> SERVLET_CLASS_NAME
		HashMap<String,String> servletNameToClass_map = new HashMap<String,String>();
		
		// hash map of:
		//	SERVLET_NAME -> SERVLET_URL
		HashMap<String, String> servletNameToUrl_map = new HashMap<String,String>();
		
		// hash map of context parameters of the application [WE ARE RUNNING ONLY ONE HERE]
		//	CONTEXT_PARAM_NAME -> CONTEXT_PARAM_VALUE
		HashMap<String,String> contextParamNameToValue_map = new HashMap<String,String>();
		
		
		// hash map of init parameters of the servlets 
		// SERVLET_NAME -> INIT_PARAMETER_NAME -> INIT_PARAM_VALUE
		HashMap<String,HashMap<String,String>> servletNameToInitParamNameToValue_map = new HashMap<String,HashMap<String,String>>();

		
		
		// SAXy parsing magic:
		
		private int m_state = 0;
		private String m_servletName;
		private String m_paramName;
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) 
		{
			if (qName.compareTo("servlet-name") == 0) {
				m_state = 1;
			} else if (qName.compareTo("servlet-class") == 0) {
				m_state = 2;
			} else if (qName.compareTo("url-pattern") == 0){
				m_state = 5;
			} else if (qName.compareTo("context-param") == 0) {
				m_state = 3;
			} else if (qName.compareTo("init-param") == 0) {
				m_state = 4;
			} else if (qName.compareTo("param-name") == 0) {
				m_state = (m_state == 3) ? 10 : 20;		//m_state = 10 if context-param and 20 if init-param
			} else if (qName.compareTo("param-value") == 0) {
				m_state = (m_state == 10) ? 11 : 21;	//m_state = 11 if context-param and 21 if init-param
			}
		}
		
		public void characters(char[] ch, int start, int length) 
		{
			String value = new String(ch, start, length);

			if (m_state == 1) 
			{
				m_servletName = value;
				m_state = 0;
			}
			
			else if (m_state == 2) 
			{
				servletNameToClass_map.put(m_servletName, value);
				
//				m_servletName = null;	//Will this cause problems? Yes, in init-params it does
				m_state = 0;
			} 
			
			else if (m_state == 5)
			{
				servletNameToUrl_map.put(m_servletName, value);
				
//				m_servletName = null;	//Will this cause problems? Yes, in init-params it does
				m_state = 0;
			}
			
			else if (m_state == 10 || m_state == 20) 
			{
				m_paramName = value;
			} 
			
			else if (m_state == 11) 
			{
				if (m_paramName == null) {
					System.err.println("Context parameter value '" + value + "' without name");
					System.exit(-1);
				}
				contextParamNameToValue_map.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			} 
			
			else if (m_state == 21) 
			{
				if (m_paramName == null) 
				{
					System.err.println("Servlet parameter value '" + value + "' without name");
					System.exit(-1);
				}
				
				HashMap<String,String> p = servletNameToInitParamNameToValue_map.get(m_servletName);
				
				if (p == null) 
				{
					p = new HashMap<String,String>();
					servletNameToInitParamNameToValue_map.put(m_servletName, p);
				}
				
				p.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			}
		}
	
	}
	
	
	// uses SAX parses to parse web.xml
	private static saxEventHandler parseWebdotxml(String webdotxml) throws Exception 
	{
		saxEventHandler h = new saxEventHandler();
		File file = new File(webdotxml);
		
		if (file.exists() == false) 
		{
			System.err.println("error: cannot find " + file.getPath());
			System.exit(-1);
		}
		
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(file, h);
		
		return h;
	}
	
	
	// creates context and sets context_params
	private static Context createContext(saxEventHandler h) 
	{		
		Context fc = new Context();
		
		//Load the context parameters stored in handler into the new context
		for (String param : h.contextParamNameToValue_map.keySet()) 
		{
			fc.setInitParam(param, h.contextParamNameToValue_map.get(param));
		}

		return fc;
	}

	
	// returns hashmap of: SERVLET_NAME -> SERVLET_INSTANCE
	private static HashMap<String,HttpServlet> createServlets(saxEventHandler handler, Context context) throws Exception 
	{		
		// SERVLET_NAME -> SERVLET_INSTANCE
		HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
		
		/*
		 * 		INITIALIZING SERVLETS:
		*/

		//for each servlet name stored in the hash map "m_servlets" of the handler
		for (String servletName : handler.servletNameToClass_map.keySet()) 
		{
			//create a new servlet config using servlet_name and context
			Config config = new Config(servletName, context);		
			
			
			//get the class name of the servlet
			String className = handler.servletNameToClass_map.get(servletName);
			//and create a new instance with it
			Class servletClass = Class.forName(className);
			
			
			//create a new instance  of the servlet using the servlet_class_name we just retrieved
			HttpServlet servlet = (HttpServlet) servletClass.newInstance();
						
			//load up the init params of the servlets
			HashMap<String,String> servletParams = handler.servletNameToInitParamNameToValue_map.get(servletName);
			
			//if servlet params are not empty
			if (servletParams != null) 
			{
				// store them in the created servlet config
				for (String param : servletParams.keySet()) 
				{
					config.setInitParam(param, servletParams.get(param));
				}
			}
			
			//itialize the servlet - pass the its config as a parameter
			servlet.init(config);
			
			//store the servlet name and servlet object into the hash map
			servlets.put(servletName, servlet);
		}
		
		return servlets;
	}

//	
//	// error message showing how to use test harness
//	private static void usage() {
//		System.err.println("usage: java TestHarness <path to web.xml> [<GET|POST> <servlet_name?params> ...]");
//	}
//	
//	
//	public static void main(String[] args) throws Exception {
//		
//		if (args.length < 3 || args.length % 2 == 0) {
//			usage();
//			System.exit(-1);
//		}
//		
//		saxEventHandler handler = parseWebdotxml(args[0]);	//args[0] is web.xml path
//		
//		Context context = createContext(handler);
//		
//		HashMap<String,HttpServlet> servlets = createServlets(handler, context);
//		
//		Session session = null;
//		
//		for (int i = 1; i < args.length - 1; i += 2) 
//		{
//			Request request = new Request(session);
//			Response response = new Response();
//		
//			//Observe that args[i+1] or args[2] == servlet_name?params 
//			//split the param_name and param_value pairs and store them
//			String[] strings = args[i+1].split("\\?|&|=");
//			
//			//get the the servlet specified in command line
//			HttpServlet servlet = servlets.get(strings[0]);
//			
//			//if null, print error
//			if (servlet == null) 
//			{
//				System.err.println("error: cannot find mapping for servlet " + strings[0]);
//				System.exit(-1);
//			}
//			
//			// Store the param_names and param_value pairs 
//			//	in the parameter hashmap of Request object
//			for (int j = 1; j < strings.length - 1; j += 2) 
//			{
//				request.setParameter(strings[j], strings[j+1]);
//				System.out.println(strings[j]+", "+strings[j+1]);
//			}
//						
//			// Check request method and set it in request if valid
//			if (args[i].compareTo("GET") == 0 || args[i].compareTo("POST") == 0) 
//			{
//				request.setMethod(args[i]);
//				
//				//FINALLY call the service method of the servlet
//				servlet.service(request, response);
//				
//			} else {
//				System.err.println("error: expecting 'GET' or 'POST', not '" + args[i] + "'");
//				usage();
//				System.exit(-1);
//			}
//			
//			session = (Session) request.getSession(false);
//			
//			System.out.println("Hit the breaks!");
//			
//		}
//	}

}
 
