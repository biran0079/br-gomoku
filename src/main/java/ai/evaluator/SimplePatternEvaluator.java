package ai.evaluator;

import com.google.common.collect.Iterables;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import common.pattern.PatternType;

/**
 * Simple implementation of evaluator.
 */
public class SimplePatternEvaluator implements Evaluator<Pattern> {

  @Override
  public int eval(BoardClass<Pattern> boardClass, StoneType nextToMove) {
    if (boardClass.matchesAny(nextToMove, PatternType.FOUR)
        || boardClass.matchesAny(nextToMove, PatternType.THREE)) {
      return nextToMove == StoneType.BLACK ? 10 : -10;
    }
    StoneType opponent = nextToMove.getOpponent();
    int ws4 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.FOUR));
    int w3 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.THREE));
    return nextToMove == StoneType.BLACK ? - ws4 - w3 : ws4 + w3;
  }
}

