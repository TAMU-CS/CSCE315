import java.util.Arrays;
import java.util.Scanner;

public class Board {

	//board data structure, 2x6 board to represent houses for seeds
	private int[][] board; // = new int[2][6];
	private int houses;
	private int seeds;
	private int timeToMove;

	//score datastructure for keep track of scores
	private int[] score = new int[2];
	private int[] playerTracker = new int[2];
	private int playerturn;

	//player objects, correspond with their score index
	private Player[] players = new Player[2];

	// Flag that keeps track if Player 2 chose Pie Rule
	boolean switchOn = false;

	/*
	 * constructor, initiates kalah board and all other values associated with the board
	 */
	public Board(int numHouses, int numSeeds, Boolean random, int timeForMoves) {
		timeToMove = timeForMoves;
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
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < houses; j++) {
				board[i][j] = seeds;
			}
		}

		//initiate score
		score[0] = 0;
		score[1] = 0;

		//player turn is set to player 1 by default
		playerturn = 0;
	}
	
	/*
	 * StartGame is the main game loop where it players through the game logic
	 */
	public void StartGame(Player p0, Player p1) {
		players[0] = p0;
		players[1] = p1;

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
		System.out.print("Player 0: " + players[0].getScore() + "\n");
		System.out.print("Player 1: " + players[1].getScore() + "\n");
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
				if(playerturn == 0 && rowTemp == 0) {
					//Debug
					//System.out.println("Player 0 Scores");
					//score[0] += 1;
					players[0].incrementScore();
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
					players[1].incrementScore();
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
				if(i == numMoves - 1 && board[rowTemp][indexTemp] == 1 && playerturn == rowTemp) {
					if(rowTemp == 0) {
						int addToScore = board[1][5 - indexTemp] + board[rowTemp][indexTemp];
						board[1][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;

						//score[0] += addToScore;
						players[0].updateScoreWithInt(addToScore);
					}
					if(rowTemp == 1) {
						int addToScore = board[0][5 - indexTemp] + board[rowTemp][indexTemp];
						board[0][5 - indexTemp] = 0;
						board[rowTemp][indexTemp] = 0;

						//score[1] += addToScore;
						players[1].updateScoreWithInt(addToScore);
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
		int length = board[0].length;
		int [][] moves = new int[2][length];
		int oplr = (plr == 1) ? 0 : 1;
		for(int i = 0; i < length; i++){
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
	 * Check Move returns true if valid move or false if not valid move
	 */
	public boolean CheckMove(int plr, int move) {
		//check that the move is within bounds
		if(move < board[0].length && move >= 0) {
			int moveBoard[][] = GetMoves(plr);
			
			//check if the move is a legal move 
			return moveBoard[plr][move] == 1;
		}
		
		return false;
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

//				Scanner scanObj = new Scanner(System.in);
//
//				int choice = scanObj.nextInt();
//				while(choice < 1 || choice > 2) {
//					System.out.print("Enter a valid choice: ");
//					choice = scanObj.nextInt();
//				}
				
				int choice = players[playerturn].getMove(0, 2);
				while(!(choice == 1 || choice == 2)){
					System.out.println("Invalid Pie Rule Input:");
					choice = players[playerturn].getMove(0, 2);					
				}

				if(choice == 2) { // Player 2 chooses to swap
					Player playerTemp = players[0];
					players[0] = players[1]; // Player 1 becomes Player 2
					players[1] = playerTemp; // Player 2 becomes Player 1

					//int tempScore = score[0]; // swap the scores
					//score[0] = score[1];
					//score[1] = tempScore;

					plr = 1;
					playerturn = 0;
					switchOn = true;
				}
			} // End Pie Rule
			System.out.println("Player " + playerturn);

			move = players[plr].getMove(timeToMove, 1);

			// Check for Out of Bounds
			while(move < 0 || move > 5) {
				System.out.println("Index out of bounds. Try again.");
				move = players[plr].getMove(timeToMove, 1);
			}

			// Now figure out possible moves for this player
			int[][] possibleMoves = GetMoves(plr);

			// Now check if player picked a valid house (house must have stones in it)
			while(possibleMoves[plr][move] == 0) { // player picked a house with empty stones
				System.out.println("Cannot pick empty house! Try again.");
				move = players[plr].getMove(timeToMove, 1);
			}
		} while( Move(plr, move) );

		playerturn = (playerturn == 1) ? 0 : 1;
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

	/*
	 * Stringify functions allows for board info to be passed between buffers
	 */
	public String toString() {
		String resp = board[0].length + " ";
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < board[i].length; j++) {
				resp += board[i][j] + " ";
			}
		}
		
		return resp + players[0].getScore() + " " + players[1].getScore();
	}

	/*
	 * Main, runs board
	 */
	public static void main(String[] args) {
		Scanner newObj = new Scanner(System.in);
		System.out.println("Enter # of houses, # of seeds, and if random");
		int houseIn = newObj.nextInt();
		int seedsIn = newObj.nextInt();
		Boolean randIn = newObj.hasNextShort();
		Board board = new Board(houseIn, seedsIn, randIn, 0);
	}
	
}
