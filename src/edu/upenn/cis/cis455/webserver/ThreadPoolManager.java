package edu.upenn.cis.cis455.webserver;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;


public class ThreadPoolManager {
	 
    private final int THREADPOOL_CAPACITY;
    private RequestQueue requestQueue = new RequestQueue();
    
    private ServerSocket serverSocket;
    
    private boolean shutdownCalled = false;
    boolean exit = false;
    
    private ArrayList <WorkerThreads> workerList;	//used to get statuses of the threads in cp
    
    public ThreadPoolManager(int capacity, ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
        
    	this.THREADPOOL_CAPACITY = capacity;
        
    	workerList = new ArrayList<WorkerThreads>(THREADPOOL_CAPACITY);
        
        try {
				initAllWorkers();
			} 
        catch (Exception e) 
        	{
				e.printStackTrace();
        	}
        
    }
     
    private void initAllWorkers() throws Exception	//initialises and starts all the workers
    {
        for(Integer i = 0; i < THREADPOOL_CAPACITY; i++)
        {        	
            WorkerThreads worker = new WorkerThreads(requestQueue, "Thread_"+ i.toString(), this);
            workerList.add(worker);
            worker.start();
        }
    }
     
     
    public void submitTask(Socket clientSocket){
    	if(!shutdownCalled)	//if /shutdown is not called then enqueue the request
    		requestQueue.enqueue(clientSocket);
    }
     
    public String getWorkersStatus()
    {
    	String status="";
    	for(WorkerThreads t : workerList)
    	{
    		if(t.getState().toString().equals("RUNNABLE"))
    			status+= t.threadName + " " + t.currentURL + "<br/>";
    		else 
    			status+=t.threadName + " " + t.getState() + "<br/>";

    	}
    	
    	return status;
    	
    }
    
    
    
    public synchronized void shutdown()
    {
    	shutdownCalled = true; //stop taking requests
    	
    	System.out.println("Server is shutting down\nThreads are put to death"); //print to console
    	
    	for(WorkerThreads t : workerList)
    		t.isInterrupted=true;	//set flags for all threads 
    		
							    		//!IMPORTANT: a seperate flag for interrupt handling is used here instead of the in built one,
							    		//this is because once the the Thread.interrupted() is called, the flag will be reset if it were true
							    		//but in my server the flag needs to be checked more than once
    	
    	// give 2 seconds of grace time for ongoing requests
    	try {
				wait(2000);
			} 
    	catch (InterruptedException e1) 
    		{
				e1.printStackTrace();
    		}
    	
    	//close the serversocket
    	try {
				serverSocket.close();
			} 
    	catch (IOException e) 
    		{			
				e.printStackTrace();
    		}
    	
    	//print out the statuses of the threads on shutdown
		for(WorkerThreads t : workerList)
			System.out.println(t.threadName + ":" + t.getState());

		
		System.out.println("Everything ends..");
		
		System.exit(0); //I used this since the main thread doesn't exit for some reason in eclipse
    }
    
    
}