import java.util.Scanner;

public class Player {
	Server.ClientHandler clientHandler;
	boolean consoleInput;
	int numTurnsHasTaken;
	int score;
	
	/*
	 * default constructor
	 */
	public Player() {
		consoleInput = true;
		numTurnsHasTaken = 0;
		score = 0;
	}

	
	/*
	 * construct player with client handler
	 */
	public Player(Server.ClientHandler ch) {
		clientHandler = ch;
		consoleInput = false;
	}

	public int getNumTurns() {
		return numTurnsHasTaken;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		score++;
	}

	public void updateScoreWithInt(int _score) {
		score += _score;
	}

	/*
	 * GetMove, returns the i, j position of the mancala house
	 * the player attempts to move
	 */
	public int getMove(int timeForMove) {
		numTurnsHasTaken++;
		if(consoleInput) {
			Scanner scanObj = new Scanner(System.in);
			System.out.println("Enter index of your houses:");
			return scanObj.nextInt();
		}else {
			return clientHandler.getMove(timeForMove);
		}
	}
}
