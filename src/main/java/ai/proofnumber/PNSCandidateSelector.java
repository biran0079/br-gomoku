package ai.proofnumber;

import ai.candidatemoveselector.CandidateMovesSelector;
import ai.candidatemoveselector.CandidateMovesSelectorBuilder;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.threatbasedsearch.ThreatBasedSearch;
import com.google.auto.value.AutoValue;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.threatbased.ThreatUtil;
import common.pattern.Threat;
import model.Position;

import java.util.Collection;
import java.util.stream.Collectors;

import static ai.candidatemoveselector.CandidateMoveSelectorUtil.getAllEmpty;
import static ai.candidatemoveselector.CandidateMoveSelectorUtil.getNeighboringMoves;

/**
 * Candidate move selector for PNS.
 */
public class PNSCandidateSelector implements CandidateMovesSelectorBuilder.MoveSelector<Threat> {

  private final ThreatBasedSearch tbs;

  public PNSCandidateSelector(ThreatBasedSearch tbs) {
    this.tbs = tbs;
  }

  @Override
  public Collection<Position> select(BoardClass<Threat> boardClass, StoneType nextToMove) {
    Collection<Position> result;
    if (nextToMove == StoneType.BLACK) {
      result = getNeighboringMoves(boardClass).stream()
          .filter(p -> tbs.winningMove(boardClass.withPositionSet(p, StoneType.BLACK), StoneType.BLACK) != null)
          .collect(Collectors.toList());
      if (result.isEmpty()) {
        result = getNeighboringMoves(boardClass).stream()
            .map(p -> Move.create(p, evaluate(boardClass, p, nextToMove)))
            .sorted()
            .limit(10)
            .map(Move::getPosition)
            .collect(Collectors.toList());
      }
    } else {
      result = tbs.findImplicitThreats(boardClass, StoneType.BLACK);
      if (result.isEmpty()) {
        result = getAllEmpty(boardClass);
      }
    }
    return result;
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
