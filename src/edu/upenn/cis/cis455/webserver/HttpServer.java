package edu.upenn.cis.cis455.webserver;

import java.util.*;
import java.io.*;
import java.net.*;


public class HttpServer {

	static final int port = 8080;

	public static void main(String args[]) throws Exception {
		ServerSocket serverSocket = new ServerSocket(port);

		while (true) {
			// receive the http request message
			Socket clientSocket = serverSocket.accept(); // The method blocks until a connection is made

			// Construct an object to process the HTTP request message and pass
			// it to a handler thread
			HttpRequest request = new HttpRequest(clientSocket);

			// Create a new handler thread to process the request
			Thread thread = new Thread(request);

			// Start the thread
			thread.start();
		}
		
	}

}

class HttpRequest implements Runnable {
	
	final static String CRLF = "\r\n";
	final String root = "/home/cis455/workspace/HW1/www/";
	Socket clientSocket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.clientSocket = socket;
	}

	// Implement the run() method of the Runnable interface
	public void run() {

			try 
			{
				processRequest();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		
	}

	private void processRequest() throws Exception {
		
		// Get a reference to the socket's input and output streams
		InputStream in = clientSocket.getInputStream();
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		OutputStream output = clientSocket.getOutputStream();


		// Read the status line		
		String str = input.readLine();
		StringTokenizer requestTokens = new StringTokenizer(str, " ");

		// check if the status line is of format: <req_method> <resource> <http_ver>
		if(requestTokens.countTokens()	!=	3)
			try {
				sendBadRequest(output);
				} 
			catch (Exception e1) 
				{
				sendServerError(output);
				e1.printStackTrace();
				}
		 
		
		String requestMethod = requestTokens.nextToken();
		String requestedResource = requestTokens.nextToken();
		String requestHttpVersion = requestTokens.nextToken();
		
		File file = new File(root + requestedResource).getCanonicalFile();
		
		
		
		//if the httpversion is 1.1, then the "Host:" header is mandatory
		if(requestHttpVersion.equalsIgnoreCase("HTTP/1.1") && checkHOSTHeader(input))					
			try
			{
				processStatusLine(requestMethod, file, requestHttpVersion, output);
			} 
			catch (Exception e) 
			{
				sendServerError(output);
				e.printStackTrace();
			}
		else
			sendBadRequest(output);
		
	}




	private boolean checkHOSTHeader(BufferedReader input) throws Exception
	{
		
		String str = input.readLine();
		
		while(str.startsWith("HOST:") || str.startsWith("Host:") || str.startsWith("host:") || str.equals(null))
			str = input.readLine();
		
		if(str.equals(null))	//no HOST: header found
			return false;
		else
			return true;
	
	}

	private void processStatusLine(String requestMethod, File file, String requestHttpVersion, OutputStream output) throws Exception
	{

		if ( (requestMethod.equalsIgnoreCase("GET") || requestMethod.equalsIgnoreCase("HEAD")) 	//Check the request method 
				&& (requestHttpVersion.equalsIgnoreCase("HTTP/1.0")	||	requestHttpVersion.equalsIgnoreCase("HTTP/1.1")) ) //Check the request protocol version
		{
			// check the file path to detect path traversal attacks
			if (!file.getPath().startsWith(root)) 
				sendForbidden(output);	// Suspected path traversal attack: reject with 403 error


			// check if requested file exists
			else if (!file.isFile()) 
				sendNotFound(output);	// Object does not exist or is not a file: reject with 404
			

			// if the requested file exists
			else 
				serveFile(output, file, requestMethod);  // Serve the file or just the headers depending on request method
														
		}

		// if there is a problem with request method or http version give a 400 
		else
			sendBadRequest(output);

	}

	private void serveFile(OutputStream output, File file, String requestMethod) throws Exception
	{
		
		// if method is GET, send the headers and serve the file
		if(requestMethod.equalsIgnoreCase("GET"))
		{
			//Write the headers
			output.write(("HTTP/1.1 200 OK" + CRLF
					+ "Content Type: " + getContentType(file) + CRLF
					+ "Content Length: " + file.length() + CRLF
					+ "Date: " + new Date() + CRLF
					+ CRLF).getBytes());

			//send the file in pieces
			FileInputStream fs = new FileInputStream(file);
			final byte[] buffer = new byte[0x10000];
			int count = 0;

			//read data into a fixed size buffer and send it
			while ((count = fs.read(buffer)) >= 0) 
			{
				output.write(buffer,0,count);
			}

			//close streams after serving request
			fs.close();
			output.close();
		}
		
		// if method is HEAD, send the headers but don't serve the file
		else
		{
			//Write the headers
			output.write(("HTTP/1.1 200 OK" + CRLF
					+ "Content Type: " + getContentType(file) + CRLF
					+ "Content Length: " + file.length() + CRLF
					+ "Date: " + new Date() + CRLF
					+ CRLF).getBytes());
			
			output.close();
		}

		
	}

	private void sendNotFound(OutputStream output) {
		
		try {
			output.write(("HTTP/1.1 404 Not Found" + CRLF 
						+ "Date: " + new Date() + CRLF 
						+ CRLF 
						+ "<html> Nothing comes of nothing.. </html>").getBytes());
			
		} catch (Exception e) {
			sendServerError(output);
			e.printStackTrace();
		}
		
		try {
			output.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}


	}

	private void sendServerError(OutputStream output) 
	{
		try {
			output.write(("HTTP/1.1 500 (Server Error)" + CRLF 
					+ "Date:" + new Date() + CRLF
					+ CRLF
					+ "<html> The fault is of the server </html>").getBytes());
			output.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendForbidden(OutputStream output) 
	{		
		try {
			output.write(("HTTP/1.1 403 (Forbidden)" + CRLF
					+ "Date:" + new Date() + CRLF
					+ CRLF
					+ "<html> End of the road </html>").getBytes());
			
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private String getContentType(File file) 
	{
		// tokenize and get the content type
		String contentType = null;
		StringTokenizer tokens = new StringTokenizer(file.getAbsolutePath(),".");

		//iterate until there are no more tokens left
		while(tokens.hasMoreTokens())
		{
			contentType = tokens.nextToken();			
		}

		//if the file type is any of the following, return the respective http/1.1 content type string  
		if(contentType.equalsIgnoreCase("jpg") || contentType.equalsIgnoreCase("jpeg"))
			return "image/jpg";
		if(contentType.equalsIgnoreCase("png"))
			return "image/png";
		if(contentType.equalsIgnoreCase("gif"))
			return "image/gif";
		if(contentType.equalsIgnoreCase("html") || contentType.equalsIgnoreCase("htm"))
			return "text/html";
		if(contentType.equalsIgnoreCase("txt"))
			return "text/plain";
		if(contentType.equalsIgnoreCase("pdf"))
			return "pdf";
		if(contentType.equalsIgnoreCase("css"))
			return "css";
		if(contentType.equalsIgnoreCase("jsp"))
			return "application/jason";

		//if file type is none of the above, return null 
		return null;
	}

	private void sendBadRequest(OutputStream output)
	{
		try {
			output.write(("HTTP/1.1 400 (Bad Request)" + CRLF
					+ "Date" + new Date() + CRLF					
					+ CRLF
					+ "<html> Ask and you won't receive </html>").getBytes());
			
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}