package ai.minmax;

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
        int ws4 = (int) boardClass.filterMatchedPatterns(Patterns.WHITE_STRAIT_FOUR).count();
        int w3 = (int) boardClass.filterMatchedPatterns(Patterns.WHITE_THREE).count();
        return -(ws4 + w3);
      }
    } else {
      if (boardClass.matchesAny(Patterns.WHITE_STRAIT_FOUR)
          || boardClass.matchesAny(Patterns.WHITE_THREE)) {
        return -10;
      } else {
        int bs4 = (int) boardClass.filterMatchedPatterns(Patterns.BLACK_STRAIT_FOUR).count();
        int b3 = (int) boardClass.filterMatchedPatterns(Patterns.BLACK_THREE).count();
        return bs4 + b3;
      }
    }
  }
}
