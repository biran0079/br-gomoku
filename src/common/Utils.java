package common;

import model.GameBoard;

/**
 * Utility methods.
 */
public class Utils {

  public static boolean playerWins(GameBoard gameBoard, Square curPiece) {
    return playerWins(gameBoard.toArray(), curPiece);
  }

  public static boolean playerWins(Square[][] chessBoard, Square curPiece) {
    int r = Constants.ROW_NUM, c = Constants.COL_NUM;
    int[][] d = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };
    for (int i = 0; i < r; i++)
      for (int j = 0; j < c; j++) {
        for (int k = 0; k < 4; k++) {
          if (isValidPosition(i + 4 * d[k][0], j + 4 * d[k][1])) {
            int l;
            for (l = 0; l < 5; l++) {
              if (chessBoard[i + l * d[k][0]][j + l * d[k][1]] != curPiece) {
                break;
              }
            }
            if (l == 5)
              return true;
          }
        }
      }
    return false;
  }

  public static boolean isValidPosition(int i, int j) {
    return i >= 0 && i < Constants.ROW_NUM && j >= 0 && j < Constants.COL_NUM;
  }

  public static void printBoard(Square[][] board) {
    int R = Constants.ROW_NUM, C = Constants.COL_NUM;
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        if (board[i][j] == Square.NOTHING)
          System.out.print("__");
        else if (board[i][j] == Square.BLACK_PIECE)
          System.out.print("*_");
        else
          System.out.print("o_");
      }
      System.out.println();
    }
    System.out.println();
  }
}
