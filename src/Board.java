
public class Board {
	private int[][] board = new int[2][8]; // 2 rows, 6 houses + 2 mancala house per player
	
	public void printBoard() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}
