package model;

/**
 * Game board utility methods.
 */
public class GameBoards {

  public static GameBoard createGameBoard() {
    return new GameBoardImpl();
  }
}
