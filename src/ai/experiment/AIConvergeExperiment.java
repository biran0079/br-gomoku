package ai.experiment;

import com.google.common.collect.Lists;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;
import common.boardclass.BoardFactories;

import java.util.Collection;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

/**
 * Experiment that compares performance of two AIs.
 */
public class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .withName("d3")
        .withBoardClassFactory(BoardFactories.PRE_COMPUTE_MATCHING_FACTORY)
        .withMaxDepth(3)
        .build();

    Collection<BoardClass> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
