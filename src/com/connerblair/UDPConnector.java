package com.connerblair;

import java.net.DatagramPacket;

public abstract class UDPConnector {
	private UDPThreadManager threadManager;

	protected UDPConnector() {
		threadManager = new UDPThreadManager((e) -> this.handleError(e),
											 (p) -> this.handlePacketReceived(p),
											 ()  -> this.createPacketToSend());
	}
	
	public void start() {
		threadManager.start();
	}
	
	public void stop() {
		threadManager.stop();
	}
	
	public abstract void handleError(Exception e);
	public abstract void handlePacketReceived(DatagramPacket packet);
	public abstract DatagramPacket createPacketToSend();
}
