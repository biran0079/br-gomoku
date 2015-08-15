package ai.experiment;

import com.google.common.collect.Lists;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;
import common.boardclass.BoardFactories;
import common.boardclass.testing.BoardClassSamples;

import java.util.Collection;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

/**
 * Experiment
 */
public class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d4")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(4)
        .build();
    Collection<BoardClass> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
