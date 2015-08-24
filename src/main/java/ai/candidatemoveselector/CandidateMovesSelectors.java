package ai.candidatemoveselector;

import common.pattern.Pattern;

/**
 * Commonly used candidate moves selector.
 */
public class CandidateMovesSelectors {

  public static final CandidateMovesSelector<Pattern> FOR_TEST =
      CandidateMovesSelectorBuilder.newBuilder()
          .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
          .add(CandidateMoveSelectorUtil::minOffendFour)
          .add(CandidateMoveSelectorUtil::minDefendFour)
          .add(CandidateMoveSelectorUtil::allOffendThree)
          .add(CandidateMoveSelectorUtil::mostFrequentDefendThree)
          .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
          .build();

  public static final CandidateMovesSelector<Pattern> DEFAULT =
      CandidateMovesSelectorBuilder.newBuilder()
          .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
          .add(CandidateMoveSelectorUtil::anyOffendFour)
          .add(CandidateMoveSelectorUtil::anyDefendFour)
          .add(CandidateMoveSelectorUtil::allOffendThree)
          .add(CandidateMoveSelectorUtil::defendThreeIntersections)
          .add(CandidateMoveSelectorUtil::allDefendThree)
          .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
          .build();
}
