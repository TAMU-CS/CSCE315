import java.util.Scanner;

public class Player {

	public Player() {

	}


	/*
	 * GetMove, returns the i, j position of the mancala house
	 * the player attempts to move
	 */
	public int getMove() {
		Scanner scanObj = new Scanner(System.in);
		System.out.println("Enter index of your houses:");

		return scanObj.nextInt();
	}
}
