package ai.minmax.transitiontable;

import static common.PositionTransformer.IDENTITY;

import common.Transformable;
import common.boardclass.BoardClass;

/**
 * Be smart about transition to safe space.
 */
public class SmartTransitionTable<T extends Transformable<T>> extends TransitionTableImpl<T> {

  @Override
  public void put(BoardClass boardClass, T node) {
    if (boardClass.getStoneCount() < 6) {
      super.put(boardClass, node);
    } else {
      cache.put(boardClass.getBoard(IDENTITY), node);
    }
  }
}
