package server;

import java.io.*;
import java.net.*;

public class Connectivity implements Runnable
{
    Socket c_socket;
    
    Connectivity(Socket c_socket) 
    {
        this.c_socket = c_socket;
    }
    
    Connectivity() 
    {
        
    }
    
    public void listen(int port) throws Exception 
    {
        ServerSocket ssock = new ServerSocket(port);
        System.out.println("Listening");
        
        while (true) 
        {
            Socket sock = ssock.accept();
            System.out.println("Connected");
            new Thread(new Connectivity(sock)).start();
        }
    }
    
    public void run() {
        try
        {
            PrintStream p_stream = new PrintStream(c_socket.getOutputStream());
            
            for (int i = 100; i >= 0; i--) 
            {
                p_stream.println(i + " test");
            }
            p_stream.close();
            c_socket.close();
        }
        
        catch (IOException e) 
        {
            System.out.println(e);
        }
    }
}
