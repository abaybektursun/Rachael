package com.connerblair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UDPThreadManager {
	private int port = -1;
	
	private volatile boolean receiverThreadRunning = false;
	private volatile boolean senderThreadRunning = false;
	
	private Consumer<Exception> functionHandleError;
	private Consumer<DatagramPacket> functionHandlePacketReceived;
	private Supplier<DatagramPacket> functionCreatePacketToSend;
	
	private DatagramSocket socket;
	private DatagramPacket packetToReceive = null;
	
	private SocketReceiver receiver;
	private SocketSender sender;
	
	private Thread receiverThread;
	private Thread senderThread;
	
	public UDPThreadManager(Consumer<Exception> functionHandleError,
							Consumer<DatagramPacket> functionHandlePacketReceived,
							Supplier<DatagramPacket> functionCreatePacketToSend) {
		this.functionHandleError = functionHandleError;
		this.functionHandlePacketReceived = functionHandlePacketReceived;
		this.functionCreatePacketToSend = functionCreatePacketToSend;
	}
	
	public void initialize() {	
		try {
			socket = new DatagramSocket(port);			
			receiver = new SocketReceiver();
			sender = new SocketSender();
		} catch (SocketException e) {
			functionHandleError.accept(e);
		}
	}
	
	public void start() {
		receiverThreadRunning = true;
		senderThreadRunning = true;
		
		receiverThread = new Thread(receiver);
		senderThread = new Thread(sender);
		
		receiverThread.start();
		senderThread.start();
	}
	
	public void stop() {
		receiverThreadRunning = false;
		senderThreadRunning = false;
		
		try {
			receiverThread.join();
		} catch (InterruptedException e) {
			functionHandleError.accept(e);
		}
		
		try {
			senderThread.join();
		} catch (InterruptedException e) {
			functionHandleError.accept(e);
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	private class SocketReceiver implements Runnable {
		public void run() {
			while (receiverThreadRunning) {
				if (packetToReceive == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						functionHandleError.accept(e);
					}
					
					continue;
				}
				
				try {
					socket.receive(packetToReceive);
				} catch (IOException e) {
					functionHandleError.accept(e);
				}
				
				functionHandlePacketReceived.accept(packetToReceive);
			}
		}
		
	}
	
	private class SocketSender implements Runnable {
		public void run() {
			while (senderThreadRunning) {
				DatagramPacket packetToSend = functionCreatePacketToSend.get();
				
				if (packetToSend == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						functionHandleError.accept(e);
					}
					
					continue;
				}
				
				try {
					socket.send(packetToSend);
				} catch (IOException e) {
					functionHandleError.accept(e);
				}
			}
		}
	}
}
