package ai.experiment;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartTransitionTable;

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
        .withName("d4-minmax-kill")
        .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
        .withTransitionTableFactory(SmartTransitionTable::new)
        .withMaxDepth(4)
        .useKillerHeuristic()
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4-negamax-kill")
        .withAlgorithm(MinMaxSearch.Algorithm.NEGAMAX)
        .withTransitionTableFactory(SmartTransitionTable::new)
        .withMaxDepth(4)
        .useKillerHeuristic()
        .build();

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
