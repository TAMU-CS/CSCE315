import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Board {

	//board data structure, 2x6 board to represent houses for seeds
	private int[][] board; // = new int[2][6];
	private int houses;
	private int seeds;

	//score datastructure for keep track of scores
	private int[] score = new int[2];
	private int[] playerTracker = new int[2];
	private int playerturn;

	//player objects, correspond with their score index
	private Player[] players = new Player[2];

	// Flag that keeps track if Player 2 chose Pie Rule
	boolean switchOn = false;

	/*
	 * Default constructor, initiates an empty kalah board with players
	 */
	public Board(int numHouses, int numSeeds, Boolean rando) {
		System.out.println(rando);
		if(numHouses > 3 && numHouses < 10) {
			board = new int[2][numHouses];
			houses = numHouses;
		}
		else {
			board = new int[2][6];
			houses = 6;
		}

		// init board
		if(numSeeds > 0 && numSeeds < 11) {
			seeds = numSeeds;
		}
		else {
			seeds = 4;
		}

		if(rando == true) {
			//System.out.println("Debug");
			int totalSeeds = seeds * houses;
			int sum = 0;
			int tempSeed;
			int range = 2;
			int[] randHouses = new int[houses];
			Random generator = new Random();

			//Generate an array of random values along a normal distribution
			for(int i = 0; i < houses; i++) {
				if(seeds == 2) {
					range = 1;
				}
				double randDouble = generator.nextGaussian() * range + seeds;
				tempSeed = (int) Math.round(randDouble);
				if(tempSeed < 0) {
					tempSeed *= -1;
				}
				sum += tempSeed;
				randHouses[i] = tempSeed;
			}

			int dif = totalSeeds - sum;

			//Debug: Display Difference
			System.out.println("Diff: " + dif);

			//Check difference in totals
			if(dif > 0) {
				int min = 0;
				for(int i = 0; i < randHouses.length; i++) {
					if(randHouses[i] < randHouses[min]) {
						min = i;
					}
				}
				randHouses[min] += dif;
			}
			else if(dif < 0) {
				int max = 0;
				for(int i = 0; i < randHouses.length; i++) {
					if(seeds == 1 && dif < 0 && randHouses[i] > 0) {
						randHouses[i] -= 1;
						dif += 1;
					}
					if(randHouses[i] > randHouses[max] && seeds != 1) {
						max = i;
					}
				}
				if(seeds != 1) {
				randHouses[max] += dif;
				}
			}

			//Debug: Display Random Values
			for(int i = 0; i < houses; i++) {
				System.out.println(randHouses[i]);
			}

			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < houses; j++) {
					if(seeds == 1) {
						board[i][j] = 1;
					}
					else {
						board[i][j] = randHouses[j];
					}
				}
			}
		}
		else {
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < houses; j++) {
					board[i][j] = seeds;
				}
			}
		}

		//initiate score
		score[0] = 0;
		score[1] = 0;

		//player turn is set to player 1 by default
		playerturn = 0;

		//player initiation
		players[0] = new Player(0);
		players[1] = new Player(1);

		//begin the first turn
		//This is incorrect, we need to check if there is a possible move!
		while(!endgame(GetMoves(playerturn))) {
			//keep getting next turns, which call move for the player
			NextTurn();

			//display the current Kalah state
			//use print temporarily for now
			//printBoard();
			//getPlayerScores();
		}

		//display who won, etc.
		if(players[0].getScore() > players[1].getScore()) {
			System.out.println("Player 0 won!");
		}else if(players[1].getScore() > players[0].getScore()) {
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
			for(int j = 0; j < houses; j++) {
				if(i == 0) {
					System.out.print(board[1][houses-1-j]);
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
		System.out.print("Player " + players[0].getSide() + ": " + players[0].getScore() + "\n");
		System.out.print("Player " + players[1].getSide() + ": " + players[1].getScore() + "\n");
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
			if(indexTemp == houses) {
				//Player 1 scores
				if(row == rowTemp && row == 0) {
					//Debug
					//System.out.println("Player 0 Scores");
					//score[0] += 1;
					players[row].incrementScore();
					if(i == numMoves - 1) {
						return true;
					}
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 scores
				else if(row == 1 && rowTemp == row) {
					//Debug
					//System.out.println("Player 1 Scores");
					//score[1] += 1;
					System.out.println("Row: " + row + " Player: " + players[row].getSide());
					players[row].incrementScore();
					if(i == numMoves - 1) {
						return true;
					}
					rowTemp = 0;
					indexTemp = -1;
				}
				//Player 1 moves from side 2 to side 1
				else if(rowTemp == 0) {
					board[1][0] += 1;
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 moves from side 1 to side 2
				else {
					board[0][0] += 1;
					rowTemp = 0;
					indexTemp = -1;
				}
			}
			else {
				board[rowTemp][indexTemp] += 1;
				//Check if the last piece is deposited in an empty spot on the player's side
				if(i == numMoves - 1 && board[rowTemp][indexTemp] == 1 && row == rowTemp) {
					if(rowTemp == 0) {
						int addToScore = board[1][5 - indexTemp] + board[rowTemp][indexTemp];
						board[1][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;

						//score[0] += addToScore;
						players[row].updateScoreWithInt(addToScore);
					}
					if(rowTemp == 1) {
						int addToScore = board[0][5 - indexTemp] + board[rowTemp][indexTemp];
						board[0][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;

						//score[1] += addToScore;
						players[row].updateScoreWithInt(addToScore);
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
			/*
			 * put error checking for move here:
			 * continuously ask for moves if plr inputs incorrect move
			 */
			printBoard();
			getPlayerScores();


			// Pie Rule
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

					//int tempScore = score[0]; // swap the scores
					//score[0] = score[1];
					//score[1] = tempScore;

					int tempScore = players[0].getScore();
					players[0].score = players[1].getScore();
					players[1].score = tempScore;
					plr = 1;
					playerturn = 0;
					switchOn = true;

					System.out.print("Switched scores");
					getPlayerScores();
				}
			} // End Pie Rule
			System.out.println("Player " + playerturn);

			move = players[plr].getMove(0);

			// Check for Out of Bounds
			while(move < 0 || move > 5) {
				System.out.println("Index out of bounds. Try again.");
				move = players[plr].getMove(0);
			}

			// Now figure out possible moves for this player
			int[][] possibleMoves = GetMoves(plr);

			// Now check if player picked a valid house (house must have stones in it)
			if(plr == 0) { // this player can only access the 0th row
				while(possibleMoves[plr][move] == 0) { // player picked a house with empty stones
					System.out.println("Cannot pick empty house! Try again.");
					move = players[plr].getMove(0);
				}
			} else {
				while(possibleMoves[plr][move] == 0) { // player picked a house with empty stones
					System.out.println("Cannot pick empty house! Try again.");
					move = players[plr].getMove(0);
				}
			}
		} while( Move(plr, move) );

		playerturn = (playerturn == 1) ? 0 : 1;
	}

	/*
	 * Main, runs board
	 */
	public static void main(String[] args) {
		Scanner newObj = new Scanner(System.in);
		System.out.println("Enter # of houses, # of seeds, and if random");
		int houseIn = newObj.nextInt();
		int seedsIn = newObj.nextInt();
		Boolean randIn = newObj.nextBoolean();
		Board board = new Board(houseIn, seedsIn, randIn);
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

		System.out.println("Endgame");

		return true;
	}

}
