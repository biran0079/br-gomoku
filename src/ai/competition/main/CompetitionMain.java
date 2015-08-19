package ai.competition.main;

import common.StoneType;
import common.boardclass.testing.BoardClassSamples;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

/**
 * Entry point of a competition.
 */
class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d3")
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d3-killer")
        .useKillerHeuristic()
        .withMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).competeSequential(ai1, ai2, StoneType.BLACK);
  }
}
