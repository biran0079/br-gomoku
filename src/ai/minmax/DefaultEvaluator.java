package ai.minmax;

import com.google.common.collect.Iterables;

import common.PatternType;
import common.StoneType;
import common.boardclass.BoardClass;

/**
 * Default implementation of evaluator.
 */
public class DefaultEvaluator implements Evaluator {

  @Override
  public int eval(BoardClass boardClass, StoneType nextToMove) {
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

