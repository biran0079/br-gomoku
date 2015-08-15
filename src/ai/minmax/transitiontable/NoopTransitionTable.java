package ai.minmax.transitiontable;

import common.Transformable;
import common.boardclass.BoardClass;

/**
 * Transition table that does nothing.
 */
public class NoopTransitionTable<T extends Transformable<T>> implements TransitionTable<T> {

  @Override
  public T get(BoardClass boardClass) {
    return null;
  }

  @Override
  public void put(BoardClass boardClass, T node) {

  }
}
