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

		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception {
		
		// Get a reference to the socket's input and output streams
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		OutputStream output = clientSocket.getOutputStream();


		// Read the request
		
		String str = input.readLine();
		StringTokenizer requestTokens = new StringTokenizer(str, " ");

		String requestMethod = requestTokens.nextToken();
		String requestedResource = requestTokens.nextToken();
		String requestHttpVersion = requestTokens.nextToken();
		File file = new File(root + requestedResource).getCanonicalFile();

		if ( requestMethod.equalsIgnoreCase("GET") || requestMethod.equalsIgnoreCase("HEAD") 	//Check the request method 
			|| !(requestHttpVersion.equalsIgnoreCase("HTTP/1.0")	||	requestHttpVersion.equalsIgnoreCase("HTTP/1.1"))) //Check the request protocol version
		{
			// check the file path to detect path traversal attacks
			if (!file.getPath().startsWith(root)) {
				
				// Suspected path traversal attack: reject with 403 error.
				output.write(("HTTP/1.1 403 (Forbidden)" 
							+ "Date:" + new Date() 
							+ CRLF).getBytes());
				
				output.close();
			}

			// check if requested file exists
			else if (!file.isFile()) {
				
				// Object does not exist or is not a file: reject with 404
				output.write(("HTTP/1.1 404 (Not Found)\n" 
							+ "Date:" + new Date() 
							+ CRLF).getBytes());
				
				output.close();
			}

			// if the requested file exists
			else {

				// if method is GET, send the headers and serve the file
				if(requestMethod.equalsIgnoreCase("GET"))
				{
					//Write the headers
					output.write(("HTTP/1.1 200 OK\n" 
							+ "Content Type: " + getContentType(file) + "\n" 
							+ "Content Length: " + file.length() + "\n"
							+ "Date: " + new Date()
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
					input.close();
				}

				// if method is HEAD, send the headers but don't serve the file
			}
		}

		// if 
		else
		{
			
		}
	}

	private boolean inspectStatusLine(){
		
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

}