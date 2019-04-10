package test;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client extends Thread {
	private static Socket clientSocket;
	public static PrintWriter out;
	public static BufferedReader in;
	private static boolean debugging = true;

	public static void startConnection(String ip, int port) throws IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		String resp = in.readLine();
		return resp;
	}

	public void stopConnection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}

	// thread class that runs the client start connection
	public static class ClientRunner extends Thread {
		String tempIP;
		int tempPort;

		public ClientRunner(String IP, int Port) {
			tempIP = IP;
			tempPort = Port;
		}

		public void run() {
			try {
				Client.startConnection(tempIP, tempPort);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ClientRunner cl = new ClientRunner("127.0.0.1", 6666);
		cl.start();
	}
}