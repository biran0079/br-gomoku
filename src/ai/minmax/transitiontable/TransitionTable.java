package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;
import common.BoardClass;

/**
 * Table that keeps track of intermediate search result to avoid
 * duplicated search.
 */
public interface TransitionTable {

  MinMaxNode get(BoardClass boardClass);

  void put(BoardClass boardClass, MinMaxNode node);

  interface Factory {
    TransitionTable create();
  }
}
