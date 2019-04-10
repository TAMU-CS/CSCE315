package old;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Board {

	//board data structure, 2x6 board to represent houses for seeds
	public int[][] board; // = new int[2][6];

	int timeToMove;
	private int psuedoScore;
	int houses;
	int seeds;


	//score datastructure for keep track of scores
	private int[] score = new int[2];
	private int[] playerTracker = new int[2];
	private int playerturn;

	//player objects, correspond with their score index
	Player[] players = new Player[2];

	// Flag that keeps track if Player 2 chose Pie Rule
	boolean switchOn = false;

	/*
	 * constructor that takes in stringified version and creates from there
	 * used for client
	 */
	public Board(String tokens[], Player p0, Player p1) {
		//read in length
		int length = Integer.parseInt(tokens[1]);
		board = new int[2][length];
		houses = length;

		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < length; j++) {
				board[i][j] = Integer.parseInt(tokens[2 + i * length + j]);
			}
		}


		//handle players setup
		int plr0 = Integer.parseInt(tokens[2 + length * 2]);
		int plr1 = Integer.parseInt(tokens[4 + length * 2]);
		
		//set up players array
		if(plr0 == 0) { //sides were not flipped
			if(p0.side == 0) {
				players[0] = p0;
				players[1] = p1;
			}else {
				players[0] = p1;
				players[1] = p0;
			}
		}else { //sides were flipped
			if(p0.side == 1) {
				players[0] = p0;
				players[1] = p1;
			}else {
				players[0] = p1;
				players[1] = p0;
			}			
		}
		
		
		//set the scores of players
		players[0].score = Integer.parseInt(tokens[3 + length * 2]);
		players[1].score = Integer.parseInt(tokens[5 + length * 2]);
	}

	/*
	 * constructor, initiates kalah board and all other values associated with the board
	 */
	public Board(int numHouses, int numSeeds, Boolean random, int timeForMoves, Boolean AI) {
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

		if(random == true) {
			//System.out.println("Debug");
			int totalSeeds = seeds * houses;
			int sum = 0;
			int tempSeed;
			int range = 2;
			int[] randHouses = new int[houses];
			for(int i = 0; i < houses; i++) {
				randHouses[i] = 1;
			}
			Random generator = new Random();

			//Generate an array of random values along a normal distribution
			for(int i = 0; i < houses; i++) {
				if(seeds == 2) {
					range = 1;
				}
				double randDouble = generator.nextGaussian() * range + seeds -  1;
				tempSeed = (int) Math.round(randDouble);
				if(tempSeed < 0) {
					tempSeed *= -1;
				}
				sum += tempSeed;
				randHouses[i] += tempSeed;
				if(randHouses[i] > 10 ) {
					int tenDif = randHouses[i] - 10;
					randHouses[i] -= tenDif;
					sum -= tenDif;
				}
			}
			
			int dif = (totalSeeds - houses) - sum;
			

			//Debug: Display Difference

			//System.out.println("Diff: " + dif);
			

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

			//for(int i = 0; i < houses; i++) {
				//System.out.println(randHouses[i]);
			//}
			

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
	}

	/*
	 * StartGame is the main game loop where it players through the game logic
	 */
	public void StartGame(Player p0, Player p1, Server server) {
		players[0] = p0;
		players[1] = p1;

		if(!p0.consoleInput) {
			p0.clientHandler.out.println("1 " + server.board.toString());
		}
		if(!p1.consoleInput) {
			p1.clientHandler.out.println("1 " + server.board.toString());
		}
		
		//player initiation
		//players[0] = new Player(0, false);
		//players[1] = new Player(1, AI);

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
		
		//trigger clients to notify them
		players[0].getMove(0, 3);
		players[1].getMove(0, 3);
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

	public String getPlayerScores(int playerNum) {
		if(playerNum == players[0].getSide()) {
			return "" + players[0].getScore();
		}
		else if(playerNum == players[1].getSide()) {
			return "" + players[1].getScore();
		}
		else {
		System.out.print("Player " + players[0].getSide() + ": " + players[0].getScore() + "\n");
		System.out.print("Player " + players[1].getSide() + ": " + players[1].getScore() + "\n");
		return "";
		}
	}

	/*
	 * Move takes the row and index on mancala board, and
	 * tries to move that house. Return false if unsuccessful,
	 * true otherwise
	 */
	public boolean Move(int row, int index, boolean aiCheck) {
		//Setup temporary variables
		int[][] tempBoard = new int[2][houses];
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < houses; j ++) {
				tempBoard[i][j] = board[i][j];
				//System.out.print(tempBoard[i][j]);
			}
			//System.out.println();
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < houses; j++) {
				if(i == 0) {
					//System.out.print(tempBoard[1][houses - j]);
				}
				if(i == 1) {
					//System.out.print(tempBoard[0][j]);
				}
			}
			//System.out.println();
		}
		psuedoScore = 0;
		int numMoves = tempBoard[row][index];
		tempBoard[row][index] = 0;
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
					if(!aiCheck) {
						players[row].incrementScore();
					}
					else {
						psuedoScore += 1;
					}
					if(i == numMoves - 1) {
						//System.out.println("Bad 1");
						if(!aiCheck) {
							for(int k = 0; k < 2; k++) {
								for(int l = 0; l < houses; l ++) {
									board[k][l] = tempBoard[k][l];
								}
							}
							return true;
						}
						else {
							return true;
						}
					}
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 scores
				else if(row == 1 && rowTemp == row) {
					//Debug
					//System.out.println("Player 1 Scores");
					//score[1] += 1;
					//System.out.println("Row: " + row + " Player: " + players[row].getSide());
					if(!aiCheck) {
						players[row].incrementScore();
					}
					else {
						psuedoScore += 1;
					}
					if(i == numMoves - 1 ) {
						//System.out.println("Bad 2");
						if(!aiCheck) {
							for(int k = 0; k < 2; k++) {
								for(int l = 0; l < houses; l ++) {
									board[k][l] = tempBoard[k][l];
								}
							}
							return true;
						}
						else {
							return true;
						}
					}
					rowTemp = 0;
					indexTemp = -1;
				}
				//Player 1 moves from side 2 to side 1
				else if(rowTemp == 0) {
					tempBoard[1][0] += 1;
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 moves from side 1 to side 2
				else {
					tempBoard[0][0] += 1;
					rowTemp = 0;
					indexTemp = -1;
				}
			}
			else {
				tempBoard[rowTemp][indexTemp] += 1;
				//Check if the last piece is deposited in an empty spot on the player's side
				if(i == numMoves - 1 && tempBoard[rowTemp][indexTemp] == 1 && row == rowTemp) {
					if(rowTemp == 0) {
						int addToScore = tempBoard[1][5 - indexTemp] + tempBoard[rowTemp][indexTemp];
						tempBoard[1][5 - indexTemp] = 0;
						tempBoard[rowTemp][indexTemp] = 0;

						//score[0] += addToScore;
						if(!aiCheck) {
							players[row].updateScoreWithInt(addToScore);
						}
						else {
							psuedoScore += addToScore;
						}
					}
					if(rowTemp == 1) {
						int addToScore = tempBoard[0][5 - indexTemp] + tempBoard[rowTemp][indexTemp];
						tempBoard[0][5 - indexTemp] = 0;
						tempBoard[rowTemp][indexTemp] = 0;

						//score[1] += addToScore;
						if(!aiCheck) {
							players[row].updateScoreWithInt(addToScore);
						}
						else {
							psuedoScore += addToScore;
						}
					}
				}
			}
		}
		if(aiCheck) {
			psuedoScore += 0;
		}
		else {
			for(int k = 0; k < 2; k++) {
				for(int l = 0; l < houses; l ++) {
					board[k][l] = tempBoard[k][l];
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
			getPlayerScores(-1);


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
				int choice;
				if(players[1].isAi()) {
					choice = AImove(true, plr);
				}
				else {
				choice = players[playerturn].getMove(0, 2);
				while(!(choice == 1 || choice == 2)){
					System.out.println("Invalid Pie Rule Input:");
					choice = players[playerturn].getMove(0, 2);
				}
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
					getPlayerScores(-1);
				}
			} // End Pie Rule
			System.out.println("Player " + playerturn);

			if(players[plr].isAi()) {
				//System.out.println("Correct");
				move = AImove(false, plr);
			}
			else {
				move = players[plr].getMove(timeToMove, 1);
			}

			// Check for Out of Bounds
			while(move < 0 || move > houses) {
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
			
			 players[plr].clientHandler.out.println(
					"4 " +
					toString()
				);
	
		} while( Move(plr, move, false) );

		//System.out.println("Next Turn");
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
	
	
	public int AImove(Boolean pie, int plrNum) {
		if(pie) {
			if(players[0].getScore() > 1) {
				System.out.println("AI: 2");
				return 2;
			}
			else {
				System.out.println("AI: 1");
				return 1;
			}
		}
		else {
			players[plrNum].numTurnsHasTaken += 1;
			int[] moves = new int[houses];
			boolean extraTurn;
			for(int i = 0; i < houses; i++) {
				extraTurn = Move(plrNum, i, true);
				if(extraTurn) {
					moves[i] = 5 + psuedoScore;
				}
				else {
					moves[i] = psuedoScore;
				}
			}
			int maxIndex = 0;
			for(int i = 0; i < houses; i++) {
				if(moves[i] > moves[maxIndex]) {
					maxIndex = i;
				}
			}
			System.out.println("AI: " + maxIndex);
			return maxIndex;
		}
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

		
		return resp + players[0].getSide() + " " + players[0].getScore() + " " + 
			players[1].getSide() + " " + players[1].getScore();
	}

	/*
	 * Main, runs board
	 */

	public static void main(String[] args) {
		Scanner newObj = new Scanner(System.in);
		System.out.println("Enter # of houses, # of seeds, if random, and if AI");
		int houseIn = newObj.nextInt();
		int seedsIn = newObj.nextInt();
		Boolean randIn = newObj.nextBoolean();
		Boolean AIin = newObj.nextBoolean();
		Board board = new Board(houseIn, seedsIn, randIn, 0 ,  AIin);
		Player p0 = new Player(0, false);
		Player p1 = new Player(1, AIin);
		//board.StartGame(p0, p1);
	}

}
