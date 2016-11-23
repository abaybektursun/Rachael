package client;

import java.util.ArrayList;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
//import java.io.*;

import java.net.*;

public class ServerProtocol implements Runnable{
    protected volatile boolean runnable = false;
    protected volatile String  hostName = null;
    protected volatile Integer hostPort = null;
    @Override
    public void run() {
        synchronized (this) {
            // Make sure this variables were set
            if (hostName != null && hostPort != null){
                try (
                    Socket serverSocket = new Socket(hostName, hostPort);
                    // IO streams
                    ObjectOutputStream out_stream = new ObjectOutputStream(serverSocket.getOutputStream());
                    ObjectInputStream  in_stream  = new ObjectInputStream (serverSocket.getInputStream ());
                ) {
                    ArrayList<Object> out_data = new ArrayList<Object>();
                    out_data.add(2);
                    out_data.add("This came from client!");
                    out_stream.writeObject(out_data);
                    
                    out_stream.close();
                    in_stream.close();
                    serverSocket.close();
                    
                } catch (UnknownHostException e) {
                    System.err.println("Unknown Host: " + hostName);
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection to " + hostName);
                    //System.exit(1);
                }
            }
        }
    }
}