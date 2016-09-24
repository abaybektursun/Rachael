package com.connerblair.tests;

import java.net.DatagramPacket;

import com.connerblair.old.UDPServer;

public class TestUDPServer extends UDPServer {
	
	public TestUDPServer() {
		super(4435);
	}
	
	@Override
	protected DatagramPacket packetRecieved(DatagramPacket received) {
		String msg = new String(received.getData(), 0, received.getLength());
		byte[] buffer = new byte[256];
		
		if (msg.equalsIgnoreCase("ping")) {
			buffer = new String("pong").getBytes();
		} else if (msg.equalsIgnoreCase("stop")) {
			stop();
			return null;
		} else {
			buffer = new String(":(").getBytes();
		}
		
		return new DatagramPacket(buffer, buffer.length, received.getAddress(), received.getPort());
	}
	
	public static void main(String[] args) {
		TestUDPServer server = new TestUDPServer();
		server.start();
	}
}
