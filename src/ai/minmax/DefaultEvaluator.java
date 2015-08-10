package ai.minmax;

import com.google.common.collect.Iterables;
import common.BoardClass;
import common.Patterns;

/**
 * Default implementation of evaluator.
 */
public class DefaultEvaluator implements Evaluator {

  @Override
  public int eval(BoardClass boardClass, boolean blackMoveNext) {
    if (blackMoveNext) {
      if (boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR)
          || boardClass.matchesAny(Patterns.BLACK_THREE)) {
        return 10;
      } else {
        int ws4 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.WHITE_STRAIT_FOUR));
        int w3 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.WHITE_THREE));
        return -(ws4 + w3);
      }
    } else {
      if (boardClass.matchesAny(Patterns.WHITE_STRAIT_FOUR)
          || boardClass.matchesAny(Patterns.WHITE_THREE)) {
        return -10;
      } else {
        int bs4 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.BLACK_STRAIT_FOUR));
        int b3 = Iterables.size(boardClass.filterMatchedPatterns(Patterns.BLACK_THREE));
        return bs4 + b3;
      }
    }
  }
}
