package old;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client extends Thread {
	private static Socket clientSocket;
	private static PrintWriter out;
	private static BufferedReader in;
	private static Player plr;

	public static void startConnection(String ip, int port) throws IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		// io
		Scanner scanObj = new Scanner(System.in);
		String inputLine;

		// 1. welcome message read in
		inputLine = in.readLine();
		System.out.println(inputLine);
		// 2. setup board configuration
		inputLine = in.readLine();
		System.out.println(inputLine);
		String confTokens[] = inputLine.split(" ");
		out.println("READY");
		inputLine = in.readLine();
		String boardTokens[] = inputLine.split(" ");
		
		
		// define board conf variables
		int houses = Integer.parseInt(confTokens[1]);
		int seeds = Integer.parseInt(confTokens[2]);
		int timeToMove = Integer.parseInt(confTokens[3]);
		int id = Integer.parseInt(confTokens[4]);
		int oplrId = id == 1 ? 0 : 1;
		Player oplr = new Player(oplrId, false);

		// create player object
		plr = new Player(id, false);
		System.out.println("Hello Player " + plr.side + "!");

		// update boardscene ui configuratio
		Board board = new Board(boardTokens, plr, oplr);
		BoardScene.updateInputConfiguration(0, out, in, board, plr);
		System.out.println(inputLine);

		// 3. acknowledge that input was received

		int inputCounter = 0;
		while ((inputLine = in.readLine()) != null) {
			// parse through input line and create board information
			String tokens[] = inputLine.split(" ");

			// check for different cases
			// System.out.println(inputLine);

			// display board and update state
			int opt = Integer.parseInt(tokens[0]);
			if (opt == 1 || opt == 2) {
				// initiate other players
				// LAST ERROR, this portion was unresponsive!
				oplrId = plr.side == 1 ? 0 : 1;

				oplr = new Player(oplrId, false);

				// initiate board
				board = new Board(tokens, plr, oplr);

				// print board state
				System.out.println("\n\nBoard State:");

				board.printBoard();
				board.getPlayerScores(-1);

				// update the board
				BoardScene.updateBoard(board);
				BoardScene.inputCounter = true;
			} else if (opt == 3) { // game ended
				// display the winner
				System.out.println("Winner: ");
				System.out.println(inputLine);

				break;
			} else if (opt == 4) { //just update the board
				// initiate other players
				// LAST ERROR, this portion was unresponsive!
				oplrId = plr.side == 1 ? 0 : 1;

				oplr = new Player(oplrId, false);

				// initiate board
				board = new Board(tokens, plr, oplr);

				// print board state
				System.out.println("\n\nBoard State:");

				board.printBoard();
				board.getPlayerScores(-1);

				// update the board
				BoardScene.updateBoard(board);				
			}

			// getting input from cli
			// String resp = BoardScene.getInput(timeToMove) + "";

//        	String resp = plr.getMove(0, opt) + "";
//        	if(resp.length() > 0) {
//        		out.println(resp); //response
//        	}
			inputCounter++;
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

	public static void main(String[] args) throws IOException {
		ClientRunner cl = new ClientRunner("127.0.0.1", 6666);
		cl.start();

		//start up the home scene
		HomeScene.main(args);
	}
}