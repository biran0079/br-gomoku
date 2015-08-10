package model;

import common.StoneType;

/**
 * Entry for a move stored in game history.
 */
public class HistoryEntry {

  private final Position position;
  private final StoneType stoneType;

  HistoryEntry(Position position, StoneType stoneType) {
    this.position = position;
    this.stoneType = stoneType;
  }

  public Position getPosition() {
    return position;
  }

  public StoneType getStoneType() {
    return stoneType;
  }
}
