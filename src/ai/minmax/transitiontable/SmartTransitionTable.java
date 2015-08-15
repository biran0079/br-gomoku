package ai.minmax.transitiontable;

import ai.minmax.MinMaxNode;

import common.Transformable;
import common.boardclass.BoardClass;

import static common.PositionTransformer.IDENTITY;

import javax.xml.crypto.dsig.Transform;

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
