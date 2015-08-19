package ai.experiment;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import com.google.common.collect.Lists;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;

import java.util.Collection;

/**
 * Experiment that compares performance of two AIs.
 */
class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4")
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4-killer")
        .useKillerHeuristic()
        .withMaxDepth(4)
        .build();

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
