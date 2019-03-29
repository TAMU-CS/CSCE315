import java.util.Scanner;

public class Player {
	Server.ClientHandler clientHandler;
	boolean serversided;
	int numTurnsHasTaken;
	int score;
	
	/*
	 * default constructor
	 */
	public Player() {
		serversided = false;
		numTurnsHasTaken = 0;
		score = 0;
	}

	
	/*
	 * construct player with client handler
	 */
	public Player(Server.ClientHandler ch) {
		clientHandler = ch;
		serversided = true;
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
		if(serversided) {
			Scanner scanObj = new Scanner(System.in);
			System.out.println("Enter index of your houses:");
			return scanObj.nextInt();
		}else {
			return clientHandler.getMove(timeForMove);
		}
	}
}
