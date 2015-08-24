package edu.upenn.cis.cis455.webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class HttpServer {

	static final int port = 8080;

	public static void main(String args[]) throws Exception {
		
		
		ServerSocket serverSocket = new ServerSocket(port);	//open serverSocket to get requests

		ThreadPoolManager threadPoolManager = new ThreadPoolManager(10, serverSocket);	//initialize threadpool

		while (!serverSocket.isClosed()) 
		{			
			// receive the http request message
			
			try
			{
				Socket clientSocket = serverSocket.accept(); // The method blocks until a connection is made
				threadPoolManager.submitTask(clientSocket);	 // submit task to the manager, it adds it to the request queue
			}
			
			catch(SocketException e)
			{
				if(serverSocket.isClosed())
					break;
			}
			
												
		}
		
	}

}
