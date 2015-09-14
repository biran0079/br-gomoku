package ai.candidatemoveselector;

import com.google.common.collect.Iterables;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.PatternType;
import common.pattern.Threat;
import model.Position;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Candidate selector for threat based BoardClass.
 */
public class ThreatCandidateMoveSelectorUtil {

  public static Collection<Position> anyOffendFiveThreat(
      BoardClass<Threat> boardClass, StoneType stoneType) {
    for (Threat p : boardClass.getMatchingPatterns(stoneType,
        PatternType.FIVE)) {
      return singleton(p.getOffensiveMove());
    }
    return emptyList();
  }

  public static Collection<Position> anyDefendFiveThreat(
      BoardClass<Threat> boardClass, StoneType stoneType) {
    for (Threat p : boardClass.getMatchingPatterns(stoneType.getOpponent(),
        PatternType.FIVE)) {
      return singleton(p.getOffensiveMove());
    }
    return emptyList();
  }

  public static Collection<Position> anyOffendStraitFour(
      BoardClass<Threat> boardClass, StoneType stoneType) {
    for (Threat p : boardClass.getMatchingPatterns(stoneType,
        PatternType.STRAIT_FOUR)) {
      return singleton(p.getOffensiveMove());
    }
    return emptyList();
  }

  public static Collection<Position> allDefendStraitFour(
      BoardClass<Threat> boardClass, StoneType stoneType) {
    Set<Position> moves = new HashSet<>();
    for (Threat p : boardClass.getMatchingPatterns(stoneType.getOpponent(),
        PatternType.STRAIT_FOUR)) {
      moves.add(p.getOffensiveMove());
      moves.addAll(p.getDefensiveMoves());
    }
    return moves;
  }

  public static Collection<Position> allOffendAndDefendFourAndThree(
      BoardClass<Threat> boardClass, StoneType stoneType) {
    Set<Position> moves = new HashSet<>();
    for (Threat p : Iterables.concat(
        boardClass.getMatchingPatterns(stoneType, PatternType.FOUR),
        boardClass.getMatchingPatterns(stoneType, PatternType.THREE),
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.FOUR),
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE))) {
      moves.add(p.getOffensiveMove());
    }
    return moves;
  }
}
