package TestHarness;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

class Request implements HttpServletRequest {

	// see: http://stackoverflow.com/questions/5243754/difference-between-getattribute-and-getparameter
	
	// these are POST or GET parameters that can be accessed by servlets
	// i.e the query string parameters
	// need to initialise these
	private Properties m_params = new Properties();	// <string, string> map

	// this is used to store objects for server side usage only
	// no need to initialise these
	//http://stackoverflow.com/questions/911529/how-the-attribute-field-of-a-httpservletrequest-maps-to-a-raw-http-request
	private Properties m_attribs = new Properties();	// <string, object> map
	
	private HashMap<String, String> requestHeaders;
	private Session m_session = null;
	private String m_method;
	
	Request() {
	}
	
	Request(Session session, HashMap<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
		m_session = session;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	//IMPORTANT: READ DOC
	public HttpSession getSession(boolean arg0) {	//whether a new session is assigned or not (when there is no session), depends on arg0
		if (arg0) {
			if (! hasSession()) {
				m_session = new Session();
			}
		} else {
			if (! hasSession()) {
				m_session = null;
			}
		}
		return m_session;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {										 	
		return getSession(true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	// DONE
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		
		return null;
	}
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()									//DONE
	 */
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new StringReader(requestHeaders.get("Request_Body")));
	}

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {													// DONE 		
		return "BASIC";
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)		//DONE UNTESTED
	 */
	public long getDateHeader(String arg0) {											
		
		
		SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		Date d = new Date();
		long milliseconds;
		String stringDate = requestHeaders.get(arg0);
		
		if (stringDate == null)
			milliseconds = -1;
		else
		{
			try 
			{
				d = f.parse(stringDate);
			} 
			catch (ParseException e) 
			{
				throw new IllegalArgumentException("Error parsing the Date header.");
			}
			
			milliseconds = d.getTime();
		}
		
		return milliseconds;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)			//Come back here if something mucks up with headers and cookies
	 */
	//TODO
	//http://stackoverflow.com/questions/3241326/set-more-than-one-http-header-with-the-same-name
	public String getHeader(String arg0) {
		return requestHeaders.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	//http://stackoverflow.com/questions/3241326/set-more-than-one-http-header-with-the-same-name
	public Enumeration getHeaders(String arg0) {										//DONE
		String val = requestHeaders.get(arg0);
		if (val == null)
			return Collections.enumeration(Collections.emptyList());
		else
		{
			//remember we have comma seperated the param values that have same param name
			StringTokenizer Tokens = new StringTokenizer(val, ",");
			return Tokens;
				
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	@SuppressWarnings("rawtypes")
	public Enumeration getHeaderNames() {												//DONE
		Enumeration<String> strEnum = Collections.enumeration(requestHeaders.keySet());
		return strEnum;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0) {												//DONE
		String s = requestHeaders.get(arg0);
		
		if(s == null)
			return -1;
		else 
			return Integer.parseInt(s);		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {														// DONE
		return m_method;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	//CLARIFICATION: SHOULD ALWAYS RETURN THE REMAINDER OF URL REQUEST AFTER THE PORTION MATCHED 
	// BY THE URL PATTERN IN WEB.XML. IT STARTS WITH "/"
	public String getPathInfo() {									//NOT DOING IT unless I need it - needs refactoring of code
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {								//NOT REQUIRED
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {								//DONE
		return "";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	//returns "" if empty											//DONE
	public String getQueryString() {
		return requestHeaders.get("Query_String");	
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {									//NOT SUPPORTED
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {						//NOT REQUIRED
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {							//NOT REQUIRED
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {									//DONE
		String req = requestHeaders.get("Requested_Resource");
		String str[] = null;
		String slash;
		int index;

		//if the request contains the scheme
		if(req.contains(":\\"))
			slash = ":\\";
		else if (req.contains("://"))
			slash = "://";
		else
			slash = null;											//DO NOT KNOW IF THIS IS RIGHT

		//if the scheme is present
		if(slash!=null)
		{	str = req.split(slash);
			index = 1;
		}
		else 
		{	index = 0; 
			str[index] = req;
		}
		
		//if the url contains query string
		if(str[index].contains("\\?"))
		{
			String str2[] = str[index].split("\\?");
			return str2[0];
		}
		else
			return str[index];
		

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {								//DONE
		String url = "" + getScheme() + ":" + File.separator + File.separator  + getServerName() + ":" + getServerPort();  		
		return new StringBuffer (url);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {									//NOT DOING THIS UNTIL I NEED TO
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {					//DONE
		return m_attribs.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {					//DONE
		// TODO Auto-generated method stub
		return m_attribs.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	//CLARIFICATION: SHOULD RETURN "ISO-8859-1" BY DEFAULT
	// AND THE RESULT OF setCharacterEncoding() IF THAT WAS PREVIOUSLY CALLED 
	public String getCharacterEncoding() {									//DONE
		String enc;
		
		//if setCharacterEncoding wasn't previously called
		if((enc = requestHeaders.get("Character-Encoding")) == null)
		{ 
			String cont;
			
			//if content type is specified
			if((cont = requestHeaders.get("Content-Type")) != null)
			{
				String strings[] = cont.split("; ");
				
				//if encoding is specified
				if(strings.length>1)
					return strings[1];
				else return null;
			}
			else
				return null;
		}
		else
			return enc;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException 		//DONE
	{
		requestHeaders.put("Character-Encoding", arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {									//DONE
		return Integer.parseInt(requestHeaders.get("Content-Length"));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {								//DONE
		return 	requestHeaders.get("Content-Type");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {	//NOT REQUIRED
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {					//DONE
		return m_params.getProperty(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {					//DONE
		return m_params.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {			//DONE
		
		String val = null;
		String parVals[] = null;
		
		if((val = m_params.getProperty(arg0)) != null)
		{
			if(val.contains(","))
				return val.split(",");			
			else		
				return new String[]{val};			
		}
		else return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {								//DONE
		return m_params;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {								//DONE HARDCODED
		return "HTTP";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	// DONE
	public String getScheme() {									//DONE
		return "http";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {								//DONE
		String hostAndPort = (String) requestHeaders.get("Host");
		String host = hostAndPort.split(":")[0];
		return host;
	
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {								//DONE
		String hostAndPort = (String) requestHeaders.get("Host");
		String port = hostAndPort.split(":")[1];
		return Integer.parseInt(port);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {								//DONE
		return requestHeaders.get("Remote_Addr");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {								//DONE
		return requestHeaders.get("Remote-Host");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {			//DONE
		m_attribs.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {						//DONE
		m_attribs.remove(arg0);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocale()				//DONE
	 */
	public Locale getLocale() {
		String header;		
		
		if((header = requestHeaders.get("Accept-Language"))!=null)
		return new Locale (header.split(",")[0]);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {								//NOT REQUIRED
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {										//DONE
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {	//NOT REQUIRED
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {						//DONE HARDCODED
		return "http://localhost:8080/" + arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getRemotePort()		//DONE
	 */
	public int getRemotePort() {									
		return Integer.parseInt(requestHeaders.get("Remote_Port"));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalName()
	 */
	public String getLocalName() {							// DONE
		return "localhost";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalAddr()
	 */
	public String getLocalAddr() {							//DONE
		return requestHeaders.get("Local_Addr");										//null because it isn't hosted on the internet
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocalPort()
	 */
	public int getLocalPort() {								//DONE
		return Integer.parseInt(requestHeaders.get("Local_Port"));
	}

	

	
	// used in testharness main class
	void setMethod(String method) {
		m_method = method;
	}
	
	void setParameter(String key, String value) {
		m_params.setProperty(key, value);
	}
	
	void clearParameters() {
		m_params.clear();
	}
	
	boolean hasSession() {
		return ((m_session != null) && m_session.isValid());
	}
	
}
