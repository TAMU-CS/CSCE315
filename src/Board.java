import java.util.Arrays;
import java.util.Scanner;

public class Board {

	//board data structure, 2x6 board to represent houses for seeds
	private int[][] board = new int[2][6];

	//score datastructure for keep track of scores
	private int[] score = new int[2];
	private int playerturn;

	//player objects, correspond with their score index
	private Player[] players = new Player[2];

	// Flag that keeps track if Player 2 chose Pie Rule
	boolean switchOn = false;

	/*
	 * Default constructor, initiates an empty kalah board with players
	 */
	public Board() {
		// init board
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 6; j++) {
				board[i][j] = 4;
			}
		}

		//initiate score
		score[0] = 0;
		score[1] = 0;

		//player turn is set to player 1 by default
		playerturn = 0;

		//player initiation
		players[0] = new Player();
		players[1] = new Player();

		//begin the first turn
		//This is incorrect, we need to check if there is a possible move!
		while(!endgame(GetMoves(playerturn))) {
			//keep getting next turns, which call move for the player
			NextTurn();

			//display the current Kalah state
			//use print temporarily for now
			printBoard();
			getPlayerScores();
		}

		//display who won, etc.
		if(score[0] > score[1]) {
			System.out.println("Player 0 won!");
		}else if(score[1] > score[0]) {
			System.out.println("Player 1 won!");
		}else {
			System.out.println("TIE!");
		}
	}

	/*
	 * Print board, prints out the current kalah board state
	 */
	public void printBoard() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 6; j++) {
				if(i == 0) {
					System.out.print(board[1][5-j]);
				}
				if(i == 1) {
					System.out.print(board[0][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void getPlayerScores() {
		System.out.print("Player 0: " + score[0] + "\n");
		System.out.print("Player 1: " + score[1] + "\n");
	}

	/*
	 * Move takes the row and index on mancala board, and
	 * tries to move that house. Return false if unsuccessful,
	 * true otherwise
	 */
	public boolean Move(int row, int index) {
		//Setup temporary variables
		int numMoves = board[row][index];
		board[row][index] = 0;
		int indexTemp = index;
		int rowTemp = row;

		for(int i = 0; i < numMoves; i++) {
			indexTemp++;
			//Check if at the end of the row
			if(indexTemp == 6) {
				//Player 1 scores
				if(playerturn == 0 && rowTemp == 0) {
					//Debug
					//System.out.println("Player 0 Scores");
					score[0] += 1;
					if(i == numMoves - 1) {
						return true;
					}
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 scores
				else if(playerturn == 1 && rowTemp == 1) {
					//Debug
					//System.out.println("Player 1 Scores");
					score[1] += 1;
					if(i == numMoves - 1) {
						return true;
					}
					rowTemp = 0;
					indexTemp = -1;
				}
				//Player 1 moves from side 2 to side 1
				else if(rowTemp == 0) {
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 moves from side 1 to side 2
				else {
					rowTemp = 0;
					indexTemp = -1;
				}
			}
			else {
				board[rowTemp][indexTemp] += 1;
				//Check if the last piece is deposited in an empty spot on the player's side
				if(i == numMoves - 1 && board[rowTemp][indexTemp] == 1 && playerturn != rowTemp) {
					if(rowTemp == 0) {
						int addToScore = board[1][5 - indexTemp] + board[rowTemp][indexTemp];
						board[1][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;
						score[0] += addToScore;
					}
					if(rowTemp == 1) {
						int addToScore = board[0][5 - indexTemp] + board[rowTemp][indexTemp];
						board[0][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;
						score[1] += addToScore;
					}
				}
			}
		}
		return false;
	}

	/*
	 * GetMoves returns an array of possible moves on the mancala board
	 * 0 means not possible move, 1 means possible move
	 */
	public int[][] GetMoves(int plr) {
		//player 1 has control over left half
		//player 2 has control over right half
		int [][] moves = new int[2][6];
		int oplr = (plr == 1) ? 0 : 1;
		for(int i = 0; i < 6; i++){
			moves[plr][i] = 1;
			moves[oplr][i] = 0;

			//make sure there are pebbles in that house
			if(board[plr][i] == 0) {
				moves[plr][i] = 0;
			}
		}

		return moves;
	}

	/*
	 * NextTurn function queries player to return with input for the player
	 */
	private void NextTurn() {
		//query the current player for the next turn

		// plr is dependent if the players are switched from Pie Rule
		int plr;
		if(!switchOn) {
			plr = playerturn; // plr is normal
		} else { // Player 0 chose to switch
			if(playerturn == 0) {
				plr = 1; // Player 0 controls Player 1's stuff
			} else {
				plr = 0; // Player 1 controls Player 0's stuff
			}
		}

		int move;
		do {
			System.out.println();
			printBoard();

			if(plr == 1 && players[1].numTurnsHasTaken == 0) { // Player 2 now has option to do Pie Rule
				System.out.println("Player 1. You have 2 options");
				System.out.println("1. Continue with your turn");
				System.out.println("2. Swap places with Player 1");
				System.out.print("Enter your choice: ");

				Scanner scanObj = new Scanner(System.in);

				int choice = scanObj.nextInt();
				while(choice < 1 || choice > 2) {
					System.out.print("Enter a valid choice: ");
					choice = scanObj.nextInt();
				}

				if(choice == 2) { // Player 2 chooses to swap
					Player playerTemp = players[0];
					players[0] = players[1]; // Player 1 becomes Player 2
					players[1] = playerTemp; // Player 2 becomes Player 1

					int score = players[0].score


					plr = 1;
					playerturn = 0;
					switchOn = true;
				}
			}
			System.out.println("Player " + playerturn);

			move = players[plr].getMove();

			// Check for Out of Bounds
			while(move < 0 || move > 5) {
				System.out.println("Index out of bounds. Try again.");
				move = players[plr].getMove();
			}

			// Now figure out possible moves for this player
			int[][] possibleMoves = GetMoves(plr);

			// Now check if player picked a valid house (house must have stones in it)
			if(plr == 0) { // this player can only access the 0th row
				while(possibleMoves[plr][move] == 0) { // player picked a house with empty stones
					System.out.println("Cannot pick empty house! Try again.");
					move = players[plr].getMove();
				}
			} else {
				while(possibleMoves[plr][move] == 0) { // player picked a house with empty stones
					System.out.println("Cannot pick empty house! Try again.");
					move = players[plr].getMove();
				}
			}
		} while( Move(plr, move) );

		playerturn = (playerturn == 1) ? 0 : 1;
	}

	/*
	 * Main, runs board
	 */
	public static void main(String[] args) {
		Board board = new Board();
	}

	/*
	 * Checks if the side of the current player is empty, adding all the other players seeds to their score
	 */
	public boolean endgame(int[][] availableMoves) {
		for(int i = 0; i < 6; i++) {
			if(availableMoves[playerturn][i] != 0) {
				return false;
			}
		}
		int scoringPlayer = playerturn == 1 ? 0 : 1;
		int sum = 0;
		for(int i = 0; i < 6; i++) {
			sum += board[scoringPlayer][i];
		}
		score[scoringPlayer] += sum;
		return true;
	}

}
