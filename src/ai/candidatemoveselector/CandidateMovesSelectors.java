package ai.candidatemoveselector;

/**
 * Commonly used candidate moves selector.
 */
public class CandidateMovesSelectors {

  public static final CandidateMovesSelector FOR_TEST = new CandidateMovesSelectorBuilder()
      .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
      .add(CandidateMoveSelectorUtil::minOffendFour)
      .add(CandidateMoveSelectorUtil::minDefendFour)
      .add(CandidateMoveSelectorUtil::allOffendThree)
      .add(CandidateMoveSelectorUtil::mostFrequentDefendThree)
      .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
      .build();

  public static CandidateMovesSelector DEFAULT = new CandidateMovesSelectorBuilder()
      .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
      .add(CandidateMoveSelectorUtil::allOffendFour)
      .add(CandidateMoveSelectorUtil::allDefendFour)
      .add(CandidateMoveSelectorUtil::allOffendThree)
      .add(CandidateMoveSelectorUtil::defendThreeIntersections)
      .add(CandidateMoveSelectorUtil::allDefendThree)
      .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
      .build();
}
