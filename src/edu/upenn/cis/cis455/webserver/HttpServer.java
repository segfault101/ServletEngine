package edu.upenn.cis.cis455.webserver;

import java.text.DateFormat;
import java.util.*;
import java.io.*;
import java.net.*;

public class HttpServer 
{

	static final int port = 8080;

	public static void main(String args[]) throws Exception 
	{
		ServerSocket serverSocket = new ServerSocket(port);
		

		while (true) 
		{
			// receive the http request message
			Socket clientSocket = serverSocket.accept(); //The method blocks until a connection is made
			
			// Construct an object to process the HTTP request message and pass it to a handler thread
			HttpRequest request = new HttpRequest(clientSocket);

			// Create a new handler thread to process the request
			Thread thread = new Thread(request);

			// Start the thread
			thread.start();
		}
		
	}

}

class HttpRequest implements Runnable 
{
	final static String CRLF = "\r\n";
	Socket clientSocket;
	
	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.clientSocket = socket;
	}

	// Implement the run() method of the Runnable interface
	public void run() 
	{

		try 
		{
			processRequest();
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception
	{
		// Get a reference to the socket's input and output streams
		
		//InputStream  input  = clientSocket.getInputStream();
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		OutputStream output = clientSocket.getOutputStream();
		
		output.write(("HTTP/1.1 200 OK"+"\nDate:"+new Date()+"\nContent-Type: text/html"+"\n<html><body>" + "It werks </body></html>").getBytes());
        
		output.close();        
        input.close();
		
        
		
	}
}