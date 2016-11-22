package server;

import java.io.*;
import java.net.*;

public class Server {
    
    static boolean listening; 
    
    public static void main(String[] args) throws IOException {

        listening      = true;
        int portNumber = 3141;
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) 
            {
                // Create a thread for every client
                new ClientThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not start the server on port: " + portNumber);
            e.printStackTrace(System.out);
            System.exit(-1);
        }
    }
}