package ai.candidatemoveselector;

import common.Constants;
import common.PatternType;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;
import common.pattern.Pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import model.Position;

/**
 * Utility methods for candidate selector.
 */
public class CandidateMoveSelectorUtil {

  public static Collection<Position> allDefendFour(BoardClass<Pattern> boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType.getOpponent(),
        PatternType.FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> anyDefendFour(BoardClass<Pattern> boardClass, StoneType stoneType) {
    return allDefendFour(boardClass, stoneType).stream().min((a, b) -> a.compareTo(b));
  }

  public static Collection<Position> allDefendThree(BoardClass<Pattern> boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p :
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> bestDefendThree(BoardClass<Pattern> boardClass, StoneType stoneType) {
    List<Position> candidates = new ArrayList<>();
    for (Pattern p :
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return mostFrequentElement(candidates);
  }

  public static Collection<Position> allOffendFour(BoardClass<Pattern> boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType, PatternType.FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    return candidates;
  }

  public static Optional<Position> anyOffendFour(BoardClass<Pattern> boardClass, StoneType stoneType) {
    return allOffendFour(boardClass, stoneType).stream().min((a, b) -> a.compareTo(b));
  }

  public static Collection<Position> allOffendThree(BoardClass<Pattern> boardClass, StoneType stoneType) {
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

  public static Collection<Position> getNeighboringMoves(BoardClass boardClass) {
    Set<Position> result = new HashSet<>();
    BitBoard board = boardClass.getBoard(PositionTransformer.IDENTITY);
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board.get(i, j) != StoneType.NOTHING) {
          for (int k = 0; k < d.length; k++) {
            int ti = i + d[k][0], tj = j + d[k][1];
            if (Position.isValid(ti, tj) && board.get(ti, tj) == StoneType.NOTHING) {
              Position pos = Position.of(ti, tj);
              result.add(pos);
            }
          }
        }
      }
    }
    return result;
  }
}
