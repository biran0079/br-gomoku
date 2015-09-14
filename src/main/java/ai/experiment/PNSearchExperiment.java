package ai.experiment;

import ai.candidatemoveselector.CandidateMoveSelectorUtil;
import ai.candidatemoveselector.CandidateMovesSelector;
import ai.candidatemoveselector.CandidateMovesSelectorBuilder;
import ai.candidatemoveselector.ThreatCandidateMoveSelectorUtil;
import ai.proofnumber.PNSCandidateSelector;
import ai.proofnumber.ProofNumberSearch;
import ai.proofnumber.TBSEvaluator;
import ai.threatbasedsearch.ThreatBasedSearch;
import common.StoneType;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassUtil;
import common.pattern.Threat;

/**
 * Created by biran on 9/3/2015.
 */
public class PNSearchExperiment {

  public static void main(String[] args) {
    ThreatBasedSearch tbs = new ThreatBasedSearch();
    CandidateMovesSelector<Threat> threatCandidateMovesSelector =
        CandidateMovesSelectorBuilder.<Threat>newBuilder()
            .add(CandidateMoveSelectorUtil::centerIfEmptyBoard)
            .add(ThreatCandidateMoveSelectorUtil::anyOffendFiveThreat)
            .add(ThreatCandidateMoveSelectorUtil::anyDefendFiveThreat)
            .add(ThreatCandidateMoveSelectorUtil::anyOffendStraitFour)
            .add(ThreatCandidateMoveSelectorUtil::allDefendStraitFour)
            .add(new PNSCandidateSelector(tbs))
            .build();
    ProofNumberSearch<Threat> pns = new ProofNumberSearch<>(
        threatCandidateMovesSelector,
        new TBSEvaluator(tbs));
    System.err.println(
        pns.search(
            BoardFactories.FOR_THREAT.fromGameBoard(
                BoardClassUtil.fromString("_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "______O________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n")),
            StoneType.BLACK));
  }
}
