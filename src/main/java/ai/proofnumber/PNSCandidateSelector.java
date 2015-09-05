package ai.proofnumber;

import ai.candidatemoveselector.CandidateMovesSelector;
import ai.evaluator.Evaluator;
import ai.evaluator.SimpleThreatEvaluator;
import ai.threatbasedsearch.ThreatBasedSearch;
import autovalue.shaded.com.google.common.common.collect.Lists;
import autovalue.shaded.com.google.common.common.collect.Sets;
import com.google.auto.value.AutoValue;
import common.StoneType;
import common.boardclass.BoardClass;

import static ai.candidatemoveselector.CandidateMoveSelectorUtil.*;
import static common.boardclass.threatbased.ThreatUtil.getThreatsSet;
import static common.boardclass.threatbased.ThreatUtil.restrictedThreats;

import common.boardclass.threatbased.ThreatUtil;
import common.pattern.Threat;
import model.Position;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by biran on 9/3/2015.
 */
public class PNSCandidateSelector implements CandidateMovesSelector<Threat> {

  private final ThreatBasedSearch tbs;

  public PNSCandidateSelector(ThreatBasedSearch tbs) {
    this.tbs = tbs;
  }

  @Override
  public Collection<Position> getCandidateMoves(BoardClass<Threat> boardClass, StoneType nextToMove) {
    if (boardClass.isEmpty()) {
      return centerIfEmptyBoard(boardClass, nextToMove);
    }
    if (nextToMove == StoneType.BLACK) {
      Collection<Position> result = getNeighboringMoves(boardClass).stream()
          .filter(p -> tbs.winningMove(boardClass.withPositionSet(p, StoneType.BLACK), StoneType.BLACK) != null)
          .collect(Collectors.toList());
      if (!result.isEmpty()) {
        return result;
      }
      result = getNeighboringMoves(boardClass).stream()
          .map(p -> Move.create(p, evaluate(boardClass, p, nextToMove)))
          .sorted()
          .limit(10)
          .map(Move::getPosition)
          .collect(Collectors.toList());
      return result;
    } else {
      Collection<Position> res = tbs.findImplicitThreats(boardClass, StoneType.BLACK);
      if (res.isEmpty()) {
        res = getAllEmpty(boardClass);
      }
      return res;
    }
  }

  private int evaluate(BoardClass<Threat> boardClass, Position p, StoneType stoneType) {
    int score = 0;
    for (Threat threat : ThreatUtil.restrictedThreats(
        boardClass.withPositionSet(p, stoneType)
            .filterMatching(boardClass.getCorpus().get(p, stoneType)), 2)) {
      switch (threat.getPatternType()) {
        case STRAIT_FOUR:
          score += 4;
          break;
        case THREE:
          score += 2;
          break;
      }
    }
    return score;
  }

  @AutoValue
  abstract static class Move implements Comparable<Move> {

    static Move create(Position p, int score) {
      return new AutoValue_PNSCandidateSelector_Move(p, score);
    }

    abstract Position getPosition();
    abstract int getScore();

    @Override
    public int compareTo(Move move) {
      return move.getScore() - getScore();
    }
  }
}
