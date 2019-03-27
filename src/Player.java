import java.util.Scanner;

public class Player {

	int numTurnsHasTaken;
	int score;

	public Player() {
		numTurnsHasTaken = 0;
		score = 0;
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
	public int getMove() {
		Scanner scanObj = new Scanner(System.in);
		System.out.print("Enter index of your houses: ");
		numTurnsHasTaken++;
		return scanObj.nextInt();
	}
}
