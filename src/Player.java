import java.util.Scanner;

public class Player {
	Server.ClientHandler clientHandler;
	boolean serversided;
	int numTurnsHasTaken;
	int score;
	int side;
	boolean ai;
	
	/*
	 * default constructor
	 */
	public Player(int defSide, boolean type) {
		serversided = true;
		numTurnsHasTaken = 0;
		score = 0;
		side = defSide;
		ai = type;
	}

	
	/*
	 * construct player with client handler
	 */
	public Player(Server.ClientHandler ch, int defSide, boolean type) {
		clientHandler = ch;
		serversided = false;
		numTurnsHasTaken = 0;
		score = 0;
		side = defSide;
		ai = type;
	}

	public int getNumTurns() {
		return numTurnsHasTaken;
	}
	
	public int getSide() {
		return side;
	}
	
	public boolean isAi() {
		return ai;
	}
	
	public void setSide(int newSide) {
		side = newSide;
		return;
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
