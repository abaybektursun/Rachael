package com.connerblair.old;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class UDPClient {
	protected InetAddress serverAddress;
	protected int serverPort;
	
	private DatagramSocket socket;
	
	public UDPClient(String serverAddress, int serverPort) {
		try {
			this.serverAddress = InetAddress.getByName(serverAddress);
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.serverPort = serverPort;
	}
	
	public final boolean sendPacket() {
		try {
			socket = new DatagramSocket();
		} catch (Exception e) {
			handleClientError(e);
		}
		
		DatagramPacket packet = createPacket();
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			handleClientError(e);
		}
		
		byte[] buf = new byte[256];
		
		packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			handleClientError(e);
		}
		
		handlePacketReceived(packet);
		
		return true;
	}
	
	protected abstract DatagramPacket createPacket();
	
	protected abstract void handlePacketReceived(DatagramPacket packet);
	
	protected abstract void handleClientError(Exception e);
}
