package model;

import common.StoneType;

/**
 * History of the game played.
 */
public interface History {

  void recordMove(GameBoard preState, Position position, StoneType stoneType);

  HistoryEntry popLastEntry();

  int size();

  void clear();
}
