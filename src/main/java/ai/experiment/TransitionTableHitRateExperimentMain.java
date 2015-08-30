package ai.experiment;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxNode;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartCompleteTransitionTable;
import ai.minmax.transitiontable.CompleteTransitionTable;
import ai.minmax.transitiontable.TransitionTableWithStats;
import common.StoneType;
import common.boardclass.testing.BoardClassSamples;

/**
 * Compare cache hit rate between smart and default transition tables.
 */
class TransitionTableHitRateExperimentMain {

  public static void main(String[] args) {
    TransitionTableWithStats.Stats smartStats = new TransitionTableWithStats.Stats("smart");
    TransitionTableWithStats.Stats defaultStats = new TransitionTableWithStats.Stats("default");

    AI ai1 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4-smart-transition")
        .withTransitionTableFactory(
            () -> new TransitionTableWithStats<MinMaxNode>(
                new SmartCompleteTransitionTable<>(), smartStats))
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4")
        .withTransitionTableFactory(
            () -> new TransitionTableWithStats<MinMaxNode>(
                new CompleteTransitionTable<>(), defaultStats))
        .withMaxDepth(3)
        .build();
      new Competition(BoardClassSamples.INITIAL_10).competeParallel(ai1, ai2, StoneType.BLACK);

    System.err.println(smartStats);
    System.err.println(defaultStats);
  }
}
