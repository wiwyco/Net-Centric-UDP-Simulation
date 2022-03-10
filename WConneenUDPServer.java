//COSC 4360 Assignment 3
//Winslow Conneen
//Simulation of UDP sending and receiving practices in java

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class WConneenUDPServer 
{

	public static void main(String[] args) 
	{
		
		try 
		{
			DatagramSocket serverSocket = new DatagramSocket(10999); //creates a datagram socket and binds it to port 10999
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			while (true) 
			{
			
				System.out.println("UDP Server waiting for client on port " + serverSocket.getLocalPort() + "...");
				
				//intakes packet
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					
				serverSocket.receive(receivePacket);
				
				//gets IP address and port of client for return signal
				InetAddress IPAddress = receivePacket.getAddress();
				
				int port = receivePacket.getPort();
				
				String receivedSentence = new String(receivePacket.getData());
				
				//data checking method
				boolean confirmation = true;
				for(int i = 0; i < 1000; i++)
				{
					if(receivedSentence.charAt(i) != (char) 0xAA)
					{
						confirmation = false;
						System.out.println("Error found in text, connection closed");
						break;
					}
				}
				System.out.println("No loss found in the message.");
				
				//return signal
				if(confirmation)
				{
					System.out.println("RECEIVED: from IPAddress " + 
					IPAddress + " and from port " + port + " the data: " + receivedSentence);
					
					sendData = receiveData;
					
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					
					serverSocket.send(sendPacket);
	
					Arrays.fill(receiveData, (byte)0); 
	
					Arrays.fill(sendData, (byte)0);
					
					System.out.println();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
