package com.connerblair.old;

import java.net.DatagramPacket;
import java.net.SocketException;

public abstract class UDPServer {
	private static final String THREAD_NAME = "UDPServer";
	private int port;
	
	private UDPServerThread serverThread;
	
	public UDPServer(int port) {
		setPort(port);
	}
	
	public final boolean start() {
		if (serverThread != null && serverThread.isRunning()) {
			return true;
		}		
		
		try {
			serverThread = new UDPServerThread(port, (x) -> this.packetRecieved(x));
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		
		new Thread(serverThread).start();
		
		return true;
	}
	
	public final boolean stop() {
		if (serverThread == null || !serverThread.isRunning()) {
			return true;
		}
		
		serverThread.setRunning(false);
		
		return true;
	}
	
	public final int getPort() {
		return port;
	}
	
	public final void setPort(int port) {
		this.port = port;
	}
	
	protected abstract DatagramPacket packetRecieved(DatagramPacket received);
}
