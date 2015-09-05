package ai.evaluator;

import com.google.common.collect.Iterables;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import common.pattern.Threat;

/**
 * Simple evaluator for threat based board class.
 */
public class SimpleThreatEvaluator implements Evaluator<Threat, Integer> {

  @Override
  public Integer eval(BoardClass<Threat> boardClass, StoneType nextToMove) {
    StoneType opponent = nextToMove.getOpponent();
    int a5 = Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.FIVE));
    if (a5 > 0) {
      return (nextToMove == StoneType.BLACK ? 4 : -4) * a5;
    }
    int b5 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.FIVE));
    if (b5 > 1) {
      return (opponent == StoneType.BLACK ? 4 : -4) * (b5 - 1);
    }
    int a4 = Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.STRAIT_FOUR));
    if (a4 > 0) {
      return (nextToMove == StoneType.BLACK ? 2 : -2) * (a4 - b5);
    }
    int b4 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.STRAIT_FOUR));
    if (b4 > 1) {
      return (opponent == StoneType.BLACK ? 2 : -2) * (b4 + b5 - 1);
    }
    int a3 = Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.FOUR))
        + Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.THREE));
    int b3 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.FOUR))
        + Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.THREE));
    return (nextToMove == StoneType.BLACK ? 1 : -1) * (a3 + 1 - b3 - b4 - b5);
  }
}
