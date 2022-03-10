//COSC 4360 Assignment 3
//Winslow Conneen
//Simulation of UDP sending and receiving practices in java

import java.net.*;
import java.text.DecimalFormat;
import java.io.*;
import javax.swing.JOptionPane;

public class WConneenUDPClient 
{

	public static void main(String[] args) 
	{
		DecimalFormat df = new DecimalFormat("0.00");	//Create decimal format for results
		
		String serverName = "localhost";
		//String serverName = "192.168.1.152";
		int port = 10999;
		
		byte ping_byte = (byte)0xAA; //set byte equivalent to '10101010'
		
		byte [] message = new byte [1000]; //create and fill a byte array for byte
		
		for ( int i = 0; i < 1000; i++)
		{
			message[i] = ping_byte;
		}
		
		String message_S = new String(message);		//convert byte array to string
		
		int totalRTT = 0;
		
		for(int j = 0; j < 10; j++)
		{
			try 
			{
				DatagramSocket clientSocket = new DatagramSocket();
				
				InetAddress IPAddress = InetAddress.getByName(serverName);
				
				System.out.println("UDP Client says: " + message_S);
				
				DatagramPacket sendPacket = new DatagramPacket(message, message.length, IPAddress, port);
				
				//begin recording time
				long startTime = System.nanoTime();		
				clientSocket.send(sendPacket);
				
				byte[] receiveData = new byte[1024];
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				clientSocket.receive(receivePacket);
				//stop recording time
				long endTime = System.nanoTime();
				
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
				
				if(confirmation)
				{
					int RTT = (int) (endTime - startTime);
					System.out.println("No loss found in the message. \n"
							+ "Round Trip Time: " + RTT + " Nanoseconds");
					totalRTT += RTT;
					
					System.out.println("UDP Server says: " + receivedSentence);
				}
				
				clientSocket.close();
					
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} 
		}
		
		double avgRTT = totalRTT/10;
		
		//format and print results
		System.out.println("Average Round Trip Time: " + df.format(avgRTT/1000000) + "ms\nAverage Data Rate: " + df.format((16/(avgRTT/1000000))) +  "Mbps");
	} 

}
