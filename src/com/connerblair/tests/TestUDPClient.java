package com.connerblair.tests;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.connerblair.old.UDPClient;

public class TestUDPClient extends UDPClient {

	public TestUDPClient(String serverAddress, int serverPort) {
		super(serverAddress, serverPort);
	}

	@Override
	protected DatagramPacket createPacket() {
		byte[] buf = new String("ping").getBytes();
		return new DatagramPacket(buf, buf.length, serverAddress, serverPort);
	}

	@Override
	protected void handlePacketReceived(DatagramPacket packet) {
		String msg = new String(packet.getData(), 0, packet.getLength());
		System.out.println("Message received: " + msg);
	}

	@Override
	protected void handleClientError(Exception e) {
		e.printStackTrace();
	}
	
	public static void main(String[] args) {
		TestUDPClient client = new TestUDPClient("localhost", 4435);
		client.sendPacket();
		client.sendPacket();
		client.sendPacket();
	}
}
