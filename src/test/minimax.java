package test;
import java.util.*;

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
