package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.HashEvaluationTable;
import ai.minmax.MinMaxSearch;

import common.StoneType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d3-hash-eval")
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
