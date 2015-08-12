package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;
import common.BoardClass;

import static common.PositionTransformer.IDENTITY;

/**
 * Be smart about transition to safe space.
 */
public class SmartTransitionTable extends TransitionTableImpl {

  @Override
  public void put(BoardClass boardClass, MinMaxNode node) {
    if (boardClass.getStoneCount() < 6) {
      super.put(boardClass, node);
    } else {
      cache.put(boardClass.getBoard(IDENTITY), node);
    }
  }
}
