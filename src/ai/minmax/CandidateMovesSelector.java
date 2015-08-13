package ai.minmax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.*;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;
import common.pattern.Pattern;

import model.Position;

import java.util.*;

/**
 * Class that selects candidate moves.
 */
public class CandidateMovesSelector {

  private final int randomSampleBranchCandidate;

  CandidateMovesSelector(int randomSampleBranchCandidate) {
    this.randomSampleBranchCandidate = randomSampleBranchCandidate;
  }

  public Collection<Position> getCandidateMoves(BoardClass boardClass, StoneType stoneType) {
    if (boardClass.isEmpty()) {
      return Collections.singleton(Position.create(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
    }
    return immediateOffensiveMoves(boardClass, stoneType)
        .orElseGet(() -> immediateDefensiveMoves(boardClass, stoneType)
            .orElseGet(() -> regularOffensiveMoves(boardClass, stoneType)
                .orElseGet(() -> regularDefensiveMoves(boardClass, stoneType)
                    .orElseGet(() -> getNeighboringMoves(boardClass)))));
  }

  private Optional<Collection<Position>> immediateDefensiveMoves(BoardClass boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType.getOpponent(),
        PatternType.STRAIT_FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    if (candidates.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(Collections.singleton(Collections.min(candidates)));
  }

  private Optional<Collection<Position>> regularDefensiveMoves(BoardClass boardClass, StoneType stoneType) {
    List<Pattern> threateningPattern = Lists.newArrayList(
        boardClass.getMatchingPatterns(stoneType.getOpponent(), PatternType.THREE));
    if (!threateningPattern.isEmpty()) {
      List<Position> candidates = new ArrayList<>();
      for (Pattern p : threateningPattern) {
        candidates.addAll(p.getDefensiveMoves());
      }
      return Optional.of(Collections.singleton(mostFrequentElement(candidates)));
    }
    return Optional.empty();
  }

  private Position mostFrequentElement(List<Position> candidates) {
    Collections.sort(candidates);
    int ct = 0, maxCt = 0;
    Position pre = null, result = null;
    for (Position cur : candidates) {
      if (cur.equals(pre)) {
        ct++;
      } else {
        if (ct > maxCt) {
          result = pre;
          maxCt = ct;
        }
        ct = 1;
      }
      pre = cur;
    }
    if (ct > maxCt) {
      result = pre;
    }
    return result;
  }

  private Optional<Collection<Position>> immediateOffensiveMoves(BoardClass boardClass, StoneType stoneType) {
    Set<Position> candidates = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType, PatternType.STRAIT_FOUR)) {
      candidates.addAll(p.getDefensiveMoves());
    }
    if (candidates.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(Collections.singleton(Collections.min(candidates)));
  }

  private Optional<Collection<Position>> regularOffensiveMoves(BoardClass boardClass, StoneType stoneType) {
    Set<Position> result = new HashSet<>();
    for (Pattern p : boardClass.getMatchingPatterns(stoneType, PatternType.THREE)) {
      result.addAll(p.getDefensiveMoves());
    }
    if (result.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(result);
  }

  private Collection<Position> getNeighboringMoves(BoardClass boardClass) {
    Set<Position> result = new HashSet<>();
    BitBoard board = boardClass.getBoard(PositionTransformer.IDENTITY);
    int[][] d = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
    for (int i = 0; i < Constants.BOARD_SIZE; i++) {
      for (int j = 0; j < Constants.BOARD_SIZE; j++) {
        if (board.get(i, j) != StoneType.NOTHING) {
          for (int k = 0; k < d.length; k++) {
            int ti = i + d[k][0], tj = j + d[k][1];
            if (Position.isValid(ti, tj) && board.get(ti, tj) == StoneType.NOTHING) {
              Position pos = Position.create(ti, tj);
              result.add(pos);
            }
          }
        }
      }
    }
    if (randomSampleBranchCandidate > 0) {
      List<Position> sample = new ArrayList<>(result);
      Collections.shuffle(sample);
      return sample.subList(0, Math.min(sample.size(), randomSampleBranchCandidate));
    }
    return result;
  }
}
