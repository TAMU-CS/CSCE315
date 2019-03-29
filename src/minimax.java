import java.util.*;

int MAX = 1000;
int MIN = -1000;

public int miniMax(int depth, boolean isMaximizingPlayer, int playerturn) {
  // Get possible next moves
  int[][] nextMoves = GetMoves(playerturn);

  int bestScore = (isMaximizingPlayer == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
  int currentScore;
  int bestRow = -1;
  int bestCol = -1;

  if(nextMoves.isEmpty() || depth == 0) {
    System.out.println("Game over");
  } else {
    for(int[] move: nextMoves) {
      
    }
  }
}
