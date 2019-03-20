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
	public boolean Move() {
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
