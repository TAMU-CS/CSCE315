package test;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client extends Thread {
	private static Socket clientSocket;
	private static PrintWriter out;
	private static BufferedReader in;

	public static void startConnection(String ip, int port) throws IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		// io
		Scanner scanObj = new Scanner(System.in);
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			// run a thread to process server response
			String tokens[] = inputLine.split(" ");
			Worker thread = new Worker(tokens);
			thread.start();
		}
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
	private static class ClientRunner extends Thread {
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

	private static class Worker extends Thread {
		String tokens[];

		public Worker(String _tokens[]) {
			tokens = _tokens;
		}

		public void run() {
			String opt = tokens[0];

			switch (opt) {
			case "1":
				System.out.println("Message Command Received-" + opt);
				break;
			case "2":
				System.out.println("Message Command Received-" + opt);
				break;
			case "3":
				System.out.println("Message Command Received-" + opt);
				break;
			case "4":
				System.out.println("Message Command Received-" + opt);
				break;
			default:
				System.out.println("Error Message Command");
				break;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ClientRunner cl = new ClientRunner("127.0.0.1", 6666);
		cl.start();
	}
}