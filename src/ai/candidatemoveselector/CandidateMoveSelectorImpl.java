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
      return singleton(Position.of(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2));
/*
    Optional<Position> candidate = CandidateMoveSelectorUtil.minOffendFour(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    candidate = CandidateMoveSelectorUtil.minDefendFour(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    Collection<Position> candidateList = CandidateMoveSelectorUtil.allOffendThree(
        boardClass, stoneType);
    if (!candidateList.isEmpty()) {
      return candidateList;
    }
    candidate = CandidateMoveSelectorUtil.mostFrequentDefendThree(boardClass, stoneType);
    if (candidate.isPresent()) {
      return singleton(candidate.get());
    }
    return randomSample(CandidateMoveSelectorUtil.getNeighboringMoves(boardClass), randomSampleBranchCandidate);
*/
  }
}
