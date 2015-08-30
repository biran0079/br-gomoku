package ai.experiment;

import ai.AI;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartCompleteTransitionTable;

import ai.threatbasedsearch.CompeleteThreatSearchAI;
import ai.threatbasedsearch.ThreatBasedSearchAI;
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
    AI ai1 = new CompeleteThreatSearchAI(
        MinMaxSearch.defaultBuilderForThreat()
            .withName("d4-minmax")
            .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
            .withTransitionTableFactory(SmartCompleteTransitionTable::new)
            .useKillerHeuristic()
            .withMaxDepth(4)
            .build(),
        4);
    AI ai2 = new ThreatBasedSearchAI(
        MinMaxSearch.defaultBuilderForThreat()
            .withName("d4-minmax")
            .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
            .withTransitionTableFactory(SmartCompleteTransitionTable::new)
            .useKillerHeuristic()
            .withMaxDepth(4)
            .build());

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
