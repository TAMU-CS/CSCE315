
public class Board {
	private int[][] board = new int[2][8]; // 2 rows, 6 houses + 2 mancala house per player
	
	private Player[] players = new Player[2]; 
	
	public Board() {
		for(int i = 0; i < 2; i++) {
			for(int j = 1; j < 7; j++) {
				board[i][j] = 4; // each house starts with 4 seeds
			}
		}
	}
	
	public void printBoard() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void getPlayerScores() {
		System.out.print("Player 1 Score: " + players[0].getScore());
		System.out.print("Player 2 Score: " + players[1].getScore());
	}
}
