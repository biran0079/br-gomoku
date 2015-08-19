package ai.minmax;

import com.google.common.collect.Iterables;

import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import common.pattern.PatternType;

/**
 * Better evaluator than default evaluator.
 */
public class EnhancedEvaluator implements Evaluator<Pattern> {

  @Override
  public int eval(BoardClass<Pattern> boardClass, StoneType nextToMove) {
    StoneType opponent = nextToMove.getOpponent();
    int a4 = Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.FOUR));
    if (a4 > 0) {
      return (nextToMove == StoneType.BLACK ? 4 : -4) * a4;
    }
    int b4 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.FOUR));
    if (b4 > 1) {
      return (opponent == StoneType.BLACK ? 4 : -4) * b4;
    }
    int a3 = Iterables.size(boardClass.getMatchingPatterns(nextToMove, PatternType.THREE));
    if (a3 > 0) {
      return (nextToMove == StoneType.BLACK ? 2 : -2) * (a3 - b4);
    }
    int b3 = Iterables.size(boardClass.getMatchingPatterns(opponent, PatternType.THREE));
    if (b3 > 1) {
      return (opponent == StoneType.BLACK ? 2 : -2) * (b3 + b4);
    }
    return (opponent == StoneType.BLACK ? 1 : -1) * (b3 + b4);
  }
}
