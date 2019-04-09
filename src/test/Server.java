package test;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Server {
	// socket to receive info from client sockets
	private ServerSocket serverSocket;

	// constructor
	public Server() {
	}

	// starts server to listen in an infinite loop to handle client handler threads
	public void start(int port) throws IOException {

		// server socket setup with plr id
		serverSocket = new ServerSocket(port);
		int tid = 0;

		// loop through client handlers and set up their sockets
		while (tid < 2) {
			ClientHandler ch;
			ch = new ClientHandler(serverSocket.accept());
			ch.run();
			tid++;
		}
		// initiate the board in server class
	}

	public void stop() throws IOException {
		serverSocket.close();
	}

	public static class ClientHandler extends Thread {
		private Socket clientSocket;
		public PrintWriter out;
		private BufferedReader in;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				out.println("1");
				out.println("3");
				out.println("2");
				out.println("4");
				out.println("0");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void closeSocket() throws IOException {
			in.close();
			out.close();
			clientSocket.close();
		}

	}

	public static void main(String[] args) throws IOException {
		// initiate the ui
		Kalah.main(args);
	}

}