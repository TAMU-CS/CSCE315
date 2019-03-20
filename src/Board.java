
public class Board {
	private int[][] board = new int[2][8]; // 2 rows, 6 houses + 2 mancala houses
	
	private Player[] players = new Player[2]; 
	
	public Board() {
		// init board
		
		players[0] = new Player();
		players[1] = new Player();
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
		System.out.print("User: " + players[0].getScore() + "\n");
		System.out.print("AI: " + players[1].getScore());
	}
}
