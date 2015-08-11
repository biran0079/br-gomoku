package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import common.StoneType;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .setName("d4")
        .setMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .setName("d3")
        .setMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
