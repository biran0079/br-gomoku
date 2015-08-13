package ai.experiment;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartTransitionTable;
import ai.minmax.transitiontable.TransitionTableImpl;
import ai.minmax.transitiontable.TransitionTableWithStats;
import common.StoneType;

/**
 * Compare cache hit rate between smart and default transition tables.
 */
public class TransitionTableHitRateExperimentMain {

  public static void main(String[] args) {
    TransitionTableWithStats.Stats smartStats = new TransitionTableWithStats.Stats("smart");
    TransitionTableWithStats.Stats defaultStats = new TransitionTableWithStats.Stats("default");

    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d4-smart-transition")
        .withTransitionTableFactory(() -> new TransitionTableWithStats(
            new SmartTransitionTable(), smartStats))
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d4")
        .withTransitionTableFactory(() -> new TransitionTableWithStats(
            new TransitionTableImpl(), defaultStats))
        .withMaxDepth(3)
        .build();
      new Competition(BoardClassSamples.INITIAL_10).competeParallel(ai1, ai2, StoneType.BLACK);

    System.err.println(smartStats);
    System.err.println(defaultStats);
  }
}
