package model;

import common.Square;
import javafx.geometry.Pos;

/**
 * Entry for a move stored in game history.
 */
public class HistoryEntry {

  private final Position position;
  private final Square stoneType;

  HistoryEntry(Position position, Square stoneType) {
    this.position = position;
    this.stoneType = stoneType;
  }

  public Position getPosition() {
    return position;
  }

  public Square getStoneType() {
    return stoneType;
  }
}
