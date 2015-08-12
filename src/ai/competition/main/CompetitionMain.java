package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

import ai.minmax.transitiontable.SmartTransitionTable;
import common.StoneType;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
