package testClient;

import java.io.*;
import java.net.*;
import java.util.*;

public class TestClient {

    public static void main(String[] args) throws IOException {


        
        String hostName = "localhost";
        int portNumber  = 3141;

        try (
            Socket serverSocket = new Socket(hostName, portNumber);
            
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
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }

    }
}
