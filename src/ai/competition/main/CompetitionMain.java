package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

import common.StoneType;
import common.boardclass.BoardClassUtil;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d4")
        .withBoardClassFactory(BoardClassUtil.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d5")
        .withBoardClassFactory(BoardClassUtil.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(5)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).competeSequential(ai1, ai2, StoneType.BLACK);
  }
}
