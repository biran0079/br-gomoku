package ai.minmax;

import com.google.common.collect.Iterables;
import common.boardclass.BoardClass;
import common.PatternType;
import common.StoneType;

/**
 * Default implementation of evaluator.
 */
public class DefaultEvaluator implements Evaluator {

  @Override
  public int eval(BoardClass boardClass, boolean blackMoveNext) {
    if (blackMoveNext) {
      if (boardClass.matchesAny(StoneType.BLACK, PatternType.STRAIT_FOUR)
          || boardClass.matchesAny(StoneType.BLACK, PatternType.THREE)) {
        return 10;
      } else {
        int ws4 = Iterables.size(boardClass.getMatchingPatterns(StoneType.WHITE, PatternType.STRAIT_FOUR));
        int w3 = Iterables.size(boardClass.getMatchingPatterns(StoneType.WHITE, PatternType.THREE));
        return -(ws4 + w3);
      }
    } else {
      if (boardClass.matchesAny(StoneType.WHITE, PatternType.STRAIT_FOUR)
          || boardClass.matchesAny(StoneType.WHITE, PatternType.THREE)) {
        return -10;
      } else {
        int bs4 = Iterables.size(boardClass.getMatchingPatterns(StoneType.BLACK, PatternType.STRAIT_FOUR));
        int b3 = Iterables.size(boardClass.getMatchingPatterns(StoneType.BLACK, PatternType.THREE));
        return bs4 + b3;
      }
    }
  }
}
