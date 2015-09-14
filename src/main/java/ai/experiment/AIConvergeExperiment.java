package ai.experiment;

import ai.AI;
import ai.candidatemoveselector.CandidateMoveSelectorUtil;
import ai.candidatemoveselector.CandidateMovesSelectorBuilder;
import ai.candidatemoveselector.ThreatCandidateMoveSelectorUtil;
import ai.competition.Competition;
import ai.evaluator.Evaluator;
import ai.evaluator.SimpleThreatEvaluator;
import ai.minmax.MinMaxSearch;
import ai.minmax.transitiontable.SmartCompleteTransitionTable;

import ai.threatbasedsearch.AggresiveThreatSearchAI;
import ai.threatbasedsearch.DatabaseManager;
import ai.threatbasedsearch.ThreatBasedSearch;
import ai.threatbasedsearch.ThreatBasedSearchAI;
import com.google.common.collect.Lists;
import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;
import common.pattern.Threat;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Experiment that compares performance of two AIs.
 */
class AIConvergeExperiment {

  public static void main(String[] args) throws SQLException {
    AI ai1 = new AggresiveThreatSearchAI(
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
            .build(),
        new ThreatBasedSearch(DatabaseManager.fileDb()));

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
