package ai.minmax;

import com.google.common.collect.Sets;

import common.BitBoard;
import common.BoardClass;
import common.Constants;
import common.Patterns;
import common.PositionTransformer;
import common.StoneType;
import common.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.Position;

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

  private static <T> Collection<T> singleton(T e) {
    return Collections.singleton(e);
  }

  private Optional<Collection<Position>> immediateDefensiveMoves(
      BoardClass boardClass,
      StoneType stoneType) {
    return boardClass.filterMatchedPatterns(Patterns.getStraitFour(stoneType.getOpponent()))
        .map((p) -> singleton(p.getDefensiveMoves().get(0)))
        .findFirst();
  }

  private Optional<Collection<Position>> regularDefensiveMoves(
      BoardClass boardClass,
      StoneType stoneType) {
    return boardClass.filterMatchedPatterns(Patterns.getThree(stoneType.getOpponent()))
        .map((p) -> (Set<Position>) Sets.newHashSet(p.getDefensiveMoves()))
        .reduce((s1, s2) -> Sets.union(s1, s2))
        .map((s) -> (Collection<Position>) s);
  }

  private Optional<Collection<Position>> immediateOffensiveMoves(
      BoardClass boardClass,
      StoneType stoneType) {
    return boardClass.filterMatchedPatterns(Patterns.getStraitFour(stoneType))
        .map((p) -> singleton(p.getDefensiveMoves().get(0)))
        .findFirst();
  }

  private Optional<Collection<Position>> regularOffensiveMoves(
      BoardClass boardClass,
      StoneType stoneType) {
    return boardClass.filterMatchedPatterns(Patterns.getThree(stoneType))
        .map(p -> (Set<Position>) Sets.newHashSet(p.getDefensiveMoves()))
        .reduce((s1, s2) -> Sets.union(s1, s2))
        .map(p -> (Collection<Position>) p);
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
