package ai.proofnumber;

import ai.evaluator.Evaluator;
import ai.threatbasedsearch.ThreatBasedSearch;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import common.pattern.Threat;

/**
 * Created by biran on 9/3/2015.
 */
public class TBSEvaluator implements Evaluator<Threat, Result> {

  private final ThreatBasedSearch tbs;

  public TBSEvaluator(ThreatBasedSearch tbs) {
    this.tbs = tbs;
  }

  @Override
  public Result eval(BoardClass<Threat> boardClass, StoneType nextToMove) {
    if (boardClass.matchesAny(StoneType.BLACK, PatternType.GOAL)) {
      return Result.TRUE;
    }
    if (boardClass.matchesAny(StoneType.WHITE, PatternType.GOAL) || boardClass.isFull()) {
      return Result.FALSE;
    }
    if (tbs.winningMove(boardClass, nextToMove) != null) {
      return nextToMove == StoneType.BLACK ? Result.TRUE : Result.FALSE;
    }
    if (boardClass.getStoneCount() > 10 && nextToMove == StoneType.WHITE
        && tbs.winningMove(boardClass, StoneType.BLACK) == null) {
      // bad game for black
      return Result.FALSE;
    }
    return Result.UNKNOWN;
  }
}
