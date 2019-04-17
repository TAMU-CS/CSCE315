
import java.util.*;

public class MiniMax{
	
	//minimax function
	public static int minimax(String boardState, int depth, int maximizingPlayer) {
		
		//check if terminated
		Board board = new Board(boardState.split(" "));
		if(depth == 0 || board.endgame()) {
			board.getOutcome();
			return board.getScore(maximizingPlayer) - board.getScore(maximizingPlayer == 1 ? 0 : 1);
		}
		
		//maximizing player
		if(board.curMove == maximizingPlayer) {
			int value = Integer.MIN_VALUE;
			int[] moves = board.getMoves();
			
			for(int i = 0; i < moves.length; i++) {
				if(moves[i] == 1) { //possible move
					board = new Board(boardState.split(" "));
					board.nextTurn(i);
					value = Math.max(value, minimax(board.toString(), depth - 1, maximizingPlayer));
				}
			}
			return value;
			
		}else { //minimizing player
			int value = Integer.MAX_VALUE;
			int[] moves = board.getMoves();
			
			for(int i = 0; i < moves.length; i++) {
				if(moves[i] == 1) { //possible move
					board = new Board(boardState.split(" "));
					board.nextTurn(i);
					value = Math.min(value, minimax(board.toString(), depth - 1, maximizingPlayer));
				}
			}
			return value;			
		}
	}

	//get best move function
	public static int getMove(String boardState, int depth, int maximizingPlayer) {
		Board board = new Board(boardState.split(" "));
		
		//maximizing player
		int value = Integer.MIN_VALUE;
		int[] moves = board.getMoves();
		int bestMove = 0;
		
		for(int i = 0; i < moves.length; i++) {
			if(moves[i] == 1) { //possible move
				board = new Board(boardState.split(" "));
				board.nextTurn(i);
				int search =  minimax(board.toString(), depth - 1, maximizingPlayer);
				if(value < search) {
					value = search;
					bestMove = i;
				}
			}
		}
		return bestMove;
	}
}
