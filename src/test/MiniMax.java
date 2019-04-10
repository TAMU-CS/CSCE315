package test;
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

/*public int miniMax(int depth, boolean isMaximizingPlayer, int playerturn, int[][] boardState) {
  // Get possible next moves
  int[][] nextMoves = GetMoves(playerturn);

  int bestScore = (isMaximizingPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
  int currentScore;
  int bestRow = -1;
  int bestCol = -1;

  if(nextMoves.isEmpty() || depth == 0) {
    System.out.println("Game over");
  } else {
    for(int[] move: nextMoves) {

      // figure out best move for this player
      boardState[[move[0]][move[1]]] = playerturn;

      if(isMaximizingPlayer) { // AI is the maximizing player
        currentScore = miniMax(depth - 1, true, 1);
        if(currentScore > bestScore) {
          bestScore = currentScore;
          bestRow = move[0];
          bestCol = move[1];
        } else { // Human player
          currentScore = miniMax(depth - 1, false, 0);
          if(currentScore < bestScore) {
            bestRow = move[0];
            bestCol = move[1];
          }
        }
        // Undo move
        boardState[[move[0]][move[1]]] = -1;
      }
    }
  }
  return new int[] {bestScore, bestRow, bestCol};
}*/
