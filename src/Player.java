import java.util.Scanner;

public class Player {

	private int score;
	private int seedsAtPlay; // the amount of seeds
							// that the player chooses to play

	public Player() {
		score = 0;
		seedsAtPlay = 0;
	}

	public void pickedUpSeeds(int seeds) {

	}

	/*
	 * GetMove, returns the i, j position of the mancala house
	 * the player attempts to move
	 */
	public int[] getMove() {
		Scanner scanObj = new Scanner(System.in);
		System.out.println("Enter index i, j of house you want to move");

		int [] move = new int[2];
		move[0] = scanObj.nextInt();
		move[1] = scanObj.nextInt();

		return move;
	}

	public int getScore() {
		return score;
	}
}
