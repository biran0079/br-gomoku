package model;

import common.StoneType;

/**
 * Entry for a move stored in game history.
 */
public class HistoryEntry {

  private final Position lastMove;
  private final GameBoard gameBoard;
  private final StoneType stoneType;

  HistoryEntry(GameBoard gameBoard, Position lastMove, StoneType stoneType) {
    this.gameBoard = gameBoard;
    this.lastMove = lastMove;
    this.stoneType = stoneType;
  }

  public Position getLastMove() {
    return lastMove;
  }

  public StoneType getStoneType() {
    return stoneType;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }

}
