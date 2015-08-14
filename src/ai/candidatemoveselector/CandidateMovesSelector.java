package ai.candidatemoveselector;

import static java.util.Collections.singleton;

import common.Constants;
import common.PositionTransformer;
import common.StoneType;
import common.boardclass.BitBoard;
import common.boardclass.BoardClass;

import model.Position;

import java.util.*;

/**
 * Class that selects candidate moves.
 */
public class CandidateMovesSelector {

  private final int randomSampleBranchCandidate;

  public CandidateMovesSelector(int randomSampleBranchCandidate) {
    this.randomSampleBranchCandidate = randomSampleBranchCandidate;
  }

  public Collection<Position> getCandidateMoves(BoardClass boardClass, StoneType stoneType) {
    if (boardClass.isEmpty()) {
      return singleton(Position.create(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
    }
    Optional<Position> candidate = CandidateMoveSelectorUtil.anyOffendFour(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    candidate = CandidateMoveSelectorUtil.anyDefendFour(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    Collection<Position> candidateList = CandidateMoveSelectorUtil.allOffendThree(
        boardClass, stoneType);
    if (!candidateList.isEmpty()) {
      return candidateList;
    }
    candidate = CandidateMoveSelectorUtil.bestDefendThree(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    return getNeighboringMoves(boardClass);
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
