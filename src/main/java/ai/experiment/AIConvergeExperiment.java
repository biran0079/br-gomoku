package ai.experiment;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.competition.Competition;
import ai.evaluator.SimpleThreatEvaluator;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartTransitionTable;

import ai.threatbasedsearch.ThreatBasedAI;
import com.google.common.collect.Lists;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;
import common.boardclass.BoardFactories;
import common.pattern.Threat;

import java.util.Collection;

/**
 * Experiment that compares performance of two AIs.
 */
class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = new ThreatBasedAI(
        MinMaxSearch.defaultBuilderForThreat()
            .withName("d4-minmax")
            .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
            .withTransitionTableFactory(SmartTransitionTable::new)
            .useKillerHeuristic()
            .withMaxDepth(4)
            .build(),
        3);
    AI ai2 = new ThreatBasedAI(
        MinMaxSearch.defaultBuilderForThreat()
            .withName("d4-minmax")
            .withAlgorithm(MinMaxSearch.Algorithm.MINMAX)
            .withTransitionTableFactory(SmartTransitionTable::new)
            .useKillerHeuristic()
            .withMaxDepth(4)
            .build(),
        4);

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
