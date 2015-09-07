package edu.upenn.cis.cis455.webserver;

import TestHarness.Container;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HttpServer {

	public static final int PORT = 8080;	
	static final String WEBXML_PATH = "/home/cis455/workspace/HW1/src/WEB-INF/web.xml";
	static final String ROOT = "/home/cis455/workspace/HW1/www/";
	static Container container;	//We need only one container
	static ServerSocket serverSocket;
	public static void main(String args[]) throws Exception {


		try
		{
			serverSocket = new ServerSocket(PORT); // open serverSocket
															// to get requests
		}
		
		
		
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("");
			System.exit(-1);
		}
		
		try
		{
			container = new Container(WEBXML_PATH, ROOT);			// Initialize the container
		}
		catch (Exception E)
		{
			E.printStackTrace();
			System.out.println("Error in main() when trying to initialize a container.");
		}

		
		ThreadPoolManager threadPoolManager = new ThreadPoolManager(10, serverSocket, container, ROOT); // initialize threadpool

		
		
		while (!serverSocket.isClosed()) {					// accept connections till server socket is closed
			try {
				Socket clientSocket = serverSocket.accept();// The method
															// blocks until
															// a connection
															// is made

				threadPoolManager.submitTask(clientSocket); // submit task to
															// the manager, it
															// adds it to the
															// request queue
			}

			catch (SocketException e) {
				if (serverSocket.isClosed())
					break;
			}

		}

	}

}
