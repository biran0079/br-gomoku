package ai.minmax.transitiontable;

import com.google.common.collect.Maps;
import common.PositionTransformer;
import common.Transformable;
import common.boardclass.AbstractBoardClass;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;

import java.util.Map;

import static common.PositionTransformer.*;

/**
 * Non-thread-safe transition table.
 */
public class CompleteTransitionTable<T extends Transformable<T>> extends AbstractTransitionTable<T> {

  static final PositionTransformer[] IDENTICAL_TRANSFORMERS =
      new PositionTransformer[] {
          IDENTITY,
          IDENTITY_M,
          CLOCK_90,
          CLOCK_90_M,
          CLOCK_180,
          CLOCK_180_M,
          CLOCK_270,
          CLOCK_270_M,
      };

  public CompleteTransitionTable() {
    super(IDENTICAL_TRANSFORMERS);
  }
}
