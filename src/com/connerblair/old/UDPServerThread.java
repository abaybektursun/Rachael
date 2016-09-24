package com.connerblair.old;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.function.Function;

public class UDPServerThread implements Runnable {
	private boolean running = false;
	
	private DatagramSocket socket;
	private Function<DatagramPacket, DatagramPacket> packetReceived;
	
	public UDPServerThread(int port, Function<DatagramPacket, DatagramPacket> packetReceived) throws SocketException {
		socket = new DatagramSocket(port);
		this.packetReceived = packetReceived;
		running = true;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		while (running) {
			byte[] buffer = new byte[256];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
				continue;
			}
			
			packet = packetReceived.apply(packet);
			
			if (packet == null) {
				continue;
			}
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
				continue;
			}
		}
		
		socket.close();
	}
}
