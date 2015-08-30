package ai.minmax.transitiontable;

import common.Transformable;
import common.boardclass.BoardClass;

import static common.PositionTransformer.IDENTITY;

/**
 * Be smart about transition to safe space.
 */
public class SmartCompleteTransitionTable<T extends Transformable<T>> extends CompleteTransitionTable<T> {

  @Override
  public void put(BoardClass<?> boardClass, T node) {
    if (boardClass.getStoneCount() < 6) {
      super.put(boardClass, node);
    } else {
      cache.put(boardClass.getBoard(IDENTITY), node);
    }
  }
}
