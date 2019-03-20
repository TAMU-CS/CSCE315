
public class Board {
	
	//board data structure, 2x6 board to represent houses for seeds
	private int[][] board = new int[2][6];
	
	//score datastructure for keep track of scores
	private int[] score = new int[2];
	private int playerturn;
	
	//player objects, correspond with their score index
	private Player[] players = new Player[2];
	
	/* 
	 * Default constructor, initiates an empty kalah board with players
	 */
	public Board() {
		// init board
		
		players[0] = new Player();
		players[1] = new Player();
	}
	
	/*
	 * Print board, prints out the current kalah board state
	 */
	public void printBoard() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void getPlayerScores() {
		System.out.print("User: " + players[0].getScore() + "\n");
		System.out.print("AI: " + players[1].getScore());
	}
	
	/*
	 * AddScore takes player index, and increments their score by 1
	 */
	public void AddScore(int index) {
		
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
					score[0] += 1;
					if(i == numMoves - 1) {
						return true;
					}
					rowTemp = 1;
					indexTemp = -1;
				}
				//Player 2 scores
				else if(playerturn == 1 && rowTemp == 1) {
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
			}
		}
		return false;
	}
	
	/*
	 * GetMoves returns an array of possible moves on the mancala board
	 * 0 means not possible move, 1 means possible move
	 */
	public int[][] GetMoves() {
		return new int[2][6];
	}
	
	
}
