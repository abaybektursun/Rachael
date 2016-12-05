package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class ClientThread extends Thread {
    private Socket clientSocket = null;
    private boolean clientOnline;

    final int CODE_REQUEST_TEST_1 = 1;
    final int CODE_REQUEST_TEST_2 = 2;
    final int CODE_REQUEST_FACE_REC_START    = 3;
    final int CODE_REQUEST_FACE_REC_CONTINUE = 4;
    final int CODE_REQUEST_FACE_REC_FINAL    = 5;

    final int CODE_REQUEST_FACE_MEM_START    = 6;
    final int CODE_REQUEST_FACE_MEM_CONTINUE = 7;
    final int CODE_REQUEST_FACE_MEM_FINAL    = 8;

    final int CODE_REQUEST_STATUS_UPDATE = 9;


    public ClientThread(Socket socket) {
        super("ClientThread");
        this.clientSocket = socket;
        // Debug
        System.out.println(this.clientSocket.getRemoteSocketAddress().toString() + " Connected");
        clientOnline = true;
    }

    public void run() {

        try (
                // IO streams
                ObjectOutputStream out_stream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream  in_stream  = new ObjectInputStream (clientSocket.getInputStream ());
        ) {

            ClientProtocol clientP = new ClientProtocol();

            while (clientOnline){
                // Dangerous game right here
                ArrayList<Object> in_data;
                try{
                    // This will result EOFException if there is no more data in the queue
                    in_data  = (ArrayList<Object>)in_stream.readObject();

                    int scenario = (Integer)in_data.get(0);

                    switch (scenario) {
                        case CODE_REQUEST_TEST_1:
                            clientP.TestProcess(scenario);
                            break;
                        case CODE_REQUEST_TEST_2:
                            clientP.TestProcess2((String)in_data.get(1));
                            break;

                        // Face recognition -----------------------------------------------
                        // Start session with face image ...
                        case CODE_REQUEST_FACE_REC_START:
                            clientP.startRecognition  ((BufferedImage)in_data.get(1));
                            break;
                        // ... continue recognition process
                        case CODE_REQUEST_FACE_REC_CONTINUE:
                            clientP.processRecognition((BufferedImage)in_data.get(1));
                            break;
                        // ... make a prediction (recognize)
                        case CODE_REQUEST_FACE_REC_FINAL:
                            clientP.resultRecognition ((BufferedImage)in_data.get(1));
                            break;
                        //-----------------------------------------------------------------

                        // Face Memorization ----------------------------------------------
                        case CODE_REQUEST_FACE_MEM_START:
                            clientP.startMemorization  ((BufferedImage)in_data.get(1));
                            break;
                        // ... continue recognition process
                        case CODE_REQUEST_FACE_MEM_CONTINUE:
                            clientP.processMemorization((BufferedImage)in_data.get(1));
                            break;
                        // ... make a prediction (recognize)
                        case CODE_REQUEST_FACE_MEM_FINAL:
                            clientP.resultMemorization ((BufferedImage)in_data.get(1));
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