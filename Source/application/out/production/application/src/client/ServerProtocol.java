package client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
//import java.io.*;

import java.net.*;

public class ServerProtocol {
    String  serverHostName = null;
    Integer serverHostPort = null;

    Socket serverSocket;
    ObjectOutputStream out_stream;
    ObjectInputStream  in_stream;

    public ServerProtocol(String  serverHostName, Integer serverHostPort)
    {
        this.serverHostName = serverHostName;
        this.serverHostPort = serverHostPort;

        // Make sure this variables were set
        if (serverHostName != null && serverHostPort != null){
            try {
                serverSocket = new Socket(serverHostName, serverHostPort);
                // IO streams
                ObjectOutputStream out_stream = new ObjectOutputStream(serverSocket.getOutputStream());
                ObjectInputStream  in_stream  = new ObjectInputStream (serverSocket.getInputStream ());
            }
            catch (UnknownHostException e) {
                System.err.println("Unknown Host: " + serverHostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " + serverHostName);
                System.exit(1);
            }
        }
    }
    public ServerProtocol()
    {}

    public Session tempSession()
    {
        Session abay = new Session(0, "Abay","Bektursun","127.0.0.1");

        abay.contacts.add(new Contact("John","Mccullough",0, 1,"0"));
        abay.contacts.add(new Contact("Frank","Arnold",   0, 2,"0"));
        abay.contacts.add(new Contact("Aria","Melendez",  1, 3,"localhost"));
        abay.contacts.add(new Contact("Kiley","Maddox",   1, 4,"localhost"));
        abay.contacts.add(new Contact("Alexa","Schmidt",  2, 5,"0"));
        abay.contacts.add(new Contact("Camryn","Kaiser",  2, 6,"0"));
        abay.contacts.add(new Contact("Veronica","Pace",  2, 7,"0"));

        return abay;
    }

    public void passwordLessLogin(String passwordLessUserName)
    {
        ArrayList<Object> out_data = new ArrayList<Object>();
        //TODO! Correct the request code
        out_data.add(-999);
        out_data.add(passwordLessUserName);
        try {
            out_stream.writeObject(out_data);
        }catch (Exception exc){exc.printStackTrace();}
    }

    public void updateStatus(int status)
    {
        ArrayList<Object> out_data = new ArrayList<Object>();
        //TODO! Correct the request code
        out_data.add(-999);
        out_data.add(status);
        try {
            out_stream.writeObject(out_data);
        }catch (Exception exc){exc.printStackTrace();}
    }

    public void sendFace(BufferedImage faceFrame)
    {
        ArrayList<Object> out_data = new ArrayList<Object>();
        //TODO! Correct the request code
        out_data.add(-999);
        out_data.add(faceFrame);
        try {
            out_stream.writeObject(out_data);
        }catch (Exception exc){exc.printStackTrace();}
    }


    public void closeServerConnection()
    {
        try {
            out_stream.close();
            in_stream.close();
            serverSocket.close();
        }catch (Exception exc){exc.printStackTrace();}
    }

}