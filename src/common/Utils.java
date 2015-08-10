package common;

import model.ReadOnlyGameBoard;

/**
 * Utility methods.
 */
public class Utils {

  public static boolean playerWins(ReadOnlyGameBoard gameBoard, StoneType stoneType) {
    return BoardClass.fromGameBoard(gameBoard).matchesAny(Patterns.getGoalPatterns(stoneType));
  }

  public static boolean isValidPosition(int i, int j) {
    return i >= 0 && i < Constants.BOARD_SIZE && j >= 0 && j < Constants.BOARD_SIZE;
  }
}
