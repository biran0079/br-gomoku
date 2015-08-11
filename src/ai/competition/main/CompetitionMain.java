package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import ai.minmax.SmartTransitionTable;
import common.StoneType;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d4-smart-transition")
        .withTransitionTableFactory(() -> new SmartTransitionTable())
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d4")
        .withMaxDepth(4)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
