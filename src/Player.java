import java.util.Scanner;

public class Player {
	Server.ClientHandler clientHandler;
	boolean serversided;

	/*
	 * default constructor
	 */
	public Player() {
		serversided = true;
	}
	
	/*
	 * construct player with client handler
	 */
	public Player(Server.ClientHandler ch) {
		clientHandler = ch;
		serversided = false;
	}

	/*
	 * GetMove, returns the i, j position of the mancala house
	 * the player attempts to move
	 */
	public int getMove(int timeForMove) {
		if(serversided) {
			Scanner scanObj = new Scanner(System.in);
			System.out.println("Enter index of your houses:");
			return scanObj.nextInt();
		}else {
			return clientHandler.getMove(timeForMove);
		}
	}
}
