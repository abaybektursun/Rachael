//Client
package server;

import java.io.*;
import java.net.*;

public class ClientConn
{
    private Socket SUCC;
    
    private Scanner     input;
    private PrintWriter output;

    public String client_id;
    
    //--------------------------------------------------------------
    public ClientConn(final Socket SUCC) 
    {
        this.SUCC = SUCC;
        
        try 
        {
            this.output = new PrintWriter(SUCC.getOutputStream());
            this.input  = new Scanner(SUCC.getInputStream());
        } 
        catch (IOException ex) {ex.printStackTrace();}
    
        this.client_id = input.nextLine();
    }
    //--------------------------------------------------------------
    
    public synchronized void send(ClientConn sender, String message) {
        try {
            synchronized (out) 
            {
                out.format("[%s](%s) %s\n", now(), sender.getName(), message);
                out.flush();
            }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            disconnect();
        }
    }
    
    public void disconnect() {
    if (SUCC != null && !SUCC.isClosed()) {
        try 
        {
            SUCC.close();
        }
        catch (IOException ex) {ex.printStackTrace();}
    }

        onDisconnectListener.onDisconnect();
    }

}