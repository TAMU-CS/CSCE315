package test;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Server {
	// socket to receive info from client sockets
	private ServerSocket serverSocket;
	ClientHandler chs[];

	// constructor
	public Server() {
	}

	// starts server to listen in an infinite loop to handle client handler threads
	public void start(int port) throws IOException {

		// server socket setup with plr id
		serverSocket = new ServerSocket(port);
		chs = new ClientHandler[2];
		int tid = 0;

		// loop through client handlers and set up their sockets
		while (tid < 2) {
			chs[tid] = new ClientHandler(serverSocket.accept());
			chs[tid].run();
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
		//Kalah.main(args);
	}

}