package ai.minmax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.*;
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
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getStraitFour(stoneType.getOpponent()))) {
      return Optional.of(Collections.singleton(p.getDefensiveMoves().get(0)));
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> regularDefensiveMoves(BoardClass boardClass, StoneType stoneType) {
    List<Pattern> threateningPattern = Lists.newArrayList(
        boardClass.filterMatchedPatterns(Patterns.getThree(stoneType.getOpponent())));
    if (!threateningPattern.isEmpty()) {
      Set<Position> union = null;
      for (Pattern p : threateningPattern) {
        if (union == null) {
          union = Sets.newHashSet(p.getDefensiveMoves());
        } else {
          union = Sets.union(union, Sets.newHashSet(p.getDefensiveMoves()));
        }
      }
      return Optional.of(union.isEmpty()
          ? Collections.singleton(threateningPattern.get(0).getDefensiveMoves().get(0))
          : union);
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> immediateOffensiveMoves(BoardClass boardClass, StoneType stoneType) {
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getStraitFour(stoneType))) {
      return Optional.of(Collections.singleton(p.getDefensiveMoves().get(0)));
    }
    return Optional.empty();
  }

  private Optional<Collection<Position>> regularOffensiveMoves(BoardClass boardClass, StoneType stoneType) {
    Set<Position> result = new HashSet<>();
    for (Pattern p : boardClass.filterMatchedPatterns(Patterns.getThree(stoneType))) {
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
            if (Utils.isValidPosition(ti, tj) && board.get(ti, tj) == StoneType.NOTHING) {
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
