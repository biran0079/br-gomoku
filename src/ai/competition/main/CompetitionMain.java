package ai.competition.main;

import common.StoneType;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassSamples;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d4")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d5")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(5)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).competeSequential(ai1, ai2, StoneType.BLACK);
  }
}
