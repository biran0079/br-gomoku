package ai.candidatemoveselector;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import common.PatternType;
import common.StoneType;
import common.boardclass.BoardClass;
import common.pattern.Pattern;
import model.Position;

import java.util.*;

/**
 * Utility methods for candidate selector.
 */
public class CandidateMoveSelectorUtil {

  public static Collection<Position> allDefendFour(BoardClass boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType.getOpponent(),
        PatternType.FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> anyDefendFour(BoardClass boardClass, StoneType stoneType) {
    return allDefendFour(boardClass, stoneType).stream().min((a, b) -> a.compareTo(b));
  }

  public static Collection<Position> allDefendThree(BoardClass boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p :
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> bestDefendThree(BoardClass boardClass, StoneType stoneType) {
    List<Position> candidates = new ArrayList<>();
    for (Pattern p :
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return mostFrequentElement(candidates);
  }

  public static Collection<Position> allOffendFour(BoardClass boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType, PatternType.FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> anyOffendFour(BoardClass boardClass, StoneType stoneType) {
    return allOffendFour(boardClass, stoneType).stream().min((a, b) -> a.compareTo(b));
  }

  public static Collection<Position> allOffendThree(BoardClass boardClass, StoneType stoneType) {
    Set<Position> result = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType, PatternType.THREE)) {
      result.addAll(p.getDefensiveMoves());
    }
    return result;
  }

  private static Optional<Position> mostFrequentElement(List<Position> candidates) {
    if (candidates.isEmpty()) {
      return Optional.empty();
    }
    int maxCt = 0;
    Position result = null;
    Map<Position, Integer> counter = new HashMap<>();
    for (Position position : candidates) {
      int newCount;
      if (!counter.containsKey(position)) {
        newCount = 1;
      } else {
        newCount = counter.get(position) + 1;
      }
      if (newCount > maxCt) {
        maxCt = newCount;
        result = position;
      } else if (newCount == maxCt && position.compareTo(result) < 0) {
        result = position;
      }
      counter.put(position, newCount);
    }
    return Optional.of(result);
  }
}
