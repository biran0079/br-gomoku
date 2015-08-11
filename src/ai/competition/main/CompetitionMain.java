package ai.competition.main;

import ai.AI;
import ai.competition.BoardClassSamples;
import ai.competition.Competition;
import ai.minmax.MinMaxSearch;
import common.Patterns;
import common.StoneType;

/**
 * Entry point of a competition.
 */
public class CompetitionMain {

  public static void main(String[] args) {
    AI ai1 = MinMaxSearch.newBuilder()
        .setName("d3_new-eval")
        .setMaxDepth(3)
        .setEvaluator(
            (boardClass, blackMoveNext) -> {
              if (blackMoveNext) {
                if (boardClass.matchesAny(Patterns.BLACK_STRAIT_FOUR)
                    || boardClass.matchesAny(Patterns.BLACK_THREE)) {
                  return 10;
                }
                return 0;
              } else {
                if (boardClass.matchesAny(Patterns.WHITE_STRAIT_FOUR)
                    || boardClass.matchesAny(Patterns.WHITE_THREE)) {
                  return -10;
                }
                return 0;
              }
            })
        .build();
    AI ai2 = MinMaxSearch.newBuilder()
        .setName("d3")
        .setMaxDepth(3)
        .build();
    new Competition(BoardClassSamples.INITIAL_10).compete(ai1, ai2, StoneType.BLACK);
  }
}
