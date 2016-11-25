package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ChatService {
    final int defaultPort = 2612;
    volatile boolean busy = false;
    volatile boolean callAccepted = false;

    // Default constructor will start the listener
    public ChatService()
    {
        new ChatServer().start();
    }

    public boolean callRequest(String callAddress)
    {
        if(!busy) {
            busy = true;
            try {
                InetAddress address = InetAddress.getByName(callAddress);
                DatagramSocket socket = new DatagramSocket();
                byte[] buf = new byte[128];
                // Request
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, defaultPort);
                socket.send(packet);
                // Response
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                System.out.println("Received: " + packet);

                socket.close();

            }catch (Exception iae) {iae.printStackTrace(); busy = false; return false;}
        }
        else{
            System.out.println("Busy");
            return false;
        }

        return false;
    }

    public boolean getStatus()
    {
        return this.busy;
    }

    class ChatServer extends Thread{
        public void run() {
            synchronized (this) {
            }
        }
    }

    class ChatClinet extends Thread{
        public void run() {
            synchronized (this) {
            }
        }
    }
}