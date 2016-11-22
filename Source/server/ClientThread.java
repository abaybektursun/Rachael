package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

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
            
            while (true){
                // Dangerous game right here
                ArrayList<Object> in_data;
                try{
                        // This will result EOFException if there is no more data in the queue
                        in_data  = (ArrayList<Object>)in_stream.readObject();
                        // TODO! Write down request scenario codes
                        int scenario = (Integer)in_data.get(0);
                        
                        switch (scenario) {
                            case 1: clientP.TestProcess(scenario);
                                    break;
                            case 2: clientP.TestProcess2((String)in_data.get(1));
                                    break;
                                    
                            // Face recognition -----------------------------------------------
                            // Start session with face image ...
                            case 3: clientP.startRecognition  ((BufferedImage)in_data.get(1));
                                    break;
                            // ... continue recognition process
                            case 4: clientP.processRecognition((BufferedImage)in_data.get(1));
                                    break;
                            // ... make a prediction (recognize)
                            case 5: clientP.resultRecognition ((BufferedImage)in_data.get(1));
                                    break;
                            //-----------------------------------------------------------------
                        }
                }
                catch (SocketTimeoutException toe){toe.printStackTrace(); break;}
                // Empty Stream, or it's ended
                // Assuming this is fine case
                catch (EOFException eofe){}
                catch (IOException ioe){ioe.printStackTrace(); break;}
                catch (ClassNotFoundException cnfe){cnfe.printStackTrace(); break;}
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}