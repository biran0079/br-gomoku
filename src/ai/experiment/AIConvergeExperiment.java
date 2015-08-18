package ai.experiment;

import com.google.common.collect.Lists;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;

import java.util.Collection;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;

/**
 * Experiment that compares performance of two AIs.
 */
class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d3-test")
        .withCandidateMoveSelector(CandidateMovesSelectors.FOR_TEST)
        .withMaxDepth(3)
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d3")
        .withMaxDepth(3)
        .build();

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
