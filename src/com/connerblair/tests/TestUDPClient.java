package com.connerblair.tests;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.connerblair.UDPConnector;

public class TestUDPClient extends UDPConnector {
	private static int port = 4436;
	
	private String addrServer = "localhost";
	private int portServer = 4435;
	
	private boolean sent = false;
	
	public TestUDPClient() {
		super(port);
	}

	@Override
	public synchronized void handleError(Exception e) {
		System.out.println(e.getMessage());
		
	}

	@Override
	public void handlePacketReceived(DatagramPacket packet) {
		String msg = new String(packet.getData());
		
		System.out.println("From Server: " + msg);
	}

	@Override
	public DatagramPacket createPacketToSend() {
		byte[] msg = new String("ping").getBytes();
		
		if (!sent) {
			sent = true;
			
			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(addrServer);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			return new DatagramPacket(msg, msg.length, addr, portServer);
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		TestUDPClient client = new TestUDPClient();
		client.start();
	}
}
