package model;

import common.StoneType;

/**
 * History of the game played.
 */
public interface History {

  void recordMove(Position position, StoneType stoneType);

  HistoryEntry getLastMove();

  boolean hasMore();

  void clear();
}
