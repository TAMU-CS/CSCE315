package test;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Server {
	// socket to receive info from client sockets
	private ServerSocket serverSocket;
	public ClientHandler chs[];

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
		//check for ai
		if(BoardScene.isAI) {
			tid = 1;
			while (tid < 2) {
				chs[tid] = new ClientHandler(serverSocket.accept());
				chs[tid].run();
				tid++;
			}
		}else {
			while (tid < 2) {
				chs[tid] = new ClientHandler(serverSocket.accept());
				chs[tid].run();
				tid++;
			}			
		}
	}

	public void stop() throws IOException {
		serverSocket.close();
	}
	
	//send message and return response to chs
	public String sendMsg(int index, String msg) throws IOException {
		chs[index].out.println(msg);
		return chs[index].in.readLine();
	}

	public static class ClientHandler extends Thread {
		private Socket clientSocket;
		public PrintWriter out;
		public BufferedReader in;

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