import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Todd J. Green
 */
class FakeSession implements HttpSession {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String arg0, Object arg1) {
		m_props.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	public void removeValue(String arg0) {
		m_props.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		m_valid = false;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	boolean isValid() {
		return m_valid;
	}
	
	private Properties m_props = new Properties();
	private boolean m_valid = true;
}
