package server;

import java.io.*;
import java.net.*;

public class Server
{
    public Connectivity conn;
    
    public Server(){
        conn = new Connectivity();
        
        //Start listening ----------------------//
        try{                                    //
            conn.listen(8300);                   //
        }                                       //
        catch(Exception e){e.printStackTrace();}//
        //--------------------------------------//
        
        System.out.println("asdfa");
    }
    
    public static void main(String args[]) throws Exception 
    {
        new Server();
    }
}
