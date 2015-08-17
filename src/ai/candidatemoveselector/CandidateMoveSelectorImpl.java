package ai.candidatemoveselector;

import common.Constants;
import common.StoneType;
import common.boardclass.BoardClass;
import model.Position;

import java.util.*;

import static java.util.Collections.singleton;

public class CandidateMoveSelectorImpl implements CandidateMovesSelector {

  private final int randomSampleBranchCandidate;

  public CandidateMoveSelectorImpl(int randomSampleBranchCandidate) {
    this.randomSampleBranchCandidate = randomSampleBranchCandidate;
  }

  @Override
  public Collection<Position> getCandidateMoves(BoardClass boardClass, StoneType stoneType) {
    if (boardClass.isEmpty()) {
      return singleton(Position.of(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
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
    candidateList = CandidateMoveSelectorUtil.defendThree(boardClass, stoneType);
    if (!candidateList.isEmpty()) {
      return candidateList;
    }
    return randomSample(CandidateMoveSelectorUtil.getNeighboringMoves(boardClass), randomSampleBranchCandidate);
  }

  public static <T> Collection<T> randomSample(Collection<T> c, int n) {
    if (n > c.size()) {
      return c;
    }
    List<T> sample = new ArrayList<>(c);
    Collections.shuffle(sample);
    return sample.subList(0, n);
  }
}
