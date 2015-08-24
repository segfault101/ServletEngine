package edu.upenn.cis.cis455.webserver;

import java.util.LinkedList;
import java.util.Queue;
import java.net.Socket;;

public class RequestQueue {

	 private Queue<Socket> queue = new LinkedList<Socket>();
     
     
	     /*
	     * Enqueue will add a clientSocket to this queue, and will notify any waiting
	     * threads that now there is an clientSocket available to service
	     */

	 public synchronized void enqueue(Socket clientSocket) 
	 {
	        queue.add(clientSocket);
	        notifyAll();	// Wake up anyone waiting on the queue
	        
	 }
	 
	    /*
	    * Dequeue makes a blocking call so that we will only return when the queue has
	    * something on it, otherwise wait until something is put on it
	    */

	 public synchronized Socket dequeue()
	 {

		 Socket clientSocket = null;
	         
	        while(queue.isEmpty())
	        {
	            try {
	                wait();
	            	} 
	            catch (InterruptedException e1) 
	            {
	                return clientSocket;
	            }
	        }
	        
	      clientSocket = queue.remove();
	        
	      return clientSocket;
	   }
}
