package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientThread extends Thread {
    private Socket clientSocket = null;

    public ClientThread(Socket socket) {
        super("ClientThread");
        this.clientSocket = socket;
        // Debug
        System.out.println(this.clientSocket.getRemoteSocketAddress().toString() + " Connected");
    }
    
    public void run() {

        try (
            // IO streams
            ObjectOutputStream out_stream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream  in_stream  = new ObjectInputStream (clientSocket.getInputStream ());
        ) { 

            
            ClientProtocol clientP = new ClientProtocol();
            
            // Dangerous game
            ArrayList<Object> in_data;
            try{
                while (true)
                {
                    in_data  = (ArrayList<Object>)in_stream.readObject();
                    int test = (Integer)in_data.get(0);
                    
                    switch (test) {
                        case 1: clientP.TestProcess(test);
                                break;
                    }
                }
            }catch (SocketTimeoutException toe){
                toe.printStackTrace();
            }catch (EOFException eofe){
                // Empty Stream, or it's ended
                // Assuming this is fine case
            }catch (IOException ioe){
                ioe.printStackTrace();
            }catch (ClassNotFoundException cnfe){
                cnfe.printStackTrace(); 
            }

            clientSocket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}