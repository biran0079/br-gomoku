package ai.candidatemoveselector;

import common.pattern.Pattern;
import common.pattern.Threat;

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

  public static final CandidateMovesSelector<Pattern> FOR_PATTERN =
      CandidateMovesSelectorBuilder.newBuilder()
          .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
          .add(CandidateMoveSelectorUtil::anyOffendFour)
          .add(CandidateMoveSelectorUtil::anyDefendFour)
          .add(CandidateMoveSelectorUtil::allOffendThree)
          .add(CandidateMoveSelectorUtil::defendThreeIntersections)
          .add(CandidateMoveSelectorUtil::allDefendThree)
          .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
          .build();

  public static final CandidateMovesSelector<Threat> FOR_THREAT =
      CandidateMovesSelectorBuilder.<Threat>newBuilder()
          .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
          .add(ThreatCandidateMoveSelectorUtil::anyOffendFiveThreat)
          .add(ThreatCandidateMoveSelectorUtil::anyDefendFiveThreat)
          .add(ThreatCandidateMoveSelectorUtil::anyOffendStraitFour)
          .add(ThreatCandidateMoveSelectorUtil::allDefendStraitFour)
          .add(ThreatCandidateMoveSelectorUtil::allOffendAndDefendFourAndThree)
          .add(CandidateMoveSelectorUtil.neighbour(Integer.MAX_VALUE))
          .build();
}
