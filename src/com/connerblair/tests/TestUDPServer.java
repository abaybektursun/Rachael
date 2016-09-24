package com.connerblair.tests;

import java.net.DatagramPacket;
import java.net.InetAddress;

import com.connerblair.UDPConnector;

public class TestUDPServer extends UDPConnector {
	private static int port = 4435;
	
	private int portRespond = 0;
	private InetAddress addrRespond;
	private String msgRespond;
	
	public TestUDPServer() {
		super(port);
	}

	@Override
	public synchronized void handleError(Exception e) {
		System.out.println(e.getMessage());
	}

	@Override
	public void handlePacketReceived(DatagramPacket packet) {
		String msg = new String(packet.getData());
		
		System.out.println("From client: " + msg);
		
		portRespond = packet.getPort();
		addrRespond = packet.getAddress();
		
		if (msg.equalsIgnoreCase("ping")) {			
			msgRespond = "pong";
		} else {
			msgRespond = "NA";
		}
	}

	@Override
	public DatagramPacket createPacketToSend() {
		byte[] data = msgRespond.getBytes();
		
		return new DatagramPacket(data, data.length, addrRespond, portRespond);
	}
	
	public static void main(String[] args) {
		TestUDPServer server = new TestUDPServer();
		server.start();
	}
}
