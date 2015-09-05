package ai.experiment;

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
    ProofNumberSearch<Threat> pns = new ProofNumberSearch<>(
        new PNSCandidateSelector(tbs),
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
                    + "______X_XO_____\n"
                    + "______XO_______\n"
                    + "______X_O______\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n"
                    + "_______________\n")),
            StoneType.BLACK));
  }
}
