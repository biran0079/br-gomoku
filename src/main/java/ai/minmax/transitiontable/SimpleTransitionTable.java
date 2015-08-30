package ai.minmax.transitiontable;

import common.PositionTransformer;
import common.Transformable;

/**
 * Transition table that only stores identity board.
 */
public class SimpleTransitionTable<T extends Transformable<T>> extends AbstractTransitionTable<T> {

  public SimpleTransitionTable() {
    super(new PositionTransformer[] {PositionTransformer.IDENTITY});
  }
}
