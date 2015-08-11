package ai.minmax;

import common.BoardClass;

/**
 * Transition table that does nothing.
 */
public class NoopTransitionTable implements TransitionTable {

  @Override
  public MinMaxNode get(BoardClass boardClass) {
    return null;
  }

  @Override
  public void put(BoardClass boardClass, MinMaxNode node) {

  }
}
