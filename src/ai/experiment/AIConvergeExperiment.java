package ai.experiment;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import common.StoneType;
import common.boardclass.BoardClass;
import common.boardclass.BoardClassGenerator;
import common.pattern.Pattern;
import common.pattern.PatternType;

import java.util.Collection;

import ai.AI;
import ai.candidatemoveselector.CandidateMovesSelectors;
import ai.competition.Competition;
import ai.minmax.EnhancedEvaluator;
import ai.minmax.Evaluator;
import ai.minmax.MinMaxSearch;

/**
 * Experiment that compares performance of two AIs.
 */
class AIConvergeExperiment {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4")
        .withEvaluator(new EnhancedEvaluator())
        .withMaxDepth(4)
        .build();
    AI ai2 = MinMaxSearch.defaultBuilderForPattern()
        .withName("d4-killer")
        .withEvaluator(new EnhancedEvaluator())
        .useKillerHeuristic()
        .withMaxDepth(4)
        .build();

    Collection<BoardClass<?>> games = Lists.newArrayList(
        new BoardClassGenerator().generateBoardWithStones(4));

    new Competition(games).competeParallel(ai1, ai2, StoneType.BLACK);
  }
}
