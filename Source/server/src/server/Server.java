package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService executionThreadPool;
    
    static boolean listening;

    
    public static void main(String[] args) throws IOException {

        executionThreadPool = Executors.newCachedThreadPool();

        listening      = true;
        int portNumber = 3141;
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) 
            {
                // Create a thread for every client
                executionThreadPool.submit( new ClientThread(serverSocket.accept()) );
            }
        } catch (IOException e) {
            System.err.println("Could not start the server on port: " + portNumber);
            e.printStackTrace(System.out);
            System.exit(-1);
        }

        executionThreadPool.shutdown();
    }
}