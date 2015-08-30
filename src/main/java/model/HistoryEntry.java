package model;

import com.google.auto.value.AutoValue;
import common.StoneType;

/**
 * Entry for a move stored in game history.
 */
@AutoValue
public abstract class HistoryEntry {

  public static HistoryEntry create(GameBoard gameBoard, Position lastMove, StoneType stoneType) {
    return new AutoValue_HistoryEntry(gameBoard, lastMove, stoneType);
  }

  public abstract GameBoard getGameBoard();

  public abstract Position getLastMove();

  public abstract StoneType getStoneType();
}
