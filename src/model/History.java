package model;

import common.Square;

/**
 * History of the game played.
 */
public interface History {

  void recordMove(Position position, Square stoneType);

  HistoryEntry getLastMove();

  boolean hasMore();

  void clear();
}
