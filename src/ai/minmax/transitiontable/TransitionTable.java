package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;

import common.Transformable;
import common.boardclass.BoardClass;

/**
 * Table that keeps track of intermediate search result to avoid
 * duplicated search.
 */
public interface TransitionTable<T extends Transformable<T>> {

  T get(BoardClass boardClass);

  void put(BoardClass boardClass, T node);

  interface Factory<T extends Transformable<T>> {
    TransitionTable<T> create();
  }
}
